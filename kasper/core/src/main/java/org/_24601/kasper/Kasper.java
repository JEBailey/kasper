/*
 * Interpertor.java
 *
 * Created on April 14, 2003, 8:03 PM
 */

package org._24601.kasper;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.core.Lexer;
import org._24601.kasper.core.Parser;
import org._24601.kasper.error.KasperException;
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

	

	public Kasper(final String script) {
		Lexer lexer = new Lexer(script);
		Parser parser = new Parser();
		collectors = parser.process(lexer.tokenize());

	}

	public List<Object> eval(Scope scope) throws KasperException {
		List<Object> reply = new LinkedList<>();
		for (Collector statement : collectors) {
			reply.add(((StatementCreator) statement).accept(scope));
		}
		return reply;
	}


}
