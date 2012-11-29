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

import com.avaje.ebean.Page;
import models.*;
import play.mvc.*;
import play.data.*;
import play.mvc.Controller;
import views.html.*;

import play.i18n.Messages;

/**
 * <p>Admin controller serving administration pages.<p>
 * 
 * @author Cédric Sougné
 *
 */
@Security.Authenticated(Secured.class)
public class Admin extends Controller {

	/**
	 * <p>Delete referenced group the return to group list</p>
	 * 
	 * @param id
	 * @return Result
	 */
	public static Result deletegroup(Long id) {
		if (Secured.hasWriteAccess()) {
			try {
				Group.find.ref(id).delete();
				flash("success", "Le groupe a été supprimée");
			} catch (Exception e) {
				flash("error", "Le groupe n'a pas été supprimée");
			}
			return redirect(routes.Admin.groups(0, "name", "asc"));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Delete referenced supervisor daemon the return to supervisor list</p>
	 * 
	 * @param id
	 * @return Result
	 */
	public static Result deletesupervisor(Long id) {
		if (Secured.hasWriteAccess()) {
			try {
				Supervisor.find.ref(id).delete();
				flash("success", "Le supervisor a été supprimée");
			} catch (Exception e) {
				flash("error", "Le supervisor n'a pas été supprimée");
			}
			return redirect(routes.Admin.supervisors(0, "name", "asc"));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Delete referenced user the return to user list</p>
	 * 
	 * @param id
	 * @return Result
	 */
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
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Show edit form for referenced group</p>
	 * 
	 * @param id
	 * @return Result
	 */
	public static Result editgroup(Long id) {
		if (Secured.hasWriteAccess()) {
			Form<Group> groupeForm = form(Group.class).fill(
					Group.find.where().idEq(id).findUnique());
			return ok(editGroupeForm.render(id, groupeForm));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Show edit form for referenced user login</p>
	 * 
	 * @param login
	 * @return Result
	 */
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
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Show edit form for referenced supervisor</p>
	 * 
	 * @param id
	 * @return Result
	 */
	public static Result editsupervisor(Long id) {
		if (Secured.hasWriteAccess()) {
			Form<Supervisor> supervisorForm = form(Supervisor.class).fill(
					Supervisor.find.where().idEq(id).findUnique());
			return ok(editSupervisorForm.render(id, supervisorForm));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Show edit form for referenced user</p>
	 * 
	 * @param id
	 * @return Result
	 */
	public static Result edituser(Long id) {
		if (Secured.hasAdminAccess()) {
			Form<User> userForm = form(User.class).fill(
					User.find.where().idEq(id).findUnique());
			return ok(editUserForm.render(id, userForm));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Show group list and action links to create, modify... with some sorting and filtering</p>
	 * 
	 * @param page
	 * @param sortBy
	 * @param order
	 * @return Result
	 */
	public static play.mvc.Result groups(int page, String sortBy, String order) {
		if (Secured.hasReadAccess()) {
			if (page == -1) {
				String sessionval = session("groups.page");
				if (sessionval != null) {
					page = Integer.parseInt(sessionval);
				} else {
					page = 0;
				}
			} else if (page == -2) {
				session().remove("groups.page");
				page = 0;
			} else {
				session("groups.page", String.valueOf(page));
			}

			if (sortBy == "") {
				String sessionval = session("groups.sortBy");
				if (sessionval != null) {
					sortBy = sessionval;
				} else {
					sortBy = "name";
				}
			} else if (sortBy == ".") {
				session().remove("groups.sortBy");
				sortBy = "";
			} else {
				session("groups.sortBy", sortBy);
			}

			if (order == "") {
				String sessionval = session("groups.order");
				if (sessionval != null) {
					order = sessionval;
				}
			} else if (order == ".") {
				session().remove("groups.order");
				order = "";
			} else {
				session("groups.order", order);
			}
			Page<Group> groupsPage = Group.page(page, 25, sortBy, order);
			if (page >= groupsPage.getTotalPageCount()) {
				page = 0;
				session().remove("groups.page");
				groupsPage = Group.page(0, 25, sortBy, order);
			}

			return ok(groups.render(groupsPage, sortBy, order));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}


	/**
	 * <p>Main default access page</p>
	 * 
	 * @return Result
	 */
	public static play.mvc.Result index() {
		if (Secured.hasReadAccess()) {
			return redirect("/admin/groups");
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Show create form for a new group</p>
	 * 
	 * @return Result
	 */
	public static Result newgroup() {
		if (Secured.hasWriteAccess()) {
			Form<Group> groupeForm = form(Group.class);
			return ok(newGroupeForm.render(groupeForm));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Show create form for a new supervisor</p>
	 * 
	 * @return Result
	 */
	public static Result newsupervisor() {
		if (Secured.hasWriteAccess()) {
			Form<Supervisor> supervisorForm = form(Supervisor.class);
			return ok(newSupervisorForm.render(supervisorForm));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Show create form for a new user</p>
	 * 
	 * @return Result
	 */
	public static Result newuser() {
		if (Secured.hasAdminAccess()) {
			Form<User> userForm = form(User.class);
			return ok(newUserForm.render(userForm));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Save new group form datas to database then return to group list</p>
	 * 
	 * @return Result
	 */
	public static Result savegroup() {
		if (Secured.hasWriteAccess()) {
			Form<Group> groupeForm = form(Group.class).bindFromRequest();
			if (groupeForm.hasErrors()) {
				return badRequest(newGroupeForm.render(groupeForm));
			}
			try {
				groupeForm.get().save();
				flash("success", "Group " + groupeForm.get().name
						+ " a été créé");
			} catch (Exception e) {
				flash("error", "Group " + groupeForm.get().name + " non créé");
			}
			return redirect(routes.Admin.groups(0, "name", "asc"));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Save new supervisor form datas to database then return to supervisor list</p>
	 * 
	 * @return Result
	 */
	public static Result savesupervisor() {
		if (Secured.hasWriteAccess()) {
			Form<Supervisor> supervisorForm = form(Supervisor.class)
					.bindFromRequest();
			if (supervisorForm.hasErrors()) {
				return badRequest(newSupervisorForm.render(supervisorForm));
			}
			try {
				supervisorForm.get().save();
				flash("success", "Supervisor " + supervisorForm.get().name
						+ " a été créé");
			} catch (Exception e) {
				flash("error", "Supervisor " + supervisorForm.get().name
						+ " non créé");
			}
			return redirect(routes.Admin.supervisors(0, "name", "asc"));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Save new user form datas to database then return to user list</p>
	 * 
	 * @return Result
	 */
	public static Result saveuser() {
		if (Secured.hasAdminAccess()) {
			Form<User> userForm = form(User.class).bindFromRequest();
			if (userForm.hasErrors()) {
				return badRequest(newUserForm.render(userForm));
			}
			try {
				userForm.get().save();
				flash("success", "L'utilisateur " + userForm.get().name
						+ " a été créé");
			} catch (Exception e) {
				flash("error", "L'utilisateur " + userForm.get().name
						+ " n'a pas été créé");
			}
			return redirect(routes.Admin.users(0, "name", "asc"));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Show supervisor list and action links to create, modify... with some sorting and filtering</p>
	 * 
	 * @param page
	 * @param sortBy
	 * @param order
	 * @return Result
	 */
	public static play.mvc.Result supervisors(int page, String sortBy,
			String order) {
		if (Secured.hasReadAccess()) {
			if (page == -1) {
				String sessionval = session("supervisors.page");
				if (sessionval != null) {
					page = Integer.parseInt(sessionval);
				} else {
					page = 0;
				}
			} else if (page == -2) {
				session().remove("supervisors.page");
				page = 0;
			} else {
				session("supervisors.page", String.valueOf(page));
			}

			if (sortBy == "") {
				String sessionval = session("supervisors.sortBy");
				if (sessionval != null) {
					sortBy = sessionval;
				} else {
					sortBy = "name";
				}
			} else if (sortBy == ".") {
				session().remove("supervisors.sortBy");
				sortBy = "";
			} else {
				session("supervisors.sortBy", sortBy);
			}

			if (order == "") {
				String sessionval = session("supervisors.order");
				if (sessionval != null) {
					order = sessionval;
				}
			} else if (order == ".") {
				session().remove("supervisors.order");
				order = "";
			} else {
				session("supervisors.order", order);
			}
			Page<Supervisor> supervisorsPage = Supervisor.page(page, 25,
					sortBy, order);
			if (page >= supervisorsPage.getTotalPageCount()) {
				page = 0;
				session().remove("supervisors.page");
				supervisorsPage = Supervisor.page(0, 25, sortBy, order);
			}

			return ok(supervisors.render(supervisorsPage, sortBy, order));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Update referenced group from form datas into database then return to group list</p>
	 * 
	 * @param id
	 * @return Result
	 */
	public static Result updategroup(Long id) {
		if (Secured.hasWriteAccess()) {
			Form<Group> groupeForm = form(Group.class).bindFromRequest();
			if (groupeForm.hasErrors()) {
				return badRequest(editGroupeForm.render(id, groupeForm));
			}
			try {
				groupeForm.get().update(id);
				flash("success", "Le groupe " + groupeForm.get().name
						+ " a été modifié");
			} catch (Exception e) {
				flash("error", "Le groupe " + groupeForm.get().name
						+ " n'a pas été modifié");
			}
			return redirect(routes.Admin.groups(0, "name", "asc"));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Update referenced supervisor from form datas into database then return to supervisor list</p>
	 * 
	 * @param id
	 * @return Result
	 */
	public static Result updatesupervisor(Long id) {
		if (Secured.hasWriteAccess()) {
			Form<Supervisor> supervisorForm = form(Supervisor.class)
					.bindFromRequest();
			if (supervisorForm.hasErrors()) {
				return badRequest(editSupervisorForm.render(id, supervisorForm));
			}
			try {
				supervisorForm.get().update(id);
				flash("success", "Le supervisor " + supervisorForm.get().name
						+ " a été modifié");
			} catch (Exception e) {
				flash("error", "Le supervisor " + supervisorForm.get().name
						+ " n'a pas été modifié");
			}
			return redirect(routes.Admin.supervisors(0, "name", "asc"));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Update referenced user from form datas into database then return to user list</p>
	 * 
	 * @param id
	 * @return Result
	 */
	public static Result updateuser(Long id) {
		if (Secured.hasReadAccess()) {
			Form<User> userForm = form(User.class).bindFromRequest();
			if (userForm.hasErrors()) {
				return badRequest(editUserForm.render(id, userForm));
			}
			try {
				userForm.get().update(id);
				flash("success", "L'utilisateur " + userForm.get().name
						+ " a été modifiée");
			} catch (Exception e) {
				flash("error", "L'utilisateur " + userForm.get().name
						+ " n'a pas été modifiée");
			}
			return redirect(routes.Admin.users(0, "name", "asc"));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}

	/**
	 * <p>Show user list and action links to create, modify... with some sorting and filtering</p>
	 * 
	 * @param page
	 * @param sortBy
	 * @param order
	 * @return Result
	 */
	public static play.mvc.Result users(int page, String sortBy, String order) {
		if (Secured.hasAdminAccess()) {
			if (page == -1) {
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
				session("users.page", String.valueOf(page));
			}

			if (sortBy == "") {
				String sessionval = session("users.sortBy");
				if (sessionval != null) {
					sortBy = sessionval;
				} else {
					sortBy = "name";
				}
			} else if (sortBy == ".") {
				session().remove("users.sortBy");
				sortBy = "";
			} else {
				session("users.sortBy", sortBy);
			}

			if (order == "") {
				String sessionval = session("users.order");
				if (sessionval != null) {
					order = sessionval;
				}
			} else if (order == ".") {
				session().remove("users.order");
				order = "";
			} else {
				session("users.order", order);
			}
			Page<User> usersPage = User.page(page, 25, sortBy, order);
			if (page >= usersPage.getTotalPageCount()) {
				page = 0;
				session().remove("users.page");
				usersPage = User.page(0, 25, sortBy, order);
			}

			return ok(users.render(usersPage, sortBy, order));
		} else {
			return unauthorized(errorPage.render(Messages.get("notice.warning")
					+ "!", Messages.get("authentification.unauthorizedaccess")));
		}
	}
}
