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
 * <p>Supervisor state information constructed from xmlrpc Map type result</p> 
 * 
 * @author Cédric Sougné
 *
 */
class SupervisorState {
	int statecode;
	String statename;

	/**
	 * Construct object from xmlrpc result
	 * 
	 * @param supervisorState
	 */
	public SupervisorState(Map supervisorState) {
		this.statecode = (Integer) supervisorState.get("statecode");
		this.statename = (String) supervisorState.get("statename");
	}

	/**
	 * Get state code
	 * 
	 * @return State code
	 */
	public int getStatecode() {
		return statecode;
	}


	/**
	 * Get state name
	 * 
	 * @return State name
	 */
	public String getStatename() {
		return statename;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SupervisorState{" + "statecode=" + statecode + ", statename="
				+ statename + '}';
	}

}
