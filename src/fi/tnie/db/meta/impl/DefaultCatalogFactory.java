package fi.tnie.db.meta.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import java.sql.DatabaseMetaData;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.util.AbstractQueryProcessor;
import fi.tnie.db.meta.util.QueryProcessor;
import fi.tnie.db.meta.util.StringListReader;

public class DefaultCatalogFactory implements CatalogFactory {

	private static Logger logger = Logger.getLogger(DefaultCatalogFactory.class);	
	private static final int TABLE_NAME_COLUMN = 3;
	
	
	class ColumnReader
		extends AbstractQueryProcessor
	{
		private DefaultMutableBaseTable table;

		public ColumnReader(DefaultMutableBaseTable table) {
			super();
			this.table = table;
		}
		
		@Override
		public void process(ResultSet rs, long ordinal) throws SQLException {			
//		   1. TABLE_CAT String => table catalog (may be null)
//		   2. TABLE_SCHEM String => table schema (may be null)
//		   3. TABLE_NAME String => table name
//		   4. COLUMN_NAME String => column name
//		   5. DATA_TYPE int => SQL type from java.sql.Types
//		   6. TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
//		   7. COLUMN_SIZE int => column size. For char or date types this is the maximum number of characters, for numeric or decimal types this is precision.
//		   8. BUFFER_LENGTH is not used.
//		   9. DECIMAL_DIGITS int => the number of fractional digits
//		  10. NUM_PREC_RADIX int => Radix (typically either 10 or 2)
//		  11. NULLABLE int => is NULL allowed.
//		          * columnNoNulls - might not allow NULL values
//		          * columnNullable - definitely allows NULL values
//		          * columnNullableUnknown - nullability unknown 
//		  12. REMARKS String => comment describing column (may be null)
//		  13. COLUMN_DEF String => default value (may be null)
//		  14. SQL_DATA_TYPE int => unused
//		  15. SQL_DATETIME_SUB int => unused
//		  16. CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
//		  17. ORDINAL_POSITION int => index of column in table (starting at 1)
//		  18. IS_NULLABLE String => "NO" means column definitely does not allow NULL values; "YES" means the column might allow NULL values. An empty string means nobody knows.
//		  19. SCOPE_CATLOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
//		  20. SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
//		  21. SCOPE_TABLE String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF)
//		  22. SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
			
			// TODO: use AUTO_INCREMENT column with JDBC 4.0
			// read it from system tables in < JDBC 4.0
			
			String cat = rs.getString(1);
			String tbl = rs.getString(3);
			
			if (!this.table.getName().equals(tbl)) {
				throw new IllegalStateException(
						"encountered table name " + table + 
						" when populating table " + this.table.getName());
			}
			
			
				
			String name = rs.getString(4);
						
			logger().debug("column: " + cat + "." + tbl + "." + name);
			
			short type = rs.getShort(5);
			String typeName = rs.getString(6);			
			
			DataTypeImpl dataType = new DataTypeImpl(type, typeName);
			DefaultMutableColumn col = new DefaultMutableColumn(name, dataType);
			
			dataType.setSize(rs.getInt(7));
			dataType.setDecimalDigits(rs.getInt(9));
			dataType.setNumPrefixRadix(rs.getInt(10));
			
			col.setNullable(rs.getInt(11));
			col.setRemarks(rs.getString(12));
			col.setColumnDefault(rs.getString(13));
			
			dataType.setCharOctetLength(rs.getInt(16));
						
			table.add(col);
		}
	}	
	
