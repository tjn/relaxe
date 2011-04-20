/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.VarcharType;

public class VarcharAttributeExtractor<
	A extends Attribute, 
	R extends Reference,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
>
	extends AttributeExtractor<A, R, T, E, String, VarcharType, VarcharHolder, VarcharKey<A, R, T, E>> {

	public VarcharAttributeExtractor(A attribute, EntityMetaData<A, R, T, E> meta, ValueExtractorFactory vef, int col) {
		super(vef, attribute, meta.getVarcharKey(attribute), col);
	}

	@Override
	protected ValueExtractor<String, VarcharType, VarcharHolder> createValueExtractor(ValueExtractorFactory vef, int col) {
		return vef.createVarcharExtractor(col);
	}
}
