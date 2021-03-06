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
package com.appspot.relaxe.rdbms;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.SchemaName;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataTypeImpl;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.ImmutableBaseTable;
import com.appspot.relaxe.meta.ImmutableCatalog;
import com.appspot.relaxe.meta.ImmutableColumn;
import com.appspot.relaxe.meta.ImmutableForeignKey;
import com.appspot.relaxe.meta.ImmutablePrimaryKey;
import com.appspot.relaxe.meta.ImmutableSchema;
import com.appspot.relaxe.meta.ImmutableTable;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.SchemaElementMap;
import com.appspot.relaxe.meta.SchemaElementNameMap;
import com.appspot.relaxe.meta.SchemaNameComparator;
import com.appspot.relaxe.meta.Table;
import com.appspot.relaxe.query.QueryException;

import java.sql.DatabaseMetaData;

import com.appspot.relaxe.io.IOHelper;

public class DefaultCatalogFactory 
	extends AbstractCatalogFactory {

	private static Logger logger = LoggerFactory.getLogger(DefaultCatalogFactory.class);
		
//	TODO: requiring environment as an argument for constructor, makes
//	instantiating via Class.newInstance() (too) painful 
//	in Environment -implementations.
	public DefaultCatalogFactory(Environment environment) {
		super(environment);		
	}	
	
	@Override
	public Catalog create(Connection c) 
		throws QueryException, SQLException {
		
		Environment env = getEnvironment();		
		DatabaseMetaData meta = c.getMetaData();
		
		logger().info("catalog-factory: " + toString());	
		logger().info("user: " + meta.getUserName());
		logger().info("url: " + meta.getURL());
		logger().info("driver-name: " + meta.getDriverName());
		logger().info("driver-version: " + meta.getDriverVersion());
		logger().info("environment: " + env.getClass());
		logger().info("identifier-rules: " + env.getIdentifierRules().getClass());
		logger().info("identifier-comparator: " + env.getIdentifierRules().comparator());
		
						
		try  {
			prepare(meta);
			
			String current = getCatalogName(c);
			logger().info("current: " + current);
			
			Identifier catalog = id(current);
									
			ImmutableCatalog.Builder cb = new ImmutableCatalog.Builder(env, catalog);
						
			createSchemas(meta, cb);			
		
			Catalog cat = cb.newCatalog();
		
			return cat;
							
		}
		finally {		
			finish();
		}
	}

    protected void finish() {
		
	}

	private void createSchemas(DatabaseMetaData meta, ImmutableCatalog.Builder cb) throws SQLException {

		logger().debug("createSchemas - enter");
		
    	ResultSet schemas = null;
    	List<SchemaName> names = new ArrayList<SchemaName>();
    	
    	try {
    		schemas = getSchemas(meta);
    	
	    	while(schemas.next()) {
	    		String c = getCatalogNameFromSchemas(meta, schemas);
	    		String s = getSchemaNameFromSchemas(meta, schemas);
	    		
	    		logger().debug("catalog: '" + c + "'");
	    		logger().debug("schema: '" + s + "'");
	    		
	    		SchemaName sn = new SchemaName(id(c), id(s));
	    		names.add(sn);
	    	}
    	}
    	finally {
    		close(schemas);
    	}
    	
    	Comparator<Identifier> icmp = getEnvironment().getIdentifierRules().comparator();
    	SchemaElementNameMap<ImmutableBaseTable.Builder> tbmap = new SchemaElementNameMap<ImmutableBaseTable.Builder>(icmp);
    	    	
    	SchemaNameComparator scmp = new SchemaNameComparator(icmp);
    	Map<SchemaName, ImmutableSchema.Builder> sbmap = new TreeMap<SchemaName, ImmutableSchema.Builder>(scmp);
    	    	
    	for (SchemaName sn : names) {
    		ImmutableSchema.Builder sb = new ImmutableSchema.Builder(getEnvironment(), sn);	    		
    		populate(meta, sb, tbmap);
    		sbmap.put(sn, sb);
		}
    	    	
    	
    	for (ImmutableBaseTable.Builder b : tbmap.values()) {
    		addPrimaryKey(meta, b);    		
    		addForeignKeys(meta, b, tbmap);
    		ImmutableBaseTable result = b.getResult();
    		SchemaName sn = result.getName().getQualifier();    		
    		ImmutableSchema.Builder sb = sbmap.get(sn);
    		addBaseTable(result, sb);
		}
    	
    	for (ImmutableSchema.Builder sb : sbmap.values()) {
    		ImmutableSchema schema = sb.newSchema();	    		
    		cb.add(schema);	
		}
    	
    	logger().debug("createSchemas - exit");
    	
	}

	protected void addBaseTable(BaseTable table, ImmutableSchema.Builder sb) {		
		sb.add(table);		
		addPrimaryKey(table.getPrimaryKey(), sb);
		addForeignKeys(table.foreignKeys(), sb);		
	}

	protected void addPrimaryKey(PrimaryKey pk, ImmutableSchema.Builder sb) {
		if (pk != null && pk.getName() != null) {
			sb.add(pk);
		}
	}
	
	protected void addForeignKeys(SchemaElementMap<ForeignKey> km, ImmutableSchema.Builder sb) {
		if (km.size() > 0) {
			for (ForeignKey fk : km.values()) {
				sb.add(fk);
			}	
		}
	}

	private void populate(DatabaseMetaData meta, ImmutableSchema.Builder sb, Map<SchemaElementName, ImmutableBaseTable.Builder> tbmap) throws SQLException {

		SchemaName sn = sb.getSchemaName();
				
		ResultSet tables = null;
		List<String> names = new ArrayList<String>(); 
		
		try {
			tables = getTablesForSchema(meta, sn, Table.BASE_TABLE);
			
			while(tables.next()) {
				String n = tables.getString(3);
				names.add(n);
			}
		}
		finally {
			close(tables);
		}
		
		
		for (String n : names) {
			Identifier tn = id(n);
			final SchemaElementName sen = new SchemaElementName(sn, tn);			
			ImmutableBaseTable.Builder tb = new ImmutableBaseTable.Builder(getEnvironment(), sen);													
			populateBaseTable(meta, tb);
			tb.prepare();			
			tbmap.put(sen, tb);
		}
	}
	
	private void populateBaseTable(DatabaseMetaData meta, ImmutableBaseTable.Builder tb) throws SQLException {					
		addColumns(meta, tb);		
	}
	
	private void addPrimaryKey(DatabaseMetaData meta, ImmutableBaseTable.Builder tb) throws SQLException {
		ResultSet rs = null;
		
		try {			
			logger.debug("pk:table: " + tb.getName());
			
			rs = getPrimaryKeyForTable(meta, tb.getName());
						
//			Retrieves a description of the given table's primary key columns. They are ordered by COLUMN_NAME. 
//			Each primary key column description has the following columns: 
//
//			TABLE_CAT String => table catalog (may be null) 
//			TABLE_SCHEM String => table schema (may be null) 
//			TABLE_NAME String => table name 
//			COLUMN_NAME String => column name 
//			KEY_SEQ short => sequence number within primary key( a value of 1 represents the first column of the primary key, a value of 2 would represent the second column within the primary key). 
//			PK_NAME String => primary key name (may be null)
			
			
			String name = null;
			Map<Short, String> om = new TreeMap<Short, String>();
			
			while (rs.next()) {
				String column = rs.getString(4);
				short ordinal = rs.getShort(5);
				
				if (name == null) {
					name = rs.getString(6);
				}
				
				logger().debug("addPrimaryKey: column=" + column);
				logger().debug("addPrimaryKey: ordinal=" + ordinal);
				
				om.put(Short.valueOf(ordinal), column);
			}
			
			logger.debug("keys mapped");
			
			if (!om.isEmpty()) {
				if (name == null) {
					// name = // TODO: generate;
				}
				
				Identifier pkname = (name == null) ? null : id(name);				
				ImmutablePrimaryKey.Builder pkb = tb.getPrimaryKeyBuilder();			
				pkb.setConstraintName(pkname);
				
				logger().debug("addPrimaryKey: pkname=" + pkname);
				logger().debug("addPrimaryKey: pk-cols=" + om);
				
				for (String n : om.values()) {
					Identifier cn = id(n);					
					Column column = tb.getColumn(cn);					
					pkb.add(column);				
				}
			}
		}
		finally {
			rs = close(rs);
		}
	}
	
	
	private void addForeignKeys(DatabaseMetaData meta, ImmutableBaseTable.Builder tb, SchemaElementNameMap<ImmutableBaseTable.Builder> tbmap) throws SQLException {
		ResultSet rs = null;
		
		try {
			SchemaElementName tableName = tb.getName();
			SchemaName sn = tableName.getQualifier();
			rs = getForeignKeysForTable(meta, sn, tableName.getUnqualifiedName());
						
//			Retrieves a description of the primary key columns that are referenced by the given table's foreign key columns (the primary keys imported by a table). They are ordered by PKTABLE_CAT, PKTABLE_SCHEM, PKTABLE_NAME, and KEY_SEQ. 
//			Each primary key column description has the following columns: 
//
//			PKTABLE_CAT String => primary key table catalog being imported (may be null) 
//			PKTABLE_SCHEM String => primary key table schema being imported (may be null) 
//			PKTABLE_NAME String => primary key table name being imported 
//			PKCOLUMN_NAME String => primary key column name being imported 
//			FKTABLE_CAT String => foreign key table catalog (may be null) 
//			FKTABLE_SCHEM String => foreign key table schema (may be null) 
//			FKTABLE_NAME String => foreign key table name 
//			FKCOLUMN_NAME String => foreign key column name 
//			KEY_SEQ short => sequence number within a foreign key( a value of 1 represents the first column of the foreign key, a value of 2 would represent the second column within the foreign key). 
//			UPDATE_RULE short => What happens to a foreign key when the primary key is updated: 
//			importedNoAction - do not allow update of primary key if it has been imported 
//			importedKeyCascade - change imported key to agree with primary key update 
//			importedKeySetNull - change imported key to NULL if its primary key has been updated 
//			importedKeySetDefault - change imported key to default values if its primary key has been updated 
//			importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) 
//			DELETE_RULE short => What happens to the foreign key when primary is deleted. 
//			importedKeyNoAction - do not allow delete of primary key if it has been imported 
//			importedKeyCascade - delete rows that import a deleted key 
//			importedKeySetNull - change imported key to NULL if its primary key has been deleted 
//			importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) 
//			importedKeySetDefault - change imported key to default if its primary key has been deleted 
//			FK_NAME String => foreign key name (may be null) 
//			PK_NAME String => primary key name (may be null) 
//			DEFERRABILITY short => can the evaluation of foreign key constraints be deferred until commit 
//			importedKeyInitiallyDeferred - see SQL92 for definition 
//			importedKeyInitiallyImmediate - see SQL92 for definition 
//			importedKeyNotDeferrable - see SQL92 for definition 

			while (rs.next()) {
				
				String pkcat = getCatalogNameFromImportedKeys(meta, rs);
				String pksch = getSchemaNameFromImportedKeys(meta, rs);
				String pktab = rs.getString(3);
				String pkcol = rs.getString(4);

				String fkcol = rs.getString(8);			
				
				int keyseq = rs.getInt(9);

				// TODO: implement rules
				int ur = rs.getInt(10);
				int dr = rs.getInt(11);
				String n = rs.getString(12);
				int def = rs.getInt(14);
								
				logger().debug("fk constraint name: '" + n + "'");
								
				Identifier constraintName = id(n);
												
				ImmutableForeignKey.Builder fkb = tb.getForeignKeyBuilder(constraintName);
				
				if (fkb == null) {
					SchemaName pksn = new SchemaName(id(pkcat), id(pksch));
					SchemaElementName key = new SchemaElementName(pksn, id(pktab));
					fkb = tb.createForeignKeyBuilder(constraintName, key, tbmap);					
				}								
				
				Identifier a = id(fkcol);
				Identifier b = id(pkcol);
				
				fkb.add(a, b);				
			}
		}
		finally {
			rs = close(rs);
		}
	}

	private void addColumns(DatabaseMetaData meta, ImmutableTable.Builder<?> tb) throws SQLException {
		ResultSet rs = null;
		
		try {
			logger.debug("addColumns: " + tb.getName().getUnqualifiedName());
			
			rs = getColumnsForTable(meta, tb.getName());
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int cc = rsmd.getColumnCount();			
			
			while (rs.next()) {
				Column column = createColumn(rs, cc);
				tb.add(column);
				
				logger.debug(column.getColumnName().getContent() + ": (" + column.getColumnDefault() + ")");				
			}
		}
		finally {
			close(rs);
		}
	}
	
	
	
	
	private Column createColumn(ResultSet rs, int columnCount) 
		throws SQLException {
		// 1. TABLE_CAT String => table catalog (may be null)
		// 2. TABLE_SCHEM String => table schema (may be null)
		// 3. TABLE_NAME String => table symbol
		// 4. COLUMN_NAME String => column symbol
		// 5. DATA_TYPE int => SQL type from java.sql.Types
		// 6. TYPE_NAME String => Data source dependent type symbol, for a UDT the
		// type symbol is fully qualified
		// 7. COLUMN_SIZE int => column size. For char or date types this is the
		// maximum number of characters, for numeric or decimal types this is
		// precision.
		// 8. BUFFER_LENGTH is not used.
		// 9. DECIMAL_DIGITS int => the number of fractional digits
		// 10. NUM_PREC_RADIX int => Radix (typically either 10 or 2)
		// 11. NULLABLE int => is NULL allowed.
		// * columnNoNulls - might not allow NULL values
		// * columnNullable - definitely allows NULL values
		// * columnNullableUnknown - nullability unknown
		// 12. REMARKS String => comment describing column (may be null)
		// 13. COLUMN_DEF String => default value (may be null)
		// 14. SQL_DATA_TYPE int => unused
		// 15. SQL_DATETIME_SUB int => unused
		// 16. CHAR_OCTET_LENGTH int => for char types the maximum number of bytes
		// in the column
		// 17. ORDINAL_POSITION int => index of column in table (starting at 1)
		// 18. IS_NULLABLE String => "NO" means column definitely does not allow
		// NULL values; "YES" means the column might allow NULL values. An empty
		// string means nobody knows.
		// 19. SCOPE_CATLOG String => catalog of table that is the scope of a
		// reference attribute (null if DATA_TYPE isn't REF)
		// 20. SCOPE_SCHEMA String => schema of table that is the scope of a
		// reference attribute (null if the DATA_TYPE isn't REF)
		// 21. SCOPE_TABLE String => table symbol that this the scope of a
		// reference attribure (null if the DATA_TYPE isn't REF)
		// 22. SOURCE_DATA_TYPE short => source type of a distinct type or
		// user-generated Ref type, SQL type from java.sql.Types (null if
		// DATA_TYPE isn't DISTINCT or user-generated REF)		
		// 23. IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
		
					
		String name = rs.getString(4);	

		int type = rs.getInt(5);
		String typeName = rs.getString(6);
		
		// DefaultMutableColumn col = null;
		
		int v = 0;
		
		v = rs.getInt(7);
		Integer size = rs.wasNull() ? null : Integer.valueOf(v); 
		
        v = rs.getInt(9);
        Integer dd = rs.wasNull() ? null : Integer.valueOf(v);
                
        v = rs.getInt(10);
        Integer radix = rs.wasNull() ? null : Integer.valueOf(v);
        
        
        if (logger().isDebugEnabled()) {
            logger.debug("name: {}", name);
            logger.debug("type: {}", Integer.toString(type));
            logger.debug("type-name: {}", typeName);
            logger.debug("column-size: {}", size);
            logger.debug("decimal-digits: {}", dd);       	
            logger.debug("radix: {}", radix);
        }
        
					    			
			// col = new DefaultMutableColumn(this.table, ci, dataType, ai);
				
		int nullability = rs.getInt(11);
		boolean definitelyNotNullable = (nullability == DatabaseMetaData.columnNoNulls); 
						
		String remarks = rs.getString(12);
		String columnDefault = rs.getString(13);
		
		v = rs.getInt(16);		
        Integer colen = rs.wasNull() ? null : Integer.valueOf(v);
        
        
        logger.debug(name + " => col-char-octet-len=" + colen);
				
//		col.getDataTypeImpl().setCharOctetLength(rs.getInt(16));
		int op = rs.getInt(17);
		
		
		String ais = (columnCount >= 23) ? rs.getString(23) : null;
		
		Boolean ai =
			"YES".equals(ais) ? Boolean.TRUE :
			"NO".equals(ais) ? Boolean.FALSE :
			null;
		
				
		DataTypeImpl dataType = new DataTypeImpl(type, typeName, colen, dd, radix, size);
				
		Identifier columnName = id(name);
				
		ImmutableColumn col = new ImmutableColumn(columnName, op, dataType, ai, remarks, definitelyNotNullable, columnDefault);

		return col;
	}

	protected ResultSet getColumnsForTable(DatabaseMetaData meta, SchemaElementName name) throws SQLException {
		SchemaName q = name.getQualifier();
		String cp = getCatalogPattern(q);
		String sp = getSchemaPattern(q);
		
		Identifier table = name.getUnqualifiedName();
		
		logger().debug("getColumnsForTable:");		
		logger().debug("cp: " + cp);
		logger().debug("sp: " + sp);		
		logger().debug("table: " + table.getContent());
				
		return meta.getColumns(cp, sp, table.getContent(), "%");
	}
	
	protected ResultSet getPrimaryKeyForTable(DatabaseMetaData meta, SchemaElementName sen) throws SQLException {
		SchemaName q = sen.getQualifier();
		
		String c = getPrimaryKeyCatalogNameParameter(q);
		String s = getPrimaryKeySchemaNameParameter(q);
		
		Identifier tableName = sen.getUnqualifiedName();
		
		logger().debug("getPrimaryKeyForTable:");		
		logger().debug("c: " + c);
		logger().debug("s: " + s);		
		logger().debug("table: " + tableName.getContent());
							
		ResultSet rs = meta.getPrimaryKeys(c, s, tableName.getContent());
		
		return rs;
	}
	
	protected ResultSet getForeignKeysForTable(DatabaseMetaData meta, SchemaName schema, Identifier tableName) throws SQLException {
		String c = getForeignKeyCatalogNameParameter(schema);
		String s = getForeignKeySchemaNameParameter(schema);
		
		logger().debug("getForeignKeysForTable:");		
		logger().debug("c: " + c);
		logger().debug("s: " + s);		
		logger().debug("table: " + tableName.getContent());
							
		ResultSet rs = meta.getImportedKeys(c, s, tableName.getContent());
		return rs;
	}

	protected ResultSet getTablesForSchema(DatabaseMetaData meta, SchemaName sn, String tableType) throws SQLException {
		String[] types = { tableType };
		String cp = getCatalogPattern(sn);
		String sp = getSchemaPattern(sn);
		return meta.getTables(cp, sp, "%", types);
	}

	public void prepare(DatabaseMetaData m) 
		throws SQLException {		
	}

	public static void main(String[] args) {
		try {
			if (args.length < 3) {
				System.err.println("usage:\n" + "java "
						+ CatalogFactory.class.getName()
						+ " <driver-symbol> <url> <config-file>");
				System.exit(-1);
			}

			String driverName = args[0];
			String url = args[1];
			String cfg = args[2];

//			PrintWriter out = new PrintWriter(System.out);

			logger().debug("loading " + driverName);
			Class<?> driverClass = Class.forName(driverName);
			logger().debug("driver loaded.");

			Driver selected = null;

			List<Driver> loaded = Collections.list(DriverManager.getDrivers());

			for (Driver d : loaded) {
				if (d.getClass().equals(driverClass)) {
					selected = d;
					break;
				}
			}

			if (!selected.acceptsURL(url)) {
				throw new IllegalArgumentException("driver " + selected.getClass()
						+ " does not accept URL: " + url);
			}

			// DefaultCatalogFactory cf = new DefaultCatalogFactory();
//			DefaultCatalogFactory cf = new MySQLCatalogFactory();
//			Implementation impl = new MySQLImplementation();			
//			CatalogFactory cf = impl.catalogFactory();

			logger().debug("loading config: " + new File(cfg).getAbsolutePath());
			Properties info = IOHelper.doLoad(cfg);
			logger().debug("config loaded.");

			logger().debug("connecting to: " + url);

			Connection c = null;					

			try {
				c = selected.connect(url, info);

				logger().debug("connected.");

				if (c == null) {
					throw new IllegalArgumentException("can not create connection to "
							+ url);
				}

				DatabaseMetaData meta = c.getMetaData();

				logger().debug("inspect meta-data...");
				logger().debug("driver-version: " + meta.getDriverVersion());
				logger().debug("JDBC-major version: " + meta.getJDBCMajorVersion());
				logger().debug("JDBC-minor version: " + meta.getJDBCMinorVersion());

				logger().debug("populating meta-data...");
				// Catalog catalog = cf.create(meta, null);

				logger().debug("view catalog...");
				// new PrimaryKeyPrinter(null).setCatalog(catalog);
			} finally {
				if (c != null) {
					c.close();
				}
			}

		} catch (Exception e) {
			logger().error(e.getMessage(), e);
		}
	}

	// private Catalog run(Connection c, PrintWriter out)
	// throws SQLException {
	//		
	// try {
	// DatabaseMetaData meta = c.getMetaData();
	// System.out.println("catalog term: " + meta.getCatalogTerm());
	// return create(meta, null);
	// }
	// catch (SQLException e) {
	// logger().error(e.getMessage(), e);
	// out.println("SQL-state: " + e.getSQLState());
	// out.println("error-code: " + e.getErrorCode());
	// out.println("message: " + e.getMessage());
	// out.println(e.getMessage());
	// throw e;
	// }
	// finally {
	// out.flush();
	// logger().debug("closing connection...");
	// c.close();
	// logger().debug("connection closed");
	// }
	// }

	public static Logger logger() {
		return DefaultCatalogFactory.logger;
	}

//	protected ResultSet getTablesForSchema(DatabaseMetaData meta,
//			DefaultMutableSchema ms) throws SQLException {
//		String[] types = { "TABLE" };
//		String cp = getCatalogPattern(ms);
//		String sp = getSchemaPattern(ms);
//		return meta.getTables(cp, sp, "%", types);
//	}

	protected Identifier id(String name) {
		return (name == null) ? null : getEnvironment().getIdentifierRules().toIdentifier(name);		
	}

//	@Override
//	public Identifier createIdentifier(String name)
//			throws IllegalIdentifierException {
//		return AbstractIdentifier.create(name);
//	}

//	@Override
//	public Comparator<Identifier> identifierComparator() {
//		if (this.identifierComp == null) {
//			this.identifierComp = new FoldingComparator();
//		}
//		
//		return identifierComp;
//	}
	
	@Override
	public String getCatalogName(Connection c) throws SQLException {
	    return c.getCatalog();		
	}

	
}
