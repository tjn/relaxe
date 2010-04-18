/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.impl.pg.PGEnvironmentTest;
import fi.tnie.util.io.FileProcessor;
import fi.tnie.util.io.IOHelper;

public class TestSuiteBuilder
    extends TestCase {      
    
//    private final String ROOT_PACKAGE = "fi.tnie.db.testapp";
    private File testConfigDir;
    
    private static FileFilter DIRECTORY = new FileFilter() {
        @Override
        public boolean accept(File p) {
            return p.isDirectory();
        }        
    };
    
    private static FileFilter PROPERTIES_FILE = new FileFilter() {
        @Override
        public boolean accept(File p) {
            return p.isFile() && p.getName().endsWith(".properties");
        }        
    };
    
    private static String JDBC_DRIVER_CONFIG_FILE_NAME = "jdbc.properties";    
    private static File JDBC_DRIVER_CONFIG_FILE = new File(JDBC_DRIVER_CONFIG_FILE_NAME);
    
    private static FileFilter TEST_CONFIG_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(File p) {
            return p.isFile() && new File(p.getName()).equals(JDBC_DRIVER_CONFIG_FILE);
        }        
    };
    
    private static FileFilter JAR_FILE = new FileFilter() {
        @Override
        public boolean accept(File p) {
            return p.isFile() && p.getName().endsWith(".jar");
        }        
    };    
    
    private static FileFilter DRIVER_BUNDLE_DIR = new FileFilter() {
        @Override
        public boolean accept(File p) {
            File[] jars = p.listFiles(JAR_FILE);
            boolean a = (jars != null) && (jars.length > 0);            
            logger().debug(p.getAbsolutePath() + " driver-bundle-dir ? " + a);
            return a;
        }        
    };
    
    private static Logger logger = Logger.getLogger(TestSuiteBuilder.class);
    
    /**
     * @param args
     */
    public static void main(String[] args) {
//        int failed = 0;
//        
//        try {            
//            TestResult result = TestRunner.run(suite());
//            failed = result.failureCount();            
//        }
//        catch (Throwable e) {
//            logger().error(e.getMessage(), e);
//            failed = -1;
//        }
//        
//        System.exit(failed);
    }
    
//    public static Test suite() {
//        
//        final List<TestSuiteBuilder> all = new ArrayList<TestSuiteBuilder>();
//
//        try {
//            createSuite(all, new File("test-config"));
//        }
//        catch (Exception e) {
//            logger().error(e.getMessage(), e);
//            throw new RuntimeException("test suite creation failed");
//        }
//                
//        return new Test() {
//            @Override
//            public int countTestCases() {              
//                return all.size();
//            }
//
//            @Override
//            public void run(TestResult result) {
//                for (TestSuiteBuilder s : all) {
//                    s.run(result);
//                }
//            }            
//        };
//    }

    public Test createSuite(File cfgdir) 
        throws Exception {
        logger().info("runSuite: enter");
        
        TestSuite all = new TestSuite("All");        
                
        File[] impls = cfgdir.listFiles(DIRECTORY);
        
        for (File impl : impls) {
            logger().debug("impl-dir: " + impl.getAbsolutePath());
            
            TestSuite suite = createImplementationSuite(impl);            
            addSuite(suite, all);
        }       
        
        return all;
    }

    public TestSuite createImplementationSuite(final File impl) 
        throws Exception {
        
        logger().info("impl-dir: " + impl.getPath());
        
        TestSuite implSuite = new TestSuite(impl.getName()); 
        
        // collect the jar-dirs:
        File drivers = new File(impl, "drivers");
        
        final List<File> jardirs = files(drivers, DRIVER_BUNDLE_DIR);
        logger().debug("jardirs in " + drivers.getPath() + ": " + jardirs);
                
        Properties implConfig = loadTestConfig(impl);
        
        File srvdir = new File(impl, "servers");
        srvdir.mkdirs();
        
        final List<File> servers = files(srvdir, PROPERTIES_FILE);
        logger().info("servers: " + servers);
        
        String environmentTypeName = implConfig.getProperty("env-type");
        assertNotNull("no key 'env-type' in impl-config for " + impl, environmentTypeName);        
        
        Class<?> environmentType = Class.forName(environmentTypeName);            
        Environment env = (Environment) environmentType.newInstance();
        
        IOHelper ioh = new IOHelper();
                
        for (File jardir : jardirs) {            
            Driver driver = loadDriver(env, jardir);
                        
            List<File> driverConfigFiles = files(jardir, TEST_CONFIG_FILE_FILTER);
            
            if (driverConfigFiles.isEmpty()) {
                File defaults = new File(jardir, JDBC_DRIVER_CONFIG_FILE_NAME);
                defaults.createNewFile();
                driverConfigFiles.add(defaults);
            }
            
            logger().info("driver-config-files: " + driverConfigFiles);
            
            if (driver == null) {
                String msg = "unable to load jdbc-driver from jardir: " + jardir.getPath();
                logger().warn(msg);
                implSuite.addTest(failure(msg));    
            }
            else {
                TestSuite driverSuite = new TestSuite(driverInfo(driver));
                                                               
                logger().info("JDBC-driver: " + driverInfo(driver, jardir));
                
                for (File f : driverConfigFiles) {     
                    File cd = f.getParentFile();
                    Properties drvcfg = loadTestConfig(cd);
                    
                    TestSuite driverConfigSuite = new TestSuite("Driver config: " + cd.getName());
                    driverSuite.addTest(driverConfigSuite);
                                        
                    for (File s : servers) {
                        Properties srvcfg = ioh.load(s.getAbsolutePath());                                                
                        TestSuite test = createSuite(env, driver, drvcfg, srvcfg);                        
                        addSuite(test, driverConfigSuite);                        
                    }                    
                }
                
                addSuite(driverSuite, implSuite);                
            }
        }
        
        return implSuite;
    }
    
    
    private Test failure(final String msg) {
        return new Test() {
            @Override
            public int countTestCases() {
                return 1;
            }

            @Override
            public void run(TestResult result) {
                fail(msg);
            }
        };
    }
    
