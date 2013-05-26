/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appspot.relaxe.expr.ColumnExpr;
import com.appspot.relaxe.expr.ColumnName;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.Select;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;


public class MutableDataObject
	implements DataObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5199827695443766073L;
	
	private List<AbstractPrimitiveHolder<?, ?, ?>> content;
	private MutableDataObject.MetaData metaData;
				
	public static class MetaData
		implements DataObject.MetaData, Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5107557882111255138L;
		
		private int columnCount;
		private List<ValueExpression> valueList;
		private List<ColumnExpr> columnList;
		private Map<ColumnName, Integer> columnIndexMap;
		private QueryExpressionSource queryExpressionSource;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		protected MetaData() {	
		}
		
		public MetaData(QueryExpressionSource qes) 
			throws QueryException {
			this.queryExpressionSource = qes;
			QueryExpression qe = qes.getQueryExpression();			
			Select s = qe.getTableExpr().getSelect();
			
			final int cc = s.getColumnCount();			
			this.columnCount = cc;
			this.valueList = new ArrayList<ValueExpression>(s.expandValueExprList());
			this.columnList = new ArrayList<ColumnExpr>(s.expandColumnExprList());
			
			Map<ColumnName, Integer> xm = this.columnIndexMap = new HashMap<ColumnName, Integer>();
			
			int i = 0;
			
			for (ValueExpression e : this.valueList) {
				ColumnName cn = e.getColumnName();
				xm.put(cn, Integer.valueOf(i++));								
			}			
		}

		@Override
		public int getColumnCount() {
			return columnCount;
		}
		
		private int index(ColumnName n) {
			return this.columnIndexMap.get(n).intValue();
		}

		@Override
		public ColumnExpr column(int index) {
			return this.columnList.get(index); 
		}

		@Override
		public ValueExpression expr(int index) {
			return this.valueList.get(index);
		}

		@Override
		public QueryExpression getQuery() 
			throws QueryException {
			return queryExpressionSource.getQueryExpression();
		}
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected MutableDataObject() {		
	}
		
	public MutableDataObject(MetaData m) {
		super();
		this.metaData = m;
		
		int cc = m.getColumnCount();
		
		this.content = new ArrayList<AbstractPrimitiveHolder<?,?,?>>(cc);
		
		for (int i = 0; i < cc; i++) {
			this.content.add(null);
		}
	}
	
	public void set(int index, AbstractPrimitiveHolder<?, ?, ?> value) {
		this.content.set(index, value);
	}	

	public List<AbstractPrimitiveHolder<?, ?, ?>> getContent() {		
		return content;
	}

	@Override
	public DataObject.MetaData meta() {
		return this.metaData;
	}

	@Override
	public AbstractPrimitiveHolder<?, ?, ?> get(int index) {		
		return this.content.get(index);
	}
		
	public AbstractPrimitiveHolder<?, ?, ?> get(ColumnName a) {
		int index = this.metaData.index(a);
		return this.content.get(index);
	}

	@Override
	public String toString() {
		return "{" + super.toString() + ":" + this.content + "}";
	}
}