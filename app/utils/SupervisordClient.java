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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * <p>Client class to communicate with supervisord xmlrpc2 interface. 
 * This class implement most of the Supervisord API</p>
 * 
 * <p>Instantiate this class with supervisord host/port (and login information) and it will do the rest for you.</p>
 * 
 * @author Cédric Sougné
 *
 */
public class SupervisordClient {
	/**
	 * <p>Supervisor host name or ip where xmlrpc interface is listening on.</p>
	 */
	String host;
	/**
	 * <p>Supervisor port where xmlrpc interface is listening on.</p>
	 */
	int port;
	/**
	 * <p>Http user if required</p>
	 */
	String user = null;
	/**
	 * <p>Http password if required</p>
	 */
	String password = null;

	/**
	 * <p>Apache xmlrpc client</p>
	 */
	XmlRpcClient client = new XmlRpcClient();;

	/**
	 * <p>Contructor without http authentication</p>
	 * 
	 * @param host Supervisord host name or ip
	 * @param port Supervisord port
	 * @throws MalformedURLException
	 */
	public SupervisordClient(String host, int port)
			throws MalformedURLException {

		this.host = host;
		this.port = port;

		setconfig();
	}

	/**
	 * <p>Contructor when http authentication is required</p>
	 * 
	 * @param host Supervisord host name or ip
	 * @param port Supervisord port
	 * @param user Http user
	 * @param password Http password
	 * @throws MalformedURLException
	 */
	public SupervisordClient(String host, int port, String user, String password)
			throws MalformedURLException {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;

		setconfig();
	}

	// method : supervisor.addProcessGroup
	/**
	 * Update the config for a running process from config file.
	 * 
	 * @param name name of process group to add
	 * @return true if successful
	 * @throws XmlRpcException
	 */
	public Boolean addProcessGroup(String name) throws XmlRpcException {
		Object[] params = new Object[] { name };
		Boolean result = (Boolean) client.execute("supervisor.addProcessGroup",
				params);
		return result;
	}

	// method : supervisor.clearAllProcessLogs
	/**
	 * Clear all process log files
	 * 
	 * @return Always return true
	 * @throws XmlRpcException
	 */
	public Boolean clearAllProcessLogs() throws XmlRpcException {
		Object[] params = new Object[] {};
		Boolean result = (Boolean) client.execute(
				"supervisor.clearAllProcessLogs", params);
		return result;
	}

	// method : supervisor.clearLog
	/**
	 * Clear the main log.
	 * 
	 * If the log cannot be cleared because the log file does not exist, the fault NO_FILE will be raised. If the log cannot be cleared for any other reason, the fault FAILED will be raised.
	 * 
	 * @return always returns True unless error
	 * @throws XmlRpcException
	 */
	public Boolean clearLog() throws XmlRpcException {
		Object[] params = new Object[] {};
		Boolean result = (Boolean) client
				.execute("supervisor.clearLog", params);
		return result;
	}

	// method : supervisor.clearProcessLogs
	/**
	 * Clear the stdout and stderr logs for the named process and reopen them.
	 * 
	 * @param name The name of the process (or ‘group:name’)
	 * @return Always True unless error
	 * @throws XmlRpcException
	 */
	public Boolean clearProcessLogs(String name) throws XmlRpcException {
		Object[] params = new Object[] { name };
		Boolean result = (Boolean) client.execute(
				"supervisor.clearProcessLogs", params);
		return result;
	}

	// method : supervisor.getAllConfigInfo
	/**
	 * Undocumented in current supervisord API
	 * 
	 * @return Object[] of config infos
	 * @throws XmlRpcException
	 */
	public Object[] getAllConfigInfo() throws XmlRpcException {
		Object[] params = new Object[] {};
		Object[] results = (Object[]) client.execute(
				"supervisor.getAllConfigInfo", params);
		return results;
	}

