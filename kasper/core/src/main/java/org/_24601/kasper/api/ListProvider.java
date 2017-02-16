package org._24601.kasper.api;

/**
 * Provides a visitor implementation.
 * 
 * @author jebailey
 *
 */
public interface ListProvider {
	Object accept(ListProviderVisitor visitor);
}