//    @SuppressWarnings("deprecation")
//    private void run(final Environment env, File jardir, Properties srv) 
//        throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, SQLException {
//        
//        try {         
//            File[] jars = jardir.listFiles(JAR_FILE);
//            URL[] urls = new URL[jars.length];
//            
//            final String cp = System.getProperty("java.class.path");
//            logger().debug("cp: " + cp);
//            
//            String newCp = cp;
//            
//            for (int i = 0; i < jars.length; i++) {
//                urls[i] = jars[i].toURL();
//                newCp += ";" + jars[i].getAbsolutePath();
//                logger().debug(urls[i].toString());
//            }
//            
//            logger().debug("new-cp: " + newCp);
//            System.setProperty("java.class.path", newCp);
//            
//            logger().debug("new-cp-set: " + System.getProperty("java.class.path"));
//            
//            Class<?> dmclazz = DriverManager.class;
//            
//            logger().debug("drvman-loader: " + dmclazz.getClassLoader());        
//            logger().debug("drvman-class-instance: " + System.identityHashCode(dmclazz));
//                            
//    //        URLClassLoader ucl = new URLClassLoader(urls, getClass().getClassLoader());        
//    //        logger().debug("ucl-parent: " + ucl.getParent());
//            
//            // Thread.currentThread().setContextClassLoader(ucl);
//            
//            logger().debug("loading driver: " + env.driverClassName());
//            
//            Class<?> drvcls = Class.forName(env.driverClassName());
//    //        Class<?> drvcls = Class.forName(env.driverClassName(), true, ucl);
//            logger().debug("drvcls: " + drvcls);
//            logger().debug("drvcls-loader: " + drvcls.getClassLoader());
//            
//            System.setProperty("java.class.path", cp);
//                                           
//            Enumeration<Driver> drivers = DriverManager.getDrivers();
//            
//            logger().debug("enumerating drivers: " + drivers);
//                                
//            Driver driver = null;
//                       
//            while (drivers.hasMoreElements()) {
//                Driver drv = drivers.nextElement();
//                
//                logger().debug("drv: " + drv);
//                
//                if (drv.getClass().equals(drvcls)) {
//                    driver = drv;
//                    break;
//                }
//            }
//            
//            if (driver == null) {
//                throw new IllegalStateException("driver instance not found");
//            }
//            else {
//                logger().debug("deregistering: " + driver);
//                DriverManager.deregisterDriver(driver);
//                logger().debug("deregistered: " + driver);
//            }
//            
//            logger().debug("jdbc-driver: " + driver);
//        }
//        catch (Throwable e) {
//            logger().error(e.getMessage(), e);
//        }
//    }
    
  public static String driverInfo(Driver driver) {
      return driverInfo(driver, null);
  }
    
  private static String driverInfo(Driver driver, File jardir) {
      return 
          driver.getClass().getName() + " (" + 
          driver.getMajorVersion() + "." +
          driver.getMinorVersion() + ")" +
          ((jardir == null) ? "" : "@" + jardir.getPath());
  }

  private TestSuite createSuite(final Environment env, Driver driver, Properties drvcfg, Properties srvcfg) 
      throws SQLException 
  {            
      assertNotNull(env);
      assertNotNull(driver);
      assertNotNull(drvcfg);
      assertNotNull(srvcfg);
                
      String url = srvcfg.getProperty("jdbc.url");
      assertNotNull("no key jdbc.url in server configuration" , url);
      assertFalse(url.trim().equals(""));         
      
      boolean a = driver.acceptsURL(url);
      assertTrue("JDBC driver " + driverInfo(driver) + " does dot accept url: " + url, a);
      
      SimpleTestContext tctx = new SimpleTestContext(env, driver, url, drvcfg);
//      suite.setCurrent(tctx);          
//      dest.add(suite);
      
      // EnvironmentTest es = new EnvironmentTest("all", tctx);
      
      TestSuite ts = createEnvironmentTestSuite(tctx);      
      
      return ts;
  }
  
  
  private TestSuite createEnvironmentTestSuite(SimpleTestContext tctx) {
     TestSuite ts = new TestSuite("env");
     
     Environment env = tctx.getEnvironment();
          
     addSuite(createTestsFor(Environment.class, tctx), ts);
     addSuite(createTestsFor(env.getClass(), tctx), ts);
          
     addSuite(createTestsFor(CatalogFactory.class, tctx), ts);
     addSuite(createTestsFor(env.catalogFactory().getClass(), tctx), ts);
                    
     return ts;
  }

