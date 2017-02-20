package org._24601.kasper.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org._24601.kasper.Scope;
import org._24601.kasper.type.Reference;

public class ArgumentProvider {

	Iterator<?> objects;

	public ArgumentProvider(List<?> objects) {
		this.objects = objects.iterator();
	}

	private Scope scope;

	public String name() {
		return null;
	}

	public Number getNumber() {
		return scope.eval(objects.next(), Number.class);
	}

	public boolean getBoolean() {
		return scope.eval(objects.next(), Boolean.class);
	}

	
	public <T> T nextAs(Class<T> klass) {
		return scope.eval(objects.next(), klass);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> nextAsListOf(Class<T> klass) {
		List<T> emptyList = Collections.emptyList();
		return scope.eval(objects.next(), emptyList.getClass());
	}
	
	public Reference nextAsResource() {
		return new Reference(objects.next(),scope);
	}
	
	

}
