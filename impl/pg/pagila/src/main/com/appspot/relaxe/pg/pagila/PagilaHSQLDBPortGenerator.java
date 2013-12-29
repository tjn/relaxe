/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
package com.appspot.relaxe.pg.pagila;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.DefaultTableMapper;
import com.appspot.relaxe.DefaultTypeMapper;
import com.appspot.relaxe.QueryHelper;
import com.appspot.relaxe.TestContext;
import com.appspot.relaxe.ent.value.HasVarcharArray;
import com.appspot.relaxe.ent.value.HasVarcharArrayKey;
import com.appspot.relaxe.ent.value.VarcharArrayAccessor;
import com.appspot.relaxe.ent.value.VarcharArrayKey;
import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.DefaultDataAccessContext;
import com.appspot.relaxe.env.hsqldb.AbstractHSQLDBImplementation;
import com.appspot.relaxe.env.hsqldb.HSQLDBFileImplementation;
import com.appspot.relaxe.env.hsqldb.HSQLDBImplementation;
import com.appspot.relaxe.env.hsqldb.HSQLDBPersistenceContext;
import com.appspot.relaxe.env.hsqldb.expr.Shutdown;
import com.appspot.relaxe.exec.QueryProcessor;
import com.appspot.relaxe.exec.QueryProcessorAdapter;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.ddl.AlterTableAddForeignKey;
import com.appspot.relaxe.expr.ddl.AlterTableAddPrimaryKey;
import com.appspot.relaxe.expr.ddl.AlterTableDropConstraint;
import com.appspot.relaxe.expr.ddl.CreateDomain;
import com.appspot.relaxe.expr.ddl.CreateSchema;
import com.appspot.relaxe.expr.ddl.CreateTable;
import com.appspot.relaxe.expr.ddl.types.IntTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.VarBinaryTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.VarcharTypeDefinition;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.IdentifierRules;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.impl.hsqldb.HSQLDBEnvironment;
import com.appspot.relaxe.meta.impl.hsqldb.HSQLDBTest;
import com.appspot.relaxe.meta.impl.pg.PGImplementation;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rpc.StringArray;
import com.appspot.relaxe.rpc.VarcharArrayHolder;
import com.appspot.relaxe.service.DataAccessContext;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.StatementSession;
import com.appspot.relaxe.source.DefaultAttributeInfo;
import com.appspot.relaxe.source.SourceGenerator;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.VarcharArrayType;

