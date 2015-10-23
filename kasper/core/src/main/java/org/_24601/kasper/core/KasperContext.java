package org._24601.kasper.core;

import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.script.Bindings;
import javax.script.ScriptContext;

import org._24601.kasper.annotations.Command;
import org._24601.kasper.annotations.Primitive;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Parser;
import org._24601.kasper.lang.KasperLangImpl;
import org._24601.kasper.lex.ClosingElement;
import org._24601.kasper.lex.Comments;
import org._24601.kasper.lex.DoubleQuoteString;
import org._24601.kasper.lex.Eol;
import org._24601.kasper.lex.ExternalExpression;
import org._24601.kasper.lex.Identifier;
import org._24601.kasper.lex.AttributeList;
import org._24601.kasper.lex.SingleQuoteStrings;
import org._24601.kasper.lex.Special;
import org._24601.kasper.lex.StatementBlock;
import org._24601.kasper.lex.WhiteSpace;

public class KasperContext implements ScriptContext {

	private Parser parser;

	public static String globalScopeId = "_GLOBAL_SCOPE";

	public static String engineScopeID = "_ENGING_SCOPE";

	private KasperBindings bindings;

	private List<Lexeme> lexemes;

	private static final String output = "__outputstream";

	private static final Logger log = Logger.getLogger(KasperContext.class.getName());

	public KasperContext() {
		this(new DefaultParser(), new KasperBindings());
	}

	public KasperContext(KasperBindings scope) {
		this(new DefaultParser(), scope);
	}

	public KasperContext(Parser parser, KasperBindings bindings) {
		this.parser = parser;
		this.bindings = bindings;
		this.lexemes = standardLexemes();
		this.load(new KasperLangImpl());
		bindings.put(output, System.out);
	}

	/**
	 * Examines a class object and loads any static methods into the context for
	 * execution by the script
	 * 
	 * @param libraryClass
	 */
	public void load(Class<?> libraryClass) {
		Method[] methods = libraryClass.getMethods();
		loadMethods(null, methods);
	}

	/**
	 * Loads an object which will have methods used as part of the scripting
	 * process, this allows for state to be maintained in the java object
	 * separate from any script based objects
	 * 
	 * @param libraryObject
	 */
	public void load(Object libraryObject) {
		Method[] methods = libraryObject.getClass().getMethods();
		loadMethods(libraryObject, methods);
	}

	@SuppressWarnings("serial")
	public static List<Lexeme> standardLexemes() {
		return new ArrayList<Lexeme>() {
			{
				add(new WhiteSpace());
				add(new Comments());
				add(new Identifier());
				add(new DoubleQuoteString());
				add(new SingleQuoteStrings());
				add(new ExternalExpression());
				add(new Eol());
				add(new Special());
				add(new AttributeList());
				add(new StatementBlock());
				add(new ClosingElement());
			}
		};
	}

	public Parser getParser() {
		return parser;
	}

	public KasperBindings getScope() {
		return bindings;
	}

	/**
	 * facade to add a key value pair to the underlying scope
	 * 
	 * @param key
	 *            A string representation
	 * @param value
	 * @return
	 */
	public Object put(String key, Object value) {
		return bindings.put(key, value);
	}

	public Object get(String key) {
		return bindings.get(key);
	}

	public boolean containsKey(String key) {
		return bindings.containsKey(key);
	}

	public List<Lexeme> getLexemes() {
		return new ArrayList<Lexeme>(lexemes);
	}

	/**
	 * 
	 * 
	 * 
	 * @param object
	 *            instance of class that the methods belong to
	 * @param methods
	 *            all potential methods
	 */
	private void loadMethods(Object object, Method[] methods) {
		for (Method method : methods) {
			boolean isCommand = method.isAnnotationPresent(Command.class);
			boolean isPrimitive = method.isAnnotationPresent(Primitive.class);
			if (isCommand || isPrimitive) {
				if (!Modifier.isStatic(method.getModifiers()) && object == null) {
					log.severe("Attempting to add a non static command without an associated object");
					log.severe(method.getName());
					break;
				} else {
					String[] id = null;
					Object prior = null;
					if (isCommand) {
						id = method.getAnnotation(Command.class).value();
					}
					if (isPrimitive) {
						id = new String[] { method.getAnnotation(Primitive.class).value() };
					}
					if (id == null) {
						id = new String[] { method.getName() };
						log.fine("no id defined, using the method name: " + id);
					}
					if (isCommand) {
						MethodProxy methodProxy = new MethodProxy(method, object);
						for (String itemId : id) {
							prior = bindings.put(itemId, methodProxy);
							log.fine("command : " + itemId + " has been added");
						}

					} else {
						try {
							prior = bindings.put(id[0], method.invoke(object));
						} catch (Exception e) {
							log.warning("error occured when attempting to create primitive: " + id[0]);
							prior = "";
						}
					}
					if (prior != null) {
						log.warning("adding " + id + " displaced previously assigned object");
					}
				}
			}
		}
	}

	@Override
	public void setBindings(Bindings bindings, int scope) {
		switch (scope) {
		case ENGINE_SCOPE:
			if (bindings == null) {
				throw new NullPointerException("Engine scope cannot be null.");
			}
			this.bindings.put(engineScopeID, bindings);
			break;
		case GLOBAL_SCOPE:
			this.bindings.put(globalScopeId, bindings);
			break;
		default:
			throw new IllegalArgumentException("Invalid scope value.");
		}
	}

	@Override
	public Bindings getBindings(int scope) {
		switch (scope) {
		case ENGINE_SCOPE:
			return (Bindings) this.bindings.get(engineScopeID);
		case GLOBAL_SCOPE:
			return (Bindings) this.bindings.get(globalScopeId);
		default:
			throw new IllegalArgumentException("Illegal scope value.");
		}
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		Bindings bindings = getBindings(scope);
		if (bindings != null) {
			bindings.put(name, value);
		}
	}

	@Override
	public Object getAttribute(String name, int scope) {
		Bindings bindings = getBindings(scope);
		if (bindings != null) {
			return bindings.get(name);
		}
		return null;
	}

	@Override
	public Object removeAttribute(String name, int scope) {
		Bindings bindings = getBindings(scope);
		if (bindings != null) {
			return bindings.remove(name);
		}
		return null;
	}

	@Override
	public Object getAttribute(String name) {
		Object reply = getAttribute(name, ENGINE_SCOPE);
		if (reply == null) {
			Bindings bindings = (Bindings) this.bindings.get(globalScopeId);
			if (bindings != null) {
				reply = bindings.get(name);
			}
		}
		return reply;
	}

	@Override
	public int getAttributesScope(String name) {
		Object temp = getAttribute(name, ENGINE_SCOPE);
		if (temp != null){
			return ENGINE_SCOPE;
		}
		temp = getAttribute(name, GLOBAL_SCOPE);
		if (temp != null){
			return GLOBAL_SCOPE;
		}
		return -1;
	}

	@Override
	public Writer getWriter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Writer getErrorWriter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWriter(Writer writer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setErrorWriter(Writer writer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Reader getReader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReader(Reader reader) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Integer> getScopes() {
		// TODO Auto-generated method stub
		return null;
	}

}
