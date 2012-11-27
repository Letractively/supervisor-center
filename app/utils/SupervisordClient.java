/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.bigjimnetwork.testxmlrpc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 *
 * @author bigjim
 */
public class SupervisordClient {
    String host;
    int port;
    String user = null;
    String password = null;
    
    XmlRpcClient client = new XmlRpcClient();;

    public SupervisordClient(String host, int port) throws MalformedURLException {
        
        this.host = host;
        this.port = port;
        
        connect();
    }

    public SupervisordClient(String host, int port, String user, String password) throws MalformedURLException {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        
        connect();
    }
    
    private void connect() throws MalformedURLException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        if (user != null && password != null){
        config.setBasicUserName(user);
        config.setBasicPassword(password);
        }
        config.setServerURL(new URL("http://"+host+":"+port+"/RPC2/"));
        client.setConfig(config);
        client.setTypeFactory(new MyTypeFactory(client));
    }
    
    //method : supervisor.addProcessGroup
    public Boolean addProcessGroup(String name) throws XmlRpcException {
        Object[] params = new Object[]{name};
        Boolean result = (Boolean) client.execute("supervisor.addProcessGroup", params);
        return result;
    }
    //method : supervisor.clearAllProcessLogs
    public Boolean clearAllProcessLogs() throws XmlRpcException {
        Object[] params = new Object[]{};
        Boolean result = (Boolean) client.execute("supervisor.clearAllProcessLogs", params);
        return result;
    }
    //method : supervisor.clearLog
    public Boolean clearLog() throws XmlRpcException {
        Object[] params = new Object[]{};
        Boolean result = (Boolean) client.execute("supervisor.clearLog", params);
        return result;
    }
    //method : supervisor.clearProcessLogs
    public Boolean clearProcessLogs(String name) throws XmlRpcException {
        Object[] params = new Object[]{name};
        Boolean result = (Boolean) client.execute("supervisor.clearProcessLogs", params);
        return result;
    }
    
    //method : supervisor.getAPIVersion
    public String getAPIVersion() throws XmlRpcException {
        Object[] params = new Object[]{};
        String result = (String) client.execute("supervisor.getAPIVersion", params);
        return result;
    }
    
    //method : supervisor.getAllConfigInfo
    public Object[] getAllConfigInfo() throws XmlRpcException {
        Object[] params = new Object[]{};
        Object[] results = (Object[]) client.execute("supervisor.getAllConfigInfo", params);
        return results;
    }
    //method : supervisor.getAllProcessInfo
    public List<ProcessInfo> getAllProcessInfo() throws XmlRpcException {
        Object[] params = new Object[]{};
        Object[] results = (Object[]) client.execute("supervisor.getAllProcessInfo", params);
        List<ProcessInfo> processlist= new ArrayList<ProcessInfo>();
        for (Object object : results) {
           processlist.add(new ProcessInfo((Map)object));
        }
        return processlist;
    }
    //method : supervisor.getIdentification
    public String getIdentification() throws XmlRpcException {
        Object[] params = new Object[]{};
        String result = (String) client.execute("supervisor.getIdentification", params);
        return result;
    }
    //method : supervisor.getPID
    public int getPID() throws XmlRpcException {
        Object[] params = new Object[]{};
        int result;
        result = (Integer) client.execute("supervisor.getPID", params);
        return result;
    }
    //method : supervisor.getProcessInfo
    public ProcessInfo getProcessInfo(String group,String name) throws XmlRpcException {
        Object[] params = new Object[]{name};
        Object result = client.execute("supervisor.getProcessInfo", params);
        ProcessInfo processInfo = new ProcessInfo((Map)result);
        return processInfo;
    }
    //method : supervisor.getState
    public SupervisorState getState() throws XmlRpcException {
        Object[] params = new Object[]{};
        Object result = client.execute("supervisor.getState", params);
        SupervisorState supervisorState = new SupervisorState((Map)result);
        return supervisorState;
    }
    
    
    //method : supervisor.getSupervisorVersion
    public String getSupervisorVersion() throws XmlRpcException {
        Object[] params = new Object[]{};
        String result = (String) client.execute("supervisor.getSupervisorVersion", params);
        return result;
    }
    //method : supervisor.getVersion
    //method : supervisor.readLog
    public String readLog(int offset,int lenght) throws XmlRpcException {
        Object[] params = new Object[]{new Integer(offset),new Integer(lenght)};
        String result = (String) client.execute("supervisor.readLog", params);
        return result;
    }
    //method : supervisor.readMainLog
    //method : supervisor.readProcessLog
    //method : supervisor.readProcessStderrLog
    public String readProcessStderrLog(String name,int offset,int lenght) throws XmlRpcException {
        Object[] params = new Object[]{name,new Integer(offset),new Integer(lenght)};
        String result = (String) client.execute("supervisor.readProcessStderrLog", params);
        return result;
    }
    //method : supervisor.readProcessStdoutLog
    public String readProcessStdoutLog(String name,int offset,int lenght) throws XmlRpcException {
        Object[] params = new Object[]{name,new Integer(offset),new Integer(lenght)};
        String result = (String) client.execute("supervisor.readProcessStdoutLog", params);
        return result;
    }
    //method : supervisor.reloadConfig
    //method : supervisor.removeProcessGroup
    public Boolean removeProcessGroup(String name) throws XmlRpcException {
        Object[] params = new Object[]{name};
        Boolean result = (Boolean) client.execute("supervisor.removeProcessGroup", params);
        return result;
    }
    //method : supervisor.restart
    public Boolean restart() throws XmlRpcException {
        Object[] params = new Object[]{};
        Boolean result = (Boolean) client.execute("supervisor.restart", params);
        return result;
    }
    //method : supervisor.sendProcessStdin
    public Boolean sendProcessStdin(String name, String chars) throws XmlRpcException {
        Object[] params = new Object[]{name,chars};
        Boolean result = (Boolean) client.execute("supervisor.sendProcessStdin", params);
        return result;
    }
    //method : supervisor.sendRemoteCommEvent
    public Boolean sendRemoteCommEvent(String name, String data) throws XmlRpcException {
        Object[] params = new Object[]{name,data};
        Boolean result = (Boolean) client.execute("supervisor.sendRemoteCommEvent", params);
        return result;
    }
    //method : supervisor.shutdown
    public Boolean shutdown() throws XmlRpcException {
        Object[] params = new Object[]{};
        Boolean result = (Boolean) client.execute("supervisor.shutdown", params);
        return result;
    }
    //method : supervisor.startAllProcesses
    public List<StartStopStatus> startAllProcesses(Boolean wait) throws XmlRpcException {
        Object[] params = new Object[]{true};
        List<StartStopStatus> startStatusList = new ArrayList<StartStopStatus>();
        Object[] resultarray = (Object[]) client.execute("supervisor.startAllProcesses", params );
        for (Object object : resultarray) {
           startStatusList.add(new StartStopStatus((HashMap)object));
        }
        return startStatusList;
    }
    public List<StartStopStatus> startAllProcesses() throws XmlRpcException {
        return startAllProcesses(true);
    }
    //method : supervisor.startProcess
    public Boolean startProcess(String name, Boolean wait) throws XmlRpcException {
        Object[] params = new Object[]{name,wait};
        Boolean result = (Boolean) client.execute("supervisor.startProcess", params);
        return result;
    }
    public Boolean startProcess(String name) throws XmlRpcException {
        return startProcess(name,true);
    }
    //method : supervisor.startProcessGroup
    public List<StartStopStatus> startProcessGroup(String name, Boolean wait) throws XmlRpcException {
        Object[] params = new Object[]{name,true};
        List<StartStopStatus> startStatusList = new ArrayList<StartStopStatus>();
        Object[] resultarray = (Object[]) client.execute("supervisor.startProcessGroup", params );
        for (Object object : resultarray) {
           startStatusList.add(new StartStopStatus((HashMap)object));
        }
        return startStatusList;
    }
    public List<StartStopStatus> startProcessGroup(String name) throws XmlRpcException {
        return startProcessGroup(name,true);
    }
    //method : supervisor.stopAllProcesses
    public List<StartStopStatus> stopAllProcesses(Boolean wait) throws XmlRpcException {
        Object[] params = new Object[]{true};
        List<StartStopStatus> startStatusList = new ArrayList<StartStopStatus>();
        Object[] resultarray = (Object[]) client.execute("supervisor.stopAllProcesses", params );
        for (Object object : resultarray) {
           startStatusList.add(new StartStopStatus((HashMap)object));
        }
        return startStatusList;
    }
    public List<StartStopStatus> stopAllProcesses() throws XmlRpcException {
        return stopAllProcesses(true);
    }
    //method : supervisor.stopProcess
    public Boolean stopProcess(String name, Boolean wait) throws XmlRpcException {
        Object[] params = new Object[]{name,wait};
        Boolean result = (Boolean) client.execute("supervisor.stopProcess", params);
        return result;
    }
    public Boolean stopProcess(String name) throws XmlRpcException {
        return stopProcess(name,true);
    }
    //method : supervisor.stopProcessGroup
    public List<StartStopStatus> stopProcessGroup(String name, Boolean wait) throws XmlRpcException {
        Object[] params = new Object[]{name,true};
        List<StartStopStatus> startStatusList = new ArrayList<StartStopStatus>();
        Object[] resultarray = (Object[]) client.execute("supervisor.stopProcessGroup", params );
        for (Object object : resultarray) {
           startStatusList.add(new StartStopStatus((HashMap)object));
        }
        return startStatusList;
    }
    public List<StartStopStatus> stopProcessGroup(String name) throws XmlRpcException {
        return stopProcessGroup(name,true);
    }
    //method : supervisor.tailProcessStderrLog
    public TailLog tailProcessStderrLog(String name,int offset,int lenght) throws XmlRpcException {
        Object[] params = new Object[]{name,new Integer(offset),new Integer(lenght)};
        Object[] result = (Object[]) client.execute("supervisor.tailProcessStderrLog", params);
        return new TailLog(result);
    }
    //method : supervisor.tailProcessStdoutLog
    public TailLog tailProcessStdoutLog(String name,int offset,int lenght) throws XmlRpcException {
        Object[] params = new Object[]{name,new Integer(offset),new Integer(lenght)};
        Object[] result = (Object[]) client.execute("supervisor.tailProcessStdoutLog", params);
        return new TailLog(result);
    }
    //method : system.listMethods
    public List<String> listMethods() throws XmlRpcException {
        Object[] params = new Object[]{};
        List<String> methods = new ArrayList<String>();
        Object[] resultarray = (Object[]) client.execute("system.listMethods", params );
        for (Object object : resultarray) {
           methods.add(object.toString());
        }
        return methods;
    }
    //method : system.methodHelp
    public String methodHelp(String name) throws XmlRpcException {
        Object[] params = new Object[]{name};
        String result = (String) client.execute("system.methodHelp", params);
        return result;
    }
    //method : system.methodSignature
    public Object[] methodSignature(String name) throws XmlRpcException {
        Object[] params = new Object[]{name};
        Object[] result = (Object[]) client.execute("system.methodSignature", params);
        return result;
    }
    
    //method : supervisor.tailProcessLog
    //method : system.multicall
}
