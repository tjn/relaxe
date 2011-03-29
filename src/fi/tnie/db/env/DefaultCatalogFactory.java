/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import java.sql.DatabaseMetaData;

import fi.tnie.db.QueryException;
import fi.tnie.db.QueryHelper;
import fi.tnie.db.env.util.AbstractQueryProcessor;
import fi.tnie.db.env.util.IdentifierReader;
import fi.tnie.db.env.util.StringListReader;
import fi.tnie.db.exec.QueryProcessor;
import fi.tnie.db.exec.QueryProcessorAdapter;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.CatalogMap;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;
import fi.tnie.db.meta.impl.DataTypeImpl;
import fi.tnie.db.meta.impl.DefaultCatalogMap;
import fi.tnie.db.meta.impl.DefaultForeignKey;
import fi.tnie.db.meta.impl.DefaultMutableBaseTable;
import fi.tnie.db.meta.impl.DefaultMutableCatalog;
import fi.tnie.db.meta.impl.DefaultMutableColumn;
import fi.tnie.db.meta.impl.DefaultMutableSchema;
import fi.tnie.db.meta.impl.DefaultMutableTable;
import fi.tnie.db.meta.impl.DefaultPrimaryKey;
import fi.tnie.util.io.IOHelper;

public class DefaultCatalogFactory implements CatalogFactory {

	private static Logger logger = Logger.getLogger(DefaultCatalogFactory.class);
	private static final int TABLE_NAME_COLUMN = 3;

	private Environment environment;
	
//	TODO: requiring environment as an argument for constructor, makes
//	instantiating via Class.newInstance() (too) painful 
//	in Environment -implementations.
	public DefaultCatalogFactory(Environment environment) {
		super();
		
		if (environment == null) {
			throw new NullPointerException("'environment' must not be null");
		}
		
		this.environment = environment;
	}	

	class ColumnReader extends QueryProcessorAdapter {
		private DefaultMutableBaseTable table;
		
		public ColumnReader(DefaultMutableBaseTable table, DatabaseMetaData meta) {
			super();
			this.table = table;		
		}

		@Override
		public void process(ResultSet rs, long ordinal) throws SQLException {
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

			// TODO:
			// use AUTO_INCREMENT column with JDBC 4.0
			// read it from system tables in < JDBC 4.0

			String cat = rs.getString(1);
			String tbl = rs.getString(3);
			
			if (!tbl.equals(this.table.getName().getUnqualifiedName().getName())) {
			    throw new RuntimeException("tbl name mismatch: " +
			        tbl + " <> " + 
			        this.table.getName().getUnqualifiedName().getName());
			}

			// TODO: implement MySQL / others

			// SchemaElementName tableName = new SchemaElementName();
			//						
			// if (!this.table.getName().equals(tableName)) {
			// throw new IllegalStateException(
			// "encountered table identifier '" + tbl +
			// "' when populating table '" + this.table.getName() + "'");
			// }

			String name = rs.getString(4);

//			logger().debug("column: " + cat + "." + tbl + "." + name);

			short type = rs.getShort(5);
			String typeName = rs.getString(6);
			
			DefaultMutableColumn col = null;

			{
    			DataTypeImpl dataType = new DataTypeImpl(type, typeName);			
                dataType.setSize(rs.getInt(7));
                dataType.setDecimalDigits(rs.getInt(9));
                dataType.setNumPrecRadix(rs.getInt(10));
    			
    			Identifier n = getEnvironment().createIdentifier(name);

    			col = new DefaultMutableColumn(this.table, n, dataType);
			}
					
			col.setNullable(rs.getInt(11));
			col.setRemarks(rs.getString(12));
			col.setColumnDefault(rs.getString(13));

			col.getDataTypeImpl().setCharOctetLength(rs.getInt(16));
			col.setOrdinalPosition(rs.getInt(17));
		}
	}

	class PrimaryKeyReader extends AbstractQueryProcessor {
		private DefaultMutableBaseTable table;
		private DefaultMutableCatalog catalog;

		private List<DefaultMutableColumn> columns;
		private DefaultPrimaryKey result = null;
		private DatabaseMetaData meta;
			
