/**
 * Created on Sep 10, 2004
 *
 *
 */
package org._24601.fxc.xml;

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
import java.io.Writer;
import java.nio.charset.Charset;

import org._24601.fxc.Attribute;
import org._24601.fxc.Element;
import org._24601.fxc.Formatter;

/**
 * Provides the XML header and standard attributes
 * 
 * 
 * @author Jason E Bailey
 */
public class XMLHeader extends Element {

	protected String DEFAULT_VERSION = "1.0";

	public XMLHeader() {
		this(false);
	}

	/**
	 * Creates a header for the node tree that does not cause indentation
	 * 
	 * @param simple
	 *            if false, adds the version and encoding attributes
	 */
	public XMLHeader(boolean simple) {
		super("");
		START_TAG = "<?xml%s%s?>";
		EMPTY_TAG = START_TAG;
		END_TAG = VOID;
		setAttribute(new Attribute("version", DEFAULT_VERSION));
		if (!simple) {
			setAttribute(new Attribute("encoding", Charset.defaultCharset()));
		}
	}

	public XMLHeader setEncoding(String encoding) {
		setAttribute(new Attribute("encoding", encoding));
		return this;
	}

	public XMLHeader setStandalone(boolean standalone) {
		setAttribute(new Attribute("standalone", standalone ? "yes" : "no"));
		return this;
	}

	@Override
	public void write(Writer writer, Formatter formatter) throws IOException {
		writer.write(String.format(START_TAG, "", getAttributes()));
		if (!elements.isEmpty()) {
			writer.write(formatter.getEol());
		}
		for (Element element : elements) {
			element.write(writer, formatter);
		}
	}

}
