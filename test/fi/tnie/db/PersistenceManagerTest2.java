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
import fi.tnie.db.gen.ent.personal.HourReport;
import fi.tnie.db.gen.ent.personal.Organization;
import fi.tnie.db.gen.ent.personal.Person;
import fi.tnie.db.gen.ent.personal.PersonalFactory;
import fi.tnie.db.gen.ent.personal.Person.Reference;
import fi.tnie.db.gen.ent.personal.Person.Type;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.Interval;
import fi.tnie.db.rpc.ReferenceHolder;
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
                        
        LiteralCatalog cc = LiteralCatalog.getInstance();
                
        BaseTable ct = LiteralCatalog.LiteralBaseTable.PUBLIC_CONTINENT;
        assertNotNull(ct);
        
//        logger().debug("bound tables: " + cc.getMetaMap().keySet());
//        logger().debug("key-table: " + ct);
//        logger().debug("key-table-loader: " + ct.getClass().getClassLoader());
//        
//        logger().debug("bound-table-loader: " + cc.getMetaMap().keySet().iterator().next().getClass().getClassLoader());
                
//        assertTrue(cc.getMetaMap().containsKey(ct));               
                        
        EntityMetaData<?, ?, ?, ?, ?, ?, ?> meta = cc.getMetaData(ct);
        assertNotNull(meta);
                
        EntityFactory<?, ?, ?, ?> ef = meta.getFactory();
        assertNotNull(ef);
                
        PersonalFactory pf = cc.newPersonalFactory();
        Person p = pf.newPerson();
                
        PGImplementation impl = new PGImplementation();
        
        PersistenceManager<
        	fi.tnie.db.gen.ent.personal.Person.Attribute, Reference, Type, Person, Person.Holder, Person.Factory, Person.MetaData> pm = 
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
        
        Organization org = pf.newOrganization();
        
        DateKey<HourReport.Attribute, HourReport.Type, HourReport> dk = 
        	hr.getMetaData().getDateKey(HourReport.Attribute.REPORT_DATE);
        
        hr.setDate(dk, DateHolder.currentDate());                
        assertNotNull(hr.reportDate().get());
        assertNotNull(hr.getReportDate());
        hr.setComment("asdfasd");
        hr.setStartedAt(new Date());
        hr.setFinishedAt(new Date());
        
        org.setRef(Organization.FK_COMPANY_CEO, p.ref());
        org.setName("Ab Firma Oy " + ((int) (Math.random() * 1000)));
        
        PersistenceManager<?, ?, ?, ?, ?, ?, ?> om = create(org, impl);
        
        om.merge(c);
        c.commit();
                
                
        
//       hr.ref(HourReport.Reference.FK_HHR_EMPLOYER). org);        
//        Organization.Key<Attribute, R, ReferenceType<T>, Entity<A,R,T,E>, ?>        
//        hr.setRef(HourReport.FK_HHR_EMPLOYER, org);
        
        hr.id().set(null);
        hr.setRef(HourReport.FK_HHR_EMPLOYER, org.ref());
        
        PersistenceManager<fi.tnie.db.gen.ent.personal.HourReport.Attribute, 
        	fi.tnie.db.gen.ent.personal.HourReport.Reference, 
        	fi.tnie.db.gen.ent.personal.HourReport.Type, 
        	HourReport,
        	fi.tnie.db.gen.ent.personal.HourReport.Holder,
        	fi.tnie.db.gen.ent.personal.HourReport.Factory,
        	HourReport.MetaData> hrm =
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
	
	private <
		A extends Attribute, 
		R extends fi.tnie.db.ent.Reference, 
		T extends ReferenceType<T, M>, 
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,		
		M extends EntityMetaData<A, R, T, E, H, F, M>
	>
	PersistenceManager<A, R, T, E, H, F, M> create(E e, Implementation impl) {
		PersistenceManager<A, R, T, E, H, F, M> pm = new PersistenceManager<A, R, T, E, H, F, M>(e, impl);
		return pm;
	}

}
