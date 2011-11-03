/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.util.List;

import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.MutableDataObject;
import fi.tnie.db.ent.QueryExpressionSource;
import fi.tnie.db.query.QueryException;

public class DataObjectReader
	extends DataObjectProcessor<MutableDataObject> {
	
	private List<DataObject> content;		

	public DataObjectReader(ValueExtractorFactory vef, QueryExpressionSource qo, List<DataObject> content) 
		throws QueryException {
		super(vef, qo);
		
		if (content == null) {
			throw new NullPointerException("content");
		}
		
		this.content = content;
	}

	@Override
	protected void put(MutableDataObject o) {
		this.content.add(o);
	}

	@Override
	protected MutableDataObject get() {
		return new MutableDataObject(getMetaData());
	}
}