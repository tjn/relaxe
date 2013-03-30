/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import fi.tnie.db.IntegerExtractor;
import fi.tnie.db.ValueExtractor;
import fi.tnie.db.VarcharExtractor;
import fi.tnie.db.env.pg.PGTSVectorType;
import fi.tnie.db.env.pg.PGValueExtractorFactory;

public class PagilaValueExtractorFactory extends PGValueExtractorFactory {

//	private static Logger logger = Logger.getLogger(PGValueExtractorFactory.class);

	
	
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


