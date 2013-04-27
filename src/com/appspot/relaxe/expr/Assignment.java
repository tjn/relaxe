/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public class Assignment extends CompoundElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7845480008197565746L;
	private ColumnName name;
	private Element value;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected Assignment() {
	}
	
	/**
	 * Constructs an assignment using the <code>value</code> as an expression or SQL NULL value if the <code>value</code> 
	 * is null. 
	 * 
	 * <p>	
	 * If <code>value</code> is not <code>null</code>:
	 * </p>
	 * 
	 * <p><sql> 
	 *  UPDATE T SET C = &lt;value&gt;
	 * </sql></p>
	 * 
	 * <p>If <code>value</code> is <code>null</code>:</p>
	 * <p><sql>
	 *  UPDATE T SET C = NULL 
	 * </sql></p>
	 * 
	 *  
	 * @param name
	 */	

	public Assignment(ColumnName name, ValueExpression value) {
		this(name, (Element) value);		
	}
	
	/**
	 * <p>Constructs an assignment using the SQL DEFAULT -value.</p>
	 * 
	 * <psql><sql>
	 *  UPDATE UPDATE_EXAMPLE SET NAME = DEFAULT 
	 * </sql></psql>
	 *  
	 * @param name
	 */	
	public Assignment(ColumnName name) {
		this(name, SQLKeyword.DEFAULT);
	}
	
	private Assignment(ColumnName name, Element value) {
		super();
		
		if (name == null) {
			throw new NullPointerException("'name' must not be null");
		}
		
		this.name = name;		
		this.value = (value == null) ? SQLKeyword.NULL : value;
	}		
		
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		name.traverse(vc, v);
		Symbol.EQUALS.traverse(vc, v);
		value.traverse(vc, v);
		v.end(this);
	}
}
