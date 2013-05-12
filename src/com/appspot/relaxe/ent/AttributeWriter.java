/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;

public interface AttributeWriter<
	A extends Attribute, E 
> {
	
	public AbstractPrimitiveHolder<?, ?, ?> write(DataObject src, E dest) throws EntityRuntimeException;

}
