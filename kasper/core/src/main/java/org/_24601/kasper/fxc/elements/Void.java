package org._24601.kasper.fxc.elements;

import org._24601.kasper.fxc.Element;

public class Void extends Element {

	public Void() {
		super("");
		this.START_TAG = VOID;
		this.END_TAG = VOID;
		this.allowAttributes = false;
		this.allowChildren = true;
	}

}
