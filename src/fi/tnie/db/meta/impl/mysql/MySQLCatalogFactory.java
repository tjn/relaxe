/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.impl.DefaultCatalogFactory;
import fi.tnie.db.meta.impl.DefaultMutableCatalog;
import fi.tnie.db.meta.impl.DefaultMutableSchema;
import fi.tnie.db.meta.util.StringListReader;

public class MySQLCatalogFactory extends DefaultCatalogFactory {

	private static Logger logger = Logger.getLogger(MySQLCatalogFactory.class);
	
	private String schema;	
		
	public MySQLCatalogFactory() {
		super();	
	}

	public MySQLCatalogFactory(String schema) {
		this();
		this.schema = schema;
	}
	
	@Override
	public Catalog create(DatabaseMetaData meta, String catalogName)
		throws SQLException {
		
		logger().debug("enter");
										
		DefaultMutableCatalog catalog = new DefaultMutableCatalog(this);
		
		{
			ResultSet schemas = meta.getCatalogs();
			List<String> names = new ArrayList<String>();					
			schemas = process(schemas, new StringListReader(names), true);
			
			logger().debug("schemas: " + names.size());
			
			for (String n : names) {
				if (this.schema == null || n.equalsIgnoreCase(this.schema)) {				
					logger().debug("processing schema: " + n);
					Identifier sch = catalog.getEnvironment().createIdentifier(n);
					DefaultMutableSchema s = createSchema(catalog, sch, meta);
					logger().debug("schema: " + s.getUnqualifiedName());
					populateSchema(s, meta);
				}
			}
		}
		
		logger().debug("catalog:schemas: " + catalog.schemas().keySet().size());
		
		logger().debug("creating pk's");		
		populatePrimaryKeys(catalog, meta);		
		
		logger().debug("creating fk's");		
		populateForeignKeys(catalog, meta);
				
		logger().debug("exit");
		return catalog;
	}

	
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
}
