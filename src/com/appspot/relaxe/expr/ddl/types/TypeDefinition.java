/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.ddl.ColumnDataType;

/**
 * Simple type name.
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */

public class TypeDefinition
	extends SQLTypeDefinition
	implements ColumnDataType {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6588251307620934502L;
	
	private SchemaElementName name;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected TypeDefinition() {
	}

	public TypeDefinition(SchemaElementName name) {
		super();
		
		if (name == null) {
			throw new NullPointerException("name");
		}
		
		this.name = name;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		this.name.traverse(vc, v);
		v.end(this);
	}

	@Override
	public SQLTypeDefinition.Name getSQLTypeName() {
		return null;
	}
}
