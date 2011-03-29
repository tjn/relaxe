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

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public class AttributeExtractor<
		A extends Attribute,
		R,
		T extends ReferenceType<T>,
		E extends Entity<A, R, T, E>,
		V extends Serializable,
		P extends PrimitiveType<P>,
		H extends PrimitiveHolder<V, P>,
		K extends PrimitiveKey<A, R, T, E, V, P, H, K>>
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
			dest.set(key, vp);
		}

		protected ValueExtractor<V, P, H> createValueExtractor(ValueExtractorFactory vef, int col) {
			return null;
		}

	}