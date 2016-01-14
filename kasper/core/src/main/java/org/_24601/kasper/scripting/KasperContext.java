package org._24601.kasper.scripting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;


public class KasperContext extends SimpleScriptContext {

	private Map<Integer,Bindings> bindings;

	private static final Logger log = Logger.getLogger(KasperContext.class
			.getName());

	public KasperContext() {
		bindings = new HashMap<Integer, Bindings>(3);
		KasperBindings temp = new KasperBindings();
		bindings.put(ScriptContext.GLOBAL_SCOPE, temp);
		bindings.put(ScriptContext.ENGINE_SCOPE, temp.createChildScope());
	}


	@Override
	public void setBindings(Bindings bindings, int scope) {
		switch (scope) {
		case ENGINE_SCOPE:
			if (bindings == null) {
				throw new NullPointerException("Engine scope cannot be null.");
			}

			break;
		default:
			//throw new IllegalArgumentException("Invalid scope value.");
		}
		Bindings current = getBindings(scope);
		if (current == null){
			current = new KasperBindings();
		}
		if (current instanceof KasperBindings){
			current.putAll(bindings);
			bindings = current;
		}
		this.bindings.put(scope, bindings);
	}

	@Override
	public Bindings getBindings(int scope) {
		switch (scope) {
		case ENGINE_SCOPE:
		case GLOBAL_SCOPE:
			return this.bindings.get(scope);
		default:
			throw new IllegalArgumentException("Illegal scope value.");
		}
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		Bindings bindings = getBindings(scope);
		if (bindings != null) {
			bindings.put(name, value);
		}
	}

	@Override
	public Object getAttribute(String name, int scope) {
		Bindings bindings = getBindings(scope);
		if (bindings != null) {
			return bindings.get(name);
		}
		return null;
	}

	@Override
	public Object removeAttribute(String name, int scope) {
		Bindings bindings = getBindings(scope);
		if (bindings != null) {
			return bindings.remove(name);
		}
		return null;
	}

	@Override
	public Object getAttribute(String name) {
		Object reply = getAttribute(name, ENGINE_SCOPE);
		if (reply == null) {
			Bindings bindings = (Bindings) this.bindings.get(GLOBAL_SCOPE);
			if (bindings != null) {
				reply = bindings.get(name);
			}
		}
		return reply;
	}

	@Override
	public int getAttributesScope(String name) {
		Object temp = getAttribute(name, ENGINE_SCOPE);
		if (temp != null) {
			return ENGINE_SCOPE;
		}
		temp = getAttribute(name, GLOBAL_SCOPE);
		if (temp != null) {
			return GLOBAL_SCOPE;
		}
		return -1;
	}


	@Override
	public List<Integer> getScopes() {
		return Arrays.asList(ENGINE_SCOPE,GLOBAL_SCOPE);
	}

}
