/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

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
import fi.tnie.db.gen.ent.personal.Project;
import fi.tnie.db.gen.ent.personal.Person.Reference;
import fi.tnie.db.gen.ent.personal.Person.Type;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.Interval;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class PersistenceManagerTest2 extends TestCase  {
	
	private Connection connection = null;
	private LiteralCatalog catalog;
	
	private static Logger logger = Logger.getLogger(PersistenceManagerTest2.class);
	
	@Override
	protected void setUp() throws Exception {
		this.connection = createConnection();		
		this.catalog = LiteralCatalog.getInstance();
	}
	
	public Connection createConnection() throws SQLException {
		String url = "jdbc:postgresql:test";
		Connection c = DriverManager.getConnection(url, "test", "password");
		c.setAutoCommit(false);
		return c;
	}
	
	@Override
	protected void tearDown() throws Exception {
		QueryHelper.doClose(this.connection);
	}

    public void testPersistenceManager() 
        throws Exception {
        
        Connection c = getConnection();        
        assertFalse(c.getAutoCommit());                        
        LiteralCatalog cc = getCatalog();
                
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
        
//        hr.id().set(null);
        hr.setRef(HourReport.FK_HHR_EMPLOYER, org.ref());
        
        PersistenceManager<fi.tnie.db.gen.ent.personal.HourReport.Attribute, 
        	fi.tnie.db.gen.ent.personal.HourReport.Reference, 
        	fi.tnie.db.gen.ent.personal.HourReport.Type, 
        	HourReport,
        	fi.tnie.db.gen.ent.personal.HourReport.Holder,
        	fi.tnie.db.gen.ent.personal.HourReport.Factory,
        	HourReport.MetaData> hrm =
        	create(hr, impl);
        
        hr.setRef(HourReport.FK_HHR_EMPLOYER, org.ref());                
        hrm.merge(c);
        
        c.commit();        
    }
    
    public void testPersistenceManager2() 
    	throws Exception {
    
	    Connection c = getConnection();    
	    assertFalse(c.getAutoCommit());                    
	    LiteralCatalog cc = LiteralCatalog.getInstance();
	            
	    PersonalFactory pf = cc.newPersonalFactory();
            
	    PGImplementation impl = new PGImplementation();
    
	    HourReport hr = pf.newHourReport();
	    assertNotNull(hr.reportDate());
    
	    	    
	    
	    
	    DateKey<HourReport.Attribute, HourReport.Type, HourReport> dk = 
	    	hr.getMetaData().getDateKey(HourReport.Attribute.REPORT_DATE);
    
	    hr.setDate(dk, DateHolder.currentDate());                
	    assertNotNull(hr.reportDate().get());
	    assertNotNull(hr.getReportDate());
	    hr.setComment("asdfasd");
	    hr.setStartedAt(new Date());
	    hr.setFinishedAt(new Date());
	    
	    String suffix = Integer.toString((int) (Math.random() * 1000));
    	
	    Organization org = pf.newOrganization();
	    org.setName("Ab Firma Oy " + suffix);

	    Organization client = pf.newOrganization();
	    client.setName("Ab Asiakas Oy " + suffix);

	    final Project proj = pf.newProject();
	    
	    proj.setName("Project " + suffix);
	    proj.setAlias("P" + suffix);
	    
	    proj.setRef(Project.FK_SUPPLIER, org.ref());
	    proj.setRef(Project.FK_CLIENT, client.ref());
    
//	    hr.id().set(null);
	    
	    hr.setProject(HourReport.FK_HHR_PROJECT, proj.ref());
	    hr.setInteger(HourReport.ID, null);
	    
	    // hr.setRef(HourReport.FK_HHR_EMPLOYER, org.ref());
    
	    PersistenceManager<fi.tnie.db.gen.ent.personal.HourReport.Attribute, 
	    	fi.tnie.db.gen.ent.personal.HourReport.Reference, 
	    	fi.tnie.db.gen.ent.personal.HourReport.Type, 
	    	HourReport,
	    	fi.tnie.db.gen.ent.personal.HourReport.Holder,
	    	fi.tnie.db.gen.ent.personal.HourReport.Factory,
	    	HourReport.MetaData> hrm =
	    	create(hr, impl);
	    
//	    hr.setRef(HourReport.FK_HHR_EMPLOYER, org.ref());                
	    hrm.merge(c);	    	    	    
	    c.commit();  
	    
	    
	    
	    
    }
    
    public void testMerge() throws Exception {
        Connection c = getConnection();        
        assertFalse(c.getAutoCommit());                        
        PGImplementation impl = new PGImplementation();

    	HourReport hr = HourReport.TYPE.getMetaData().getFactory().newInstance();
    	hr.setComment("My Comment");
    	hr.setReportDate(new Date());
    	hr.setStartedAt(new Date());
    	hr.setFinishedAt(new Date());
    	
    	long ms = System.currentTimeMillis();
    	
		Organization client = Organization.TYPE.getMetaData().getFactory().newInstance();
		client.setName("Asiakas " + ms);
		
		Organization supplier = Organization.TYPE.getMetaData().getFactory().newInstance();
		supplier.setName("Toimittaja" + ms);
		
		Project p = Project.TYPE.getMetaData().getFactory().newInstance();
		p.setName("Test Project " + ms);
							
		p.setOrganization(Project.FK_CLIENT, client.ref());
		p.setOrganization(Project.FK_SUPPLIER, supplier.ref());
		
		hr.setOrganization(HourReport.FK_HHR_EMPLOYER, supplier.ref());
		hr.setProject(HourReport.FK_HHR_PROJECT, p.ref());
		
        PersistenceManager<?, ?, ?, ?, ?, ?, ?> hm = create(hr, impl);
        
        hm.merge(c);
        c.commit();
        
        hm.merge(c);
        c.commit();
        
        hr.id().setHolder(IntegerHolder.NULL_HOLDER);        
    	hr.setStartedAt(new Date());
    	hr.setFinishedAt(new Date());
    	hm.merge(c);
        c.commit();
        
        
        c.close();
    }

	private LiteralCatalog getCatalog() {		
		return this.catalog;
	}

	public Connection getConnection() {
		return connection;
	}
	
	private <
		A extends Attribute, 
		R extends fi.tnie.db.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,		
		M extends EntityMetaData<A, R, T, E, H, F, M>
	>
	PersistenceManager<A, R, T, E, H, F, M> create(E e, Implementation impl) {
		PersistenceManager<A, R, T, E, H, F, M> pm = new PersistenceManager<A, R, T, E, H, F, M>(e, impl);
		return pm;
	}
	
	public void testInsertSelect() throws Exception { 
		Connection c = getConnection();		
		assertNotNull(c);
				
//		PreparedStatement ps = c.prepareStatement("INSERT INTO public.continent DEFAULT VALUES RETURNING *");
		PreparedStatement ps = c.prepareStatement("INSERT INTO public.continent DEFAULT VALUES RETURNING id as generated_id, current_date as ff");
//		PreparedStatement ps = c.prepareStatement("INSERT INTO public.continent DEFAULT VALUES RETURNING ");

		int u = ps.executeUpdate();
		logger().debug("testInsertSelect: u=" + u);
		
//		boolean more = ps.execute();
//		logger().debug("testInsertSelect: hasResults=" + hasResults);
//		ResultSetWriter p = new ResultSetWriter(System.out, false);
//		
//		int results = 0;
//
//		while (true) {
//			if (more) {
//				results++;
//				ResultSet rs = ps.getResultSet();
//				
//				StatementExecutor e = new StatementExecutor(new PGImplementation());				
//				e.apply(p, rs);
//				
//				logger().info("testInsertSelect: rs=" + rs);
//				logger().debug("testInsertSelect: results=" + results);
//				rs.close();
//			}
//			else {
//				int uc = ps.getUpdateCount();
//				
//				if (uc == -1) {
//					break;
//				}
//				else {
//					logger().debug("uc=" + uc);
//				}
//			}
//			
//			more = ps.getMoreResults();
//		}
//		
//		
//		logger().debug("testInsertSelect: results=" + results);
		
//		if (hasResults) {
//			ResultSet rs = ps.getResultSet();
//			logger().debug("testInsertSelect: rs=" + rs);
//
//			boolean more = ps.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
//			logger().debug("testInsertSelect: more=" + more);
//			
//			int uc = ps.getUpdateCount();
//			logger().info("testInsertSelect: uc=" + uc);
//		}
		
		c.close();		
	}

	
	private static Logger logger() {
		return PersistenceManagerTest2.logger;
	}

}
