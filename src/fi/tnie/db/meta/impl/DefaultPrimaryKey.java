package fi.tnie.db.meta.impl;

import java.util.ArrayList;
import java.util.List;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.PrimaryKey;

public class DefaultPrimaryKey 
	extends ConstraintImpl
	implements PrimaryKey {
	
	private List<DefaultMutableColumn> columnList;	
	private DefaultMutableBaseTable table;


	@Override
	public BaseTable getTable() {
		return this.table;
	}
		
	public DefaultPrimaryKey() {		
	}

	public DefaultPrimaryKey(DefaultMutableSchema schema, String name) {
		super(schema, name);
	}

	public DefaultPrimaryKey(DefaultMutableSchema schema, String name, List<DefaultMutableColumn> columnList) {
		this(schema, name);
		setColumnList(columnList);
	}	
	
	public void setColumnList(List<DefaultMutableColumn> columnList) {
		if (columnList == null) {
			throw new NullPointerException("columnList must not be null");
		}
		
		if (columnList.isEmpty()) {
			throw new IllegalArgumentException("columnList must not be empty");
		}
						
		if (this.table != null) {
			this.table.setPrimaryKey(null);
			this.table = null;
		}
						
		for (DefaultMutableColumn c : columnList) {
			if (this.table == null) {
				this.table = (DefaultMutableBaseTable) c.getParentNode();				
			}
			else {				
				ensureSameTable(c.getParentNode(), (DefaultMutableTable) this.table, 
						"all the columns of the multi-column primary key must originate from the same table");
			}						
		}
		
		this.table.setPrimaryKey(this);
		this.columnList = new ArrayList<DefaultMutableColumn>(columnList);
	}
	
	@Override
	public List<Column> columns() {
		if (this.columnList == null || this.columnList.isEmpty()) {
			return null;
		}
		
		return new ArrayList<Column>(this.columnList);
	}
	
	private void ensureSameTable(DefaultMutableTable a, DefaultMutableTable b, String msg) {
		if (a != b) {
			throw new IllegalArgumentException(msg);
		}		
	}
	
}
