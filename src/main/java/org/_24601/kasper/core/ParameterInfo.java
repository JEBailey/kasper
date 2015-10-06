package org._24601.kasper.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import org._24601.kasper.annotations.Optional;
import org._24601.kasper.annotations.Property;
import org._24601.kasper.annotations.parameter.CommandName;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.type.Reference;
import org._24601.kasper.type.Statement;

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

	public enum ParamType {
		NORMAL, SCOPE, REFERENCE
	}

	private Type type;

	private State state = State.NORMAL;
	private ParamType pType = ParamType.NORMAL;

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
		if (param == KasperBindings.class) {
			pType = pType.SCOPE;
			increment = 0;
		}
		if (param == Reference.class) {
			pType = pType.REFERENCE; 
		}
	}

	public int incr() {
		return increment;
	}

	public boolean isOptional() {
		return state == State.OPTIONAL;
	}

	public Object render(KasperBindings scope, Statement statement, int tokenIndex) throws KasperException {
		switch (state) {
		case CONTEXT_PROPERTY:
			return scope.get((String) parameter);
		case COMMANDNAME:
			return statement.get(0).toString();
		case COLLECTION:
			try {
				Object list = type.getClass().newInstance();
				ParameterizedType parameterizedType = (ParameterizedType) type.getClass().getGenericSuperclass();
				Type generic = parameterizedType.getActualTypeArguments()[0];
				Method add = Collection.class.getDeclaredMethod("add", Object.class);
				for (int index = tokenIndex; index < statement.size(); ++index) {
					add.invoke(list, scope.get(generic, statement.get(index)));
				}
			} catch (Exception e) {
				throw new KasperException(statement.startPos(), "failed to get COLLECTION");
			}

		}

		switch (pType) {
		case SCOPE:
			return scope;
		case REFERENCE:
			return new Reference(statement.get(tokenIndex), scope);
		case NORMAL:
		}
		return scope.get(type, statement.get(tokenIndex));
		
	}

}
