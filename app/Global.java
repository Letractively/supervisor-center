import com.avaje.ebean.*;

import models.*;
import java.util.*;

import play.*;
import play.libs.*;
import play.api.PlayException.UsefulException;
import play.mvc.*;
import play.mvc.Http.RequestHeader;
import scala.Option;

import static play.mvc.Results.*;

import views.html.*;

public class Global extends GlobalSettings {
	
  @Override
  public void onStart(Application app) {
    Logger.info("Application WebAccis has started");
    InitialData.insert(app);
    //Ebean.getServer(null).getAdminLogging().setDebugGeneratedSql(true);
  }  
  
  @Override
  public void onStop(Application app) {
    Logger.info("Application WebAccis shutdown...");
  }  
  
  @Override
	public Result onHandlerNotFound(RequestHeader request) {
		// TODO Auto-generated method stub
	  return notFound(
			  views.html.warningPage.render("Attention!","Chemin inconnu "+request.path())
	  );
	}
  
  @Override
	public Result onError(RequestHeader request, Throwable t) {
	  return internalServerError(
			  views.html.errorPage.render("Erreur interne!",t.getMessage())
	  );
	}
  
  @Override
	public Result onBadRequest(RequestHeader request, String error) {
	  return internalServerError(
			  views.html.errorPage.render("Erreur interne!","<p>Chemin : "+request.path()+"</p><p>"+error+"</p>")
	  );
	}
	
    
    static class InitialData {
        
        public static void insert(Application app) {
            if(Ebean.find(User.class).findRowCount() == 0) {
                
                Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");

                // Insert users first
                Ebean.save(all.get("users"));
                
            }
        }
        
    }
}
