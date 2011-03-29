/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Types;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.types.ReferenceType;

public class DefaultAttributeExtractorFactory implements AttributeExtractorFactory {

	private static Logger logger = Logger.getLogger(DefaultAttributeExtractorFactory.class);

	public <
		A extends Attribute,
		R,
		T extends ReferenceType<T>,
		E extends fi.tnie.db.ent.Entity<A, R, T, E>
	>
	AttributeExtractor<A, R, T, E, ?, ?, ?, ?> createExtractor(A attribute, EntityMetaData<A, R, T, E> meta, int sqltype, int col, ValueExtractorFactory vef) {
		AttributeExtractor<A, R, T, E, ?, ?, ?, ?> e = null;

		switch (sqltype) {
			case Types.INTEGER:
			case Types.SMALLINT:
			case Types.TINYINT:
				e = new IntegerAttributeExtractor<A, R, T, E>(attribute, meta, vef, col);
				break;
			case Types.VARCHAR:
				e = new VarcharAttributeExtractor<A, R, T, E>(attribute, meta, vef, col);
			case Types.CHAR:
				e = new CharAttributeExtractor<A, R, T, E>(attribute, meta, vef, col);
				break;
			case Types.DATE:
				e = new DateAttributeExtractor<A, R, T, E>(attribute, meta, vef, col);
				break;
//			case Types.TIMESTAMP:
//				e = new TimestampExtractor(col);
//				break;
			default:
				//
	//			e = new ObjectExtractor(colno);
				break;
			}


			logger().debug("createExtractor - exit " + sqltype + " => " + e);

			return e;
		}


		private static Logger logger() {
			return DefaultAttributeExtractorFactory.logger;
		}

//		<V extends Serializable, P extends PrimitiveType<P>, H extends PrimitiveHolder<V, P>>
//		ValueExtractor<V, P, H> createExtractor2(ResultSetMetaData meta, int col, P type)
//			throws SQLException {
//			int sqltype = meta.getColumnType(col);
//
//		}

}


