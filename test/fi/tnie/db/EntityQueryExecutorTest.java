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
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.gen.ent.LiteralCatalog;
import fi.tnie.db.gen.ent.personal.HourReport;
import fi.tnie.db.gen.ent.personal.Organization;
import fi.tnie.db.gen.ent.personal.PersonalFactory;
import fi.tnie.db.gen.ent.personal.HourReport.Type;
import fi.tnie.db.meta.Column;
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
	
	public void testExecute1() throws Exception {
		PersonalFactory pf = LiteralCatalog.getInstance().newPersonalFactory();
		HourReport template = pf.newHourReport();
		template.addAllAttributes();
		
		Organization o = pf.newOrganization();
		o.addAllAttributes();
		
		template.setOrganization(HourReport.FK_HHR_EMPLOYER, o.ref());
		QueryResult<EntityDataObject<HourReport>> qr = execute(template);
		
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
	
	public void testExecute2() throws Exception {
		PersonalFactory pf = LiteralCatalog.getInstance().newPersonalFactory();
		HourReport template = pf.newHourReport();	
		QueryResult<EntityDataObject<HourReport>> qr = execute(template);
		
		logger().debug("testExecuteQuery: qr.getElapsed()=" + qr.getElapsed());
		
		List<? extends EntityDataObject<HourReport>> el = qr.getContent();
		assertNotNull(el);
		
		HourReport.MetaData meta = template.getMetaData();
		Set<HourReport.Attribute> as = meta.attributes();
		Set<Column> pks = meta.getPKDefinition();
		
		for (EntityDataObject<HourReport> eo : el) {
			assertNotNull(eo);
			HourReport hr = eo.getRoot();
			assertNotNull(hr);
		
			for (HourReport.Attribute a : as) {
				Column c = meta.getColumn(a);
				PrimitiveKey<fi.tnie.db.gen.ent.personal.HourReport.Attribute, Type, HourReport, ?, ?, ?, ?> pk = meta.getKey(a);
				
				if (pks.contains(c)) {
					assertNotNull(hr.get(pk.self()));					
				}
				else {
					assertFalse(hr.has(pk.self()));	
				}				
			}			
		}		
	}
	
	private QueryResult<EntityDataObject<HourReport>> execute(HourReport template) throws Exception {
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
			
			
			HourReport.Query hq = new HourReport.Query(template);
			
			QueryResult<EntityDataObject<HourReport>> qr = qe.execute(hq, c);
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

}
