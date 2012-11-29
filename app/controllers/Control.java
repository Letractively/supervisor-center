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
import utils.StartStopStatus;
import views.html.*;

import static akka.pattern.Patterns.ask;
import play.libs.Akka;
import static play.mvc.Results.async;
import static play.libs.Akka.future;
import play.libs.F.*;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
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

/**
 * <p>Admin controller serving processes list and any other status informations about administrated supervisord instances.<p>
 * 
 * @author Cédric Sougné
 *
 */
@Security.Authenticated(Secured.class)
public class Control extends Controller {

	/**
	 * <p>Clear the stdout and stderr logs for the named process on referenced supervisor and reopen them.</p>
	 * 
	 * @param supid Supervisor id
	 * @param name Process name
	 * @return Result
	 */
	public static play.mvc.Result clear(Long supid, String name) {
		Supervisor supervisor = Supervisor.find.byId(supid);

		String host = supervisor.host;
		int port = supervisor.port;

		utils.SupervisordClient supClient = null;
		try {
			if (supervisor.authentification != null) {
				if (supervisor.authentification) {
					String login = supervisor.login;
					String password = supervisor.password;
					supClient = new utils.SupervisordClient(host, port, login,
							password);
				} else {
					supClient = new utils.SupervisordClient(host, port);
				}
			} else {
				supClient = new utils.SupervisordClient(host, port);
			}
			Boolean result = supClient.clearProcessLogs(name);
			if (result) {
				flash("success", "Logs for process " + name
						+ " successfully cleared on " + supervisor.name);
			} else {
				flash("error", "Logs for process " + name
						+ " could not be cleared on " + supervisor.name);
			}
		} catch (Exception ex) {
			if (ex.getCause() instanceof ConnectException) {
				flash("error", "\n<strong>" + supervisor.name + "</strong> : "
						+ ex.getCause().getMessage());
			} else if (ex.getCause() instanceof SocketTimeoutException) {
				flash("warning", "\n<strong>" + supervisor.name
						+ "</strong> : " + ex.getCause().getMessage());
			} else {
				flash("error", "\n<strong>" + supervisor.name + "</strong> : "
						+ ex.getCause() + ex.getMessage());
			}
		}
		return redirect(routes.Control.processlist());
	}

	/**
	 * <p>Main default access page</p>
	 * 
	 * @return Result
	 */
	public static play.mvc.Result index() {
		if (Secured.hasReadAccess()) {
			return ok(indexcontrol.render());
		} else {
			return unauthorized(errorPage.render("Attention!",
					"Accès non authorisé"));
		}
	}

	/**
	 * <p>Read length bytes from name’s stdout log starting at offset</p>
	 * 
	 * @param supid Supervisor id
	 * @param name Process name
	 * @return Result
	 */
	public static play.mvc.Result logs(Long supid, String name) {
		Supervisor supervisor = Supervisor.find.byId(supid);

		String host = supervisor.host;
		int port = supervisor.port;

		utils.SupervisordClient supClient = null;
		try {
			if (supervisor.authentification != null) {
				if (supervisor.authentification) {
					String login = supervisor.login;
					String password = supervisor.password;
					supClient = new utils.SupervisordClient(host, port, login,
							password);
				} else {
					supClient = new utils.SupervisordClient(host, port);
				}
			} else {
				supClient = new utils.SupervisordClient(host, port);
			}
			String result = supClient.readProcessStdoutLog(name, -200, 0);
			return ok(logs.render(name, supervisor.name, supid, result));
		} catch (Exception ex) {
			if (ex.getCause() instanceof ConnectException) {
				flash("error", "\n<li><strong>" + supervisor.name
						+ "</strong> : " + ex.getCause() + " "
						+ ex.getCause().getMessage() + "</li>");
			} else if (ex.getCause() instanceof SocketTimeoutException) {
				flash("error", "\n<li><strong>" + supervisor.name
						+ "</strong> : " + ex.getCause() + " "
						+ ex.getCause().getMessage() + "</li>");
			} else {
				flash("error", "\n<li><strong>" + supervisor.name
						+ "</strong> : " + ex + " " + ex.getMessage() + "</li>");
			}
			return redirect(routes.Control.processlist());
		}
	}

