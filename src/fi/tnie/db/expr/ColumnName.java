/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import com.sun.org.apache.regexp.internal.recompile;

import fi.tnie.db.meta.Folding;



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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6608562374808636631L;
	private Identifier name;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ColumnName() {
	}
	
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
	
	public static ColumnName create(String name) {
		return create(name, null);
	}
	
			
	public static ColumnName create(String name, Folding folding)
		throws IllegalIdentifierException {		
		return new ColumnName(AbstractIdentifier.create(name, folding));				
	}
}
