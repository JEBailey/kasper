package org._24601.kasper.fxc.elements;

import fxc.Element;

public class Comment extends Element {

	public Comment() {
		super("");
		START_TAG = "<!--";
		EMPTY_TAG = "<!-- -->";
		END_TAG = "-->";
	}

}
