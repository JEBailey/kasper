package org._24601.kasper.core;

import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Token;

public abstract class BasicToken implements Token {
	
	protected int startPos;
	protected int endPos;
	
	protected Object value;
	protected Object meta;
	
	public BasicToken(){
	}
	
	public BasicToken(String value, int start, int end) {
		this.value = value.charAt(0);
		this.startPos = start;
		this.endPos = end;
	}
	
	/* (non-Javadoc)
	 * @see posl.engine.api.Token#consume(posl.engine.api.Collector, java.util.Stack, java.util.Stack)
	 */
	@Override
	public abstract Collector consume(Collector statement, Stack<Collector> statements ,Stack<Character> charStack);

	
	@Override
	public String toString(){
		return value.toString();
	}
}
