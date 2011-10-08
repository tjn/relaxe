/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQueryExpressionSortKey;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.EntityQueryTemplateAttribute;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.expr.OrderBy;
import fi.tnie.db.gen.ent.LiteralCatalog;
import fi.tnie.db.gen.ent.personal.HourReport;
import fi.tnie.db.gen.ent.personal.Organization;
import fi.tnie.db.gen.ent.personal.Person;
import fi.tnie.db.gen.ent.personal.HourReport.Factory;
import fi.tnie.db.gen.ent.personal.HourReport.Holder;
import fi.tnie.db.gen.ent.personal.HourReport.MetaData;
import fi.tnie.db.gen.ent.personal.HourReport.Query;
import fi.tnie.db.gen.ent.personal.HourReport.QueryTemplate;
import fi.tnie.db.gen.ent.personal.HourReport.Type;
import fi.tnie.db.paging.DefaultEntityQueryPager;
import fi.tnie.db.paging.EntityFetcher;
import fi.tnie.db.paging.Receiver;
import fi.tnie.db.paging.DefaultEntityQueryPager.Command;
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
		return execute(template, null);		
	}
	
	private QueryResult<EntityDataObject<HourReport>> execute(HourReport.QueryTemplate template, FetchOptions opts) throws Exception {
		return execute(template.newQuery(), opts);
	}
	
	private QueryResult<EntityDataObject<HourReport>> execute(Query q, FetchOptions opts) throws Exception {
		Implementation imp = new PGImplementation();
		
		Connection c = newConnection(imp);
		
		try {
			EntityQueryExecutor<
				HourReport.Attribute, 
				HourReport.Reference, 
				HourReport.Type, 
				HourReport, 
				HourReport.Holder, 
				HourReport.Factory, 
				HourReport.MetaData,
				HourReport.QueryTemplate				
			> qe = createExecutor(HourReport.TYPE.getMetaData(), imp);
			
			
			// Query q = template.newQuery(limit, offset);
			
			EntityQueryResult<HourReport.Attribute, HourReport.Reference, Type, HourReport, HourReport.Holder, HourReport.Factory, MetaData, HourReport.QueryTemplate> er = qe.execute(q, opts, c);
			assertNotNull(er);
			QueryResult<EntityDataObject<HourReport>> qr = er.getContent(); 
			// QueryResult<EntityDataObject<HourReport>> qr = qe.execute(q, true, c);
			assertNotNull(qr);
			
			return qr;			
		}
		finally {
			if (c != null) {
				c.close();
			}
		}
	}

	private Connection newConnection(Implementation imp) throws SQLException {
		Properties cfg = new Properties();
		cfg.setProperty("user", "test");
		cfg.setProperty("password", "test");
		
		String url = imp.createJdbcUrl("127.0.0.1", "test");
		Driver drv = imp.getDriver();
		Connection c = drv.connect(url, cfg);
		return c;
	}
	

	public <
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M>,
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,
		M extends EntityMetaData<A, R, T, E, H, F, M>,
		QT extends EntityQueryTemplate<A, R, T, E, H, F, M, QT>
	>
	EntityQueryExecutor<A, R, T, E, H, F, M, QT> createExecutor(M meta, Implementation imp) {
		return new EntityQueryExecutor<A, R, T, E, H, F, M, QT>(imp);
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
		
		QueryResult<EntityDataObject<HourReport>> qr = execute(hrq, null);
		
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
				
		Query q3 = hrq.newQuery();								
		
		QueryResult<EntityDataObject<HourReport>> qr = execute(q3, new FetchOptions(3, 3));		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<HourReport>> el = qr.getContent();
		assertNotNull(el);
		
		logger().debug("testExecuteQuery: size=" + el.size());		
		assertTrue(el.size() <= 3);
		
		Query q36 = q3.getTemplate().newQuery();
		qr = execute(q36, new FetchOptions(3, 6));
		
		Long a = qr.getAvailable();
		assertNotNull(a);
		assertFalse(a.longValue() == 3);
		
		el = qr.getContent();
		assertNotNull(el);
		
		logger().debug("testExecuteQuery: size=" + el.size());		
		assertTrue(el.size() <= 3);
		
		logger().debug("testExecuteLimits: el=" + el);
	}
		
	public void testExecuteSort() throws Exception {
		
		HourReport.QueryTemplate hrq = new HourReport.QueryTemplate();
		hrq.addAllAttributes();
		
		Organization.QueryTemplate ot = new Organization.QueryTemplate();

		hrq.setTemplate(HourReport.FK_HHR_EMPLOYER, ot);
		hrq.desc(ot, Organization.Attribute.NAME);
				
		hrq.asc(ot, Organization.Attribute.YTUNNUS);
		hrq.desc(HourReport.Attribute.REPORT_DATE);
		
		hrq.addSortKey(EntityQueryExpressionSortKey.<HourReport.Attribute>newSortKey(new OrderBy.OrdinalSortKey(1)));
		
		Query q = hrq.newQuery();
		
		logger().info("testExecuteSort: q.getColumnMap()=" + q.getColumnMap());		
						
		String qs = q.getQueryExpression().generate();
		logger().info("testExecuteSort: qs=" + qs);
		
		qs = qs.toLowerCase();
		
		assertTrue(qs.matches(".+order +by.+name.+desc.+ytunnus.+report_date.+desc.*"));
		
	}

	public void testFetcher() throws Exception {
		HourReport.QueryTemplate hrq = new HourReport.QueryTemplate();
				
		PGImplementation imp = new PGImplementation();
		Connection c = newConnection(imp);
		HourReportFetcher f = new HourReportFetcher(imp, c);
		HourReportPager p = new HourReportPager(hrq, f);
		
		p.run(Command.LAST_PAGE);
		p.run(Command.NEXT_PAGE);
		
		p.fetchCurrent();
		p.fetchFirst();
		p.fetchNext();
		p.fetchLast();
		p.fetchPrevious();
		
		c.close();
		
		// DefaultEntityQueryPager<Attribute, Reference, ReferenceType<A,R,T,E,H,F,M>, Entity<A,R,T,E,H,F,M>, ReferenceHolder<A,R,T,E,H,M>, EntityFactory<E,H,M,F>, EntityMetaData<A,R,T,E,H,F,M>, EntityQueryTemplate<A,R,T,E,H,F,M,QT>>
		
		
	}
	
	
	public class HourReportFetcher
		extends SynchronousFetcher<
		HourReport.Attribute, 
		HourReport.Reference, 
		HourReport.Type, 
		HourReport, 
		HourReport.Holder, 
		HourReport.Factory, 
		HourReport.MetaData, 
		HourReport.QueryTemplate>
	{

		public HourReportFetcher(Implementation imp, Connection c) {			
			super(new EntityQueryExecutor<HourReport.Attribute, fi.tnie.db.gen.ent.personal.HourReport.Reference, Type, HourReport, Holder, Factory, MetaData, QueryTemplate>(imp), c);		
		}
	}

	public class HourReportPager
		extends DefaultEntityQueryPager<
		HourReport.Attribute, 
		HourReport.Reference, 
		HourReport.Type, 
		HourReport, 
		HourReport.Holder, 
		HourReport.Factory, 
		HourReport.MetaData, 
		HourReport.QueryTemplate>
	{

		public HourReportPager(
				QueryTemplate template,
				EntityFetcher<fi.tnie.db.gen.ent.personal.HourReport.Attribute, fi.tnie.db.gen.ent.personal.HourReport.Reference, Type, HourReport, Holder, Factory, MetaData, QueryTemplate> fetcher) {
			super(template, fetcher);
		}
	
		
	}
}
