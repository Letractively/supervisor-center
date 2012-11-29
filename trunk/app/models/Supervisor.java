/*
 * Copyright 2012 Cédric Sougné
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package models;

import java.util.*;

import javax.persistence.*;
import play.db.ebean.*;
import play.data.validation.*;
import play.data.validation.Constraints.Required;

import com.avaje.ebean.*;

/**
 * Supervisor entity managed by Ebean
 * 
 * @author cso990
 */
@Entity
@Table(name = "supervisors")
public class Supervisor extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	public Long id;

	@Required
	@Basic(optional = false)
	public String name;

	@Required
	@Basic(optional = false)
	public String host;

	@Required
	@Basic(optional = false)
	public int port;

	public Boolean authentification;

	@Constraints.MaxLength(50)
	public String login;

	@Constraints.MaxLength(50)
	public String password;

	@Constraints.MaxLength(50)
	public String place;

	@Constraints.MaxLength(255)
	public String comment;
	
	@ManyToOne
	public Group group;

	public static Model.Finder<Long, Supervisor> find = new Model.Finder(
			Long.class, Supervisor.class);

	/**
	 * Retrieve all supervisors.
	 */
	public static List<Supervisor> findAll() {
		return find.all();
	}

	public static Page<Supervisor> page(int page, int pageSize, String sortBy,
			String order) {
		Page<Supervisor> currentpage = null;
		com.avaje.ebean.Query<Supervisor> query = Ebean
				.createQuery(Supervisor.class);
		query.fetch("group");
		query.orderBy(sortBy + " " + order);

		currentpage = query.findPagingList(pageSize).getPage(page);

		return currentpage;
	}

	public static List<Supervisor> findOrdered(String sortBy, String order) {
		List<Supervisor> supervisors = null;
		com.avaje.ebean.Query<Supervisor> query = Ebean
				.createQuery(Supervisor.class);
		query.fetch("group");
		query.orderBy(sortBy + " " + order);

		supervisors = query.findList();

		return supervisors;
	}
}
