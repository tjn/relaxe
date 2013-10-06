/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import com.appspot.relaxe.env.DefaultCatalogFactory2;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaName;
import com.appspot.relaxe.meta.ImmutableSchema;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.impl.mysql.MySQLEnvironment;


public class MySQLCatalogFactory extends DefaultCatalogFactory2 {

	private static Logger logger = Logger.getLogger(MySQLCatalogFactory.class);
	
	public MySQLCatalogFactory(MySQLEnvironment env) {
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
		return MySQLCatalogFactory.logger;
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
    	return s.getSchemaName().getName();
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
    	String name = (pkname == null) ? null : pkname.getName();
    	
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
