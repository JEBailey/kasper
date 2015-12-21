package org._24601.kasper.core;

import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.logging.Logger;

import javax.script.Bindings;
import javax.script.ScriptContext;

import org._24601.kasper.api.Parser;

public class KasperContext implements ScriptContext {

	private Parser parser;

	public static String globalScopeId = "_GLOBAL_SCOPE";

	public static String engineScopeID = "_ENGING_SCOPE";

	private KasperBindings bindings;

	private static final String output = "__outputstream";

	private static final Logger log = Logger.getLogger(KasperContext.class
			.getName());

	public KasperContext() {
		this.bindings = new KasperBindings();
		bindings.put(output, System.out);
	}

	public KasperContext(ScriptContext context) {
		// TODO Auto-generated constructor stub
	}


	public Parser getParser() {
		return parser;
	}

	public KasperBindings getScope() {
		return bindings;
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
		return bindings.put(key, value);
	}

	public Object get(String key) {
		return bindings.get(key);
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
			this.bindings.put(engineScopeID, bindings);
			break;
		case GLOBAL_SCOPE:
			this.bindings.put(globalScopeId, bindings);
			break;
		default:
			throw new IllegalArgumentException("Invalid scope value.");
		}
	}

	@Override
	public Bindings getBindings(int scope) {
		switch (scope) {
		case ENGINE_SCOPE:
			return (Bindings) this.bindings.get(engineScopeID);
		case GLOBAL_SCOPE:
			return (Bindings) this.bindings.get(globalScopeId);
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
			Bindings bindings = (Bindings) this.bindings.get(globalScopeId);
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
		// TODO Auto-generated method stub
		return null;
	}

}
