package org._24601.kasper.fxc.elements;

import org._24601.kasper.fxc.Element;

public class DocType extends Element {

	public DocType() {
		super("doctype");
		START_TAG = "<!%s";
		END_TAG = ">";
	}

	
}
