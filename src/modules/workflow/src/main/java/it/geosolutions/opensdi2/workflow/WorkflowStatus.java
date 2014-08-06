/*
 *  OpenSDI Manager 2
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
package it.geosolutions.opensdi2.workflow;
/**
 * Status of the workflow to place in the execution context
 * This is only a draft
 * @author lorenzo
 *
 */
public class WorkflowStatus {
	public enum Status {
		WAITING,
		RUNNING,
		COMPLETED,
		FAILED
	}
	private int scheduledOperations =1;
	private int progress = 0;
	private Status currentStatus = Status.WAITING;
	private String errorMessage = null;
	private Throwable exception = null;
	
	public int getScheduledOperations() {
		return scheduledOperations;
	}

	public void setScheduledOperations(int scheduledOperations) {
		this.scheduledOperations = scheduledOperations;
	}

	public Status getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Status currentStatus) {
		this.currentStatus = currentStatus;
	}
	
	public void setFailed(String errorMessage, Throwable t) {
		currentStatus = Status.FAILED;
		this.errorMessage = errorMessage;
		this.exception = t;
	}

	public void setCompleted() {
		currentStatus = Status.COMPLETED;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public Throwable getException() {
		return exception;
	}
	
	
}
