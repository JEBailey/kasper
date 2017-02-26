package org._24601.kasper.core;

import java.util.List;
import java.util.Optional;

import org._24601.kasper.Scope;
import org._24601.kasper.fxc.Attribute;
import org._24601.kasper.type.Reference;

public class ArgumentProvider {

	private List<?> objects;

	private int index = 1;
	
	private Scope scope;
	private String name;

	public ArgumentProvider(Scope scope, List<?> objects) {
		this.objects = objects;
		this.scope = scope;
		this.name = objects.get(0).toString();
	}

	public String name() {
		return name;
	}

	public Number getNumber() {
		if (objects.size() > index) {
			return scope.eval(objects.get(index), Number.class);
		}
		return null;
	}

	public Boolean getBoolean() {
		if (objects.size() > index) {
			return scope.eval(objects.get(index), Boolean.class);
		}
		return null;
	}

	public <T> T nextAs(Class<T> klass) {
		if (objects.size() > index) {
			return scope.eval(objects.get(index), klass);
		}
		return null;
	}

	public <T> Optional<T> nextAsOptionalOf(Class<T> klass) {
		if (objects.size() > index) {
			return Optional.ofNullable(scope.eval(objects.get(index), klass));
		}
		return Optional.empty();
	}

	public Optional<List<Attribute>> nextAttributeList() {
		List <Attribute> response = null;
		if (objects.size() > index) {
			response = scope.eval(objects.get(index), List.class);
			if (response != null){
				++index;
			}
		}
		return Optional.ofNullable(response);
	}

	public Reference nextReference() {
		if (objects.size() > index) {
			return new Reference(objects.get(index), scope);
		}
		return null;
	}

}
