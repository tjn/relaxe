/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fi.tnie.db.expr.Identifier;

public class ImmutableForeignKey
	extends AbstractImmutableForeignKey
	implements ForeignKey {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6311988355706864990L;
		
	private BaseTable referenced;
	
	private ImmutableForeignKey() {
		super();	
	}

	private ImmutableForeignKey(BaseTable table, BaseTable referenced, Identifier name, ColumnMap columnMap, Map<Identifier, Identifier> columnPairMap) {
		super(table, name, columnMap, columnPairMap);
		this.referenced = referenced;
	}

	public static class Builder
		extends ImmutableConstraint.Builder<ForeignKey> {
		
		private BaseTable referenced; 
		private List<Identifier> columnList;
		private Map<Identifier, Identifier> columnPairMap;
		private Identifier constraintName;		
		
		public Builder(Identifier constraintName, BaseTable referencing, BaseTable referenced) {
			super(referencing);
			
			if (constraintName == null) {
				throw new NullPointerException("constraintName");
			}
			
			this.constraintName = constraintName;			
			this.columnList = new ArrayList<Identifier>();
			this.columnPairMap = new TreeMap<Identifier, Identifier>(getEnvironment().identifierComparator());
			this.referenced = referenced;
		}
		
		public boolean add(Identifier a, Identifier b) {
			if (a == null) {
				throw new NullPointerException("a");
			}
			
			if (b == null) {
				throw new NullPointerException("b");
			}
			
			if (this.columnPairMap.containsKey(a)) {
				return false;
			}
						
			this.columnList.add(a);
			this.columnPairMap.put(a, b);
			
			return true;
		}				
		
		@Override
		public Identifier getConstraintName() {
			return this.constraintName;
		}
		
		void setReferenced(BaseTable referenced) {
			this.referenced = referenced;
		}
		
		public BaseTable getReferenced() {
			return referenced;
		}
				
		protected ImmutableForeignKey newConstraint()
			throws ElementInstantiationException {
			
			if (this.columnList.isEmpty()) {
				return null;
			}
												
			ImmutableColumnMap.Builder cmb = new ImmutableColumnMap.Builder(getEnvironment());
						
			final BaseTable table = getTable();
						
			for (Identifier n : this.columnList) {
				Column column = table.columnMap().get(n);
				
				if (column == null) {
					throw new ElementInstantiationException("no column: " + n);
				}
				
				cmb.add(column);
			}
						
			ColumnMap cm = cmb.newColumnMap();
			
			ImmutableForeignKey fk = new ImmutableForeignKey(table, getReferenced(), constraintName, cm, this.columnPairMap);
							
			return fk;
		}
	}
	
//	@Override
//	public Column getReferenced(Column referencingColumn) {
//		if (referencingColumn == null) {
//			return null;
//		}
//				
//		Identifier cn = getReferencedColumnName(referencingColumn.getUnqualifiedName());
//		ColumnMap cm = getReferenced().columnMap();
//		Column rc = cm.get(cn);
//		return rc;
//	}

	
	@Override
	public BaseTable getReferenced() {
		return this.referenced;
	}
}
