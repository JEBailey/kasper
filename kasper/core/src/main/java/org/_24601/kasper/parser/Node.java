package org._24601.kasper.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org._24601.kasper.fxc.Element;
import org._24601.kasper.parser.ast.node.Visitor;

public class Node {

	NodeType type = NodeType.UNDEFINED;

	String value;

	List<Node> children;

	public Node() {
		// TODO Auto-generated constructor stub
	}

	public Node(NodeType type) {
		this.type = type;
		this.children = new ArrayList<>();
	}

	public Node(NodeType type, String value, Node... children) {
		this.type = type;
		this.value = value;
		this.children = new ArrayList<>();
		Arrays.stream(children).filter(child -> child != null).forEach(child -> this.children.add(child));
	}

	public Node(NodeType type, Node... children) {
		this.type = type;
		this.children = Arrays.asList(children);
	}

	public Node(String literal) {
		this.type = NodeType.LITERAL;
		this.value = literal;
	}

	public List<Node> getChildren() {
		if (children == null) {
			children = Collections.emptyList();
		}
		return children;
	}

	public String getValue() {
		return value;
	}

	public NodeType getType() {
		return type;
	}

	public static Node[] literals(String... items) {
		return Arrays.stream(items).map(item -> new Node(item)).toArray(Node[]::new);
	}

	public void add(Node child) {
		this.children.add(child);
	}

	public String accept(Visitor visitor, Element element) {
		return visitor.visit(this, element);
	}

}
