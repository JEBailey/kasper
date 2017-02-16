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

/**
 * 
 * @author je bailey
 * 
 */
public class ObjectAccessor implements Lexeme {

 	                     
	public static Pattern pattern = Pattern.compile("\\$\\{\\s*([\\w()\\[\\].]+)\\s*}");
	private Matcher matcher;
	private int length;

	public ObjectAccessor(CharSequence charSequence) {
		super();
		matcher = pattern.matcher(charSequence);
		length = charSequence.length();
	}
	
	
	@Override
	public int consume(List<Token> tokens, int offset) {
		matcher.region(offset, length);
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
