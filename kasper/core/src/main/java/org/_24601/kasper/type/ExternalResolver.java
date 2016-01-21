package org._24601.kasper.type;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.script.ScriptContext;

import org._24601.kasper.Scope;
import org._24601.kasper.api.Executable;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.api.ListProviderVisitor;
import org._24601.kasper.error.KasperException;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

public class ExternalResolver implements Executable, ListProvider {

	private String key = "";

	private JexlExpression expression;

	private JexlEngine jexl;

	public ExternalResolver(String value) {
		this.jexl = new JexlBuilder().create();
		this.key = value;
		int index = value.indexOf('.');
		if (index > 0) {
			this.key = value.substring(0, index);
			this.expression = jexl.createExpression(value);

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
	public Object execute(Scope scope, List<?> list) throws KasperException {

		ScriptContext cxt = (ScriptContext) scope.getAttribute("_context");
		Object reply = scope.eval(key);

		if (reply instanceof Undefined) {
			reply = cxt.getAttribute(key);
		}

		JexlContext jc = new MapContext();
		jc.set(key, reply);

		if (expression != null) {
			reply = expression.evaluate(jc);
		}

		if (list.size() < 2) {
			return reply;
		}
		StringBuilder sb = new StringBuilder();

		List<?> sublist = list.subList(1, list.size());

		if (reply instanceof Boolean) {
			if (((Boolean) reply).booleanValue()) {
				return sb.append(scope.eval(sublist.get(0), true));
			}
		}
		
		if (reply.getClass().isArray()) {
			for (Object item : (Object[]) reply) {
				Scope childScope = scope.createChildScope();
				childScope.put("this", item);
				sb.append(childScope.eval(sublist.get(0)));
			}
		}
		
		if (reply instanceof Iterable) {
			reply = ((Iterable<?>)reply).iterator();
		}
		
		
		if (reply instanceof Iterator){
			Iterator<?> collection = (Iterator<?>) reply;

			while (collection.hasNext()) {
				Scope childScope = scope.createChildScope();
				childScope.put("this", collection.next());
				sb.append(childScope.eval(sublist.get(0)));
			}
		}

		return sb.toString();
	}

	@Override
	public Object accept(ListProviderVisitor visitor) throws KasperException {
		return visitor.apply(Arrays.asList(this));
	}

}
