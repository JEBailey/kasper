package org._24601.kasper.parser.ast.node;

import java.util.List;

import org._24601.kasper.fxc.Element;
import org._24601.kasper.fxc.elements.Void;
import org._24601.kasper.parser.Node;

public class Visitor {

	Element root = new Void();

	public String visit(Node node, Element element) {
		switch (node.getType()) {
		case STATEMENT_BLOCK:
			node.getChildren().forEach(child -> {
				child.accept(this, element);
			});
			break;
		case STATEMENT:
			String tag = node.getValue();
			Element child = new Element(tag);
			element.add(child);
			node.getChildren().forEach(attr -> attr.accept(this, child));
			break;
		case LIST:
			node.getChildren().forEach(attr -> attr.accept(this, element));
			break;
		case ASSIGNMENT:
			List<Node> children = node.getChildren();
			if (children.size() > 0){
				String attribute = children.get(0).accept(this, null);
				String value = null;
				if (children.size() == 2) {
					value = children.get(1).accept(this, null);
				}
				element.setAttribute(attribute, value);
			}
			break;
		case LITERAL:
			String text = node.getValue();
			if (element != null){
				element.add(text);
			}
			return text;
		case UNDEFINED:
			break;
		}
		return null;

	}

}
