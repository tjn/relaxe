/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.VarcharType;

public class VarcharAttributeExtractor<A extends Attribute, E extends Entity<A,?,?,E>>
	extends AttributeExtractor<String, VarcharType, VarcharHolder, A, E, VarcharKey<A, E>> {

	public VarcharAttributeExtractor(A attribute, EntityMetaData<A, ?, ?, E> meta, ValueExtractorFactory vef, int col) {
		super(vef, attribute, meta.getVarcharKey(attribute), col);
	}

	@Override
	protected ValueExtractor<String, VarcharType, VarcharHolder> createValueExtractor(ValueExtractorFactory vef, int col) {
		return vef.createVarcharExtractor(col);
	}
}
