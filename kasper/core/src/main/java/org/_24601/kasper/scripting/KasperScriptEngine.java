package org._24601.kasper.scripting;
import java.io.Reader;
import java.io.Writer;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;

import org._24601.kasper.Interpreter;
import org._24601.kasper.Scope;

public class KasperScriptEngine  implements ScriptEngine {

    protected Scope scope;
    
    protected ScriptContext scriptContext;
    
    protected ScriptEngineFactory factory;
    
    private Interpreter interpreter;

    public KasperScriptEngine(KasperScriptEngineFactory factory) {
        this();
        this.factory = factory;
    }
    
    public KasperScriptEngine() {
        interpreter = new Interpreter();
        scope = new Scope();
        scriptContext = new SimpleScriptContext();
        scope.put("_context", scriptContext);
    }

    /**
     * Creates a new instance using the specified <code>Bindings</code> as the
     * <code>ENGINE_SCOPE</code> <code>Bindings</code> in the protected <code>context</code> field.
     *
     * @param bindings The specified <code>Bindings</code>.
     * @throws NullPointerException if n is null.
     */
    public KasperScriptEngine(Bindings bindings) {
        this();
        if (bindings == null) {
            throw new NullPointerException("bindings is null");
        }
        scriptContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
    }

    /**
     * Sets the value of the protected <code>context</code> field to the specified
     * <code>ScriptContext</code>.
     *
     * @param ctxt The specified <code>ScriptContext</code>.
     * @throws NullPointerException if ctxt is null.
     */
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
    public ScriptContext getContext() {
        return scriptContext;
    }

    /**
     * Returns the <code>Bindings</code> with the specified scope value in
     * the protected <code>context</code> field.
     *
     * @param scope The specified scope
     *
     * @return The corresponding <code>Bindings</code>.
     *
     * @throws IllegalArgumentException if the value of scope is
     * invalid for the type the protected <code>context</code> field.
     */
    public Bindings getBindings(int scope) {
    	return scriptContext.getBindings(scope);
    }

    /**
     * Sets the <code>Bindings</code> with the corresponding scope value in the
     * <code>context</code> field.
     *
     * @param bindings The specified <code>Bindings</code>.
     * @param scope The specified scope.
     *
     * @throws IllegalArgumentException if the value of scope is
     * invalid for the type the <code>context</code> field.
     * @throws NullPointerException if the bindings is null and the scope is
     * <code>ScriptContext.ENGINE_SCOPE</code>
     */
    public void setBindings(Bindings bindings, int scope) {
    	scriptContext.setBindings(bindings, scope);
    }

    public void put(String key, Object value) {
        Bindings bindings = getBindings(ScriptContext.ENGINE_SCOPE);
        if (bindings != null) {
            bindings.put(key, value);
        }
    }

    /**
     * Gets the value for the specified key in the <code>ENGINE_SCOPE</code> of the
     * protected <code>context</code> field.
     *
     * @return The value for the specified key.
     *
     * @throws NullPointerException if key is null.
     * @throws IllegalArgumentException if key is empty.
     */
    public Object get(String key) {
        Bindings bindings = getBindings(ScriptContext.ENGINE_SCOPE);
        if (bindings != null) {
            return bindings.get(key);
        }
        return null;
    }

    public Object eval(Reader reader, Bindings bindings ) throws ScriptException {
        setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        return eval(reader, scriptContext);
    }

    public Object eval(String script, Bindings bindings) throws ScriptException {
    	setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        return eval(script , scriptContext);
    }


    public Object eval(Reader reader) throws ScriptException {
        return eval(reader, scriptContext);
    }

    public Object eval(String script) throws ScriptException {
        return eval(script, scriptContext);
    }


	@Override
	public Object eval(String script, ScriptContext context) throws ScriptException {
		scope.put("_context", context);
		try {
			Object response = interpreter.process(scope, script);
			Writer writer = context.getWriter();
			if (writer != null){
				writer.write(toString(response));
			}
			return response;
		} catch (Throwable e) {
			throw new ScriptException((Exception)e);
		}
	}

	private String toString(Object response) {
		if (response instanceof String){
			return (String)response;
		}
		return response.toString();
	}

	@Override
	public Object eval(Reader reader, ScriptContext context) throws ScriptException {
		scope.put("_context", context);
		try {
			Object response = interpreter.process(scope, reader);
			Writer writer = context.getWriter();
			if (writer != null){
				writer.write(toString(response));
			}
			return response;
		} catch (Throwable e) {
			throw new ScriptException(new Exception(e));
		}
	}



	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	@Override
	public ScriptEngineFactory getFactory() {
		if (this.factory == null){
			this.factory =  new KasperScriptEngineFactory();
		}
		return this.factory;
	}
}
