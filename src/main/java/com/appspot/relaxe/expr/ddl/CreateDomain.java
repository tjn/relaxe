/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
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
public class CreateDomain
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
	protected CreateDomain() {
	}
	
	public CreateDomain(Identifier domainName, SQLTypeDefinition baseType) {
		this(new SchemaElementName(null,  null, domainName), baseType);
	}
	
	public CreateDomain(SchemaElementName domainName, SQLTypeDefinition baseType) {
		super(Statement.Name.CREATE_DOMAIN);
		this.domainName = domainName;
		this.baseType = baseType;
	}
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.CREATE.traverse(vc, v);
		SQLKeyword.DOMAIN.traverse(vc, v);
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