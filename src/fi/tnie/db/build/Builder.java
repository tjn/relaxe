/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.build;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import fi.tnie.db.DefaultTableMapper;
import fi.tnie.db.QueryException;
import fi.tnie.db.feature.Features;
import fi.tnie.db.feature.SQLGenerationException;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.util.Tool;
import fi.tnie.db.source.SourceGenerator;
import fi.tnie.util.cli.CommandLine;
import fi.tnie.util.cli.Option;
import fi.tnie.util.cli.Parser;
import fi.tnie.util.io.FileProcessor;
import fi.tnie.util.io.IOHelper;

public class Builder
    extends Tool {
    
//    public static final String KEY_PACKAGE = "package";
//    public static final String KEY_DEFAULT_SOURCE_DIR = "root-dir";
    
    private Features features;
    private String rootPackage;
    private File sourceDir;    
    
    public static void main(String[] args) {
        try {
            Builder b = new Builder();
            Parser clp = new Parser();
            
            Option cfg = clp.option("config", "c", false);
            Option et = clp.option("env-type", "e", false);            
            Option jurl = clp.option("jdbc-url", "j", false);
            Option jcfg = clp.option("jdbc-config", null, false);
            Option jusr = clp.option("jdbc-user", "u", true);
            Option jpw = clp.option("jdbc-password", "p", true);
            Option src = clp.option("source-dir", "d", false);
            Option rpkg = clp.option("root-package", "r", false);
            Option help = clp.option("help", "h", false);
            
            CommandLine cl = clp.parse(args);
            
            if (cl.isEmpty() || cl.options().contains(help)) {
                System.err.println(clp.usage(Builder.class));
                System.exit(-1);
                return;
            }
            
            String configPath = b.optional(cl, cfg);
            
            final Properties config = (configPath == null) ?
                new Properties() : 
                IOHelper.doLoad(configPath);
            
            b.overrideWith(config, cl, et);
            b.overrideWith(config, cl, jcfg);
            b.overrideWith(config, cl, jurl);
            b.overrideWith(config, cl, src);
            b.overrideWith(config, cl, rpkg);

            String jdbcConfigPath = b.optional(cl, jcfg);
            
            Properties jdbcConfig = (jdbcConfigPath == null) ? 
                new Properties() :
                IOHelper.doLoad(jdbcConfigPath);
                
            b.overrideWith(jdbcConfig, cl, jusr, "user");
            b.overrideWith(jdbcConfig, cl, jpw, "password");
                        
            String environmentTypeName = b.required(config, et);
            Class<?> environmentType = Class.forName(environmentTypeName);            
            Environment env = (Environment) environmentType.newInstance();
            
            String jdbcUrl = b.required(config, jurl);
            
            // set up builder:
            final Features f = new Features();            
            b.setFeatures(f); 
                        
            b.setRootPackage(b.required(config, rpkg));
            b.setSourceDir(new File(b.required(config, src)));
                                              
            b.run(env, jdbcUrl, config);
        }
        catch (Exception e) {
            logger().error(e.getMessage(), e);
        }
    }
    
    private String overrideWith(Properties config, CommandLine cl, Option opt) {
        return overrideWith(config, cl, opt, key(opt));
    }
    
    private String overrideWith(Properties config, CommandLine cl, Option opt, String key) {
        String value = optional(cl, opt);
        
        if (value != null) {
            config.setProperty(key, value);            
        }
        
        return value;
    }

    public String optional(CommandLine cl, Option opt) {
        if (!cl.options().contains(opt)) {
            return null;
        }
        
        return cl.value(opt);
    }
    
    protected String key(Option o) {
        return key(getKeyPrefix(), o);
    }
        
    protected String key(String prefix, Option o) {
        String n = o.name();
        return ((prefix == null) ? ""  : prefix)  + ((n != null) ? n : o.flag());
    }
    
    public String required(Properties config, Option opt) {
        String k = key(getKeyPrefix(), opt);
        
        if (!config.containsKey(k)) {
            throw new IllegalArgumentException("option " + opt.name() + " is required");
        }
        
        String value = config.getProperty(k);
        
        if (value == null) {
            throw new IllegalArgumentException(
                    "required argument for option " + opt.name() + " is missing");
        }
        
        return value;
    }
    
    public String required(CommandLine cl, Option opt) {
        if (!cl.options().contains(opt)) {
            throw new IllegalArgumentException("option " + opt.name() + " is required");
        }
        String value = cl.value(opt);
        
        if (value == null) {
            throw new IllegalArgumentException(
                    "required argument for option " + opt.name() + " is missing");
        }
        
        return value;
    }

    @Override
      public void run(final Environment env, Connection c)
          throws QueryException, IOException, SQLGenerationException, SQLException {

          c.setAutoCommit(false);        
          CatalogFactory cf = env.catalogFactory();          
          getFeatures().installAll(c, cf, false);         
                    
          String pkg = getRootPackage();
          File root = getSourceDir();
                    
          generateSources(c, env, root, pkg);
                              
          new FileProcessor(true) {
            @Override
            public void apply(File file) {
                logger().debug(file.getAbsolutePath());                
            }              
          }.traverse(root);
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
    
    private void generateSources(Connection c, Environment env, File sourceRoot, String pkg)
            throws QueryException, IOException {
        try {
            final File sourceList = getSourceList(sourceRoot);            
            remove(sourceList);
                        
            SourceGenerator gen = new SourceGenerator(sourceRoot);              
            DefaultTableMapper tm = new DefaultTableMapper(pkg);   
                  
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
    
}
