/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import fi.tnie.db.exec.QueryProcessor;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.expr.ddl.DropConstraint;
import fi.tnie.db.expr.ddl.DropTable;
import fi.tnie.db.expr.ddl.DropView;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.PrimaryKey;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.util.ResultSetWriter;
import fi.tnie.db.env.util.SimpleQueryProcessor;
import fi.tnie.db.env.util.StringListReader;
import fi.tnie.util.io.Launcher;
import fi.tnie.util.io.RunResult;

import junit.framework.TestCase;

public abstract class JDBCTestCase
	extends TestCase {
	
	private Connection connection;	
	private Properties jdbcConfig;
	private String database;
	private String driverClass;
	
	private static Logger logger = Logger.getLogger(JDBCTestCase.class);
	
	protected JDBCTestCase(String driverClass, Properties jdbcConfig) {
		this(driverClass, "tester", "password", "dbmeta_test", jdbcConfig);
		
	}
	
	protected RunResult exec(List<String> args) 
	    throws IOException, InterruptedException {
	    return Launcher.doExec(args);
	}
	
	protected JDBCTestCase(String driverClass, String userid, String passwd, String database, Properties config) {
		super();
		this.database = database;
		this.driverClass = driverClass;
		
		if (userid == null) {
			throw new NullPointerException("'userid' must not be null");
		}
		
		if (passwd == null) {
			throw new NullPointerException("'passwd' must not be null");
		}
		
		if (database == null) {
			throw new NullPointerException("'database' must not be null");
		}
		
		if (config == null) {
			config = new Properties();
		}
		
		config.setProperty("user", userid);
		config.setProperty("password", passwd);
		
		this.jdbcConfig = config;
	}

	protected abstract String getDatabaseURL();

	@Override
	protected void setUp() throws Exception {			
		super.setUp();
		
		if (this.driverClass != null) {
			Class.forName(this.driverClass);
		}		
	}
	
	protected void setUp(boolean drop, boolean truncate) throws Exception {
	    super.setUp();
	    
	    if (truncate) {
	        Connection c = getConnection();
	        Catalog cat = getCatalog();
	        
    	    if (cat != null) {
    	        truncate(cat);
    	    }
	    }
	}

	@Override
	protected void tearDown() throws Exception {
		if (this.connection != null) {			
			if (!this.connection.getAutoCommit()) {
				try {				
					this.connection.rollback();
				}
				catch (SQLException e) {
					logger().debug("error in rollback: " + e.getMessage());
				}				
			}
			this.connection.close();
			this.connection = null;
			logger().debug("connection closed");
		}		
	}

	protected Connection getConnection() 
	    throws SQLException {
		if (connection == null) {
	        String url = getDatabaseURL();
	        this.connection = DriverManager.getConnection(url, this.jdbcConfig);
	        logger().debug("connection reserved");            
        }

        return connection;
	}

	protected String getUserid() {
		return getJdbcConfig().getProperty("user");
	}

	protected String getPassword() {
		return getJdbcConfig().getProperty("password");
	}

	protected String getDatabase() {
		return database;
	}
	
	public void restore()
		throws Exception {		
	}

	protected <E extends Exception> void assertThrown(Class<? extends E> e) {
		fail(e + " was not thrown");
	}

	protected void testPrimaryKey(PrimaryKey pk) {
		assertNotNull(pk);
		assertNotNull(pk.getTable());				
		assertNotNull(pk.getColumnMap());
		assertFalse(pk.getColumnMap().isEmpty());
	}

	protected void testForeignKey(ForeignKey fk) {
		assertNotNull(fk);
		assertNotNull(fk.getReferenced());
		assertNotNull(fk.getReferencing());
		assertFalse(fk.getColumnMap().isEmpty());
		
//		for (Map.Entry<Column, Column> p : fk.columns().entrySet()) {					
//			assertNotNull(p.getKey());
//			assertNotNull(p.getValue());
//			
//			assertNotSame(p.getKey(), p.getValue());
//			
//			logger().debug("ref'ing: " + p.getKey().getUnqualifiedName().getName());
//			logger().debug("ref'ed: " + p.getValue().getUnqualifiedName().getName());
//			
//			assertNotNull(fk.getReferencing().columnMap().get(p.getKey().getUnqualifiedName()));
//			assertNotNull(fk.getReferenced().columnMap().get(p.getValue().getUnqualifiedName()));
//								
//			// TODO: 
//			// it should be enough for all referenced columns to be
//			// part of the same candidate key (not necessarily a primary key),
//			// but we have no representation for candidate key at the moment 
////			assertTrue(p.getValue().isPrimaryKeyColumn());
//		}
	}

	
	public static Logger logger() {
		return JDBCTestCase.logger;
	}

	public void dumpMetaData() throws QueryException, SQLException {		
		Connection c = getConnection();		
		DatabaseMetaData meta = c.getMetaData();
		
		fi.tnie.db.env.util.ResultSetWriter rw = new ResultSetWriter(System.out, false);		
		
//		final int jdbcMajor = meta.getJDBCMajorVersion();
		
		rw.println("URL\t" + meta.getURL());
		rw.println("user\t" + meta.getUserName());
				
		rw.println("database-product-name\t" + meta.getDatabaseProductName());
		rw.println("database-product-version\t" + meta.getDatabaseProductVersion());
		rw.println("driver-name\t" + meta.getDriverName());
		rw.println("driver-version\t" + meta.getDriverVersion());
		rw.println("jdbc-major-version\t" + meta.getJDBCMajorVersion());
		rw.println("jdbc-minor-version\t" + meta.getJDBCMinorVersion());		
		
		rw.println("catalog-term\t" + meta.getCatalogTerm());		
		rw.println("schema-term\t" + meta.getSchemaTerm());
		rw.println("procedure-term\t" + meta.getProcedureTerm());
		rw.println("MaxUserNameLength\t" + meta.getMaxUserNameLength());
				
		rw.println("catalog-separator\t'" + meta.getCatalogSeparator() + "'");
		
		rw.println("sql-keywords\t" + meta.getSQLKeywords());
		
		rw.println("string-functions\t" + meta.getStringFunctions());
		rw.println("numeric-functions\t" + meta.getNumericFunctions());
		
		rw.println("DefaultTransactionIsolation\t" + meta.getDefaultTransactionIsolation());
		rw.println("isReadOnly\t" + meta.isReadOnly());
		rw.println("ExtraNameCharacters\t" + meta.getExtraNameCharacters());
		rw.println("IdentifierQuoteString\t" + meta.getIdentifierQuoteString());
				
	
		rw.println("allProceduresAreCallable\t" + meta.allProceduresAreCallable());
		rw.println("allTablesAreSelectable\t" + meta.allTablesAreSelectable());
		rw.println("dataDefinitionCausesTransactionCommit\t" + meta.dataDefinitionCausesTransactionCommit());
		rw.println("dataDefinitionIgnoredInTransactions\t" + meta.dataDefinitionIgnoredInTransactions());
		rw.println("doesMaxRowSizeIncludeBlobs\t" + meta.doesMaxRowSizeIncludeBlobs());
		rw.println("isCatalogAtStart\t" + meta.isCatalogAtStart());
		
		rw.println("locatorsUpdateCopy\t" + meta.locatorsUpdateCopy());
		rw.println("nullPlusNonNullIsNull\t" + meta.nullPlusNonNullIsNull());
		rw.println("nullsAreSortedAtEnd\t" + meta.nullsAreSortedAtEnd());
		rw.println("nullsAreSortedAtStart\t" + meta.nullsAreSortedAtStart());
		rw.println("nullsAreSortedHigh\t" + meta.nullsAreSortedHigh());
		rw.println("nullsAreSortedLow\t" + meta.nullsAreSortedLow());
		rw.println("storesLowerCaseIdentifiers\t" + meta.storesLowerCaseIdentifiers());
		rw.println("storesLowerCaseQuotedIdentifiers\t" + meta.storesLowerCaseQuotedIdentifiers());
		rw.println("storesMixedCaseIdentifiers\t" + meta.storesMixedCaseIdentifiers());
		rw.println("storesMixedCaseQuotedIdentifiers\t" + meta.storesMixedCaseQuotedIdentifiers());
		rw.println("storesUpperCaseIdentifiers\t" + meta.storesUpperCaseIdentifiers());
		rw.println("storesUpperCaseQuotedIdentifiers\t" + meta.storesUpperCaseQuotedIdentifiers());
		rw.println("supportsAlterTableWithAddColumn\t" + meta.supportsAlterTableWithAddColumn());
		rw.println("supportsAlterTableWithDropColumn\t" + meta.supportsAlterTableWithDropColumn());
		rw.println("supportsANSI92EntryLevelSQL\t" + meta.supportsANSI92EntryLevelSQL());
		rw.println("supportsANSI92FullSQL\t" + meta.supportsANSI92FullSQL());
		rw.println("supportsANSI92IntermediateSQL\t" + meta.supportsANSI92IntermediateSQL());
		rw.println("supportsBatchUpdates\t" + meta.supportsBatchUpdates());
		rw.println("supportsCatalogsInDataManipulation\t" + meta.supportsCatalogsInDataManipulation());
		rw.println("supportsCatalogsInIndexDefinitions\t" + meta.supportsCatalogsInIndexDefinitions());
		rw.println("supportsCatalogsInPrivilegeDefinitions\t" + meta.supportsCatalogsInPrivilegeDefinitions());
		rw.println("supportsCatalogsInProcedureCalls\t" + meta.supportsCatalogsInProcedureCalls());
		rw.println("supportsCatalogsInTableDefinitions\t" + meta.supportsCatalogsInTableDefinitions());
		rw.println("supportsColumnAliasing\t" + meta.supportsColumnAliasing());
		rw.println("supportsConvert\t" + meta.supportsConvert());
		rw.println("supportsCoreSQLGrammar\t" + meta.supportsCoreSQLGrammar());
		rw.println("supportsCorrelatedSubqueries\t" + meta.supportsCorrelatedSubqueries());
		rw.println("supportsDataDefinitionAndDataManipulationTransactions\t" + meta.supportsDataDefinitionAndDataManipulationTransactions());
		rw.println("supportsDataManipulationTransactionsOnly\t" + meta.supportsDataManipulationTransactionsOnly());
		rw.println("supportsDifferentTableCorrelationNames\t" + meta.supportsDifferentTableCorrelationNames());
		rw.println("supportsExpressionsInOrderBy\t" + meta.supportsExpressionsInOrderBy());
		rw.println("supportsExtendedSQLGrammar\t" + meta.supportsExtendedSQLGrammar());
		rw.println("supportsFullOuterJoins\t" + meta.supportsFullOuterJoins());
		rw.println("supportsGetGeneratedKeys\t" + meta.supportsGetGeneratedKeys());
		rw.println("supportsGroupBy\t" + meta.supportsGroupBy());
		rw.println("supportsGroupByBeyondSelect\t" + meta.supportsGroupByBeyondSelect());
		rw.println("supportsGroupByUnrelated\t" + meta.supportsGroupByUnrelated());
		rw.println("supportsIntegrityEnhancementFacility\t" + meta.supportsIntegrityEnhancementFacility());
		rw.println("supportsLikeEscapeClause\t" + meta.supportsLikeEscapeClause());
		rw.println("supportsLimitedOuterJoins\t" + meta.supportsLimitedOuterJoins());
		rw.println("supportsMinimumSQLGrammar\t" + meta.supportsMinimumSQLGrammar());
		rw.println("supportsMixedCaseIdentifiers\t" + meta.supportsMixedCaseIdentifiers());
		rw.println("supportsMixedCaseQuotedIdentifiers\t" + meta.supportsMixedCaseQuotedIdentifiers());
		rw.println("supportsMultipleOpenResults\t" + meta.supportsMultipleOpenResults());
		rw.println("supportsMultipleResultSets\t" + meta.supportsMultipleResultSets());
		rw.println("supportsMultipleTransactions\t" + meta.supportsMultipleTransactions());
		rw.println("supportsNamedParameters\t" + meta.supportsNamedParameters());
		rw.println("supportsNonNullableColumns\t" + meta.supportsNonNullableColumns());
		rw.println("supportsOpenCursorsAcrossCommit\t" + meta.supportsOpenCursorsAcrossCommit());
		rw.println("supportsOpenCursorsAcrossRollback\t" + meta.supportsOpenCursorsAcrossRollback());
		rw.println("supportsOpenStatementsAcrossCommit\t" + meta.supportsOpenStatementsAcrossCommit());
		rw.println("supportsOpenStatementsAcrossRollback\t" + meta.supportsOpenStatementsAcrossRollback());
		rw.println("supportsOrderByUnrelated\t" + meta.supportsOrderByUnrelated());
		rw.println("supportsOuterJoins\t" + meta.supportsOuterJoins());
		rw.println("supportsPositionedDelete\t" + meta.supportsPositionedDelete());
		rw.println("supportsPositionedUpdate\t" + meta.supportsPositionedUpdate());
		rw.println("supportsSavepoints\t" + meta.supportsSavepoints());
		rw.println("supportsSchemasInDataManipulation\t" + meta.supportsSchemasInDataManipulation());
		rw.println("supportsSchemasInIndexDefinitions\t" + meta.supportsSchemasInIndexDefinitions());
		rw.println("supportsSchemasInPrivilegeDefinitions\t" + meta.supportsSchemasInPrivilegeDefinitions());
		rw.println("supportsSchemasInProcedureCalls\t" + meta.supportsSchemasInProcedureCalls());
		rw.println("supportsSchemasInTableDefinitions\t" + meta.supportsSchemasInTableDefinitions());
		rw.println("supportsSelectForUpdate\t" + meta.supportsSelectForUpdate());
		rw.println("supportsStatementPooling\t" + meta.supportsStatementPooling());
		rw.println("supportsStoredProcedures\t" + meta.supportsStoredProcedures());
		rw.println("supportsSubqueriesInComparisons\t" + meta.supportsSubqueriesInComparisons());
		rw.println("supportsSubqueriesInExists\t" + meta.supportsSubqueriesInExists());
		rw.println("supportsSubqueriesInIns\t" + meta.supportsSubqueriesInIns());
		rw.println("supportsSubqueriesInQuantifieds\t" + meta.supportsSubqueriesInQuantifieds());
		rw.println("supportsTableCorrelationNames\t" + meta.supportsTableCorrelationNames());
		rw.println("supportsTransactions\t" + meta.supportsTransactions());
		rw.println("supportsUnion\t" + meta.supportsUnion());
		rw.println("supportsUnionAll\t" + meta.supportsUnionAll());
		rw.println("usesLocalFilePerTable\t" + meta.usesLocalFilePerTable());
		rw.println("usesLocalFiles\t" + meta.usesLocalFiles());
		
		
	
		rw.header("Catalogs");
		rw.apply(meta.getCatalogs());
				
		rw.header("Schemas");
		rw.apply(meta.getSchemas());		
		
		rw.header("JavaType Info");
		rw.apply(meta.getTypeInfo());

		rw.header("Table Types");
		rw.apply(meta.getTableTypes());
		
		try {
			rw.header("Attributes");			
			rw.apply(meta.getAttributes(null, null, null, null));
		}
		catch (SQLException e) {
			rw.println(e.getMessage());
		}		

		try {
			rw.header("UDTs");			
			rw.apply(meta.getUDTs(null, null, null, null));
		}
		catch (SQLException e) {
			rw.println(e.getMessage());
		}
		
		try {
			rw.header("Functions");			
			rw.apply(meta.getFunctions(null, null, null));
		}
        catch (SQLException e) {
            rw.println(e.getMessage());
        }       
        catch (AbstractMethodError e) {
            rw.println(e.getMessage());
        }
		
		try {
			rw.header("Function Columns");			
			rw.apply(meta.getFunctionColumns(null, null, null, null));
		}
        catch (SQLException e) {
            rw.println(e.getMessage());
        }		
		catch (AbstractMethodError e) {
			rw.println(e.getMessage());
		}

		try {
			rw.header("Procedures");			
			rw.apply(meta.getProcedures(null, null, null));
		}
		catch (SQLException e) {
			rw.println(e.getMessage());
		}
		
		try {
			rw.header("Procedure columns");			
			rw.apply(meta.getProcedureColumns(null, null, null, null));
		}
		catch (Exception e) {
			rw.println(e.getMessage());
		}
			

		{
			rw.header("Tables");
			rw.apply(meta.getTables(null, "public", "%", null));
		}
		
		{
		    rw.header("Tables");
	        rw.apply(meta.getTables(c.getCatalog(), null, "%", null));
	    }
				
//		if (jdbcMajor >= 4) {
//			rw.header("ClientInfo properties");
//			rw.apply(meta.getClientInfoProperties());
//						
//			rw.header("Functions");
//			rw.apply(meta.getFunctions(null, null, null));			
//		}		
		

		
//		try {
//			rw.header("Attributes");			
//			rw.apply(meta.getIndexInfo(catalog, schema, table, unique, approximate));
//		}
//		catch (SQLException e) {
//			rw.println("NOT SUPPORTED: " + e.getMessage());
//		}
		
		ArrayList<String> tables = new ArrayList<String>();
		StringListReader r = new StringListReader(tables, 3);
		r.apply(meta.getTables(null, "public", "%", null));
		
		logger().debug("table-count: " + r.getRowCount());
		
		if (r.getRowCount() == 0) {		    
		    // for MySQL:
		    r.apply(meta.getTables(c.getCatalog(), null, "%", null));
		    logger().debug("table-count-2: " + r.getRowCount());
		}
		
		for (String n : tables) {				
			rw.header("Columns in table: " + n);
			rw.apply(meta.getColumns(null, null, n, null));
			
			rw.header("Primary key columns in table");
			rw.apply(meta.getPrimaryKeys(null, null, n));
			
			rw.header("Imported key column in table ");
			rw.apply(meta.getImportedKeys(null, null, n));

			rw.header("Exported key column in table ");
			rw.apply(meta.getExportedKeys(null, null, n));

			rw.header("Cross-ref 1 column in table ");
			rw.apply(meta.getCrossReference(null, null, n, null, null, null));

			rw.header("Cross-ref 2 column in table ");
			rw.apply(meta.getCrossReference(null, null, null, null, null, n));
			
			rw.header("Table privileges");
			rw.apply(meta.getTablePrivileges(null, null, n));

			try {
				rw.header("Column privileges");			
				rw.apply(meta.getColumnPrivileges(null, null, n, null));
			}
			catch (SQLException e) {
				rw.println(e.getMessage());
			}
			
						
			rw.header("Index info");
			rw.apply(meta.getIndexInfo(null, null, n, false, false));
			
			try {
				rw.header("Version Columns");			
				rw.apply(meta.getVersionColumns(null, null, n));
			}
			catch (SQLException e) {
				rw.println(e.getMessage());
			}
			



//				rw.header("Column privileges");
			
			{
				ResultSet bestIds = meta.getBestRowIdentifier(null, null, n, DatabaseMetaData.bestRowSession, true);
				rw.header("Best row identifier (for session) column in table " + n);
				rw.apply(bestIds);
			}
			
			{
				ResultSet bestIds = meta.getBestRowIdentifier(null, null, n, DatabaseMetaData.bestRowTransaction, true);
				rw.header("Best row identifier (for tx) column in table " + n);
				rw.apply(bestIds);
			}
		}
	}
		
	public List<Statement> truncation(Catalog cat) {
	    
	    List<Statement> statements = new ArrayList<Statement>(); 
	    
	    for (Schema s : cat.schemas().values()) {
	        logger().debug("fks: " + s.foreignKeys().values().size());
	        logger().debug("pks: " + s.primaryKeys().values().size());
	        logger().debug("constraints: " + s.constraints().values().size());
	        
            for (ForeignKey fk : s.foreignKeys().values()) {
                Statement stmt = new DropConstraint(fk);
                statements.add(stmt);                
            }
        }
	    
        for (Schema s : cat.schemas().values()) {                        
            for (Table t : s.tables().values()) {
                Statement stmt = t.isBaseTable() ? 
//                        new Truncate((BaseTable) t) :
                        new DropTable(t.getName(), Boolean.TRUE) :                            
                        new DropView(t.getName(), Boolean.TRUE);                        
                statements.add(stmt);                
            }
        }
        
        return statements;
	}
	
    public void truncate(Catalog cat) throws QueryException, SQLException {        
        Connection c = getConnection();     
                
        List<Statement> statements = truncation(cat);
                
        // TODO: Inherit from the base class with getPersistenceContext(); 
        fail("not implemented.");
        
//        StatementExecutor e = new StatementExecutor(getImplementation());
//        
//        QueryProcessor qp = createQueryProcessor();
//        
//        c.setAutoCommit(false);
//                        
//        for (Statement s : statements) {
//            String q = s.generate();
//            logger().debug(q);
//            e.execute(s, c, qp);
//            c.commit();
//        }
    }

    protected QueryProcessor createQueryProcessor() {
        PrintWriter pw = new PrintWriter(System.out) {
            @Override
            public void print(String s) {            
                logger().debug(s);
            }           
        };
        
        QueryProcessor qp = new SimpleQueryProcessor(pw) {
            @Override
            public void abort(Throwable e) {             
                throw new RuntimeException(e);
            }
        };
        return qp;
    }
    
    protected Catalog getCatalog()
        throws QueryException, SQLException {
        return null;
    }
    
    protected abstract Implementation<?> getImplementation();

	public Properties getJdbcConfig() {
		if (jdbcConfig == null) {
			jdbcConfig = new Properties();			
		}

		return jdbcConfig;
	}

}

