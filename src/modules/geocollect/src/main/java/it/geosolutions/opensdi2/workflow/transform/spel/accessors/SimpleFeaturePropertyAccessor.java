package it.geosolutions.opensdi2.workflow.transform.spel.accessors;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class SimpleFeaturePropertyAccessor implements PropertyAccessor {

	@Override
	public boolean canRead(EvaluationContext ctx, Object target, String propertyName)
			throws AccessException {
		return target instanceof SimpleFeature;
	}

	@Override
	public boolean canWrite(EvaluationContext ctx, Object target, String propertyName)
			throws AccessException {
		if(target instanceof SimpleFeature) {
			SimpleFeature feature = (SimpleFeature) target;
			return feature.getFeatureType().getDescriptor(propertyName) != null;
		} 
		return false;
	}

	@Override
	public Class[] getSpecificTargetClasses() {
		return new Class[] {SimpleFeature.class, SimpleFeatureBuilder.class};
	}

	@Override
	public TypedValue read(EvaluationContext ctx, Object target, String propertyName)
			throws AccessException {
		if(target instanceof SimpleFeature) {
			return new TypedValue(((SimpleFeature)target).getAttribute(propertyName));
		}
		return null;
	}

	@Override
	public void write(EvaluationContext ctx, Object target, String propertyName,
			Object value) throws AccessException {
		if(target instanceof SimpleFeature) {
			SimpleFeature feature = (SimpleFeature) target;
			feature.setAttribute(propertyName, value);
		}
	}

}
