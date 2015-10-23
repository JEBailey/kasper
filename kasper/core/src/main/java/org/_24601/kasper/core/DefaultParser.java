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
import org._24601.kasper.api.Token;
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
	
	private Stack<Collector> collectors = new Stack<Collector>();

	private Collector collector;
	
	private Stack<Character> charStack = new Stack<Character>();


	public DefaultParser() {
		lexer = new DefaultLexer();
		collector = new Statement(0,1);
	}


	@Override
	public Stack <Collector> process(CharSequence is, List<Lexeme> lexemes) throws KasperException {
		List<Token> tokens = lexer.tokenize(is, lexemes);
		for (Token token: tokens) {			
			collector = token.consume(collector, collectors, charStack);
		}
		return collectors;
	}


}
