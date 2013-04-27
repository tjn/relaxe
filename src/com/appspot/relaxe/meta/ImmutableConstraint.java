/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;

public abstract class ImmutableConstraint
	extends ImmutableSchemaElement
	implements Constraint {	
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -1563851898050957700L;
	
	
	private BaseTable table;
		
	public BaseTable getTable() {
		return this.table;
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ImmutableConstraint() {
	}	
	
	protected ImmutableConstraint(BaseTable table, Identifier name) {
		super(table.getEnvironment(), new SchemaElementName(table.getName().getQualifier(), name));
		this.table = table;
	}
	
	public static abstract class Builder<C extends Constraint>
		extends MetaObjectBuilder {
		
		private BaseTable table;		
		
		protected Builder(BaseTable table) {
			super(table.getEnvironment());
			this.table = table;
		}
		
		public BaseTable getTable() {
			return table;
		}	

		public abstract Identifier getConstraintName();
	}
}
