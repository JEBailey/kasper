package org._24601.kasper.type;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.api.ListProviderVisitor;
import org._24601.kasper.error.KasperException;

/**
 * Base unit of execution, a statement is a prefixed argument.
 * 
 * @author jebailey
 *
 */
public class StatementCreator implements Collector, ListProvider {
	
	private List<Object> content = new LinkedList<Object>();
	private int startPos;
	
	public StatementCreator(int lineNumber){
		this.startPos = startPos;
	}
	
	
	public StatementCreator(Collection<? extends Object> list) {
		content.addAll(list);
	}
	
	
	public boolean notEmpty(){
		return !content.isEmpty();
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


	@Override
	public void addEol() {
	}


	@Override
	public List<Object> get() {
		return content;
	}

	@Override
	public Object accept(ListProviderVisitor visitor) {
		return visitor.apply(content);
	}


	@Override
	public boolean isCollectorFull() {
		return !content.isEmpty();
	}


	@Override
	public int getLineNumber() {
		return startPos;
	}
	

}
