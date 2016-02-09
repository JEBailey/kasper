package org._24601.kasper.type;

/**
 * Textual identifier which represents another type.
 * 
 * The item represented could be any object.
 * 
 * 
 * @author je bailey
 *
 */
public class Atom {

	private String string;
	
	public Atom(String key){
		this.string = key;
	}

	@Override
	public String toString() {
		return string;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		
		if (string == null || !string.equals(obj.toString())){
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return (string == null) ? 0 : string.hashCode();
	}

}
