package org._24601.kasper.api;

import java.util.List;

public interface ListProviderVisitor {

	Object apply(List<? extends Object> list);

}
