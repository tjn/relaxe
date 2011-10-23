/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.build;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;

import fi.tnie.db.DefaultTableMapper;
import fi.tnie.db.env.CatalogFactory;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.feature.Features;
import fi.tnie.db.feature.SQLGenerationException;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.source.DefaultNamingPolicy;
import fi.tnie.db.source.NamingPolicy;
import fi.tnie.db.source.SourceGenerator;
import fi.tnie.db.source.TemplateGenerator;
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
    
    private File templateDir;
    
    public static final Option OPTION_GENERATED_DIR = 
        new SimpleOption("generated-sources", "g", new Argument(false), "Dir for generated source files.");
    
    public static final Option OPTION_TEMPLATE_DIR = 
        new SimpleOption("generated-templates", "t", new Argument(false), "Dir for generated template files.");      

    public static final Option OPTION_ROOT_PACKAGE = 
        new SimpleOption("root-package", "r", new Argument(false), "Root java package name for generated classes.");  

    public static final Option OPTION_CATALOG_CONTEXT_PACKAGE = 
        new SimpleOption("catalog-context-package", "c", new Argument(false), "Java package name for generated catalog context classes.");  

    private static Logger logger = Logger.getLogger(Builder.class);
    
    public static void main(String[] args) {
        System.exit(new Builder().run(args));        
    }
    
    @Override
    protected void prepare(Parser p) {     
        super.prepare(p);
        addOption(p, OPTION_GENERATED_DIR);
        addOption(p, OPTION_TEMPLATE_DIR);
        addOption(p, OPTION_ROOT_PACKAGE);
        addOption(p, OPTION_CATALOG_CONTEXT_PACKAGE);        
    }      
    
    @Override
    protected void init(CommandLine cl) 
        throws ToolConfigurationException, ToolException {     
        super.init(cl);
        
        String gen = cl.value(require(cl, OPTION_GENERATED_DIR));
        String tem = cl.value(require(cl, OPTION_TEMPLATE_DIR));
        String pkg = cl.value(require(cl, OPTION_ROOT_PACKAGE));
        String ccp = cl.value(OPTION_CATALOG_CONTEXT_PACKAGE);
        
        setSourceDir(new File(gen));
        setTemplateDir(new File(tem));
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
            
            Catalog cat = cf.create(c);
            
            File root = getSourceDir();            
            generateSources(cat, root);
            traverseFiles(root);
            
            NamingPolicy np = new DefaultNamingPolicy();            
            generateTemplates(cat, getTemplateDir(), np);
            traverseFiles(getTemplateDir());
            
            
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
        catch (Throwable e) {
            logger().error(e.getMessage(), e);
            throw new ToolException(e.getMessage(), e);
        }        
        finally {
            
        }
      }

	private void traverseFiles(File root) {
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
    
    private void generateSources(Catalog cat, File sourceRoot)
            throws QueryException, IOException {
        final File sourceList = getSourceList(sourceRoot);            
        remove(sourceList);
        
        String rp = getRootPackage();
        String ccp = getCatalogContextPackage();
                    
        SourceGenerator gen = new SourceGenerator(sourceRoot);            
        DefaultTableMapper tm = new DefaultTableMapper(rp, ccp);   
              
        Properties current = gen.run(cat, tm);
        
        IOHelper.doStore(current, sourceList.getPath(), "List of the generated source files");            
    }
    
    private void generateTemplates(Catalog cat, File templateRoot, NamingPolicy np)
    	throws QueryException, IOException, XMLStreamException, TransformerException {
	    final File templateList = getTemplateList(templateRoot);            
	    remove(templateList);
	    
	    TemplateGenerator gen = new TemplateGenerator(templateRoot);
	    		    		                
	    Properties current = gen.run(cat, np);
	    
	    IOHelper.doStore(current, templateList.getPath(), "List of the generated template files");            
	}    
    
    public Builder() {
    }
    
    public File getSourceList(File sourceRoot) {
        return new File(sourceRoot, "generated-sources-files.txt");        
    }
    
    public File getTemplateList(File templateRoot) {
        return new File(templateRoot, "generated-templates.txt");        
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

	public File getTemplateDir() {
		return templateDir;
	}

	public void setTemplateDir(File templateDir) {
		this.templateDir = templateDir;
	}
    
    
}
