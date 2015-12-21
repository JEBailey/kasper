package org._24601.kasper.api;

import java.util.List;

import javax.script.ScriptContext;

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

	Object execute(ScriptContext context, List<Object> statement) throws KasperException;

}