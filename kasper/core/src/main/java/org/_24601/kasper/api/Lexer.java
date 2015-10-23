package org._24601.kasper.api;

import java.util.List;

/**
 * 
 * 
 * @author Jason E Bailey
 *
 */
public interface Lexer {
	
	List<Token> tokenize(CharSequence data, List<Lexeme>lexemes);

}