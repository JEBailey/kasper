package org._24601.kasper.lang;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletRequest;

import org._24601.kasper.Scope;
import org._24601.kasper.annotations.Command;
import org._24601.kasper.annotations.Primitive;
import org._24601.kasper.core.ArgumentProvider;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.fxc.Attribute;
import org._24601.kasper.fxc.Element;
import org._24601.kasper.fxc.elements.Comment;
import org._24601.kasper.fxc.elements.DocType;
import org._24601.kasper.fxc.elements.VoidElement;
import org._24601.kasper.type.Atom;
import org._24601.kasper.type.Reference;

public class KasperLangImpl {

	/**
	 * When no other defined command is available, this is the default. A closed
	 * tag is created of the defaulted element.
	 * 
	 * @param tag
	 *            String value of tag that does not have it's own command
	 * @param arguments
	 *            List of arguments to be processed as attributes
	 * @param reference
	 * @return
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 * @throws KasperException
	 */
	@Command(value = "_default", numOptional = 2)
	public String defaultCommand(Scope scope, ArgumentProvider args)
			throws IOException, UnsupportedOperationException, KasperException {
		Element element = new Element(args.name());
		element.add("");
		Optional<List<Attribute>> attributeList = args.nextAttributeList();
		Optional<Reference> reference = Optional.ofNullable(args.nextReference());
		if (attributeList.isPresent()) {
			attributeList.get().forEach(attr -> element.setAttribute(attr));
		}
		if (reference.isPresent()) {
			element.add(reference.get().evaluate().toString());
		}
		return element.toString();
	}

	@Command({ "area", "base", "br", "col", "command", "embed", "hr", "img", "input", "keygen", "link", "meta", "param",
			"source", "track", "wbr" })
	public Object defaultVoid(Scope scope, ArgumentProvider args, List<Object> attributes)
			throws IOException, UnsupportedOperationException, KasperException {
		return genericElement(new VoidElement(args.name()), args);
	}

	@Command(value = "var", classes = { Reference.class, Atom.class, Reference.class })
	public Object run(Scope scope, ArgumentProvider args)
			throws IOException, UnsupportedOperationException, KasperException {
		Reference varName = args.nextAs(Reference.class);
		String assign = args.nextAs(String.class);
		Reference value = args.nextAs(Reference.class);
		if (!assign.equals("=")) {
			throw new UnsupportedOperationException();
		}
		varName.setValue(value.evaluate());
		return varName.getKey();
	}
	
	@Command("forward")
	public Object forward(Scope scope, ArgumentProvider args)
			throws IOException, UnsupportedOperationException, KasperException {
		Optional<List<Attribute>> attributes = args.nextAttributeList();
		ServletRequest request = scope.eval("request",ServletRequest.class);
		request.getRequestDispatcher("");
		return null;
	}
	

	@Command("doctype")
	public String doctype(Scope scope, ArgumentProvider args) throws IOException, UnsupportedOperationException, KasperException {
		return genericElement(new DocType(), args);
	}

	@Command("comment")
	public Object comment(Scope scope, ArgumentProvider args) throws IOException, UnsupportedOperationException, KasperException {
		return genericElement(new Comment(), args);
	}
	
	private String genericElement(Element element, ArgumentProvider args) throws KasperException {
		Optional<List<Attribute>> attributeList = args.nextAttributeList();
		Optional<Reference> reference = Optional.ofNullable(args.nextReference());
		if (attributeList.isPresent()) {
			attributeList.get().forEach(attr -> element.setAttribute(attr));
		}
		if (reference.isPresent()) {
			element.add(reference.get().evaluate().toString());
		}
		return element.toString();
	}

	@Primitive("true")
	public boolean boolTrue() {
		return true;
	}

	@Primitive("false")
	public boolean boolFalse() {
		return false;
	}

}
