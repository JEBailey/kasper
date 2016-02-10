package org._24601.kasper.lex;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Token;
import org._24601.kasper.core.BasicToken;
import org._24601.kasper.type.Atom;

/**
 * 
 * 
 * 
 * @author jebailey
 *
 */
public class Assignment implements Lexeme {

	Pattern pattern = Pattern.compile("[=,.]");
	private Matcher matcher;
	private int length;
	
	public Assignment(CharSequence cachedStream) {
		super();
		matcher = pattern.matcher(cachedStream);
		length = cachedStream.length();
	}

	@Override
	public int consume(List<Token> tokens, int offset) {
		int totalCaptured = 0;
		matcher.region(offset, length);
		if (matcher.lookingAt()) {
			String s = matcher.group();
			tokens.add(new Inner(s, offset, matcher.end()));
			totalCaptured = s.length();
		}
		return totalCaptured;
	}

	private class Inner extends BasicToken {

		public Inner(String value, int i, int end) {
			this.value = new Atom(value);
			this.startPos = i;
			this.endPos = i + value.length();
		}

		@Override
		public Collector consume(Collector statement, Stack<Collector> statements, Stack<Character> charStack) {
			statement.add(value);
			return statement;
		}
	}
}
