package org._24601.kasper.api;

import org._24601.kasper.error.KasperException;

/**
 * 
 * 
 * @author jebailey
 *
 */
public interface StatementProvider {

	Object accept(StatementProviderVisitor visitor) throws KasperException;
}
