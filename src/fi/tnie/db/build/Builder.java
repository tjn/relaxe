/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.build;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import fi.tnie.db.DefaultTableMapper;
import fi.tnie.db.QueryException;
import fi.tnie.db.QueryHelper;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.feature.Feature;
import fi.tnie.db.feature.Features;
import fi.tnie.db.feature.SQLGenerationException;
import fi.tnie.db.feature.SQLGenerationResult;
import fi.tnie.db.feature.SQLGenerator;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.impl.pg.PGEnvironment;
import fi.tnie.db.meta.util.Tool;
import fi.tnie.db.source.SourceGenerator;
import fi.tnie.util.io.FileProcessor;
import fi.tnie.util.io.IOHelper;

public class Builder
    extends Tool {
    
    public static final String KEY_PACKAGE = "package";
    public static final String KEY_DEFAULT_SOURCE_DIR = "root-dir";
    
    private Features features;

    private Environment environment;
    public static void main(String[] args) {
        try {
            PGEnvironment env = new PGEnvironment();
            Builder b = new Builder(env);
            
            final Features f = new Features();            
            b.setFeatures(f);
            b.run(args);
        }
        catch (Exception e) {
            logger().error(e.getMessage(), e);
        }
    }    
    
      @Override
      public void run(Connection c, Properties config)
          throws QueryException, IOException, SQLGenerationException, SQLException {
          
          final Environment env = this.environment;
          CatalogFactory cf = env.catalogFactory();          
          getFeatures().installAll(c, cf, false);         
                    
          String pkg = config.getProperty(KEY_PACKAGE);
          File root = getSourceRoot(config);
                    
          generateSources(c, env, root, pkg);
                              
          new FileProcessor(true) {
            @Override
            public void apply(File file) {
                logger().debug(file.getAbsolutePath());                
            }              
          }.traverse(root);
      }

    

    private File getSourceRoot(Properties config) {
        String r = config.getProperty(KEY_DEFAULT_SOURCE_DIR);
          
        File root = (r == null) ? new File(".") : new File(r);
    
        if (!root.isDirectory()) {
            throw new IllegalArgumentException(
                    "Path (" + root.getPath() + ") configured as source dir is not a directory");
        }
          
        return root;
    }
    
    public int removePreviouslyGenerated(final File sourceRootDir) 
        throws IOException {
        File sourceList = getSourceList(sourceRootDir);        
        return remove(sourceList);
    }
        
    public int remove(final File sourceList) 
        throws IOException {
        
        logger().info("removing file in the " + sourceList);
        
        int count = 0;        
        String p = sourceList.getAbsolutePath();
        
        logger().info("source list available ? " + sourceList.exists());
        
        if (sourceList.exists()) {
            Properties previous = IOHelper.doLoad(p);
            logger().info("previous files: " + previous.size());
            
            for (Object o : previous.values()) {
                File d = new File(o.toString());
                
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
    
    public Builder(Environment env) {
        if (env == null) {
            throw new NullPointerException("'env' must not be null");
        }
        
        this.environment = env;
    }
    
    private File getSourceList(File sourceRoot) {
        return new File(sourceRoot, "generated-sources-files.txt");        
    }

    public Features getFeatures() {
        return features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }

    
}
