/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.pg;

import java.sql.Types;

import fi.tnie.db.AbstractAttributeWriter;
import fi.tnie.db.DefaultAttributeWriterFactory;
import fi.tnie.db.IntervalAttributeWriter;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.ColumnResolver;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.value.IntervalKey;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.types.ReferenceType;

public class PGAttributeWriterFactory
	extends DefaultAttributeWriterFactory {
	
		@Override
	public <
		A extends Attribute, 
		T extends ReferenceType<A, ?, T, E, ?, ?, M, ?>, 
		E extends Entity<A, ?, T, E, ?, ?, M, ?>, 
		M extends EntityMetaData<A, ?, T, E, ?, ?, M, ?>
	> 
	AbstractAttributeWriter<A, T, E, ?, ?, ?, ?> createWriter(M em, ColumnResolver cr, int index) {		
		Column col = cr.getColumn(index);
		A a = em.getAttribute(col);
		
		if (a == null) {
			return null;
		}		
		
		DataType type = col.getDataType();
		
		int sqltype = type.getDataType();
		final String n = type.getTypeName();
		
		AbstractAttributeWriter<A, T, E, ?, ?, ?, ?> w = null;
			
		if (sqltype == Types.OTHER && "interval".equals(n)) {	
			IntervalKey.DayTime<A, T, E> key = em.getDayTimeIntervalKey(a);
			w = new IntervalAttributeWriter.DayTime<A, T, E>(key, index);			
		}
		
		if (sqltype == Types.OTHER && "interval_ym".equals(n)) {				
			IntervalKey.YearMonth<A, T, E> key = em.getYearMonthIntervalKey(a);
			w = new IntervalAttributeWriter.YearMonth<A, T, E>(key, index);		
		}		
		
		if (w == null) {
			w = super.createWriter(em, cr, index);
		}
				
		return w;
	}

}
