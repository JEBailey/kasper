package org._24601.kasper.type;

import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.core.BasicToken;

public class EOS extends BasicToken {

	public EOS(String value, int start, int end) {
		this.value = value;
		this.startPos = start;
		this.endPos = end;
	}

	@Override
	public Collector consume(Collector collector, Stack<Collector> collectors, Stack<Character> charStack) {
		if (collector.invokeEndOfStatement()) {
			collectors.add(collector);
			collector = new Statement(startPos, collector.getLineNumber());
		}
		return collector;
	}

}
