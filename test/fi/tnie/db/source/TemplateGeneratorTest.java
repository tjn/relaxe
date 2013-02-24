/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.source;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import fi.tnie.db.AbstractUnitTest;
import fi.tnie.db.TestContext;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.gen.pg.ent.LiteralCatalog;
import fi.tnie.db.query.QueryException;

public abstract class TemplateGeneratorTest<I extends Implementation<I>> extends AbstractUnitTest<I> {
	
	@Override
	protected void init() {
		super.init();
	}
	
	public void testRun() throws SQLException, QueryException, IOException, XMLStreamException, TransformerException, EntityException {
		
//		File tdir = new File("test/war/WEB-INF/templates");
		File tdir = new File("C:/Users/tnie/devel/project/hr/HRUI/war/WEB-INF/templates");
		
					
		LiteralCatalog cat = LiteralCatalog.getInstance();				
		TemplateGenerator g = new TemplateGenerator(tdir, cat);
		
		TestContext<I> tc = getCurrent();
		assertNotNull(tc);
		
		assertNotNull(cat);
				
		NamingPolicy np = new DefaultNamingPolicy();		
		g.run(cat, np);
	}
	
}
