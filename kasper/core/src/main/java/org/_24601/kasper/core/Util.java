package org._24601.kasper.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.script.Bindings;
import javax.script.ScriptContext;

import org._24601.kasper.Interpreter;
import org._24601.kasper.Scope;
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
	public static void load(Scope scope, Object libraryObject) {
		Method[] methods = libraryObject.getClass().getMethods();
		loadMethods(scope, libraryObject, methods);
	}

	/**
	 * Examines a class object and loads any static methods into the context for
	 * execution by the script
	 * 
	 * @param libraryClass
	 */
	public static void load(Scope bindings, Class<?> libraryClass) {
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
	public static void loadMethods(Scope scope, Object object,
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
							scope.put(itemId, methodProxy);
						}

					} else {
						try {
							scope.put(id[0], method.invoke(object));
						} catch (Exception e) {
							//prior = "";
						}
					}
				}
			}
		}
	}
	

}
