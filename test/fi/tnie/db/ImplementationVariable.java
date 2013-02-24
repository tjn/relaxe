/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.List;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGImplementation;

public class ImplementationVariable<I extends Implementation<I>> {
		
	private I value;
		
	public ImplementationVariable(I imp, List<DriverVersionVariable<I>> drivers) {
		super();
		this.value = imp;
		
		new PGImplementation();
	}
	
	public I implementation() {
		return value;				
	}
	
	
	
}
