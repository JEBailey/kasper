package org._24601.kasper.api;

import java.util.Iterator;
import java.util.List;

import org._24601.kasper.error.KasperException;
import org._24601.kasper.type.Statement;

/**
 * Converts a string into a series of Statements as defined by the Lexemes
 * that are passed in.
 * 
 * @author je bailey
 *
 */
public interface Parser extends Iterator<Statement> {
	
	void process(CharSequence string, List<Lexeme> lexemes) throws KasperException;

}