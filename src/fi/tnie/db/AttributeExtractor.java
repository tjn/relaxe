/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.ReferenceType;

public class AttributeExtractor	<
		A,
		R,
		T extends ReferenceType<T>,
		E extends Entity<A, R, T, ? extends E>>
	{		
		private A attribute;		
		private ValueExtractor<?, ?> extractor;
		
		public AttributeExtractor(A attribute, ValueExtractor<?, ?> extractor) {
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
			PrimitiveHolder<?, ?> h = extractor.extractValue(src);
			dest.set(this.attribute, h);			
		}		
	}