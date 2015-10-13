package org._24601.kasper.lang;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org._24601.kasper.annotations.Command;
import org._24601.kasper.annotations.Optional;
import org._24601.kasper.annotations.Primitive;
import org._24601.kasper.annotations.parameter.CommandName;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.fxc.elements.Comment;
import org._24601.kasper.fxc.elements.DocType;
import org._24601.kasper.fxc.elements.VoidElement;
import org._24601.kasper.type.Atom;
import org._24601.kasper.type.MultiLineStatement;
import org._24601.kasper.type.Reference;

import fxc.Element;

public class KasperLangImpl {

	private Writer writer;

	public KasperLangImpl(Writer writer) {
		this.writer = writer;
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * @param tag
	 *            the invoking command name
	 * @param arg1
	 *            Either a list of attributes, a multi line list of child
	 *            arguments or a single string
	 * @param arg2
	 * @return
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 * @throws KasperException
	 */
	@Command("_default")
	public Object defaultCommand(@CommandName String tag, @Optional List<Object> arg1, @Optional Reference arg2)
			throws IOException, UnsupportedOperationException, KasperException {
		Element element = new Element(tag);
		// force closing tag
		element.add("");
		if (arg1 != null) {
			listToAttributes(element, arg1);
		}
		if (arg2 != null) {
			element.add((String) arg2.evaluate());
		}
		return element.toString();
	}

	@Command({ "area", "base", "br", "col", "command", "embed", "hr", "img", "input", "keygen", "link","meta","param","source","track","wbr" })
	public Object defaultVoid(@CommandName String tag, @Optional List<Object> arg1)
			throws IOException, UnsupportedOperationException, KasperException {
		Element element = new VoidElement(tag);
		if (arg1 != null) {
			listToAttributes(element, arg1);
		}
		return element.toString();
	}

	enum state {
		lookingForVar, lookingForAssignment, lookingForValue, lookingForSeperator
	};

	private void listToAttributes(Element element, List<Object> argList) throws KasperException {
		state stateMachine = state.lookingForVar;
		Object key = null;
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

	@Command("var")
	public Object run(@CommandName String functionName, @Optional List<Object> argList,
			@Optional Reference functionBody) throws IOException, UnsupportedOperationException, KasperException {
		Element element = new Element(functionName);
		element.add((String) functionBody.evaluate());
		// writer.write(element.toString());
		return element.toString();
	}

	@Command("doctype")
	public Object doctype(@Optional List<Object> arg1)
			throws IOException, UnsupportedOperationException, KasperException {
		Element element = new DocType();
		if (arg1 != null) {
			listToAttributes(element, arg1);
		}
		return element.toString();
	}

	@Command("comment")
	public Object comment(@Optional Reference arg1) throws IOException, UnsupportedOperationException, KasperException {
		Element element = new Comment();
		if (arg1 != null) {
			element.add((String) arg1.evaluate());
		}
		return element.toString();
	}
	
	@Command("if")
	public Object condition(Reference condition, Reference commands) throws IOException, UnsupportedOperationException, KasperException {
		Object foo = condition.evaluate();
		if (Boolean.valueOf(condition.evaluate().toString())){
			return commands.evaluate();
		}
		return "";
	}
	
	@Primitive("true")
	public boolean boolTrue(){
		return true;
	}
	
	@Primitive("false")
	public boolean boolFalse(){
		return false;
	}

}
