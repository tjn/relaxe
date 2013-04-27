/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe;

import java.util.List;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.MutableDataObject;
import com.appspot.relaxe.ent.QueryExpressionSource;
import com.appspot.relaxe.query.QueryException;


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