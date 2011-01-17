/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import fi.tnie.db.types.Type;
import fi.tnie.db.types.VarcharType;

public class VarcharHolder
	extends StringHolder<VarcharType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2479889538730689743L;
	public static final VarcharHolder NULL_HOLDER = new VarcharHolder();
	public static final VarcharHolder EMPTY_HOLDER = new VarcharHolder("");
	
	/**
	 * TODO: should we have size?
	 */
		
	protected VarcharHolder() {
		super();
	}

	public VarcharHolder(String value) {
		super(value);
	}
	
	public static VarcharHolder valueOf(String s) {
		return 
			(s == null) ? NULL_HOLDER : 
			(s.equals("")) ? EMPTY_HOLDER :
			new VarcharHolder(s);
	}
	
	@Override
	public VarcharType getType() {
		return VarcharType.TYPE;
	}
	
	@Override
	public int getSqlType() {
		return Type.VARCHAR;
	}

}
