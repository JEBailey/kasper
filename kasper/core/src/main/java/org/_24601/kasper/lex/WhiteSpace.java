package org._24601.kasper.lex;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org._24601.kasper.api.Lexeme;
import org._24601.kasper.api.Token;

public class WhiteSpace implements Lexeme {

	// custom white space identifier as we don't want to capture EOL's
	private Pattern pattern = Pattern.compile("[ \\t\\x0B\\f]+");
	private Matcher matcher;
	private int length;

	public WhiteSpace(CharSequence charSequence) {
		super();
		matcher = pattern.matcher(charSequence);
		length = charSequence.length();
	}

	@Override
	public int consume(List<Token> tokens, int offset) {
		int totalCaptured = 0;
		matcher.region(offset, length);
		if (matcher.lookingAt()) {
			String s = matcher.group();
			totalCaptured += s.length();
		}
		return totalCaptured;
	}

}
