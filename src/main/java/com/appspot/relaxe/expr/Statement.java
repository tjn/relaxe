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

public abstract class Statement
	extends CompoundElement {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -7451238461402923413L;

	public enum Type {
		/**
		 * CREATE_SCHEMA/ALTER_TABLE/DROP_SCHEMA/TRUNCATE/COMMENT/RENAME -statement 
		 */			  
		DATA_DEFINITION_LANGUAGE,
		/**
		 * SELECT/
		 */
		DATA_MANIPULATION_LANGUAGE,
		
		/**
		 * GRANT/REVOKE
		 */
		DATA_CONTROL_LANGUAGE,
		/**
		 * COMMIT/SAVEPOINT/ROLLBACK/SET TRANSACTION 
		 */
		TRANSACTION_CONTROL	
	}
		
	public static final Type DDL = Type.DATA_DEFINITION_LANGUAGE;	
	public static final Type DML = Type.DATA_MANIPULATION_LANGUAGE;	
	public static final Type DCL = Type.DATA_CONTROL_LANGUAGE;	
	public static final Type TCL = Type.TRANSACTION_CONTROL;
	
	public enum Name {
//	DDL:
		CREATE_SCHEMA(DDL),
		CREATE_TABLE(DDL),
		CREATE_VIEW(DDL),
		CREATE_DOMAIN(DDL),
		CREATE_TYPE(DDL),
		ALTER_TABLE(DDL), 
		DROP_SCHEMA(DDL),
		DROP_TABLE(DDL),
		DROP_VIEW(DDL),				
		COMMENT(DDL),
		RENAME(DDL),		

//	DML:
		SELECT(DML),
		INSERT(DML), 
		UPDATE(DML),
		DELETE(DML),
		CALL(DML),
		TRUNCATE(DML),
		EXPLAIN_PLAN(DML),
		
//	DCL:
		GRANT(DCL),
		REVOKE(DCL),

//	TCL:
		COMMIT(TCL),
		SAVEPOINT(TCL),
		ROLLBACK(TCL),
		SET_TRANSACTION(TCL)
		;
		
		private Type type;
				
		private Name(Type t) {
			this.type = t;
		}
		
		private Type getType() {
			return this.type;
		}
	}	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected Statement() {
	}
	
	
	private Name name;
	
	protected Statement(Name name) {
		super();
		
		if (name == null) {
			throw new NullPointerException("'name' must not be null");
		}
		
		this.name = name;
	}

	public Name getName() {
		return this.name;
	}
	
	public Type getType() {
		return getName().getType();
	}
	
	protected SQLKeyword cascade(Boolean cascade) {
	    if (cascade == null) {
	        return null;
	    }
	
	    return cascade.booleanValue() ? SQLKeyword.CASCADE : SQLKeyword.RESTRICT;
    }
	

	protected SchemaElementName relativize(SchemaElementName n) {	    
	    SchemaName q = n.getQualifier();
	    
	    if (q == null || q.isRelative()) {
	        return n;
	    }
	    
	    return new SchemaElementName(n.getQualifier().toRelative(), n.getUnqualifiedName());
	}
	
	
	public SelectStatement asSelectStatement() {
		return null;
	}
            

}