	// method : supervisor.getAllProcessInfo
	/**
	 * Get info about all processes
	 * 
	 * @return List of ProcessInfo objects
	 * @throws XmlRpcException
	 */
	public List<ProcessInfo> getAllProcessInfo() throws XmlRpcException {
		Object[] params = new Object[] {};
		Object[] results = (Object[]) client.execute(
				"supervisor.getAllProcessInfo", params);
		List<ProcessInfo> processlist = new ArrayList<ProcessInfo>();
		for (Object object : results) {
			processlist.add(new ProcessInfo((Map) object));
		}
		return processlist;
	}

	// method : supervisor.getAPIVersion
	/**
	 * Return the version of the RPC API used by supervisord
	 * 
	 * This API is versioned separately from Supervisor itself. The API version returned by getAPIVersion only changes when the API changes. Its purpose is to help the client identify with which version of the Supervisor API it is communicating.
	 * 
	 * @return version id
	 * @throws XmlRpcException
	 */
	public String getAPIVersion() throws XmlRpcException {
		Object[] params = new Object[] {};
		String result = (String) client.execute("supervisor.getAPIVersion",
				params);
		return result;
	}

	// method : supervisor.getIdentification
	/**
	 * Return identifiying string of supervisord
	 * 
	 * @return identifiying string
	 * @throws XmlRpcException
	 */
	public String getIdentification() throws XmlRpcException {
		Object[] params = new Object[] {};
		String result = (String) client.execute("supervisor.getIdentification",
				params);
		return result;
	}

	// method : supervisor.getPID
	/**
	 * Return the PID of supervisord
	 * 
	 * @return PID
	 * @throws XmlRpcException
	 */
	public int getPID() throws XmlRpcException {
		Object[] params = new Object[] {};
		int result;
		result = (Integer) client.execute("supervisor.getPID", params);
		return result;
	}

	// method : supervisor.getProcessInfo
	/**
	 * Get info about a process named name as a ProcessInfo object
	 * 
	 * @param name Process name
	 * @return ProcessInfo object
	 * @throws XmlRpcException
	 */
	public ProcessInfo getProcessInfo(String name)
			throws XmlRpcException {
		Object[] params = new Object[] { name };
		Object result = client.execute("supervisor.getProcessInfo", params);
		ProcessInfo processInfo = new ProcessInfo((Map) result);
		return processInfo;
	}

	// method : supervisor.getState
	/**
	 * Return current state of supervisord as a SupervisorState object
	 * 
	 * @return SupervisorState object
	 * @throws XmlRpcException
	 */
	public SupervisorState getState() throws XmlRpcException {
		Object[] params = new Object[] {};
		Object result = client.execute("supervisor.getState", params);
		SupervisorState supervisorState = new SupervisorState((Map) result);
		return supervisorState;
	}

	// method : supervisor.getSupervisorVersion
	/**
	 * Return the version of the supervisor package in use by supervisord
	 * 
	 * @return version id
	 * @throws XmlRpcException
	 */
	public String getSupervisorVersion() throws XmlRpcException {
		Object[] params = new Object[] {};
		String result = (String) client.execute(
				"supervisor.getSupervisorVersion", params);
		return result;
	}

	// method : system.listMethods
	/**
	 * Return an arraylist of available method names
	 * 
	 * @return method names list
	 * @throws XmlRpcException
	 */
	public List<String> listMethods() throws XmlRpcException {
		Object[] params = new Object[] {};
		List<String> methods = new ArrayList<String>();
		Object[] resultarray = (Object[]) client.execute("system.listMethods",
				params);
		for (Object object : resultarray) {
			methods.add(object.toString());
		}
		return methods;
	}

	// method : system.methodHelp
	/**
	 * Return a string showing the method’s documentation
	 * 
	 * @param name The name of the method
	 * @return The documentation for the method name.
	 * @throws XmlRpcException
	 */
	public String methodHelp(String name) throws XmlRpcException {
		Object[] params = new Object[] { name };
		String result = (String) client.execute("system.methodHelp", params);
		return result;
	}

