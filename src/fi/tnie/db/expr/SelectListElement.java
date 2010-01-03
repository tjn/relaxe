/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class SelectListElement
	extends CompoundElement {
	
	private ValueExpression expr;
	private ColumnName newName;
	private ColumnName name;
	
	public SelectListElement(SelectListElement e) {
		this(e.getValue(), null);
		this.name = e.getColumnName();
	}
			
	public SelectListElement(ValueExpression expr) {
		this(expr, null);
	}

	public SelectListElement(ValueExpression expr, final String newName) {
		super();
		
		if (expr == null) {
			throw new NullPointerException("'expr' must not be null");
		}
		
		this.expr = expr;
		
		if (newName != null) {
			this.newName = new ColumnName(newName);	
		}		
	}
	
	public ColumnName getColumnName() {
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
	
	
}
