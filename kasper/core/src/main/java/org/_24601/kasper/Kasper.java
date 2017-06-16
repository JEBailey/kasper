/*
 * Interpertor.java
 *
 * Created on April 14, 2003, 8:03 PM
 */

package org._24601.kasper;

import java.util.List;
import java.util.Stack;

import org._24601.kasper.api.Collector;
import org._24601.kasper.error.KasperException;

/**
 * Initiates the process of turning a string of text into an executable
 * structure based on information supplied in the Scope
 * 
 * 
 * @author je bailey
 */
public class Kasper {

	private Stack<Collector> collectors = new Stack<Collector>();

	@SuppressWarnings("serial")
	

	public Kasper(final String script) {

	}

	public List<Object> eval(Scope scope) throws KasperException {
		return null;
	}


}
