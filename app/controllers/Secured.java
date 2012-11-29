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

import play.mvc.*;
import play.mvc.Http.*;

import models.*;

/**
 * <p>Security controller based on play framework 2 examples.</p>
 * 
 * @author Cédric Sougné
 *
 */
public class Secured extends Security.Authenticator {

	/**
	 * @return true if user has admin rights
	 */
	public static boolean hasAdminAccess() {
		String appLevel = User.findByLogin(Context.current().request()
				.username()).role;
		if (appLevel.compareTo("READ") == 0) {
			return false;
		} else if (appLevel.compareTo("WRITE") == 0) {
			return false;
		} else if (appLevel.compareTo("ADMIN") == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return true if user has read rights
	 */
	public static boolean hasReadAccess() {
		String appLevel = User.findByLogin(Context.current().request()
				.username()).role;
		if (appLevel.compareTo("READ") == 0) {
			return true;
		} else if (appLevel.compareTo("WRITE") == 0) {
			return true;
		} else if (appLevel.compareTo("ADMIN") == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return true if write has admin rights
	 */
	public static boolean hasWriteAccess() {
		String appLevel = User.findByLogin(Context.current().request()
				.username()).role;
		if (appLevel.compareTo("READ") == 0) {
			return false;
		} else if (appLevel.compareTo("WRITE") == 0) {
			return true;
		} else if (appLevel.compareTo("ADMIN") == 0) {
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see play.mvc.Security.Authenticator#getUsername(play.mvc.Http.Context)
	 */
	@Override
	public String getUsername(Context ctx) {
		return ctx.session().get("login");
	}

	/* (non-Javadoc)
	 * @see play.mvc.Security.Authenticator#onUnauthorized(play.mvc.Http.Context)
	 */
	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.Application.login());
	}

}