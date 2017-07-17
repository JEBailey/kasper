package org._24601.kasper.fxc.elements;

import org._24601.kasper.fxc.Element;

public class Void extends Element {

	public Void(String label) {
		super(label);
		START_TAG = "<%s>";
		END_TAG = VOID;
	}
}
