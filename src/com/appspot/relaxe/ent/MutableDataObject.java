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
import com.appspot.relaxe.expr.Identifier;
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
		private Map<Identifier, Integer> columnIndexMap;
		private QueryExpression queryExpression;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		protected MetaData() {	
		}
		
		public MetaData(QueryExpression qe) 
			throws QueryException {
			this.queryExpression = qe;
						
			Select s = qe.getTableExpr().getSelect();
			
			final int cc = s.getColumnCount();			
			this.columnCount = cc;
			this.valueList = new ArrayList<ValueExpression>(s.expandValueExprList());
			this.columnList = new ArrayList<ColumnExpr>(s.expandColumnExprList());
			
			Map<Identifier, Integer> xm = this.columnIndexMap = new HashMap<Identifier, Integer>();
			
			int i = 0;
			
			for (ValueExpression e : this.valueList) {
				Identifier cn = e.getColumnName();
				xm.put(cn, Integer.valueOf(i++));								
			}			
		}

		@Override
		public int getColumnCount() {
			return columnCount;
		}
		
		private int index(Identifier n) {
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
		public QueryExpression getQueryExpression() {
			return queryExpression;
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
		
	public AbstractPrimitiveHolder<?, ?, ?> get(Identifier a) {
		int index = this.metaData.index(a);
		return this.content.get(index);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("{").append(super.toString()).append(":");
		buf.append(this.content).append("}");		
		return buf.toString();
	}
}
