package fi.tnie.db.expr;

import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.QueryContext;
import fi.tnie.db.meta.Table;

public class TableReference extends AbstractTableReference {
	private Table table;

	public TableReference(QueryContext qc, Table table) {
		super(qc, null);
		
		if (table == null) {
			throw new NullPointerException("'table' must not be null");
		}
		
		this.table = table;
	}
	
	@Override
	public void generate(QueryContext qc, StringBuffer dest) {		
		Table t = getTable();
		
		if (t == null) {
			throw new NullPointerException("table must not be null");
		}	
		
		String cn = getCorrelationName();					
		dest.append(" ");			
		// TODO: specifing qualified name?
		dest.append(t.getName());
						
		dest.append(" ");
		dest.append(cn);
		dest.append(" ");
	}
	
	public Table getTable() {
		return table;
	}

	@Override
	public SelectList<QueryExpression> getSelectList() {		
		List<QueryExpression> cl = new ArrayList<QueryExpression>();
		
		for (String n : getTable().columns().keySet()) {
			cl.add(new ColumnExpr(this, n));
		}
				
		return new SelectList<QueryExpression>(cl);
	}
	
}
