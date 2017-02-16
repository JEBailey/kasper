package org._24601.kasper.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Reference implementation of the Lexer the incoming CharSequence based on the
 * List of Lexical definitions
 * 
 * 
 * @author Jason E Bailey
 *
 */
public class Lexer {
	
	private static final Pattern whitespace = Pattern.compile("[ \\t\\x0B\\f]+");
	private static final Pattern comments = Pattern.compile("^((?m)//.*$)");
	private static final Pattern identifier = Pattern.compile("\\p{IsAlphabetic}[\\p{IsAlphabetic}_\\p{IsDigit}]*");
	private static final Pattern quotes = Pattern.compile("^\"([^\"\\\\]*(?:\\\\.[^\"\\\\]*)*)\"");
	private static final Pattern external = Pattern.compile("\\$\\{\\s*([\\w()\\[\\].]+)\\s*}");
	private static final Pattern eol = Pattern.compile("\r?\n");
	private static final Pattern special = Pattern.compile("[=,]");
	private static final Pattern numbers = Pattern.compile("^-?\\d+([.]\\d+)?(([eE])[+-]?\\d+)?");
	private static final Pattern error = Pattern.compile(".");


	int offset = 0;
	
	int consumed = 0;
	
	List<Token> tokens = new ArrayList<Token>();
	
	CharSequence data;
	
	public Lexer(final CharSequence data) {
		this.data = data;
	}


	public List<Token> tokenize() {
		int offset = consumed;
		while (offset < data.length()) {
			consume(whitespace, TokenType.WHITESPACE);
			consume(comments, TokenType.LINE_COMMENT);
			consume(identifier, TokenType.IDENTIFIER);
			consume(quotes, TokenType.STRING);
			consume(external, TokenType.STRING);
			consume(eol, TokenType.EOL);
			consume(special, TokenType.IDENTIFIER);
			consume(numbers, TokenType.NUMBER);
			//list creator
			consume('(', TokenType.LEFT_PAREN);
			consume(')', TokenType.RIGHT_PAREN);
			//multi line statement
			consume('{', TokenType.LEFT_BRACE);
			consume('}', TokenType.RIGHT_BRACE);
			//object access
			consume('[', TokenType.LEFT_BRACKET);
			consume(']', TokenType.RIGHT_BRACKET);
			consume('.', TokenType.PERIOD);
			// if we've iterated through and nothing has been consumed
			// return.(EOF could trigger this)
			// NOTE: If this IS EOF an EOL would have been caught
			if (offset == consumed) {
				if (data.length() > offset) {
					consume(error, TokenType.ERROR);
				}
			}
			// reset to false for next iteration
			offset = consumed;
		}
		return tokens;
	}
	
	private void consume(Pattern pattern, TokenType tokenType) {
		if (consumed < data.length()) {
			Matcher matcher = pattern.matcher(data);
			matcher.region(consumed, data.length());
			if (matcher.lookingAt()) {
				String string = matcher.group();
				tokens.add(new Token(tokenType, string, consumed, 0));
				consumed += string.length();
			} 
		}
	}

	private void consume(char ch, TokenType tokenType) {
		if (consumed < data.length()) {
			if (data.charAt(consumed) == ch) {
				tokens.add(new Token(tokenType, String.valueOf(ch), consumed, consumed + 1));
				++ consumed;
			}
		}
	}

}
