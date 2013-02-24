/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import fi.tnie.db.rpc.Decimal;
import fi.tnie.db.rpc.DecimalHolder;
import fi.tnie.db.types.DecimalType;

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
