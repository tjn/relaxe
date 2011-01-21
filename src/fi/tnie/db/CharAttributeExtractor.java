/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.types.CharType;

public class CharAttributeExtractor<A extends Serializable, E extends Entity<A,?,?,E>>
	extends AttributeExtractor<String, CharType, CharHolder, A, E, CharKey<A, E>> {

	public CharAttributeExtractor(A attribute, EntityMetaData<A, ?, ?, E> meta, ValueExtractorFactory vef, int col) {
		super(vef, attribute, meta.getCharKey(attribute), col);
	}

	@Override
	protected ValueExtractor<String, CharType, CharHolder> createValueExtractor(ValueExtractorFactory vef, int col) {		
		return vef.createCharExtractor(col);
	}
}
