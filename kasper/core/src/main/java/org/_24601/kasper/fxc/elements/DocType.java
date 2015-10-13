package org._24601.kasper.fxc.elements;

import fxc.Element;

public class DocType extends Element {

	public DocType() {
		super("doctype");
		START_TAG = "<!%s%s>";
		EMPTY_TAG = START_TAG;
		END_TAG = VOID;
	}

	
}