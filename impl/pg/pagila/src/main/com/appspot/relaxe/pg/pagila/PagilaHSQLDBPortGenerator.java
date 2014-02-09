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
package com.appspot.relaxe.pg.pagila;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.env.IdentifierRules;
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
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rdbms.DefaultResolver;
import com.appspot.relaxe.rdbms.Implementation;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.service.DataAccessContext;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.StatementSession;
import com.appspot.relaxe.tools.CatalogTool;
import com.appspot.relaxe.tools.ToolConfigurationException;
import com.appspot.relaxe.tools.ToolException;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.cli.CommandLine;
import com.appspot.relaxe.cli.Option;
import com.appspot.relaxe.cli.Parameter;
import com.appspot.relaxe.cli.SimpleOption;
import com.appspot.relaxe.io.IOHelper;

public class PagilaHSQLDBPortGenerator
	extends CatalogTool {	
	private static Logger logger = LoggerFactory.getLogger(PagilaHSQLDBPortGenerator.class);
	
//	private String dataDir;	

//    public static final Option OPTION_DESTINATION_CONTEXT = 
//            new SimpleOption("destination-persistence-context", "d", new Parameter(false),
//                "Fully qualified name of the class which implements '" + 
//                PersistenceContext.class.getName() + "'. " +
//               "Implementation must have a no-arg public constructor.");
    
//    public static final Option OPTION_JDBC_URL = 
//            new SimpleOption("jdbc-url", "u", new Parameter(false), "JDBC Driver URL of the source database");
        
//    public static final Option OPTION_JDBC_CONFIG = 
//            new SimpleOption("jdbc-driver-config", "j", 
//                new Parameter(false), "JDBC Driver configuration for the source database connection as file path to Java properties file");
    
    public static final Option OPTION_DESTINATION_JDBC_URL = 
            new SimpleOption("destination-jdbc-url", "d", 
            		new Parameter(false), "JDBC Driver URL of the destination database");

    public static final Option OPTION_DESTINATION_JDBC_CONFIG = 
            new SimpleOption("destination-jdbc-driver-config", null, 
                new Parameter(false), "JDBC Driver configuration for the destination database connection as file path to Java properties file");

    public static final Option OPTION_DESTINATION_PERSISTENCE_CONTEXT = 
            new SimpleOption("destination-persistence-context", "x", new Parameter(false),
                "Fully qualified name of the class which implements '" + 
                PersistenceContext.class.getName() + "'. " +
               "Implementation must have a no-arg public constructor.");

    
	public PagilaHSQLDBPortGenerator() {		
		super(CatalogTool.OPTION_ENV, 
			  CatalogTool.OPTION_HELP,			  
			  CatalogTool.OPTION_JDBC_URL,
			  CatalogTool.OPTION_JDBC_CONFIG,			  
			  PagilaHSQLDBPortGenerator.OPTION_DESTINATION_JDBC_URL,
			  PagilaHSQLDBPortGenerator.OPTION_DESTINATION_JDBC_CONFIG,
			  PagilaHSQLDBPortGenerator.OPTION_DESTINATION_PERSISTENCE_CONTEXT);
	}
	
	
	private PersistenceContext<?> destinationPersistenceContext;
	private String destinationJdbcUrl;
	private Properties destinationJdbcConfig;
	
	public PagilaHSQLDBPortGenerator(
			String jdbcUrl, Properties jdbcConfig, 
			PersistenceContext<?> destinationPersistenceContext, 
			String destinationJdbcUrl, Properties destinationJdbcConfig) {
		super();
		setJdbcURL(jdbcUrl);
		setJdbcConfig(jdbcConfig);
		this.destinationJdbcUrl = destinationJdbcUrl;
		this.destinationPersistenceContext = destinationPersistenceContext;
		this.destinationJdbcConfig = destinationJdbcConfig;
	}
		
	
	@Override
	protected void init(CommandLine cl) throws ToolConfigurationException, ToolException {
		super.init(cl);
		
		try {		
			this.destinationJdbcUrl = cl.value(require(cl, OPTION_DESTINATION_JDBC_URL));
			
			String destConfigPath = cl.value(OPTION_DESTINATION_JDBC_CONFIG);					
			this.destinationJdbcConfig = (destConfigPath == null) ? null : IOHelper.doLoad(destConfigPath);
			
			initPersistenceContext(cl);
		}
        catch (IOException e) {
            e.printStackTrace();
            throw new ToolException(e.getMessage(), e);
        }
	}


	private void initPersistenceContext(CommandLine cl)
			throws ToolConfigurationException {
		String jdbcURL = getJdbcURL();
		String pctype = cl.value(PagilaHSQLDBPortGenerator.OPTION_DESTINATION_PERSISTENCE_CONTEXT);		
		
		if (pctype == null) {			
			DefaultResolver dr = new DefaultResolver();
			this.destinationPersistenceContext = dr.resolveDefaultContext(jdbcURL);
			
			if (destinationPersistenceContext == null) {
				throw new ToolConfigurationException("Unable to resolve default persistence context by JDBC URL: " + jdbcURL);
			}
		}
		else {
			try {
				this.destinationPersistenceContext = (PersistenceContext<?>) instantiate(pctype);
			} 
			catch (Exception e) {
				throw new ToolConfigurationException(e.getMessage(), e);
			}									
		}
	}	
	

	public static void main(String[] args) {
		System.exit(new PagilaHSQLDBPortGenerator().run(args));
	}	
	
	@Override
	protected void run() throws ToolException {		
	
		try {			
			transform(getCatalog());
		} 
		catch (Exception e) {
			throw new ToolException(e.getMessage(), e);
		}	
	}		
	
	public void transform(Catalog cat) throws Exception {
								
								
		final Implementation<?> di = this.destinationPersistenceContext.getImplementation();
		String tdc = di.defaultDriverClassName();
						
		if (tdc != null) {
			Class.forName(tdc);		
		}		
		
							
		Environment henv = di.environment();
		
		final DataTypeMap htm = henv.getDataTypeMap();
						
		final DataTypeMap dtm = new DataTypeMap() {			
			@Override
			public ValueType<?> getType(DataType type) {
				return htm.getType(type);
			}
			
			@Override
			public SQLTypeDefinition getSQLTypeDefinition(DataType dataType) {
				SQLTypeDefinition def = htm.getSQLTypeDefinition(dataType);
				
				if (def == null) {
					int t = dataType.getDataType();
					
					logger.debug("unmapped: " + dataType.getTypeName() + ": " + dataType.getDataType());
					
					if (t == ValueType.ARRAY && dataType.getTypeName().equals("_text")) {
						def = di.getSyntax().newArrayTypeDefinition(VarcharTypeDefinition.get(1024));
					}
					
					if (t == ValueType.BINARY && dataType.getTypeName().equals("bytea")) {												
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
			String name = s.getUnqualifiedName().getContent();
			
			if (name.equalsIgnoreCase("pg_catalog") || name.equalsIgnoreCase("information_schema")) {
				continue;
			}
			
			sc.add(s);
			
			// A new HSQLDB database contains a 'public' schema initially.  
			if ("public".equalsIgnoreCase(s.getUnqualifiedName().getContent())) {
				continue;
			}
			
			CreateSchema def = new CreateSchema(s.getUnqualifiedName());
			sl.add(def);
		}
		
		 
		
			
		addCreateTableStatements(dtm, sc, sl);
		addPrimaryKeyStatements(sc, sl);
				
//		sl.addAll(addfks);		

		StringBuilder buf = new StringBuilder();
		
		for (Statement st : sl) {
			write(buf, st);		
		}			
				
//		logger().info("target data-dir: {}", dataDir);
		
																
		// String url = hi.createJdbcUrl(dataDir);
		String url = getDestinationJdbcUrl();
		
		logger().info("target url: {}", url);
		
		PersistenceContext<?> destctx = this.destinationPersistenceContext;
		
		DataAccessContext hctx = destctx.newDataAccessContext(getDestinationJdbcUrl(), getDestinationJdbcConfig());
							
										
		DataAccessSession das = hctx.newSession();				
		StatementSession ss = das.asStatementSession();
								
		QueryProcessor qp = new QueryProcessorAdapter();
		IdentifierRules hid = destctx.getImplementation().environment().getIdentifierRules();	
		createDomains(ss, qp, hid);		
		
		executeAll(ss, qp, sl);
		
		das.commit();
		
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
		
		List<Statement> addfks = new ArrayList<Statement>();
		addForeignKeyStatements(sc, addfks);
		
			
		logger().info("add foreign keys...");
		executeAll(ss, qp, addfks);		
		das.commit();		

				
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
	
    protected Object instantiate(String typename) throws Exception {
		Class<?> clazz = Class.forName(typename);		
		return clazz.newInstance();
	}

	
	public Logger logger() {
		return logger;
	}
	
	public String getDestinationJdbcUrl() {
		return destinationJdbcUrl;
	}
	
	private Properties getDestinationJdbcConfig() {
		return destinationJdbcConfig;
	}
}
