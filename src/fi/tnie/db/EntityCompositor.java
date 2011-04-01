/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import fi.tnie.db.ent.DefaultDataObject;
import fi.tnie.db.ent.DefaultEntityQuery;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityContext;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.SelectListElement;

public class EntityCompositor
	extends DataObjectProcessor {

	public EntityCompositor(EntityContext ctx, ValueExtractorFactory vef, DefaultEntityQuery<?, ?, ?, ?> q) {
		super(vef, q.getQuery());
					
		DefaultAttributeExtractorFactory ef = new DefaultAttributeExtractorFactory();
	
		
		
		
//		select.get(0).getTableColumnExpr(0))
				
//		ef.createExtractor(attribute, meta, sqltype, col, vef)
		
		
	}
	
	@Override
	public void startQuery(ResultSetMetaData m) throws QueryException,
			SQLException {	
		super.startQuery(m);				
	}
	
	@Override
	protected void put(DefaultDataObject o) {
			
		
		super.put(o);
	}

	@Override
	protected DefaultDataObject get() {
		return new DefaultDataObject(getMetaData());
	}
}
