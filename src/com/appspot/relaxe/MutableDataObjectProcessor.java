/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import com.appspot.relaxe.ent.MutableDataObject;
import com.appspot.relaxe.ent.QueryExpressionSource;
import com.appspot.relaxe.query.QueryException;

public class MutableDataObjectProcessor
	extends DataObjectProcessor<MutableDataObject> {
	
	private MutableDataObject content;
	private MutableDataObject result;
		
	public MutableDataObjectProcessor(ValueExtractorFactory vef, QueryExpressionSource qes) throws QueryException {
		super(vef, qes);
	}
	
	@Override
	public void prepare() {
		this.content = new MutableDataObject(getMetaData());		
	}
	
	@Override
	protected MutableDataObject get() {
		return this.content;
	}
	
	@Override
	protected void put(MutableDataObject o) {
		this.result = o;
	}
	
	public MutableDataObject getResult() {
		return result;
	}
}
