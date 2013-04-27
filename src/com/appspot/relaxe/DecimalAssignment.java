/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.appspot.relaxe.rpc.Decimal;
import com.appspot.relaxe.rpc.DecimalHolder;
import com.appspot.relaxe.types.DecimalType;

public class DecimalAssignment
	extends AbstractParameterAssignment<Decimal, DecimalType, DecimalHolder> {

	public DecimalAssignment(DecimalHolder value) {
		super(value);
	}

	@Override
	public void assign(PreparedStatement ps, int ordinal, Decimal v)
			throws SQLException {		
		BigDecimal n = BigDecimal.valueOf(v.getUnscaled(), v.getScale());
		ps.setBigDecimal(ordinal, n);
	}
}
