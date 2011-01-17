/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;

import fi.tnie.db.expr.ColumnExpr;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.rpc.PrimitiveHolder;

public interface DataObject	
	extends Serializable
{	
	PrimitiveHolder<?, ?> get(int index);
	
	/**
	 * May return null if data object is not read from result-set 
	 * or value does not map to table column. 
	 *  
	 * @param a
	 * @return
	 */
	MetaData meta();
	
	public interface MetaData {		
		public int getColumnCount();
		public QueryExpression getQuery();
		public ColumnExpr column(int index);
		public ValueExpression expr(int index);
	}

}
