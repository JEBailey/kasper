package org._24601.kasper.scripting;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;

import org._24601.kasper.Scope;
import org._24601.kasper.fxc.Element;
import org._24601.kasper.parser.Node;
import org._24601.kasper.parser.NodesFactory;
import org._24601.kasper.parser.Parser;
import org._24601.kasper.parser.node.Visitor;

public class KasperScriptEngine implements ScriptEngine {

	protected Scope scope;

	protected ScriptContext scriptContext;

	protected ScriptEngineFactory factory;

	public KasperScriptEngine(KasperScriptEngineFactory factory) {
		this();
		this.factory = factory;
	}

	public KasperScriptEngine() {
		scope = new Scope();
		scriptContext = new SimpleScriptContext();
		scope.put("_context", scriptContext);
	}

	/**
	 * Creates a new instance using the specified <code>Bindings</code> as the
	 * <code>ENGINE_SCOPE</code> <code>Bindings</code> in the protected
	 * <code>context</code> field.
	 *
	 * @param bindings
	 *            The specified <code>Bindings</code>.
	 * @throws NullPointerException
	 *             if n is null.
	 */
	public KasperScriptEngine(Bindings bindings) {
		this();
		if (bindings == null) {
			throw new NullPointerException("bindings is null");
		}
		scriptContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
	}

	/**
	 * Sets the value of the protected <code>context</code> field to the
	 * specified <code>ScriptContext</code>.
	 *
	 * @param ctxt
	 *            The specified <code>ScriptContext</code>.
	 * @throws NullPointerException
	 *             if ctxt is null.
	 */
	@Override
	public void setContext(ScriptContext ctxt) {
		if (ctxt == null) {
			throw new NullPointerException("null context");
		}
		scriptContext = ctxt;
		scope.put("_context", ctxt);
	}

	/**
	 * Returns the value of the protected <code>context</code> field.
	 *
	 * @return The value of the protected <code>context</code> field.
	 */
	@Override
	public ScriptContext getContext() {
		return scriptContext;
	}

	/**
	 * Returns the <code>Bindings</code> with the specified scope value in the
	 * protected <code>context</code> field.
	 *
	 * @param scope
	 *            The specified scope
	 *
	 * @return The corresponding <code>Bindings</code>.
	 *
	 * @throws IllegalArgumentException
	 *             if the value of scope is invalid for the type the protected
	 *             <code>context</code> field.
	 */
	@Override
	public Bindings getBindings(int scope) {
		return scriptContext.getBindings(scope);
	}

	/**
	 * Sets the <code>Bindings</code> with the corresponding scope value in the
	 * <code>context</code> field.
	 *
	 * @param bindings
	 *            The specified <code>Bindings</code>.
	 * @param scope
	 *            The specified scope.
	 *
	 * @throws IllegalArgumentException
	 *             if the value of scope is invalid for the type the
	 *             <code>context</code> field.
	 * @throws NullPointerException
	 *             if the bindings is null and the scope is
	 *             <code>ScriptContext.ENGINE_SCOPE</code>
	 */
	@Override
	public void setBindings(Bindings bindings, int scope) {
		scriptContext.setBindings(bindings, scope);
	}

	@Override
	public void put(String key, Object value) {
		Bindings bindings = getBindings(ScriptContext.ENGINE_SCOPE);
		if (bindings != null) {
			bindings.put(key, value);
		}
	}

	/**
	 * Gets the value for the specified key in the <code>ENGINE_SCOPE</code> of
	 * the protected <code>context</code> field.
	 *
	 * @return The value for the specified key.
	 *
	 * @throws NullPointerException
	 *             if key is null.
	 * @throws IllegalArgumentException
	 *             if key is empty.
	 */
	@Override
	public Object get(String key) {
		Bindings bindings = getBindings(ScriptContext.ENGINE_SCOPE);
		if (bindings != null) {
			return bindings.get(key);
		}
		return null;
	}

	@Override
	public Object eval(Reader reader, Bindings bindings) throws ScriptException {
		setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		return eval(reader, scriptContext);
	}

	@Override
	public Object eval(String script, Bindings bindings) throws ScriptException {
		setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		return eval(script, scriptContext);
	}

	@Override
	public Object eval(Reader reader) throws ScriptException {
		return eval(reader, scriptContext);
	}

	@Override
	public Object eval(String script) throws ScriptException {
		return eval(script, scriptContext);
	}

	@Override
	public Object eval(String script, ScriptContext context) throws ScriptException {
		scope.put("_context", context);
		Element root = new org._24601.kasper.fxc.elements.Void();
		Visitor visitor = new Visitor();
		try {
			Parser parser = new Parser(new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8)),
					StandardCharsets.UTF_8.toString(), new NodesFactory());
			Node node = parser.Input();
			node.accept(visitor, root);
		} catch (Throwable e) {
			return e.toString();
		}
		return root.toString();
	}

	public static String toString(Reader reader) {
		StringBuilder out = new StringBuilder();
		char[] b = new char[4096];
		try {
			for (int n; (n = reader.read(b)) != -1;) {
				out.append(b, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toString();
	}

	@Override
	public Object eval(Reader reader, ScriptContext context) throws ScriptException {
		return eval(toString(reader), context);
	}

	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	@Override
	public ScriptEngineFactory getFactory() {
		if (this.factory == null) {
			this.factory = new KasperScriptEngineFactory();
		}
		return this.factory;
	}
}
