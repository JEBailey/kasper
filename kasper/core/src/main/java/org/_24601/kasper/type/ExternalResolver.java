package org._24601.kasper.type;

import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Executable;
import org._24601.kasper.core.KasperBindings;
import org._24601.kasper.core.KasperContext;
import org._24601.kasper.error.KasperException;

public class ExternalResolver implements Executable, Collector {

	private List<Object> items = new ArrayList<>();

	@Override
	public Object execute(KasperBindings scope, Statement statement) throws KasperException {
		if (statement.size() != 0){
			//throw exception
		}
		KasperBindings bindings = (KasperBindings)scope.get(KasperContext.engineScopeID);
		return bindings.getValue(items.get(0));
	}

	@Override
	public boolean add(Object token) {
		return items.add(token);
	}

	@Override
	public void addEOL() {
	}

	@Override
	public int getLineNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean invokeEndOfStatement() {
		return false;
	}

	@Override
	public Object get() {
		return this;
	}

}
