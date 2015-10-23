package org._24601.kasper.type;

import javax.script.Bindings;

import org._24601.kasper.Interpreter;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.core.KasperBindings;
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

	private KasperBindings bindings;

	public Reference(Object key, KasperBindings scope) {
		this.key = key;
		this.bindings = scope;
	}

	public Object getKey() {
		return key;
	}

	@SuppressWarnings("unchecked")
	public <R> R getValue(Class<R> klass) throws KasperException {
		return (R) bindings.get(klass, key);
	}

	public Object getValue() {
		return bindings.get(key.toString());
	}

	public void setValue(Object value) {
		this.bindings.put(key.toString(), value);
	}
			
	/**
	 * Inserts a new value into the original Scope for the key that this
	 * reference is associated with
	 * 
	 * @param value
	 * @return inserted value
	 */
	public Object updateValue(Object value) {
		return this.bindings.update(key.toString(), value);
	}

	/**
	 * Inserts a new value into the current Scope for the key that this
	 * reference is associated with. If the original key is associated with a
	 * parent Scope then this will create a new key value pair in the currently
	 * active Scope
	 * 
	 * @param value
	 * @return inserted value
	 */
	public Object put(Object value) {
		return bindings.put(key.toString(), value);
	}

	public void createChildScope() {
		this.bindings= this.bindings.createChildScope();
	}

	public Object evaluate() throws KasperException {
		Object result = bindings.getValue(key);
		if (result instanceof ListProvider) {
			return Interpreter.process(bindings, (ListProvider) result);
		}
		return result;
	}
	
	public Bindings getBindings(){
		return (Bindings)this.bindings;
	}

}
