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

    private Environment environment;
    private List<Feature> featureList;

    public static void main(String[] args) {
        try {
            PGEnvironment env = new PGEnvironment();
            Builder b = new Builder(env);
            
            final Features f = new Features();            
            b.addFeature(f);
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
          
          List<Feature> features = getFeatureList();
          
          {
              ArrayList<Feature> reversed = new ArrayList<Feature>(features);
              Collections.reverse(reversed);              
              boolean revert = true;
              
              for(int i = 0; i < 5; i++) {
                  // revert all
                  if (!process(c, cf, reversed, revert)) {                  
                  }
              }
          }
                              
          for(int i = 0; i < 5; i++) {
              //  keep generating layers until any feature
              //  has nothing to add/remove:
              if (!process(c, cf, features, false)) {
                  break;
              }
          }
          
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

    private boolean process(Connection c, CatalogFactory cf, List<Feature> features, boolean revert) 
        throws QueryException, SQLException, SQLGenerationException {
        
        Catalog cat = cf.create(c);
        int count = 0;
                  
        for (Feature f : features) {                            
            SQLGenerator g = f.getSQLGenerator();
              
            if (g != null) {
                SQLGenerationResult result = revert ? g.revert(cat) : g.modify(cat);
                List<Statement> list = result.statements();
                
                if (!list.isEmpty()) {
                  executeAll(c, list);
                  count += list.size();
                                    
//                  recreate:
                  cat = cf.create(c);
                }                
            }
        }
    
        return count > 0;
    }

    private void executeAll(Connection c, List<Statement> list)
        throws SQLException {
        
        for (Statement statement : list) {
            String sql = statement.generate();
            
            logger().debug("query: " + sql);            
            PreparedStatement ps = c.prepareStatement(sql);
            
            try {
                ps.executeUpdate();      
            }
            finally {
                ps = QueryHelper.doClose(ps);                
            }
        }
        
        c.commit();
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
        
        if (sourceList.exists()) {
            Properties previous = IOHelper.load(p);
            
            for (Object o : previous.values()) {
                File d = new File(o.toString());
                
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
            
            IOHelper.store(current, sourceList.getPath(), "List of the generated source files");            
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
    
    public void addFeature(Feature feature) {
        if (feature == null) {
            throw new NullPointerException("'feature' must not be null");
        }
        
        getFeatureList().add(feature);
    }

    private List<Feature> getFeatureList() {
        if (featureList == null) {
            featureList = new ArrayList<Feature>();            
        }

        return featureList;
    }
    
    private File getSourceList(File sourceRoot) {
        return new File(sourceRoot, "generated-sources-files.txt");        
    }

    
}
