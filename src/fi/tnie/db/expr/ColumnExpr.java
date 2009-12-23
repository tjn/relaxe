package fi.tnie.db.expr;

import fi.tnie.db.QueryContext;

public class ColumnExpr
	implements QueryExpression {
	
	private AbstractTableReference table;
	private String name;
	
	public ColumnExpr(String name) {
		super();
		this.name = name;
	}

	public ColumnExpr(AbstractTableReference table, String name) {
		this(name);
		this.table = table;		
	}
	
	

	@Override
	public void generate(QueryContext qc, StringBuffer dest) {
		if (name == null) {
			throw new NullPointerException("'name' must not be null");
		}
		
		String cn = null;
		AbstractTableReference tref = getTable();
		
		if (tref != null) {
			cn = tref.getCorrelationName();
			
			if (cn == null) {
				throw new IllegalStateException("table reference (" + tref + ") is not in accessible at this point");
			}
		}
		
		if (cn != null) {
			dest.append(cn);
			dest.append(".");
		}
		
		dest.append(name);
		dest.append(" ");
	}
	
	public AbstractTableReference getTable() {
		return table;
	}

	public void setTable(AbstractTableReference table) {
		this.table = table;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}
}
