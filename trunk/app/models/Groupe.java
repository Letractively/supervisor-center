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
 * Groupe entity managed by Ebean
 * @author cso990
 */
@Entity
@Table(name = "groupes")
public class Groupe extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    public Long id;

    @Required
    @Constraints.MaxLength(25)
    @Basic(optional = false)
    public String name;

    @Required
    @Constraints.MaxLength(255)
    @Basic(optional = false)
    public String description;
    
    @ManyToMany
    public List<Supervisor> supervisors;
    

    // -- Queries
    
    public static Model.Finder<Long,Groupe> find = new Model.Finder(Long.class, Groupe.class);
    
    /**
     * Retrieve all groups.
     */
    public static List<Groupe> findAll() {
        return find.all();
    }
    
	public static Page<Groupe> page(int page, int pageSize, String sortBy, String order) {
		Page<Groupe> currentpage = null;
		com.avaje.ebean.Query<Groupe> query = Ebean.createQuery(Groupe.class);
		query.fetch("supervisors");
		query.orderBy(sortBy + " " + order );

		currentpage = query.findPagingList(pageSize).getPage(page);
		
		return currentpage;
	}
    
	public static List<Groupe> findOrdered(String sortBy, String order) {
		List<Groupe> groupes = null;
		com.avaje.ebean.Query<Groupe> query = Ebean.createQuery(Groupe.class);
		query.orderBy(sortBy + " " + order );

		groupes = query.findList();
		
		return groupes;
	}
	
    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        options.put("", "None");
        for(Groupe g: Groupe.find.where().orderBy("name").findList()) {
            options.put(g.id.toString(), g.name);
        }
        return options;
    }
}
