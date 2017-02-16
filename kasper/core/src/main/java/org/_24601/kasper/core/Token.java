package org._24601.kasper.core;

public class Token {

	protected int startPos;
	protected int endPos;

	protected TokenType type;
	protected String value;
	
	protected int lineNo;

	public Token(TokenType type, String value, int start, int lineNo) {
		this.type = type;
		this.value = value;
		this.startPos = start;
		this.endPos = start + value.length();
	}
	
	public int getStartOffset() {
		return startPos;
	}

	
	public int getEndOffset() {
		return endPos;
	}

	public TokenType getType(){
		return type;
	}

	
	public String getString() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
