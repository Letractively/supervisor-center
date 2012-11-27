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

import utils.ProcessInfo;
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
public class Control extends Controller {

	public static play.mvc.Result index() {
		if (Secured.hasReadAccess()) {
			return ok(indexcontrol.render());
		} else {
			return unauthorized(errorPage.render("Attention!","Accès non authorisé"));
		}
	}

	public static play.mvc.Result processlist() {
		if (Secured.hasReadAccess()) {
			
			List<Supervisor> supervisors = Supervisor.find.where()
					.order("group.name asc, name asc").findList();
			List<utils.SupervisorProcess> processs = new ArrayList<utils.SupervisorProcess>();
			
			for (Supervisor supervisor : supervisors) {
				new ArrayList<ProcessInfo>();
				String host = supervisor.host;
				int port = supervisor.port;
				
				utils.SupervisordClient supClient = null;
				try {
					if (supervisor.authentification != null) {
						if (supervisor.authentification) {
							String login = supervisor.login;
							String password = supervisor.password;
							supClient = new utils.SupervisordClient(host, port, login, password);
						} else {
							supClient = new utils.SupervisordClient(host, port);
						}
					} else {
						supClient = new utils.SupervisordClient(host, port);
					}
					List<ProcessInfo> processInfos = supClient.getAllProcessInfo();
					for (ProcessInfo processInfo : processInfos) {
						processs.add(new utils.SupervisorProcess(processInfo, supervisor));
					}
				} catch (Exception ex) {
					Logger.warn("Error loading processes", ex);
				}
			}

			return ok(processlist.render(processs));
		} else {
			return unauthorized(errorPage.render("Attention!","Accès non authorisé"));
		}
	}
}
