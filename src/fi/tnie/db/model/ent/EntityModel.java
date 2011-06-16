/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.ent;

import java.io.Serializable;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.model.MutableValueModel;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public interface EntityModel<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	D extends EntityModel<A, R, T, E, H, F, M, D>
> 
	extends Entity<A, R, T, E, H, F, M>
	{		
	<
		V extends Serializable,
		P extends PrimitiveType<P>,
		PH extends PrimitiveHolder<V, P>,
		K extends PrimitiveKey<A, T, E, V, P, PH, K>
	>
	MutableValueModel<PH> getValueModel(K k) throws EntityRuntimeException;	

	MutableValueModel<VarcharHolder> getVarcharModel(VarcharKey<A, T, E> key) throws EntityRuntimeException;
	MutableValueModel<CharHolder> getCharModel(CharKey<A, T, E> key) throws EntityRuntimeException;
	MutableValueModel<DateHolder> getDateModel(DateKey<A, T, E> key) throws EntityRuntimeException;
	MutableValueModel<TimestampHolder> getTimestampModel(TimestampKey<A, T, E> key) throws EntityRuntimeException;	
	MutableValueModel<IntegerHolder> getIntegerModel(IntegerKey<A, T, E> key) throws EntityRuntimeException;
	
	D asModel();
}
