 package org._24601.kasper.type;

import org._24601.kasper.Scope;
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

	private Scope context;

	public Reference(Object key, Scope context) {
		this.key = key;
		this.context = context;
	}

	public Object getKey() {
		return key;
	}

	@SuppressWarnings("unchecked")
	public <R> R getValue(Class<R> klass) throws KasperException {
		return (R) context.eval(key, klass);
	}

	public Object getValue() throws KasperException {
		return context.eval(key);
	}

	public void setValue(Object value) {
		this.context.put(key.toString(),value);
	}

	public void createChildScope() {
		this.context= context.createChildScope();
	}

	public Object evaluate() throws KasperException {
		return context.eval(key);
	}

}
