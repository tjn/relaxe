/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Types;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.meta.Column;
import fi.tnie.db.types.ReferenceType;

public class DefaultAttributeWriterFactory
	implements AttributeWriterFactory {

	@Override
	public <
		A extends Attribute, 
		T extends ReferenceType<T, M>,
		E extends Entity<A, ?, T, E, ?, ?, M>,
		M extends EntityMetaData<A, ?, T, E, ?, ?, M>
	> 
	AttributeWriter<A, T, E, ?, ?, ?, ?> createWriter(M em, ColumnResolver cr, int index) {
		AttributeWriter<A, T, E, ?, ?, ?, ?> w = null;
		
//		ColumnExpr ce = meta.column(index);
//		ColumnName cn = ce.getColumnName();
//		Column col = em.getBaseTable().columnMap().get(cn);
		
		Column col = cr.getColumn(index);		
		A a = em.getAttribute(col);
		
		if (a == null) {
			return null;
		}		
		
		int sqltype = col.getDataType().getDataType();
		
//		PrimitiveKey<A, ?, E, ?, ?, ?, ?> pk = em.getKey(a);

		switch (sqltype) {
			case Types.INTEGER:
			case Types.SMALLINT:
			case Types.TINYINT:				
				w = new IntegerAttributeWriter<A, T, E>(em.getIntegerKey(a), index);
				break;
			case Types.VARCHAR:
//				e = new VarcharAttributeExtractor<A, R, T, E>(attribute, meta, vef, col);
			case Types.CHAR:
//				e = new CharAttributeExtractor<A, R, T, E>(attribute, meta, vef, col);
				break;
			case Types.DATE:
//				e = new DateAttributeExtractor<A, R, T, E>(attribute, meta, vef, col);
				break;
//			case Types.TIMESTAMP:
//				e = new TimestampExtractor(col);
//				break;
			default:
				//
	//			e = new ObjectExtractor(colno);
				break;
			}


//		logger().debug("createExtractor - exit " + sqltype + " => " + w);

		return w;
	}

}