		public PrimaryKeyReader(DefaultMutableBaseTable table, DatabaseMetaData meta) {
			super();			
			this.table = table;
			this.catalog = table.getMutableSchema().getMutableCatalog();
			this.meta = meta;
		}

		@Override
		public void endQuery() {
			logger().debug("endQuery - enter");			
			pushCompleted();
		}

		@Override
		public void process(ResultSet rs, long ordinal) throws SQLException {
			// 1. TABLE_CAT String => table catalog (may be null)
			// 2. TABLE_SCHEM String => table schema (may be null)
			// 3. TABLE_NAME String => table symbol
			// 4. COLUMN_NAME String => column symbol
			// 5. KEY_SEQ short => sequence number within primary key
			// 6. PK_NAME String => primary key symbol (may be null)
		    		    
			String pkcat = getCatalogNameFromPrimaryKeys(meta, rs); 
			String pksch = getSchemaNameFromPrimaryKeys(meta, rs);
			String pktab = rs.getString(3);
			String pkcol = rs.getString(4);
			int keyseq = rs.getInt(5);
			String pkname = rs.getString(6);
			
            if (pkname == null || pkname.equals("PRIMARY")) {                   
                pkname = "PK_" + pktab;
                logger().debug("made up a primary key name: " + pkname);
            }			
			
			if (keyseq == 1) {
				pushCompleted();
				
				Identifier pkid = catalog.getEnvironment().createIdentifier(pkname);
				logger().debug("pksch: " + pksch);
				logger().debug("pktab: " + pktab);
				logger().debug("pkname: " + pkname);
				logger().debug("table: " + table.getName().getUnqualifiedName().getName());
				
				this.result = new DefaultPrimaryKey(table, pkid);				
			}

			logger().debug("pkcat: " + pkcat);
			logger().debug("pksch: " + pksch);
			logger().debug("pktab: " + pktab);			
			logger().debug("pkcol: " + pkcol);
			logger().debug("keyseq: " + keyseq);
			logger().debug("symbol: " + pkname);

			Schema pks = getSchema(catalog, pksch, pkcat);
						
			// logger().debug("pksch: " + pksch);
			// logger().debug("pks: " + pks);
			logger().debug("pktab: " + pktab);
			logger().debug("pkcol: " + pkcol);

			DefaultMutableBaseTable pkt = (DefaultMutableBaseTable) pks.baseTables().get(pktab);
			
			logger().debug("pkt: " + pkt);
			
			DefaultMutableColumn pkc = pkt.columnMap().get(pkcol);

			if (pkc == null) {
				throw new NullPointerException("'pkc' must not be null: " + pkcol);
			}
			
			getColumnList().add(pkc);
		}

		private void pushCompleted() {
			logger().debug("pushCompleted - enter: " + result);
			
			if (this.result != null) {
				logger().debug("pushCompleted - cols: " + getColumnList());
				
				if (!getColumnList().isEmpty()) {					
					this.result.setColumnList(getColumnList());					
					logger().debug("created pk: " + this.result);
					getColumnList().clear();
				}				
			}
		}

		private List<DefaultMutableColumn> getColumnList() {
			if (columns == null) {
				columns = new ArrayList<DefaultMutableColumn>(1);
			}

			return columns;
		}
		
		
	}

	class ForeignKeyReader extends AbstractQueryProcessor {
		private DefaultMutableBaseTable referencingTable;
		private DefaultMutableCatalog catalog;

		private List<DefaultForeignKey.Pair> columnPairList;
		private DefaultForeignKey result = null;
		private DatabaseMetaData meta;

		public ForeignKeyReader(DefaultMutableBaseTable table, DatabaseMetaData meta) {
			super();
			this.referencingTable = table;
			this.catalog = table.getMutableSchema().getMutableCatalog();
			this.meta = meta;
		}

		@Override
		public void endQuery() {
			push();
		}

