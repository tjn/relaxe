package fi.tnie.db.expr;

import fi.tnie.db.QueryContext;

public class NestedTableReference
	extends AbstractTableReference {

	private AbstractTableReference query;
		
	public NestedTableReference(QueryContext context, AbstractTableReference query) {
		super(context, null);
		setQuery(query);
	}

	@Override
	public void generate(QueryContext qc, StringBuffer dest) {
		AbstractTableReference query = getQuery();
		
		if (query == null) {
			throw new NullPointerException("'query' must not be null");
		}	
		
		String cn = this.getCorrelationName();
		dest.append(" (");
		query.generate(qc, dest);		
		dest.append(") AS ");
		dest.append(cn);
		dest.append(" ");
	}

	public AbstractTableReference getQuery() {
		return query;
	}

	public void setQuery(AbstractTableReference query) {
		if (query == null) {
			throw new NullPointerException("'query' must not be null");
		}

		this.query = query;
	}

	@Override
	public SelectList<QueryExpression> getSelectList() {
		// TODO: 		
		return getQuery().getSelectList();
	}
}