	// method : system.methodSignature
	/**
	 * Return an array describing the method signature in the form [rtype, ptype, ptype...] where rtype is the return data type of the method, and ptypes are the parameter data types that the method accepts in method argument order.
	 * 
	 * @param name The name of the method.
	 * @return The result.
	 * @throws XmlRpcException
	 */
	public Object[] methodSignature(String name) throws XmlRpcException {
		Object[] params = new Object[] { name };
		Object[] result = (Object[]) client.execute("system.methodSignature",
				params);
		return result;
	}

	// method : supervisor.getVersion
	// method : supervisor.readLog
	/**
	 * Read length bytes from the main log starting at offset
	 * It can either return the entire log, a number of characters from the tail of the log, or a slice of the log specified by the offset and length parameters:
	 * 
	 * Offset	Length	Behavior of readProcessLog
	 * Negative	Not Zero	Bad arguments. This will raise the fault BAD_ARGUMENTS.
	 * Negative	Zero	This will return the tail of the log, or offset number of characters from the end of the log. For example, if offset = -4 and length = 0, then the last four characters will be returned from the end of the log.
	 * Zero or Positive	Negative	Bad arguments. This will raise the fault BAD_ARGUMENTS.
	 * Zero or Positive	Zero	All characters will be returned from the offset specified.
	 * Zero or Positive	Positive	A number of characters length will be returned from the offset.
	 * If the log is empty and the entire log is requested, an empty string is returned.
	 * 
	 * If either offset or length is out of range, the fault BAD_ARGUMENTS will be returned.
	 * 
	 * If the log cannot be read, this method will raise either the NO_FILE error if the file does not exist or the FAILED error if any other problem was encountered.
	 * 
	 * Note: The readLog() method replaces readMainLog() found in Supervisor versions prior to 2.1. It is aliased for compatibility but readMainLog() is deprecated and support will be dropped from Supervisor in a future version.
	 * 
	 * @param offset offset to start reading from.
	 * @param lenght length number of bytes to read from the log.
	 * @return result Bytes of log
	 * @throws XmlRpcException
	 */
	public String readLog(int offset, int lenght) throws XmlRpcException {
		Object[] params = new Object[] { new Integer(offset),
				new Integer(lenght) };
		String result = (String) client.execute("supervisor.readLog", params);
		return result;
	}

	// method : supervisor.readMainLog
	// method : supervisor.readProcessLog
	// method : supervisor.readProcessStderrLog
	/**
	 * Read length bytes from name’s stderr log starting at offset
	 * 
	 * @param name the name of the process (or ‘group:name’)
	 * @param offset offset to start reading from.
	 * @param lenght number of bytes to read from the log. 
	 * @return result Bytes of log
	 * @throws XmlRpcException
	 */
	public String readProcessStderrLog(String name, int offset, int lenght)
			throws XmlRpcException {
		Object[] params = new Object[] { name, new Integer(offset),
				new Integer(lenght) };
		String result = (String) client.execute(
				"supervisor.readProcessStderrLog", params);
		return result;
	}

	// method : supervisor.readProcessStdoutLog
	/**
	 * Read length bytes from name’s log starting at offset
	 * 
	 * @param name the name of the process (or ‘group:name’)
	 * @param offset offset to start reading from.
	 * @param lenght number of bytes to read from the log. 
	 * @return result Bytes of log
	 * @throws XmlRpcException
	 */
	public String readProcessStdoutLog(String name, int offset, int lenght)
			throws XmlRpcException {
		Object[] params = new Object[] { name, new Integer(offset),
				new Integer(lenght) };
		String result = (String) client.execute(
				"supervisor.readProcessStdoutLog", params);
		return result;
	}