		@Override
		public void process(ResultSet rs, long ordinal) throws SQLException {
			// 1. PKTABLE_CAT String => primary key table catalog being imported (may
			// be null)
			// 2. PKTABLE_SCHEM String => primary key table schema being imported (may
			// be null)
			// 3. PKTABLE_NAME String => primary key table symbol being imported
			// 4. PKCOLUMN_NAME String => primary key column symbol being imported
			// 5. FKTABLE_CAT String => foreign key table catalog (may be null)
			// 6. FKTABLE_SCHEM String => foreign key table schema (may be null)
			// 7. FKTABLE_NAME String => foreign key table symbol
			// 8. FKCOLUMN_NAME String => foreign key column symbol
			// 9. KEY_SEQ short => sequence number within a foreign key
			// 10. UPDATE_RULE short => What happens to a foreign key when the primary
			// key is updated:
			// * importedNoAction - do not allow update of primary key if it has been
			// imported
			// * importedKeyCascade - change imported key to agree with primary key
			// update
			// * importedKeySetNull - change imported key to NULL if its primary key
			// has been updated
			// * importedKeySetDefault - change imported key to default values if its
			// primary key has been updated
			// * importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x
			// compatibility)
			// 11. DELETE_RULE short => What happens to the foreign key when primary
			// is deleted.
			// * importedKeyNoAction - do not allow delete of primary key if it has
			// been imported
			// * importedKeyCascade - delete rows that import a deleted key
			// * importedKeySetNull - change imported key to NULL if its primary key
			// has been deleted
			// * importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x
			// compatibility)
			// * importedKeySetDefault - change imported key to default if its primary
			// key has been deleted
			// 12. FK_NAME String => foreign key symbol (may be null)
			// 13. PK_NAME String => primary key symbol (may be null)
			// 14. DEFERRABILITY short => can the evaluation of foreign key
			// constraints be deferred until commit
			// * importedKeyInitiallyDeferred - see SQL92 for definition
			// * importedKeyInitiallyImmediate - see SQL92 for definition
			// * importedKeyNotDeferrable - see SQL92 for definition

			String pkcat = getCatalogNameFromImportedKeys(meta, rs);
			String pksch = getSchemaNameFromImportedKeys(meta, rs);
			String pktab = rs.getString(3);
			String pkcol = rs.getString(4);

			String fkcat = getReferencedTableCatalogName(meta, rs);			
			String fksch = getReferencedTableSchemaName(meta, rs);			
			String fktab = rs.getString(7);
			String fkcol = rs.getString(8);
			
			int keyseq = rs.getInt(9);

			int ur = rs.getInt(10);
			int dr = rs.getInt(11);
			String n = rs.getString(12);
			int def = rs.getInt(14);

			if (keyseq == 1) {
				push();

				// Identifier fkname = this.catalog.create(n);
				Identifier fkname = catalog.getEnvironment().createIdentifier(n);

				this.result = new DefaultForeignKey(
						referencingTable.getMutableSchema(), fkname);

				DefaultForeignKey fk = this.result;

				fk.setUpdateRule(ur);
				fk.setDeleteRule(dr);
				fk.setDeferrability(def);
			}

			logger().debug("fkname: " + n);
//			logger().debug("keyseq: " + keyseq);

			// Schema pks = this.catalog.schemas().get(pkca);

			Schema pks = getSchema(catalog, pksch, pkcat);
			
			logger().debug("pksch: " + pksch);			
			logger().debug("pktab: " + pktab);
			logger().debug("pkcol: " + pkcol);			

			DefaultMutableBaseTable pkt = (DefaultMutableBaseTable) pks.tables().get(pktab);
			
			DefaultMutableColumn pkc = pkt.columnMap().get(pkcol);

			if (pkc == null) {
				throw new NullPointerException("'pkc' must not be null");
			}
						
			// TODO: handle base-tables in different catalogs
			
			if (fkcat != null && pkcat != null) {
				if (!fkcat.equals(pkcat)) {
					throw new RuntimeException(
							"we currently can not handle the case where " +
							"the catalog (" + fkcat + ") of the column referenced by foreign key " +
							"is different from the catalog (" + pkcat + ") of the referencing column");
				}
			}				
			
			Schema fks = pks.getCatalog().schemas().get(fksch);
			DefaultMutableBaseTable referencedTable = (DefaultMutableBaseTable) fks.baseTables().get(fktab);
			DefaultMutableColumn fkc = referencedTable.columnMap().get(fkcol);

			if (fkc == null) {
				throw new NullPointerException("'fkc' must not be null");
			}

			getColumnPairList().add(new DefaultForeignKey.Pair(fkc, pkc));
		}

