package org._24601.kasper.core;

import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Token;

public abstract class BasicPairedToken implements Token {

	protected int startPos;
	protected int endPos;
	
	protected String value;
	protected Object meta;
	
	protected char open;
	protected char close;
	
	public BasicPairedToken(){
	}
	
	public BasicPairedToken(String value, int start, int end, char open, char close) {
		this.value = value;
		this.startPos = start;
		this.endPos = end;
		this.open = open;
		this.close = close;
	}

	@Override
	public abstract Collector consume(Collector statement, Stack<Collector> statements ,Stack<Character> charStack);
	
	
	public String toString(){
		return value.toString();
	}

}
