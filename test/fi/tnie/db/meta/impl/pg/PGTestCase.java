/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import fi.tnie.db.env.CatalogFactory;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.SchemaMap;
import fi.tnie.db.meta.impl.common.JDBCTestCase;
import fi.tnie.db.query.QueryException;
import fi.tnie.dbmeta.tools.pg.PGRestore;

public abstract class PGTestCase
    extends JDBCTestCase {
	
	private Catalog catalog = null;
	public static final String SCHEMA_PUBLIC = "public";
	public static final String TABLE_CONTINENT = "continent";
	public static final String TABLE_COUNTRY = "country";
	
	private Implementation implementation;

	public PGTestCase() {
		super("org.postgresql.Driver", "tester", "password", "dbmeta_test");	
	}
	

	@Override
	public String getDatabaseURL() {
		return "jdbc:postgresql:" + getDatabase();
	}
	
	protected File getTestDump() {
		File d = dump(getClass());
		
		if (!d.exists()) {
			d = dump(PGTestCase.class);		
		}
		
		return d;
	}
		
	private static File dump(Class<?> t) {
		String pkg = PGTestCase.class.getPackage().getName();
		return new File("testdata/" + pkg + "/" + t.getName());
	}
	
	public static File dump() {
	    return PGTestCase.dump(PGTestCase.class);
	}

	
	@Override
	public void restore()
		throws IOException, InterruptedException {
	    
        File dump = getTestDump();
        
        if (!dump.canRead()) {
            throw new FileNotFoundException(dump.getAbsolutePath());
        }
        
	    pg().restore(dump);	    	    
	}
	
	private PGRestore pg() {	    
	    return new PGRestore(getUserid(), getPasswd(), getDatabase());        
    }


    protected PGImplementation newEnv() {
		return new PGImplementation();
	}
	
	@Override
	protected Catalog getCatalog() 
		throws QueryException, SQLException {
	
		if (catalog == null) {
			PGImplementation env = newEnv();
			CatalogFactory cf = env.catalogFactory();
			
			assertNotNull(cf);		
			Connection c = getConnection();
			
			assertNotNull(c);
			
			this.catalog = cf.create(c);
			assertNotNull(catalog);
			
		}
		return catalog;
	}

	protected BaseTable getContinentTable() 
		throws QueryException, SQLException {
		return getWellKnownBaseTable(SCHEMA_PUBLIC, TABLE_CONTINENT);
	}

	protected BaseTable getCountryTable() 
		throws QueryException, SQLException {
		return getWellKnownBaseTable(SCHEMA_PUBLIC, TABLE_COUNTRY);
	}

	protected BaseTable getWellKnownBaseTable(String schema, String table) 
		throws QueryException, SQLException {
		SchemaMap sm = getCatalog().schemas();
		assertNotNull(sm);				
		Schema pub = sm.get(schema);
		assertNotNull(sm);
		BaseTable t = pub.baseTables().get(table);
		assertNotNull(t);		
		return t;
	}

	protected Identifier id(String name) 
		throws IllegalIdentifierException, NullPointerException, QueryException, SQLException {
		return getCatalog().getEnvironment().createIdentifier(name);
	}
	
	    
    public boolean databaseExists()
       throws IOException, InterruptedException {        
       return pg().databaseExists();
    }    
    
    public void dropDatabaseIfExists() 
        throws IOException, InterruptedException {
        pg().dropDatabaseIfExists();
    }
    
    public void dropDatabase() throws IOException, InterruptedException {
        pg().dropDatabase();
    }

    public void createDatabase()
        throws IOException, InterruptedException {        
        pg().createDatabase();        
    }

    @Override
    protected Implementation getImplementation() {
    	if (implementation == null) {
			implementation = new PGImplementation();			
		}

		return implementation;
    }
    
}
