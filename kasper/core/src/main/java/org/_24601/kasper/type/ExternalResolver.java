package org._24601.kasper.type;

import org._24601.kasper.api.Executable;
import org._24601.kasper.api.StatementProvider;
import org._24601.kasper.api.StatementProviderVisitor;
import org._24601.kasper.core.KasperBindings;
import org._24601.kasper.core.KasperContext;
import org._24601.kasper.error.KasperException;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class ExternalResolver implements Executable, StatementProvider {


	private String key = "";
	
	private OgnlContext context = new OgnlContext();

	private Object expression;
	
	public ExternalResolver(String value) {
		this.key = value;
		int index = value.indexOf('.');
		if (index > 0){
			this.key = value.substring(0, index);
			try {
				this.expression = Ognl.parseExpression(value.substring(index+1));
			} catch (OgnlException e) {
				throw new UnsupportedOperationException();
			}
		}
		
	}

	@Override
	public Object execute(KasperBindings scope, Statement statement) throws KasperException {
		KasperBindings bindings = (KasperBindings)scope.get(KasperContext.engineScopeID);
		Object reply = bindings.get(key);
		if (reply == null){
			reply = scope.get(key);
		}
		if (expression != null){
			try {
				reply = Ognl.getValue(expression, context, reply);
			} catch (OgnlException e) {
				throw new KasperException(0,e.getMessage());
			}
		}
		return reply;
	}

	@Override
	public Object accept(StatementProviderVisitor visitor)
			throws KasperException {
		return visitor.apply(new Statement(this));
	}

}
