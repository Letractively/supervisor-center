package models;

import java.util.*;

import javax.persistence.*;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import play.Play;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.data.validation.Constraints.Required;
import play.api.i18n.Messages;

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
	@Column(name = "id_user")
	public Long id;

	@Required
	@Basic(optional = false)
	@Constraints.MaxLength(25)
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

	// -- Queries

	public static Model.Finder<Long, User> find = new Model.Finder(Long.class,
			User.class);

	/**
	 * Retrieve all users.
	 */
	public static List<User> findAll() {
		return find.all();
	}

	/**
	 * Retrieve a User from email.
	 */
	public static User findByLogin(String login) {
		return find.where().eq("login", login).findUnique();
	}

	/**
	 * Authenticate a User.
	 */
	public static User authenticate(String login, String password) {
			User appUser = find.where().eq("login", login).eq("password", password).findUnique();

		return appUser;
	}

	// --

	public String toString() {
		return "User(" + login + ")";
	}

	public static Page<User> page(int page, int pageSize, String sortBy,
			String order) {
		Page<User> currentpage = null;
		com.avaje.ebean.Query<User> query = Ebean.createQuery(User.class);
		if (sortBy.compareTo("nom") == 0) {
			query.orderBy(sortBy + " " + order + ",prenom " + order);
		} else {
			query.orderBy(sortBy + " " + order + ",prenom desc");
		}

		currentpage = query.findPagingList(pageSize).getPage(page);

		return currentpage;
	}
}
