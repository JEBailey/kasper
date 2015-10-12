package org._24601.kasper.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org._24601.kasper.api.Executable;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.type.Statement;

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
	public Object execute(KasperBindings argumentScope, Statement statement) throws KasperException {
		try {
			return method.invoke(object, resolver.render(argumentScope, statement));
		} catch (InvocationTargetException ite) {
			KasperException exception = new KasperException(statement.startPos(),ite.getCause().toString());
			throw exception;
		} catch (IllegalAccessException e) {
			throw new KasperException(statement.startPos(),e.toString());
		} 
	}
	
	@Override
	public String toString() {
		return "COMMAND :: " + super.toString();
	}

}
