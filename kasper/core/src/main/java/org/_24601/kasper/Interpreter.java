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

import org._24601.kasper.api.Executable;
import org._24601.kasper.api.Parser;
import org._24601.kasper.api.StatementProvider;
import org._24601.kasper.api.StatementProviderVisitor;
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
		KasperBindings scope = context.getScope();
		Object result = null;
		parser.process(string, context.getLexemes());
		while (parser.hasNext()) {
			result = process(scope, parser.next());
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
	public static Object process(final KasperBindings scope, StatementProvider provider) throws KasperException {
		return provider.accept(new StatementProviderVisitor() {
			
			@Override
			public Object apply(Statement statement) throws KasperException {
				Object token = scope.getValue(statement.get(0), true);
				if (token instanceof Executable) {
					token = ((Executable) token).execute(scope, statement);
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