	// method : supervisor.reloadConfig
	// method : supervisor.removeProcessGroup
	/**
	 * Remove a stopped process from the active configuration.
	 * 
	 * @param name name of process group to remove
	 * @return Indicates wether the removal was successful
	 * @throws XmlRpcException
	 */
	public Boolean removeProcessGroup(String name) throws XmlRpcException {
		Object[] params = new Object[] { name };
		Boolean result = (Boolean) client.execute(
				"supervisor.removeProcessGroup", params);
		return result;
	}

	// method : supervisor.restart
	/**
	 * Restart the supervisor process
	 * 
	 * @return always return True unless error
	 * @throws XmlRpcException
	 */
	public Boolean restart() throws XmlRpcException {
		Object[] params = new Object[] {};
		Boolean result = (Boolean) client.execute("supervisor.restart", params);
		return result;
	}

	// method : supervisor.sendProcessStdin
	/**
	 * Send a string of chars to the stdin of the process name. If non-7-bit data is sent (unicode), it is encoded to utf-8 before being sent to the process’ stdin. If chars is not a string or is not unicode, raise INCORRECT_PARAMETERS. If the process is not running, raise NOT_RUNNING. If the process’ stdin cannot accept input (e.g. it was closed by the child process), raise NO_FILE.
	 * 
	 * @param name The process name to send to (or ‘group:name’)
	 * @param chars  The character data to send to the process
	 * @return Always return True unless error
	 * @throws XmlRpcException
	 */
	public Boolean sendProcessStdin(String name, String chars)
			throws XmlRpcException {
		Object[] params = new Object[] { name, chars };
		Boolean result = (Boolean) client.execute(
				"supervisor.sendProcessStdin", params);
		return result;
	}

	// method : supervisor.sendRemoteCommEvent
	/**
	 * Send an event that will be received by event listener subprocesses subscribing to the RemoteCommunicationEvent.
	 * 
	 * @param name String for the “type” key in the event header
	 * @param data Data for the event body
	 * @return Always return True unless error
	 * @throws XmlRpcException
	 */
	public Boolean sendRemoteCommEvent(String name, String data)
			throws XmlRpcException {
		Object[] params = new Object[] { name, data };
		Boolean result = (Boolean) client.execute(
				"supervisor.sendRemoteCommEvent", params);
		return result;
	}

	/**
	 * Undocumented in supervisord API
	 * 
	 * @throws MalformedURLException
	 */
	private void setconfig() throws MalformedURLException {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

		// Setting timeouts for xmlrpc calls made using
		// XmlRpcSunHttpTransportFactory, the default connection factory
		int xmlrpcConnTimeout = 1000; // Connection timeout
		int xmlrpcReplyTimeOut = 6000; // Reply timeout

		config.setConnectionTimeout(xmlrpcConnTimeout);
		config.setReplyTimeout(xmlrpcReplyTimeOut);
		config.setEncoding("UTF-8");

		if (user != null && password != null) {
			config.setBasicUserName(user);
			config.setBasicPassword(password);
		}
		config.setServerURL(new URL("http://" + host + ":" + port + "/RPC2/"));
		client.setConfig(config);
		client.setTypeFactory(new MyTypeFactory(client));
	}

	// method : supervisor.shutdown
	/**
	 * Shut down the supervisor process
	 * 
	 * @return always returns True unless error
	 * @throws XmlRpcException
	 */
	public Boolean shutdown() throws XmlRpcException {
		Object[] params = new Object[] {};
		Boolean result = (Boolean) client
				.execute("supervisor.shutdown", params);
		return result;
	}

	/**
	 * Start all processes listed in the configuration file waiting for each process to be fully started 
	 * 
	 * @return List of StartStopStatus
	 * @throws XmlRpcException
	 */
	public List<StartStopStatus> startAllProcesses() throws XmlRpcException {
		return startAllProcesses(true);
	}

