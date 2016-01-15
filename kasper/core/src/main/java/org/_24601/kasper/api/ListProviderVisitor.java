package org._24601.kasper.api;

import java.util.List;

import org._24601.kasper.error.KasperException;

public interface ListProviderVisitor {

	Object apply(List<?> list) throws KasperException;

}
