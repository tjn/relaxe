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
	MetaData meta();
	
	public interface MetaData
		extends Serializable {		
		public int getColumnCount();
		public QueryExpression getQuery()
			throws EntityException;
		
		public ColumnExpr column(int index);
		public ValueExpression expr(int index);
	}
}
