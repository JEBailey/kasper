package org._24601.kasper.api;

import java.util.List;
import java.util.Stack;

import org._24601.kasper.error.KasperException;

/**
 * Converts a string into a series of Statements as defined by the Lexemes
 * that are passed in.
 * 
 * @author je bailey
 *
 */
public interface Parser {
	
	Stack<Collector> process(CharSequence string, List<Lexeme> lexemes) throws KasperException;

}