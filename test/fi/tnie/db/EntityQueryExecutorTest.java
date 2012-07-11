/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQueryExpressionSortKey;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.EntityQueryTemplateAttribute;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.ent.PredicateAttributeTemplate;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.expr.OrderBy;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.gen.ent.LiteralCatalog;
import fi.tnie.db.gen.ent.personal.HourReport;
import fi.tnie.db.gen.ent.personal.Organization;
import fi.tnie.db.gen.ent.personal.Person;
import fi.tnie.db.gen.ent.personal.Project;
import fi.tnie.db.gen.ent.personal.HourReport.MetaData;
import fi.tnie.db.gen.ent.personal.HourReport.Query;
import fi.tnie.db.gen.ent.personal.HourReport.Type;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.query.QueryResult;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class EntityQueryExecutorTest extends AbstractUnitTest {
	
		
	@Override
	protected void init() {				
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
				HourReport.Content,
				HourReport.QueryTemplate				
			> qe = createExecutor(HourReport.Type.TYPE.getMetaData(), imp);
			
			
			// Query q = template.newQuery(limit, offset);
			
			EntityQueryResult<HourReport.Attribute, HourReport.Reference, Type, HourReport, HourReport.Holder, HourReport.Factory, HourReport.MetaData, HourReport.Content, HourReport.QueryTemplate> er = qe.execute(q, opts, c);
			assertNotNull(er);
			
			DataObjectQueryResult<EntityDataObject<HourReport>> qr = er.getContent(); 
			// QueryResult<EntityDataObject<HourReport>> qr = qe.execute(q, true, c);
			assertNotNull(qr);
			
			DataObject.MetaData meta = qr.getMeta();
			assertNotNull(meta);
			
			int cc = meta.getColumnCount();
			assertTrue(cc > 0);
			
			for (int i = 0; i < cc; i++) {
				ValueExpression ve = meta.expr(i);
				assertNotNull(ve);
			}
			
			
			return qr;			
		}
		finally {
			if (c != null) {
				c.close();
			}
		}
	}

	private Connection newConnection(Implementation imp) throws Exception {
		Properties cfg = new Properties();
		cfg.setProperty("user", "test");
		cfg.setProperty("password", "test");
		
		String url = imp.createJdbcUrl("127.0.0.1", "test");
		Class.forName(imp.defaultDriverClassName());
		Connection c = DriverManager.getConnection(url, cfg);
		return c;
	}
	

	public <
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content,
		QT extends EntityQueryTemplate<A, R, T, E, H, F, M, C, QT>
	>
	EntityQueryExecutor<A, R, T, E, H, F, M, C, QT> createExecutor(M meta, Implementation imp) {
		return new EntityQueryExecutor<A, R, T, E, H, F, M, C, QT>(imp, getIdentityContext());
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
		
		Project.QueryTemplate jt = new Project.QueryTemplate();
		jt.addAllAttributes();		
		hrq.setTemplate(HourReport.FK_HHR_PROJECT, jt);
							
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
			
			IntervalHolder.DayTime dt = hr.getInterval(HourReport.TRAVEL_TIME);
			assertNotNull(dt);
			
			Project.Holder jh = hr.getProject(HourReport.FK_HHR_PROJECT);
			assertNotNull(jh);
			assertNotNull(jh.value());			
			
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

	public void testNotNullPredicate() throws Exception {
		HourReport.QueryTemplate ht = new HourReport.QueryTemplate();
		ht.addAllAttributes();
		
		PredicateAttributeTemplate<fi.tnie.db.gen.ent.personal.HourReport.Attribute> p = 
			PredicateAttributeTemplate.eq(HourReport.Attribute.ID, (Integer) null);
		
		ht.addPredicate(p);
		
		Organization.QueryTemplate ot = new Organization.QueryTemplate();

		ht.setTemplate(HourReport.FK_HHR_EMPLOYER, ot);
		ht.desc(ot, Organization.Attribute.NAME);				
		
		Query q = ht.newQuery();
		
		QueryResult<EntityDataObject<HourReport>> qr = execute(q, null);
		assertNotNull(qr);
		assertEquals(0, qr.size());
		
	}
	
	
	public void testNullPredicate() throws Exception {
		PredicateAttributeTemplate<fi.tnie.db.gen.ent.personal.HourReport.Attribute> p = PredicateAttributeTemplate.eq(HourReport.Attribute.ID, (Integer) null);
		QueryResult<EntityDataObject<HourReport>> qr = executeWithPredicate(p);
		assertNotNull(qr);
		assertEquals(0, qr.size());
	}
	
	public void testIntPredicate() throws Exception {
		PredicateAttributeTemplate<fi.tnie.db.gen.ent.personal.HourReport.Attribute> p = 
			PredicateAttributeTemplate.eq(HourReport.Attribute.ID, Integer.valueOf(30));
		
		QueryResult<EntityDataObject<HourReport>> qr = executeWithPredicate(p);
		assertNotNull(qr);
		assertEquals(1, qr.size());
	}
	
	
	public void testStringPredicate() throws Exception {
		PredicateAttributeTemplate<fi.tnie.db.gen.ent.personal.HourReport.Attribute> p = 
			PredicateAttributeTemplate.eq(HourReport.Attribute.COMMENT, "palaveri");
		
		QueryResult<EntityDataObject<HourReport>> qr = executeWithPredicate(p);
		assertNotNull(qr);
		assertEquals(5, qr.size());
	}
	
	public QueryResult<EntityDataObject<HourReport>> executeWithPredicate(PredicateAttributeTemplate<fi.tnie.db.gen.ent.personal.HourReport.Attribute> p) throws Exception {
		HourReport.QueryTemplate ht = new HourReport.QueryTemplate();
		ht.addAllAttributes();
		
		ht.addPredicate(p);
		
		Organization.QueryTemplate ot = new Organization.QueryTemplate();

		ht.setTemplate(HourReport.FK_HHR_EMPLOYER, ot);
		ht.desc(ot, Organization.Attribute.NAME);				
		
		Query q = ht.newQuery();
		
		QueryResult<EntityDataObject<HourReport>> qr = execute(q, null);
		assertNotNull(qr);
		return qr;
		
	}

	
	
	public void testProject() throws SQLException, QueryException, EntityException, ClassNotFoundException {
		Project.QueryTemplate qt = new Project.QueryTemplate();
		
		qt.add(
				Project.Attribute.ALIAS, 
				Project.Attribute.NAME
//				, 
//				Project.Attribute.CREATED_AT
		);
		
		Organization.QueryTemplate ct = new Organization.QueryTemplate();
		ct.add(Organization.Attribute.NAME);		
		qt.setTemplate(Project.FK_CLIENT, ct);
		
		Organization.QueryTemplate st = new Organization.QueryTemplate();
		st.add(Organization.Attribute.NAME);	
				
		qt.setTemplate(Project.FK_SUPPLIER, st);
		
		Person.QueryTemplate cpt = new Person.QueryTemplate();
		cpt.add(Person.Attribute.FIRST_NAME, Person.Attribute.LAST_NAME);
		st.setTemplate(Organization.FK_COMPANY_CEO, cpt);
		
		fi.tnie.db.gen.ent.personal.Project.Query q = qt.newQuery();
		
		String qs = q.getQueryExpression().generate();
		logger().debug("testProject: qs=" + qs);

		EntityQueryExecutor<
			Project.Attribute,
	        Project.Reference,        
	        Project.Type,
	        Project,
	        Project.Holder,		
	        Project.Factory,
	        Project.MetaData,
	        Project.Content,
	        Project.QueryTemplate
	    > qe = new EntityQueryExecutor<
	        Project.Attribute,
	        Project.Reference,        
	        Project.Type,
	        Project,
	        Project.Holder,		
	        Project.Factory,
	        Project.MetaData,
	        Project.Content,
	        Project.QueryTemplate
	      >(getImplementation(), getIdentityContext());

		Connection c = newConnection();
		
		qe.execute(q, null, c);
		
		c.close();
	}
	
	
	private UnificationContext getIdentityContext(){
		return new SimpleUnificationContext();
	}
}
