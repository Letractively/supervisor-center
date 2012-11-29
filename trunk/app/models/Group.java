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
 * Group entity managed by Ebean
 * 
 * @author cso990
 */
@Entity
@Table(name = "groups")
public class Group extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	public Long id;

	@Required
	@Constraints.MaxLength(50)
	@Basic(optional = false)
	public String name;

	@Required
	@Constraints.MaxLength(255)
	@Basic(optional = false)
	public String description;

	@OneToMany(mappedBy = "group")
	public List<Supervisor> supervisors;

	public static Model.Finder<Long, Group> find = new Model.Finder(Long.class,
			Group.class);

	/**
	 * Retrieve all groups.
	 */
	public static List<Group> findAll() {
		return find.all();
	}

	public static Page<Group> page(int page, int pageSize, String sortBy,
			String order) {
		Page<Group> currentpage = null;
		com.avaje.ebean.Query<Group> query = Ebean.createQuery(Group.class);
		query.fetch("supervisors");
		query.orderBy(sortBy + " " + order);

		currentpage = query.findPagingList(pageSize).getPage(page);

		return currentpage;
	}

	public static List<Group> findOrdered(String sortBy, String order) {
		List<Group> groups = null;
		com.avaje.ebean.Query<Group> query = Ebean.createQuery(Group.class);
		query.orderBy(sortBy + " " + order);

		groups = query.findList();

		return groups;
	}

	public static Map<String, String> options() {
		LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
		options.put("", "None");
		for (Group g : Group.find.where().orderBy("name").findList()) {
			options.put(g.id.toString(), g.name);
		}
		return options;
	}
}
