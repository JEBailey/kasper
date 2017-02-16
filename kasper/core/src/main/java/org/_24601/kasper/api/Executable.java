package org._24601.kasper.api;

import java.util.List;

import org._24601.kasper.Scope;
import org._24601.kasper.error.KasperException;

/**
 * Designates an object that performs work in the script environment.
 * In processing a default statement, the assumption is that an executable is the first
 * object to be encountered.
 * 
 * @author je bailey
 *
 */
public interface Executable {

	Object execute(Scope context, List<? extends Object> statement);

}