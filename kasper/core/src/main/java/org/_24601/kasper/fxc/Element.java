package org._24601.kasper.fxc;

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
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org._24601.kasper.fxc.elements.TextElement;

/**
 * Basic element of an XML structure.
 * 
 * 
 * @author Jason E Bailey
 */
public class Element {

	protected static final String VOID = "";

	// String formatting where the first item is the tag value and the second is
	// the set of attributes
	protected String START_TAG = "<%s>";

	// String formatting where the first item is the tag value and the second is
	// the set of attributes
	protected String EMPTY_TAG = "<%s/>";

	// String formatting where the replaced item is the tag value
	protected String END_TAG = "</%s>";

	// allows attributes, true by default
	protected boolean allowAttributes = true;

	// allows children, true by default
	protected boolean allowChildren = true;

	/**
	 * tag name
	 */
	protected String label = "";

	public List<Element> elements = new ArrayList<Element>(4);
	public Set<Attribute> attributes = new LinkedHashSet<Attribute>();

	/**
	 * Creates a new instance of Element The supplied label is used as the tag
	 * identifier
	 * 
	 * @param label
	 */
	public Element(String label) {
		this.label = label;
	}

	/**
	 * prints the content of this element to the provided writer,
	 * 
	 * 
	 * @param writer
	 *            writer to write this element to
	 * @throws IOException
	 *             on an incorrect formatting
	 */
	public void write(Writer writer) throws IOException {
		String tag = elements.isEmpty() ? EMPTY_TAG : START_TAG;
		
		writer.write(String.format(tag, label.concat(getAttributes())));
		if (!elements.isEmpty()) {
			for (Element element : elements) {
				element.write(writer);
			}
			writer.write(String.format(END_TAG, label));
		}
	}

	/**
	 * Prints out the content of the element formatted in the manner as defined
	 * by the provided formatter
	 * 
	 * @param writer
	 *            output writer
	 * @param formatter
	 *            defines the stylin of the output
	 * @throws IOException
	 *             on an incorrect formatting
	 */
	public void write(Writer writer, Formatter formatter) throws IOException {
		String tag = elements.isEmpty() ? EMPTY_TAG : START_TAG;
		writer.write(String.format(tag, label.concat(getAttributes())));
		if (!elements.isEmpty()) {
			// end of a tag with contents. if this element is is not inline then
			// eol;
			if (!this.isInline(formatter)) {
				writer.write(formatter.getEol());
				formatter.inc();
			}
			for (Element element : elements) {
				if (!this.isInline(formatter)) {
					writer.write(formatter.getIndent());
				}
				element.write(writer, formatter);
				if (!this.isInline(formatter)) {
					writer.write(formatter.getEol());
				}
			}
			if (!this.isInline(formatter)) {
				formatter.dec();
				writer.write(formatter.getIndent());
			}
			writer.write(String.format(END_TAG, label));
		}

	}

	/**
	 * Appends element to the internal list of children
	 * 
	 * @param value
	 *            element to append
	 * @return current element
	 * @throws UnsupportedOperationException
	 *             if the implementation of this method does not support this
	 *             functionality
	 */
	public Element add(Element value) throws UnsupportedOperationException {
		if (!allowChildren) {
			throw new UnsupportedOperationException("Can not add child element");
		}
		elements.add(value);
		return this;
	}

	/**
	 * Add a child text node
	 * 
	 * @param text
	 *            Text to be added as a child node of type text
	 * @return parent element
	 * @throws UnsupportedOperationException
	 *             if the implementation of this method does not support this
	 *             functionality
	 */
	public Element add(String text) throws UnsupportedOperationException {
		elements.add(new TextElement(text));
		return this;
	}

	/**
	 * Convenience method for a common use case where xml is being used in the
	 * format of :
	 * <p>
	 * {@code
	 * <key> value </key>
	 * }
	 * <p>
	 * 
	 * @param tagName
	 *            tag label
	 * @param textValue
	 *            child text node for the new tag
	 * @return this
	 */
	public Element add(String tagName, String textValue) throws UnsupportedOperationException {
		elements.add(new Element(tagName).add(textValue));
		return this;
	}

