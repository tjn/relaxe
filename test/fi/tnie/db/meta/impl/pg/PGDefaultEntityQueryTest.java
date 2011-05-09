/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fi.tnie.db.EntityBuilder;
import fi.tnie.db.EntityReader;
import fi.tnie.db.PersistenceManager;
import fi.tnie.db.PersistenceManagerTest;
import fi.tnie.db.QueryException;
import fi.tnie.db.StatementExecutor;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.ent.CyclicTemplateException;
import fi.tnie.db.ent.DefaultEntityQuery;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGCatalogFactory;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.exec.QueryProcessor;
import fi.tnie.db.gen.ent.LiteralCatalog;
import fi.tnie.db.gen.ent.personal.HourReport;
import fi.tnie.db.gen.ent.personal.Organization;
import fi.tnie.db.gen.ent.personal.PersonalFactory;
import fi.tnie.db.gen.ent.personal.Project;
import fi.tnie.db.gen.ent.personal.HourReport.MetaData;
import fi.tnie.db.gen.ent.personal.HourReport.Reference;
import fi.tnie.db.gen.ent.personal.HourReport.Type;
import fi.tnie.db.gen.ent.personal.Project.Holder;
import fi.tnie.db.gen.ent.personal.Project.Key;
import fi.tnie.db.meta.DBMetaTestCase;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.rpc.VarcharHolder;

public class PGDefaultEntityQueryTest extends DBMetaTestCase {

    @Override
	public PGCatalogFactory factory() {    	
    	implementation();
    	
        PGImplementation e = new PGImplementation();
        return new PGCatalogFactory(e.getEnvironment());
    }
    
    @Override
    protected Implementation implementation() {
    	return new PGImplementation();
    }
    
    public void testConstructor() throws CyclicTemplateException, SQLException, QueryException {
    	Connection c = getConnection();
    	assertNotNull(c);
    	
    	Implementation imp = implementation();
    	    	
    	LiteralCatalog cc = LiteralCatalog.getInstance();
    	PersonalFactory pf = cc.newPersonalFactory();
    	    	
    	HourReport hr = pf.newHourReport();
    	Organization org = pf.newOrganization();
    	Project proj = pf.newProject();
    	    	
    	proj.setName("project name");
    	org.name().set("org name");
    	hr.setRef(HourReport.FK_HHR_PROJECT, proj.ref());
    	proj.setRef(Project.FK_SUPPLIER, org.ref());
    	
    	Key<Reference, Type, HourReport, MetaData> k = HourReport.FK_HHR_PROJECT;
    	Holder h = k.get(hr);
    	assertNotNull(h);
    	assertSame(proj.ref(), h);
    	
//    	DefaultEntityQuery<?, ?, ?, ?, ?> e = 
//    		new DefaultEntityQuery<HourReport.Attribute, HourReport.Reference, HourReport.Type, HourReport, HourReport.MetaData>(hr);
    	
    	HourReport.Query e = new HourReport.Query(hr);
    	
    	String qs = e.getQuery().generate();
    	    	   	
    	logger().info("testThis: qs=" + qs);
    	    	
    	Statement st = c.createStatement();
    	
    	ValueExtractorFactory vef = imp.getValueExtractorFactory();
    	    	    	    	
    	EntityReader<HourReport.Attribute, HourReport.Reference, HourReport.Type, HourReport, HourReport.MetaData> eb
    		= new EntityReader<HourReport.Attribute, HourReport.Reference, HourReport.Type, HourReport, HourReport.MetaData>(vef, e);
    	    	    	    	
    	StatementExecutor se = new StatementExecutor();
    	se.execute(eb.getQuery().getQuery(), c, eb);
    	
    	logger().info("testConstructor: eb.getContent().size()=" + eb.getContent().size());
    	
//    	ResultSet rs = st.executeQuery(qs);
//    	rs.next();
//    	rs.close();
    	st.close();   	
    }
    
    
    public void testConstructor2() throws CyclicTemplateException, SQLException {
    	Connection c = getConnection();
    	assertNotNull(c);
    	    	
    	LiteralCatalog cc = LiteralCatalog.getInstance();
    	PersonalFactory pf = cc.newPersonalFactory();
    	    	
    	HourReport hr = pf.newHourReport();
    	
    	DefaultEntityQuery<?, ?, ?, ?, ?> e = 
    		new DefaultEntityQuery<HourReport.Attribute, HourReport.Reference, HourReport.Type, HourReport, HourReport.MetaData>(hr.getMetaData());

    	String qs = e.getQuery().generate();
    	
    	logger().info("testConstructor2: qs=" + qs);
    	
    	Statement st = c.createStatement();
    	
    	ResultSet rs = st.executeQuery(qs);
    	
    	rs.next();
    	rs.close();
    	st.close();  	
    	
    }
    
    
//    public void testCreate() 
//        throws Exception {        
//        PGCatalogFactory factory = factory();
//        testCatalogFactory(factory, getConnection());
//    }
    
    
}
