package com.appspot.relaxe.service;

import java.io.IOException;
import java.io.OutputStream;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.HasLongVarBinary;
import com.appspot.relaxe.ent.value.LongVarBinaryAttribute;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public interface BinaryResultReader {

	<
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M> & HasLongVarBinary.Read<A, E, B>,
		B extends MutableEntity<A, R, T, E, B, H, F, M> & HasLongVarBinary.Write<A, E, B>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,
		RE extends EntityQueryElement<A, R, T, E, B, H, F, M, RE>
	>
	long read(EntityQuery<A, R, T, E, B, H, F, M, RE> query, LongVarBinaryAttribute<A, E, B> attr, OutputStream out) throws QueryException, IOException;

}
