/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila.types;

import com.appspot.relaxe.env.pg.PGEnvironment;
import com.appspot.relaxe.meta.DataTypeMap;

public class PagilaEnvironment
	extends PGEnvironment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2500799350338943593L;
	
	private static PagilaEnvironment environment = new PagilaEnvironment();
	
	private DataTypeMap dataTypeMap = new PagilaDataTypeMap();	
		
	public static PGEnvironment environment() {
		return PagilaEnvironment.environment;
	}
	
	@Override
	public DataTypeMap getDataTypeMap() {
		return dataTypeMap;
	}
	
	
	
	
}
