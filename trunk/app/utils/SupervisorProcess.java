package utils;

import java.sql.Date;

import play.Logger;

import models.Supervisor;

public class SupervisorProcess {
	String state;
	String name;
	Date startDate;
	Date stopDate;
	utils.ProcessInfo processInfo;
	Supervisor supervisor;
	
	public SupervisorProcess(ProcessInfo processInfo, Supervisor supervisor) {
		super();
		this.processInfo = processInfo;
		this.supervisor = supervisor;
		
		this.state =   processInfo.getStatename();
		this.name = processInfo.getName();
		int start = processInfo.getStart();
		int stop = processInfo.getStop();
		startDate = new Date((long) (start*1000L));
		if (stop != 0) {
			stopDate = new Date((long) (stop*1000L));
		}
	}

	public utils.ProcessInfo getProcessInfo() {
		return processInfo;
	}

	public Supervisor getSupervisor() {
		return supervisor;
	}

	public String getState() {
		return state;
	}

	public String getName() {
		return name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}
	
	
}
