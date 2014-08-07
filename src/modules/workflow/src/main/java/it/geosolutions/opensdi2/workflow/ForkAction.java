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
import java.util.Map;

import org.springframework.expression.Expression;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
/**
 * Execute a list of actions if a condition is verified.
 * The condition is verified using the context and the inputPropertyAccessors 
 * passed as arguments.
 * The context is necessary to monitor also the status elements
 * @author lorenzo
 *
 */
public class ForkAction extends BaseAction {
	private static SpelExpressionParser expressionParser = new SpelExpressionParser();
	private Map<String,ActionBlock> branches;
	private List<PropertyAccessor> inputPropertyAccessors;
	
	@Override
	public void executeAction(WorkflowContext ctx) throws WorkflowException {
		StandardEvaluationContext inputEvaluationContext = new StandardEvaluationContext(ctx);
		inputEvaluationContext.setPropertyAccessors(inputPropertyAccessors);
		if(branches!=null){
			for(String rule : branches.keySet()){
				Expression conversionExpression = expressionParser.parseExpression(rule);
				
				boolean parseResult = conversionExpression.getValue(inputEvaluationContext,Boolean.class);
				if(parseResult){
					branches.get(rule).execute(ctx);
				}
			}
		}
		
	}
	/**
	 * get the branches
	 * @return
	 */
	public Map<String,ActionBlock> getBranches() {
		return branches;
	}
	/**
	 * Set the branches of the fork. 
	 * Each branch will be executed only
	 * if the rule, key of the map, returns true.
	 * @param branches a <Map> of rule,actionblock
	 */
	public void setBranches(Map<String,ActionBlock> branches) {
		this.branches = branches;
	}
	
	/**
	 * get the property accessor for the evaluation of the rules
	 * @return
	 */
	public List<PropertyAccessor> getInputPropertyAccessors() {
		return inputPropertyAccessors;
	}
	
	/**
	 * set the property accessor for the evaluation of the rules
	 * @param inputPropertyAccessors
	 */
	public void setInputPropertyAccessors(List<PropertyAccessor> inputPropertyAccessors) {
		this.inputPropertyAccessors = inputPropertyAccessors;
	}
	
	
	

}
