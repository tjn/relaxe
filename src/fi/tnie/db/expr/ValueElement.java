/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.Collections;
import java.util.List;



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
		
		return 
			(this.newName != null) ? this.newName : 
			(this.name != null) ? this.name :
			null;		
	}
	
	
	public ValueExpression getValue() {
		return this.expr;
	}
	

//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		expr.generate(qc, dest);
//	
//		if (newName != null) {
//			dest.append(" AS ");
//			dest.append(newName);
//			dest.append(" ");
//		}
//	}
		
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
	
	
}
