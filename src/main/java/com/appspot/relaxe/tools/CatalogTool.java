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
package com.appspot.relaxe.tools;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.appspot.relaxe.QueryHelper;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rdbms.CatalogFactory;
import com.appspot.relaxe.rdbms.DefaultResolver;
import com.appspot.relaxe.rdbms.Implementation;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.cli.Parameter;
import com.appspot.relaxe.cli.CommandLine;
import com.appspot.relaxe.cli.Option;
import com.appspot.relaxe.cli.Parser;
import com.appspot.relaxe.cli.SimpleOption;
import com.appspot.relaxe.io.IOHelper;

public abstract class CatalogTool {
        
    public static final Option OPTION_HELP = 
        new SimpleOption("help", "h", null, "Shows this help");
        
    public static final Option OPTION_VERSION = 
        new SimpleOption("version", null, null, "Shows information about the program version.");  
        
    public static final Option OPTION_VERBOSE = 
        new SimpleOption("verbose", "v", "Output more information");
    
    public static final Option OPTION_ENV = 
        new SimpleOption("environment-type", "e", new Parameter(false),
            "Fully qualified name of the class which implements '" + 
            Implementation.class.getName() + "'. " +
           "Implementation must have a no-arg public constructor.");

    
    public static final Option OPTION_PERSISTENCE_CONTEXT = 
            new SimpleOption("persistence-context-type", "p", new Parameter(false),
                "Fully qualified name of the class which implements '" + 
                PersistenceContext.class.getName() + "'. " +
               "Implementation must have a no-arg public constructor.");
    
    public static final Option OPTION_JDBC_URL = 
        new SimpleOption("jdbc-url", "u", new Parameter(false));
    
    public static final Option OPTION_JDBC_CONFIG = 
        new SimpleOption("jdbc-driver-config", "j", 
            new Parameter(false), "JDBC Driver configuration as file path to Java properties file");
    
    public static final Option OPTION_SCHEMA = 
        new SimpleOption("schema", "s", new Parameter(false), "Process this schema only.");  

    public static final Option OPTION_TABLE = 
        new SimpleOption("table", "t", new Parameter(false), "Process this table only.");  
    
    private Connection connection;
    private Catalog catalog;
    private boolean verbose;
        
    // private Implementation<?> implementation;
    private PersistenceContext<?> persistenceContext;
    
    private String jdbcURL;
    private Properties jdbcConfig;
    
    private List<Option> options;    
        
    public CatalogTool() {
        this(CatalogTool.OPTION_HELP, 
                CatalogTool.OPTION_ENV,
                CatalogTool.OPTION_JDBC_URL,
                CatalogTool.OPTION_JDBC_CONFIG,
                CatalogTool.OPTION_SCHEMA);
    }
    
    public CatalogTool(Option... opts) {
        if (opts == null || opts.length == 0) {
            this.options = Collections.emptyList();
        }
        else {                        
            this.options = new ArrayList<Option>(opts.length);
            
            for (Option o : opts) {
                if (o != null) {
                    this.options.add(o);
                }
            }        
        }        
    }

    public Catalog getCatalog() {
        return catalog;
    }

    protected void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
    
    protected void run()
        throws ToolException {        
    }

    protected int run(String[] args) {
        int result = -1;        
        Parser p = new Parser();        
        Connection c = null;
        
        try {
            prepare(p);
            CommandLine cl = p.parse(args);            
            init(cl);
            run();            
            result = 0;
        } 
        catch (ToolConfigurationException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            
            System.err.println();
            System.err.println(p.usage(getClass()));
            
            result = e.getExitCode();
        } 
        catch (ToolException e) {
            result = e.getExitCode();
            
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            e.printStackTrace();
        } 
        finally {
            finish();
            c = QueryHelper.doClose(c);            
        }
        
        return result;
    }

    protected void finish() {        
    }
        
    protected void prepare(Parser p) {                
        if (this.options != null) {
            for (Option o : this.options) {
                addOption(p, o);
            }
        }
        
        String ver = getVersion();
                        
        if (ver != null) {            
            addOption(p, OPTION_VERSION);
        }        
    }
    
    protected void addOption(Parser p, Option opt) {
//        if (!p.contains(opt)) {
    	p.addOption(opt);
//        }   
    }
    