	/**
	 * <p>Process list with actions link for process control</p>
	 * 
	 * @return Result
	 */
	public static play.mvc.Result processlist() {
		if (Secured.hasReadAccess()) {

			List<Supervisor> supervisors = Supervisor.find.fetch("group")
					.where().order("group.name asc, name asc").findList();
			List<utils.SupervisorProcess> processs = new ArrayList<utils.SupervisorProcess>();

			String flash = "";

			for (Supervisor supervisor : supervisors) {
				String host = supervisor.host;
				int port = supervisor.port;

				utils.SupervisordClient supClient = null;
				try {
					if (supervisor.authentification != null) {
						if (supervisor.authentification) {
							String login = supervisor.login;
							String password = supervisor.password;
							supClient = new utils.SupervisordClient(host, port,
									login, password);
						} else {
							supClient = new utils.SupervisordClient(host, port);
						}
					} else {
						supClient = new utils.SupervisordClient(host, port);
					}
					List<ProcessInfo> processInfos = supClient
							.getAllProcessInfo();
					for (ProcessInfo processInfo : processInfos) {
						processs.add(new utils.SupervisorProcess(processInfo,
								supervisor));
					}
				} catch (Exception ex) {
					if (ex.getCause() instanceof ConnectException) {
						flash += "\n<li><strong>" + supervisor.name
								+ "</strong> : " + ex.getCause().getMessage()
								+ "</li>";
					} else if (ex.getCause() instanceof SocketTimeoutException) {
						flash += "\n<li><strong>" + supervisor.name
								+ "</strong> : " + ex.getCause().getMessage()
								+ "</li>";
					} else {
						flash += "\n<li><strong>" + supervisor.name
								+ "</strong> : " + ex.getMessage() + "</li>";
					}
				}
			}

			if (!flash.isEmpty()) {
				flash("warning", "<ul>\n" + flash + "\n</ul>");
			}

			return ok(processlist.render(processs));
		} else {
			return unauthorized(errorPage.render("Attention!",
					"Accès non authorisé"));
		}
	}

	/**
	 * <p>Restart process on referenced supervisor then return to the process list page</p>
	 * 
	 * @param supid Supervisor id
	 * @param name Process name
	 * @return Result
	 */
	public static play.mvc.Result restart(Long supid, String name) {
		Supervisor supervisor = Supervisor.find.byId(supid);

		String host = supervisor.host;
		int port = supervisor.port;

		utils.SupervisordClient supClient = null;
		try {
			if (supervisor.authentification != null) {
				if (supervisor.authentification) {
					String login = supervisor.login;
					String password = supervisor.password;
					supClient = new utils.SupervisordClient(host, port, login,
							password);
				} else {
					supClient = new utils.SupervisordClient(host, port);
				}
			} else {
				supClient = new utils.SupervisordClient(host, port);
			}
			try {
				Boolean result = supClient.stopProcess(name, true);
			} catch (Exception ex) {
				Thread.sleep(1000);
			}
			Boolean result = supClient.startProcess(name, true);
			if (result) {
				flash("success", "Process " + name
						+ " successfully restarted on " + supervisor.name);
			} else {
				flash("error", "Process " + name
						+ " could not be restarted on " + supervisor.name);
			}
		} catch (Exception ex) {
			if (ex.getCause() instanceof ConnectException) {
				flash("error", "\n<li><strong>" + supervisor.name
						+ "</strong> : " + ex.getCause().getMessage() + "</li>");
			} else if (ex.getCause() instanceof SocketTimeoutException) {
				flash("error", "\n<li><strong>" + supervisor.name
						+ "</strong> : " + ex.getCause().getMessage() + "</li>");
			} else {
				flash("error", "\n<li><strong>" + supervisor.name
						+ "</strong> : " + ex.getMessage() + "</li>");
			}
		}
		return redirect(routes.Control.processlist());
	}

