package org._24601.kasper.api;

import org._24601.kasper.error.KasperException;
import org._24601.kasper.type.Statement;

public interface StatementProviderVisitor {

	Object apply(Statement statement) throws KasperException;

}
