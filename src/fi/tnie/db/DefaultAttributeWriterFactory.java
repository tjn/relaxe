/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Types;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.expr.ColumnExpr;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.meta.Column;
import fi.tnie.db.types.ReferenceType;

public class DefaultAttributeWriterFactory
	implements AttributeWriterFactory {

	@Override
	public <
		A extends Attribute, 
		R, 
		T extends ReferenceType<T>, 
		E extends Entity<A, R, T, E>> 
	AttributeWriter<A, R, T, E, ?, ?, ?, ?> createWriter(EntityMetaData<A, R, T, E> em, DataObject.MetaData meta, int index) {
		AttributeWriter<A, R, T, E, ?, ?, ?, ?> w = null;
		
		ColumnExpr ce = meta.column(index);		
		ColumnName cn = ce.getColumnName();
		
		Column col = em.getBaseTable().columnMap().get(cn);
		A a = em.getAttribute(col);
		int sqltype = col.getDataType().getDataType();
		
		PrimitiveKey<A, R, T, E, ?, ?, ?, ?> pk = em.getKey(a);

		switch (sqltype) {
			case Types.INTEGER:
			case Types.SMALLINT:
			case Types.TINYINT:
				IntegerKey<A, R, T, E> k = em.getIntegerKey(a);
				w = new IntegerAttributeWriter<A, R, T, E>(em.getIntegerKey(a), index);
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
