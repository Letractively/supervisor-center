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

import java.util.Map;

/**
 * <p>Process information constructed from xmlrpc Map type result</p> 
 * 
 * @author Cédric Sougné
 */
public class ProcessInfo {
	String name;
	String group;
	int start;
	int stop;
	int now;
	int state;
	String statename;
	String spawnerr;
	int exitstatus;
	String stdout_logfile;
	String stderr_logfile;
	int pid;

	/**
	 * <p>Main constructor</p>
	 * 
	 * @param processInfo Map object returned from xmlrpc client
	 */
	public ProcessInfo(Map processInfo) {
		this.name = (String) processInfo.get("name");
		this.group = (String) processInfo.get("group");
		this.start = (int) processInfo.get("start");
		this.stop = (int) processInfo.get("stop");
		this.now = (int) processInfo.get("now");
		this.state = (Integer) processInfo.get("state");
		this.statename = (String) processInfo.get("statename");
		this.spawnerr = (String) processInfo.get("spawnerr");
		this.exitstatus = (Integer) processInfo.get("exitstatus");
		this.stdout_logfile = (String) processInfo.get("stdout_logfile");
		this.stderr_logfile = (String) processInfo.get("stderr_logfile");
		this.pid = (Integer) processInfo.get("pid");
	}

	/**
	 * <p>Get process exit status</p>
	 * 
	 * @return exitstatus
	 */
	public int getExitstatus() {
		return exitstatus;
	}

	/**
	 * <p>Get process group name</p>
	 * 
	 * @return group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * <p>Get process name</p>
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Get process current host unix timestamp</p>
	 * 
	 * @return current process unix timestamp
	 */
	public int getNow() {
		return now;
	}

	/**
	 * <p>Get process pid</p>
	 * 
	 * @return pid
	 */
	public int getPid() {
		return pid;
	}

	/**
	 * <p>Get process spawnerr</p>
	 * 
	 * @return spawnerr
	 */
	public String getSpawnerr() {
		return spawnerr;
	}

	/**
	 * <p>Get process start unix timestamp</p>
	 * 
	 * @return start unix timestamp
	 */
	public int getStart() {
		return start;
	}

	/**
	 * <p>Get process state</p>
	 * 
	 * @return state
	 */
	public int getState() {
		return state;
	}

	/**
	 * <p>Get process state name</p>
	 * 
	 * @return statename
	 */
	public String getStatename() {
		return statename;
	}

	/**
	 * <p>Get process error log file location</p>
	 * 
	 * @return stderr_logfile
	 */
	public String getStderr_logfile() {
		return stderr_logfile;
	}

	/**
	 * <p>Get process log file location</p>
	 * 
	 * @return stdout_logfile
	 */
	public String getStdout_logfile() {
		return stdout_logfile;
	}

	/**
	 * <p>Get process stop unix timestamp</p>
	 * 
	 * @return stop unix timestamp
	 */
	public int getStop() {
		return stop;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProcessInfo{" + "name=" + name + ", group=" + group
				+ ", start=" + start + ", stop=" + stop + ", now=" + now
				+ ", state=" + state + ", statename=" + statename
				+ ", spawnerr=" + spawnerr + ", exitstatus=" + exitstatus
				+ ", stdout_logfile=" + stdout_logfile + ", stderr_logfile="
				+ stderr_logfile + ", pid=" + pid + '}';
	}

}
