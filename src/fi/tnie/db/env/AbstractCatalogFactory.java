/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.SchemaName;
import fi.tnie.db.meta.Environment;

public abstract class AbstractCatalogFactory
	implements CatalogFactory {
	
	private Environment environment;
	
	private static Logger logger = Logger.getLogger(AbstractCatalogFactory.class);
	
	public AbstractCatalogFactory(Environment environment) {
		super();
		this.environment = environment;
	}

	public String getCatalogNameFromSchemas(DatabaseMetaData meta, ResultSet schemas)
		throws SQLException {		
		return schemas.getString(2);
	}

	public String getSchemaNameFromSchemas(DatabaseMetaData meta, ResultSet schemas)
		throws SQLException {						
		return schemas.getString(1);
	}

	public String getSchemaNameFromTables(DatabaseMetaData meta, ResultSet tables)
			throws SQLException {
		return tables.getString(2);
	}

	public String getCatalogNameFromTables(DatabaseMetaData meta, ResultSet tables)
			throws SQLException {
		return tables.getString(1);		
	}

	public String getReferencingTableCatalogName(DatabaseMetaData meta, ResultSet fkcols)
			throws SQLException {		
		return fkcols.getString(5);
	}

	public String getReferencingTableSchemaName(DatabaseMetaData meta, ResultSet fkcols)
			throws SQLException {		
		return fkcols.getString(6);
	}

	public String getSchemaNameFromPrimaryKeys(DatabaseMetaData meta, ResultSet pkcols)
			throws SQLException {
		return pkcols.getString(2);		
	}

	public String getCatalogNameFromPrimaryKeys(DatabaseMetaData meta, ResultSet pkcols)
			throws SQLException {
	    return pkcols.getString(1);     
	}

	public String getCatalogNameFromImportedKeys(DatabaseMetaData meta, ResultSet fkcols)
			throws SQLException {
	    return fkcols.getString(5);     
	}

	protected String getCatalogPattern(SchemaName s) {		
		Identifier cn = (s == null) ? null : s.getCatalogName();
		return (cn == null) ? null : cn.getName();
	}

	protected String getSchemaPattern(SchemaName s) {
		Identifier sn = (s == null) ? null : s.getSchemaName();		
		return (sn == null) ? null : sn.getName();
	}
	
	protected String getPrimaryKeyCatalogNameParameter(SchemaName s) {		
		return getCatalogNameParameter(s);
	}
	
	protected String getPrimaryKeySchemaNameParameter(SchemaName s) {		
		return getSchemaNameParameter(s);
	}
	
	protected String getForeignKeyCatalogNameParameter(SchemaName s) {
		return getCatalogNameParameter(s);
	}
	
	protected String getForeignKeySchemaNameParameter(SchemaName s) {		
		return getSchemaNameParameter(s);
	}

	
	protected String getCatalogNameParameter(SchemaName s) {		
		Identifier cn = (s == null) ? null : s.getCatalogName();
		return (cn == null) ? null : cn.getName();
	}
	
	protected String getSchemaNameParameter(SchemaName s) {		
		Identifier n = (s == null) ? null : s.getSchemaName();
		return (n == null) ? null : n.getName();
	}


	public Environment getEnvironment() {
		return environment;
	}

	public String getCatalogName(Connection c) throws SQLException {
	    return c.getCatalog();
	}

	protected ResultSet close(ResultSet rs) {		
		try {
			if (rs != null) {
				rs.close();
			}
		} 
		catch (SQLException e) {
			logger().warn(e.getMessage(), e);
		}
		
		return null;
	}
	
	public ResultSet getSchemas(DatabaseMetaData meta) throws SQLException {                      
        return meta.getSchemas();
    }

	
	protected String getSchemaNameFromImportedKeys(DatabaseMetaData meta, ResultSet fkcols)
			throws SQLException {
				return fkcols.getString(2);		
			}

	private static Logger logger() {
		return AbstractCatalogFactory.logger;
	}
}
