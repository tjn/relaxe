/*
 * This file is part of the Relaxe project.
 * Copyright (c) 2009-2013 Topi Nieminen
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
package com.appspot.relaxe.build;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.DefaultTableMapper;
import com.appspot.relaxe.DefaultTypeMapper;
import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.feature.Features;
import com.appspot.relaxe.feature.SQLGenerationException;
import com.appspot.relaxe.map.TableMapper;
import com.appspot.relaxe.map.TypeMapper;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Environment;
import com.appspot.relaxe.meta.IdentifierRules;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.source.SourceGenerator;
import com.appspot.relaxe.tools.CatalogTool;
import com.appspot.relaxe.tools.ToolConfigurationException;
import com.appspot.relaxe.tools.ToolException;

import fi.tnie.util.cli.Parameter;
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
    // private Class<? extends Environment> environmentType = null;
    private Environment targetEnvironment = null;
    
    private transient TableMapper tableMapper;
    private transient TypeMapper typeMapper;

    
    private SchemaFilter schemaFilter;
    
    private static final SchemaFilter ALL_SCHEMAS = new SchemaFilter() {		
		@Override
		public boolean accept(Schema s) {
			return true;
		}
	};
    
    public static final Option OPTION_GENERATED_DIR = 
        new SimpleOption("generated-sources", "g", new Parameter(false), "Dir for generated source files.");
    
    public static final Option OPTION_TEMPLATE_DIR = 
        new SimpleOption("generated-templates", "t", new Parameter(false), "Dir for generated template files.");      

    public static final Option OPTION_ROOT_PACKAGE = 
        new SimpleOption("root-package", "r", new Parameter(false), "Root java package name for generated classes.");  

    public static final Option OPTION_CATALOG_CONTEXT_PACKAGE = 
        new SimpleOption("catalog-context-package", "c", new Parameter(false), "Java package name for generated catalog context classes.");  

    public static final Option OPTION_INCLUDE_ONLY_SCHEMAS = 
        new SimpleOption("only-schemas", "o", new Parameter("schema-name", 1, null), "Schema names to include.");
    
    public static final Option OPTION_TYPE_MAPPER_IMPLEMENTATION = 
        new SimpleOption("type-mapper-implementation", "m", new Parameter(false), "Class name of the implementation to be used as a type mapper");  

    public static final Option OPTION_ENVIRONMENT_IMPLEMENTATION = 
        new SimpleOption("environment-implementation", null, new Parameter(false), "Class name of the implementation of the environment");  

    private static Logger logger = LoggerFactory.getLogger(Builder.class);
    
    public static void main(String[] args) {
    	logger().info("version: 1");
    	
        System.exit(new Builder().run(args));        
    }
    
    @Override
    protected void prepare(Parser p) {     
        super.prepare(p);
        addOption(p, OPTION_GENERATED_DIR);
        addOption(p, OPTION_TEMPLATE_DIR);
        addOption(p, OPTION_ROOT_PACKAGE);
        addOption(p, OPTION_CATALOG_CONTEXT_PACKAGE);        
//        addOption(p, OPTION_SCHEMA);        
        addOption(p, OPTION_INCLUDE_ONLY_SCHEMAS);
        addOption(p, OPTION_TYPE_MAPPER_IMPLEMENTATION);
        addOption(p, OPTION_ENVIRONMENT_IMPLEMENTATION);
    }
        
    @Override
    protected void init(CommandLine cl) 
        throws ToolConfigurationException, ToolException {
    	
        super.init(cl);
        
        String gen = cl.value(require(cl, OPTION_GENERATED_DIR));
        String tem = cl.value(require(cl, OPTION_TEMPLATE_DIR));
        String pkg = cl.value(require(cl, OPTION_ROOT_PACKAGE));
        String ccp = cl.value(OPTION_CATALOG_CONTEXT_PACKAGE);        
        String tmi = cl.value(OPTION_TYPE_MAPPER_IMPLEMENTATION);
        String eim = cl.value(OPTION_ENVIRONMENT_IMPLEMENTATION);
                
        TypeMapper tym = null;
        
        
        if (tmi == null) {
        	tym = new DefaultTypeMapper();
        }
        else {
        	try {
        		logger().debug("loading type mapper -class: " + tmi);
        		Class<?> tmc = Class.forName(tmi);
        		logger().debug("instantiating type mapper...");        		
        		tym = (TypeMapper) tmc.newInstance();
        		logger().debug("type mapper instantiated");
        	}
        	catch (Exception e) {
        		logger().error(e.getMessage(), e);
        		throw new ToolConfigurationException("Unable to instantiate type mapper: " + tmi + " (" + e.getMessage() + ")", e);
			}
        }     
        
        Environment env = null;
        
        if (eim != null) {
        	Class<? extends Environment> envtype = getEnvironmentType(eim);        
            env = loadEnvironment(envtype);	
        }
        
                
                
        List<String> schemas = cl.values(OPTION_INCLUDE_ONLY_SCHEMAS);
        
        final SchemaFilter sf = createSchemaFilter(schemas);
        setSchemaFilter(sf);
        
        logger().info("schema filter: " + sf);
        	    
	    Catalog cat = getCatalog();
	    	    
	    
	    setCatalog(cat);
	        
        setSourceDir(new File(gen));
        setTemplateDir(new File(tem));
        setRootPackage(pkg);
        setCatalogContextPackage(ccp);
        setTypeMapper(tym);
        setTargetEnvironment(env);
    }

	private Environment loadEnvironment(Class<? extends Environment> envtype) throws ToolException {
		Environment env = null;
		try {
			Method method = envtype.getMethod("environment");
			
			if ((method.getModifiers() & Modifier.STATIC) != Modifier.STATIC) {
				throw new ToolException("Environment implementation must have a static method: environment()");
			}
			
			env = (Environment) method.invoke(null);
		} 
        catch (Exception e) {
        	logger().error(e.getMessage(), e);
        	throw new ToolException(e.getMessage(), e);
		} 
        
		return env;
	}

	private Class<? extends Environment> getEnvironmentType(String eim)
			throws ToolConfigurationException {
		Class<? extends Environment> env = null;
        
        if (eim == null) {
        	env = getImplementation().environment().getClass();
        }
        else {
        	try {
        		logger().debug("loading environment -class: " + eim);        		        		        		
        		Class<?> t = Class.forName(eim);
        		        		
        		Method method = t.getMethod("environment");
        		Object eo = method.invoke(null);
        		Environment e = (Environment) eo;
        		env = e.getClass(); 
        	}
        	catch (Exception e) {
        		logger().error(e.getMessage(), e);
        		throw new ToolConfigurationException("Unable to load environment implementation: " + eim + " (" + e.getMessage() + ")", e);
			}        	
        }
        
		return env;
	}

	private SchemaFilter createSchemaFilter(List<String> schemas) {
		SchemaFilter sf = null;
		
		if (schemas == null) {
        	sf = ALL_SCHEMAS;
        	logger().debug("createSchemaFilter: ALL_SCHEMAS");
        }
        else {
        	final Implementation<?> imp = getImplementation();
        	final IdentifierRules rules = imp.environment().getIdentifierRules();
        	final Comparator<Identifier> icmp = rules.comparator();			

        	final Set<Identifier> ss = new TreeSet<Identifier>(icmp);
        	
        	for (String schema : schemas) {
        		ss.add(rules.toIdentifier(schema));
			}        	
        	
        	sf = new SchemaFilter() {				
				@Override
				public boolean accept(Schema s) {
					Identifier n = s.getUnqualifiedName();
					boolean include = ss.contains(n);
					logger().debug("include schema '" + n.getName() + "' ? " + include);
					return include;
					
				}
			};
			

			logger().debug("createSchemaFilter: only schemas: " + ss);
        }
		
		return sf;
	}
    
    @Override
    protected void run() 
        throws ToolException {
        
        try {        
            Connection c = getConnection();
//            Catalog cat = getCatalog();            
            
//            Environment env = cat.getEnvironment();            
            Implementation<?> impl = getImplementation();
            
            c.setAutoCommit(false);        
            CatalogFactory cf = impl.catalogFactory();          
            getFeatures().installAll(c, cf, false);         
            
            Catalog cat = cf.create(c);
            
            File root = getSourceDir();            
            generateSources(cat, root);
            traverseFiles(root);
            
            // TODO: generate templates:            
//            NamingPolicy np = new DefaultNamingPolicy();            
//            generateTemplates(cat, getTemplateDir(), np);
//            traverseFiles(getTemplateDir());
            
            logger.info("run - exit");
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
    	
    	
        TableMapper tm = getTableMapper();       
        SourceGenerator gen = new SourceGenerator(sourceRoot, getSchemaFilter());
                
        StringBuilder details = new StringBuilder();
        
        if (gen.check(cat, getTypeMapper(), details) > 0) {
        	logger().error(details.toString());        	
        	throw new RuntimeException("Generation failed: " + details);        
        }    	
    	
        final File sourceList = getSourceList(sourceRoot);            
        remove(sourceList);               
                
        Properties current = gen.run(cat, tm, getTypeMapper(), getTargetEnvironment());
        
        IOHelper.doStore(current, sourceList.getPath(), "List of the generated source files");            
    }
    
//	private void generateTemplates(Catalog cat, File templateRoot, NamingPolicy np)
//    	throws QueryException, IOException, XMLStreamException, TransformerException {
//	    final File templateList = getTemplateList(templateRoot);            
//	    remove(templateList);
//	    
//	    TableMapper tm = getTableMapper();
//	    DefaultEntityContext ec = new DefaultEntityContext(cat, null, tm);	    
//	    TemplateGenerator gen = new TemplateGenerator(templateRoot, ec);	    		    		                
//	    Properties current = gen.run(cat, np);
//	    
//	    IOHelper.doStore(current, templateList.getPath(), "List of the generated template files");            
//	}    
    
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

	private TableMapper getTableMapper() {
		if (tableMapper == null) {
			String rp = getRootPackage();
	        String ccp = getCatalogContextPackage();
			tableMapper = new DefaultTableMapper(rp, ccp);
		}

		return tableMapper;
	}

	public SchemaFilter getSchemaFilter() {
		return schemaFilter;
	}

	public void setSchemaFilter(SchemaFilter schemaFilter) {
		this.schemaFilter = schemaFilter;
	}

	public TypeMapper getTypeMapper() {
		return typeMapper;
	}

	public void setTypeMapper(TypeMapper typeMapper) {
		this.typeMapper = typeMapper;
	}

	public Environment getTargetEnvironment() {
		return targetEnvironment;
	}

	public void setTargetEnvironment(Environment targetEnvironment) {
		this.targetEnvironment = targetEnvironment;
	}	
}
