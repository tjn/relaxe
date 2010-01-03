/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;



/**
 * What does this class represents???
 * 
 * @author Administrator
 *
 */
public class NestedColumnExpr 
	extends ColumnExpr {
	
	private String name;
	private ColumnExpr inner;	
	
	public NestedColumnExpr(AbstractTableReference table, ColumnExpr inner, String name) {
		super(table);
		this.inner = inner;
		this.name = name;
	}
	
	@Override
	public String getName() {
		String n = this.name;
	
		if (n == null && this.inner != null) {
			n = this.inner.getName();
		}
		
		return n;
	}

	@Override
	public int getType() {		
		return this.inner.getType();
	}

			
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		// TODO: 
		
		vc = v.start(vc, this);		
		v.end(this);
	}
	
//	@Override
//	public String getTerminalSymbol() {		
//		return getName();
//	}
}
