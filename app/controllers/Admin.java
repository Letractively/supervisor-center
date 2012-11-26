package controllers;

import akka.util.Duration;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;
import com.avaje.ebean.ValidationException;
import com.avaje.ebean.JoinConfig;

import models.*;
import play.*;
import play.mvc.*;
import play.data.*;
import play.mvc.Controller;
import play.api.Configuration;

import views.html.*;

import static akka.pattern.Patterns.ask;
import play.libs.Akka;
import static play.mvc.Results.async;
import static play.libs.Akka.future;
import play.libs.F.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Security.Authenticated(Secured.class)
public class Admin extends Controller {

	public static play.mvc.Result index() {
		if (Secured.hasReadAccess()) {
			return ok(indexadmin.render());
		} else {
			return unauthorized(errorPage.render("Attention!","Accès non authorisé"));
		}
	}

	public static play.mvc.Result users(int page, String sortBy, String order) {
		if (Secured.hasAdminAccess()) {
			if(page == -1) {
				String sessionval = session("users.page");
				if (sessionval != null) {
					page = Integer.parseInt(sessionval);
				} else {
					page = 0;
				}
			} else if (page == -2) {
				session().remove("users.page");
				page = 0;
			} else {
				session("users.page",String.valueOf(page));
			} 

			if(sortBy == "") {
				String sessionval = session("users.sortBy");
				if (sessionval != null) {
					sortBy = sessionval;
				} else {
					sortBy = "name";
				}
			} else if (sortBy == ".") {
				// efface la session pour mettre vide
				session().remove("users.sortBy");
				sortBy = "";
			} else {
				session("users.sortBy",sortBy);
			} 

			if(order == "") {
				String sessionval = session("users.order");
				if (sessionval != null) {
					order = sessionval;
				}
			} else if (order == ".") {
				// efface la session pour mettre vide
				session().remove("users.order");
				order = "";
			} else {
				session("users.order",order);
			} 
			Page<User> usersPage = User.page(page, 25, sortBy, order);
			if (page >= usersPage.getTotalPageCount()) {
				page = 0;
				session().remove("users.page");
				usersPage = User.page(0, 25, sortBy, order);
			}

			return ok(users.render(usersPage, sortBy, order));
		} else {
			return unauthorized(errorPage.render("Attention!","Accès non authorisé"));
		}
	}

	public static Result saveuser() {
		if (Secured.hasAdminAccess()) {
			Form<User> userForm = form(User.class).bindFromRequest();
			if (userForm.hasErrors()) {
				return badRequest(newUserForm.render(userForm));
			}
			try {
				userForm.get().save();
				flash("success", "L'utilisateur " + userForm.get().name + " a été créé");
			} catch (Exception e) {
				flash("error", "L'utilisateur " + userForm.get().name + " n'a pas été créé");
			}
			return redirect(routes.Admin.users(0, "name", "asc"));
		} else {
			return unauthorized(errorPage.render("Attention!","Accès non authorisé"));
		}
	}

	public static Result newuser() {
		if (Secured.hasAdminAccess()) {
			Form<User> userForm = form(User.class);
			return ok(newUserForm.render(userForm));
		} else {
			return unauthorized(errorPage.render("Attention!","Accès non authorisé"));
		}
	}

	public static Result deleteuser(Long id) {
		if (Secured.hasAdminAccess()) {
			try {
				User.find.ref(id).delete();
				flash("success", "L'utilisateur a été supprimée");
			} catch (Exception ex) {
				flash("error", "L'utilisateur n'a pas été supprimée");
			}
			return redirect(routes.Admin.users(0, "name", "asc"));
		} else {
			return unauthorized(errorPage.render("Attention!","Accès non authorisé"));
		}
	}

	public static Result edituser(Long id) {
		if (Secured.hasAdminAccess()) {
			Form<User> userForm = form(User.class).fill(
					User.find.where().idEq(id).findUnique());
			return ok(editUserForm.render(id, userForm));
		} else {
			return unauthorized(errorPage.render("Attention!","Accès non authorisé"));
		}
	}

	public static Result editmyuser(String login) {
		if (Secured.hasReadAccess()) {
			try {
				User user = User.findByLogin(login);
				if (session("login").compareTo(user.login) == 0) {
					Form<User> userForm = form(User.class).fill(user);
					return ok(editUserForm.render(user.id, userForm));
				} else {
					flash("error", "Accès interdit");
					return index();
				}
			} catch (Exception e) {
				flash("error", "Utilisateur non connu");
				return index();
			}
		} else {
			return unauthorized(errorPage.render("Attention!","Accès non authorisé"));
		}
	}

	public static Result updateuser(Long id) {
		if (Secured.hasReadAccess()) {
			Form<User> userForm = form(User.class).bindFromRequest();
			if (userForm.hasErrors()) {
				return badRequest(editUserForm.render(id, userForm));
			}
			try {
				userForm.get().update(id);
				flash("success", "L'utilisateur " + userForm.get().name + " a été modifiée");
			} catch (Exception e) {
				flash("error", "L'utilisateur " + userForm.get().name + " n'a pas été modifiée");
			}
			return redirect(routes.Admin.users(0, "name", "asc"));
		} else {
			return unauthorized(errorPage.render("Attention!","Accès non authorisé"));
		}
	}

}
