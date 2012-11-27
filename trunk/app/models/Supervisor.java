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

import com.avaje.ebean.*;

/**
 * Supervisor entity managed by Ebean
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
    
    @Constraints.MaxLength(25)
    public String login;

    @Constraints.MaxLength(25)
    public String password;

    @Constraints.MaxLength(25)
    public String place;
    
    @Constraints.MaxLength(255)
    public String comment;
    
    @ManyToOne
    public Group group;

    
    // -- Queries
    
    public static Model.Finder<Long,Supervisor> find = new Model.Finder(Long.class, Supervisor.class);
    
    /**
     * Retrieve all supervisors.
     */
    public static List<Supervisor> findAll() {
        return find.all();
    }
    
	public static Page<Supervisor> page(int page, int pageSize, String sortBy, String order) {
		Page<Supervisor> currentpage = null;
		com.avaje.ebean.Query<Supervisor> query = Ebean.createQuery(Supervisor.class);
		query.fetch("group");
		query.orderBy(sortBy + " " + order );

		currentpage = query.findPagingList(pageSize).getPage(page);
		
		return currentpage;
	}
    
	public static List<Supervisor> findOrdered(String sortBy, String order) {
		List<Supervisor> supervisors = null;
		com.avaje.ebean.Query<Supervisor> query = Ebean.createQuery(Supervisor.class);
		query.fetch("group");
		query.orderBy(sortBy + " " + order );

		supervisors = query.findList();
		
		return supervisors;
	}
}
