package com.appspot.relaxe.pg.pagila;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.TestContext;
import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.feature.MetaDataGenerator;
import com.appspot.relaxe.feature.SQLGenerationResult;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.SchemaElementMap;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.rdbms.pg.PGImplementation;

public class PagilaMetaDataGeneratorTest
	extends AbstractPagilaEntityTestCase {
	
	
	private static Logger logger = LoggerFactory.getLogger(PagilaMetaDataGeneratorTest.class);
	
	public void testMetaDataGeneration() throws Exception {
		
			
		executeUpdate("drop schema if exists meta cascade");
		TestContext<PGImplementation> tc = getCurrent();
		
		{		
			MetaDataGenerator mgen = new MetaDataGenerator();				
			
			Catalog cat = tc.getCatalog();
			
			PersistenceContext<PGImplementation> pc = tc.getPersistenceContext();
			DataTypeMap tm = pc.getDataTypeMap();		
			SQLGenerationResult gr = mgen.modify(cat, tm);		
			List<Statement> stmts = gr.statements();
			
			logger.info("stmts.size(): {}", stmts.size());
			
			List<String> sl = new ArrayList<String>();
						
			for (Statement stmt : stmts) {										
				sl.add(stmt.generate());
			}
			
			executeAll(sl);
		}
		
		{		
			Catalog cat = tc.newCatalog();
						
			Environment env = cat.getEnvironment();
						
			Schema schema = cat.schemas().get(MetaDataGenerator.SCHEMA_NAME);
			
			assertNotNull(schema);
			
			SchemaElementMap<BaseTable> btm = schema.baseTables();
			
			MetaDataGenerator mgen = new MetaDataGenerator();
						
			for (MetaDataGenerator.TableName n : MetaDataGenerator.TableName.values()) {				
				Identifier tn = mgen.toIdentifier(env, n);				
				final BaseTable t = btm.get(tn);
				assertNotNull("Table not found " + n, t);	
			}			
				
			
		}
		
		
	}
	

}
