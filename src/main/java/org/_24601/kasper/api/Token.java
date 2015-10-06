package org._24601.kasper.api;

import java.util.Stack;

public interface Token {

	/**
	 * Encapsulates the logic to determine how to apply the token data
	 * to the incoming structures.
	 * 
	 * @param collector current Collector which is consuming tokens
	 * @param collectorss stack of Collectors which represents nesting
	 * @param charStack used for to look for current bounding representation
	 * @return the collector to be used for the next Token
	 */
	Collector consume(Collector collector,
			Stack<Collector> collectors, Stack<Character> charStack);

}