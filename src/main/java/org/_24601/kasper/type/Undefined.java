package org._24601.kasper.type;

public class Undefined {
	
	private Undefined(){
	}
	
	private static Undefined instance;
	
	public static synchronized Undefined getInstance(){
		if (instance == null){
			instance = new Undefined();
		}
		return instance;
	}

}
