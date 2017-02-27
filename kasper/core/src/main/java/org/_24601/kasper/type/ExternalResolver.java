package org._24601.kasper.type;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.script.ScriptContext;

import org._24601.kasper.Scope;
import org._24601.kasper.api.Executable;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.api.ListProviderVisitor;
public class ExternalResolver implements Executable, ListProvider {

	private String key = "";

	public ExternalResolver(String value) {
		this.key = value;
		int index = value.indexOf('.');
		if (index > 0) {
			this.key = value.substring(0, index);

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
	public Object execute(Scope scope, List<?> list) {

		ScriptContext cxt = (ScriptContext) scope.get("_context");
		Object reply = scope.eval(key);


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
	public Object accept(ListProviderVisitor visitor) {
		return visitor.apply(Arrays.asList(this));
	}

}
