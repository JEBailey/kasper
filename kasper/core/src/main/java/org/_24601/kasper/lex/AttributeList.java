package org._24601.kasper.lex;

import java.util.List;
import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Token;
import org._24601.kasper.core.BasicToken;
import org._24601.kasper.type.ListCreator;

public class AttributeList implements Lexeme {

	private char open = '(';

	private char close = ')';

	private CharSequence cachedStream;
	
	public AttributeList(CharSequence cachedStream) {
		super();
		this.cachedStream = cachedStream;
	}

	@Override
	public int consume(List<Token> tokens, int offset) {
		char item = cachedStream.charAt(offset);
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
			if (charValue == '(') {
				charStack.push(')');
				collectors.push(collector);
				collector = new ListCreator(0);
			} else {
				if (!charStack.empty() && charStack.pop() == charValue) {
					Collector temp = collector;
					collector = collectors.pop();
					collector.add(temp.get());
				} else {
					// throw new
					// PoslException(lineNumber,"could not match parenthesis");
				}
			}
			return collector;
		}

	}

}
