package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

public class Secured extends Security.Authenticator {
    
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("login");
    }
    
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.login());
    }
    
    // Access rights
    public static boolean hasReadAccess() {
    	String appLevel = User.findByLogin(Context.current().request().username()).role;
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
    
    public static boolean hasWriteAccess() {
    	String appLevel = User.findByLogin(Context.current().request().username()).role;
    	if (appLevel.compareTo("Consultation") == 0) {
    		return false;
    	} else if (appLevel.compareTo("Modification") == 0) {
    		return true;
    	} else if (appLevel.compareTo("Administration") == 0) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public static boolean hasAdminAccess() {
    	String appLevel = User.findByLogin(Context.current().request().username()).role;
    	if (appLevel.compareTo("Consultation") == 0) {
    		return false;
    	} else if (appLevel.compareTo("Modification") == 0) {
    		return false;
    	} else if (appLevel.compareTo("Administration") == 0) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
}