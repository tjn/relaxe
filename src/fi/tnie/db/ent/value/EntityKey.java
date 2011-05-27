/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.gen.ent.personal.HourReport;
import fi.tnie.db.gen.ent.personal.Project;
import fi.tnie.db.gen.ent.personal.HourReport.MetaData;
import fi.tnie.db.gen.ent.personal.HourReport.Type;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;


/**
 * 
 * @author tnie
 *
 * @param <R>
 * @param <T>
 * @param <E>
 * @param <S>
 * @param <P>
 * @param <V>
 * @param <H>
 * @param <D>
 * @param <K>
 */
public interface EntityKey<	
	R extends Reference,
	T extends ReferenceType<T, S>,
	E extends Entity<?, R, T, E, ?, ?, S>,	
	S extends EntityMetaData<?, R, T, E, ?, ?, S>,
	P extends ReferenceType<P, D>,
	V extends Entity<?, ?, P, V, H, ?, D>,
	H extends ReferenceHolder<?, ?, P, V, H, D>,
	D extends EntityMetaData<?, ?, P, V, H, ?, D>,
	K extends EntityKey<R, T, E, S, P, V, H, D, K>
>
	extends Key<T, E, P, K>, Serializable
{	
	R name();	
	H newHolder(V newValue);	
	H get(E e);
	V value(E e);
	void set(E e, H newValue);
	void set(E e, V newValue);
	
	S getSource();	
	D getTarget();
	
//	void link(E referencing, R ref, V newValue);
//	EntityKey<fi.tnie.db.gen.ent.personal.HourReport.Reference, Type, HourReport, MetaData, Z, V, VH, O, ?> newKey();
	
	
}
