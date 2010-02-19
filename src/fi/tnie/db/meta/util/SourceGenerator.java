/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import fi.tnie.db.DefaultEntityFactory;
import fi.tnie.db.AbstractEntity;
import fi.tnie.db.EntityFactory;
import fi.tnie.db.Person;
import fi.tnie.db.QueryException;
import fi.tnie.db.Person.Attribute;
import fi.tnie.db.expr.AbstractIdentifier;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.impl.pg.PGEnvironment;
import fi.tnie.util.io.IOHelper;


public class SourceGenerator
	extends Tool {
	
	private static Logger logger = Logger.getLogger(SourceGenerator.class);
		
	public static final String KEY_PACKAGE = "package";
	public static final String KEY_SOURCE_ROOT_DIR = "root-dir";
	

	public static void main(String[] args) {
		
		try {
			new SourceGenerator().run(args);			
		}			
		catch (Exception e) {
			logger().error(e.getMessage(), e);
		}
	}
		
	
	private static boolean knownAbbr(String t) {
		return t.equals("url") || t.equals("http") || t.equals("xml");
	}

	private static boolean knownProposition(String t) {
		return 
			t.equals("of") || t.equals("in") || t.equals("with") || 
			t.equals("at") || t.equals("for");
	}

	public static Logger logger() {
		return SourceGenerator.logger;
	}
	
	@Override
	public void run(Connection c, Properties config) 
		throws QueryException, IOException {
		
		try {
			String pkg = config.getProperty("package");
			
			String r = config.getProperty("root-dir");
			File root = (r == null) ? new File(".") : new File(r);
							
			if (!root.isDirectory()) {
				throw new IllegalArgumentException("No root directory: " + root.getAbsolutePath());
			}		
					
			PGEnvironment env = new PGEnvironment();
			CatalogFactory cf = env.catalogFactory();
			DatabaseMetaData meta = c.getMetaData();
			Catalog catalog = cf.create(meta, c.getCatalog());
			
			for (Schema s : catalog.schemas().values()) {
				process(s, root, pkg);
			}			
		}
		catch (SQLException e) {
			throw new QueryException(e.getMessage(), e);
		}
		
	}

	private void process(Schema s, final File root, final String rpkg) 
		throws IOException {
		
		String name = s.getUnqualifiedName().getName().toLowerCase();
		
		if (name.equals("public")) {
			name = "pub";
		}
		
		String pkg = (rpkg == null) ? name : rpkg + "." + name;
		final File pd = packageDir(root, pkg);
				
		
		for (BaseTable t : s.baseTables().values()) {
			String n = t.getName().getUnqualifiedName().getName();
			final String etype = translate(n);
			
			CharSequence source = generate(t, pkg, etype);
			
												
			IOHelper.write(source, getSourceFile(pd, etype));			
		}						
	}

	private CharSequence generate(BaseTable t, String pkg, String uname) {
		StringBuffer content = new StringBuffer();
		
		if (pkg != null) {			
			content.append("package ");			
			content.append(pkg);
			content.append(";\n\n");
		}
		
		content.append("import fi.tnie.db.Entity;\n");
		content.append("import fi.tnie.db.EntityFactory;\n");
		content.append("import fi.tnie.db.DefaultEntityFactory;\n");
		content.append("import fi.tnie.db.meta.Column;\n");
						
		content.append("\n\n");
		
		content.append("public class ");
		content.append(uname);		
		content.append("\n"); 
		indent(1, content);
		content.append("extends ");
		content.append("Entity<");
		content.append(getAttributeType(uname));
		content.append(", ");
		content.append(uname);
		content.append(">");						
		content.append(" ");
		content.append("{");
		content.append("\n\n");
		
		members(t, uname, content);		
		
		content.append("\n}\n");
				
		return content;
	}
	
	private void members(BaseTable t, String etype, StringBuffer content) {
		
		attrs(t, content);
		content.append("\n\n");		
		factory(etype, content);
		content.append("\n\n");
		
	}


	private void factory(String uname, StringBuffer content) {
		String ivfact = "factory";
		
		String atype = getAttributeType();
		
		content.append("private static EntityFactory<" + atype + ", " + uname + "> " + ivfact + ";\n\n");		
		
		content.append("@Override\n");		
		
		content.append(
		"public EntityFactory<" + atype + ", " + uname + "> getFactory() {\n" +
		"  if (factory == null) {\n" +
		"    factory = new DefaultEntityFactory<" + atype + ", " + uname + ">() {\n" +
		"      @Override\n" +
		"      public " + uname + " newInstance() {\n" +
		"        return new " + uname + "();\n" +
		"      }\n" +
		"    };\n" +
		"  }\n\n" +
		"  return factory;\n" +
		"}\n\n");
			
		
	}


	private void attrs(BaseTable t, StringBuffer content) {								
		content.append("public enum ");
		content.append(getAttributeType());
		content.append(" {\n");
						 
		Set<Identifier> fkcols = foreignKeyColumns(t);
				
		for (Column c : t.columns()) {		
			// only non-fk-columns are included in attributes.
			// fk-columns  are not intended to be set individually,
			// but atomically with 'ref -method
			if (!fkcols.contains(c.getColumnName())) {
				content.append(attr(c));			
				content.append(",\n");				
			}			
		}
		
		content.append("}\n");
	}
	
	private Set<Identifier> foreignKeyColumns(BaseTable t) {
		Comparator<Identifier> icmp = t.getSchema().getCatalog().getEnvironment().identifierComparator();
		Set<Identifier> cs = new TreeSet<Identifier>(icmp);
		
		logger().debug("table: " + t.getQualifiedName());
				
		for (ForeignKey fk : t.foreignKeys().values()) {
			for (Column c : fk.columns().keySet()) {
				cs.add(c.getUnqualifiedName());
			}			
		}
		
		return cs;
	}


	private String attr(Column c) {
		ColumnName n = c.getColumnName();
		
		String attr = n.getName().toUpperCase();		
		
		if (!n.isOrdinary()) {
			attr = attr.replace(' ', '_');
		}
		
		return attr;
	}

	private void indent(int indentLevel, StringBuffer dest) {
		String indent = "  ";
		
		for (int level = 0; level < indentLevel; level++) {
			dest.append(indent);			
		}
	}


	private File getSourceFile(File pd, String etype) {		
		return new File(pd, etype + ".java");
	}


	private String translate(String n) {
		String[] tokens = n.split("_");
		
		StringBuffer buf = new StringBuffer();
		
		for (int i = 0; i < tokens.length; i++) {
			capitalize(tokens[i], buf);			
		}
		
		return buf.toString();
	}
	
	private void capitalize(String t, StringBuffer dest) {		
		dest.append(Character.toUpperCase(t.charAt(0)));
		
		if (t.length() > 1) {
			dest.append(t.substring(1).toLowerCase());
		}		
	}


	private File packageDir(File root, String pkg) 
		throws IOException {
		if (pkg == null) {
			return root;
		}
						
		String[] elems = pkg.split(Pattern.quote("."));
		
		StringBuffer path = new StringBuffer(elems[0]); 
		
		for (int i = 1; i < elems.length; i++) {
			path.append(File.separatorChar);	
			path.append(elems[i]);
		}
		
		File pd = new File(root, path.toString());
		
		pd.mkdirs();
		
		if (!pd.isDirectory()) {
			throw new IOException("unable to create directory: " + pd.getPath());			
		}
			
		return pd;
	}
	

	private String qualifiedName(String pkg, String uname) {
		return pkg == null || pkg.equals("") ? uname : pkg + "." + uname;
	}
	
	public String getAttributeType(String uname) {
		return uname + "." + getAttributeType();
	}
	
	public String getAttributeType() {
		return "Attribute";		
	}
	
}
