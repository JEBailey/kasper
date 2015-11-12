package org._24601.kasper.fxc.elements;

import org._24601.fxc.Element;

public class Comment extends Element {

	public Comment() {
		super("");
		START_TAG = "<!--";
		EMPTY_TAG = "<!-- -->";
		END_TAG = "-->";
	}

}
