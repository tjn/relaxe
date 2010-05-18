/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;



/**
 * Represents scalar-valued select-list-element.
 *  
 * @author Administrator
 */

public class ValueElement
	extends CompoundElement
	implements SelectListElement {
	
	private ValueExpression expr;
	private ColumnName newName;
	private ColumnName name;
	
	private static Logger logger = Logger.getLogger(ValueElement.class);
	
	public ValueElement(ValueElement e) {
		this(e.getValue(), e.getColumnName());
	}
			
	public ValueElement(ValueExpression expr) {
		this(expr, (ColumnName) null);
	}

	public ValueElement(ValueExpression expr, final String newName)
		throws IllegalIdentifierException {		
		this(expr, new ColumnName(newName));		
	}
	
	public ValueElement(ValueExpression expr, final ColumnName newName) {		
		super();
		
		if (expr == null) {
			throw new NullPointerException("'expr' must not be null");
		}
		
		this.expr = expr;
		
		if (newName != null) {
			this.newName = newName;	
		}		
	}
	
	public ColumnName getColumnName() {
		// TODO: fix: we should return a "made-up" name if there 
		//		is not column name available		
		ColumnName cn = 
			(this.newName != null) ? this.newName : 
			(this.name != null) ? this.name :
			null;		
	
		logger().debug("column-name: " + cn);
		return cn;
	}
	
	
	public ValueExpression getValue() {
		return this.expr;
	}
		
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		this.expr.traverse(vc, v);
		
		if (newName != null) {			
			Keyword.AS.traverse(vc, v);
			newName.traverse(vc, v);			
		}	
	}

	@Override
	public List<? extends ColumnName> getColumnNames() {				
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
	
	public static Logger logger() {
        return ValueElement.logger;
    }
	
}
