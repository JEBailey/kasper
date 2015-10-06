package org._24601.kasper.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org._24601.kasper.error.KasperException;
import org._24601.kasper.type.Statement;
/**
 * Provides a mechanism to resolve the parameters that
 * will be passed into a wrapped java method.
 * 
 * 
 * @author JE Bailey
 *
 */
public class Resolver  {
	
protected Type[] params;
	
	protected ParameterInfo[] info;
	
	public Resolver(Method method){
		this.params = method.getGenericParameterTypes();
		Annotation[][] annotations = method.getParameterAnnotations();
		info = new ParameterInfo[params.length];
		for (int i = 0;i < params.length ; i++){
			info[i] = new ParameterInfo(params[i], annotations[i]);
		}
	}
	

	/**
	 * 
	 * 
	 * 
	 * @param scope provides the available set of objects to work with
	 * @param statement is the executable statement
	 * @return
	 * @throws KasperException
	 */
	public Object[] render(KasperBindings scope, Statement statement) throws KasperException {
		// This is the argument array that will be passed in the method call
		Object[] arguments = new Object[info.length];
		// we're going to loop through the parameter information
		// i is the index for the parameter information
		// t is the index for the passed in tokens.
		int providedArgIndex = 0;
		// number of arguments minus the caller
		int providedArgSize = statement.size() - 1; 
		for (int storedParamIndex = 0; storedParamIndex < info.length; ++storedParamIndex) {
			ParameterInfo param = info[storedParamIndex];
			// first check to see if we've ran out of arguments
			// do we increment?
			providedArgIndex += param.incr();
			if (providedArgIndex > providedArgSize) {
				if (!param.isOptional()) {
					throw new KasperException(statement.startPos(),
							"incorrect number of arguments");
				}
			} else {
				// we have enough arguments. do we need them?
				arguments[storedParamIndex] = param.render(scope, statement, providedArgIndex);
				//if we were unable to obtain an object and the parameter was optional
				//lets move on
				if (param.isOptional() && arguments[storedParamIndex] == null){
					providedArgIndex -= param.incr();
				}
			}

		}
		return arguments;
	}

}