public class PagilaHSQLDBPortGenerator
	extends AbstractPagilaTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(PagilaHSQLDBPortGenerator.class);
	
	private String dataDir;
	
	public PagilaHSQLDBPortGenerator() {		
	}
	
	public PagilaHSQLDBPortGenerator(String dataDir) {
		this.dataDir = dataDir;
	}
	
		
	public static void main(String[] args) {
		try {
			String dd = (args.length == 0) ? null : args[0];					
			PagilaHSQLDBPortGenerator pgen = new PagilaHSQLDBPortGenerator(dd);			
			pgen.setUp();
			
			try {
				pgen.testTransform();		
			}
			finally {
				pgen.tearDown();	
			}
		} 
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}	
	
	
	public void testTransform() throws Exception {
		logger.debug("testTransform - enter");
		
		try {						
			TestContext<PGImplementation> tc = getCurrent();
			Catalog cat = tc.getCatalog();
			transform(cat);
		} 
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		finally {
			logger.debug("testTransform - exit");
		}		
	}
	
	
	public void transform(Catalog cat) throws Exception {
				
		final AbstractHSQLDBImplementation hi = new HSQLDBFileImplementation();
		HSQLDBPersistenceContext hpc = new HSQLDBPersistenceContext(hi);
				
		String tdc = hi.defaultDriverClassName();
		
		HSQLDBTest ht = new HSQLDBTest();
		final String titag = ht.implementationTag();
						
		if (tdc != null) {
			Class.forName(tdc);
		}		
		
		if (this.dataDir == null) {
			this.dataDir = ht.getDatabase();
		}
						
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
						def = hi.getSyntax().newArrayTypeDefinition(VarcharTypeDefinition.get(1024));
					}
					
					if (t == PrimitiveType.BINARY && dataType.getTypeName().equals("bytea")) {												
						def = VarBinaryTypeDefinition.get(dataType.getSize());
					}
					
					if (SQLTypeDefinition.isBinaryType(t)) {
						def = VarBinaryTypeDefinition.get(dataType.getSize());
					}
				}
				
				return def;
			}
		};
						
		Collection<Schema> sc = new ArrayList<Schema>(); 
		
		List<Statement> sl = new ArrayList<Statement>();
			
		for (Schema s : cat.schemas().values()) {
			String name = s.getUnqualifiedName().getName();
			
			if (name.equalsIgnoreCase("pg_catalog") || name.equalsIgnoreCase("information_schema")) {
				continue;
			}
			
			sc.add(s);
			
			// A new HSQLDB database contains a 'public' schema initially.  
			if ("public".equalsIgnoreCase(s.getUnqualifiedName().getName())) {
				continue;
			}
			
			CreateSchema def = new CreateSchema(s.getUnqualifiedName());
			sl.add(def);
		}
		
		List<Statement> addfks = new ArrayList<Statement>(); 
		
			
		addCreateTableStatements(dtm, sc, sl);
		addPrimaryKeyStatements(sc, sl);		
		addForeignKeyStatements(sc, addfks);
		
		sl.addAll(addfks);		

		StringBuilder buf = new StringBuilder();
		
		for (Statement st : sl) {
			write(buf, st);		
		}			
				
		logger().info("target data-dir: {}", dataDir);
														
		String url = hi.createJdbcUrl(dataDir);
		
		logger().info("target url: {}", url);
		
		DataAccessContext hctx = new DefaultDataAccessContext<HSQLDBImplementation>(hpc, url, null);
					
										
		DataAccessSession das = hctx.newSession();				
		StatementSession ss = das.asStatementSession();
								
		QueryProcessor qp = new QueryProcessorAdapter();
		IdentifierRules hid = hi.getEnvironment().getIdentifierRules();	
		createDomains(ss, qp, hid);		
		
		executeAll(ss, qp, sl);
		
		das.commit();
		
		CatalogFactory hcf = hi.catalogFactory();
		Connection c = null;
		Catalog hcat = null;
		
		try {			
			c = DriverManager.getConnection(url);
			
			long s = System.currentTimeMillis();
			hcat = hcf.create(c);
			long e = System.currentTimeMillis();
			
			logger.info("hcat: " + hcat + " : " + (e - s));
			
			SourceGenerator gen = new SourceGenerator(new File("build/impl/" + titag + "/pagila/src/gen"));
			
			DefaultTableMapper tam = new DefaultTableMapper("com.appspot.relaxe.gen.hsqldb.pagila.ent");
			
			DefaultTypeMapper tym = new DefaultTypeMapper() {
				{
					DefaultAttributeInfo info = new DefaultAttributeInfo();
			    	info.setAttributeType(StringArray.class);
		        	info.setHolderType(VarcharArrayHolder.class);
		        	info.setKeyType(VarcharArrayKey.class);
		        	info.setAccessorType(VarcharArrayAccessor.class);
		        	info.setPrimitiveType(VarcharArrayType.TYPE);
		        	info.setContainerType(HasVarcharArray.class);
		        	info.setContainerMetaType(HasVarcharArrayKey.class);
					
					register(PrimitiveType.ARRAY, "VARCHAR(1024) ARRAY", info);
				}
			};
			
			gen.run(hcat, tam, tym, henv);
						
			logger.info("hcat: " + hcat + " : " + (e - s));
		}
		finally {			
			c = QueryHelper.doClose(c);
		}
		
