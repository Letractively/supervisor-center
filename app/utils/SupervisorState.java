/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.bigjimnetwork.testxmlrpc;

import java.util.Map;

/**
 *
 * @author bigjim
 */
class SupervisorState {
    int statecode;
    String statename;

    public SupervisorState(Map supervisorState) {
        this.statecode = (Integer) supervisorState.get("statecode");
        this.statename = (String) supervisorState.get("statename");
    }

    public int getStatecode() {
        return statecode;
    }

    public String getStatename() {
        return statename;
    }

    @Override
    public String toString() {
        return "SupervisorState{" + "statecode=" + statecode + ", statename=" + statename + '}';
    }
    
    
}
