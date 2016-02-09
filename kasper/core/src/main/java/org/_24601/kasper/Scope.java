/*
 * KasperBindings.java
 *
 * Created on September 26, 2003, 9:16 AM
 */

package org._24601.kasper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org._24601.kasper.api.Executable;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.api.ListProviderVisitor;
import org._24601.kasper.core.Util;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.lang.KasperLangImpl;
import org._24601.kasper.type.Atom;
import org._24601.kasper.type.StatementCreator;
import org._24601.kasper.type.Undefined;

/**
 * Provides a wrapper around an enclosed Map which represents the items
 * available to the Interpreter at a particular level. Additionally implements
 * the <tt>Bindings</tt> interface
 * 
 * @author JE Bailey
 */
public class Scope implements ListProviderVisitor {

	// parent scope
	private Scope parentBindings;

	// objects existing in the current scope
	private Map<String, Object> content;

	/**
	 * Default creation process wrapping a <tt>Map</tt>
	 * 
	 */
	public Scope() {
		this(null);
		Util.load(this, new KasperLangImpl());

	}

	/**
	 * Creates a new empty scope, with the provided <tt>Scope</tt> as the parent
	 * 
	 * @param scope
	 */
	public Scope(Scope scope) {
		content = new HashMap<String, Object>();
		parentBindings = scope;
	}

	/**
	 * Stores the key & value at the existing level
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Object put(String key, Object value) {
		return content.put(key, value);
	}

	/**
	 * Convenience mechanism to provide a child scope whose parent is the
	 * current one
	 * 
	 * @return child scope
	 */
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

	/**
	 * Returns the value associated with the key, or null if the provided key is
	 * not found
	 * 
	 * @param key
	 * @return
	 */
	public Object get(Object key) {
		Object response = content.get(key);
		if (response == null) {
			if (parentBindings != null) {
				return parentBindings.get(key);
			}
		}
		return response;
	}

	public Object remove(Object key) {
		Object value = content.remove(key);
		if (value == null) {
			if (parentBindings != null) {
				return parentBindings.remove(key);
			}
		}
		return value;
	}

	public Object update(String name, Object value) {
		if (content.containsKey(name)) {
			put(name, value);
		} else {
			if (parentBindings != null) {
				if (parentBindings instanceof Scope) {
					parentBindings.update(name, value);
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

	/**
	 * Evaluates the object in the context of the existing scope. 
	 * 
	 * @param object
	 * @return
	 * @throws KasperException
	 */
	public Object eval(Object object) throws KasperException {
		Object response = object;
		if (response instanceof String) {
			response = this.get(response);
		}
		if (response instanceof Atom) {
			response = this.get(object);
		}
		if (response instanceof ListProvider) {
			response = ((ListProvider) response)
					.accept((ListProviderVisitor) this);
		}
		if (response == null) {
			return Undefined.getInstance();
		}
		return response;
	}

	public Object eval(Object object, boolean useDefault)
			throws KasperException {
		Object response = object;
		if (response instanceof Atom) {
			response = this.get(object);
			if (response == null) {
				return this.get("_default");
			}
		}
		if (response instanceof StatementCreator) {
			response = ((ListProvider) response)
					.accept((ListProviderVisitor) this);
		}

		return response;
	}

	public Object eval(Object object, Type type) throws KasperException {
		if (type instanceof ParameterizedType) {
			return eval(object, ((ParameterizedType) type).getRawType());
		}

		if (object instanceof StatementCreator) {
			return eval(
					((StatementCreator) object).accept((ListProviderVisitor) this),
					type);
		}

		if (object instanceof Atom) {
			if (type != Atom.class) {
				return eval(eval(object), type);
			}
		}

		final Class<? extends Object> klass = object.getClass();
		if (((Class<?>) type).isAssignableFrom(klass)) {
			return object;
		}

		return null;
	}

	@Override
	public Object apply(List<?> list) throws KasperException {
		Object token = this.eval(list.get(0), true);
		if (token instanceof Executable) {
			return ((Executable) token).execute(this, list);
		}
		return token;
	}

}
