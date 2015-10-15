package org._24601.kasper.lang;

import java.lang.reflect.Array;
import java.util.List;

import org._24601.kasper.error.KasperException;
import org._24601.kasper.lang.KasperLangImpl.state;
import org._24601.kasper.type.Atom;

import fxc.Element;

public class Utils {

	public Utils() {
		// TODO Auto-generated constructor stub
	}
	
	public static void listToAttributes(Element element, List<Object> argList) throws KasperException {
		state stateMachine = state.lookingForVar;
		Object key = null;
		String value = null;
		for (Object object : argList) {
			switch (stateMachine) {
			case lookingForVar:
				if (object instanceof Atom) {
					key = object;
					stateMachine = state.lookingForAssignment;
					break;
				}
				throw new KasperException(0, "attribute assignment incorrect");
			case lookingForAssignment:
				if (object.toString().equals("=")) {
					stateMachine = state.lookingForValue;
					break;
				}
				if (object.toString().equals(",")) {
					stateMachine = state.lookingForVar;
					element.setAttribute(key.toString());
					key = null;
					break;
				}
				throw new KasperException(0, "attribute assignment incorrect");
			case lookingForValue:
				if (object instanceof String) {
					stateMachine = state.lookingForSeperator;
					element.setAttribute(key.toString(), (String) object);
					key = null;
					break;
				}
				throw new KasperException(0, "attribute assignment incorrect");
			case lookingForSeperator:
				if (object.toString().equals(",")) {
					stateMachine = state.lookingForVar;
					break;
				}
				throw new KasperException(0, "attribute assignment incorrect");
			}
		}
		if (key != null) {
			element.setAttribute(key.toString());
		}
	}

	public static Object[] toArray(Object foo) {
		if (foo.getClass().isArray()){
			return (Object[])foo;
		}
		return null;
	}

	public static String toString(Object object) {
		if (object instanceof String){
			return (String)object;
		}
		return object.toString();
	}

}