	// method : supervisor.startAllProcesses
	/**
	 * Start all processes listed in the configuration file
	 * 
	 * @param wait Wait for each process to be fully started 
	 * @return A list of StartStopStatuss
	 * @throws XmlRpcException
	 */
	public List<StartStopStatus> startAllProcesses(Boolean wait)
			throws XmlRpcException {
		Object[] params = new Object[] { true };
		List<StartStopStatus> startStatusList = new ArrayList<StartStopStatus>();
		Object[] resultarray = (Object[]) client.execute(
				"supervisor.startAllProcesses", params);
		for (Object object : resultarray) {
			startStatusList.add(new StartStopStatus((HashMap) object));
		}
		return startStatusList;
	}

	/**
	 * Start a process waiting for process to be fully started
	 * 
	 * @param name Process name (or ‘group:name’, or ‘group:*‘)
	 * @return Always true unless error
	 * @throws XmlRpcException
	 */
	public Boolean startProcess(String name) throws XmlRpcException {
		return startProcess(name, true);
	}

	// method : supervisor.startProcess
	/**
	 * Start a process
	 * 
	 * @param name Process name (or ‘group:name’, or ‘group:*‘)
	 * @param wait Wait for process to be fully started
	 * @return Always true unless error
	 * @throws XmlRpcException
	 */
	public Boolean startProcess(String name, Boolean wait)
			throws XmlRpcException {
		Object[] params = new Object[] { name, wait };
		Boolean result = (Boolean) client.execute("supervisor.startProcess",
				params);
		return result;
	}

	/**
	 * Start all processes in the group named ‘name’
	 * 
	 * @param name The group name
	 * @return A list of StartStopStatus
	 * @throws XmlRpcException
	 */
	public List<StartStopStatus> startProcessGroup(String name)
			throws XmlRpcException {
		return startProcessGroup(name, true);
	}

	// method : supervisor.startProcessGroup
	/**
	 * Start all processes in the group named ‘name’
	 * 
	 * @param name The group name
	 * @param wait Wait for process group to be fully started
	 * @return A list of StartStopStatus
	 * @throws XmlRpcException
	 */
	public List<StartStopStatus> startProcessGroup(String name, Boolean wait)
			throws XmlRpcException {
		Object[] params = new Object[] { name, true };
		List<StartStopStatus> startStatusList = new ArrayList<StartStopStatus>();
		Object[] resultarray = (Object[]) client.execute(
				"supervisor.startProcessGroup", params);
		for (Object object : resultarray) {
			startStatusList.add(new StartStopStatus((HashMap) object));
		}
		return startStatusList;
	}

	/**
	 * Stop all processes listed in the configuration file waiting for each process to be fully stopped 
	 * 
	 * @return A list of StartStopStatuss
	 * @throws XmlRpcException
	 */
	public List<StartStopStatus> stopAllProcesses() throws XmlRpcException {
		return stopAllProcesses(true);
	}

	// method : supervisor.stopAllProcesses
	/**
	 * Stop all processes listed in the configuration file
	 * 
	 * @param wait Wait for each process to be fully stopped 
	 * @return A list of StartStopStatuss
	 * @throws XmlRpcException
	 */
	public List<StartStopStatus> stopAllProcesses(Boolean wait)
			throws XmlRpcException {
		Object[] params = new Object[] { true };
		List<StartStopStatus> startStatusList = new ArrayList<StartStopStatus>();
		Object[] resultarray = (Object[]) client.execute(
				"supervisor.stopAllProcesses", params);
		for (Object object : resultarray) {
			startStatusList.add(new StartStopStatus((HashMap) object));
		}
		return startStatusList;
	}

	/**
	 * Stop a process waiting for process to be fully stopped
	 * 
	 * @param name Process name (or ‘group:name’, or ‘group:*‘)
	 * @return Always true unless error
	 * @throws XmlRpcException
	 */
	public Boolean stopProcess(String name) throws XmlRpcException {
		return stopProcess(name, true);
	}

