/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class Assignment extends CompoundElement {

	private ColumnName name;
	private Element value;

	public Assignment(ColumnName name, ValueExpression value) {
		this(name, (Element) value);		
	}
	
	public Assignment(ColumnName name) {
		this(name, Keyword.DEFAULT);
	}
	
	private Assignment(ColumnName name, Element value) {
		super();
		
		if (name == null) {
			throw new NullPointerException("'name' must not be null");
		}
		
		this.name = name;		
		this.value = (value == null) ? Keyword.NULL : value;
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
