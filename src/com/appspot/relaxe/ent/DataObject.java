/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;

import com.appspot.relaxe.expr.ColumnExpr;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rpc.PrimitiveHolder;


public interface DataObject	
	extends Serializable
{	
	PrimitiveHolder<?, ?, ?> get(int index);	
	MetaData meta();
	
	public interface MetaData
		extends Serializable {		
		public int getColumnCount();
		public QueryExpression getQuery()
			throws QueryException;
		
		public ColumnExpr column(int index);
		public ValueExpression expr(int index);
	}
}
