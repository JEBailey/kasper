package org._24601.kasper.type;

import java.util.LinkedList;
import java.util.List;

import org._24601.kasper.api.Collector;
import org._24601.kasper.error.KasperRuntimeException;
import org._24601.kasper.fxc.Attribute;

/**
 * Collects objects into a <code>LinkedList</code>
 * 
 * @author je bailey
 */
public class ListCreator implements Collector {

	private List<Attribute> content;

	private state stateMachine = state.lookingForVar;
	String key = null;

	private enum state {
		lookingForVar, lookingForAssignment, lookingForValue, lookingForSeperator
	};

	public ListCreator(int lineNumber) {
		content = new LinkedList<Attribute>();
	}

	@Override

	public boolean add(Object object) {
		switch (stateMachine) {
		case lookingForVar:
			if (object instanceof Atom) {
				key = object.toString();
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
				content.add(new Attribute(key.toString()));
				key = null;
				break;
			}
			throw new KasperRuntimeException("attribute assignment incorrect");
		case lookingForValue:
			if (object instanceof String) {
				stateMachine = state.lookingForSeperator;
				content.add(new Attribute(key, (String) object));
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

		return true;
	}

	@Override
	public Collector addEol() {
		if (key != null) {
			content.add(new Attribute(key.toString()));
		}
		return this;
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
