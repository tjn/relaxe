/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.Key;
import fi.tnie.db.ent.value.Value;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public class AttributeExtractor<
		V extends Serializable,
		P extends PrimitiveType<P>,
		H extends PrimitiveHolder<V, P>,
		A extends Serializable,
		E extends Entity<A, ?, ?, E>,
		K extends Key<A, V, P, H, E, K>>
	{		
		private ValueExtractor<V, P, H> extractor;
		private K key = null;
								
		protected AttributeExtractor(ValueExtractorFactory vef, A a, K key, int col) {
			super();
			
			if (key == null) {
				throw new NullPointerException("attribute key for: " + a);
			}
						
			this.extractor = createValueExtractor(vef, col);
			this.key = key;
		}
		
		public AttributeExtractor(ValueExtractor<V, P, H> ve, A a, K key, int col) {
			super();
			
			if (key == null) {
				throw new NullPointerException("attribute key for: " + a);
			}
						
			this.extractor = ve;
			this.key = key;
		}
		
		public void extract(ResultSet src, E dest) 
			throws SQLException {			
			H vp = extractor.extractValue(src);
			Value<A, V, P, H, E, K> v = key.value(dest);
			v.setHolder(vp);
		}
		
		protected ValueExtractor<V, P, H> createValueExtractor(ValueExtractorFactory vef, int col) {
			return null;
		}
		
	}