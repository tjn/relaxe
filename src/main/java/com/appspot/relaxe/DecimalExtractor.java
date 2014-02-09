/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
/**
 * 
 */
package com.appspot.relaxe;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.types.DecimalType;
import com.appspot.relaxe.value.Decimal;
import com.appspot.relaxe.value.DecimalHolder;


public class DecimalExtractor
	extends ValueExtractor<Decimal, DecimalType, DecimalHolder>
{
	public DecimalExtractor(int column) {
		super(column);			
	}

	@Override
	public DecimalHolder extract(ResultSet rs) throws SQLException {
		BigDecimal v = rs.getBigDecimal(getColumn());
		
		if (v == null) {
			return DecimalHolder.NULL_HOLDER;
		}
		// TODO: unsafe
		long u = v.unscaledValue().longValue();
		return DecimalHolder.valueOf(u, v.scale());			
	}
	
}