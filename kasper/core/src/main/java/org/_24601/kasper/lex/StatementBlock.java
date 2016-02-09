package org._24601.kasper.lex;

import java.util.List;
import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Token;
import org._24601.kasper.core.BasicPairedToken;
import org._24601.kasper.type.MultipleStatementCreator;

public class StatementBlock implements Lexeme {

	private char open = '{';
	
	private char closed = '}';

	@Override
	public int consume(List<Token> tokens, CharSequence ps, int offset) {
		char item = ps.charAt(offset);
		if (item == open) {
			tokens.add(new Inner(item, offset, offset + 1));
			return 1; 
		}
		return 0;
	}

	private class Inner extends BasicPairedToken {

		public Inner(char value, int start, int end) {
			super(value,start,end);
		}

		@Override
		public Collector consume(Collector collector, Stack<Collector> collectors, Stack<Character> charStack) {
				// push the expected closing value
				charStack.push(closed);
				collectors.push(collector);
				// define a new Collector that will accept multiple lines
				return new MultipleStatementCreator(this.startPos);
		}

	}

}
