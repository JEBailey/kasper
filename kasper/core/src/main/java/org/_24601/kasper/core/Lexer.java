package org._24601.kasper.core;

import java.util.ArrayList;
import java.util.List;

import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Token;
import org._24601.kasper.error.KasperRuntimeException;
import org._24601.kasper.lex.tokens.EOS;


/**
 * Reference implementation of the Lexer the incoming CharSequence based on the
 * List of Lexical definitions
 * 
 * 
 * @author Jason E Bailey
 *
 */
public class Lexer {


	public static List<Token> tokenize(CharSequence data, List<Lexeme> lexemes) {

		List<Token> tokens = new ArrayList<Token>();
		int offset = 0;
		while (offset < data.length()) {
			// for each iteration through the available lexes
			// we want to be assured that something was consumed
			int consumed = offset;
			for (Lexeme lexeme : lexemes) {
				consumed += lexeme.consume(tokens, data, consumed);
				if (consumed != offset) {
					break;
				}
			}
			// if we've iterated through and nothing has been consumed
			// return.(EOF could trigger this)
			// NOTE: If this IS EOF an EOL would have been caught
			if (consumed == offset) {
				if (data.length() > consumed) {
					throw new KasperRuntimeException("unprocessed: "+ data.subSequence(consumed, data.length()));
				}
			}
			// reset to false for next iteration
			offset = consumed;
		}
		tokens.add(new EOS("", offset, offset));
		return tokens;
	}

}
