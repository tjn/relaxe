/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
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
import com.appspot.relaxe.value.AbstractValueHolder;


public class MutableDataObject
	implements DataObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5199827695443766073L;
	
	private List<AbstractValueHolder<?, ?, ?>> content;
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
		
		this.content = new ArrayList<AbstractValueHolder<?,?,?>>(cc);
		
		for (int i = 0; i < cc; i++) {
			this.content.add(null);
		}
	}
	
	public void set(int index, AbstractValueHolder<?, ?, ?> value) {
		this.content.set(index, value);
	}	

	public List<AbstractValueHolder<?, ?, ?>> getContent() {		
		return content;
	}

	@Override
	public DataObject.MetaData meta() {
		return this.metaData;
	}

	@Override
	public AbstractValueHolder<?, ?, ?> get(int index) {		
		return this.content.get(index);
	}
		
	public AbstractValueHolder<?, ?, ?> get(Identifier a) {
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
