/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.pg.pp;

import java.sql.Connection;
import java.sql.Statement;

import com.appspot.relaxe.EntityQueryExpressionBuilder;
import com.appspot.relaxe.EntityReader;
import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.StatementExecutor;
import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.ent.EntityDataObject;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.rdbms.Implementation;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.gen.pg.pp.ent.pub.Actor;
import com.appspot.relaxe.gen.pg.pp.ent.pub.Category;
import com.appspot.relaxe.gen.pg.pp.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pp.ent.pub.FilmActor;
import com.appspot.relaxe.gen.pg.pp.ent.pub.FilmActor.Attribute;
import com.appspot.relaxe.gen.pg.pp.ent.pub.FilmActor.Content;
import com.appspot.relaxe.gen.pg.pp.ent.pub.FilmActor.Factory;
import com.appspot.relaxe.gen.pg.pp.ent.pub.FilmActor.Holder;
import com.appspot.relaxe.gen.pg.pp.ent.pub.FilmActor.MetaData;
import com.appspot.relaxe.gen.pg.pp.ent.pub.FilmActor.QueryElement;
import com.appspot.relaxe.gen.pg.pp.ent.pub.FilmActor.Reference;
import com.appspot.relaxe.gen.pg.pp.ent.pub.FilmActor.Type;
import com.appspot.relaxe.gen.pg.pp.ent.pub.FilmCategory;
import com.appspot.relaxe.rdbms.pg.PGCatalogFactory;
import com.appspot.relaxe.rdbms.pg.PGImplementation;
import com.appspot.relaxe.rdbms.pg.PGTestCase;


