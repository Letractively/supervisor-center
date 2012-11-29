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

import java.util.HashMap;

/**
 * <p>Process start stop status constructed from xmlrpc HashMap type result</p> 
 * 
 * @author Cédric Sougné
 */
public class StartStopStatus {
	int status;
	String description;
	String name;
	String group;

	/**
	 * <p>Main constructor</p>
	 * 
	 * @param startStatus
	 */
	public StartStopStatus(HashMap startStatus) {
		this.status = (Integer) startStatus.get("status");
		this.description = (String) startStatus.get("description");
		this.name = (String) startStatus.get("name");
		this.group = (String) startStatus.get("group");
	}

	/**
	 * <p>Get status description</p>
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <p>Get status process group name</p>
	 * 
	 * @return group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * <p>Get status process name</p>
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Get status value</p>
	 * 
	 * @return status
	 */
	public int getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StartStatus{" + "status=" + status + ", description="
				+ description + ", name=" + name + ", group=" + group + '}';
	}

}
