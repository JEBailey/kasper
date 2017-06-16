package org._24601.kasper.parser;

public interface Typed {
	
	
	NodeType type = NodeType.UNDEFINED;

	default NodeType getType(){
		return type;
	}

}
