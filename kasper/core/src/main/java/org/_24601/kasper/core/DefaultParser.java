/*
 * DefaultParser.java
 *
 * Created on October 10, 2003, 5:33 PM
 */

package org._24601.kasper.core;

import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Lexer;
import org._24601.kasper.api.Parser;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.type.Statement;

/**
 * 
 * 
 * @author je bailey
 * 
 */
public class DefaultParser implements Parser {

	private Lexer lexer;
	
	private static Logger log = Logger.getLogger(DefaultParser.class.getName());

	private Stack<Collector> collectors = new Stack<Collector>();

	private Collector collector;
	
	private Stack<Character> charStack = new Stack<Character>();


	public DefaultParser() {
		lexer = new DefaultLexer();
		collector = new Statement(0,1);
	}


	@Override
	public void process(CharSequence is, List<Lexeme> lexemes) throws KasperException {
		lexer.tokenize(is, lexemes);
		while (lexer.hasNext()) {			
			collector = lexer.next().consume(collector, collectors, charStack);
		}
	}

	@Override
	public Statement next() {
		return (Statement)collectors.remove(0);
	}


	@Override
	public boolean hasNext() {
		return !collectors.isEmpty();
	}


	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