	/**
	 * Adds and/or appends the supplied attribute to the internal attributes
	 * representation
	 * 
	 * @param attribute
	 *            to append
	 * @return this
	 */
	public Element setAttribute(Attribute attribute) throws UnsupportedOperationException {
		if (!allowAttributes) {
			throw new UnsupportedOperationException("Can not add attributes");
		}
		if (!attributes.add(attribute)) {
			for (Attribute a : attributes) {
				if (a.equals(attribute)) {
					a.update(attribute);
				}
			}
		}
		return this;
	}

	/**
	 * Create or/append a new attribute with the given key
	 * 
	 * @param attribute
	 *            the key value of the new attribute
	 * @return this
	 */
	public Element setAttribute(String attribute) {
		return this.setAttribute(new Attribute(attribute));
	}

	/**
	 * Creates a new Attribute with the supplied key and value and sets to the
	 * current node
	 * 
	 * @param attribute
	 *            key value of attribute
	 * @param value
	 *            of the provided key
	 * @return this
	 */
	public Element setAttribute(String attribute, String value) {
		return this.setAttribute(new Attribute(attribute, value));
	}

	/**
	 * Provides a string representation of the supplied attributes
	 * 
	 * @return this
	 */
	protected String getAttributes() {
		StringBuilder sb = new StringBuilder();
		for (Attribute key : attributes) {
			sb.append(" ");
			sb.append(key.toString());
		}
		return sb.toString();
	}

	/**
	 * Utility method that determines, based on the given formatter, whether the
	 * tags will be printed out inline or in a staggered representation
	 * 
	 * @param formatter
	 *            output formatting
	 * @return true if tags are to be written out unstaggered
	 */
	public boolean isInline(Formatter formatter) {
		if (elements.size() > 1) {
			return false;
		} else {
			return getSize() < formatter.getSegmentLength();
		}
	}

	/**
	 * the size of the this nodes label and the labels of all of it's children
	 * 
	 * @return calculated size
	 */
	public int getSize() {
		int response = this.label.length();
		for (Element element : elements) {
			response += element.getSize();
		}
		return response;
	}

	public String toString() {
		return toString(null);
	}

	/**
	 * Returns a String representation of this element formatted around the
	 * provided formatter
	 * 
	 * @param formatter
	 *            output formatting
	 * @return this
	 */
	public String toString(Formatter formatter) {
		StringWriter bos = new StringWriter();
		try {
			if (formatter == null) {
				write(bos);
			} else {
				write(bos, formatter);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return bos.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + END_TAG.hashCode();
		result = prime * result + START_TAG.hashCode();
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + label.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Element other = (Element) obj;
		if (!END_TAG.equals(other.END_TAG)) {
			return false;
		}
		if (!EMPTY_TAG.equals(other.EMPTY_TAG)) {
			return false;
		}
		if (!START_TAG.equals(other.START_TAG)) {
			return false;
		}
		if (attributes == null) {
			if (other.attributes != null) {
				return false;
			}
		} else if (!attributes.equals(other.attributes)) {
			return false;
		}
		if (!label.equals(other)) {
			return false;
		}
		return true;
	}

	/**
	 * When extending the element with a new class this provides a facility to
	 * set whether attributes will be accepted or not.
	 * 
	 * @param bool
	 *            allow the addition of attributes
	 */
	public void setAllowAttributes(boolean bool) {
		this.allowAttributes = bool;
	}

	/**
	 * When extending the element with a new class this provides a facility to
	 * set whether child elements will be accepted or not.
	 * 
	 * @param bool
	 *            allow the addition of child elements
	 */
	public void setAllowChildren(boolean bool) {
		this.allowChildren = bool;
	}

}
