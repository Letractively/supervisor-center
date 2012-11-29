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

/**
 * <p>Tail logs object constructed from xmlrpc Object array type result</p> 
 * 
 * @author Cédric Sougné
 *
 */
public class TailLog {
	String bytes;
	int offset;
	boolean overflow;

	/**
	 * <p>Construct from xmlrpc Object array type result</p> 
	 * 
	 * @param tailLog
	 */
	public TailLog(Object[] tailLog) {
		this.bytes = (String) tailLog[0];
		this.offset = (Integer) tailLog[1];
		this.overflow = (Boolean) tailLog[2];
	}

	/**
	 * Get tail log output
	 * 
	 * @return bytes
	 */
	public String getBytes() {
		return bytes;
	}

	/**
	 * get offset to start reading from for next call
	 * 
	 * @return offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 *  If the total log size is greater than (offset + length), the overflow flag is set and the (offset) is automatically increased to position the buffer at the end of the log
	 * 
	 * @return true if overflow
	 */
	public boolean isOverflow() {
		return overflow;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TailLog{" + "bytes=" + bytes + ", offset=" + offset
				+ ", overflow=" + overflow + '}';
	}

}
