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

import it.geosolutions.opensdi2.workflow.WorkflowStatus.Status;

/**
 * Base Action with Context
 * @author lorenzo
 *
 */
public abstract class BaseAction implements ActionBlock {
	private BlockConfiguration configuration;

	private String id;
	
	private WorkflowStatus status = new WorkflowStatus(); 
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setConfiguration(BlockConfiguration config) {
		this.configuration = config;
		
	}
	@Override
	public BlockConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * Delegates real execution to the executeAction method, dealing with status
	 * handling.
	 * 
	 */
	@Override
	public final void execute(WorkflowContext ctx) throws
			WorkflowException {
		if(this.getId() != null) {
			ctx.getStatusElements().put(this.getId(), status);
		}
		status.setCurrentStatus(Status.RUNNING);
		try {
			executeAction(ctx);
			complete();
		} catch(Throwable t) {
			fail(t);
			throw new WorkflowException(t);
		}
	}

	/**
	 * To be defined by each extending class, implements the real action execution.
	 * 
	 * @param ctx
	 */
	protected abstract void executeAction(WorkflowContext ctx) throws WorkflowException;

	private void complete() {
		status.setCompleted();
	}

	protected void fail(Throwable t) {
		fail(null, t);
	}
	
	protected void fail(String message, Throwable t) {
		status.setFailed(message, t);
	}
	
}
