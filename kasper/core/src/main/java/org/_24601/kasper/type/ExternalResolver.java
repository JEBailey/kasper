package org._24601.kasper.type;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org._24601.kasper.api.Executable;
import org._24601.kasper.api.ListProvider;
import org._24601.kasper.api.ListProviderVisitor;
import org._24601.kasper.core.KasperBindings;
import org._24601.kasper.core.KasperContext;
import org._24601.kasper.error.KasperException;
import org._24601.kasper.error.KasperRuntimeException;
import org._24601.kasper.lang.Utils;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class ExternalResolver implements Executable, ListProvider {


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
	public Object execute(KasperBindings scope, List<Object> list)  {
		KasperBindings bindings = (KasperBindings)scope.get(KasperContext.engineScopeID);
		Object reply = bindings.get(key);
		if (reply == null){
			reply = scope.get(key);
		}
		if (expression != null){
			try {
				reply = Ognl.getValue(expression, context, reply);
			} catch (OgnlException e) {
				throw new KasperRuntimeException(e.getMessage());
			}
		}
	
		if (reply instanceof Boolean){
			
		}
		
		if (reply instanceof Collection<?>){
			
		}
		
		if (reply.getClass().isArray()){
			for (Object a:Arrays.asList(reply)){
				
			}
		}
		
		return reply;
	}

	@Override
	public Object accept(ListProviderVisitor visitor)
			throws KasperException {
		return visitor.apply(Arrays.asList(this));
	}

}
