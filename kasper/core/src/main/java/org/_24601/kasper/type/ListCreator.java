package org._24601.kasper.type;

import java.util.LinkedList;
import java.util.List;

import org._24601.kasper.api.Collector;

/**
 * Collects objects into a <code>LinkedList</code>
 * 
 * @author je bailey
 */
public class ListCreator implements Collector {

	private List<Object>content;

	public ListCreator() {
		content = new LinkedList<Object>();
	}

	
	@Override
	public boolean add(Object object){
		return content.add(object);
	}

	@Override
	public void addEol() {
	}

	@Override
	public Object get() {
		return content;
	}


	@Override
	public boolean finished() {
		return true;
	}


	@Override
	public int getLineNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

}
