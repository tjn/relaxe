/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public abstract class AbstractAttribute<A extends Enum<A>, T>
	implements Attribute<A, T> {
	
	private A name;
			
	public AbstractAttribute(A name) {
		super();
		// TODO: throw npe
		this.name = name;
	}

	@Override
	public A name() {	
		return this.name;
	}

	@Override
	public abstract T getValue();



}