		private void push() {
			if (this.result != null) {
				this.result.setColumnMap(getColumnPairList());
				getColumnPairList().clear();
			}
		}

		private List<DefaultForeignKey.Pair> getColumnPairList() {
			if (columnPairList == null) {
				columnPairList = new ArrayList<DefaultForeignKey.Pair>(1);
			}

			return columnPairList;
		}
	}
	
	public CatalogMap createAll(Connection c) 
		throws QueryException, SQLException {
		return create(c, true);
	}
	
	public Catalog create(Connection c) 
		throws QueryException, SQLException {		
		CatalogMap cm = create(c, false);		
		Catalog catalog = cm.get(getCatalogName(c));
		return catalog;
	}
		
	private CatalogMap create(Connection c, boolean all)
			throws QueryException, SQLException {
						
		final Environment env = getEnvironment();		
//		logger().debug("enter");								
		DatabaseMetaData meta = c.getMetaData();		
						
		try  {			
			prepare(meta);
			
			DefaultCatalogMap cm = new DefaultCatalogMap(env);
						
			if (!all) {
				String current = getCatalogName(c);
				Identifier cn = (current == null) ? null : env.createIdentifier(current);				
				cm.add(new DefaultMutableCatalog(env, cn));
			}
			else {
				Set<Identifier> names = new TreeSet<Identifier>(env.identifierComparator());
				ResultSet cats = meta.getCatalogs();
				cats = process(cats, new IdentifierReader(names, env), true);			
				logger().debug("catalogs: " + names);
				
				for (Identifier cn : names) {
					DefaultMutableCatalog cat = new DefaultMutableCatalog(env, cn);
					cm.add(cat);
				}							
			}
			
			createSchemas(meta, cm);			
			populateTables(meta, cm);
				
//				logger().debug("catalog:schemas: " + catalog.schemas().keySet().size());
				
			logger().debug("creating pk's");		
			populatePrimaryKeys(cm, meta);
			
			logger().debug("creating fk's");		
			populateForeignKeys(cm, meta);
			
			logger().debug("exit");
			
			return cm;				
		}
		finally {		
			finish();
		}
	}


    protected void finish() {		
	}

	public void prepare(DatabaseMetaData m) 
		throws SQLException {		
	}

	protected void createSchemas(DatabaseMetaData meta, DefaultCatalogMap cm) throws SQLException {
		ResultSet schemas = null;
		
//		dumpResultSet(meta.getSchemas());		
//		meta.getConnection();
		
		try {
			schemas = getSchemas(meta);
			
			while(schemas.next()) {
				String sch = getSchemaNameFromSchemas(meta, schemas);				
				String cat = getCatalogNameFromSchemas(meta, schemas);				
				getSchema(cm, cat, sch);
			}
		}
		finally {
			schemas = QueryHelper.doClose(schemas);
		}
	}

	public String getCatalogNameFromSchemas(DatabaseMetaData meta, ResultSet schemas) throws SQLException {		
		return schemas.getString(2);
	}	
	
	public ResultSet getSchemas(DatabaseMetaData meta) throws SQLException {                      
        return meta.getSchemas();
    }


	public String getSchemaNameFromSchemas(DatabaseMetaData meta, ResultSet schemas) throws SQLException {						
		return schemas.getString(1);
	}

