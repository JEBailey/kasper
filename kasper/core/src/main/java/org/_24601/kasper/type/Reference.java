 package org._24601.kasper.type;

import javax.script.Bindings;
import javax.script.ScriptContext;

import org._24601.kasper.core.Util;
import org._24601.kasper.error.KasperException;

/**
 * Represents the relationship between an Object and it's un-evaluated value.
 * This provides access to an item without having it evaluated.
 * 
 * 
 * @author je bailey
 */
public class Reference {

	
	private Object key;

	private ScriptContext context;

	public Reference(Object key, ScriptContext context) {
		this.key = key;
		this.context = context;
	}

	public Object getKey() {
		return key;
	}

	@SuppressWarnings("unchecked")
	public <R> R getValue(Class<R> klass) throws KasperException {
		return (R) Util.eval(context, key, klass);
	}

	public Object getValue() throws KasperException {
		return Util.eval(context , key);
	}

	public void setValue(Object value) {
		this.context.setAttribute(key.toString(),value, ScriptContext.ENGINE_SCOPE);
	}

	public void createChildScope() {
		this.context= Util.createChildScope(context);
	}

	public Object evaluate() throws KasperException {
		return Util.eval(context, key);
	}
	
	public Bindings getBindings(){
		return (Bindings)this.context;
	}

}
