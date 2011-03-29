/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.util.List;

import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DefaultDataObject;
import fi.tnie.db.expr.QueryExpression;

public class DataObjectReader
	extends DataObjectProcessor {
	
	private List<DataObject> content;		

	public DataObjectReader(ValueExtractorFactory vef, QueryExpression qo, List<DataObject> content) {
		super(vef, qo);
		
		if (content == null) {
			throw new NullPointerException("content");
		}
		
		this.content = content;
	}

	@Override
	protected void put(DefaultDataObject o) {
		this.content.add(o);
	}

	@Override
	protected DefaultDataObject get() {
		return new DefaultDataObject(getMetaData());
	}
}