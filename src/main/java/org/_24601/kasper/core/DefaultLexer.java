package org._24601.kasper.core;

import java.util.ArrayList;
import java.util.List;

import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Lexer;
import org._24601.kasper.api.Token;
import org._24601.kasper.type.EOS;

/**
 * Tokenizes the incoming CharSequence based on the List of Lexical definitions
 * 
 * 
 * @author jebailey
 *
 */
public class DefaultLexer implements Lexer {

	private List<Token> tokens;

	private List<Lexeme> lexemes;

	private CharSequence data;

	@Override
	public void tokenize(CharSequence reader, List<Lexeme> lexemes) {
		data = reader;
		tokens = new ArrayList<Token>();
		this.lexemes = lexemes;
		tokenize();
	}

	/**
	 * 
	 * 
	 */
	private void tokenize() {
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
					// TODO unhandled data - must log
					// String problem = wrapper.getSubString();
					System.out.print("unprocessed: " + data.subSequence(consumed, data.length()));
				}
				return;
			}
			// reset to false for next iteration
			offset = consumed;
		}
		tokens.add(new EOS("", offset, offset));
	}

	@Override
	public Token next() {
		if (!tokens.isEmpty()) {
			return tokens.remove(0);
		}
		return null;
	}

	@Override
	public boolean hasNext() {
		return !tokens.isEmpty();
	}

}