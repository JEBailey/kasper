package org._24601.kasper.type;

import java.util.LinkedList;
import java.util.List;

import org._24601.kasper.api.Collector;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.api.ListProviderVisitor;
import org._24601.kasper.error.KasperException;

/**
 * consume a series of Lists into a single List of those List items
 * 
 * 
 * @author je bailey
 *
 */
public class MultipleListCollector implements ListProvider, Collector {

	private List<Statement> statements = new LinkedList<Statement>();

	private Statement statement;

	private int startPos;

	public MultipleListCollector() {
		statement = new Statement(0, 0);
	}

	@Override
	public boolean add(Object object) {
		return statement.add(object);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (Statement statement : statements) {
			sb.append(statement.toString());
			sb.append('\n');
		}
		sb.append('}');
		return sb.toString();
	}

	@Override
	public List<Statement> get() {
		return statements;
	}

	@Override
	public void addEol() {
		if (statement.notEmpty()) {
			statements.add(statement);
			this.statement = new Statement(0, 0);
		}
	}

	@Override
	public Object accept(ListProviderVisitor function) throws KasperException {
		StringBuilder sb = new StringBuilder();
		for (Statement statement : statements) {
			Object result = function.apply(statement.get());
			if (result instanceof String) {
				sb.append((String) result);
			} else {
				sb.append(result.toString());
			}
		}
		return sb.toString();
	}

	@Override
	public boolean finished() {
		return false;
	}

	@Override
	public int getLineNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

}
