package org._24601.kasper.api;

/**
 * Class that gathers a sequence of tokens into a collection of tokens that may
 * or not consist of the Collector class
 * 
 * @author je bailey
 *
 */
public interface Collector {

	/**
	 *
	 * @param token
	 *            to be added to the aggregate
	 * @return add was successful
	 */
	boolean add(Object token);
	
	/**
	 * Indicates whether the collector can continue to accept more tokens
	 */
	boolean finished();
	
	/**
	 * return the last line number this collector references
	 */
	int getLineNumber();

	/**
	 * Informs the collector that an end of line has been reached. 
	 */
	void addEol();

	/**
	 * Returns the object that the collector is collecting into. This could
	 * the collector itself or a new object
	 * 
	 * @return object which encompasses the supplied tokens
	 */
	Object get();
}
