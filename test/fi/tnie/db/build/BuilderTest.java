/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.build;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import fi.tnie.db.DefaultEntityContext;
import fi.tnie.db.DefaultTableMapper;
import fi.tnie.db.EntityContext;
import fi.tnie.db.QueryException;
import fi.tnie.db.expr.ddl.CreateTable;
import fi.tnie.db.feature.Features;
import fi.tnie.db.feature.SQLGenerationException;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.impl.pg.PGEnvironment;
import fi.tnie.testapp.CatalogContext;
import fi.tnie.util.io.IOHelper;
import fi.tnie.util.io.Launcher;
import fi.tnie.util.io.RunResult;
import junit.framework.TestCase;
import junit.framework.TestResult;

public class BuilderTest extends TestCase {
    
    public static final String ROOT_PACKAGE = "fi.tnie.testapp";
    
    private static Logger logger = Logger.getLogger(BuilderTest.class);
        
    private Catalog catalog = null;
    private Connection connection = null;    
    private String rootPackage = null;
    private File sourceDir = null;
    private File outputDir = null;
        
//    @SuppressWarnings("deprecation")
//    public void testGeneration(Catalog cat, Connection c) 
//        throws IOException, QueryException, SQLGenerationException, SQLException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException {
//    
//        File srcdir = new File("generated/src");
//        File bindir = new File("generated/bin");        
//                        
//        testGeneration(cat, c, ROOT_PACKAGE, srcdir, bindir);        
//    }
  
       
    
    @Override
    public void run(TestResult result) {
        try {
            testGeneration(getCatalog(), getConnection(), getRootPackage(), getSourceDir(), getOutputDir(), result);
        }
        catch (Exception e) {
            result.addError(this, e);
        }
    }
    
    @SuppressWarnings("deprecation")
    public void testGeneration(Catalog cat, Connection c, String rootpkg, File srcdir, File bindir, TestResult result) 
        throws IOException, QueryException, SQLGenerationException, SQLException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    
        Builder b = new Builder();
                                
        srcdir.mkdirs();
        b.removePreviouslyGenerated(srcdir);
        b.setSourceDir(srcdir);
        b.setRootPackage(rootpkg);
        
        final Features f = new Features();            
        b.setFeatures(f);
        
        b.run(cat.getEnvironment(), c);
        
        File sourceList = b.getSourceList(srcdir);
        assertNotNull(sourceList);
        assertTrue(sourceList.exists());        
        bindir.mkdirs();        
        Properties sources = IOHelper.doLoad(sourceList.getPath());
        assertNotNull(sources);
        assertFalse(sources.isEmpty());
                
        Launcher cl = new Launcher();
        cl.setDir(srcdir);
        
        cl.add("javac");
        cl.add("-sourcepath");
        cl.add(srcdir.getPath());
        cl.add("-classpath");
        cl.add(System.getProperty("java.class.path"));        
        cl.add("-d");
        cl.add(bindir.getAbsolutePath());        
        
        for (Object p : sources.values()) {
            cl.add(p.toString());
        }
        
        logger().debug(cl.args().toString());
        
        logger().debug("compiling [" + sources.size() + "] java sources files..." );        
        RunResult r = cl.exec();
        
        logger().debug("compiled: " + r.succeeded());
        
        StringBuffer buf = new StringBuffer();
        buf.append("exit-code: ");        
        buf.append(r.getExitCode());
        buf.append("; output=");
        buf.append(r.getOutput());
        buf.append("; ");
        buf.append("; error=");
        buf.append(r.getError());
                
        assertTrue(buf.toString(), r.succeeded());
        
        URL[] path = { bindir.toURL() };                  
        URLClassLoader gcl = new URLClassLoader(path);
        
        logger().debug("instantiating entity context...");
        String n = ROOT_PACKAGE + ".CatalogContext";        
        EntityContext ec = load(n, gcl);
        assertNotNull(ec);
        logger().debug("binding all...");
        ec.bindAll(cat, new DefaultTableMapper(ROOT_PACKAGE));        
        logger().debug("generation OK.");
    }

    public static Logger logger() {
        return BuilderTest.logger;
    }
    
    
    private EntityContext load(String n, ClassLoader cl) 
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> c = Class.forName(n, true, cl);
        final Class<? extends EntityContext> cc = c.asSubclass(EntityContext.class);
        return cc.newInstance();
    }


    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public Catalog getCatalog() {
        return catalog;
    }


    public Connection getConnection() {
        return connection;
    }


    public void setConnection(Connection connection) {
        this.connection = connection;
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
        this.sourceDir = sourceDir;
    }



    public File getOutputDir() {
        return outputDir;
    }



    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }    
}
