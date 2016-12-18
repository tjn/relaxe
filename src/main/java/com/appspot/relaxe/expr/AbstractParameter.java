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
package com.appspot.relaxe.expr;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ValueHolder;


/**
 * Value should be primitive holder
 * @author tnie
 *
 */
public abstract class AbstractParameter<
	V extends Serializable,
	T extends ValueType<T>, 
	H extends ValueHolder<V, T, H>
>
	implements Parameter<V, T, H>, ValueExpression, SelectListElement, Token {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3615036325774581065L;
	private String name;
	private DataType columnType;
	private Identifier columnName;	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractParameter() {
	}
	
	public AbstractParameter(Identifier columnName, DataType type) {
		if (columnName == null) {
			throw new NullPointerException("columnName");
		}
		
		if (type == null) {
			throw new NullPointerException("type");
		}
		
		this.columnName = columnName;
		this.columnType = type;
	}
		
	public AbstractParameter(Column column) {
		this.columnName = column.getColumnName();
		this.columnType = column.getDataType();
	}

	@Override
	public int getType() {
		return this.columnType.getDataType();
	}
	
	@Override
	public abstract H getValue();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isOrdinary() {
			return false;
	}

	@Override
	public String getTerminalSymbol() {		
		return "?";
	}

	@Override
	public Identifier getColumnName() {
		return this.columnName;
	}

	@Override
	public int getColumnCount() {
			return 1;
	}

	@Override
	public List<Identifier> getColumnNames() {
		return Collections.singletonList(this.columnName);
	}
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		Parameter<?, ?, ?> p = this;
		v.start(vc, p);
		v.end(p);
	}

	@Override
	public ValueExpression getColumnExpr(int column) {
		if (column != 1) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}
		
		return this;
	}
	
	@Override
	public DataType getColumnType() {
		return this.columnType;
	}
	
	
	
		
//	@Override
//	public ColumnExpr getTableColumnExpr(int column) {
//		if (column != 1) {
//			throw new IndexOutOfBoundsException(Integer.toString(column));
//		}
//		
//		return null;
//	}
	
	@Override
	public SQLKeyword asDefaultSpecification() {
		return null;
	}
	
	@Override
	public SQLKeyword asNullSpecification() {
		return null;
	}
	
	@Override
	public ValueExpression asValueExpression() {
		return this;
	}	
}
