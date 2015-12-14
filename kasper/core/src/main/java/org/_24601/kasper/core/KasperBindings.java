/*
 * KasperBindings.java
 *
 * Created on September 26, 2003, 9:16 AM
 */

package org._24601.kasper.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.Bindings;

import org._24601.kasper.Interpreter;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.type.Atom;
import org._24601.kasper.type.Statement;
import org._24601.kasper.type.Undefined;

/**
 * Provides a wrapper around an enclosed Map which represents the items
 * available to the Interpreter at a particular level. Additionally implements
 * the <tt>Bindings</tt> interface
 * 
 * @author JE Bailey
 */
public class KasperBindings implements Bindings {

	/**
	 * parent scope
	 */
	private KasperBindings enclosingScope;

	/**
	 * objects existing in the current scope
	 */
	private Map<String, Object> content;

	
	/**
	 * Default creation process wrapping a <tt>ConcurrentHashMap</tt>
	 * 
	 */
	public KasperBindings() {
		content = new ConcurrentHashMap<String, Object>();
	}

	private KasperBindings(KasperBindings scope) {
		this();
		enclosingScope = scope;
	}

	/**
	 * Wrapper class over the default String key, Object value
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Object put(Atom key, Object value) {
		return put(key.toString(), value);
	}

	/**
	 * Place the key value mapping into the backing Map
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Object put(String key, Object value) {
		return content.put(key, value);
	}

	public KasperBindings createChildScope() {
		return new KasperBindings(this);
	}
	
	public KasperBindings getParentScope(){
		return enclosingScope;
	}

	/**
	 * Returns the associated object or, if the object is a statement, evaluates
	 * the statement and returns the response
	 * 
	 * @param object
	 * @return
	 * @throws KasperException
	 */
	public Object getValue(final Object object) throws KasperException {
		Object response = object;
		if (response instanceof Atom) {
			response = get(object.toString());
		}
		if (response instanceof ListProvider) {
			response = Interpreter.process(this, (ListProvider) response);
		}
		if (response == null) {
			return Undefined.getInstance();
		}
		return response;
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
	public Object getValue(final Object object, boolean useDefault) throws KasperException {
		Object response = object;
		if (response instanceof Atom) {
			response = get(object.toString());
			if (response == null) {
				return get("_default");
			}
		}
		if (response instanceof Statement) {
			response = Interpreter.process(this, (ListProvider) response);
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
	public Object get(Type requestedType, Object object) throws KasperException {
		if (requestedType instanceof ParameterizedType) {
			return get(((ParameterizedType) requestedType).getRawType(), object);
		}

		if (object instanceof Statement) {
			return get(requestedType,
					Interpreter.process(this, (Statement) object));
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
	 * Attempts to locate the key object in the hierarchy of Scopes. This is a
	 * very expensive operation
	 * 
	 * @param value
	 * @return true if key is located
	 */
	public boolean containsKey(Object key) {
		boolean result = content.containsKey(key);
		if (!result) {
			if (enclosingScope != null) {
				return enclosingScope.containsKey(key);
			}
		}
		return result;
	}

	/**
	 * Attempts to locate the value object in the hierarchy of Scopes. This is a
	 * very expensive operation
	 * 
	 * @param value
	 * @return true if value is located
	 */
	public boolean containsValue(Object value) {
		boolean result = content.containsValue(value);
		if (!result) {
			if (enclosingScope != null) {
				return enclosingScope.containsValue(value);
			}
		}
		return result;
	}


	public Object remove(Object key) {
		return null;
	}

	public Object update(String key, Object value) {
		if (content.containsKey(key)) {
			put(key, value);
		} else {
			if (enclosingScope != null) {
				enclosingScope.update(key, value);
			} else {
				throw new RuntimeException(
						"unable to update value that's not defined");
			}
		}
		return value;
	}

	public Set<String> keySet() {
		return content.keySet();
	}

	@Override
	public int size() {
		return content.size();
	}

	@Override
	public boolean isEmpty() {
		return content.isEmpty();
	}

	@Override
	public void clear() {
		content.clear();
	}

	@Override
	public Collection<Object> values() {
		return content.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return content.entrySet();
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> toMerge) {
		content.putAll(toMerge);
	}

	@Override
	public Object get(Object key) {
		return content.get(key);
	}

}
