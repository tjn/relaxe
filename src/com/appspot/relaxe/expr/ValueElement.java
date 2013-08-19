/*
 * Copyright (c) 2009-2013 Topi Nieminen
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
	
//	private static Logger logger = Logger.getLogger(ValueElement.class);
	
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
