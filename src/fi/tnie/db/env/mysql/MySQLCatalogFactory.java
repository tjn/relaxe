/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import fi.tnie.db.QueryException;
import fi.tnie.db.env.DefaultCatalogFactory;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.CatalogMap;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.impl.DefaultCatalogMap;
import fi.tnie.db.meta.impl.DefaultMutableCatalog;

public class MySQLCatalogFactory extends DefaultCatalogFactory {

	private static Logger logger = Logger.getLogger(MySQLCatalogFactory.class);
	
//	private String schema;	
		
	public MySQLCatalogFactory(MySQLEnvironment env) {
		super(env);	
	}

//	public MySQLCatalogFactory(MySQLEnvironment env, String schema) {
//		this(env);
//		this.schema = schema;
//	}
	
	
		
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
	
	
	
//	@Override
//	public Catalog create(DatabaseMetaData meta, String catalogName)
//		throws QueryException, SQLException {
//		
//		logger().debug("enter");
//														
//		DefaultMutableCatalog catalog = new DefaultMutableCatalog(getEnvironment());
//		
//		{
//			ResultSet schemas = meta.getCatalogs();
//			List<String> names = new ArrayList<String>();					
//			schemas = process(schemas, new StringListReader(names), true);
//			
//			logger().debug("schemas: " + names.size());
//			
//			for (String n : names) {
//				if (this.schema == null || n.equalsIgnoreCase(this.schema)) {				
//					logger().debug("processing schema: " + n);
//					Identifier sch = catalog.getEnvironment().createIdentifier(n);
//					DefaultMutableSchema s = createSchema(catalog, sch, meta);
//					logger().debug("schema: " + s.getUnqualifiedName());
//					populateSchema(s, meta);
//				}
//			}
//		}
//		
//		logger().debug("catalog:schemas: " + catalog.schemas().keySet().size());
//		
//		logger().debug("creating pk's");		
//		populatePrimaryKeys(catalog, meta);		
//		
//		logger().debug("creating fk's");		
//		populateForeignKeys(catalog, meta);
//				
//		logger().debug("exit");
//		return catalog;
//	}

	
	public static Logger logger() {
		return MySQLCatalogFactory.logger;
	}
	
//	@Override
//	protected ResultSet getTablesForSchema(DatabaseMetaData meta,
//			DefaultMutableSchema ms) throws SQLException {
//		String[] types = { "TABLE" };
//		
//		// schema corresponds catalog in MySQL:
//		return meta.getTables(ms.getName(), null, "%", types);
//	}
	
//	protected ResultSet getColumnsForTable(DatabaseMetaData meta, DefaultMutableTable t) 
//		throws SQLException {
//		DefaultMutableSchema s = t.getSchemaImpl();
//		String catalog = s.getName(); 
//					
//		return meta.getColumns(catalog, null, t.getName(), "%");		
//	}

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
