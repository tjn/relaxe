/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityContext;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.env.util.CatalogTraversal;
import com.appspot.relaxe.map.JavaType;
import com.appspot.relaxe.map.TableMapper;
import com.appspot.relaxe.map.TableMapper.Part;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;


public class DefaultEntityContext
	implements EntityContext {
			
	private Map<BaseTable, EntityMetaData<?, ?, ?, ?, ?, ?, ?>> metaMap;	
	private Catalog	catalog;
	private Catalog boundTo;	
	private ClassLoader loader;
		
	private static Logger logger = LoggerFactory.getLogger(DefaultEntityContext.class);
	
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
	
	@Override
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
			EntityMetaData<?, ?, ?, ?, ?, ?, ?> meta = getMetaData(t);			
			
			if (meta != null) {
				throw new IllegalStateException("duplicate meta-data mapping for table: " + t);
			}

			try {
				JavaType impl = tm.entityType(t, Part.IMPLEMENTATION);
												
				ClassLoader pl = getClassLoader();
				
				logger().debug("loading impl: " + impl.getQualifiedName());
				Class<?> m = Class.forName(impl.getQualifiedName(), false, pl);
				
//				this is ugly, isn't it:
				Entity<?,?,?,?,?,?,?> prototype = (Entity<?,?,?,?,?,?,?>) m.newInstance();								
				meta = prototype.getMetaData();			
				// meta.bind(t);
				
				register(meta);				
			}
//			catch (EntityException e) {
//				logger().error(e.getMessage(), e);
//				throw e;
//			}
			catch (Exception e) {
				logger().error(e.getMessage(), e);
				throw new EntityException(e.getMessage(), e);
			}
		}		
	}

	@Override
	public EntityMetaData<?, ?, ?, ?, ?, ?, ?> getMetaData(BaseTable table) {					
		return getMetaMap().get(table);
	}	
	
	private void register(EntityMetaData<?, ?, ?, ?, ?, ?, ?> meta) {	    
		getMetaMap().put(meta.getBaseTable(), meta);		
	}

	public Map<BaseTable, EntityMetaData<?, ?, ?, ?, ?, ?, ?>> getMetaMap() {
		if (metaMap == null) {
			metaMap = new HashMap<BaseTable, EntityMetaData<?, ?, ?, ?, ?, ?, ?>>();
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
