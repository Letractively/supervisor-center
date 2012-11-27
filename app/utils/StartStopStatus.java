/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.bigjimnetwork.testxmlrpc;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bigjim
 */
public class StartStopStatus {
    int status;
    String description;
    String name;
    String group;

    public StartStopStatus(HashMap startStatus) {
        this.status = (Integer) startStatus.get("status");
        this.description = (String) startStatus.get("description");
        this.name = (String) startStatus.get("name");
        this.group = (String) startStatus.get("group");
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "StartStatus{" + "status=" + status + ", description=" + description + ", name=" + name + ", group=" + group + '}';
    }
    
}
