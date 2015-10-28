package org._24601.kasper.lex;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Token;
import org._24601.kasper.core.BasicToken;
import org._24601.kasper.type.Statement;

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

	private CharSequence cachedSequence;

	@Override
	public int consume(List<Token> tokens, CharSequence ps, int offset) {
		if (ps != cachedSequence) {
			cachedSequence = ps;
			matcher = pattern.matcher(ps);
		}
		matcher.region(offset, ps.length());
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
			if (collector.finished()) {
				collectors.add(collector);
				collector = new Statement(startPos, collector.getLineNumber());
			}
			return collector;
		}

	}
}
