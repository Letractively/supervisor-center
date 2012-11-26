package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.*;
import play.mvc.Controller;
import play.api.Configuration;
import play.api.i18n.Messages;

import views.html.*;

public class Application extends Controller {
  
	public static Result index() {
		return redirect(routes.Control.index());
	}

	// -- Authentication

	public static class Login {

		public String login;
		public String password;

		public String validate() {
			
			if (User.authenticate(login, password) == null) {
				return play.i18n.Messages.get("login.invalid");
			}
			return null;
		}

	}

	/**
	 * Login page.
	 */
	public static Result login() {
				return ok(login.render(form(Login.class)));
	}

	/**
	 * Handle login form submission.
	 */
	public static Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			flash("error", play.i18n.Messages.get("login.invalid"));
			return badRequest(login.render(loginForm));
		} else {
			User appUser = User.findByLogin(loginForm.get().login);
			String appLevel = appUser.role;
			session("login", loginForm.get().login);
			session("level", appLevel);
			session("version",Play.application().configuration().getString("app.version"));
			flash("success", play.i18n.Messages.get("login.success"));
			return redirect(routes.Application.index());
		}
	}

	/**
	 * Logout and clean the session.
	 */
	public static Result logout() {
		session().clear();
		flash("success", play.i18n.Messages.get("logout.success"));
		return redirect(routes.Application.login());
	}
  
}
