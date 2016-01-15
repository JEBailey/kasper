package org._24601.kasper.type;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.script.ScriptContext;

import org._24601.kasper.Scope;
import org._24601.kasper.api.Executable;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.api.ListProviderVisitor;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.error.KasperRuntimeException;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class ExternalResolver implements Executable, ListProvider {

	private String key = "";

	private Object expression;

	public ExternalResolver(String value) {
		this.key = value;
		int index = value.indexOf('.');
		if (index > 0) {
			this.key = value.substring(0, index);
			try {
				this.expression = Ognl.parseExpression(value
						.substring(index + 1));
			} catch (OgnlException e) {
				throw new UnsupportedOperationException();
			}
		}

	}

	/**
	 * Execute is called when the binding in the form of ${foo} is the first
	 * object in the command line. 
	 * 
	 * Given the type of object that is returned the rest of the line may be
	 * executed with some additional context.
	 * 
	 * 
	 */
	@Override
	public Object execute(Scope scope, List<?> list)
			throws KasperException {

		ScriptContext cxt = (ScriptContext)scope.getAttribute("_context");
		Object reply = scope.eval(key);
		
		if (reply instanceof Undefined){
			reply = cxt.getAttribute(key);
		}
		
		
		if (expression != null) {
			try {
				reply = Ognl.getValue(expression,new OgnlContext(), reply);
			} catch (OgnlException e) {
				throw new KasperRuntimeException(e.getMessage());
			}
		}
		
		if (list.size() < 2){
			return reply;
		}
		StringBuilder sb = new StringBuilder();
		
		List<?> sublist = list.subList(1, list.size());
		
		if (reply instanceof Boolean) {
			if (((Boolean) reply).booleanValue()) {
				return sb.append(scope.eval(sublist.get(0) , true));
			}
		}

		if (reply instanceof Collection<?>) {
			Iterator<?> collection = ((Collection<?>) reply).iterator();
			
			while (collection.hasNext()) {
				Scope childScope = scope.createChildScope();
				childScope.put("this", collection.next());
				sb.append(childScope.eval(sublist.get(0)));
			}
			return reply;
		}

		if (reply.getClass().isArray()) {
			for (Object item : (Object[])reply) {
				Scope childScope = scope.createChildScope();
				childScope.put("this", item);
				sb.append(childScope.eval(sublist.get(0)));
			}
		}

		return sb.toString() ;
	}

	@Override
	public Object accept(ListProviderVisitor visitor) throws KasperException {
		return visitor.apply(Arrays.asList(this));
	}

}
