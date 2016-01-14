package org._24601.kasper.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org._24601.kasper.Scope;
import org._24601.kasper.annotations.Optional;
import org._24601.kasper.annotations.Property;
import org._24601.kasper.annotations.parameter.CommandName;
import org._24601.kasper.error.KasperException;
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
		for (Annotation annotation : annotations) {
			if (annotation instanceof Optional) {
				state = State.OPTIONAL;
			} else if (annotation instanceof Property) {
				increment = 0;
				state = State.CONTEXT_PROPERTY;
				this.parameter = ((Property) annotation).value();
			} else if (annotation instanceof CommandName) {
				increment = 0;
				state = State.COMMANDNAME;
			}
		}
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

	public Object render(Scope context, List<Object> statement,
			int tokenIndex) throws KasperException {
		switch (state) {
		case CONTEXT_PROPERTY:
			return context.getAttribute(parameter.toString());
		case COMMANDNAME:
			return statement.get(0).toString();
		case COLLECTION:
			try {
				Object list = type.getClass().newInstance();
				ParameterizedType parameterizedType = (ParameterizedType) type
						.getClass().getGenericSuperclass();
				Type generic = parameterizedType.getActualTypeArguments()[0];
				Method add = Collection.class.getDeclaredMethod("add",
						Object.class);
				for (int index = tokenIndex; index < statement.size(); ++index) {
					add.invoke(list,
							context.eval(statement.get(index), generic));
				}
			} catch (Exception e) {
				throw new KasperException(-1, "failed to get COLLECTION");
			}
		default:

		}
		if (type == Scope.class) {
			return context;
		}
		if (type == Reference.class) {
			return new Reference(statement.get(tokenIndex), context);
		}
		return context.eval(statement.get(tokenIndex), type);
	}

}
