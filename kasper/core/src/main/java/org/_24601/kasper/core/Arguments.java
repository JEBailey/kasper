package org._24601.kasper.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org._24601.kasper.Scope;
import org._24601.kasper.type.Reference;

public class Arguments {

	Iterator<Object> objects;

	public Arguments(List<Object> objects) {
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

	@SuppressWarnings("unchecked")
	public <T> Optional<List<T>> nextAsOptionalListOf(Class<T> klass) {
		return Optional.ofNullable(scope.eval(objects.next(), new ArrayList<T>().getClass()));
	}

	public <T> Optional<T> nextAsOptional(Class<T> klass) {
		return Optional.ofNullable(scope.eval(objects.next(), klass));
	}
	
	public <T> T nextAs(Class<T> klass) {
		return scope.eval(objects.next(), klass);
	}

}
