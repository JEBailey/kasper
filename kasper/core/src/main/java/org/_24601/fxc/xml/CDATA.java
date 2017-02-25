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

import org._24601.fxc.Formatter;

/**
 * Utility class to represent CDATA based data within the XML Structure
 * 
 * @author Jason E Bailey
 *
 */
public class CDATA extends TextElement {

	public CDATA(String content) {
		super("");
		START_TAG = "<![CDATA[";
		END_TAG = "]]>";
		EMPTY_TAG = "<![CDATA[ ]]>";
		this.add(content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fxc.TextElement#write(java.io.Writer, fxc.Formatter)
	 */
	@Override
	public void write(Writer os, Formatter formatter) throws IOException {
		write(os);
	}

}
