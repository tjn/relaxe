/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import fi.tnie.db.TableMapper.Part;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.env.util.CatalogTraversal;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.source.JavaType;

public class DefaultEntityContext
	implements EntityContext {
		
	private Map<BaseTable, EntityMetaData<?, ?, ?, ?, ?>> metaMap;	
	private Catalog	catalog;
	private Catalog boundTo;	
	private ClassLoader loader;
		
	private static Logger logger = Logger.getLogger(DefaultEntityContext.class);
	
	public DefaultEntityContext() {
	    System.err.println("instantiated version: " + 2);
	}
		
	public DefaultEntityContext(Catalog catalog, ClassLoader cl) {
		this(catalog, cl, null);		
	}
	
	public DefaultEntityContext(Catalog catalog, ClassLoader cl, final TableMapper tm) {
		super();
		
		if (catalog == null) {
            throw new NullPointerException("'catalog' must not be null");
        }
		
		this.catalog = catalog;
		
		setClassLoader(cl);
		
		if (tm != null) {		
		    bindAll(tm);
		}
	}
	
	public void bindAll(final TableMapper tm) {	    	    
	    bindAll(this.catalog, tm);
	}
	
	public void bindAll(Catalog catalog, final TableMapper tm) {
	    if (tm == null) {
	        throw new NullPointerException();
	    }
	    
	    if (catalog == null) {
            throw new NullPointerException("'catalog' must not be null");
        }
	    
		if (metaMap != null) {
			metaMap.clear();
		}
		
		CatalogTraversal ct = new CatalogTraversal() {
			@Override
			public boolean visitBaseTable(BaseTable t) {
				try {
					bind(t, tm);
				} 
				catch (EntityException e) {
					logger().warn(e.getMessage());
				}
				return false;
			}			
		};
		
		ct.traverse(catalog);
		this.catalog = catalog;
		this.boundTo = this.catalog;
	}	

	protected void bind(BaseTable t, TableMapper tm)
		throws EntityException {
		
		logger().error("binding table: " + t);
		
//		Map<Part, JavaType> types = tm.entityType(table, part)t);
		
		JavaType mt = tm.entityType(t, Part.INTERFACE);
		
		if (mt == null) {
			logger().error("no interface type for " + t);
		}
		else {
			EntityMetaData<?, ?, ?, ?, ?> meta = getMetaData(t);			
			
			if (meta != null) {
				throw new IllegalStateException("duplicate meta-data mapping for table: " + t);
			}

			try {
				JavaType impl = tm.entityType(t, Part.IMPLEMENTATION);
												
				ClassLoader pl = getClassLoader();
				
				logger().debug("loading impl: " + impl.getQualifiedName());
				Class<?> m = Class.forName(impl.getQualifiedName(), false, pl);
				
//				this is ugly, isn't it:
				Entity<?,?,?,?,?> prototype = (Entity<?,?,?,?,?>) m.newInstance();								
				meta = prototype.getMetaData();			
				meta.bind(t);
				
				register(meta);				
			}
			catch (EntityException e) {
				logger().error(e.getMessage(), e);
				throw e;
			}
			catch (Exception e) {
				logger().error(e.getMessage(), e);
				throw new EntityException(e.getMessage(), e);
			}
		}		
	}

	@Override
	public EntityMetaData<?, ?, ?, ?, ?> getMetaData(BaseTable table) {					
		return getMetaMap().get(table);
	}	
	
	private void register(EntityMetaData<?, ?, ?, ?, ?> meta) {	    
		getMetaMap().put(meta.getBaseTable(), meta);		
	}

	public Map<BaseTable, EntityMetaData<?, ?, ?, ?, ?>> getMetaMap() {
		if (metaMap == null) {
			metaMap = new HashMap<BaseTable, EntityMetaData<?, ?, ?, ?, ?>>();
		}

		return metaMap;
	}	

	@Override
	public Catalog boundTo() {	
		return this.boundTo;
	}
	
	public static Logger logger() {
		return DefaultEntityContext.logger;
	}

    public ClassLoader getClassLoader() {
        return (this.loader == null) ? getClass().getClassLoader() : this.loader;            
    }

    public void setClassLoader(ClassLoader cl) {
        this.loader = cl;
    }	
	
}
