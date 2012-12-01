/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.sql.Connection;
import java.sql.Statement;

import fi.tnie.db.EntityReader;
import fi.tnie.db.SimpleUnificationContext;
import fi.tnie.db.StatementExecutor;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGCatalogFactory;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.expr.SelectStatement;
import fi.tnie.db.gen.pg.ent.LiteralCatalog;
import fi.tnie.db.gen.pg.ent.personal.HourReport;
import fi.tnie.db.gen.pg.ent.personal.Organization;
import fi.tnie.db.gen.pg.ent.personal.PersonalFactory;
import fi.tnie.db.gen.pg.ent.personal.Project;
import fi.tnie.db.meta.DBMetaTestCase;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.Holder;

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
    
    public void testConstructor() throws Exception {
    	Connection c = getConnection();
    	assertNotNull(c);
    	
    	Implementation imp = implementation();
    	    	
    	LiteralCatalog cc = LiteralCatalog.getInstance();
    	PersonalFactory pf = cc.newPersonalFactory();
    	    	
    	HourReport hr = pf.newHourReport();
    	Organization org = pf.newOrganization();
    	Project proj = pf.newProject();
    	
    	Organization.Content oc = org.getContent();
    	    	
    	proj.getContent().setName("project name");
    	oc.name().set("org name");
    	hr.setRef(HourReport.FK_HHR_PROJECT, proj.ref());
    	// hr.setTravelTime();
    	hr.getContent().setStartedAt(new java.util.Date());
    	hr.set(HourReport.REPORT_DATE, DateHolder.NULL_HOLDER);
//    	hr.set(HourReport.CREATED_AT, TimestampHolder.NULL_HOLDER);
    	
    	proj.setRef(Project.FK_SUPPLIER, org.ref());
    	
    	Project.Key<?, HourReport.Reference, HourReport.Type, HourReport, ?, ?, HourReport.MetaData, ?> k = HourReport.FK_HHR_PROJECT;
    	Holder<?, ?> h = k.get(hr);
    	assertNotNull(h);
    	assertSame(proj.ref(), h);
    	
//    	DefaultEntityQuery<?, ?, ?, ?, ?> e = 
//    		new DefaultEntityQuery<HourReport.Attribute, HourReport.Reference, HourReport.Type, HourReport, HourReport.MetaData>(hr);
    	
    	HourReport.QueryTemplate t = new HourReport.QueryTemplate();
    	HourReport.Query e = t.newQuery();    	
    	
    	String qs = e.getQueryExpression().generate();
    	    	   	
    	logger().info("testThis: qs=" + qs);
    	    	
    	Statement st = c.createStatement();
    	
//    	ValueExtractorFactory vef = imp.getValueExtractorFactory();
    	
    	UnificationContext ic = new SimpleUnificationContext();
    	    	    	    	
    	EntityReader<HourReport.Attribute, HourReport.Reference, HourReport.Type, HourReport, HourReport.Holder, HourReport.Factory, HourReport.MetaData, HourReport.Content> eb
    		= new EntityReader<HourReport.Attribute, HourReport.Reference, HourReport.Type, HourReport, HourReport.Holder, HourReport.Factory, HourReport.MetaData, HourReport.Content>(imp, e, ic);
    	    	    	    	
    	PGImplementation impl = new PGImplementation();
    	
    	StatementExecutor se = new StatementExecutor(impl);
    	
    	SelectStatement ss = new SelectStatement(eb.getQuery().getQueryExpression());
    	se.execute(ss, c, eb);
    	
    	logger().info("testConstructor: eb.getContent().size()=" + eb.getContent().size());
    	
//    	ResultSet rs = st.executeQuery(qs);
//    	rs.next();
//    	rs.close();
    	st.close();   	
    	    	
    	for (EntityDataObject<HourReport> o : eb.getContent()) {
			logger().info("testConstructor: rpt=" + o);
			HourReport root = o.getRoot();			
			logger().debug("testConstructor: root=" + root);
			Project.Holder ph = root.getProject(HourReport.FK_HHR_PROJECT);
			logger().info("testConstructor: rpt.project=" + ph);    		
		}
    }
    
    
//    public void testConstructor2() throws CyclicTemplateException2, SQLException {
//    	Connection c = getConnection();
//    	assertNotNull(c);
//    	    	
//    	LiteralCatalog cc = LiteralCatalog.getInstance();
//    	PersonalFactory pf = cc.newPersonalFactory();
//    	    	
//    	HourReport hr = pf.newHourReport();
//    	
//    	DefaultEntityQuery<?, ?, ?, ?, ?, ?> e = 
//    		new DefaultEntityQuery<HourReport.Attribute, HourReport.Reference, HourReport.Type, HourReport, HourReport.Factory, HourReport.MetaData>(hr.getMetaData());
//
//    	String qs = e.getQueryExpression().generate();
//    	
//    	logger().info("testConstructor2: qs=" + qs);
//    	
//    	Statement st = c.createStatement();
//    	
//    	ResultSet rs = st.executeQuery(qs);
//    	
//    	rs.next();
//    	rs.close();
//    	st.close();  	
//    	
//    }
    
    
//    public void testCreate() 
//        throws Exception {        
//        PGCatalogFactory factory = factory();
//        testCatalogFactory(factory, getConnection());
//    }
    
    
}
