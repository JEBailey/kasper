package org._24601.kasper.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org._24601.kasper.Scope;
import org._24601.kasper.api.Executable;
import org._24601.kasper.error.KasperRuntimeException;

/**
 * MethodProxy is used to wrap an invocation of an executable that
 * is mapped to a specific java method.
 * 
 * 
 * @author je bailey
 *
 */
public class MethodProxy implements Executable {

	private Method method;

	private Object object;
	

	public MethodProxy(Method method, Object object) {
		this.method = method;
		this.object = object;
	}

	@Override
	public Object execute(Scope scope, List<?> list) {
		try {
			return method.invoke(object, scope, new ArgumentProvider(list));
		} catch (InvocationTargetException|IllegalAccessException ite) {
			throw new KasperRuntimeException(ite.getCause().toString());
		}
	}
	
	@Override
	public String toString() {
		return "COMMAND :: " + super.toString();
	}

}
