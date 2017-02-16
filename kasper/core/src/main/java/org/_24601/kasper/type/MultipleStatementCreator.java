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
public class MultipleStatementCreator implements ListProvider, Collector {

	private List<StatementCreator> statements = new LinkedList<StatementCreator>();

	private StatementCreator statement;

	private int currentLineNumber;

	public MultipleStatementCreator(int currentLineNumber) {
		this.currentLineNumber = currentLineNumber;
		statement = new StatementCreator(0);
	}

	@Override
	public boolean add(Object object) {
		return statement.add(object);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (StatementCreator statement : statements) {
			sb.append(statement.toString());
			sb.append('\n');
		}
		sb.append('}');
		return sb.toString();
	}

	@Override
	public List<StatementCreator> get() {
		return statements;
	}

	@Override
	public void addEol() {
		if (statement.notEmpty()) {
			statements.add(statement);
			this.statement = new StatementCreator(currentLineNumber);
		}
	}

	@Override
	public Object accept(ListProviderVisitor visitor) {
		StringBuilder sb = new StringBuilder();
		for (StatementCreator statement : statements) {
			sb.append(visitor.apply(statement.get()).toString());
		}
		return sb.toString();
	}

	@Override
	public boolean isCollectorFull() {
		return false;
	}

	@Override
	public int getLineNumber() {
		return currentLineNumber;
	}

}
