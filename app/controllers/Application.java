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
package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.*;
import play.mvc.Controller;
import views.html.*;

/**
 * <p>Main application controller serving user welcome and authentification.<p>
 * 
 * @author Cédric Sougné
 *
 */
public class Application extends Controller {

	/**
	 * <p>Main default access page</p>
	 * 
	 * @return Result
	 */
	public static Result index() {
		return redirect(routes.Control.processlist());
	}

	// -- Authentication

	/**
	 * User form binding object for validation and authentication
	 * 
	 * @author cso990
	 *
	 */
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
	 * <p>Handle login form submission.</p>
	 * 
	 * @return index page if login successful
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
			session("version",
					Play.application().configuration().getString("app.version"));
			flash("success", play.i18n.Messages.get("login.success"));
			return redirect(routes.Control.processlist());
		}
	}

	/**
	 * <p>Login page.</p>
	 * 
	 * @return Result
	 */
	public static Result login() {
		return ok(login.render(form(Login.class)));
	}

	/**
	 * <p>Logout and clean the session then redirect to login page.</p>
	 * 
	 * @return Result
	 */
	public static Result logout() {
		session().clear();
		flash("success", play.i18n.Messages.get("logout.success"));
		return redirect(routes.Application.login());
	}

}
