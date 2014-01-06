/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
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
package com.appspot.relaxe.expr;

import com.appspot.relaxe.meta.Schema;

public final class SchemaName
	extends Name {

	/**
	 * 
	 */
	private static final long serialVersionUID = 231673251640312834L;
	private Identifier catalogName;

	// TODO: rename me to unqualifiedName 
	private Identifier schemaName;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	public SchemaName() {
	}
		
	public SchemaName(Schema schema) {
		this(schema, true);		
	}
	
	public SchemaName(Schema schema, boolean relative) {
		this(relative ? null : schema.getCatalogName(), schema.getUnqualifiedName());		
	}
	
	public SchemaName(Identifier catalogName, Identifier schemaName) {
		super();
		
		if (schemaName == null) {
			throw new NullPointerException();
		}
		
		this.catalogName = catalogName;		
		this.schemaName = schemaName;		
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);		
		Identifier cn = getCatalogName();
		
		if (cn != null) {
			cn.traverse(vc, v);
			Symbol.DOT.traverse(vc, v);
		}
		
		getSchemaName().traverse(vc, v);		
		v.end(this);
	}
	
	public Identifier getCatalogName() {
		return this.catalogName;
	}
	
	public Identifier getSchemaName() {
		return this.schemaName;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj == null) {
//			throw new NullPointerException();
//		}
//						
//		Comparator<Identifier> icmp = 
//			getSchema().getCatalog().identifierComparator();
//		
//		SchemaName n = (SchemaName) obj;
//				
//		return 
//			icmp.compare(n.getCatalogName(), this.getCatalogName()) == 0 && 
//			icmp.compare(n.getSchemaName(), this.getSchemaName()) == 0;
//	}
	

	 /** 
     * Returns true if this name has not qualifying catalog name. 
     * 
     * @return
     */ 
    public boolean isRelative() {
        return this.catalogName == null; 
    }
    
    public SchemaName toRelative() {
        return (this.catalogName == null) ? this : new SchemaName(null, this.schemaName); 
    }
    
    @Override
    public String toString() {
       	return generate();
    }
}
