package org._24601.kasper.fxc.elements;

import org._24601.kasper.fxc.Element;

public class VoidElement extends Element {

	public VoidElement(String label) {
		super(label);
		START_TAG = "<%s>";
		EMPTY_TAG = START_TAG;
		END_TAG = VOID;
	}

}
