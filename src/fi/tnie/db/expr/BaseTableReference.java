package fi.tnie.db.expr;

import fi.tnie.db.QueryContext;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.ForeignKey;

public class BaseTableReference extends TableReference {

	public BaseTableReference(QueryContext qc, BaseTable table) {
		super(qc, table);
	}
	
	public BaseTable getBaseTable() {
		return (BaseTable) getTable();
	}
		
	public ForeignKeyJoinedTable innerJoin(String fkname) {				
		return new ForeignKeyJoinedTable(this, fkname, JoinType.INNER);		
	}
	
	public ForeignKeyJoinedTable leftJoin(String fkname) {				
		return new ForeignKeyJoinedTable(this, fkname, JoinType.LEFT);		
	}
}
