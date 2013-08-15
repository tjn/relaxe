/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public abstract class Statement
	extends CompoundElement {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -7451238461402923413L;

	enum Type {
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
		ALTER_TABLE(DDL), 
		DROP_SCHEMA(DDL),
		DROP_TABLE(DDL),
		DROP_VIEW(DDL),
		DROP_CONSTRAINT(DDL),		
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
            

}
