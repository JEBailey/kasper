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
/**
 * Represents an attribute in an xml element. This can be either a single
 * element or a key and value pair.
 * 
 * 
 * @author Jason E Bailey
 * 
 */
public class Attribute {

	private boolean paired;

	private String key;

	private String value;

	public Attribute(String key) {
		this.key = key;
	}

	public Attribute(String key, String value) {
		this.paired = true;
		this.key = key;
		this.value = value;
	}

	public Attribute(String key, Object value) {
		this(key, String.valueOf(value));
	}

	public boolean update(Attribute other) {
		if (!this.equals(other)) {
			return false;
		}
		this.value = other.value;
		return true;
	}
	
	public String getKey(){
		return key;
	}
	
	public String getValue(){
		return value;
	}
	
	public void setValue(String value){
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		Attribute other = (Attribute) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}

	public String toString() {
		return paired ? String.format("%s='%s'", key, value) : key;
	}

}