public class PagilaDefaultEntityQueryTest
	extends PGTestCase {

    @Override
	public PGCatalogFactory factory() {
    	return new PGCatalogFactory(implementation().getEnvironment());
    }

    public void testQuery1() throws Exception {
    	Connection c = getConnection();
    	assertNotNull(c);
    	logger().debug(c.getMetaData().getURL());

    	PersistenceContext<PGImplementation> pc = getPersistenceContext();

    	FilmActor.QueryElement.Builder qeb = new FilmActor.QueryElement.Builder();

    	FilmActor.QueryElement fae = qeb.newQueryElement();

    	FilmActor.Query faq = new FilmActor.Query(fae);

    	EntityQueryExpressionBuilder<Attribute, Reference, Type, FilmActor, Holder, Factory, MetaData, Content, QueryElement> qxb = newQueryExpressionBuilder(faq);

    	logger().debug("testQuery1: qxb=" + qxb);

    	QueryExpression qe = qxb.getQueryExpression();
    	// new EntityQ

    	String qs = qe.generate();

    	logger().info("testThis: qs=" + qs);

    	Statement st = c.createStatement();
    	UnificationContext ic = new SimpleUnificationContext();

    	ValueExtractorFactory vef = pc.getValueExtractorFactory();

    	EntityReader<FilmActor.Attribute, FilmActor.Reference, FilmActor.Type, FilmActor, FilmActor.Holder, FilmActor.Factory, FilmActor.MetaData, FilmActor.Content, FilmActor.QueryElement> er
    		= new EntityReader<FilmActor.Attribute, FilmActor.Reference, FilmActor.Type, FilmActor, FilmActor.Holder, FilmActor.Factory, FilmActor.MetaData, FilmActor.Content, FilmActor.QueryElement>(vef, qxb, ic);

    	StatementExecutor se = new StatementExecutor(pc);

    	// EntityQueryContext

    	SelectStatement ss = new SelectStatement(qe);

    	se.execute(ss, c, er);

    	logger().info("testConstructor: eb.getContent().size()=" + er.getContent().size());
    	st.close();

    	for (EntityDataObject<FilmActor> o : er.getContent()) {
//			logger().info("testConstructor: rpt=" + o);
			FilmActor root = o.getRoot();
//			logger()debug("testConstructor: root=" + root);

			assertNotNull(root);
			assertTrue(root.isIdentified());

			assertNull(root.getContent().lastUpdate().getHolder());

			{
				Actor.Holder ah = root.getActor(FilmActor.ACTOR);
				assertNotNull(ah);
				assertFalse(ah.isNull());
				assertNotNull(ah.value().getContent().getActorId());
				assertEquals(1, ah.value().attributes().size());
			}

			{
				Film.Holder fh = root.getFilm(FilmActor.FILM);
				assertNotNull(fh);
				assertFalse(fh.isNull());
				assertNotNull(fh.value().getContent().getFilmId());
				assertEquals(1, fh.value().attributes().size());
			}
		}
    }

	protected EntityQueryExpressionBuilder<Attribute, Reference, Type, FilmActor, Holder, Factory, MetaData, Content, QueryElement> newQueryExpressionBuilder(
			FilmActor.Query faq) {
		EntityQueryExpressionBuilder<Attribute, Reference, Type, FilmActor, Holder, Factory, MetaData, Content, QueryElement> qeb = new EntityQueryExpressionBuilder<>(faq);
		return qeb;
	}


    public void testQuery2() throws Exception {
    	Connection c = getConnection();
    	assertNotNull(c);
    	logger().debug(c.getMetaData().getURL());

    	Implementation<?> imp = implementation();

//    	LiteralCatalog cc = LiteralCatalog.getInstance();
    	// PublicFactory pf = new PublicFactoryImpl();

    	Film film = newEntity(Film.Type.TYPE);

    	Category category = newEntity(Category.Type.TYPE);
    	category.getContent().setName("project name");

    	Actor actor = newEntity(Actor.Type.TYPE);
    	Actor.Content ac = actor.getContent();
    	ac.firstName().set("Emilio");
    	ac.lastName().set("Bullock");

    	FilmCategory fc = newEntity(FilmCategory.Type.TYPE);
    	fc.setFilm(FilmCategory.FILM, film.ref());
    	fc.setCategory(FilmCategory.CATEGORY, category.ref());

    	FilmActor.QueryElement.Builder qeb = new FilmActor.QueryElement.Builder();

    	FilmActor.QueryElement faq = qeb.newQueryElement();

    	FilmActor.Query qo = new FilmActor.Query(faq);



    	EntityQueryExpressionBuilder<Attribute, Reference, Type, FilmActor, Holder, Factory, MetaData, Content, QueryElement> qxb =
    			newQueryExpressionBuilder(qo);

    	String qs = qxb.getQueryExpression().generate();

    	logger().info("testThis: qs=" + qs);

    	Statement st = c.createStatement();
    	UnificationContext ic = new SimpleUnificationContext();
    	ValueExtractorFactory vef = imp.getValueExtractorFactory();

    	EntityReader<FilmActor.Attribute, FilmActor.Reference, FilmActor.Type, FilmActor, FilmActor.Holder, FilmActor.Factory, FilmActor.MetaData, FilmActor.Content, FilmActor.QueryElement> eb
    		= new EntityReader<FilmActor.Attribute, FilmActor.Reference, FilmActor.Type, FilmActor, FilmActor.Holder, FilmActor.Factory, FilmActor.MetaData, FilmActor.Content, FilmActor.QueryElement>(vef, qxb, ic);

    	StatementExecutor se = new StatementExecutor(getPersistenceContext());

    	SelectStatement ss = new SelectStatement(qxb.getQueryExpression());
    	se.execute(ss, c, eb);

    	logger().info("testConstructor: eb.getContent().size()=" + eb.getContent().size());

    	st.close();

    	for (EntityDataObject<FilmActor> o : eb.getContent()) {
			logger().info("testConstructor: rpt=" + o);
			FilmActor root = o.getRoot();
			logger().debug("testConstructor: root=" + root);

			Actor.Holder ah = root.getActor(FilmActor.ACTOR);
			assertNotNull(ah);
			assertFalse(ah.isNull());

			Film.Holder fh = root.getFilm(FilmActor.FILM);
			assertNotNull(fh);
			assertFalse(ah.isNull());


			Actor.Content acc = ah.value().getContent();
			assertNotNull(acc);
			assertNotNull(acc.firstName());


			// Project.Holder ph = root.getProject(HourReport.FK_HHR_PROJECT);
//			logger().info("testConstructor: rpt.project=" + ph);
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
