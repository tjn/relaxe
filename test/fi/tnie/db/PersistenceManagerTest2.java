/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;

import junit.framework.TestCase;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.gen.ent.LiteralCatalog;
import fi.tnie.db.gen.ent.personal.DefaultOrganization;
import fi.tnie.db.gen.ent.personal.DefaultPerson;
import fi.tnie.db.gen.ent.personal.HourReport;
import fi.tnie.db.gen.ent.personal.Person;
import fi.tnie.db.gen.ent.personal.PersonalFactory;
import fi.tnie.db.gen.ent.personal.Person.Reference;
import fi.tnie.db.gen.ent.personal.Person.Type;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.Interval;
import fi.tnie.db.types.ReferenceType;

public class PersistenceManagerTest2 extends TestCase  {
	
	
	private Connection connection = null;
	private Catalog catalog;
	
	
	@Override
	protected void setUp() throws Exception {
		String url = "jdbc:postgresql:test";
		this.connection = DriverManager.getConnection(url, "test", "password");
		this.connection.setAutoCommit(false);
		this.catalog = LiteralCatalog.getInstance();
	}
	
	@Override
	protected void tearDown() throws Exception {
		QueryHelper.doClose(this.connection);
	}

    public void testPersistenceManager() 
        throws Exception {
        
        Connection c = getConnection();
        
        assertFalse(c.getAutoCommit());
        
        Catalog cat = getCatalog();
        
//        testCatalog(cat, c);        
//        TableMapper tm = getTableMapper();
        
        // ClassLoader gcl = createClassLoaderForGenerated();
        ClassLoader gcl = null;
        
        LiteralCatalog cc = LiteralCatalog.getInstance();
                
//        CatalogContext cc = new CatalogContext(cat, gcl, tm);
                        
//        assertEquals(cat, cc.boundTo());                
//        assertFalse(cc.getMetaMap().isEmpty());
                       
        BaseTable ct = LiteralCatalog.LiteralBaseTable.PUBLIC_CONTINENT;
        assertNotNull(ct);
        
//        logger().debug("bound tables: " + cc.getMetaMap().keySet());
//        logger().debug("key-table: " + ct);
//        logger().debug("key-table-loader: " + ct.getClass().getClassLoader());
//        
//        logger().debug("bound-table-loader: " + cc.getMetaMap().keySet().iterator().next().getClass().getClassLoader());
                
//        assertTrue(cc.getMetaMap().containsKey(ct));               
                        
        EntityMetaData<?, ?, ?, ?> meta = cc.getMetaData(ct);
        assertNotNull(meta);
                
        EntityFactory<?, ?, ?, ?> ef = meta.getFactory();
        assertNotNull(ef);
                
        PersonalFactory pf = cc.newPersonalFactory();
        DefaultPerson p = pf.newPerson();
                
        PGImplementation impl = new PGImplementation();        
        PersistenceManager<fi.tnie.db.gen.ent.personal.Person.Attribute, Reference, Type, Person> pm = 
       		create(p.self(), impl);
        
        
        
                        
//        PersistenceManager<Attribute, Reference, Query, 
//        	Entity<Attribute, Reference, Query, Person.Type, ? extends Person>> pm = p.createPersistentManager();
                
        // p.setId(8);
        // p.setName("asdf");
        p.setFirstName("a");
        p.setLastName("b");
        p.setDateOfBirth(new Date());        
        p.createdAt().set(new Date());
        
        p.averageNaptime().set(new Interval.DayTime(1, 30, 0));
                                       
        pm.merge(c);
        c.commit();        
        pm.delete(c);
        c.commit();
        pm.insert(c);
        c.commit();
        pm.update(c);
        c.commit();
//        pm.delete(c);
//        c.commit();
        
        HourReport hr = pf.newHourReport();
        assertNotNull(hr.reportDate());
        
        DefaultOrganization org = pf.newOrganization();
        
        DateKey<HourReport.Attribute, HourReport.Reference, HourReport.Type, HourReport> dk = 
        	hr.getMetaData().getDateKey(HourReport.Attribute.REPORT_DATE);
        
        hr.setDate(dk, DateHolder.currentDate());                
        assertNotNull(hr.reportDate().get());
        assertNotNull(hr.getReportDate());
        hr.setComment("asdfasd");
        hr.setStartedAt(new Date());
        hr.setFinishedAt(new Date());
//        hr.ref(HourReport.Reference.FK_HHR_EMPLOYER). org);
        
        hr.id().set(null);               
        
        PersistenceManager<fi.tnie.db.gen.ent.personal.HourReport.Attribute, 
        	fi.tnie.db.gen.ent.personal.HourReport.Reference, 
        	fi.tnie.db.gen.ent.personal.HourReport.Type, HourReport> hrm =
        	create(hr, impl);
        
                
        hrm.merge(c);
        c.commit();        
    }

	private Catalog getCatalog() {		
		return this.catalog;
	}

	public Connection getConnection() {
		return connection;
	}
	
	private 
	<A extends Attribute, R, T extends ReferenceType<T>, E extends Entity<A, R, T, E>>
	PersistenceManager<A, R, T, E> create(E e, Implementation impl) {
		PersistenceManager<A, R, T, E> pm = new PersistenceManager<A, R, T, E>(e, impl);
		return pm;
	}

}
