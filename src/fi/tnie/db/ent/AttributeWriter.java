/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.rpc.PrimitiveHolder;

public interface AttributeWriter<
	A extends Attribute, E 
> {
	
	public PrimitiveHolder<?, ?, ?> write(DataObject src, E dest) throws EntityRuntimeException;

}
