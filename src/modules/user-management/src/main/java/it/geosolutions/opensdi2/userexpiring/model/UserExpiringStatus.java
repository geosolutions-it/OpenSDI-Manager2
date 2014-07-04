/*
 *  OpenSDI Manager
 *  Copyright (C) 2014 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package it.geosolutions.opensdi2.userexpiring.model;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

/**
 * Modelling object for task execution status
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 *
 */
public class UserExpiringStatus {
	private String lastRun;
	private ExecutionState lastExecutionOutCome;
	private String Message;
	private TaskState status;
	/**
	 * The size of the log buffer
	 */
	private static final int BUFFER_SIZE = 30;
	private CircularFifoBuffer expiredUsersLog = new CircularFifoBuffer(BUFFER_SIZE);
	
	/**
	 * Add an element to the log
	 * @param e
	 */
	public void addLog(ExpireLogElement e){
		expiredUsersLog.add(e);
	}
	
	public TaskState getStatus() {
		return status;
	}
	public void setStatus(TaskState status) {
		this.status = status;
	}
	public String getLastRun() {
		return lastRun;
	}
	public void setLastRun(String lastRun) {
		this.lastRun = lastRun;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	
	/**
	 * @return the lastExecutionEsit
	 */
	public ExecutionState getLastExecutionOutCome() {
		return lastExecutionOutCome;
	}
	/**
	 * @param lastExecutionEsit the lastExecutionEsit to set
	 */
	public void setLastExecutionOutCome(ExecutionState lastExecutionEsit) {
		this.lastExecutionOutCome = lastExecutionEsit;
	}
	/**
	 * @return the expiredUsersLog
	 */
	public CircularFifoBuffer getExpiredUsersLog() {
		return expiredUsersLog;
	}
	/**
	 * @param expiredUsersLog the expiredUsersLog to set
	 */
	public void setExpiredUsersLog(CircularFifoBuffer expiredUsersLog) {
		this.expiredUsersLog = expiredUsersLog;
	}
	
	
}
