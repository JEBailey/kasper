package org._24601.kasper.core;

public enum TokenType {

	// Structure Tokens
	EOL, WHITESPACE, ERROR,
	
	//Grammar
	LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,LEFT_BRACKET,RIGHT_BRACKET, PERIOD,
	
	//Non Capturng
	LINE_COMMENT,

	//Operators
	OPERATORS,

	// Literals.
	IDENTIFIER, STRING, NUMBER

}
