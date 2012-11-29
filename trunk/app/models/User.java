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
 * User entity managed by Ebean
 * 
 * @author cso990
 */
@Entity
@Table(name = "users")
public class User extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	public Long id;

	@Required
	@Basic(optional = false)
	@Constraints.MaxLength(50)
	public String name;

	@Required
	@Constraints.MaxLength(25)
	public String login;
	
	@Required
	@Constraints.MaxLength(25)
	public String password;

	@Required
	@Constraints.MaxLength(25)
	@Basic(optional = false)
	public String role;

	public static Model.Finder<Long, User> find = new Model.Finder(Long.class,
			User.class);

	/**
	 * Retrieve all users.
	 */
	public static List<User> findAll() {
		return find.all();
	}

	// --

	/**
	 * Retrieve a User from login.
	 */
	public static User findByLogin(String login) {
		return find.where().eq("login", login).findUnique();
	}

	@Override
	public String toString() {
		return "User(" + login + ")";
	}

	/**
	 * Authenticate a User.
	 */
	public static User authenticate(String login, String password) {
		User appUser = find.where().eq("login", login).eq("password", password)
				.findUnique();

		return appUser;
	}

	public static Page<User> page(int page, int pageSize, String sortBy,
			String order) {
		Page<User> currentpage = null;
		com.avaje.ebean.Query<User> query = Ebean.createQuery(User.class);
		query.orderBy(sortBy + " " + order);

		currentpage = query.findPagingList(pageSize).getPage(page);

		return currentpage;
	}
}
