/*
 * Interpertor.java
 *
 * Created on April 14, 2003, 8:03 PM
 */

package org._24601.kasper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Lexeme;
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
import org._24601.kasper.type.StatementCreator;

/**
 * Initiates the process of turning a string of text into an executable
 * structure based on information supplied in the Scope
 * 
 * 
 * @author je bailey
 */
public class Kasper {

	private Stack<Collector> collectors = new Stack<Collector>();

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

	public Kasper(String script) {
		Collector collector = new StatementCreator(0, 1);
		Stack<Character> charStack = new Stack<Character>();

		List<Token> tokens = Lexer.tokenize(script, lexemes);
		for (Token token : tokens) {
			collector = token.consume(collector, collectors, charStack);
		}
	}

	public List<Object> eval(Scope scope) throws KasperException {
		List<Object> reply = new LinkedList<>();
		for (Collector statement : collectors) {
			reply.add(((StatementCreator) statement).accept(scope));
		}
		return reply;
	}


}
