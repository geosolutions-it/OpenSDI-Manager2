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
package it.getosolutions.opensdi2.workflow.transform.spel;

import it.getosolutions.opensdi2.workflow.transform.PreBuilder;
import it.getosolutions.opensdi2.workflow.transform.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.expression.Expression;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;


/**
 * The <SpelTransformer> is a <Transformer> that converts objects into
 * other using a map of rules and the Spel Expressions.
 * See {@linktourl http://docs.spring.io/spring/docs/4.0.x/spring-framework-reference/html/expressions.html}
 * 
 * The "rules" object (a <Map> of <String>,<String> that represents the
 * **outputExpression** and the **inputExpression**) to 
 * produce the result.
 * In both **outputExpression** and **inputExpression** you can use 
 * spEL language to create the input value from input object and 
 * output attribute to write to.
 * Basically the output object properties (identified by the outputExpression)
 * will be populated, using the transform method, with values generated
 * from the outputExpressions, using the argument of the tranform object as 
 * context. You can Set <PropertyAccessor> objects for input and output objects
 * Example: 
 * input object :
 * {
 * 	att1:1,
 *  att2:"attribute2"
 * }
 * rules:
 * {
 * 	"b1.nestedobj" : "'hello'",
 *  "b2": "att1 +5",
 *  "b3": "att2 + ' other stuff'"
 * }
 * result:
 * {
 * 	b1:{
 * 	nestedobj : "hello",
 *  }
 *  b2: 6,
 *  b3: "attribute2 other stuff"
 * }
 * or something like that, depending of the output object
 * @author Lorenzo Natali
 *
 */
public class SpelTransformer<SOURCETYPE,DESTTYPE> implements Transformer<SOURCETYPE,DESTTYPE>{
	public static SpelExpressionParser expressionParser = new SpelExpressionParser();

	
	private Map<String,String> rules;
	private List<PropertyAccessor> inputaccessors = new ArrayList<PropertyAccessor>();
	private List<PropertyAccessor> outputaccessors = new ArrayList<PropertyAccessor>();
	private PreBuilder<DESTTYPE> outputPreBuilder;


	private DESTTYPE outputObject;
	
	/**
	 * get the output object
	 * @return
	 */
	public DESTTYPE getOutputObject() {
		return outputObject;
	}
	/**
	 * Set the output object
	 * @param outputObject
	 */
	public void setOutputObject(DESTTYPE outputObject) {
		this.outputObject = outputObject;
	}

	/**
	 * Get a builder to initialize the output object
	 * @return
	 */
	public PreBuilder<DESTTYPE> getOutputPreBuilder() {
		return outputPreBuilder;
	}

	/**
	 * Set a builder to initialize the output object
	 * @param outputPreBuilder
	 */
	public void setOutputPreBuilder(PreBuilder<DESTTYPE> outputPreBuilder) {
		this.outputPreBuilder = outputPreBuilder;
	}

	/**
	 * Set the rules map
	 * @param map a map of "outExpression","inputExpression"
	 */
	public void setRules(Map<String, String> map) {
		this.rules = map;
	}
	/**
	 * get rules map
	 * @return the map of rules
	 */
	public Map<String, String> getRules() {
		return rules;
	}

	/**
	 * Get the rules map
	 * @return
	 */
	public List<PropertyAccessor> getInputaccessors() {
		return inputaccessors;
	}

	/**
	 * Set <PropertyAccessors> for input object
	 * @param inputaccessors
	 */
	public void setInputaccessors(List<PropertyAccessor> inputaccessors) {
		this.inputaccessors = inputaccessors;
	}

	/**
	 * Get <PropertyAccessors> for output object
	 * @return
	 */
	public List<PropertyAccessor> getOutputaccessors() {
		return outputaccessors;
	}

	/**
	 * Set <PropertyAccessors> for output object
	 * @param inputaccessors
	 */
	public void setOutputaccessors(List<PropertyAccessor> outputaccessors) {
		this.outputaccessors = outputaccessors;
	}

	@Override
	public DESTTYPE transform(SOURCETYPE source)
			throws IllegalArgumentException {
		
		//create the output object
		DESTTYPE output = null;
		if(outputObject==null && outputPreBuilder != null){
			output = outputPreBuilder.build(rules);
		}else if(outputPreBuilder != null){
			output = outputPreBuilder.build(outputObject,rules);
		}else if (outputObject!=null){
			output = outputObject;
		}else{
			throw new IllegalArgumentException("no outputObject or outPrebuilder provided");
		}
		
		//create evaluation Context
		StandardEvaluationContext inputEvaluationContext = new StandardEvaluationContext(source);
		StandardEvaluationContext outputevaluationContext = new StandardEvaluationContext(output);
		//set property accessors for input and output
		inputEvaluationContext.setPropertyAccessors(inputaccessors);
		outputevaluationContext.setPropertyAccessors(outputaccessors);
		if(rules !=null){
			Set<String> attributes = rules.keySet();
			
			//parse rules to create output
			for(String attribute : attributes){
				String expression = rules.get(attribute);
				//create expressions for in and out
				Expression conversionExpression = expressionParser.parseExpression(expression);
				Expression outputAttribute = expressionParser.parseExpression(attribute);
				//evaluate input value
				Object value = conversionExpression.getValue(inputEvaluationContext); //TODO create a second evaulationContext for output
				//set the attribute value using the output context
				outputAttribute.setValue(outputevaluationContext,value);
			}
		}
		return output;
		
	}

	
		
}
