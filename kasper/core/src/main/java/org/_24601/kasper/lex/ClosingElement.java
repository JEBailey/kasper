package org._24601.kasper.lex;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Token;
import org._24601.kasper.core.BasicPairedToken;

public class ClosingElement implements Lexeme {

	private char close = '}';
	private CharSequence charSequence;

	public ClosingElement(CharSequence charSequence) {
		super();
		this.charSequence = charSequence;
	}
	@Override
	public int consume(List<Token> tokens, int offset) {
		char item = charSequence.charAt(offset);
		if (item == close) {
			tokens.add(new Inner(item, offset, offset + 1));
			return 1;
		}
		return 0;
	}

	private class Inner extends BasicPairedToken {


		public Inner(char item, int offset, int i) {
			super(item,offset,i);
		}

		@Override
		public Collector consume(Collector collector, Stack<Collector> collectors, Stack<Character> charStack) {

			// sanity check to determine validity by looking comparing char
			// to the expected value from the char stack
			if (!charStack.empty() && charStack.pop() == close) {
				// let the collector know that we've come to a grammatical end
				collector.addEol();
				// hold onto the Collector
				Object temp = collector;
				// replace it with one from the stack
				collector = collectors.pop();
				// add the collector we were using as a new piece of the prior
				// collector
				collector.add(temp);
				return collector;
			} else {
				// throw new
				// PoslException(lineNumber,"could not match brace");
			}

			return collector;
		}


	}

}
