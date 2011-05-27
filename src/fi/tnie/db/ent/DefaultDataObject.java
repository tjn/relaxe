/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.tnie.db.expr.ColumnExpr;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.rpc.PrimitiveHolder;

public class DefaultDataObject
	implements DataObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5199827695443766073L;
	
	private List<PrimitiveHolder<?, ?>> content;
	private DefaultDataObject.MetaData metaData;
		
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
		private QueryExpression query;
		
		/**
			 * No-argument constructor for GWT Serialization
			 */
		@SuppressWarnings("unused")
		private MetaData() {	
		}
		
		public MetaData(QueryExpression qe) {
			this.query = qe;			
			Select s = qe.getTableExpr().getSelect();
			
			final int cc = s.getColumnCount();			
			this.columnCount = cc;
			this.valueList = s.expandValueExprList();
			this.columnList = s.expandColumnExprList();
			
			Map<ColumnName, Integer> xm = this.columnIndexMap = new HashMap<ColumnName, Integer>();
			
			int i = 0;
			
			for (ValueExpression e : this.valueList) {
				ColumnName cn = e.getColumnName();
				xm.put(cn, Integer.valueOf(i++));								
			}			
		}

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
		public QueryExpression getQuery() {
			return query;
		}
	}
		
	public DefaultDataObject(MetaData m) {
		super();
		this.metaData = m;
		
		int cc = m.getColumnCount();
		
		this.content = new ArrayList<PrimitiveHolder<?,?>>(cc);
		
		for (int i = 0; i < cc; i++) {
			this.content.add(null);
		}
	}
	
	public void set(int index, PrimitiveHolder<?, ?> value) {
		this.content.set(index, value);
	}	

	public List<PrimitiveHolder<?, ?>> getContent() {		
		return content;
	}

	@Override
	public DataObject.MetaData meta() {
		return this.metaData;
	}

	@Override
	public PrimitiveHolder<?, ?> get(int index) {		
		return this.content.get(index);
	}
		
	public PrimitiveHolder<?, ?> get(ColumnName a) {
		int index = this.metaData.index(a);
		return this.content.get(index);
	}
	
	
	
}