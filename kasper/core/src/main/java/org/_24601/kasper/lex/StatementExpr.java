package org._24601.kasper.lex;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Token;
import org._24601.kasper.core.BasicToken;
import org._24601.kasper.type.ExternalResolver;
import org._24601.kasper.type.Statement;

/**
 * Represents the physical structure components of a basic Posl implementation.
 * The following represent built in types of structures
 * <p>
 * `{` and `}` surround a block of tokens which can be on multiple lines `[` and
 * `]` surround a single expression `(` and `)` represent a list of tokens
 * </p>
 * 
 * @author je bailey
 * 
 */
public class StatementExpr implements Lexeme {

	private char close = '}';

	Pattern pattern = Pattern.compile("\\${\\s*([\\w()\\[\\]\\.]+)\\s*}");

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
			collector.add(new ExternalResolver((String)value));
			return collector;
		}

	}

}
