package org._24601.kasper.lang;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.regex.Matcher;

import org._24601.kasper.Interpreter;
import org._24601.kasper.annotations.Command;
import org._24601.kasper.annotations.Optional;
import org._24601.kasper.annotations.Primitive;
import org._24601.kasper.annotations.parameter.CommandName;
import org._24601.kasper.core.KasperBindings;
import org._24601.kasper.core.KasperContext;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.fxc.elements.Comment;
import org._24601.kasper.fxc.elements.DocType;
import org._24601.kasper.fxc.elements.VoidElement;
import org._24601.kasper.lex.ExternalExpression;
import org._24601.kasper.type.Atom;
import org._24601.kasper.type.Reference;

import fxc.Element;

public class KasperLangImpl {

	private Writer writer;

	public KasperLangImpl(Writer writer) {
		this.writer = writer;
	}

	/**
	 */
	@Command("_default")
	public Object defaultCommand(KasperBindings bindings, @CommandName String tag, @Optional List<Object> arg1, @Optional Reference arg2)
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
	public Object defaultVoid(KasperBindings bindings, @CommandName String tag, @Optional List<Object> arg1)
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
	
	private String evalString(KasperContext context, String toParse) throws KasperException{
		Matcher matcher = ExternalExpression.pattern.matcher(toParse);
		if (matcher.find()) {
			String s = matcher.group(0);
			Object replacement = Interpreter.process(context, s);
		}
		return null;
	}

	@Command("var")
	public Object run(KasperBindings bindings, Reference varName ,Atom assign, Reference value) throws IOException, UnsupportedOperationException, KasperException {
		if (!assign.equals("=")){
			throw new UnsupportedOperationException();
		}
		varName.updateValue(value.evaluate());
		return "";
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
