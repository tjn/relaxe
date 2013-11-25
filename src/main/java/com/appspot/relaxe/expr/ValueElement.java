/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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

import java.util.Collections;
import java.util.List;

import com.appspot.relaxe.meta.IdentifierRules;

/**
 * Represents scalar-valued select-list-element.
 *  
 * @author Administrator
 */

public class ValueElement
	extends CompoundElement
	implements SelectListElement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3171506330402365606L;
	private ValueExpression expr;
	private Identifier newName;
	private Identifier name;
	
//	private static Logger logger = LoggerFactory.getLogger(ValueElement.class);
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ValueElement() {
	}
	
	public ValueElement(ValueElement e) {
		this(e.getValue(), e.getColumnName());
	}
			
	public ValueElement(ValueExpression expr) {
		this(expr, (Identifier) null);
	}

	public ValueElement(ValueExpression expr, final String newName, IdentifierRules ir)
		throws IllegalIdentifierException {		
		this(expr, ir.toIdentifier(newName));		
	}
	
	public ValueElement(ColumnReference cr) {
		this(cr, cr.getColumnName());
	}
	
	public ValueElement(ValueExpression expr, final Identifier newName) {		
		super();
		
		if (expr == null) {
			throw new NullPointerException("'expr' must not be null");
		}
		
		this.expr = expr;
		
		if (newName != null) {
			this.newName = newName;	
		}		
	}
	
	public Identifier getColumnName() {
		// TODO: fix: we should return a "made-up" name if there 
		//		is not column name available		
		Identifier cn = 
			(this.newName != null) ? this.newName : 
			(this.name != null) ? this.name :
			null;		
	
//		logger().debug("column-name: " + cn);
		return cn;
	}	
	
	public ValueExpression getValue() {
		return this.expr;
	}
		
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		this.expr.traverse(vc, v);
		
		if (newName != null) {			
			SQLKeyword.AS.traverse(vc, v);
			newName.traverse(vc, v);			
		}	
	}

	@Override
	public List<? extends Identifier> getColumnNames() {				
		return Collections.singletonList(getColumnName());
	}

	@Override
	public int getColumnCount() {	
		return 1;
	}

	@Override
	public ValueExpression getColumnExpr(int column) {
		if (column != 1) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}
		return getValue();
	}
	
	@Override
	public ColumnExpr getTableColumnExpr(int column) {
		if (column != 1) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}
						
		return null;
	}
	
//	public static Logger logger() {
//        return ValueElement.logger;
//    }
	
}
