package org._24601.kasper.type;

import java.util.Collection;
import java.util.LinkedList;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.Executable;
import org._24601.kasper.api.StatementProvider;
import org._24601.kasper.api.StatementProviderVisitor;
import org._24601.kasper.error.KasperException;

/**
 * Base unit of execution, a statement is a prefixed argument.
 * 
 * @author jebailey
 *
 */
public class Statement implements Collector, StatementProvider {
	
	private LinkedList<Object> content = new LinkedList<Object>();
	private int startPos;
	private int endPos;
	private int lineNumberStart;
	private int lineNumberEnd;
	
	public Statement(int startPos, int lineNumber){
		this.startPos = startPos;
	}
	
	
	public Statement(Collection<? extends Object> list) {
		content = new LinkedList<Object>(list);
	}
	
	public Statement(Executable executable) {
		content.add(executable);
	}
	
	public boolean notEmpty(){
		return !content.isEmpty();
	}

	public Statement subList(int arg0, int arg1){
		return new Statement(content.subList(arg0, arg1));
	}
	
	public String errorString() {
		return "at pos :" + toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object object:content){
			sb.append(' ');
			sb.append(object.toString());
		}
		sb.append(' ');
		return sb.toString();
	}

	@Override
	public boolean add(Object object) {
		return content.add(object);
	}

	public Object get(int i) {
		
		return content.get(i);
	}

	public int size() {
		return content.size();
	}

	public void push(Atom atom) {
		content.push(atom);
	}

	@Override
	public boolean invokeEndOfStatement() {
		return !content.isEmpty();
	}

	public int startPos() {
		return startPos;
	}

	public int endPos() {
		return endPos;
	}

	@Override
	public Object get() {
		return this;
	}

	@Override
	public Object accept(StatementProviderVisitor visitor) throws KasperException {
		return visitor.apply(this);
	}


	@Override
	public void addEOL() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getLineNumber() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
