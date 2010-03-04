/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import fi.tnie.db.DefaultTableMapper;
import fi.tnie.db.QueryException;
import fi.tnie.db.TableMapper;
import fi.tnie.db.TableMapper.Part;
import fi.tnie.db.TableMapper.Type;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.CatalogMap;
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
			
			TableMapper tm = new DefaultTableMapper(pkg); 
					
			PGEnvironment env = new PGEnvironment();
			CatalogFactory cf = env.catalogFactory();
//			DatabaseMetaData meta = c.getMetaData();
			
			CatalogMap cm = cf.create(c);
			
//			Catalog catalog = cf.create(meta, c.getCatalog());
									
			for (Catalog cat : cm.values()) {
				for (Schema s : cat.schemas().values()) {
					process(s, root, tm);
				}				
			}
			

		}
		catch (SQLException e) {
			throw new QueryException(e.getMessage(), e);
		}
		
	}

	private void process(Schema s, final File root, final TableMapper tm) 
		throws IOException {
		
		for (BaseTable t : s.baseTables().values()) {
			Map<Part, Type> types = tm.entityMetaDataType(t);
			
			TableMapper.Type intf = types.get(TableMapper.Part.INTERFACE);
			
			if (intf != null) {
				CharSequence source = generateInterface(t, intf);
				write(root, intf, source);
			}						
			
			TableMapper.Type impl = types.get(TableMapper.Part.IMPLEMENTATION);
			
			if (impl != null) {
				CharSequence source = generateImplementation(t, types);
				write(root, impl, source);	
			}
		}						
	}	
	
	
	private void write(File root, TableMapper.Type type, CharSequence source) 
		throws IOException {
		File pd = packageDir(root, type.getPackageName());				
		IOHelper.write(source, getSourceFile(pd, type.getUnqualifiedName()));
		
	}

	public CharSequence generateInterface(BaseTable t, TableMapper.Type mt) {
				
		
		StringWriter sw = new StringWriter();
		PrintWriter p = new PrintWriter(sw);
		
		String pkg = mt.getPackageName();
		
		if (pkg != null) {			
			p.print("package ");			
			p.print(pkg);
			p.println(";");
		}
		
		p.println("import fi.tnie.db.Entity;");
		p.println("import fi.tnie.db.Identifiable;");
						
		p.println();
		p.println();
		
		final String uname = mt.getUnqualifiedName();
		
		p.print("public interface ");
		p.println(uname);		
		p.print("\textends Entity<");		
		p.print(getAttributeType(uname));
		p.print(", ");
		p.print(getReferenceType(uname));
		p.print(", ");
		p.print(getQueryType(uname));
		p.print(", ");
		p.print(uname);
		p.println(">");
		p.println("{");
		p.println();
		
		p.print(interfaceMembers(t, uname));		
		
		p.println("\n}\n");				
		p.close();
		
		logger().debug("interface: " + sw);
		
		return sw.toString();
	}
	
	public CharSequence generateImplementation(BaseTable t, Map<Part, Type> types) {
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
				
		Type ifp = types.get(Part.INTERFACE);
		Type imp = types.get(Part.IMPLEMENTATION);	
		
		String intf = null;
		
		if (ifp != null) {
			intf = ifp.getQualifiedName();	
		}
		
		String ctype = (intf == null) ? imp.getUnqualifiedName() : intf;
		
		String pkg = imp.getPackageName();
		
		if (pkg != null) {			
			w.print("package ");
			w.print(pkg);
			w.println(";");
			w.println();
		}
	
		w.println("import fi.tnie.db.DefaultEntity;");
		w.println("import fi.tnie.db.DefaultEntityMetaData;");
		w.println("import fi.tnie.db.EntityException;");
		w.println("import fi.tnie.db.EntityFactory;");
												
		w.println();
		w.println();
						
		w.print("public class ");
		w.println(imp.getUnqualifiedName());		
		w.print("\textends DefaultEntity<");
		w.print(getAttributeType(ctype));
		w.print(",");
		w.print(getReferenceType(ctype));
		w.print(",");
		w.print(getQueryType(ctype));
		w.print(",");
		w.print(ctype);		
		w.println(">");
		
		if (intf != null) {		
			w.print("\timplements ");
			w.println(intf);
		}
		
		w.println("{");		
		w.print(implMembers(t, types));
		w.print("\n}\n");
		w.close();
				
		return sw.toString();
	}

	
	private String interfaceMembers(BaseTable t, String etype) {
		
		StringBuffer content = new StringBuffer();
		
		attrs(t, content);
		content.append("\n\n");
		
		refs(t, content);
		content.append("\n\n");		

		queries(t, content);
		content.append("\n\n");		

//		factory(etype, content);
//		content.append("\n\n");
		
		logger().debug("members: " + content);
		
		return content.toString();
	}

	private String implMembers(BaseTable t, Map<Part, Type> parts) {
		
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		
		final Type ctype = getContainerType(parts);
		final String cn = getContainerTypeName(parts);
		
		String at = getAttributeType(ctype.getUnqualifiedName());
		String rt = getReferenceType(ctype.getUnqualifiedName());
		String qt = getQueryType(ctype.getUnqualifiedName());
		
		String imp = parts.get(Part.IMPLEMENTATION).getUnqualifiedName();
		
		String metaDataType = ctype.getUnqualifiedName() + "MetaData";
						
		w.println("public static class " + metaDataType);
		w.println("	extends DefaultEntityMetaData");
		w.println("	<");
		w.println("		" + at + ", ");
		w.println("		" + rt + ", ");
		w.println("		" + qt + ", ");
		w.println("		" + cn + "> {");
		w.println("");
		
		w.println("	private static " + metaDataType + " instance = new " + metaDataType + "();");
		
		w.println("	public " + metaDataType + "() {");
		w.println("		super(" + at + ".class, " + rt + ".class,	" + qt + ".class);");
		w.println("	}");
		w.println("");
		w.println("	@Override");
		w.println("	public EntityFactory<" + at + ", " + rt + ", " + qt + ", " + cn + "> getFactory() {			");
		w.println("		return new EntityFactory<" + at + ", " + rt + ", " + qt + ", " + cn + ">() {");
		w.println("			@Override");
		w.println("			public " + cn + " newInstance()");
		w.println("						throws EntityException {");
		w.println("					return new " + imp + "();");
		w.println("				}		");
		w.println("		};");
		w.println("	}");
		w.println("}");
		w.println("");
		w.println("public " + imp + "() {");		
		w.println("}");
		w.println("");
		w.println("	@Override		");
		w.println("	public " + metaDataType + " getMetaData() {		");
		w.println("		return " + metaDataType + ".instance;");
		w.println("	}");		
		
		w.println("");
		w.println("	@Override		");
		w.println("	public " + ctype.getUnqualifiedName() + " self() {		");
		w.println("		return this;");
		w.println("	}");		
				
		w.close();
		
		return sw.toString();
	}

