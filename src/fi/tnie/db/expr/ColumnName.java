/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;



/**
 * Represents the unqualified name of the column.
 * symbol of the column in table-reference declaration:
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
 */

public class ColumnName 
	implements Identifier {
	
	private final Identifier name;
	
	public ColumnName(String name) 
		throws IllegalIdentifierException {
		this(name, true);
	}
	
	public ColumnName(String name, boolean ordinary) 
		throws IllegalIdentifierException {
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
		v.start(vc, this);
		v.end(this);
	}
		
	public static ColumnName create(String name)
		throws IllegalIdentifierException {		
		return new ColumnName(AbstractIdentifier.create(name));				
	}
}
