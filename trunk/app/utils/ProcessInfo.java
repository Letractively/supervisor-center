/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Map;

/**
 *
 * @author bigjim
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
    
    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public int getStart() {
        return start;
    }

    public int getStop() {
        return stop;
    }

    public int getNow() {
        return now;
    }

    public int getState() {
        return state;
    }

    public String getStatename() {
        return statename;
    }

    public String getSpawnerr() {
        return spawnerr;
    }

    public int getExitstatus() {
        return exitstatus;
    }

    public String getStdout_logfile() {
        return stdout_logfile;
    }

    public String getStderr_logfile() {
        return stderr_logfile;
    }

    public int getPid() {
        return pid;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" + "name=" + name + ", group=" + group + ", start=" + start + ", stop=" + stop + ", now=" + now + ", state=" + state + ", statename=" + statename + ", spawnerr=" + spawnerr + ", exitstatus=" + exitstatus + ", stdout_logfile=" + stdout_logfile + ", stderr_logfile=" + stderr_logfile + ", pid=" + pid + '}';
    }


}