	/**
	 * <p>Not yet implemented</p>
	 * 
	 * @return Result
	 */
	public static play.mvc.Result restartall() {
		return redirect(routes.Control.processlist());
	}


	/**
	 * <p>Start process on referenced supervisor then return to the process list page</p>
	 * <p>Action will wait result with a default timeout defined in SupervisordClient</p>
	 * 
	 * @param supid Supervisor id
	 * @param name Process name
	 * @return Result page
	 */
	public static play.mvc.Result start(Long supid, String name) {
		Supervisor supervisor = Supervisor.find.byId(supid);

		String host = supervisor.host;
		int port = supervisor.port;

		utils.SupervisordClient supClient = null;
		try {
			if (supervisor.authentification != null) {
				if (supervisor.authentification) {
					String login = supervisor.login;
					String password = supervisor.password;
					supClient = new utils.SupervisordClient(host, port, login,
							password);
				} else {
					supClient = new utils.SupervisordClient(host, port);
				}
			} else {
				supClient = new utils.SupervisordClient(host, port);
			}
			Boolean result = supClient.startProcess(name, true);
			if (result) {
				flash("success", "Process " + name
						+ " successfully started on " + supervisor.name);
			} else {
				flash("error", "Process " + name + " could not be started on "
						+ supervisor.name);
			}
		} catch (Exception ex) {
			if (ex.getCause() instanceof ConnectException) {
				flash("error", "\n<li><strong>" + supervisor.name
						+ "</strong> : " + ex.getCause().getMessage() + "</li>");
			} else if (ex.getCause() instanceof SocketTimeoutException) {
				flash("error", "\n<li><strong>" + supervisor.name
						+ "</strong> : " + ex.getCause().getMessage() + "</li>");
			} else {
				flash("error", "\n<li><strong>" + supervisor.name
						+ "</strong> : " + ex.getMessage() + "</li>");
			}
		}
		return redirect(routes.Control.processlist());
	}

	/**
	 * <p>Stop process on referenced supervisor then return to the process list page</p>
	 * <p>Action will wait result with a default timeout defined in SupervisordClient</p>
	 * 
	 * @param supid Supervisor id
	 * @param name Process name
	 * @return Result
	 */
	public static play.mvc.Result stop(Long supid, String name) {
		Supervisor supervisor = Supervisor.find.byId(supid);

		String host = supervisor.host;
		int port = supervisor.port;

		utils.SupervisordClient supClient = null;
		try {
			if (supervisor.authentification != null) {
				if (supervisor.authentification) {
					String login = supervisor.login;
					String password = supervisor.password;
					supClient = new utils.SupervisordClient(host, port, login,
							password);
				} else {
					supClient = new utils.SupervisordClient(host, port);
				}
			} else {
				supClient = new utils.SupervisordClient(host, port);
			}
			Boolean result = supClient.stopProcess(name, true);
			if (result) {
				flash("success", "Process " + name
						+ " successfully started on " + supervisor.name);
			} else {
				flash("error", "Process " + name + " could not be started on "
						+ supervisor.name);
			}
		} catch (Exception ex) {
			if (ex.getCause() instanceof ConnectException) {
				flash("error", "\n<strong>" + supervisor.name + "</strong> : "
						+ ex.getCause().getMessage());
			} else if (ex.getCause() instanceof SocketTimeoutException) {
				flash("warning", "\n<strong>" + supervisor.name
						+ "</strong> : " + ex.getCause().getMessage());
			} else {
				flash("error", "\n<strong>" + supervisor.name + "</strong> : "
						+ ex.getCause() + ex.getMessage());
			}
		}
		return redirect(routes.Control.processlist());
	}

	/**
	 * <p>Not yet implemented</p>
	 * 
	 * @return Result
	 */
	public static play.mvc.Result stopall() {
		return redirect(routes.Control.processlist());
	}
}
