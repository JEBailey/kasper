package org._24601.kasper.lex;

import java.util.List;
import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Token;
import org._24601.kasper.core.BasicToken;
import org._24601.kasper.type.MultiLineStatement;

public class StatementBlock implements Lexeme {

	private char open = '{';

	private char close = '}';

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
			this.value = value;
			this.startPos = start;
			this.endPos = end;
			this.charValue = value.charAt(0);
		}

		@Override
		public Collector consume(Collector collector, Stack<Collector> collectors, Stack<Character> charStack) {
			if (charValue == open) {
				// push the expected closing value
				charStack.push(close);
				return doConsumeOpen(collector, collectors, charStack);
			} else {
				// sanity check to determine validity by looking comparing char
				// to the expected value from the char stack
				if (!charStack.empty() && charStack.pop() == close) {
					return doConsumeClose(collector, collectors, charStack);
				} else {
					// throw new
					// PoslException(lineNumber,"could not match brace");
				}
			}
			return collector;
		}

		public Collector doConsumeOpen(Collector collector, Stack<Collector> collectors, Stack<Character> charStack) {
			// push the existing collector into the stack
			collectors.push(collector);
			// define a new Collector that will accept multiple lines
			return new MultiLineStatement();
		}

		public Collector doConsumeClose(Collector collector, Stack<Collector> collectors, Stack<Character> charStack) {
			// let the collector know that we've come to a grammatical end
			collector.invokeEndOfStatement();
			// hold onto the Collector
			Object temp = collector;
			// replace it with one from the stack
			collector = collectors.pop();
			// add the collector we were using as a new piece of the prior
			// collector
			collector.add(temp);
			return collector;
		}

	}

}
