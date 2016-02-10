package org._24601.kasper.api;

import java.util.List;

/**
 * Defines a word of interest to the Scripting Language
 * 
 * 
 * @author Jason E Bailey
 *
 */
public interface Lexeme {

	/**
	 * Scans the internal <tt>CharSequence</tt> starting from the offset. 
	 * If the stream starts with the pattern that we are looking for, that
	 * series of characters is consumed from the Stream and a token representing
	 * those characters are added to the provided <tt>List</tt> of tokens.<br/>
	 * 
	 * @param tokens
	 *            container object to put discovered tokens
	 * @param sequence
	 *            provided sequence to be scanned
	 * @param offset
	 *            the total char count already consumed from the existing data
	 * @return the amount consumed, if any, by this class
	 */
	int consume(List<Token> tokens, int offset);

}
