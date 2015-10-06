package org._24601.kasper.api;

import java.util.Iterator;
import java.util.List;

public interface Lexer extends Iterator<Token> {
	
	void tokenize(CharSequence data, List<Lexeme>lexemes);

}