@SuppressWarnings("deprecation")
  private static Driver loadDriver(final Environment env, File jardir) {      
    Driver driver = null;
      
    try {
      List<File> jars = files(jardir, JAR_FILE);      
      URL[] urls = new URL[jars.size()];
                      
      for (int i = 0; i < urls.length; i++) {
          urls[i] = jars.get(i).toURL();
          logger().debug(urls[i].toString());
      }
      
      logger().debug("driver-jars-count: " + jars.size());
      logger().debug("driver-jars: " + jars);
                
      URLClassLoader ucl = new URLClassLoader(urls);
      logger().debug("loading driver: " + env.driverClassName());
                    
      Class<?> drvcls = Class.forName(env.driverClassName(), true, ucl);
          
      logger().debug("drvcls: " + drvcls);
      driver = (Driver) drvcls.newInstance();
      logger().debug("jdbc-driver: " + driver);
    }
    catch (Throwable e) {
        logger().error(e.getMessage(), e);
    }
    return driver;
  }


    private static Properties loadTestConfig(File dir) 
        throws IOException {
        LinkedList<File> files = new LinkedList<File>(); 
        
        while (dir != null) {
            File p = new File(dir, JDBC_DRIVER_CONFIG_FILE_NAME);
            
            if (p.canRead()) {                
                files.addFirst(p);
            }
            
            dir = dir.getParentFile();
        }
        
        Properties cfg = null;
        IOHelper ioh = new IOHelper(); 
        
        if (!files.isEmpty()) {
            for (File f : files) {
                cfg = ioh.load(f.getAbsolutePath(), cfg);
            }            
        }
        
        return cfg;        
    }

    public File getTestConfigDir() {
        return testConfigDir;
    }

    public void setTestConfigDir(File testConfigDir) {
        this.testConfigDir = testConfigDir;
    }
    
