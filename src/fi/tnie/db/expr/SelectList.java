package fi.tnie.db.expr;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fi.tnie.db.QueryContext;

public class SelectList<E extends QueryExpression>
	implements QueryExpression {

	private LinkedHashMap<E, String> exprMap;
	
	public SelectList() {
		super();
	}
	
	public SelectList(List<E> el) {
		super();
		
		if (el == null) {
			throw new NullPointerException("'el' must not be null");
		}
		
		if (el == null) {
			throw new IllegalArgumentException("'el' must not be empty");
		}
		
		Map<E, String> m = getExprMap();
		
		for (E e : el) {
			m.put(e, null);
		}		
	}

	@Override
	public void generate(QueryContext qc, StringBuffer dest) {
		for(Iterator<Map.Entry<E, String>> iter = 
			getExprMap().entrySet().iterator(); iter.hasNext();) {

			Map.Entry<E, String> me = iter.next();
			QueryExpression e = me.getKey();
			String alias = me.getValue();
			
			e.generate(qc, dest);
			
			if (alias != null) {
				dest.append(" AS ");
				dest.append(alias);
			}
			
			if (iter.hasNext()) {
				dest.append(",");	
			}			
		}
		
		dest.append(" ");
	}

	private LinkedHashMap<E, String> getExprMap() {
		if (exprMap == null) {
			exprMap = new LinkedHashMap<E, String>();			
		}

		return exprMap;
	}

}
