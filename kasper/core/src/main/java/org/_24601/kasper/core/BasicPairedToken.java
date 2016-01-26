package org._24601.kasper.core;

import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Token;

public abstract class BasicPairedToken implements Token {

	protected int startPos;
	protected int endPos;
	
	protected char value;
	protected Object meta;
	
	public BasicPairedToken(char value, int start, int end) {
		this.value = value;
		this.startPos = start;
		this.endPos = end;
	}

	@Override
	public abstract Collector consume(Collector statement, Stack<Collector> statements ,Stack<Character> charStack);
	
	
	@Override
	public String toString(){
		return String.valueOf(value);
	}

}
