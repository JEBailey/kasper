/*
 * KasperBindings.java
 *
 * Created on September 26, 2003, 9:16 AM
 */

package org._24601.kasper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;
import javax.script.ScriptContext;

import org._24601.kasper.api.ListProvider;
import org._24601.kasper.core.Util;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.lang.KasperLangImpl;
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
public class Scope {

	/**
	 * parent scope
	 */
	private Scope parentBindings;

	/**
	 * objects existing in the current scope
	 */
	private Map<String, Object> content;

	
	/**
	 * Default creation process wrapping a <tt>ConcurrentHashMap</tt>
	 * 
	 */
	public Scope() {
		this(null);
		addLibrary();
	}

	private void addLibrary() {
		Util.load(this, new KasperLangImpl());
	}

	public Scope(Scope scope) {
		content = new HashMap<String, Object>();
		parentBindings = scope;
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

	public Scope createChildScope() {
		return new Scope(this);
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
			if (parentBindings != null) {
				return parentBindings.containsKey(key);
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
			if (parentBindings != null) {
				return parentBindings.containsValue(value);
			}
		}
		return result;
	}
	
	public Object getAttribute(Object key){
		Object response = content.get(key);
		if (response == null){
			if (parentBindings != null){
				return parentBindings.getAttribute(key);
			}
		}
		return response;
	}


	public Object remove(Object key) {
		return null;
	}

	public Object update(String name, Object value) {
		if (content.containsKey(name)) {
			put(name, value);
		} else {
			if (parentBindings != null) {
				if (parentBindings instanceof Scope){
					((Scope)parentBindings).update(name, value);
				} else {
					parentBindings.put(name, value);
				}
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
	
	public Object eval(Object object) throws KasperException{
		Object response = object;
		if (response instanceof String) {
			response = this.getAttribute((String)response);
		}
		if (response instanceof Atom) {
			response = this.getAttribute(object.toString());
		}
		if (response instanceof ListProvider) {
			response = new Interpreter().process(this, (ListProvider) response);
		}
		if (response == null) {
			return Undefined.getInstance();
		}
		return response;
	}
	
	public Object eval(Object object, boolean useDefault) throws KasperException{
		Object response = object;
		if (response instanceof Atom) {
			response = this.getAttribute(object.toString());
			if (response == null) {
				return this.getAttribute("_default");
			}
		}
		if (response instanceof Statement) {
			response = new Interpreter().process(this, (ListProvider) response);
		}
		
		return response;
	}
	
	public Object eval(Object object, Type type) throws KasperException{
		if (type instanceof ParameterizedType) {
			return eval(object,((ParameterizedType) type).getRawType());
		}

		if (object instanceof Statement) {
			return eval(new Interpreter().process(this, (Statement) object),type);
		}

		if (object instanceof Atom) {
			if (type != Atom.class) {
				return eval(eval(object),type);
			}
		}

		final Class<? extends Object> klass = object.getClass();
		if (((Class<?>) type).isAssignableFrom(klass)) {
			return object;
		}

		return null;
	}

}
