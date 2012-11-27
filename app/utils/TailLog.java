/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.bigjimnetwork.testxmlrpc;

/**
 *
 * @author bigjim
 */
public class TailLog {
    String bytes;
    int offset;
    boolean overflow;

    public TailLog(Object[] tailLog) {
        this.bytes = (String) tailLog[0];
        this.offset = (Integer) tailLog[1];
        this.overflow = (Boolean) tailLog[2];
    }

    public String getBytes() {
        return bytes;
    }

    public int getOffset() {
        return offset;
    }

    public boolean isOverflow() {
        return overflow;
    }

    @Override
    public String toString() {
        return "TailLog{" + "bytes=" + bytes + ", offset=" + offset + ", overflow=" + overflow + '}';
    }
    
}
