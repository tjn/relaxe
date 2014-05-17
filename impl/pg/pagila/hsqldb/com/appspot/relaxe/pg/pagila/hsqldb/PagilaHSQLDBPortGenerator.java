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
package com.appspot.relaxe.pg.pagila.hsqldb;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.env.IdentifierRules;
import com.appspot.relaxe.env.hsqldb.expr.Shutdown;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.ddl.AlterTableAddForeignKey;
import com.appspot.relaxe.expr.ddl.AlterTableAddPrimaryKey;
import com.appspot.relaxe.expr.ddl.AlterTableDropConstraint;
import com.appspot.relaxe.expr.ddl.CreateDomain;
import com.appspot.relaxe.expr.ddl.CreateSchema;
import com.appspot.relaxe.expr.ddl.CreateTable;
import com.appspot.relaxe.expr.ddl.types.SQLIntType;
import com.appspot.relaxe.expr.ddl.types.SQLVarcharType;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.pg.pagila.Copy;
import com.appspot.relaxe.pg.pagila.PagilaPersistenceContext;
import com.appspot.relaxe.rdbms.CatalogFactory;
import com.appspot.relaxe.rdbms.DefaultResolver;
import com.appspot.relaxe.rdbms.DriverManagerConnectionFactory;
import com.appspot.relaxe.rdbms.Implementation;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.service.DataAccessContext;
import com.appspot.relaxe.service.DataAccessException;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.Receiver;
import com.appspot.relaxe.service.StatementSession;
import com.appspot.relaxe.tools.CatalogTool;
import com.appspot.relaxe.tools.ToolConfigurationException;
import com.appspot.relaxe.tools.ToolException;
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
    
    public static final Option OPTION_COPY_DATA = 
            new SimpleOption("copy-data", null, 
                new Parameter(false), "Copies the data");    

    
	public PagilaHSQLDBPortGenerator() {		
		super(CatalogTool.OPTION_ENV, 
			  CatalogTool.OPTION_HELP,			  
			  CatalogTool.OPTION_JDBC_URL,
			  CatalogTool.OPTION_JDBC_CONFIG,			  
			  PagilaHSQLDBPortGenerator.OPTION_DESTINATION_JDBC_URL,
			  PagilaHSQLDBPortGenerator.OPTION_DESTINATION_JDBC_CONFIG,
			  PagilaHSQLDBPortGenerator.OPTION_DESTINATION_PERSISTENCE_CONTEXT,
			  PagilaHSQLDBPortGenerator.OPTION_COPY_DATA);
	}
	
	
	private PersistenceContext<?> destinationPersistenceContext;
	private String destinationJdbcUrl;
	private Properties destinationJdbcConfig;
	private boolean copyData;
	
	public PagilaHSQLDBPortGenerator(
			String jdbcUrl, Properties jdbcConfig, 
			PersistenceContext<?> destinationPersistenceContext, 
			String destinationJdbcUrl, 
			Properties destinationJdbcConfig, 
			boolean copyData) {
		super();
		setJdbcURL(jdbcUrl);
		setJdbcConfig(jdbcConfig);
		this.destinationJdbcUrl = destinationJdbcUrl;
		this.destinationPersistenceContext = destinationPersistenceContext;
		this.destinationJdbcConfig = destinationJdbcConfig;
		this.copyData = copyData;
	}
		
	
	@Override
	protected void init(CommandLine cl) throws ToolConfigurationException, ToolException {
		super.init(cl);
		
		try {		
			this.destinationJdbcUrl = cl.value(require(cl, OPTION_DESTINATION_JDBC_URL));
			
			String destConfigPath = cl.value(OPTION_DESTINATION_JDBC_CONFIG);					
			this.destinationJdbcConfig = (destConfigPath == null) ? null : IOHelper.doLoad(destConfigPath);
			
			String cs = cl.value(OPTION_COPY_DATA);						
			this.copyData = (cs == null) ? false : Boolean.parseBoolean(cs);
						
			initPersistenceContext(cl);
		}
        catch (IOException e) {
            e.printStackTrace();
            throw new ToolException(e.getMessage(), e);
        }
		catch (ToolConfigurationException e) {
			logger.error(e.getMessage(), e);
			throw e;
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
		
		logger.info("destination-persistent-context: {}", destinationPersistenceContext.getClass());
									
		final DataTypeMap dtm = destinationPersistenceContext.getDataTypeMap();
						
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
		
						
		logger.info("dtm: {}", dtm);
		
			
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
										
		DataAccessSession ddas = hctx.newSession();				
		StatementSession ss = ddas.asStatementSession();		
								
		Receiver receiver = new Receiver() {			
			@Override
			public void updated(Statement statement, int updateCount) {
			}
			
			@Override
			public void received(SelectStatement statement, DataObjectQueryResult<DataObject> result) {
			}
		};
		
		
		IdentifierRules hid = destctx.getImplementation().environment().getIdentifierRules();	
		createDomains(ss, receiver, hid);		
		
		executeAll(ss, receiver, sl);
		
		ddas.commit();
				
		// PagilaPersistenceContext ppc = new PagilaPersistenceContext();
		
		if (this.copyData) {		
			Implementation<?> imp = destctx.getImplementation();
					
			DriverManagerConnectionFactory dcf = new DriverManagerConnectionFactory();
			Connection c = dcf.newConnection(getDestinationJdbcUrl(), getDestinationJdbcConfig());
			
			CatalogFactory cf = imp.catalogFactory();		
			Catalog destcat = cf.create(c);		
					
			PagilaPersistenceContext ppc = new PagilaPersistenceContext();
			DataAccessContext sctx = ppc.newDataAccessContext(getJdbcURL(), getJdbcConfig());
			DataAccessSession sdas = sctx.newSession();		
					
			for (Schema sourceSchema : sc) {
				Schema ds = destcat.schemas().get(sourceSchema.getUnqualifiedName().toString());
				
				if (ds == null) {
					continue;
				}
				
				Copy copy = new Copy(sourceSchema, ds, sdas, ddas);
				copy.run();
				ddas.commit();
			}
		}		
				
		List<Statement> addfks = new ArrayList<Statement>();
		addForeignKeyStatements(sc, addfks);
		
			
		logger().info("add foreign keys...");
		executeAll(ss, receiver, addfks);		
		ddas.commit();		

				
		logger.debug("shutdown: " + Shutdown.STATEMENT.generate());
		ss.execute(Shutdown.STATEMENT, receiver);
					
		ddas.close();
	}

	private void executeAll(StatementSession ss, Receiver receiver, List<Statement> statements) throws DataAccessException {
		for (Statement st : statements) {
			String stmt = st.generate();
			logger.debug("executing: {}", stmt);
			ss.execute(st, receiver);
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

	protected void createDomains(StatementSession ss, Receiver qp,
			IdentifierRules hid) throws DataAccessException {
		{	
			CreateDomain cd = new CreateDomain(hid.toIdentifier("year"), SQLIntType.get());
			ss.execute(cd, qp);
		}
		
		{	
			CreateDomain cd = new CreateDomain(hid.newName("mpaa_rating"), SQLVarcharType.get(20));
			ss.execute(cd, qp);
		}
		
		{	
			CreateDomain cd = new CreateDomain(hid.newName("tsvector"), SQLVarcharType.get(4 * 1024));
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
