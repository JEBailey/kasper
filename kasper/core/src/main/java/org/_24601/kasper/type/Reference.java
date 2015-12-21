 package org._24601.kasper.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.script.Bindings;
import javax.script.ScriptContext;

import org._24601.kasper.Interpreter;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.core.KasperBindings;
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
		this.context.put(key.toString(), value);
	}
			
	/**
	 * Inserts a new value into the original Scope for the key that this
	 * reference is associated with
	 * 
	 * @param value
	 * @return inserted value
	 */
	public Object updateValue(Object value) {
		return this.context.update(key.toString(), value);
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
		return context.put(key.toString(), value);
	}

	public void createChildScope() {
		this.context= this.context.createChildScope();
	}

	public Object evaluate() throws KasperException {
		Object result = context.getValue(key);
		if (result instanceof ListProvider) {
			return Interpreter.process(context, (ListProvider) result);
		}
		return result;
	}
	
	public Bindings getBindings(){
		return (Bindings)this.context;
	}
	
	/**
	 * Returns the associated object or, if the object is a statement, evaluates
	 * the statement and returns the response. If no value is found attempts to
	 * return the default value if that has been defined
	 * 
	 * @param object
	 * @return
	 * @throws KasperException
	 */
	private Object getValue(final Object object, boolean useDefault) throws KasperException {
		Object response = object;
		if (response instanceof Atom) {
			response = context.getAttribute(object.toString());
			if (response == null) {
				return context.getAttribute("_default");
			}
		}
		if (response instanceof Statement) {
			response = new Interpreter().process(context, (ListProvider) response);
		}
		
		return response;
	}
	
	/**
	 * Attempts to resolve the input object to meet the requested type
	 * 
	 * 
	 * @param requestedType
	 * @param object
	 * @return
	 * @throws KasperException
	 */
	private Object get(Type requestedType, Object object) throws KasperException {
		if (requestedType instanceof ParameterizedType) {
			return get(((ParameterizedType) requestedType).getRawType(), object);
		}

		if (object instanceof Statement) {
			return get(requestedType,
					new Interpreter().process(context, (Statement) object));
		}

		if (object instanceof Atom) {
			if (requestedType != Atom.class) {
				return get(requestedType, this.getValue(object));
			}
		}

		final Class<? extends Object> klass = object.getClass();
		if (((Class<?>) requestedType).isAssignableFrom(klass)) {
			return object;
		}

		return null;
	}
	
	/**
	 * Returns the associated object or, if the object is a statement, evaluates
	 * the statement and returns the response
	 * 
	 * @param object
	 * @return
	 * @throws KasperException
	 */
	private Object getValue(final Object object) throws KasperException {
		Object response = object;
		if (response instanceof Atom) {
			response = get(object.toString());
		}
		if (response instanceof ListProvider) {
			response = new Interpreter().process(context, (ListProvider) response);
		}
		if (response == null) {
			return Undefined.getInstance();
		}
		return response;
	}


}
