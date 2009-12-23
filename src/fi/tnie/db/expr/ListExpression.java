package fi.tnie.db.expr;

import java.util.Iterator;
import java.util.List;

import fi.tnie.db.QueryContext;

public abstract class ListExpression<E extends QueryExpression> implements QueryExpression {
	private String delim;
		
	protected ListExpression(String delim) {
		super();
		this.delim = delim;
	}

	@Override
	public void generate(QueryContext qc, StringBuffer dest) {
		List<? extends E> el = getElementList();
		
		if (el == null) {
			throw new NullPointerException("element-list must not be null");
		}
		
		if (el.isEmpty()) {
			throw new IllegalArgumentException("element-list must not be empty");
		}		
		
		dest.append(" ");
		
		for(Iterator<? extends E> ei = el.iterator(); ei.hasNext();) {
			QueryExpression e = ei.next();
			e.generate(qc, dest);
			
			if (delim != null) {
				dest.append(delim);
			}
		}
		
		dest.append(" ");
	}

	protected abstract List<? extends E> getElementList();
	
}