//    private TestSuite createTestsFor(Class<?> klass, final SimpleTestContext ctx) {    
//        TestSuite injected = new TestSuite();
//        
//        String testClass = klass.getName() + "Test";
//        
//        try {
//          logger().info("test-class for " + klass.getName() + ": " + testClass);
//          
//          Class<?> tt = Class.forName(testClass);
//                    
//          final TestSuite ts = new TestSuite(tt);
//          injected.setName(ts.getName());
//                
//          int testCount = ts.testCount();
//          logger().info("test-count: " + ts.testCount());
//          
//          for (int i = 0; i < testCount; i++) {
//              final Test t = ts.testAt(i);
//              logger().debug("test: " + t.getClass());              
//              final TestCase tc = (TestCase) ((t instanceof TestCase) ? t : null);
//                            
//              TestCase a = new TestCase() {            
//                @Override
//                public void run(TestResult tr) {
//                    if (t instanceof DBMetaTest) {
//                        DBMetaTest dbtest = (DBMetaTest) t;
//                        dbtest.init(ctx);          
//                    }                    
//                    
//                    t.run(tr);
//                }
//                
//                @Override
//                public int countTestCases() {                 
//                    return t.countTestCases();
//                }
//                
//                @Override
//                public String getName() {
//                    return (tc == null) ? super.getName() : tc.getName()+ ".inj";
//                }                 
//              };
//              
//              injected.addTest(a);       
//          }      
//        } 
//        catch (ClassNotFoundException e) {
//            logger().info("no test class for: " + klass.getName());
//        }     
//        
//        return injected;
//    }

    private TestSuite createTestsFor(Class<?> klass, final SimpleTestContext ctx) {    
        
        String testClass = klass.getName() + "Test";
        TestSuite injected = new TestSuite(testClass);
        
        try {
          logger().info("test-class for " + klass.getName() + ": " + testClass);
          
          Class<?> tt = Class.forName(testClass);
//          Test test = TestSuite.createTest(tt, klass.getName());
          // Test test = TestSuite.createTest(theClass, name);
          TestSuite gts = new TestSuite(tt);
          
          logger().info("gts-test: " + gts.testCount());
          
          for (int i = 0; i < gts.testCount(); i++) {
              Test single = gts.testAt(i);            
              logger().info("testcase " + i + ": " + single.getClass() + ": elems=" + single.countTestCases());
              
              if (single instanceof DBMetaTest) {
                  DBMetaTest dbtest = (DBMetaTest) single;
                  logger().debug("initing with: " + dbtest.id() + " with " + ctx);
                  dbtest.init(ctx);
                  logger().debug("inited: " + dbtest.id());
              }
          }    
          
          injected.addTest(gts);
          
//          logger().info("test-class: " + test.getClass());
//                    
//          if (test instanceof DBMetaTest) {
//              DBMetaTest dbtest = (DBMetaTest) test;
//              logger().debug("initing with: " + dbtest.id() + " with " + ctx);
//              dbtest.init(ctx);
//              logger().debug("inited: " + dbtest.id());
//          }
          
//          injected.addTest(test);
                    
//          final TestSuite ts = new TestSuite(tt);
//          injected.setName(ts.getName());
//                
//          int testCount = ts.testCount();
//          logger().info("test-count: " + ts.testCount());
//          
//          for (int i = 0; i < testCount; i++) {
//              final Test t = ts.testAt(i);
//              logger().debug("test: " + t.getClass());              
//              final TestCase tc = (TestCase) ((t instanceof TestCase) ? t : null);
//                            
//              TestCase a = new TestCase() {            
//                @Override
//                public void run(TestResult tr) {
//                    if (t instanceof DBMetaTest) {
//                        DBMetaTest dbtest = (DBMetaTest) t;
//                        dbtest.init(ctx);          
//                    }                    
//                    
//                    t.run(tr);
//                }
//                
//                @Override
//                public int countTestCases() {                 
//                    return t.countTestCases();
//                }
//                
//                @Override
//                public String getName() {
//                    return (tc == null) ? super.getName() : tc.getName()+ ".inj";
//                }                 
//              };
//              
//              injected.addTest(a);       
//          }      
        } 
        catch (ClassNotFoundException e) {
            logger().info("no test class for: " + klass.getName());
        } 
        
        return injected;
    }

    public static Logger logger() {
        return TestSuiteBuilder.logger;
    }
        
    private static List<File> files(File dir, FileFilter filter) {
        final ArrayList<File> dest = new ArrayList<File>();
        
        FileProcessor fp = new FileProcessor(filter) {
            @Override
            public void apply(File file) {
                dest.add(file);
            }
        };
        
        fp.traverse(dir);
        return dest;
    }
        
    private void addSuite(TestSuite test, TestSuite suite) {                
        if (test != null && test.testCount() > 0) {
            suite.addTest(test);
        }
    }
}
