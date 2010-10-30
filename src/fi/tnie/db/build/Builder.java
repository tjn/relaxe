/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.build;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import fi.tnie.db.DefaultTableMapper;
import fi.tnie.db.QueryException;
import fi.tnie.db.env.CatalogFactory;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.feature.Features;
import fi.tnie.db.feature.SQLGenerationException;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.source.SourceGenerator;
import fi.tnie.dbmeta.tools.CatalogTool;
import fi.tnie.dbmeta.tools.ToolConfigurationException;
import fi.tnie.dbmeta.tools.ToolException;
import fi.tnie.util.cli.Argument;
import fi.tnie.util.cli.CommandLine;
import fi.tnie.util.cli.Option;
import fi.tnie.util.cli.Parser;
import fi.tnie.util.cli.SimpleOption;
import fi.tnie.util.io.FileProcessor;
import fi.tnie.util.io.IOHelper;

public class Builder
    extends CatalogTool {
    
    private Features features;
    private String rootPackage;
    private String catalogContextPackage;
    private File sourceDir;
    
    public static final Option OPTION_GENERATED_DIR = 
        new SimpleOption("generated-sources", "g", new Argument(false), "Dir for generated source files.");  

    public static final Option OPTION_ROOT_PACKAGE = 
        new SimpleOption("root-package", "r", new Argument(false), "Root java package name for generated classes.");  

    public static final Option OPTION_CATALOG_CONTEXT_PACKAGE = 
        new SimpleOption("catalog-context-package", "c", new Argument(false), "Java package name for generated catalog context classes.");  

    private static Logger logger = Logger.getLogger(Builder.class);
    
    public static void main(String[] args) {
        System.exit(new Builder().run(args));
        
//        try {
//            Builder b = new Builder();
//            Parser clp = new Parser();
//            
//            Option cfg = clp.option("config", "c", false);
//            Option et = clp.option("env-type", "e", false);            
//            Option jurl = clp.option("jdbc-url", "j", false);
//            Option jcfg = clp.option("jdbc-config", null, false);
//            Option jusr = clp.option("jdbc-user", "u", true);
//            Option jpw = clp.option("jdbc-password", "p", true);
//            Option src = clp.option("source-dir", "d", false);
//            Option rpkg = clp.option("root-package", "r", false);
//            Option help = clp.option("help", "h", false);
//            
//            CommandLine cl = clp.parse(args);
//            
//            if (cl.isEmpty() || cl.options().contains(help)) {
//                System.err.println(clp.usage(Builder.class));
//                System.exit(-1);
//                return;
//            }
//                                    
////            String configPath = b.optional(cl, cfg);
////            
////            logger().info("configPath: " + configPath);
////            
////            final Properties config = (configPath == null) ?
////                new Properties() : 
////                IOHelper.doLoad(configPath);
////            
////            b.overrideWith(config, cl, et);
////            b.overrideWith(config, cl, jcfg);
////            b.overrideWith(config, cl, jurl);
////            b.overrideWith(config, cl, src);
////            b.overrideWith(config, cl, rpkg);
////
////            String jdbcConfigPath = b.optional(cl, jcfg);
////            
////            Properties jdbcConfig = (jdbcConfigPath == null) ? 
////                new Properties() :
////                IOHelper.doLoad(jdbcConfigPath);
////                
////            b.overrideWith(jdbcConfig, cl, jusr, "user");
////            b.overrideWith(jdbcConfig, cl, jpw, "password");
////            String environmentTypeName = b.required(config, et);
//            
//            String environmentTypeName = cl.value(et);
//            String jdbcUrl = cl.value(jurl);
//            String rootpkg = cl.value(rpkg);
//            String srcdir = cl.value(src);
//            String jdbcConfigPath = cl.value(jcfg);
//            
//            Class<?> environmentType = Class.forName(environmentTypeName);            
//            Environment env = (Environment) environmentType.newInstance();
//            
////            String jdbcUrl = b.required(config, jurl);
//            
//            
//            // set up builder:
//            final Features f = new Features();            
//            b.setFeatures(f); 
//                        
//            // b.setRootPackage(b.required(config, rpkg));
//            b.setRootPackage(rootpkg);
////            b.setSourceDir(new File(b.required(config, src)));
//            b.setSourceDir(new File(srcdir));
//            
//          Properties jdbcConfig = (jdbcConfigPath == null) ? 
//                  new Properties() :
//                      IOHelper.doLoad(jdbcConfigPath);
//            
//            // jdbcConfig.list(System.out);                        
//            b.run(env, jdbcUrl, jdbcConfig);
//        }
//        catch (Exception e) {
//            logger().error(e.getMessage(), e);
//        }
    }
    
//    private String overrideWith(Properties config, CommandLine cl, Option opt) {
//        return overrideWith(config, cl, opt, key(opt));
//    }
//    
//    private String overrideWith(Properties config, CommandLine cl, Option opt, String key) {
//        String value = optional(cl, opt);
//        
//        if (value == null) {
//            logger().debug("no option: " + opt.name());
//        }
//        else {            
//            config.setProperty(key, value);            
//            logger().debug("set option: " + key + "=" + value);
//        }
//        
//        return value;
//    }
//
//    public String optional(CommandLine cl, Option opt) {
//        if (!cl.has(opt)) {
//            return null;
//        }
//        
//        return cl.value(opt);
//    }
//    
//    protected String key(Option o) {
//        return key(getKeyPrefix(), o);
//    }
//        
//    protected String key(String prefix, Option o) {
//        String n = o.name();
//        return ((prefix == null) ? ""  : prefix)  + ((n != null) ? n : o.flag());
//    }
//    
//    public String required(Properties config, Option opt) {
//        String k = key(getKeyPrefix(), opt);
//        
//        if (!config.containsKey(k)) {
//            throw new IllegalArgumentException("option " + opt.name() + " is required");
//        }
//        
//        String value = config.getProperty(k);
//        
//        if (value == null) {
//            throw new IllegalArgumentException(
//                    "required argument for option " + opt.name() + " is missing");
//        }
//        
//        return value;
//    }
//    
//    public String required(CommandLine cl, Option opt) {
//        if (!cl.options().contains(opt)) {
//            throw new IllegalArgumentException("option " + opt.name() + " is required");
//        }
//        String value = cl.value(opt);
//        
//        if (value == null) {
//            throw new IllegalArgumentException(
//                    "required argument for option " + opt.name() + " is missing");
//        }
//        
//        return value;
//    }
    
    @Override
    protected void prepare(Parser p) {     
        super.prepare(p);
        addOption(p, OPTION_GENERATED_DIR);
        addOption(p, OPTION_ROOT_PACKAGE);
        addOption(p, OPTION_CATALOG_CONTEXT_PACKAGE);        
    }      
    
    @Override
    protected void init(CommandLine cl) 
        throws ToolConfigurationException, ToolException {     
        super.init(cl);
        
        String gen = cl.value(require(cl, OPTION_GENERATED_DIR));
        String pkg = cl.value(require(cl, OPTION_ROOT_PACKAGE));
        String ccp = cl.value(OPTION_CATALOG_CONTEXT_PACKAGE);
        
        setSourceDir(new File(gen));
        setRootPackage(pkg);
        setCatalogContextPackage(ccp);
    }
    
    @Override
    protected void run() 
        throws ToolException {
        
        try {        
            Connection c = getConnection();
//            Catalog cat = getCatalog();            
            
//            Environment env = cat.getEnvironment();            
            Implementation impl = getImplementation();
            
            c.setAutoCommit(false);        
            CatalogFactory cf = impl.catalogFactory();          
            getFeatures().installAll(c, cf, false);         
                        
            File root = getSourceDir();
                        
            generateSources(c, impl, root);
                                  
              new FileProcessor(true) {
                @Override
                public void apply(File file) {
                    logger().debug(file.getAbsolutePath());                
                }              
              }.traverse(root);
        } 
        catch (SQLException e) {
            logger().error(e.getMessage(), e);
            throw new ToolException(e); 
        } 
        catch (QueryException e) {          
            logger().error(e.getMessage(), e);
            throw new ToolException(e.getMessage(), e);
        } 
        catch (SQLGenerationException e) {        
            logger().error(e.getMessage(), e);
            throw new ToolException(e.getMessage(), e);
        } 
        catch (IOException e) {
            logger().error(e.getMessage(), e);
            throw new ToolException(e);
        }
        finally {
            
        }
      }

    

//    private File getSourceRoot(Properties config) {
//        String r = config.getProperty(KEY_DEFAULT_SOURCE_DIR);
//          
//        File root = (r == null) ? new File(".") : new File(r);
//    
//        if (!root.isDirectory()) {
//            throw new IllegalArgumentException(
//                    "Path (" + root.getPath() + ") configured as source dir is not a directory");
//        }
//          
//        return root;
//    }
    
    public int removePreviouslyGenerated(final File sourceRootDir) 
        throws IOException {
        logger().debug("removePreviouslyGenerated - enter: " + sourceRootDir);        
        File sourceList = getSourceList(sourceRootDir);        
        logger().debug("sourceList: " + sourceList);
        return remove(sourceList);
    }
        
    public int remove(final File sourceList) 
        throws IOException {
        
        logger().info("removing files in the " + sourceList);
        
        int count = 0;        
        String p = sourceList.getAbsolutePath();
        
        logger().info("source list available ? " + sourceList.exists());
        
        if (sourceList.exists()) {
            Properties previous = IOHelper.doLoad(p);
            logger().info("previous files: " + previous.size());
            
            File dir = sourceList.getParentFile();
                        
            for (Object o : previous.values()) {
                File d = new File(dir, o.toString());
                
                logger().info("previous file: " + d.getPath() + " exists ? " + d.exists());
                
//                logger().info("deleting files: " + previous.size());
                
                if (d.exists()) {
                    if (d.delete()) {                        
                        count++;
                    }
                }
            }
        }
        
        logger().info("removed: " + count);
        
        return count;
    }
    
    private void generateSources(Connection c, Implementation env, File sourceRoot)
            throws QueryException, IOException {
        try {
            final File sourceList = getSourceList(sourceRoot);            
            remove(sourceList);
            
            String rp = getRootPackage();
            String ccp = getCatalogContextPackage();
                        
            SourceGenerator gen = new SourceGenerator(sourceRoot);            
            DefaultTableMapper tm = new DefaultTableMapper(rp, ccp);   
                  
            CatalogFactory cf = env.catalogFactory();
            Catalog cat = cf.create(c);
            Properties current = gen.run(c, cat, tm);
            
            IOHelper.doStore(current, sourceList.getPath(), "List of the generated source files");            
        }
        catch (SQLException e) {
          throw new QueryException(e.getMessage(), e);
        }
    }    
    
    public Builder() {
    }
    
    public File getSourceList(File sourceRoot) {
        return new File(sourceRoot, "generated-sources-files.txt");        
    }

    public Features getFeatures() {
        if (features == null) {
            features = new Features();            
        }

        return features; 
    }

    public void setFeatures(Features features) {
        this.features = features;
    }
    
    protected String getKeyPrefix() {
        return getClass().getPackage().getName() + ".";
    }

    public String getRootPackage() {
        return rootPackage;
    }

    public void setRootPackage(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    public File getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(File sourceDir) {
        if (sourceDir == null) {
            throw new NullPointerException("'sourceDir' must not be null");
        }
        
        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException(
                    "Path (" + sourceDir.getPath() + ") configured as source-dir is not a directory");
        }
        
        this.sourceDir = sourceDir;
    }
    
    public static Logger logger() {
        return Builder.logger;
    }

    @Override
    public void setCatalog(Catalog catalog) {
        super.setCatalog(catalog);
    }

    @Override
    public void setConnection(Connection connection) {
        super.setConnection(connection);
    }

	public String getCatalogContextPackage() {
		return catalogContextPackage;
	}

	public void setCatalogContextPackage(String catalogContextPackage) {
		this.catalogContextPackage = catalogContextPackage;
	}
    
    
}
