package org._24601.kasper.lex;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Token;
import org._24601.kasper.core.BasicToken;
import org._24601.kasper.type.StatementCreator;

/**
 * Consumes multiple end of line indicators
 * 
 * 
 * @author jebailey
 *
 */
public class EndOfLine implements Lexeme {

	private static final Pattern pattern = Pattern.compile("\r?\n");
	private Matcher matcher;
	private int length;

	public EndOfLine(CharSequence charSequence) {
		super();
		matcher = pattern.matcher(charSequence);
		length = charSequence.length();
	}

	@Override
	public int consume(List<Token> tokens, int offset) {
		matcher.region(offset, length);
		if (matcher.lookingAt()) {
			String s = matcher.group();
			tokens.add(new Inner(s, offset, matcher.end()));
			return s.length();
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
			collector.addEol();
			if (collector.isCollectorFull()) {
				collectors.add(collector);
				collector = new StatementCreator(collector.getLineNumber());
			}
			return collector;
		}

	}
}
