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
package com.appspot.relaxe.pg.pagila;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.appspot.relaxe.IntegerExtractor;
import com.appspot.relaxe.ValueExtractor;
import com.appspot.relaxe.VarcharExtractor;
import com.appspot.relaxe.env.pg.PGTSVectorType;
import com.appspot.relaxe.pg.pagila.types.MPAARating;
import com.appspot.relaxe.pg.pagila.types.MPAARatingHolder;
import com.appspot.relaxe.pg.pagila.types.MPAARatingType;
import com.appspot.relaxe.pg.pagila.types.YearType;
import com.appspot.relaxe.rdbms.pg.PGValueExtractorFactory;


public class PagilaValueExtractorFactory extends PGValueExtractorFactory {

//	private static Logger logger = LoggerFactory.getLogger(PGValueExtractorFactory.class);

	
	
	@Override
	public	
	ValueExtractor<?, ?, ?> createExtractor(int sqltype, String typename, int col) 
		throws SQLException {		
						
		ValueExtractor<?, ?, ?> ve = null;
		
		if (sqltype == Types.OTHER) {
			if (MPAARatingType.TYPE.getName().equals(typename)) {
				ve = new MPAARatingExtractor(col);	
			}
			else if (PGTSVectorType.TYPE.getName().equals(typename)) {
				ve = new VarcharExtractor(col);
			}			
		}
		else if (sqltype == Types.DISTINCT) {
			if (YearType.TYPE.getName().equals(typename)) {
				ve = IntegerExtractor.forColumn(col);
			}
		}
		else {
			ve = super.createExtractor(sqltype, typename, col);
		}	
		
		return ve;
	}
	
	
	private static class MPAARatingExtractor
		extends ValueExtractor<MPAARating, MPAARatingType, MPAARatingHolder> {
		
		public MPAARatingExtractor(int column) {
			super(column);
		}

		@Override
		public MPAARatingHolder extractValue(ResultSet rs) throws SQLException {
			String v = rs.getString(getColumn());
			return MPAARatingHolder.valueOf(v);
		}
	}	
	
	
}


