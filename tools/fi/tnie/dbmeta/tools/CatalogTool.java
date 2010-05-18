/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.dbmeta.tools;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import fi.tnie.db.QueryException;
import fi.tnie.db.QueryHelper;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.Environment;
import fi.tnie.util.cli.Argument;
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
        new SimpleOption("environment-type", "e", new Argument(false),
            "Fully qualified name of the class which implements '" + 
            Environment.class.getName() + "'. " +
           "Implementation must have a no-arg public constructor.");
    
    public static final Option OPTION_JDBC_URL = 
        new SimpleOption("jdbc-url", "u", new Argument(false));
    
    public static final Option OPTION_JDBC_CONFIG = 
        new SimpleOption("jdbc-driver-config", "j", 
            new Argument(false), "JDBC Driver configuration as file path to Java properties file");
    
    private Connection connection;
    private Catalog catalog;
    private boolean verbose;
    
    private List<Option> options;    
        
    public CatalogTool() {
        this(CatalogTool.OPTION_HELP, 
                CatalogTool.OPTION_ENV,
                CatalogTool.OPTION_JDBC_URL,
                CatalogTool.OPTION_JDBC_CONFIG);
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

    private void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
    
    public int run()
        throws ToolException {
        return 0;
    }

    protected int run(String[] args) {
        int result = -1;        
        Parser p = new Parser();        
        Connection c = null;
        
        try {
            prepare(p);
            CommandLine cl = p.parse(args);            
            init(cl);
            result = run();                        
        } 
        catch (ToolConfigurationException e) {
            System.err.println(e.getMessage());
            System.err.println();
            System.err.println(p.usage(getClass()));
        } 
        catch (ToolException e) {
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
        if (!p.contains(opt)) {
            p.option(opt);
        }   
    }

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
            
            String environmentTypeName = cl.value(require(cl, OPTION_ENV));
            String jdbcURL = cl.value(require(cl, OPTION_JDBC_URL));
            String jdbcDriverConfigPath = cl.value(require(cl, OPTION_JDBC_CONFIG)); 
            
            Properties jdbcConfig = IOHelper.doLoad(jdbcDriverConfigPath);
                
            Class<?> environmentType = Class.forName(environmentTypeName);
            Environment env = (Environment) environmentType.newInstance();
            CatalogFactory cf = env.catalogFactory();
            
            Class.forName(env.driverClassName());
            Connection c = DriverManager.getConnection(jdbcURL, jdbcConfig);
                    
            final Catalog cat = cf.create(c);
            setConnection(c);
            setCatalog(cat);            
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
        catch (SQLException e) {
            e.printStackTrace();
            throw new ToolException(e.getMessage(), e);
        } 
        catch (QueryException e) {
            e.printStackTrace();
            throw new ToolException(e.getMessage(), e);
        }
     }    
    
    protected Option require(CommandLine cl, Option required) {
        if (!cl.has(required)) {
            throw new IllegalArgumentException("option " + required.name() + " is required");
        }
                        
        return required;
    }

    public Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
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
}
