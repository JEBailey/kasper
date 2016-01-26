package org._24601.kasper.lang;

import java.io.IOException;
import java.util.List;

import javax.script.ScriptContext;

import org._24601.fxc.Element;
import org._24601.kasper.Scope;
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
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.adapter.AdapterManager;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;

public class KasperLangImpl {

	/**
	 * When no other defined command is available, this function processes the request
	 * 
	 * @param tag String value of tag that does not have it's own command
	 * @param arguments List of arguments to be processed as attributes
	 * @param reference
	 * @return
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 * @throws KasperException
	 */
	@Command("_default")
	public String defaultCommand(@CommandName String tag, @Optional List<Object> arguments, @Optional Reference reference)
			throws IOException, UnsupportedOperationException, KasperException {
		Element element = new Element(tag);
		// force closing tag
		element.add("");
		if (arguments != null) {
			Utils.listToAttributes(element, arguments);
		}
		if (reference != null) {
			element.add(Utils.toString(reference.evaluate()));
		}
		return element.toString();
	}
	
	@Command("model")
	public String model(Scope scope, Reference reference)
			throws IOException, UnsupportedOperationException, KasperException, ClassNotFoundException {
		ScriptContext context = (ScriptContext)scope.getAttribute("_context");
		SlingScriptHelper sling = (SlingScriptHelper)context.getAttribute("sling");
		
		SlingHttpServletRequest request = (SlingHttpServletRequest)context.getAttribute("request");
		String className = reference.evaluate().toString();
		
		AdapterManager manager = sling.getService(AdapterManager.class);
		DynamicClassLoaderManager classLoader  = (DynamicClassLoaderManager) sling.getService(DynamicClassLoaderManager.class);
		Class<?> klass = classLoader.getDynamicClassLoader().loadClass(className);
		Object model = manager.getAdapter(request, klass);
		scope.put("model", model);
		return "";
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
