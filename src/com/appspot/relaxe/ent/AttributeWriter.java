/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.rpc.PrimitiveHolder;

public interface AttributeWriter<
	A extends Attribute, E 
> {
	
	public int getIndex();
	public PrimitiveHolder<?, ?, ?> write(DataObject src, E dest) throws EntityRuntimeException;

}