	// method : supervisor.stopProcess
	/**
	 * Stop a process
	 * 
	 * @param name Process name (or ‘group:name’, or ‘group:*‘)
	 * @param wait Wait for process to be fully stopped
	 * @return Always true unless error
	 * @throws XmlRpcException
	 */
	public Boolean stopProcess(String name, Boolean wait)
			throws XmlRpcException {
		Object[] params = new Object[] { name, wait };
		Boolean result = (Boolean) client.execute("supervisor.stopProcess",
				params);
		return result;
	}

	/**
	 * Stop all processes in the group named ‘name’ waiting for process group to be fully stopped
	 * 
	 * @param name The group name
	 * @return A list of StartStopStatus
	 * @throws XmlRpcException
	 */
	public List<StartStopStatus> stopProcessGroup(String name)
			throws XmlRpcException {
		return stopProcessGroup(name, true);
	}

	// method : supervisor.stopProcessGroup
	/**
	 * Stop all processes in the group named ‘name’
	 * 
	 * @param name The group name
	 * @param wait Wait for process group to be fully stopped
	 * @return A list of StartStopStatus
	 * @throws XmlRpcException
	 */
	public List<StartStopStatus> stopProcessGroup(String name, Boolean wait)
			throws XmlRpcException {
		Object[] params = new Object[] { name, true };
		List<StartStopStatus> startStatusList = new ArrayList<StartStopStatus>();
		Object[] resultarray = (Object[]) client.execute(
				"supervisor.stopProcessGroup", params);
		for (Object object : resultarray) {
			startStatusList.add(new StartStopStatus((HashMap) object));
		}
		return startStatusList;
	}

	// method : supervisor.tailProcessStderrLog
	/**
	 * Provides a more efficient way to tail the (stderr) log than readProcessStderrLog(). Use readProcessStderrLog() to read chunks and tailProcessStderrLog() to tail.
	 *
	 * Requests (length) bytes from the (name)’s log, starting at (offset). If the total log size is greater than (offset + length), the overflow flag is set and the (offset) is automatically increased to position the buffer at the end of the log. If less than (length) bytes are available, the maximum number of available bytes will be returned. (offset) returned is always the last offset in the log +1.
	 * 
	 * @param name the name of the process (or ‘group:name’)
	 * @param offset offset to start reading from
	 * @param lenght maximum number of bytes to return 
	 * @return TailLog
	 * @throws XmlRpcException
	 */
	public TailLog tailProcessStderrLog(String name, int offset, int lenght)
			throws XmlRpcException {
		Object[] params = new Object[] { name, new Integer(offset),
				new Integer(lenght) };
		Object[] result = (Object[]) client.execute(
				"supervisor.tailProcessStderrLog", params);
		return new TailLog(result);
	}

	// method : supervisor.tailProcessStdoutLog
	/**
	 * Provides a more efficient way to tail the (stdout) log than readProcessStdoutLog(). Use readProcessStdoutLog() to read chunks and tailProcessStdoutLog() to tail.
	 *
	 * Requests (length) bytes from the (name)’s log, starting at (offset). If the total log size is greater than (offset + length), the overflow flag is set and the (offset) is automatically increased to position the buffer at the end of the log. If less than (length) bytes are available, the maximum number of available bytes will be returned. (offset) returned is always the last offset in the log +1.
	 * 
	 * @param name the name of the process (or ‘group:name’)
	 * @param offset offset to start reading from
	 * @param lenght maximum number of bytes to return 
	 * @return TailLog
	 * @throws XmlRpcException
	 */
	public TailLog tailProcessStdoutLog(String name, int offset, int lenght)
			throws XmlRpcException {
		Object[] params = new Object[] { name, new Integer(offset),
				new Integer(lenght) };
		Object[] result = (Object[]) client.execute(
				"supervisor.tailProcessStdoutLog", params);
		return new TailLog(result);
	}

	// method : supervisor.tailProcessLog
	// method : system.multicall
}
