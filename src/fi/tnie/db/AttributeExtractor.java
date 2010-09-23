/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AttributeExtractor	<
		A extends Enum<A> & Identifiable, 
		R extends Enum<R> & Identifiable,
		Q extends Enum<Q> & Identifiable,
		E extends Entity<A, R, Q, ? extends E>>
	{		
		private A attribute;		
		private ValueExtractor extractor;
		
		public AttributeExtractor(A attribute, ValueExtractor extractor) {
			super();
			this.attribute = attribute;
			this.extractor = extractor;
		}

//		public void set(E dest) {
//			extractor.extractValue(rs);
//			dest.set(this.attribute, extractor.last());			
//		}
		
		public void extract(ResultSet src, E dest) 
			throws SQLException {
			Holder h = extractor.extractValue(src);
			dest.set(this.attribute, h);			
		}		
	}