package org._24601.kasper.parser;

import java.util.Objects;

public class NodesFactory {

	public Node createArgument(String image) {
		// TODO Auto-generated method stub
		return null;
	}

	public Node createStatement(String tagName, Node attrList, Node body) {
		return new Node(NodeType.STATEMENT, tagName, attrList, body);
	}

	public Node createAssignment(String attr, String value) {
		if (Objects.isNull(value)){
			return new Node(NodeType.ASSIGNMENT, new Node(attr));
		}
		value = value.substring(1, value.length() -1);
		return new Node(NodeType.ASSIGNMENT, (Node[]) Node.literals(attr, value));
	}

	public Node createLiteral(String image) {
		return new Node(image.substring(1, image.length() -1));
	}
	
	public Node createList() {
		return new Node(NodeType.LIST);
	}
	
	public Node createStatementBlock(){
		return new Node(NodeType.STATEMENT_BLOCK);
	}

}