	protected void close(ResultSet rs) {		
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			logger().warn(e.getMessage(), e);
		}
	}

	protected void process(ResultSet rs, QueryProcessor qp) throws QueryException, SQLException {

		long ordinal = 0;

		qp.startQuery(rs.getMetaData());

		while (rs.next()) {
			qp.process(rs, ++ordinal);
		}
		
		logger().debug("rows processed: " + ordinal);

		qp.endQuery();
	}

	protected ResultSet process(ResultSet rs, QueryProcessor qp, boolean close)
			throws QueryException, SQLException {

		try {
			process(rs, qp);
		} 
		finally {
			if (close) {
				close(rs);
				rs = null;
			}
		}

		return rs;
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

	public DefaultMutableSchema createSchema(DefaultMutableCatalog catalog,
			Identifier n, DatabaseMetaData m) {
		return new DefaultMutableSchema(catalog, n);
	}

	public void populateSchema(DefaultMutableSchema s, DatabaseMetaData meta)
			throws QueryException, SQLException {
		logger().debug("populate schema: " + s.getUnqualifiedName());
		// table-pop
		List<String> names = new ArrayList<String>();

		// ResultSet tables = meta.getTables(s.getCatalog().getName(), s.getName(),
		// "%", types);
		ResultSet tables = getTablesForSchema(meta, s);
		tables = process(tables, new StringListReader(names, TABLE_NAME_COLUMN),
				true);

		// logger().debug("tables: " + names.size());

		for (String n : names) {
			// logger().debug("table: " + n);
			DefaultMutableBaseTable t = createBaseTable(s, n, meta);
			// logger().debug("schema, " + t.getSchema());
			// boolean added = s.add(t);
			// logger().debug("tables: " + s.tables().size());
			populateTable(t, meta);
		}
	}

	public void populateTables(DatabaseMetaData meta, DefaultCatalogMap cm)
		throws QueryException, SQLException {
//		logger().debug("populate schema: " + s.getUnqualifiedName());
		
		// table-pop
//		List<String> names = new ArrayList<String>();
		
		String[] types = { "TABLE" };
	
		ResultSet tables = meta.getTables(null, null, "%", types);
		
		try {				
			while(tables.next()) {
				String cat = getCatalogNameFromTables(meta, tables);
				String sch = getSchemaNameFromTables(meta, tables);
				String tbl = tables.getString(3);
				
				DefaultMutableSchema schema = getSchema(cm, cat, sch);
				DefaultMutableBaseTable t = createBaseTable(schema, tbl, meta);
				populateTable(t, meta);
			}
		}
		finally {
			tables = QueryHelper.doClose(tables);
		}
		
		
//		ResultSet tables = getTablesForSchema(meta, s);
		
//		tables = process(tables, new StringListReader(names, TABLE_NAME_COLUMN),
//				true);
//	
//		// logger().debug("tables: " + names.size());
//		
//		for (String n : names) {
//			// logger().debug("table: " + n);
//			
//			
//			
//			DefaultMutableBaseTable t = createBaseTable(s, n, meta);
//			// logger().debug("schema, " + t.getSchema());
//			// boolean added = s.add(t);
//			// logger().debug("tables: " + s.tables().size());
//			populateTable(t, meta);
//		}
	}

	public String getSchemaNameFromTables(DatabaseMetaData meta, ResultSet tables) throws SQLException {
		return tables.getString(2);
	}

	public String getCatalogNameFromTables(DatabaseMetaData meta, ResultSet tables) throws SQLException {
		return tables.getString(1);		
	}
	
	public String getReferencedTableCatalogName(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {		
		return fkcols.getString(5);
	}
	
	public String getReferencedTableSchemaName(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {		
		return fkcols.getString(6);
	}
	
	public String getSchemaNameFromPrimaryKeys(DatabaseMetaData meta, ResultSet pkcols) throws SQLException {
		return pkcols.getString(2);		
	}

	public String getCatalogNameFromPrimaryKeys(DatabaseMetaData meta, ResultSet pkcols) throws SQLException {
	    return pkcols.getString(1);     
    }
	
	public String getCatalogNameFromImportedKeys(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {
	    return fkcols.getString(5);     
	}
	
	protected String getSchemaNameFromImportedKeys(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {
		return fkcols.getString(2);		
	}		
	
	protected ResultSet getTablesForSchema(DatabaseMetaData meta,
			DefaultMutableSchema ms) throws SQLException {
		String[] types = { "TABLE" };
		String cp = getCatalogPattern(ms);
		String sp = getSchemaPattern(ms);
		return meta.getTables(cp, sp, "%", types);
	}

	private void populateTable(DefaultMutableBaseTable t, DatabaseMetaData meta)
			throws QueryException, SQLException {

		logger().debug("populate table: " + t.getName());

		{
			// ResultSet rs = meta.getColumns(c.getName(), s.getName(), t.getName(),
			// "%");
			ResultSet rs = getColumnsForTable(meta, t);
			process(rs, new ColumnReader(t, meta), true);
		}
	}
	private DefaultMutableBaseTable createBaseTable(DefaultMutableSchema s,

			String n, DatabaseMetaData meta) {
		// return new DefaultMutableBaseTable(s, s.getCatalog().create(n));
		return new DefaultMutableBaseTable(s, getEnvironment().createIdentifier(n));
	}

	public void populatePrimaryKeys(DefaultCatalogMap cm,
			DatabaseMetaData meta) throws QueryException, SQLException {
					
		for (DefaultMutableCatalog c : cm.values()) {
			for (Schema s : c.schemas().values()) {
				String cp = getCatalogPattern(s);
				String sp = getSchemaPattern(s);
				
				for (BaseTable t : s.baseTables().values()) {
					String n = t.getUnqualifiedName().getName();
					
					logger().debug("query primary keys for: " +
							"cat=" + cp + ", sp=" + sp + ", n=" + n);
					ResultSet rs = meta.getPrimaryKeys(cp, sp, n);
					process(rs, new PrimaryKeyReader((DefaultMutableBaseTable) t, meta), true);
				}
			}
		}
	}

	public void populateForeignKeys(DefaultCatalogMap cm,
			DatabaseMetaData meta) throws QueryException, SQLException {

		for (DefaultMutableCatalog c : cm.values()) {
			for (Schema s : c.schemas().values()) {
				// DefaultMutableSchema ms = (DefaultMutableSchema) s;
				String cp = getCatalogPattern(s);
				String sp = getSchemaPattern(s);
	
				for (Table t : s.tables().values()) {
					if (t.isBaseTable()) {
						logger().debug(
								"query imported keys for: " + t.getQualifiedName() + " / "
										+ s.getUnqualifiedName());
						ResultSet rs = meta.getImportedKeys(cp, sp, t.getUnqualifiedName().getName());
						process(rs, new ForeignKeyReader((DefaultMutableBaseTable) t, meta), true);
					}
				}
			}
		}

		// for (Iterator<Schema> iterator = c.schemas().iterator();
		// iterator.hasNext();) {
		// DefaultMutableSchema s = (DefaultMutableSchema) iterator.next();
		//			
		// s.baseTables();
		// }
	}

	protected ResultSet getColumnsForTable(DatabaseMetaData meta,
			DefaultMutableTable t) throws SQLException {
		String cp = getCatalogPattern(t.getMutableSchema());
		String sp = getSchemaPattern(t.getMutableSchema());
		
		logger().debug("table: " + t.getUnqualifiedName().getName());
		logger().debug("cp: " + cp);
		logger().debug("sp: " + sp);		
				
		return meta.getColumns(cp, sp, t.getUnqualifiedName().getName(), "%");
	}

	protected String getCatalogPattern(Schema s) {
		Catalog c = (s == null) ? null : s.getCatalog();
		Identifier cn = (c == null) ? null : c.getName();
		return (cn == null) ? null : cn.getName();
	}

	protected String getSchemaPattern(Schema s) {
		Identifier sn = (s == null) ? null : s.getUnqualifiedName();		
		return (sn == null) ? null : sn.getName();
	}

//	@Override
//	public CatalogFactory catalogFactory() {
//		return this;
//	}

	public Environment getEnvironment() {
		return environment;
	}
	
	protected Identifier id(String name) {
		return getEnvironment().createIdentifier(name);		
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
	
	protected Schema getSchema(DefaultMutableCatalog catalog, String sch, String cat) {
		return catalog.schemas().get(sch);
	}
	
	
	protected DefaultMutableSchema getSchema(DefaultCatalogMap cm, String cat, String sch) {
		Environment env = getEnvironment();
		
		final Identifier cn = env.createIdentifier(cat);		
		DefaultMutableCatalog c = cm.get(cn);
		
		if (c == null) {
			c = new DefaultMutableCatalog(getEnvironment(), cn);
			cm.add(c);
		}
		
		final Identifier sn = env.createIdentifier(sch);
		DefaultMutableSchema s = c.getSchemaMap().get(sn);
		
		if (s == null) {
			s = new DefaultMutableSchema(c, sn);
		}
		
		return s;
	}

//	@Override
//	public Catalog create(Connection c) throws QueryException, SQLException {
//		return create(c.getMetaData(), c.getCatalog());
//	}

	public String getCatalogName(Connection c) throws SQLException {
	    return c.getCatalog();
	}
	
	

	
}
