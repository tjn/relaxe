package fi.tnie.db.expr;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fi.tnie.db.QueryContext;

/**
 * Top-level SELECT -statement
 * @author Administrator
 */

public class Query 
	extends AbstractTableReference
	implements QueryExpression {
		
	public Query(QueryContext context) {
		super(context, null);
	}

	private Predicate where;		
	private SelectList<QueryExpression> selectList;		
	private TableRefList tableRefList;	
	private Predicate having;	
	private OrderByClause ordering;	
	
	private static Logger logger = Logger.getLogger(Query.class);
	
	public String generate(QueryContext ctx) {
		StringBuffer dest = new StringBuffer();		
		generate(ctx, dest);		
		return dest.toString();
	}
					
	@Override
	public void generate(QueryContext qc, StringBuffer dest) {
		SelectList<QueryExpression> sl = getExpressionList();
		
		if (sl == null) {
			throw new NullPointerException("'selectList' must not be null");
		}
		
		if (tableRefList == null) {
			throw new NullPointerException("'tableRefList' must not be null");
		}
		
//		List<AbstractTableReference> refs = tableRefList.getElementList();
		
		try {
//			for (AbstractTableReference r : refs) {				
//				qc.start(r, r.getCorrelationNamePrefix());
//			}
		
			dest.append("SELECT ");		
			sl.generate(qc, dest);		
			dest.append("FROM ");
			
			tableRefList.generate(qc, dest);
		
			if (where != null) {
				dest.append("WHERE ");
				where.generate(qc, dest);
			}
			
	//		dest.append(" GROUP BY ");
	//		
			if (having != null) {
				dest.append("HAVING ");
				having.generate(qc, dest);
			}
			
			if (ordering != null) {
				dest.append("ORDER BY ");
				ordering.generate(qc, dest);
			}		
		}
		finally {
//			endAll(qc, refs);
		}
	}
	
//	private void endAll(QueryContext qc, List<AbstractTableReference> refs) {
//		if (refs != null && (!refs.isEmpty())) {
//			for (AbstractTableReference r : refs) {
//				try {
//					qc.end(r);
//				}
//				catch (Exception e) {
//					logger().error(e.getMessage());
//				}
//			}
//		}
//	}

	public TableRefList getTableRefList() {
		return tableRefList;
	}

	public void setTableRefList(TableRefList tableRefList) {
		this.tableRefList = tableRefList;		
	}

	public SelectList<QueryExpression> getExpressionList() {
		if (selectList == null) {
			List<QueryExpression> el = new ArrayList<QueryExpression>();
												
			for (AbstractTableReference ref : tableRefList.getElementList()) {
				el.add(ref.getSelectList());				
			}
			
			return new SelectList<QueryExpression>(el);
		}		
		
		return selectList;
	}

	public Predicate getHaving() {
		return having;
	}

	public void setHaving(Predicate having) {
		this.having = having;
	}
	
	public static Logger logger() {
		return Query.logger;
	}

	public OrderByClause getOrdering() {
		return ordering;
	}

	public void setOrdering(OrderByClause ordering) {
		this.ordering = ordering;
	}

	@Override
	public SelectList<QueryExpression> getSelectList() {
		return getExpressionList();
	}
}
