import com.avaje.ebean.*;

import models.*;
import java.util.*;

import play.*;
import play.libs.*;
import play.mvc.*;
import play.mvc.Http.RequestHeader;
import static play.mvc.Results.*;

public class Global extends GlobalSettings {

	static class InitialData {

		public static void insert(Application app) {
			if (Ebean.find(User.class).findRowCount() == 0) {

				Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml
						.load("initial-data.yml");

				// Insert users first
				Ebean.save(all.get("users"));

			}
		}

	}

	@Override
	public Result onBadRequest(RequestHeader request, String error) {
		return internalServerError(views.html.errorPage.render(
				"Erreur interne!", "<p>Chemin : " + request.path() + "</p><p>"
						+ error + "</p>"));
	}

	@Override
	public Result onError(RequestHeader request, Throwable t) {
		return internalServerError(views.html.errorPage.render(
				"Erreur interne!", t.getMessage()));
	}

	@Override
	public Result onHandlerNotFound(RequestHeader request) {
		return notFound(views.html.warningPage.render("Attention!",
				"Chemin inconnu " + request.path()));
	}

	@Override
	public void onStart(Application app) {
		Logger.info("Application WebAccis has started");
		InitialData.insert(app);
		// Ebean.getServer(null).getAdminLogging().setDebugGeneratedSql(true);
	}

	@Override
	public void onStop(Application app) {
		Logger.info("Application WebAccis shutdown...");
	}
}
