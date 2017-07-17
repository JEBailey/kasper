package org._24601.kasper.fxc.elements;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org._24601.kasper.fxc.Element;

public class ElementFactory {
	
	private List<String> VoidItems = Arrays.asList( "area", "base", "br", "col", "command", "embed", "hr", "img", "input", "keygen", "link", "meta", "param",
			"source", "track", "wbr");
	
	private Map<String,Function<String,Element>> custom = new HashMap<>();
	
	public Element getElementFor(String tag){
		if (VoidItems.contains(tag)){
			return new Void(tag);
		}
		return null;
	}

}
