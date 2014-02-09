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
package com.appspot.relaxe.rdbms.mariadb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.env.mariadb.MariaDBEnvironment;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaName;
import com.appspot.relaxe.meta.ImmutableSchema;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.rdbms.DefaultCatalogFactory;


public class MariaDBCatalogFactory extends DefaultCatalogFactory {

	private static Logger logger = LoggerFactory.getLogger(MariaDBCatalogFactory.class);
	
	public MariaDBCatalogFactory(MariaDBEnvironment env) {
		super(env);	
	}
		
	@Override
	public String getCatalogNameFromSchemas(DatabaseMetaData meta, ResultSet schemas)
			throws SQLException {		
		return null;
	}
	
	@Override
	public String getSchemaNameFromSchemas(DatabaseMetaData meta, ResultSet rs) throws SQLException {
		return rs.getString(1);
	}
	
	
	@Override
	public String getCatalogNameFromTables(DatabaseMetaData meta, ResultSet tables) throws SQLException {
		return null;
	}
	
	@Override
	public String getSchemaNameFromTables(DatabaseMetaData meta, ResultSet tables)
			throws SQLException {		
		return tables.getString(1);
	}
		
	
	public static Logger logger() {
		return MariaDBCatalogFactory.logger;
	}
	
	@Override
	protected String getCatalogPattern(SchemaName s) {
		// yes, schema pattern
		return super.getSchemaPattern(s);
	}
	
	@Override
	protected String getSchemaPattern(SchemaName s) {
		// yes, null
		return null;		
	}
	
//	@Override
//	protected Schema getSchema(DefaultMutableCatalog catalog, String sch, String cat) {		
//		return catalog.schemas().get(sch);
//	}
	
	
	@Override
	public ResultSet getSchemas(DatabaseMetaData meta) throws SQLException {	 
	    return meta.getCatalogs();
	}
	
	
    @Override
    public String getSchemaNameFromPrimaryKeys(DatabaseMetaData meta, ResultSet pkcols) throws SQLException {
        return pkcols.getString(1);     
    }

    @Override
    public String getCatalogNameFromPrimaryKeys(DatabaseMetaData meta, ResultSet pkcols) throws SQLException {
        return null;     
    }
    
    @Override
    protected String getCatalogNameParameter(SchemaName s) {
    	return s.getSchemaName().getContent();
    }
    
    @Override
    protected String getSchemaNameParameter(SchemaName s) {
    	return null;
    }
    
    @Override
    public String getCatalogNameFromImportedKeys(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {
        return null;     
    }
    
    @Override
    protected String getSchemaNameFromImportedKeys(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {
        return fkcols.getString(1);     
    }
    
    @Override
    public String getReferencingTableCatalogName(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {       
        return null;
    }
    
    @Override
    public String getReferencingTableSchemaName(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {        
        return fkcols.getString(5);
    }

//    @Override
//    public CatalogMap createAll(Connection c) throws QueryException,
//            SQLException {        
//        DefaultCatalogMap cm = new DefaultCatalogMap(getEnvironment());        
//        Catalog cat = this.create(c);
//        cm.add((DefaultMutableCatalog) cat);
//        return cm;
//    }
    
    @Override
	protected void addPrimaryKey(PrimaryKey pk, ImmutableSchema.Builder sb) {
    	Identifier pkname = (pk == null) ? null : pk.getUnqualifiedName();
    	String name = (pkname == null) ? null : pkname.getContent();
    	
		if (name != null && (!"PRIMARY".equals(name))) {
			sb.add(pk);
		}
	}	

    @Override
	public String getCatalogName(Connection c) throws SQLException {
        return null;
    }
    
    @Override
    protected Identifier id(String name) {
   		return (name == null) ? null : getEnvironment().getIdentifierRules().toDelimitedIdentifier(name);		
    }
}
