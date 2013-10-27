/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;

public interface SchemaElement
	extends MetaObject {
	
	Environment getEnvironment();
	SchemaElementName getName();
	SchemaElementName getName(boolean relative);
	
	@Override
	Identifier getUnqualifiedName();
	String getQualifiedName();
}
