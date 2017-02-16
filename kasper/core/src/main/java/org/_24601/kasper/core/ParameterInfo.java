package org._24601.kasper.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org._24601.kasper.Scope;
import org._24601.kasper.error.KasperRuntimeException;
import org._24601.kasper.type.Reference;

/**
 * Represents the
 * 
 * @author jebailey
 *
 */
public class ParameterInfo {

	public enum State {
		NORMAL, OPTIONAL, CONTEXT_PROPERTY, COLLECTION, COMMANDNAME
	};

	private Type type;

	private State state = State.NORMAL;

	// represents the amount that we should update the command line index
	private int increment = 1;

	private Object parameter;

	public ParameterInfo(Type param, Annotation[] annotations) {
		this.type = param;
		if (param == Scope.class) {
			increment = 0;
		}
	}

	public int incr() {
		return increment;
	}

	public boolean isOptional() {
		return state == State.OPTIONAL;
	}

	public Object render(Scope context, List<?> statement, int tokenIndex) {
		switch (state) {
		case CONTEXT_PROPERTY:
			return context.get(parameter.toString());
		case COMMANDNAME:
			return statement.get(0).toString();
		case COLLECTION:
			try {
				Object list = type.getClass().newInstance();
				ParameterizedType parameterizedType = (ParameterizedType) type.getClass().getGenericSuperclass();
				Type generic = parameterizedType.getActualTypeArguments()[0];
				Method add = Collection.class.getDeclaredMethod("add", Object.class);
				for (int index = tokenIndex; index < statement.size(); ++index) {
					add.invoke(list, context.eval(statement.get(index), generic.getClass()));
				}
			} catch (Exception e) {
				throw new KasperRuntimeException("failed to get COLLECTION");
			}
		default:

		}
		if (type == Scope.class) {
			return context;
		}
		if (type == Reference.class) {
			return new Reference(statement.get(tokenIndex), context);
		}
		return context.eval(statement.get(tokenIndex), type.getClass());
	}

}
