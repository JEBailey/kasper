package org._24601.kasper.core;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;

import org._24601.kasper.api.Parser;

public class KasperContext implements ScriptContext {

	private Parser parser;

	private Map<Integer,Bindings> bindings;

	private static final String output = "__outputstream";

	private static final Logger log = Logger.getLogger(KasperContext.class
			.getName());

	public KasperContext() {
		bindings = new HashMap<Integer, Bindings>(3);
		KasperBindings temp = new KasperBindings();
		bindings.put(ScriptContext.GLOBAL_SCOPE, temp);
		bindings.put(ScriptContext.ENGINE_SCOPE, temp.createChildScope());
	}

	public KasperContext(ScriptContext context) {
		// TODO Auto-generated constructor stub
	}


	public Parser getParser() {
		return parser;
	}


	/**
	 * facade to add a key value pair to the underlying scope
	 * 
	 * @param key
	 *            A string representation
	 * @param value
	 * @return
	 */
	public Object put(String key, Object value) {
		setAttribute(key, value, ScriptContext.ENGINE_SCOPE);
		return value;
	}

	public Object get(String key) {
		return getAttribute(key, ENGINE_SCOPE);
	}

	public boolean containsKey(String key) {
		return bindings.containsKey(key);
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
			throw new IllegalArgumentException("Invalid scope value.");
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
	public Writer getWriter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Writer getErrorWriter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWriter(Writer writer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setErrorWriter(Writer writer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Reader getReader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReader(Reader reader) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Integer> getScopes() {
		return Arrays.asList(ENGINE_SCOPE,GLOBAL_SCOPE);
	}

}
