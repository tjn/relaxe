/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.ent;

import java.io.Serializable;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public interface EntityModel<
	A extends Attribute,
	T extends ReferenceType<T, ?>,	
	E extends Entity<A, ?, T, E, ?, ?, ?>,	
	D extends EntityModel<A, T, E, D>
> {
	
	<
		V extends Serializable,
		P extends PrimitiveType<P>,
		H extends PrimitiveHolder<V, P>,
		K extends PrimitiveKey<A, T, E, V, P, H, K>,
		AM extends AttributeModelMap<A, V, P, H, T, E, AM>
	>
	AM get(K k);

	ValueModel<VarcharHolder> getVarcharModel(VarcharKey<A, T, E> key);
	ValueModel<CharHolder> getCharModel(CharKey<A, T, E> key);
	ValueModel<DateHolder> getDateModel(DateKey<A, T, E> key);
	ValueModel<TimestampHolder> getTimestampModel(TimestampKey<A, T, E> key);	
	ValueModel<IntegerHolder> getIntegerModel(IntegerKey<A, T, E> key);
		
	D self();
	
}
