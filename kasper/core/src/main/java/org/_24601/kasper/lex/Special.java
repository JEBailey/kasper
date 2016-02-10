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
 * @author Jason E Bailey
 *
 */
public class Special implements Lexeme {

	private Pattern pattern = Pattern.compile("[=,.]");
	private Matcher matcher;
	private int length;

	public Special(CharSequence charSequence) {
		super();
		matcher = pattern.matcher(charSequence);
		length = charSequence.length();
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
