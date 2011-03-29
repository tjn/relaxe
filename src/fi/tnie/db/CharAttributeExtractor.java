/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.types.CharType;
import fi.tnie.db.types.ReferenceType;

public class CharAttributeExtractor<
	A extends Attribute, 
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
>
	extends AttributeExtractor<A, R, T, E, String, CharType, CharHolder, CharKey<A, R, T, E>> {

	public CharAttributeExtractor(A attribute, EntityMetaData<A, R, T, E> meta, ValueExtractorFactory vef, int col) {
		super(vef, attribute, meta.getCharKey(attribute), col);
	}

	@Override
	protected ValueExtractor<String, CharType, CharHolder> createValueExtractor(ValueExtractorFactory vef, int col) {
		return vef.createCharExtractor(col);
	}
}
