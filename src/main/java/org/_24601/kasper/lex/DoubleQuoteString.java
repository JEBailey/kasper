package org._24601.kasper.lex;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Token;
import org._24601.kasper.core.BasicToken;

public class DoubleQuoteString implements Lexeme {

	private static final Pattern pattern = Pattern.compile("^\"([^\"\\\\]*(?:\\\\.[^\"\\\\]*)*)\"");

	@Override
	public int consume(List<Token> tokens, CharSequence ps, int offset) {
		Matcher matcher = pattern.matcher(ps);
		matcher.region(offset, ps.length());
		if (matcher.lookingAt()) {
			String s = matcher.group(1);
			tokens.add(new Inner(s, offset, matcher.end()));
			return matcher.end() - offset;
		}
		return 0;
	}

	private class Inner extends BasicToken {

		public Inner(String value, int start, int end) {
			this.value = value;
			this.startPos = start;
			this.endPos = end;
		}

		@Override
		public Collector consume(Collector collector, Stack<Collector> collectors, Stack<Character> charStack) {
			collector.add(value);
			return collector;
		}

	}
}