//	private void factory(String uname, StringBuffer content) {
////		String ivfact = "factory";
////		
////		String atype = getAttributeType();
////		
////		content.append("private static EntityFactory<" + atype + ", " + uname + "> " + ivfact + ";\n\n");		
////		
////		content.append("@Override\n");		
////		
////		content.append(
////		"public EntityFactory<" + atype + ", " + uname + "> getFactory() {\n" +
////		"  if (factory == null) {\n" +
////		"    factory = new DefaultEntityFactory<" + atype + ", " + uname + ">() {\n" +
////		"      @Override\n" +
////		"      public " + uname + " newInstance() {\n" +
////		"        return new " + uname + "();\n" +
////		"      }\n" +
////		"    };\n" +
////		"  }\n\n" +
////		"  return factory;\n" +
////		"}\n\n");
////			
////		
//	}


	private void attrs(BaseTable t, StringBuffer content) {								
//		content.append("public enum ");
//		content.append(getAttributeType());
//		content.append(" {\n");
				
		List<String> elements = new ArrayList<String>();
		
		Set<Identifier> fkcols = foreignKeyColumns(t);
				
		for (Column c : t.columns()) {		
			// only non-fk-columns are included in attributes.
			// fk-columns  are not intended to be set individually,
			// but atomically with ref -method
			if (!fkcols.contains(c.getColumnName())) {
				elements.add(attr(c));
			}			
		}
		
		content.append(enumMember(getAttributeType(), elements));		
	}
	
	private void refs(BaseTable t, StringBuffer content) {								
		List<String> elements = new ArrayList<String>();	
			
		for (ForeignKey fk : t.foreignKeys().values()) {		
			elements.add(format(fk));
		}
		
		content.append(enumMember(getReferenceType(), elements));		
	}	
	
	private String format(ForeignKey fk) {		
		final String kn = fk.getUnqualifiedName().getName();
		String t = fk.getReferencing().getUnqualifiedName().getName().toUpperCase();
		
		String p = "^(FK_)?(" + Pattern.quote(t) + "_)";
		
		logger().debug("input {" + kn.toUpperCase() + "}");
		logger().debug("p {" + p + "}");
		
		String n = kn.toUpperCase().replaceFirst(p, "");
		
		String expr;
		
		if (n.equals(kn)) {
			expr = n;
		}
		else {
			StringBuffer buf = new StringBuffer(n);
			buf.append("(");
			buf.append('"');
			buf.append(kn);
			buf.append('"');
			buf.append(")");
			
			expr = buf.toString();
		}
		
		return expr;
	}
	
	
	private void queries(BaseTable t, StringBuffer content) {								
		List<String> elements = new ArrayList<String>();	
			
		// TODO:
//		for (ForeignKey fk : t.foreignKeys().values()) {		
//			elements.add(fk.getUnqualifiedName().getName());
//		}
		
		content.append(enumMember(getQueryType(), elements));		
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

//	private String qualifiedName(String pkg, String uname) {
//		return pkg == null || pkg.equals("") ? uname : pkg + "." + uname;
//	}
	
	public String getAttributeType(String uname) {
		return uname + "." + getAttributeType();
	}
	
	public String getReferenceType(String uname) {
		return uname + "." + getReferenceType();
	}
	
	public String getQueryType(String uname) {
		return uname + "." + getQueryType();
	}
	
	public String getAttributeType() {
		return "Attribute";		
	}
	
	public String getReferenceType() {
		return "Reference";		
	}

	
	public String getQueryType() {
		return "Query";		
	}
	
	private <E> String enumMember(String uname, Iterable<E> elements) {

		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
						
		w.print("public enum ");
		w.print(uname);
		w.print(" implements Identifiable ");		
		w.println(" {");
		
		for (Object e : elements) {
			w.print("\t");
			w.print(e.toString());
			w.println(",");
		}		
		
		w.println(";");
		w.println();
				 		
		w.println("\tprivate String identifier;");
		w.println();
		w.println("\t" +  uname + "() {}");
		w.println();
		w.println("\t" + uname + "(String identifier) {");
		w.println("\tthis.identifier = identifier;");
		w.println("\t}");
		w.println();
						
		w.println("\t@Override");
		w.println("\tpublic String identifier() {");		
		w.println("\t\treturn (identifier == null) ? name() : identifier;");
		w.println("\t}");
				
		w.println("}");		
		w.println();
		w.close();
		
		return sw.toString();
	}


	private String getContainerTypeName(Map<Part, Type> types) {
		Type ifp = types.get(Part.INTERFACE);
		Type imp = types.get(Part.IMPLEMENTATION);	
		
		String intf = null;
		
		if (ifp != null) {
			intf = ifp.getQualifiedName();	
		}
		
		return (intf == null) ? imp.getUnqualifiedName() : intf;		
	}
	
	private Type getContainerType(Map<Part, Type> types) {
		Type intf = types.get(Part.INTERFACE);
		Type impl = types.get(Part.IMPLEMENTATION);	
	
		return (intf == null) ? impl : intf;		
	}
	
	
	
}
