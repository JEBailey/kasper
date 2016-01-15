/*
 * Interpertor.java
 *
 * Created on April 14, 2003, 8:03 PM
 */

package org._24601.kasper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Executable;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.api.ListProviderVisitor;
import org._24601.kasper.api.Token;
import org._24601.kasper.core.Lexer;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.lex.AttributeList;
import org._24601.kasper.lex.ClosingElement;
import org._24601.kasper.lex.Comments;
import org._24601.kasper.lex.DoubleQuoteString;
import org._24601.kasper.lex.EndOfLine;
import org._24601.kasper.lex.ExternalExpression;
import org._24601.kasper.lex.Identifier;
import org._24601.kasper.lex.SingleQuoteStrings;
import org._24601.kasper.lex.Special;
import org._24601.kasper.lex.StatementBlock;
import org._24601.kasper.lex.WhiteSpace;
import org._24601.kasper.type.Statement;

/**
 * Initiates the process of turning a string of text into an executable
 * structure based on information supplied in the Scope
 * 
 * 
 * @author je bailey
 */
public class Interpreter {
	
	private Stack<Collector> collectors = new Stack<Collector>();

	private Collector collector;
	
	private Stack<Character> charStack = new Stack<Character>();
	
	@SuppressWarnings("serial")
	List<Lexeme> lexemes = new ArrayList<Lexeme>() {
		{
			add(new WhiteSpace());
			add(new Comments());
			add(new Identifier());
			add(new DoubleQuoteString());
			add(new SingleQuoteStrings());
			add(new ExternalExpression());
			add(new EndOfLine());
			add(new Special());
			add(new AttributeList());
			add(new StatementBlock());
			add(new ClosingElement());
		}
	};
	
	public Interpreter() {
		collector = new Statement(0,1);
	}



	public Object process(Scope scriptContext, Reader reader) throws KasperException {
		return process(scriptContext, toString(reader));
	}

	/**
	 * 
	 * 
	 * @param string
	 * @return
	 * @throws KasperException
	 */
	public  Object process(Scope context, CharSequence string) throws KasperException {

		Object result = null;
		
		List<Token> tokens = Lexer.tokenize(string, lexemes);
		for (Token token: tokens) {			
			collector = token.consume(collector, collectors, charStack);
		}

		while (!collectors.empty()){
			result = process(context, (Statement)collectors.pop());
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
	public Object process(final Scope context, ListProvider provider) throws KasperException {
		
		return provider.accept(new ListProviderVisitor() {
			
			public Object apply(List<?> list) throws KasperException {				
				Object token = context.eval(list.get(0), true);
				if (token instanceof Executable) {
					token = ((Executable) token).execute(context, list);
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
