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
import com.appspot.relaxe.TestContext;
import com.appspot.relaxe.ent.value.HasVarcharArray;
import com.appspot.relaxe.ent.value.HasVarcharArrayKey;
import com.appspot.relaxe.ent.value.VarcharArrayAccessor;
import com.appspot.relaxe.ent.value.VarcharArrayKey;
import com.appspot.relaxe.rdbms.CatalogFactory;
import com.appspot.relaxe.rdbms.DefaultDataAccessContext;
import com.appspot.relaxe.rdbms.hsqldb.HSQLDBImplementation;
import com.appspot.relaxe.rdbms.hsqldb.HSQLDBPersistenceContext;
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
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.VarcharTypeDefinition;
import com.appspot.relaxe.gen.pg.pp.ent.pub.DataTypeTest;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.env.IdentifierRules;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.env.hsqldb.HSQLDBEnvironment;
import com.appspot.relaxe.rdbms.pg.PGImplementation;
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

public class PagilaExportDDLTest
	extends AbstractPagilaTestCase {

	private static Logger logger = LoggerFactory.getLogger(PagilaExportDDLTest.class);

	public void testTransform() throws Exception {

		TestContext<PGImplementation> ctx = getCurrent();
		Catalog cat = ctx.getCatalog();

		final HSQLDBImplementation hi = new HSQLDBImplementation.File();
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

		addCreateTableStatements(dtm, sc, sl);
		addPrimaryKeyStatements(sc, sl);
		addForeignKeyStatements(sc, sl);

		StringBuilder buf = new StringBuilder();

		for (Statement st : sl) {
			write(buf, st);
		}

		String url = hi.createJdbcUrl("testdata/hsql");
		DataAccessContext hctx = new DefaultDataAccessContext<HSQLDBImplementation>(hpc, url, null);

		DataAccessSession das = hctx.newSession();
		StatementSession ss = das.asStatementSession();

		QueryProcessor qp = new QueryProcessorAdapter();
		IdentifierRules hid = hi.getEnvironment().getIdentifierRules();
		createDomains(ss, qp, hid);

		for (Statement st : sl) {
			logger.debug("executing: " + st.generate());
			ss.execute(st, qp);
			logger.debug("executed: " + st.generate());
		}

		das.commit();

		CatalogFactory hcf = hi.catalogFactory();
		Connection c = null;

		try {
			c = DriverManager.getConnection(url);

			long s = System.currentTimeMillis();
			Catalog hcat = hcf.create(c);
			long e = System.currentTimeMillis();

			logger.info("hcat: " + hcat + " : " + (e - s));

			SourceGenerator gen = new SourceGenerator(new File("hsqldb/pagila/src/out"));

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
			c = close(c);
		}







		logger.debug("shutdown: " + Shutdown.STATEMENT.generate());
		ss.execute(Shutdown.STATEMENT, qp);

		das.close();
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
