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
 * Representation of a word that represents something else in the content of the
 * script. This could be a name of a command or a variable.
 * 
 * @author jebailey
 *
 */
public class Identifier implements Lexeme {

	private static final Pattern pattern = Pattern.compile("\\p{IsAlphabetic}[\\p{IsAlphabetic}_\\-\\p{IsDigit}]*");

	private Matcher matcher;

	private CharSequence charSequence;

	@Override
	public int consume(List<Token> tokens, CharSequence ps, int offset) {
		if (charSequence != ps) {
			charSequence = ps;
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

		public Inner(String value, int i, int end) {
			this.value = new Atom(value);
			this.startPos = i;
			this.endPos = i + value.length();
		}

		@Override
		public Collector consume(Collector collector, Stack<Collector> collectors, Stack<Character> charStack) {
			collector.add(value);
			return collector;
		}

	}

}