//				
//		logger().info("dropping foreign keys temporarily...");
//		List<Statement> dropfks = new ArrayList<Statement>();
//		dropForeignKeyStatements(sc, dropfks);
//		executeAll(ss, qp, dropfks);		
//		das.commit();						
//		
//		for (Schema s : sc) {
//			SchemaElementMap<BaseTable> tm = s.baseTables();
//			
//			final Schema ts = hcat.schemas().get(s.getUnqualifiedName());
//			List<Identifier> nl = new ArrayList<Identifier>();
//			
//			for (BaseTable src : tm.values()) {
//				BaseTable dest = ts.baseTables().get(src.getUnqualifiedName());
//												
//				nl.clear();
//				
//				for(Column col : src.columnMap().values()) {
//					String n = col.getColumnName().getName();
//					Column tcol = dest.columnMap().get(n);
//					nl.add(tcol.getColumnName());
//				}				
//							
//				ElementList<Identifier> names = new ElementList<Identifier>(nl);
//				
//				List<ValueRow> rows = new ArrayList<ValueRow>();
//								
////				new ElementList<ValuesListElement>(elems);
////				new MutableValueParameter<>(column, null);
//											
//				InsertStatement ins = new InsertStatement(dest, names, rows);
//				ins.generate();
//				
//			}
//			
//		}
//			
//		logger().info("restoring foreign keys...");
//		executeAll(ss, qp, addfks);		
//		das.commit();		

				
		logger.debug("shutdown: " + Shutdown.STATEMENT.generate());
		ss.execute(Shutdown.STATEMENT, qp);		
					
		das.close();
	}

	private void executeAll(StatementSession ss, QueryProcessor qp, List<Statement> statements) throws QueryException {
		for (Statement st : statements) {
			String stmt = st.generate();
			logger.debug("executing: {}", stmt);
			ss.execute(st, qp);
			logger.debug("executed: {}", stmt);
		}
	}

	protected void addCreateTableStatements(final DataTypeMap dtm,
			Collection<Schema> sc, List<Statement> sl) {
		for (Schema s : sc) {
			for (BaseTable t : s.baseTables().values()) {
				CreateTable def = new CreateTable(dtm, t, true, false);
				sl.add(def);
			}
		}
	}

	protected void addPrimaryKeyStatements(Collection<Schema> sc,
			List<Statement> sl) {
		for (Schema s : sc) {
			for (PrimaryKey pk : s.primaryKeys().values()) {
				AlterTableAddPrimaryKey def = new AlterTableAddPrimaryKey(pk);
				sl.add(def);				
			}
		}
	}

	protected void addForeignKeyStatements(Collection<Schema> sc,
			List<Statement> sl) {
		for (Schema s : sc) {
			Collection<ForeignKey> fks = s.foreignKeys().values();
			
			for (ForeignKey fk : fks) {
				AlterTableAddForeignKey def = new AlterTableAddForeignKey(fk);				
				sl.add(def);
			}
		}
	}
	
	protected void dropForeignKeyStatements(Collection<Schema> sc, List<Statement> sl) {
		for (Schema s : sc) {
			Collection<ForeignKey> fks = s.foreignKeys().values();
			
			for (ForeignKey fk : fks) {				
				AlterTableDropConstraint def = new AlterTableDropConstraint(fk);				
				sl.add(def);
			}
		}
	}

	protected void createDomains(StatementSession ss, QueryProcessor qp,
			IdentifierRules hid) throws QueryException {
		{
			CreateDomain cd = new CreateDomain(hid.newName("year"), IntTypeDefinition.DEFINITION);
			ss.execute(cd, qp);
		}
		
		{	
			CreateDomain cd = new CreateDomain(hid.newName("mpaa_rating"), VarcharTypeDefinition.get(20));
			ss.execute(cd, qp);
		}
		
		{	
			CreateDomain cd = new CreateDomain(hid.newName("tsvector"), VarcharTypeDefinition.get(1024));
			ss.execute(cd, qp);
		}
	}
	
	protected void write(StringBuilder buf, Statement st) {
		line(buf, 1, st.generate());
		line(buf, 2, ";");		
	}
	
	private void line(StringBuilder buf, int eols, String ... elems) {
		for (int i = 0; i < elems.length; i++) {
			buf.append(elems[i]);
		}
		
		for (int i = 0; i < eols; i++) {
			buf.append("\n");	
		}		
	}
	
	
}
