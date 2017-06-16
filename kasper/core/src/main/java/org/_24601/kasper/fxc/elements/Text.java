package org._24601.kasper.fxc.elements;

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

import org._24601.kasper.fxc.Element;
import org._24601.kasper.fxc.Formatter;

/**
 * Element of text with no encapsulating structure. In the node structure this
 * element is used to provide the plain text of a given tag.
 * 
 * All other operations having to do with the modification of the element will
 * result in an Unsupported Operation.
 * 
 * As such, it's the best element to extend if you are creating a custom element
 * with limited support for operations.
 * 
 * @author Jason E Bailey
 */
public class Text extends Element {

	public Text(String label) {
		super(label);
		START_TAG = "%s";
		END_TAG = VOID;
		setAllowAttributes(false);
		setAllowChildren(false);
	}

	@Override
	public void write(Writer writer, Formatter formatter) throws IOException {
		int maxSegmentLength = formatter.getSegmentLength();
		String[] words = label.split("((?<= )|(?= ))");
		int lineLen = 0;
		for (String word : words) {
			if (lineLen + word.length() > maxSegmentLength) {
				if (lineLen != 0) {
					writer.write(formatter.getEol());
					writer.write(formatter.getIndent());
				}
				lineLen = 0;
			}
			writer.write(word);
			lineLen += word.length();
		}
	}

	@Override
	protected String getAttributes() {
		return "";
	}

}