	class PrimaryKeyReader
		extends AbstractQueryProcessor
{
	private DefaultMutableBaseTable referencedTable;
	private DefaultMutableCatalog catalog;
	
	private List<DefaultMutableColumn> columns;
	private DefaultPrimaryKey result = null;
					
	public PrimaryKeyReader(DefaultMutableBaseTable table) {
		super();
		this.referencedTable = table;
		this.catalog = table.getSchemaImpl().getCatalogImpl();
	}
	
	
	@Override
	public void endQuery() throws SQLException {
		push();
	}
	
	@Override
	public void process(ResultSet rs, long ordinal) throws SQLException {			
//	   1. TABLE_CAT String => table catalog (may be null)
//	   2. TABLE_SCHEM String => table schema (may be null)
//	   3. TABLE_NAME String => table name
//	   4. COLUMN_NAME String => column name
//	   5. KEY_SEQ short => sequence number within primary key
//	   6. PK_NAME String => primary key name (may be null) 		
		
		String pkcat = rs.getString(1);
		String pksch = rs.getString(2);
		String pktab = rs.getString(3);
		String pkcol = rs.getString(4);		
		int keyseq = rs.getInt(5);		
		String n = rs.getString(6);
		
		if (keyseq == 1) {					
			push();								
			
			this.result = new DefaultPrimaryKey(referencedTable.getSchema(), n);							
		}

		logger().debug("pkcat: " + pkcat);
		logger().debug("pksch: " + pksch);
		logger().debug("pktab: " + pktab);
		logger().debug("name: " + n);
		logger().debug("keyseq: " + keyseq);

		Schema pks = this.catalog.schemas().get(pkcat);
	//		logger().debug("pksch: " + pksch);
	//		logger().debug("pks: " + pks);
	//		logger().debug("pktab: " + pktab);
			
			BaseTable pkt = (BaseTable) pks.tables().get(pktab);
			DefaultMutableColumn pkc = (DefaultMutableColumn) pkt.columns().get(pkcol);
		
			if (pkc == null) {
				throw new NullPointerException("'pkc' must not be null");
			}
			
			getColumns().add(pkc);
		}

		private void push() {
			if (this.result != null) {
				this.result.setColumnList(getColumns());					
				getColumns().clear();
			}
		}


	private List<DefaultMutableColumn> getColumns() {
		if (columns == null) {
			columns = new ArrayList<DefaultMutableColumn>(1);					
		}

		return columns;
	}
}	
		
	
	class ForeignKeyReader
			extends AbstractQueryProcessor
		{
			private DefaultMutableBaseTable referencingTable;
			private DefaultMutableCatalog catalog;
			
			private List<DefaultForeignKey.Pair> columns;

			private DefaultForeignKey result = null;
							
			public ForeignKeyReader(DefaultMutableBaseTable table) {
				super();
				this.referencingTable = table;
				this.catalog = table.getSchemaImpl().getCatalogImpl();
			}
			
			
			@Override
			public void endQuery() throws SQLException {
				push();
			}
			
			@Override
			public void process(ResultSet rs, long ordinal) throws SQLException {			
//			   1. PKTABLE_CAT String => primary key table catalog being imported (may be null)
//			   2. PKTABLE_SCHEM String => primary key table schema being imported (may be null)
//			   3. PKTABLE_NAME String => primary key table name being imported
//			   4. PKCOLUMN_NAME String => primary key column name being imported
//			   5. FKTABLE_CAT String => foreign key table catalog (may be null)
//			   6. FKTABLE_SCHEM String => foreign key table schema (may be null)
//			   7. FKTABLE_NAME String => foreign key table name
//			   8. FKCOLUMN_NAME String => foreign key column name
//			   9. KEY_SEQ short => sequence number within a foreign key
//			  10. UPDATE_RULE short => What happens to a foreign key when the primary key is updated:
//			          * importedNoAction - do not allow update of primary key if it has been imported
//			          * importedKeyCascade - change imported key to agree with primary key update
//			          * importedKeySetNull - change imported key to NULL if its primary key has been updated
//			          * importedKeySetDefault - change imported key to default values if its primary key has been updated
//			          * importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) 
//			  11. DELETE_RULE short => What happens to the foreign key when primary is deleted.
//			          * importedKeyNoAction - do not allow delete of primary key if it has been imported
//			          * importedKeyCascade - delete rows that import a deleted key
//			          * importedKeySetNull - change imported key to NULL if its primary key has been deleted
//			          * importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
//			          * importedKeySetDefault - change imported key to default if its primary key has been deleted 
//			  12. FK_NAME String => foreign key name (may be null)
//			  13. PK_NAME String => primary key name (may be null)
//			  14. DEFERRABILITY short => can the evaluation of foreign key constraints be deferred until commit
//			          * importedKeyInitiallyDeferred - see SQL92 for definition
//			          * importedKeyInitiallyImmediate - see SQL92 for definition
//			          * importedKeyNotDeferrable - see SQL92 for definition
				
				
				String pkcat = rs.getString(1);
				String pksch = rs.getString(2);
				String pktab = rs.getString(3);
				String pkcol = rs.getString(4);
				
				String fkcol = rs.getString(8);
				int keyseq = rs.getInt(9);
				
				int ur = rs.getInt(10);
				int dr = rs.getInt(11);
				String n = rs.getString(12);
				int def = rs.getInt(14);
				
				if (keyseq == 1) {					
					push();
										
					
					this.result = new DefaultForeignKey(referencingTable.getSchema(), n);
					DefaultForeignKey fk = this.result;
					
					fk.setUpdateRule(ur);
					fk.setDeleteRule(dr);
					fk.setDeferrability(def);
				}
				

			logger().debug("pkcat: " + pkcat);
			logger().debug("pksch: " + pksch);
			logger().debug("pktab: " + pktab);
			logger().debug("name: " + n);
			logger().debug("keyseq: " + keyseq);

				Schema pks = this.catalog.schemas().get(pkcat);
//				logger().debug("pksch: " + pksch);
//				logger().debug("pks: " + pks);
//				logger().debug("pktab: " + pktab);
				
				BaseTable pkt = (BaseTable) pks.tables().get(pktab);
				DefaultMutableColumn pkc = (DefaultMutableColumn) pkt.columns().get(pkcol);
				
				if (pkc == null) {
					throw new NullPointerException("'pkc' must not be null");
				}
				
//				System.out.println("fk: " + fkcol);
				
				DefaultMutableColumn fkc = (DefaultMutableColumn) referencingTable.columns().get(fkcol);
				
				if (fkc == null) {
					throw new NullPointerException("'fkc' must not be null");
				}
				
				getColumns().add(new DefaultForeignKey.Pair(fkc, pkc));
			}
	
			private void push() {
				if (this.result != null) {
					this.result.setColumnMap(getColumns());					
					getColumns().clear();
				}
			}


			private List<DefaultForeignKey.Pair> getColumns() {
				if (columns == null) {
					columns = new ArrayList<DefaultForeignKey.Pair>(1);					
				}

				return columns;
			}
		}



