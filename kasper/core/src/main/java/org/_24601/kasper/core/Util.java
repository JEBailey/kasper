package org._24601.kasper.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.script.Bindings;
import javax.script.ScriptContext;

import org._24601.kasper.Interpreter;
import org._24601.kasper.annotations.Command;
import org._24601.kasper.annotations.Primitive;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.type.Atom;
import org._24601.kasper.type.Statement;
import org._24601.kasper.type.Undefined;

public class Util {

	/**
	 * Loads an object which will have methods used as part of the scripting
	 * process, this allows for state to be maintained in the java object
	 * separate from any script based objects
	 * 
	 * @param libraryObject
	 */
	public static void load(Bindings bindings, Object libraryObject) {
		Method[] methods = libraryObject.getClass().getMethods();
		loadMethods(bindings, libraryObject, methods);
	}

	/**
	 * Examines a class object and loads any static methods into the context for
	 * execution by the script
	 * 
	 * @param libraryClass
	 */
	public static void load(Bindings bindings, Class<?> libraryClass) {
		Method[] methods = libraryClass.getMethods();
		loadMethods(bindings, null, methods);
	}

	/**
	 * 
	 * 
	 * 
	 * @param object
	 *            instance of class that the methods belong to
	 * @param methods
	 *            all potential methods
	 */
	public static void loadMethods(Bindings bindings, Object object,
			Method[] methods) {
		for (Method method : methods) {
			boolean isCommand = method.isAnnotationPresent(Command.class);
			boolean isPrimitive = method.isAnnotationPresent(Primitive.class);
			if (isCommand || isPrimitive) {
				if (!Modifier.isStatic(method.getModifiers()) && object == null) {
					break;
				} else {
					String[] id = null;
					if (isCommand) {
						id = method.getAnnotation(Command.class).value();
					}
					if (isPrimitive) {
						id = new String[] { method.getAnnotation(
								Primitive.class).value() };
					}
					if (id == null) {
						id = new String[] { method.getName() };
					}
					if (isCommand) {
						MethodProxy methodProxy = new MethodProxy(method,
								object);
						for (String itemId : id) {
							bindings.put(itemId, methodProxy);
						}

					} else {
						try {
							bindings.put(id[0], method.invoke(object));
						} catch (Exception e) {
							//prior = "";
						}
					}
				}
			}
		}
	}
	
	public static  Object eval(ScriptContext context, Object object) throws KasperException{
		Object response = object;
		if (response instanceof Atom) {
			response = context.getAttribute(object.toString());
		}
		if (response instanceof ListProvider) {
			response = new Interpreter().process(context, (ListProvider) response);
		}
		if (response == null) {
			return Undefined.getInstance();
		}
		return response;
	}
	
	public static Object eval(ScriptContext context, Object object, boolean useDefault) throws KasperException{
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
	
	public static Object eval(ScriptContext context, Object object, Type type) throws KasperException{
		if (type instanceof ParameterizedType) {
			return eval(context,object,((ParameterizedType) type).getRawType());
		}

		if (object instanceof Statement) {
			return eval(context,new Interpreter().process(context, (Statement) object),type);
		}

		if (object instanceof Atom) {
			if (type != Atom.class) {
				return eval(context,eval(context,object),type);
			}
		}

		final Class<? extends Object> klass = object.getClass();
		if (((Class<?>) type).isAssignableFrom(klass)) {
			return object;
		}

		return null;
	}
	
	public static ScriptContext createChildScope(ScriptContext context){
		return null;
	}
	
	public static ScriptContext removeChildScope(ScriptContext context){
		return null;
	}
}
