/*
 * LineFactory.java
 *
 * Created on October 10, 2003, 5:33 PM
 */

package org._24601.kasper.core;

import java.beans.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.type.Atom;
import org._24601.kasper.type.ListCreator;
import org._24601.kasper.type.MultipleStatementCreator;
import org._24601.kasper.type.StatementCreator;



/**
 * 
 * 
 * @author je bailey
 * 
 */
public class Parser {

	private Stack<Collector> collectors = new Stack<Collector>();

	private Collector collector = new StatementCreator(0);

	private Stack<Character> charStack = new Stack<Character>();
	
	private NumberFormat nf = NumberFormat.getInstance();

	int currentLineNumber = 1;

	public Stack<Collector> process(List<Token> tokens) {
		
		tokens.forEach(token -> {
			switch (token.getType()) {
			case LINE_COMMENT:
			case ERROR:
			case WHITESPACE:
				break;
			case EOL:
				++currentLineNumber;
				collector.addEol();
				if (collector.isCollectorFull()) {
					collectors.add(collector);
					collector = new StatementCreator(currentLineNumber);
				}
				break;
			case LEFT_BRACKET:
				charStack.push(']');
				collectors.push(collector);
				collector = new StatementCreator(currentLineNumber);
				break;
			case RIGHT_BRACKET:
				if (!charStack.empty() && charStack.pop() == ']') {
					Object temp = collector;
					collector = collectors.pop();
					collector.add(temp);
				} else {
					// throw new
					// PoslException(lineNumber,"could not match square
					// bracket");
				}
				break;
				//Attribute List
			case LEFT_PAREN:
				charStack.push(')');
				collectors.push(collector);
				collector = new ListCreator(currentLineNumber);
				break;
			//multi line
			case LEFT_BRACE:
				charStack.push('}');
				collectors.push(collector);
				collector = new MultipleStatementCreator(currentLineNumber);
				break;
			case RIGHT_PAREN:
				if (!charStack.empty() && charStack.pop() == ')') {
					Collector temp = collector;
					collector = collectors.pop();
					collector.add(temp.get());
				} else {
					// throw new
					// PoslException(lineNumber,"could not match parenthesis");
				}
				break;

			case RIGHT_BRACE:
				if (!charStack.empty() && charStack.pop() == '}') {
					collector.addEol();
					Object temp = collector;
					collector = collectors.pop();
					collector.add(temp);
				} else {
					// throw new
					// PoslException(lineNumber,"could not match brace");
				}
				break;
			case IDENTIFIER:
				collector.add(new Atom(token.getString()));
				break;
			case NUMBER:
				try {
					collector.add(nf.parse(token.getString()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case OPERATORS:
				collector.add(new Atom(token.getString()));
				break;
			case STRING:
				String temp = token.getString();
				collector.add(temp.subSequence(1, temp.length()-1));
				break;

			default:
				break;

			}
		});
		collector.addEol();
		collectors.push(collector);
		return collectors;
	}

	public Statement next() {
		return (Statement) collectors.remove(0);
	}

	public boolean hasNext() {
		return !collectors.isEmpty();
	}


	public boolean complete() {
		return charStack.isEmpty();
	}

}
