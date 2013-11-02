/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;


/**
 * 
 * @author Administrator
 */
public class CreateType
	extends SQLSchemaStatement {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -6135477541858193752L;
	
	private SchemaElementName domainName;
	private SQLTypeDefinition baseType;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected CreateType() {
	}
	
	public CreateType(Identifier domainName, SQLTypeDefinition baseType) {
		this(new SchemaElementName(null,  null, domainName), baseType);
	}
	
	public CreateType(SchemaElementName domainName, SQLTypeDefinition baseType) {
		super(Statement.Name.CREATE_TYPE);
		this.domainName = domainName;
		this.baseType = baseType;
	}
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.CREATE.traverse(vc, v);
		SQLKeyword.TYPE.traverse(vc, v);
		this.domainName.traverse(vc, v);
		SQLKeyword.AS.traverse(vc, v);
		this.baseType.traverse(vc, v);
	}    
		
	public SQLTypeDefinition getBaseType() {
		return baseType;
	}
	
	public SchemaElementName getDomainName() {
		return domainName;
	}
}