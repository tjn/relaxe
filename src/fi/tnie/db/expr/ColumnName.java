/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;


/**
 * Represents the symbol of the column in table-reference declaration:
 * 
 * <code>
 * 	SELECT 
 *    * 
 *  FROM
 *  (
 *    ...
 *  )
 *  AS R (<column-symbol>, ...)
 * </code>
 *  
 * @author Administrator
 *
 */

public class ColumnName 
	implements Identifier {
	
	private Identifier name;
	
	public ColumnName(String name) {
		this(name, true);
	}
	
	public ColumnName(String name, boolean ordinary) {
		this(ordinary ? new OrdinaryIdentifier(name) : new DelimitedIdentifier(name));
	}
	
	public ColumnName(Identifier name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name.getName();
	}
	
	@Override
	public String getTerminalSymbol() {		
		return this.name.getName();
	}
	
	@Override
	public boolean isOrdinary() {
		return this.name.isOrdinary();
	}
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		this.name.traverse(vc, v);		
	}
}
