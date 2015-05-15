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

import java.util.List;
/**
 * Container of a sequence of actions to execute in order
 * @author lorenzo
 *
 */
public class ActionSequence extends BaseAction {
	private List<ActionBlock> actions;
	@Override
	public void executeAction(WorkflowContext ctx) throws WorkflowException {
		if(actions!=null){
			for(ActionBlock a : actions){
				a.execute(ctx);
			}
		}
	}
	public List<ActionBlock> getActions() {
		return actions;
	}
	public void setActions(List<ActionBlock> actions) {
		this.actions = actions;
	}
	
	

}
