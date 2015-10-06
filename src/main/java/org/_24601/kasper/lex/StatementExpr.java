package org._24601.kasper.lex;

import java.util.List;
import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Token;
import org._24601.kasper.core.BasicToken;
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

	private char open = '[';

	private char close = ']';

	@Override
	public int consume(List<Token> tokens, CharSequence ps, int offset) {
		char item = ps.charAt(offset);
		if (item == open || item == close) {
			tokens.add(new Inner(Character.toString(item), offset, offset + 1));
			return 1;
		}
		return 0;
	}

	private class Inner extends BasicToken {

		private char charValue;

		public Inner(String value, int start, int end) {
			this.charValue = value.charAt(0);
			this.value = value;
			this.startPos = start;
			this.endPos = end;
		}

		@Override
		public Collector consume(Collector collector, Stack<Collector> collectors, Stack<Character> charStack) {
			if (charValue == open) {
				charStack.push(close);
				collectors.push(collector);
				collector = new Statement(startPos, collector.getLineNumber());
			} else {
				if (!charStack.empty() && charStack.pop() == close) {
					Object temp = collector;
					collector = collectors.pop();
					collector.add(temp);
				} else {
					// throw new
					// PoslException(lineNumber,"could not match square
					// bracket");
				}
			}
			return collector;
		}

	}

}
