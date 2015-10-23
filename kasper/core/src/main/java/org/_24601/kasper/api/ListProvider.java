package org._24601.kasper.api;

import org._24601.kasper.error.KasperException;

/**
 * Provides a visitor implementation.
 * 
 * @author jebailey
 *
 */
public interface ListProvider {
	Object accept(ListProviderVisitor visitor) throws KasperException;
}
