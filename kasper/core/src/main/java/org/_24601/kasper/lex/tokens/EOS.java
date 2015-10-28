package org._24601.kasper.lex.tokens;

import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.core.BasicToken;
import org._24601.kasper.type.Statement;

public class EOS extends BasicToken {

	public EOS(String value, int start, int end) {
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
