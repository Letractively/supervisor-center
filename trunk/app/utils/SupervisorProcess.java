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
package utils;

import java.sql.Date;

import models.Supervisor;

/**
 * <p>Convenient object representing processes states and infos</p>
 * <p>Used as param to show information into templates</p>
 * 
 * @author Cédric Sougné
 *
 */
public class SupervisorProcess {
	String state;
	String name;
	Date startDate;
	Date stopDate;
	utils.ProcessInfo processInfo;
	Supervisor supervisor;

	/**
	 * Constructor
	 * 
	 * @param processInfo
	 * @param supervisor
	 */
	public SupervisorProcess(ProcessInfo processInfo, Supervisor supervisor) {
		super();
		this.processInfo = processInfo;
		this.supervisor = supervisor;

		this.state = processInfo.getStatename();
		this.name = processInfo.getName();
		int start = processInfo.getStart();
		int stop = processInfo.getStop();
		startDate = new Date(start * 1000L);
		if (stop != 0) {
			stopDate = new Date(stop * 1000L);
		}
	}

	/**
	 * Get process name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get ProcessInfo object of the process
	 * 
	 * @return ProcessInfo
	 */
	public utils.ProcessInfo getProcessInfo() {
		return processInfo;
	}

	/**
	 * Get last start date of the process
	 * 
	 * @return Date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Get state name of the process (RUNNING,STOPPED,...)
	 * 
	 * @return State name
	 */
	public String getState() {
		return state;
	}


	/**
	 * Get stop date of the process
	 * 
	 * @return Date or null if running
	 */
	public Date getStopDate() {
		return stopDate;
	}

	/**
	 * Get Supervisor model object linked to this process
	 * 
	 * @return Supervisor
	 */
	public Supervisor getSupervisor() {
		return supervisor;
	}

}
