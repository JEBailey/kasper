package org._24601.kasper.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org._24601.kasper.Scope;
import org._24601.kasper.api.Executable;
import org._24601.kasper.error.KasperException;

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
	
	private Resolver resolver;	

	public MethodProxy(Method method, Object object) {
		this.method = method;
		this.object = object;
		this.resolver = new Resolver(method);
	}

	@Override
	public Object execute(Scope scope, List<?> list) throws KasperException {
		try {
			return method.invoke(object, resolver.render(scope, list));
		} catch (InvocationTargetException|IllegalAccessException ite) {
			KasperException exception = new KasperException(-1,ite.getCause().toString());
			throw exception;
		}
	}
	
	@Override
	public String toString() {
		return "COMMAND :: " + super.toString();
	}

}
