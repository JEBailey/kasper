package org._24601.kasper.type;

import org._24601.kasper.api.Executable;
import org._24601.kasper.core.KasperBindings;
import org._24601.kasper.core.KasperContext;
import org._24601.kasper.error.KasperException;

public class ExternalResolver implements Executable {

	private String value;

	public ExternalResolver(String value) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(KasperBindings scope, Statement statement) throws KasperException {
		if (statement.size() != 0){
			//throw exception
		}
		KasperBindings bindings = (KasperBindings)scope.get(KasperContext.engineScopeID);
		return bindings.getValue(value);
	}

}
