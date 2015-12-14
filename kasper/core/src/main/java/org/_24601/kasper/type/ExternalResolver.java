package org._24601.kasper.type;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

import org._24601.kasper.Interpreter;
import org._24601.kasper.api.Executable;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.api.ListProviderVisitor;
import org._24601.kasper.core.KasperBindings;
import org._24601.kasper.core.KasperContext;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.error.KasperRuntimeException;

public class ExternalResolver implements Executable, ListProvider {

	private String key = "";

	private OgnlContext context = new OgnlContext();

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
	public Object execute(KasperBindings scope, List<Object> list)
			throws KasperException {
		KasperBindings bindings = (KasperBindings) scope
				.get(KasperContext.engineScopeID);
		
		Object reply = bindings.get(key);
		
		// if bindings don't reference the key,
		// obtain from scope
		if (reply == null) {
			reply = scope.get(key);
		}
		
		
		if (expression != null) {
			try {
				reply = Ognl.getValue(expression, context, reply);
			} catch (OgnlException e) {
				throw new KasperRuntimeException(e.getMessage());
			}
		}
		
		if (list.size() < 2){
			return reply;
		}

		List<Object> sublist = list.subList(1, list.size());
		
		if (reply instanceof Boolean) {
			if (((Boolean) reply).booleanValue()) {
				return scope.getValue(sublist.get(0), true);
			}
		}

		if (reply instanceof Collection<?>) {
			Iterator<?> collection = ((Collection<?>) reply).iterator();
			
			while (collection.hasNext()) {
				KasperBindings childScope = scope.createChildScope();
				childScope.put("this", collection.next());
				reply = scope.getValue(reply);
			}
			return reply;
		}

		if (reply.getClass().isArray()) {
			for (Object item : Arrays.asList(reply)) {
				KasperBindings childScope = scope.createChildScope();
				childScope.put("this", item);
				reply = scope.getValue(reply);
			}
		}

		return reply;
	}

	@Override
	public Object accept(ListProviderVisitor visitor) throws KasperException {
		return visitor.apply(Arrays.asList(this));
	}

}
