/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.MutableDataObject;
import fi.tnie.db.ent.QueryExpressionSource;

public class MutableDataObjectProcessor
	extends DataObjectProcessor<MutableDataObject> {
	
	private MutableDataObject content;
	private MutableDataObject result;
		
	public MutableDataObjectProcessor(ValueExtractorFactory vef, QueryExpressionSource qes) throws EntityException {
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
