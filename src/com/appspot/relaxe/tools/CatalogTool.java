/*
 * Copyright (c) 2009-2013 Topi Nieminen
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
import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.env.mysql.MySQLImplementation;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.impl.pg.PGImplementation;
import com.appspot.relaxe.query.QueryException;

import fi.tnie.util.cli.Parameter;
import fi.tnie.util.cli.CommandLine;
import fi.tnie.util.cli.Option;
import fi.tnie.util.cli.Parser;
import fi.tnie.util.cli.SimpleOption;
import fi.tnie.util.io.IOHelper;

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
        
    private Implementation<?> implementation;
    
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
            String environmentTypeName = cl.value(OPTION_ENV);
            
            Implementation<?> env = null;
            
            if (environmentTypeName != null) {
                Class<?> environmentType = Class.forName(environmentTypeName);
                env = (Implementation<?>) environmentType.newInstance();                
            }

            String jdbcDriverConfigPath = cl.value(require(cl, OPTION_JDBC_CONFIG));            
            Properties jdbcConfig = IOHelper.doLoad(jdbcDriverConfigPath);
            
            init(jdbcURL, jdbcConfig, env);
            
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
    
     public void init(String jdbcURL, Properties jdbcConfig, Implementation<?> impl)
         throws ToolConfigurationException, ToolException {

         try {
             if (jdbcURL == null) {
                throw new NullPointerException("'jdbcURL' must not be null");
             }
             
             if (jdbcConfig == null) {
                throw new NullPointerException("'jdbcConfig' must not be null");
             }
             
             String implementationTypeName = null;
             
             if (impl == null) {
                 if (jdbcURL.toLowerCase().startsWith("jdbc:postgresql:")) {
                     implementationTypeName = PGImplementation.class.getName();
                 }
                 
                 if (jdbcURL.toLowerCase().startsWith("jdbc:mysql:")) {
                     implementationTypeName = MySQLImplementation.class.getName();
                 }
                 
                 if (implementationTypeName == null) {
                     throw new ToolConfigurationException(
                             "Environment-type is not set and " +
                             "can not be determined from jdbc-url: " + jdbcURL);
                 }  
                 
                 Class<?> implementationType = Class.forName(implementationTypeName);
                 Object io = implementationType.newInstance();                 
                 impl = (Implementation<?>) io;                 
             }
             
             setJdbcURL(jdbcURL);
             setJdbcConfig(jdbcConfig);
             setImplementation(impl);             
                                                  
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
         catch (InstantiationException e) {
             e.printStackTrace();
             throw new ToolException(e.getMessage(), e);
         } 
         catch (IllegalAccessException e) {
             e.printStackTrace();
             throw new ToolException(e.getMessage(), e);
         } 
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
        return implementation;
    }

    public void setImplementation(Implementation<?> environment) {
    	if (environment == null) {
			throw new NullPointerException();
		}
    	
        this.implementation = environment;
    }
    
    
    
    
}
