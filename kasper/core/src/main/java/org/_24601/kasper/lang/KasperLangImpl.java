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
import org._24601.kasper.api.Executable;
import org._24601.kasper.core.KasperBindings;
import org._24601.kasper.core.KasperContext;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.fxc.elements.Comment;
import org._24601.kasper.fxc.elements.DocType;
import org._24601.kasper.fxc.elements.VoidElement;
import org._24601.kasper.lex.ExternalExpression;
import org._24601.kasper.type.Atom;
import org._24601.kasper.type.Reference;
import org._24601.kasper.type.Statement;

import fxc.Element;

public class KasperLangImpl {

	private Writer writer;

	public KasperLangImpl() {
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
			Utils.listToAttributes(element, arg1);
		}
		if (arg2 != null) {
			element.add(Utils.toString(arg2.evaluate()));
		}
		return element.toString();
	}

	@Command({ "area", "base", "br", "col", "command", "embed", "hr", "img", "input", "keygen", "link","meta","param","source","track","wbr" })
	public Object defaultVoid(KasperBindings bindings, @CommandName String tag, @Optional List<Object> arg1)
			throws IOException, UnsupportedOperationException, KasperException {
		Element element = new VoidElement(tag);
		if (arg1 != null) {
			Utils.listToAttributes(element, arg1);
		}
		return element.toString();
	}

	enum state {
		lookingForVar, lookingForAssignment, lookingForValue, lookingForSeperator
	};


	
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
			Utils.listToAttributes(element, arg1);
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
	public Object condition(Reference predicate, Reference commands) throws IOException, UnsupportedOperationException, KasperException {
		if (Boolean.valueOf(predicate.evaluate().toString())){
			return commands.evaluate();
		}
		return "";
	}
	
	@Command("forEach")
	public Object forEach(Reference items, Reference commands) throws IOException, UnsupportedOperationException, KasperException {
		Object foo = items.evaluate();
		Object[] arr = Utils.toArray(foo);
		StringBuilder sb = new StringBuilder();
		for(Object object:arr){
			commands.createChildScope();
			commands.getBindings().put("item", object);
			String str = (String)commands.evaluate();
			sb.append(str);
		}
		return sb.toString();
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
