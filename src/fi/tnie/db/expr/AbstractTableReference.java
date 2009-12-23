package fi.tnie.db.expr;

import fi.tnie.db.QueryContext;

public abstract class AbstractTableReference implements QueryExpression {

//	private String correlationNamePrefix;
	private QueryContext context;
	private String correlationName;	
			
	public AbstractTableReference(QueryContext context, String correlationNamePrefix) {
		super();
		
		if (context == null) {
			throw new NullPointerException("'context' must not be null");
		}
		
		this.context = context;
		this.correlationName = context.name(this, correlationNamePrefix);
	}

//	public String getCorrelationNamePrefix() {
//		return correlationNamePrefix;
//	}

//	public void setCorrelationNamePrefix(String correlationName) {
//		this.correlationNamePrefix = correlationName;
//	}
	
	public String getCorrelationName() {
		return this.correlationName;
	}
	
	public JoinedTable innerJoin(AbstractTableReference right, JoinCondition jc) {
		return new JoinedTable(this, right, JoinType.INNER, jc);
	}
	
	
	public JoinedTable leftJoin(AbstractTableReference right, JoinCondition jc) {
		return new JoinedTable(this, right, JoinType.LEFT, jc);
	}
	
	public abstract SelectList<QueryExpression> getSelectList();
	
	
	public TableRefList asList()  {
		return new TableRefList(this);
	}

	public QueryContext getContext() {
		return context;
	}

}
