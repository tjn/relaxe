/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.Driver;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQueryTemplateAttribute;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.gen.ent.LiteralCatalog;
import fi.tnie.db.gen.ent.personal.HourReport;
import fi.tnie.db.gen.ent.personal.Organization;
import fi.tnie.db.gen.ent.personal.Person;
import fi.tnie.db.gen.ent.personal.HourReport.Query;
import fi.tnie.db.query.QueryResult;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;
import junit.framework.TestCase;

public class EntityQueryExecutorTest extends TestCase {
	
	private static Logger logger = Logger.getLogger(EntityQueryExecutorTest.class);
	
	@Override
	protected void setUp() throws Exception {
		LiteralCatalog.getInstance();
	}
	
	private QueryResult<EntityDataObject<HourReport>> execute(HourReport.QueryTemplate template) throws Exception {
		return execute(template.newQuery(0, 0));
	}
	
	private QueryResult<EntityDataObject<HourReport>> execute(Query q) throws Exception {
		Implementation imp = new PGImplementation();
		
		Properties cfg = new Properties();
		cfg.setProperty("user", "test");
		cfg.setProperty("password", "test");
		
		String url = imp.createJdbcUrl("127.0.0.1", "test");
		Driver drv = imp.getDriver();
		Connection c = drv.connect(url, cfg);
		
		try {
			EntityQueryExecutor<
				HourReport.Attribute, 
				HourReport.Reference, 
				HourReport.Type, 
				HourReport, 
				HourReport.Holder, 
				HourReport.Factory, 
				HourReport.MetaData
			> qe = createExecutor(HourReport.TYPE.getMetaData(), imp);
			
			
			// Query q = template.newQuery(limit, offset);			
			 
			QueryResult<EntityDataObject<HourReport>> qr = qe.execute(q, c);
			assertNotNull(qr);
			
			return qr;			
		}
		finally {
			if (c != null) {
				c.close();
			}
		}
	}
	

	public <
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M>,
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,
		M extends EntityMetaData<A, R, T, E, H, F, M>
	>
	EntityQueryExecutor<A, R, T, E, H, F, M> createExecutor(M meta, Implementation imp) {
		return new EntityQueryExecutor<A, R, T, E, H, F, M>(imp);
	}
	
	private static Logger logger() {
		return EntityQueryExecutorTest.logger;
	}

	
	public void testExecute3() throws Exception {		
		HourReport.QueryTemplate hrq = new HourReport.QueryTemplate(); 
		hrq.addAllAttributes();
				
		Organization.QueryTemplate oq = new Organization.QueryTemplate();
		oq.addAllAttributes();
				
		hrq.setTemplate(HourReport.FK_HHR_EMPLOYER, oq);
		
		QueryResult<EntityDataObject<HourReport>> qr = execute(hrq);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<HourReport>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<HourReport> eo : el) {
			assertNotNull(eo);
			HourReport hr = eo.getRoot();
			assertNotNull(hr);
			
			Organization.Holder oh = hr.getOrganization(HourReport.FK_HHR_EMPLOYER);
			assertNotNull(oh);
			Organization org = oh.value();
			assertNotNull(org);
			assertTrue(org.isIdentified());
		}

	}

	public void testExecute4() throws Exception {
		HourReport.QueryTemplate hrq = new HourReport.QueryTemplate(); 
		hrq.addAllAttributes();
				
		Organization.QueryTemplate oq = new Organization.QueryTemplate();		
		oq.remove(oq.getMetaData().attributes());					
		hrq.setTemplate(HourReport.FK_HHR_EMPLOYER, oq);
		
		QueryResult<EntityDataObject<HourReport>> qr = execute(hrq);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<HourReport>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<HourReport> eo : el) {
			assertNotNull(eo);
			HourReport hr = eo.getRoot();
			assertNotNull(hr);
			
			Organization.Holder oh = hr.getOrganization(HourReport.FK_HHR_EMPLOYER);
			assertNotNull(oh);
			Organization org = oh.value();
			assertNotNull(org);
			assertTrue(org.isIdentified());
		}
	}
	
	public void testExecute5() throws Exception {
		HourReport.QueryTemplate hrq = new HourReport.QueryTemplate(); 
		hrq.addAllAttributes();
							
		hrq.setTemplate(HourReport.FK_HHR_EMPLOYER, (Organization.QueryTemplate) null);
		
		QueryResult<EntityDataObject<HourReport>> qr = execute(hrq);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<HourReport>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<HourReport> eo : el) {
			assertNotNull(eo);
			HourReport hr = eo.getRoot();
			assertNotNull(hr);
			
			Organization.Holder oh = hr.getOrganization(HourReport.FK_HHR_EMPLOYER);
			assertNull(oh);
		}
	}
	
	public void testExecute6() throws Exception {
		HourReport.QueryTemplate hrq = new HourReport.QueryTemplate(); 
		hrq.addAllAttributes();
							
		Organization.QueryTemplate ot = new Organization.QueryTemplate();
		ot.remove(ot.getMetaData().attributes());
		
		hrq.setTemplate(HourReport.FK_HHR_EMPLOYER, ot);
		
		Person.QueryTemplate pt = new Person.QueryTemplate();
		pt.add(Person.Attribute.DATE_OF_BIRTH, Person.Attribute.LAST_NAME);
		ot.setTemplate(Organization.FK_COMPANY_CEO, pt);
		
		QueryResult<EntityDataObject<HourReport>> qr = execute(hrq);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<HourReport>> el = qr.getContent();
		assertNotNull(el);
		
		for (EntityDataObject<HourReport> eo : el) {
			assertNotNull(eo);
			HourReport hr = eo.getRoot();
			assertNotNull(hr);
			
			Organization.Holder oh = hr.getOrganization(HourReport.FK_HHR_EMPLOYER);
			assertNotNull(oh);
			Organization org = oh.value();
			assertNotNull(org);
			assertTrue(org.isIdentified());
			Person.Holder ph = org.getPerson(Organization.FK_COMPANY_CEO);
			assertNotNull(ph);
			
			Person p = ph.value();
			
			if (p != null) {
				Set<fi.tnie.db.gen.ent.personal.Person.Attribute> as = p.attributes();
				assertEquals(3, as.size());
				assertTrue(as.contains(Person.Attribute.ID));
				assertTrue(as.contains(Person.Attribute.DATE_OF_BIRTH));
				assertTrue(as.contains(Person.Attribute.LAST_NAME));
				
			}
		}
	}
	
	public void testExecuteLimits() throws Exception {
		HourReport.QueryTemplate hrq = new HourReport.QueryTemplate();
		
		
		hrq.addAllAttributes();
		
		EntityQueryTemplateAttribute rd = hrq.get(HourReport.Attribute.REPORT_DATE);		
		
		Query q3 = hrq.newQuery(3, 3);								
		
		QueryResult<EntityDataObject<HourReport>> qr = execute(q3);		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<HourReport>> el = qr.getContent();
		assertNotNull(el);
		
		logger().debug("testExecuteQuery: size=" + el.size());		
		assertTrue(el.size() <= 3);
		
		Query q36 = q3.getTemplate().newQuery(3, 6);
		qr = execute(q36);
		
		el = qr.getContent();
		assertNotNull(el);
		
		logger().debug("testExecuteQuery: size=" + el.size());		
		assertTrue(el.size() <= 3);
		
		logger().debug("testExecuteLimits: el=" + el);
	}	
	
}
