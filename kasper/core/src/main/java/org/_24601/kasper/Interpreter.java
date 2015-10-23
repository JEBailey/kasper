/*
 * Interpertor.java
 *
 * Created on April 14, 2003, 8:03 PM
 */

package org._24601.kasper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Executable;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.api.ListProviderVisitor;
import org._24601.kasper.api.Parser;
import org._24601.kasper.core.KasperBindings;
import org._24601.kasper.core.KasperContext;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.type.Statement;

/**
 * Initiates the process of turning a string of text into an executable
 * structure based on information supplied in the Context
 * 
 * 
 * @author je bailey
 */
public class Interpreter {

	private Interpreter() {
	}

	public static Object process(KasperContext context, File file) throws FileNotFoundException, KasperException {
		return process(context, new FileInputStream(file));
	}

	public static Object process(KasperContext context, URL resource) throws KasperException, IOException {
		return process(context, resource.openStream());
	}

	public static Object process(KasperContext context, InputStream stream) throws KasperException {
		return process(context, toString(stream));
	}

	public static Object process(KasperContext scriptContext, Reader reader) throws KasperException {
		return process(scriptContext, toString(reader));
	}

	/**
	 * 
	 * 
	 * @param string
	 * @return
	 * @throws KasperException
	 */
	public static Object process(KasperContext context, CharSequence string) throws KasperException {
		Parser parser = context.getParser();
		KasperBindings bindings = context.getScope();
		Object result = null;
		Stack <Collector>collectors = parser.process(string, context.getLexemes());
		while (!collectors.empty()){
			result = process(bindings, (Statement)collectors.pop());
		}
		return result;
	}

	/**
	 * 
	 * 
	 * @param scope
	 *            provides the variables which the statement will be executed
	 *            within
	 * @param provider
	 * @return
	 * @throws KasperException
	 */
	public static Object process(final KasperBindings scope, ListProvider provider) throws KasperException {
		return provider.accept(new ListProviderVisitor() {
			
			@Override
			public Object apply(List list) throws KasperException {
				Object token = scope.getValue(list.get(0), true);
				if (token instanceof Executable) {
					token = ((Executable) token).execute(scope, list);
				}
				return token;
			}
		});
	}

	public static String toString(InputStream is) {
		try (Reader rd = new InputStreamReader(is)) {
			return toString(rd);
		} catch (IOException e) {
			return null;
		}
	}

	public static String toString(Reader reader) {
		StringBuilder out = new StringBuilder();
		char[] b = new char[4096];
		try {
			for (int n; (n = reader.read(b)) != -1;) {
				out.append(b, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toString();
	}

}
