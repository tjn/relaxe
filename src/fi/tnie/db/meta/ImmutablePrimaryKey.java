/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import fi.tnie.db.expr.Identifier;

public class ImmutablePrimaryKey
	extends ImmutableConstraint
	implements PrimaryKey {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6311988355706864990L;
		
	private ColumnMap columnMap;

	@Override
	public ColumnMap getColumnMap() {
		return this.columnMap;
	}
	
	
	private ImmutablePrimaryKey() {
		super();	
	}

	private ImmutablePrimaryKey(BaseTable table, Identifier name, ColumnMap columnMap) {
		super(table, name);
		this.columnMap = columnMap;
	}


	@Override
	public Type getType() {
		return Type.PRIMARY_KEY;
	}

	
	public static class Builder
		extends ImmutableConstraint.Builder<ImmutablePrimaryKey> {

		private Identifier constraintName;
		private ImmutableColumnMap.Builder columnMapBuilder;
		
		public Builder(BaseTable table) {
			super(table);
			this.columnMapBuilder = new ImmutableColumnMap.Builder(table.getEnvironment());
		}
		
		public void add(Column c) {
			this.columnMapBuilder.add(c);
		}
		
		public ImmutablePrimaryKey newConstraint() {			
			if (this.columnMapBuilder.getColumnCount() == 0) {
				return null;
			}	
			
			ColumnMap cm = this.columnMapBuilder.newColumnMap();
			ImmutablePrimaryKey pk = new ImmutablePrimaryKey(this.getTable(), constraintName, cm);
							
			return pk;
		}

		@Override
		public Identifier getConstraintName() {
			return constraintName;
		}

		public void setConstraintName(Identifier constraintName) {
			this.constraintName = constraintName;
		}
	}
}
