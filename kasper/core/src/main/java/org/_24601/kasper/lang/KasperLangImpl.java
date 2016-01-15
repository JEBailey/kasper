package org._24601.kasper.lang;

import java.io.IOException;
import java.util.List;

import org._24601.fxc.Element;
import org._24601.kasper.annotations.Command;
import org._24601.kasper.annotations.Optional;
import org._24601.kasper.annotations.Primitive;
import org._24601.kasper.annotations.parameter.CommandName;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.fxc.elements.Comment;
import org._24601.kasper.fxc.elements.DocType;
import org._24601.kasper.fxc.elements.VoidElement;
import org._24601.kasper.type.Atom;
import org._24601.kasper.type.Reference;

public class KasperLangImpl {

	/**
	 */
	@Command("_default")
	public String defaultCommand(@CommandName String tag, @Optional List<Object> arg1, @Optional Reference arg2)
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
	public Object defaultVoid(@CommandName String tag, @Optional List<Object> arg1)
			throws IOException, UnsupportedOperationException, KasperException {
		Element element = new VoidElement(tag);
		if (arg1 != null) {
			Utils.listToAttributes(element, arg1);
		}
		return element.toString();
	}

	@Command("var")
	public Object run(Reference varName ,Atom assign, Reference value) throws IOException, UnsupportedOperationException, KasperException {
		if (!assign.equals("=")){
			throw new UnsupportedOperationException();
		}
		varName.setValue(value.evaluate());
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
	
	
	@Primitive("true")
	public boolean boolTrue(){
		return true;
	}
	
	@Primitive("false")
	public boolean boolFalse(){
		return false;
	}

}
