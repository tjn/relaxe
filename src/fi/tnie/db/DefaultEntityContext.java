/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.TableMapper.Part;
import fi.tnie.db.TableMapper.Type;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.util.CatalogTraversal;

public class DefaultEntityContext
	implements EntityContext {
		
	private Map<BaseTable, EntityMetaData<?, ?, ?, ?>> metaMap;	
	private Catalog	catalog;
		
	public DefaultEntityContext(Catalog catalog, final TableMapper tm) {
		super();
		this.catalog = catalog;
				
		CatalogTraversal ct = new CatalogTraversal() {
			@Override
			public boolean visitBaseTable(BaseTable t) {
				bind(t, tm);
				return false;
			}			
		};
		
		ct.traverse(catalog);
	}

	protected void bind(BaseTable t, TableMapper tm) {
		Map<Part, Type> types = tm.entityMetaDataType(t);		
		TableMapper.Type mt = types.get(Part.INTERFACE);
		
		if (mt == null) {
			
		}
		else {
			EntityMetaData<?, ?, ?, ?> meta = getMetaData(t);			
			
			if (meta == null) {
				throw new IllegalStateException("duplicate meta-data mapping for table: " + t);
			}					 

			try {
				Class<?> m = Class.forName(mt.getQualifiedName());			
				Object mo = m.newInstance();			
				meta = (EntityMetaData<?, ?, ?, ?>) mo;			
				meta.bind(t);			
				register(meta);
			} 
			catch (ClassNotFoundException e) {
				errorClassNotFound(t, mt.getQualifiedName());
			} 
			catch (InstantiationException e) {
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) {				
				e.printStackTrace();
			} 
			catch (EntityException e) {		
				e.printStackTrace();
			}	
			
		}
		
	}

	public void errorClassNotFound(BaseTable t, String mt) {		
	}

	@Override
	public EntityMetaData<?, ?, ?, ?> getMetaData(BaseTable table) {					
		return getMetaMap().get(table);
	}	
	
	private void register(EntityMetaData<?, ?, ?, ?> meta) {
		getMetaMap().put(meta.getBaseTable(), meta);
	}

	private Map<BaseTable, EntityMetaData<?, ?, ?, ?>> getMetaMap() {
		if (metaMap == null) {
			metaMap = new HashMap<BaseTable, EntityMetaData<?, ?, ?, ?>>();
		}

		return metaMap;
	}	

	@Override
	public Catalog getCatalog() {	
		return this.catalog;
	}
	
	
}
