/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.tnie.db.ent.DataObject;
import fi.tnie.db.expr.ColumnExpr;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.rpc.PrimitiveHolder;

public class DefaultDataObject
	implements DataObject {
	
	private List<PrimitiveHolder<?, ?>> content;
	private DefaultDataObject.MetaData metaData;
		
	public static class MetaData
		implements DataObject.MetaData {
		private int columnCount;
		private List<ValueExpression> valueList;
		private List<ColumnExpr> columnList;
		private Map<ColumnName, Integer> columnIndexMap;
		private QueryExpression query;
		
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

//		@Override
//		public ColumnExpr column(ColumnName a) {
//			return this.columnList.get(index(a)); 
//		}
		
		@Override
		public ColumnExpr column(int index) {
			return this.columnList.get(index); 
		}


//		@Override
//		public ValueExpression expr(ColumnName a) {
//			return this.valueList.get(index(a));
//		}

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
		this.content = new ArrayList<PrimitiveHolder<?,?>>(m.getColumnCount());
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
