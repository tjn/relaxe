package com.appspot.relaxe.ent.query;

import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public final class EntityQueryColumn<
	A extends com.appspot.relaxe.ent.AttributeName,
	R extends com.appspot.relaxe.ent.Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,		
	M extends EntityMetaData<A, R, T, E, B, H, F, M>,
	QE extends EntityQueryElement<A, R, T, E, B, H, F, M, QE>
> {				
	
	private QE queryElement;
	private A name;
	private int ordinal;
					
	public EntityQueryColumn(QE queryElement, A name, int ordinal) {
		super();
		this.queryElement = queryElement;
		this.name = name;
		this.ordinal = ordinal;
	}		
	
	public QE getQueryElement() {
		return queryElement;
	}
	
	public A getAttribute() {
		return name;
	}
	
	public int getOrdinal() {
		return ordinal;
	}
}