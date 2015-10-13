package org._24601.kasper.api;

import java.util.List;

public interface Lexeme {

	/**
	 * The lexeme implementation defines a word. The lexeme scans the incoming
	 * CharSequence starting from the offset and if the stream starts with the
	 * defined lexeme, that series of characters is consumed from the Stream and
	 * a token representing those characters are added to the token list.<br/>
	 * 
	 * 
	 * @param tokens
	 *            That are being collected from the incoming data
	 * @param ps
	 *            Incoming data
	 * @param offset
	 *            the total char count already consumed from the existing data
	 * @return the amount consumed by this function
	 */
	int consume(List<Token> tokens, CharSequence ps, int offset);


}
