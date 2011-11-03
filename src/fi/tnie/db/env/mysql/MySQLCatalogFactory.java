/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import fi.tnie.db.env.DefaultCatalogFactory;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.CatalogMap;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.impl.DefaultCatalogMap;
import fi.tnie.db.meta.impl.DefaultMutableCatalog;
import fi.tnie.db.meta.impl.mysql.MySQLEnvironment;
import fi.tnie.db.query.QueryException;

public class MySQLCatalogFactory extends DefaultCatalogFactory {

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
	protected String getCatalogPattern(Schema s) {		
		return super.getSchemaPattern(s);
	}
	
	@Override
	protected String getSchemaPattern(Schema s) {
		return null;		
	}
	
	@Override
	protected Schema getSchema(DefaultMutableCatalog catalog, String sch, String cat) {		
		return catalog.schemas().get(sch);
	}
	
	
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
    public String getCatalogNameFromImportedKeys(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {
        return null;     
    }
    
    @Override
    protected String getSchemaNameFromImportedKeys(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {
        return fkcols.getString(5);     
    }
    
    @Override
    public String getReferencedTableCatalogName(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {       
        return null;
    }
    
    @Override
    public String getReferencedTableSchemaName(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {        
        return fkcols.getString(5);
    }

    @Override
    public CatalogMap createAll(Connection c) throws QueryException,
            SQLException {        
        DefaultCatalogMap cm = new DefaultCatalogMap(getEnvironment());        
        Catalog cat = this.create(c);
        cm.add((DefaultMutableCatalog) cat);
        return cm;
    }

    @Override
	public String getCatalogName(Connection c) throws SQLException {
        return null;
    }
}
