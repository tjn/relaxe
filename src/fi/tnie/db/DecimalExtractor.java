/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.rpc.Decimal;
import fi.tnie.db.rpc.DecimalHolder;
import fi.tnie.db.types.DecimalType;

public class DecimalExtractor
	extends ValueExtractor<Decimal, DecimalType, DecimalHolder>
{
	public DecimalExtractor(int column) {
		super(column);			
	}

	@Override
	public DecimalHolder extractValue(ResultSet rs) throws SQLException {
		BigDecimal v = rs.getBigDecimal(getColumn());
		
		if (v == null) {
			return DecimalHolder.NULL_HOLDER;
		}
		// TODO: unsafe
		long u = v.unscaledValue().longValue();
		return DecimalHolder.valueOf(u, v.scale());			
	}
	
}