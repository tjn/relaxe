/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import fi.tnie.db.ValueExtractor;
import fi.tnie.db.env.pg.PGValueExtractorFactory;
import fi.tnie.db.rpc.StringArray;

public class PagilaValueExtractorFactory extends PGValueExtractorFactory {

//	private static Logger logger = Logger.getLogger(PGValueExtractorFactory.class);
	
	@Override
	public	
	ValueExtractor<?, ?, ?> createExtractor(ResultSetMetaData meta, int col) 
		throws SQLException {		
		
		int sqltype = meta.getColumnType(col);
		String ctn = meta.getColumnTypeName(col);
						
		ValueExtractor<?, ?, ?> ve = null;
		
		if (sqltype == Types.OTHER && MPAARatingType.TYPE.getName().equals(ctn)) {		
			ve = new MPAARatingExtractor(col);
		}
		else if (sqltype == Types.ARRAY && PGTextArrayType.TYPE.getName().equals(ctn)) {
			ve = new PGTextArrayExtractor(col);
		}		
		else {
			ve = super.createExtractor(meta, col);
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
	
	
	private static class PGTextArrayExtractor
		extends ValueExtractor<StringArray, PGTextArrayType, PGTextArrayHolder> {
	
		public PGTextArrayExtractor(int column) {
			super(column);
		}

		@Override
		public PGTextArrayHolder extractValue(ResultSet rs) throws SQLException {
			Array a = rs.getArray(getColumn());
			
			if (a == null) {
				return PGTextArrayHolder.NULL_HOLDER;
			}
			
			String[] ac = (String[]) a.getArray();
			StringArray sa = new StringArray(ac);
			return PGTextArrayHolder.newHolder(sa);
		}
	}	
	
	
}


