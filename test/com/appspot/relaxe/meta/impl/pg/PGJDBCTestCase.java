/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.IllegalIdentifierException;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.SchemaMap;
import com.appspot.relaxe.meta.impl.common.JDBCTestCase;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.tools.pg.PGRestore;


public abstract class PGJDBCTestCase
    extends JDBCTestCase {
	
	private Catalog catalog = null;
	public static final String SCHEMA_PUBLIC = "public";
	public static final String TABLE_CONTINENT = "continent";
	public static final String TABLE_COUNTRY = "country";
	
	private PGImplementation implementation;

	public PGJDBCTestCase() {
		super("org.postgresql.Driver", "tester", "password", "test", null);	
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
	    return PGJDBCTestCase.dump(PGJDBCTestCase.class);
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
	    return new PGRestore(getUserid(), getPassword(), getDatabase());        
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
		return getCatalog().getEnvironment().getIdentifierRules().toIdentifier(name);
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
    protected PGImplementation getImplementation() {
    	if (implementation == null) {
			implementation = new PGImplementation();			
		}

		return implementation;
    }
    
    
    
}
