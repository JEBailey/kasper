/*
 * KasperBindings.java
 *
 * Created on September 26, 2003, 9:16 AM
 */

package org._24601.kasper.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;

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
	private Bindings parentBindings;

	/**
	 * objects existing in the current scope
	 */
	private Map<String, Object> content;

	
	/**
	 * Default creation process wrapping a <tt>ConcurrentHashMap</tt>
	 * 
	 */
	public KasperBindings() {
		this(null);
		addLibrary();
	}

	private void addLibrary() {
		// TODO Auto-generated method stub
		
	}

	private KasperBindings(Bindings scope) {
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

	public KasperBindings createChildScope() {
		return new KasperBindings(this);
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


	public Object remove(Object key) {
		return null;
	}

	public Object update(String name, Object value) {
		if (content.containsKey(name)) {
			put(name, value);
		} else {
			if (parentBindings != null) {
				if (parentBindings instanceof KasperBindings){
					((KasperBindings)parentBindings).update(name, value);
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
