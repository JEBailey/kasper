package org._24601.kasper.fxc;

import java.util.Stack;

/*
 * Copyright 2015 Jason E Bailey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Formatter {

	private int depth;

	private int INDENT = 2;
	// TODO technically this is incorrect
	private String LFCR = "\r\n";

	private Stack<TextFormatter> textRendering = new Stack<>();

	private Stack<TextFormatter> attrRendering = new Stack<>();

	private int segmentLength = 50;

	/**
	 * Returns the maximum length of a segment of xml that starts at a
	 * particular indentation.
	 * 
	 * @return segment width
	 */
	public int getSegmentLength() {
		return segmentLength;
	}

	/**
	 * Sets the maximum width of a segment of xml for an indentation level. This
	 * is not the overall width of the xml starting from root. This is the width
	 * starting from an indentation level.
	 * 
	 * @param segmentWidth
	 */
	public void setSegmentLength(int segmentWidth) {
		this.segmentLength = segmentWidth;
	}

	/**
	 * Depth of xml node.
	 * 
	 * @return current depth
	 */
	public String getIndent() {
		int indentation = depth * INDENT;
		if (indentation > 0) {
			return String.format("%1$" + indentation + "s", " ");
		} else {
			return "";
		}
	}

	/**
	 * Sets the characters to be used at an end of line
	 * 
	 * @param eol
	 * @return
	 */
	public Formatter setEol(String eol) {
		this.LFCR = eol;
		return this;
	}

	/**
	 * Characters that define the end of line
	 * 
	 * @return string of values
	 */
	public String getEol() {
		return LFCR;
	}

	/**
	 * While formatting sets the indentation size for each segment
	 * 
	 * @param size
	 * @return current formatter
	 */
	public Formatter setIndentSize(int size) {
		INDENT = size;
		return this;
	}

	/**
	 * Current indent size for a node
	 * 
	 * @return size of indent
	 */
	public int getIndentSize() {
		return INDENT;
	}

	/**
	 * Increase the current depth
	 * 
	 * @return current formatter
	 */
	public Formatter inc() {
		++depth;
		return this;
	}

	/**
	 * Decreases the depth of the current node
	 * 
	 * @return
	 */
	public Formatter dec() {
		--depth;
		return this;
	}

	/**
	 * If a text formatter has been defined for text, this will return the
	 * rendered text.
	 * 
	 * @param text
	 * @return
	 */
	public String renderText(String text) {
		if (!textRendering.empty()) {
			return textRendering.peek().render(text);
		}
		return text;
	}

	public String renderAttr(String text) {
		if (!attrRendering.empty()) {
			return attrRendering.peek().render(text);
		}
		return text;
	}

	public void setTextFormatter(TextFormatter item) {
		textRendering.push(item);
	}

	public TextFormatter removeTextFormatter() {
		return textRendering.pop();
	}

	public void setAttrFormatter(TextFormatter item) {
		attrRendering.push(item);
	}

	public TextFormatter removeAttrFormatter() {
		return attrRendering.pop();
	}

}
