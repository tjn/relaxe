/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.map.TableMapper;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;


public interface EntityContext {
		 
	EntityMetaData<?,?,?,?> getMetaData(BaseTable table);
	
	/** 
	 * Returns the catalog this context is bound to or <code>null</code> 
	 * if this context is not bound to any catalog. 
	 * 
	 * @return
	 */
	Catalog boundTo();
	
	/**
	 * Binds this context to the specific catalog by using <code>tm</code> as the mapper.
	 * 
	 * TODO: remove parameter 'tm' (it would be meaningless with enumerated catalog)   
	 * 
	 * @param catalog
	 * @param tm
	 */
	void bindAll(Catalog catalog, final TableMapper tm);
		
}
