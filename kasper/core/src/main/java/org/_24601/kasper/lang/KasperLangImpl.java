package org._24601.kasper.lang;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.script.ScriptContext;

import org._24601.fxc.Element;
import org._24601.kasper.Scope;
import org._24601.kasper.annotations.Command;
import org._24601.kasper.annotations.Primitive;
import org._24601.kasper.core.Arguments;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.fxc.elements.Comment;
import org._24601.kasper.fxc.elements.DocType;
import org._24601.kasper.fxc.elements.VoidElement;
import org._24601.kasper.type.Atom;
import org._24601.kasper.type.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.adapter.AdapterManager;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;

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
	public String defaultCommand(Scope scope, Arguments args)
			throws IOException, UnsupportedOperationException, KasperException {
		Element element = new Element(args.name());
		// force closing tag <foo></foo>
		element.add("");
		Optional<List<Object>> attributeList = args.nextAsOptionalListOf(Object.class);
		Optional<Reference> reference = args.nextAsOptional(Reference.class);
		if (attributeList.isPresent()) {
			// add to element
		}
		if (reference.isPresent()) {
			element.add(reference.get().evaluate().toString());
		}
		return element.toString();
	}

	@Command(value = "model", classes = { Reference.class })
	public String model(Scope scope, Arguments args)
			throws IOException, UnsupportedOperationException, KasperException, ClassNotFoundException {
		ScriptContext context = (ScriptContext) scope.get("_context");
		SlingScriptHelper sling = (SlingScriptHelper) context.getAttribute("sling");

		SlingHttpServletRequest request = (SlingHttpServletRequest) context.getAttribute("request");
		String className = args.nextAsOptional(Reference.class).get().evaluate().toString();

		AdapterManager manager = sling.getService(AdapterManager.class);
		DynamicClassLoaderManager classLoader = sling.getService(DynamicClassLoaderManager.class);
		Class<?> klass = classLoader.getDynamicClassLoader().loadClass(className);
		Object model = manager.getAdapter(request, klass);
		scope.put("model", model);
		return "";
	}

	@Command({ "area", "base", "br", "col", "command", "embed", "hr", "img", "input", "keygen", "link", "meta", "param",
			"source", "track", "wbr" })
	public Object defaultVoid(Scope scope, Arguments args, List<Object> attributes)
			throws IOException, UnsupportedOperationException, KasperException {
		Element element = new VoidElement(args.name());
		Optional<List<Object>> attr = args.nextAsOptionalListOf(Object.class);
		if (attr.isPresent()) {
			// Utils.listToAttributes(element, arg1);
		}
		return element.toString();
	}

	@Command(value="var",classes={Reference.class,Atom.class,Reference.class})
	public Object run(Scope scope, Arguments args)
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

	@Command("doctype")
	public Object doctype(List<Object> arg1)
			throws IOException, UnsupportedOperationException, KasperException {
		Element element = new DocType();
		if (arg1 != null) {
			// Utils.listToAttributes(element, arg1);
		}
		return element.toString();
	}

	@Command("comment")
	public Object comment(Reference arg1) throws IOException, UnsupportedOperationException, KasperException {
		Element element = new Comment();
		if (arg1 != null) {
			element.add((String) arg1.evaluate());
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
