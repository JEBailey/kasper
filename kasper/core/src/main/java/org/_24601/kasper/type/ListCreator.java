package org._24601.kasper.type;

import java.util.LinkedList;
import java.util.List;

import org._24601.fxc.Element;
import org._24601.kasper.api.Collector;
import org._24601.kasper.error.KasperRuntimeException;

/**
 * Collects objects into a <code>LinkedList</code>
 * 
 * @author je bailey
 */
public class ListCreator implements Collector {

	private List<Object> content;

	private state stateMachine = state.lookingForVar;
	private Element element = new Element("");
	Object key = null;

	private enum state {
		lookingForVar, lookingForAssignment, lookingForValue, lookingForSeperator
	};

	public ListCreator(int lineNumber) {
		content = new LinkedList<Object>();
	}

	@Override

	public boolean add(Object object) {
		switch (stateMachine) {
		case lookingForVar:
			if (object instanceof Atom) {
				key = object;
				stateMachine = state.lookingForAssignment;
				break;
			}
			throw new KasperRuntimeException("attribute assignment incorrect");
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
			throw new KasperRuntimeException("attribute assignment incorrect");
		case lookingForValue:
			if (object instanceof String) {
				stateMachine = state.lookingForSeperator;
				element.setAttribute(key.toString(), (String) object);
				key = null;
				break;
			}
			throw new KasperRuntimeException("attribute assignment incorrect");
		case lookingForSeperator:
			if (object.toString().equals(",")) {
				stateMachine = state.lookingForVar;
				break;
			}
			throw new KasperRuntimeException("attribute assignment incorrect");
		}
		if (key != null) {
			element.setAttribute(key.toString());
		}
		return true;
	}

	@Override
	public void addEol() {
	}

	@Override
	public Object get() {
		return content;
	}

	@Override
	public boolean isCollectorFull() {
		return true;
	}

	@Override
	public int getLineNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

}
