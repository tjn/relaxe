/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;

import com.appspot.relaxe.TestContext;
import com.appspot.relaxe.env.DefaultDataAccessContext;
import com.appspot.relaxe.env.hsqldb.HSQLDBImplementation;
import com.appspot.relaxe.env.hsqldb.HSQLDBPersistenceContext;
import com.appspot.relaxe.env.hsqldb.expr.Shutdown;
import com.appspot.relaxe.exec.QueryProcessor;
import com.appspot.relaxe.exec.QueryProcessorAdapter;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.ddl.AlterTableAddForeignKey;
import com.appspot.relaxe.expr.ddl.AlterTableAddPrimaryKey;
import com.appspot.relaxe.expr.ddl.CreateDomain;
import com.appspot.relaxe.expr.ddl.CreateSchema;
import com.appspot.relaxe.expr.ddl.CreateTable;
import com.appspot.relaxe.expr.ddl.types.AbstractCharacterTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.IntTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.VarBinaryTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLArrayTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.VarcharTypeDefinition;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.DataTypeTest;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.IdentifierRules;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.impl.hsqldb.HSQLDBEnvironment;
import com.appspot.relaxe.meta.impl.pg.PGImplementation;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
import com.appspot.relaxe.service.DataAccessContext;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.StatementSession;
import com.appspot.relaxe.types.PrimitiveType;

public class PagilaExportDDLTest 
	extends AbstractPagilaTestCase {
	
	private static Logger logger = Logger.getLogger(PagilaExportDDLTest.class);	
	
	private void line(StringBuilder buf, int eols, String ... elems) {
		for (int i = 0; i < elems.length; i++) {
			buf.append(elems[i]);
		}
		
		for (int i = 0; i < eols; i++) {
			buf.append("\n");	
		}		
	}
	
	public void testTransform() throws Exception {
		
		TestContext<PGImplementation> ctx = getCurrent();
		Catalog cat = ctx.getCatalog();
		
		HSQLDBImplementation hi = new HSQLDBImplementation();
		HSQLDBPersistenceContext hpc = new HSQLDBPersistenceContext(hi);		
		HSQLDBEnvironment henv = hi.getEnvironment();
		final DataTypeMap htm = henv.getDataTypeMap();
		final DataTypeMap dtm = new DataTypeMap() {			
			@Override
			public PrimitiveType<?> getType(DataType type) {
				return htm.getType(type);
			}
			
			@Override
			public SQLTypeDefinition getSQLTypeDefinition(DataType dataType) {
				SQLTypeDefinition def = htm.getSQLTypeDefinition(dataType);
				
				if (def == null) {
					int t = dataType.getDataType();
					
					logger.debug("unmapped: " + dataType.getTypeName() + ": " + dataType.getDataType());
					
					if (t == PrimitiveType.ARRAY && dataType.getTypeName().equals("_text")) {
						def = new SQLArrayTypeDefinition(VarcharTypeDefinition.get(null));
					}
					
					if (t == PrimitiveType.BINARY && dataType.getTypeName().equals("bytea")) {
						def = new SQLArrayTypeDefinition(VarBinaryTypeDefinition.get());
					}
					
					if (SQLTypeDefinition.isBinaryType(t)) {
						def = VarBinaryTypeDefinition.get(dataType.getSize());
					}
				}
				
				return def;
			}
		};
		
		{
			DataTypeTest.MetaData tm = DataTypeTest.Type.TYPE.getMetaData();
						
			Column col = tm.getColumn(DataTypeTest.Attribute.CV);			
			DataType t = col.getDataType();
			
			SQLTypeDefinition def = dtm.getSQLTypeDefinition(t);
			AbstractCharacterTypeDefinition cd = (AbstractCharacterTypeDefinition) def;
			IntLiteral len = cd.getLength();
			assertNotNull(len);
		}
		
		
				
		Collection<Schema> sc = new ArrayList<Schema>(); 
		
		List<Statement> sl = new ArrayList<Statement>(); 
		
		for (Schema s : cat.schemas().values()) {
			String name = s.getUnqualifiedName().getName();
			
			if (name.equalsIgnoreCase("pg_catalog") || name.equalsIgnoreCase("information_schema")) {
				continue;
			}
			
			sc.add(s);
			
			if ("public".equalsIgnoreCase(s.getUnqualifiedName().getName())) {
				continue;
			}
			
			CreateSchema def = new CreateSchema(s.getUnqualifiedName());
			sl.add(def);
		}
			
		for (Schema s : sc) {
			for (BaseTable t : s.baseTables().values()) {
				CreateTable def = new CreateTable(dtm, t, true, false);
				sl.add(def);
			}
		}
		
		for (Schema s : sc) {
			for (PrimaryKey pk : s.primaryKeys().values()) {
				AlterTableAddPrimaryKey def = new AlterTableAddPrimaryKey(pk);
				sl.add(def);				
			}
		}
		
		for (Schema s : sc) {
			Collection<ForeignKey> fks = s.foreignKeys().values();
			
			for (ForeignKey fk : fks) {
				AlterTableAddForeignKey def = new AlterTableAddForeignKey(fk);				
				sl.add(def);
			}
		}		

		StringBuilder buf = new StringBuilder();
		
		for (Statement st : sl) {
			write(buf, st);		
		}
			
								
		// String url = hi.createJdbcUrl("mem:test");
		String url = hi.createJdbcUrl("file:testdata/hsql");
		DataAccessContext hctx = new DefaultDataAccessContext<HSQLDBImplementation>(hpc, url, null);
						
		DataAccessSession das = hctx.newSession();		
		StatementSession ss = das.asStatementSession();
		
//		{
//			logger.debug("dropping public schema");
//			Identifier p = hi.getEnvironment().getIdentifierRules().toDelimitedIdentifier("PUBLIC");
//			DropSchema ds = new DropSchema(p, true);
//			ss.execute(ds);
//			logger.debug("dropped");
//		}
						
		QueryProcessor qp = new QueryProcessorAdapter();
		
		
		IdentifierRules hid = hi.getEnvironment().getIdentifierRules();
	
		{
			CreateDomain cd = new CreateDomain(hid.newName("year"), IntTypeDefinition.DEFINITION);
			ss.execute(cd, qp);
		}
		
		{	
			CreateDomain cd = new CreateDomain(hid.newName("mpaa_rating"), VarcharTypeDefinition.get(20));
			ss.execute(cd, qp);
		}
	
		try {		
			for (Statement st : sl) {
				logger.debug("executing: " + st.generate());
				ss.execute(st, qp);
				logger.debug("executed: " + st.generate());
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		das.commit();
		
		logger.debug("shutdown: " + Shutdown.STATEMENT.generate());
		ss.execute(Shutdown.STATEMENT, qp);		
					
		das.close();
		
		// logger().debug(buf.toString());
	}
	
	protected void write(StringBuilder buf, Statement st) {
		line(buf, 1, st.generate());
		line(buf, 2, ";");		
	}
}