    /**
     * 
     * @param cl
     * @throws ToolConfigurationException
     * @throws ToolException
     */
    protected void init(CommandLine cl)
        throws ToolConfigurationException, ToolException {
        try {            
            if (cl.has(OPTION_VERSION)) {
                System.err.println(getVersion());
            }
            
            if (cl.has(OPTION_HELP)) {
                throw new ToolConfigurationException("You need help.");            
            }            
            
            setVerbose(cl.has(OPTION_VERBOSE));                        
            
            String jdbcURL = cl.value(require(cl, OPTION_JDBC_URL));
            
            String pctype = cl.value(OPTION_PERSISTENCE_CONTEXT);
            String impltype = cl.value(OPTION_ENV);
                        
            PersistenceContext<?> pctx = null;
            
            if (pctype != null) {                
                Class<?> ctxtype = Class.forName(pctype);
                pctx = (PersistenceContext<?>) ctxtype.newInstance();                
            }
            
            Implementation<?> env = (pctx == null) ? null : pctx.getImplementation();
            
            if (env == null) {                        	            	            
	            if (impltype != null) {
	                Class<?> environmentType = Class.forName(impltype);
	                env = (Implementation<?>) environmentType.newInstance();                
	            }
            }
            
            
            if (env == null) {
            	DefaultResolver ir = new DefaultResolver();
           	 	env = ir.resolve(jdbcURL);

	            if (env == null) {
	                throw new ToolConfigurationException(
	                        "Implementation is not set and " +
	                        "can not be determined from jdbc-url: " + jdbcURL);
	            }
            }
            
            

            
            

            String jdbcDriverConfigPath = cl.value(require(cl, OPTION_JDBC_CONFIG));            
            Properties jdbcConfig = IOHelper.doLoad(jdbcDriverConfigPath);
            
                        
            pctx = (pctx == null) ? env.newDefaultPersistenceContext() : pctx;            
            
            init(jdbcURL, jdbcConfig, pctx);
            
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ToolException(e.getMessage(), e);
        } 
        catch (IOException e) {
            e.printStackTrace();
            throw new ToolException(e.getMessage(), e);
        } 
        catch (InstantiationException e) {
            e.printStackTrace();
            throw new ToolException(e.getMessage(), e);
        } 
        catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new ToolException(e.getMessage(), e);
        }
     }
    
     public void init(String jdbcURL, Properties jdbcConfig, PersistenceContext<?> pctx)
         throws ToolConfigurationException, ToolException {

         try {
             if (jdbcURL == null) {
                throw new NullPointerException("'jdbcURL' must not be null");
             }
             
             if (jdbcConfig == null) {
                throw new NullPointerException("'jdbcConfig' must not be null");
             }
             
             if (pctx == null) {
				throw new NullPointerException("pctx");
			}
                                       
//            	 
//                 if (jdbcURL.toLowerCase().startsWith("jdbc:postgresql:")) {
//                     implementationTypeName = PGImplementation.class.getName();
//                 }
//                 
//                 if (jdbcURL.toLowerCase().startsWith("jdbc:mysql:")) {
//                     implementationTypeName = MySQLImplementation.class.getName();
//                 }
                 
//                 if (implementationTypeName == null) {
//                     throw new ToolConfigurationException(
//                             "Environment-type is not set and " +
//                             "can not be determined from jdbc-url: " + jdbcURL);
//                 }  
//                 
//                 Class<?> implementationType = Class.forName(implementationTypeName);
//                 Object io = implementationType.newInstance();                 
//                 impl = (Implementation<?>) io;               
//             }
             
             setJdbcURL(jdbcURL);
             setJdbcConfig(jdbcConfig);
             setPersistenceContext(pctx);
             
             Implementation<?> impl = pctx.getImplementation();
                                                  
             Class.forName(impl.defaultDriverClassName());  
             Connection c = createConnection();
             
             CatalogFactory cf = impl.catalogFactory();
             final Catalog cat = cf.create(c);
             setConnection(c);
             setCatalog(cat);            
         }
         catch (ClassNotFoundException e) {
             e.printStackTrace();
             throw new ToolException(e.getMessage(), e);
         } 
//         catch (InstantiationException e) {
//             e.printStackTrace();
//             throw new ToolException(e.getMessage(), e);
//         } 
//         catch (IllegalAccessException e) {
//             e.printStackTrace();
//             throw new ToolException(e.getMessage(), e);
//         } 
         catch (SQLException e) {
             e.printStackTrace();
             throw new ToolException(e.getMessage(), e);
         } 
         catch (QueryException e) {
             e.printStackTrace();
             throw new ToolException(e.getMessage(), e);
         }
     }

    
    
    
    protected Option require(CommandLine cl, Option required) 
        throws ToolConfigurationException {
        if (!cl.has(required)) {
            throw new ToolConfigurationException("option " + required.name() + " is required");
        }
        return required;
        
    }

    public Connection getConnection() {
        return connection;
    }

    protected void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
    protected void message(String msg) {
        if (isVerbose()) {
            System.out.println(msg);
        }
    }    
    
    public int getVersionMajor() {
        return 0;
    }
    
    public int getVersionMinor() {
        return 3;
    }
    
    public String getVersion() {
        return getClass().getName() + " " + getVersionMajor() + "." + getVersionMinor();
    }

    public String getJdbcURL() {
        return jdbcURL;
    }

    public void setJdbcURL(String jdbcURL) {
        this.jdbcURL = jdbcURL;
    }

    public Properties getJdbcConfig() {
        return jdbcConfig;
    }

    public void setJdbcConfig(Properties jdbcConfig) {
        if (this.jdbcConfig == null) {
            this.jdbcConfig = new Properties();
        }
        
        this.jdbcConfig = jdbcConfig;
    }
        
    private Connection createConnection() 
        throws SQLException, ClassNotFoundException {
           
        String jdbcURL = getJdbcURL();
        Properties jdbcConfig = getJdbcConfig();                                
        // Class.forName(getImplementation().driverClassName());               
        
        Connection c = DriverManager.getConnection(jdbcURL, jdbcConfig);
        return c;
    }
    
    public Implementation<?> getImplementation() {
    	return getPersistenceContext().getImplementation();
    }

    public PersistenceContext<?> getPersistenceContext() {
        return this.persistenceContext;
    }

    public void setPersistenceContext(PersistenceContext<?> persistenceContext) {
    	if (persistenceContext == null) {
			throw new NullPointerException();
		}
    	
        this.persistenceContext = persistenceContext;
    }
    


    
    
}