	@Override
	public Catalog create(DatabaseMetaData meta, String catalogName)
		throws SQLException {

		return null;
	}

	protected void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		}
		catch (SQLException e) {
			logger().warn(e.getMessage(), e);
		}
	}

	protected void process(ResultSet rs, QueryProcessor qp)
		throws SQLException {
	
		long ordinal = 1;
		
		qp.startQuery(rs.getMetaData());
		
		while (rs.next()) {
			qp.process(rs, ordinal++);
		}
		
		qp.endQuery();
	}

	protected ResultSet process(ResultSet rs, QueryProcessor qp, boolean close) 
		throws SQLException {
		
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

	public Properties load(String path) 
		throws IOException {
		FileInputStream in = null;
		
		try {
			in = new FileInputStream(path);			
			Properties p = new Properties();
			p.load(in);
			return p;
		}
		finally {
			if (in != null) {				
				in.close();
			}
		}
	}

	public static void main(String[] args) {
			try {
				if (args.length < 3) {
					System.err.println("usage:\n" +
							"java " + CatalogFactory.class.getName() + " <driver-name> <url> <config-file>");
					System.exit(-1);
				}			
				
				String driverName = args[0];
				String url = args[1];
				String cfg = args[2];
							
				PrintWriter out = new PrintWriter(System.out);
							
				logger().debug("loading " + driverName);
				Class<?> driverClass = Class.forName(driverName);
				logger().debug("driver loaded.");
				
				Driver selected = null;
				
				List<Driver> loaded = Collections.list(DriverManager.getDrivers());
				
				for(Driver d : loaded) {
					if (d.getClass().equals(driverClass)) {
						selected = d;
						break;
					}				
				}
				
				if (!selected.acceptsURL(url)) {
					throw new IllegalArgumentException(
							"driver " + selected.getClass() + " does not accept URL: " + url);
				}
				
//				DefaultCatalogFactory cf = new DefaultCatalogFactory();
				DefaultCatalogFactory cf = new MySQLCatalogFactory();
				
				logger().debug("loading config: " + new File(cfg).getAbsolutePath());					
				Properties info = cf.load(cfg);
				logger().debug("config loaded.");
												
				logger().debug("connecting to: " + url);
				
				Connection c = null;
				
				try {				
					c = selected.connect(url, info);
					
					logger().debug("connected.");
					
					if (c == null) {
						throw new IllegalArgumentException("can not create connection to " + url);
					}
					

					
					DatabaseMetaData meta = c.getMetaData();
					
					logger().debug("inspect meta-data...");
					logger().debug("driver-version: " + meta.getDriverVersion());
					logger().debug("JDBC-major version: " + meta.getJDBCMajorVersion());
					logger().debug("JDBC-minor version: " + meta.getJDBCMinorVersion());
				
					
					logger().debug("populating meta-data...");
//					Catalog catalog = cf.create(meta, null);
					
					logger().debug("view catalog...");					
//					new PrimaryKeyPrinter(null).setCatalog(catalog);					
				}
				finally {
					if (c != null) {
						c.close();
					}					
				}
				
			}
			catch (Exception e) {
				logger().error(e.getMessage(), e);
			}
		}
	
//	private Catalog run(Connection c, PrintWriter out) 
//		throws SQLException {
//		
//		try {
//			DatabaseMetaData meta = c.getMetaData();
//			System.out.println("catalog term: " + meta.getCatalogTerm());
//			return create(meta, null);								
//		}
//		catch (SQLException e) {
//			logger().error(e.getMessage(), e);
//			out.println("SQL-state: " + e.getSQLState());
//			out.println("error-code: " + e.getErrorCode());
//			out.println("message: " + e.getMessage());
//			out.println(e.getMessage());
//			throw e;
//		}
//		finally {
//			out.flush();
//			logger().debug("closing connection...");
//			c.close();				
//			logger().debug("connection closed");				
//		}				
//	}
	
	
	public static Logger logger() {
		return DefaultCatalogFactory.logger;
	}
	
	protected DefaultMutableSchema createSchema(String n, DatabaseMetaData m) {
		return new DefaultMutableSchema(n);
	}
	
	protected void populateSchema(DefaultMutableSchema s, DatabaseMetaData meta)
		throws SQLException
	{			
		logger().debug("populate schema: " + s.getName());
		// table-pop		
		List<String> names = new ArrayList<String>();
		
//		ResultSet tables = meta.getTables(s.getCatalog().getName(), s.getName(), "%", types);
		ResultSet tables = getTablesForSchema(meta, s);
		tables = process(tables, new StringListReader(names, TABLE_NAME_COLUMN), true);
		
//		logger().debug("tables: " + names.size());
		
		for (String n : names) {
//			logger().debug("table: " + n);
			DefaultMutableBaseTable t = createBaseTable(s, n, meta);
//			logger().debug("schema, " + t.getSchema());
//			boolean added = s.add(t);
//			logger().debug("tables: " + s.tables().size());			
			populateTable(t, meta);
		}
	}
	
	
	protected ResultSet getTablesForSchema(DatabaseMetaData meta, DefaultMutableSchema ms) 
		throws SQLException {
		String[] types = { "TABLE" };		
		String cp = getCatalogPattern(ms);
		String sp = getSchemaPattern(ms);						
		return meta.getTables(cp, sp, "%", types);	
	}
	

	private void populateTable(DefaultMutableBaseTable t, DatabaseMetaData meta) 
		throws SQLException {		
				
		logger().debug("populate table: " + t.getName());
		
		Schema s = t.getSchema();
		Catalog c = s.getCatalog();
		
		{
//			ResultSet rs = meta.getColumns(c.getName(), s.getName(), t.getName(), "%");
			ResultSet rs = getColumnsForTable(meta, t);
			process(rs, new ColumnReader(t), true);
		}	
	}

	private DefaultMutableBaseTable createBaseTable(DefaultMutableSchema s, String n,
			DatabaseMetaData meta) {				
		return new DefaultMutableBaseTable(s, n);
	}
	
	
	protected void populatePrimaryKeys(DefaultMutableCatalog c, DatabaseMetaData meta) 
		throws SQLException {
		
		for (Schema s : c.schemas().values()) {
			DefaultMutableSchema ms = (DefaultMutableSchema) s;
			
			String cp = getCatalogPattern(ms);
			String sp = getSchemaPattern(ms);
			
			for (BaseTable t : ms.baseTables().values()) {
				logger().debug("query imported keys for: " + t.getQualifiedName() + " / " + ms.getName());
												
				ResultSet rs = meta.getPrimaryKeys(cp, sp, t.getName());															
				process(rs, new PrimaryKeyReader((DefaultMutableBaseTable) t), true);
			}
		}
	}
	
	protected void populateForeignKeys(DefaultMutableCatalog c, DatabaseMetaData meta) 
		throws SQLException {
					
		
		for (Schema s : c.schemas().values()) {
			DefaultMutableSchema ms = (DefaultMutableSchema) s;
			
			String cp = getCatalogPattern(ms);
			String sp = getSchemaPattern(ms);
			
			for (BaseTable t : ms.baseTables().values()) {
				logger().debug("query imported keys for: " + t.getQualifiedName() + " / " + ms.getName());
												
				ResultSet rs = meta.getImportedKeys(cp, sp, t.getName());															
				process(rs, new ForeignKeyReader((DefaultMutableBaseTable) t), true);
			}
		}
		
//		for (Iterator<Schema> iterator = c.schemas().iterator(); iterator.hasNext();) {
//			DefaultMutableSchema s = (DefaultMutableSchema) iterator.next();
//			
//			s.baseTables();
//		}
	}
	
	protected ResultSet getColumnsForTable(DatabaseMetaData meta, DefaultMutableTable t) 
		throws SQLException {		
		String cp = getCatalogPattern(t.getSchemaImpl());
		String sp = getSchemaPattern(t.getSchemaImpl());				
		return meta.getColumns(cp, sp, t.getName(), "%");		
	}
	
	
	protected String getCatalogPattern(DefaultMutableSchema s) {
		DefaultMutableCatalog c = (s == null) ? null : s.getCatalogImpl();
		return (c == null) ? null : c.getName();
	}
	
	protected String getSchemaPattern(DefaultMutableSchema s) {
		return (s == null) ? null : s.getName();		
	}
}
