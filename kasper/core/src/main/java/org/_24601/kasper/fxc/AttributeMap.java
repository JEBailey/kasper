package org._24601.kasper.fxc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeMap {

	private Map<String, String> innerMap = new HashMap<>();

	public AttributeMap(List<Attribute> attrs) {
		attrs.forEach(a -> innerMap.put(a.getKey(), a.getValue()));
	}
	

}
