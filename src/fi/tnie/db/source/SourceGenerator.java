/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.source;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import fi.tnie.db.QueryException;
import fi.tnie.db.TableMapper;
import fi.tnie.db.TableMapper.Part;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.Schema;
import fi.tnie.util.io.IOHelper;

public class SourceGenerator {

    /**
     * Pattern which is replaced with the simple name of the table interface in template files.
     */
    private static final String PATTERN_TABLE_INTERFACE = "{{table-interface}}";

    /**
     * Pattern which is replaced with the simple name of the abstract class in template files.
     */
    private static final String PATTERN_TABLE_ABSTRACT = "{{table-abstract-class}}";

    /**
     * Pattern which is replaced with the simple name of the hook class in template files.
     */
    private static final String PATTERN_TABLE_HOOK = "{{table-hook-class}}";

    /**
     * Pattern which is replaced with the simple name of the catalog context class in template files.
     */
    private static final String PATTERN_CATALOG_CONTEXT_CLASS = "{{catalog-context-class}}";
    
    /**
     * Pattern which is replaced with the package name of the tyep being generated in template files.
     */
    private static final String PATTERN_PACKAGE = "{{package-name}}";    
    
    /**
     * Pattern which is replaced with the list of imports.
     */
    private static final String PATTERN_IMPORTS = "{{imports}}";     

    /**
     * TODO: add constants for all patterns
     */

    /**
     * TODO:
     * Much of the generate<X> -code should be same for all the parts.
     *
     * Split code generation in to two distinct phases:
     *
     * 1) generate all dynamic content
     * 2) perform replacements for each generated type.
     *
     * In this way we don't have to care which pattern is used in which template.
     */

	private static Logger logger = Logger.getLogger(SourceGenerator.class);
	
	private File defaultSourceDir;	
	private EnumMap<Part, File> sourceDirMap;

	private Map<Class<?>, Class<?>> wrapperMap;

	@SuppressWarnings("serial")
    private static class TypeInfo
	    extends EnumMap<Part, JavaType> {

        public TypeInfo() {
            super(Part.class);
        }
	}
	
	public SourceGenerator(File defaultSourceDir) {
        super();
        this.defaultSourceDir = defaultSourceDir;
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
	
    public Properties run(Connection c, Catalog cat, TableMapper tm)
        throws QueryException, IOException {
        
        Map<File, String> gm = new HashMap<File, String>();
        Properties generated = new Properties();
         
        List<JavaType> ccil = new ArrayList<JavaType>();
        Map<JavaType, CharSequence> fm = new HashMap<JavaType, CharSequence>();

        for (Schema s : cat.schemas().values()) {
            process(s, tm, ccil, fm, generated, gm);
        }

        List<String> il = new ArrayList<String>();

        for (JavaType t : ccil) {
            if (t != null) {
                il.add(t.getQualifiedName());
            }
        }    

        JavaType cc = tm.catalogContextType();
        CharSequence src = generateContext(cc, tm, il, fm);            
        write(getSourceDir(), cc, src, generated, gm);
        
        return generated;        
    }

    private CharSequence generateContext(JavaType cc, TableMapper tm,
            Collection<String> il, Map<JavaType, CharSequence> fm) throws IOException {

        String src = getTemplateForCatalogContext();

        src = replacePackageAndImports(src, cc, il);
        src = replaceAll(src, PATTERN_CATALOG_CONTEXT_CLASS, cc.getUnqualifiedName());

        StringBuffer buf = new StringBuffer();

        for (Map.Entry<JavaType, CharSequence> e : fm.entrySet()) {
            buf.append(formatSchemaFactoryMethod(e.getKey(), e.getValue()));
        }

        src = replaceAll(src, "{{factory-method-list}}", buf.toString());

        return src;
    }

    private Object formatSchemaFactoryMethod(JavaType key, CharSequence value) {
        String n = key.getUnqualifiedName();
        return "public " + n + " new" + n + "() { return " + value + " } ";
    }


    private String getTemplateForCatalogContext() throws IOException {
        return read("CATALOG_CONTEXT.in");
    }


    private void process(Schema s, final TableMapper tm, Collection<JavaType> ccil, Map<JavaType, CharSequence> factories, Properties generated, Map<File, String> gm) 
		throws IOException {

	    List<TypeInfo> types = new ArrayList<TypeInfo>();

		for (BaseTable t : s.baseTables().values()) {
		    final JavaType intf = tm.entityType(t, Part.INTERFACE);
		    final JavaType at = tm.entityType(t, Part.ABSTRACT);
		    final JavaType hp = tm.entityType(t, Part.HOOK);
		    final JavaType impl = tm.entityType(t, Part.IMPLEMENTATION);
		    
		    if (intf == null || impl == null) {
		        continue;
		    }
		    else {
		        final Schema schema = t.getSchema();
		        
		        {
                    CharSequence source = generateInterface(t, intf, tm);                    
                    File root = getSourceDir(schema, Part.INTERFACE);
//                    logger().debug("interface: " + source);                   
                    write(root, intf, source, generated, gm);
		        }

                if (at != null) {
                   CharSequence source = generateAbstract(t, at, tm);
                   File root = getSourceDir(schema, Part.ABSTRACT);
                   write(root, at, source, generated, gm);
                }

                if (hp != null) {
                    CharSequence source = generateHook(t, hp, tm);

                    if (source != null) {
                        File root = getSourceDir(schema, Part.HOOK);
                        File sourceFile = getSourceFile(root, hp);

                        if (!sourceFile.exists()) {
                            write(root, hp, source, generated, gm);
                        }
                    }
                }

                {
                    CharSequence source = generateImplementation(t, impl, tm);
                    File root = getSourceDir(schema, Part.IMPLEMENTATION);
                    write(root, impl, source, generated, gm);
                }

                TypeInfo info = new TypeInfo();

                info.put(Part.INTERFACE, intf);
                info.put(Part.ABSTRACT, at);
                info.put(Part.HOOK, hp);
                info.put(Part.IMPLEMENTATION, impl);

                types.add(info);
		    }
		}

		{
            final JavaType intf = tm.factoryType(s, Part.INTERFACE);
//            final JavaType impl = tm.factoryType(s, Part.IMPLEMENTATION);

            if (intf != null) {
                if (types.isEmpty()) {

                }
                else {
                    File root = getSourceDir(s, Part.INTERFACE);
                    write(root, intf, generateFactoryInterface(s, intf, tm, types), generated, gm);

    //                CharSequence src = generateFactoryImplementation(s, impl, tm, types);
                    CharSequence src = generateAnonymousFactoryImplementation(s, tm, types);
                    factories.put(intf, src);

                    ccil.add(intf);
                    // ccil.add(impl);

                    for (TypeInfo info : types) {
                        ccil.add(getFactoryMethodReturnType(info));
                        ccil.add(info.get(Part.IMPLEMENTATION));
                    }
                }
            }
        }
	}

    private File getSourceDir(Schema s, Part part) {
        return getSourceDir(part);
    }

    private File getSourceFile(File root, JavaType type)
	    throws IOException {
        File pd = packageDir(type.getPackageName());        
        pd = (pd == null) ? root : new File(root, pd.getPath());     
	    return getSourceFile(pd, type.getUnqualifiedName());
	}

	private void write(File root, JavaType type, CharSequence source, Properties dest, Map<File, String> files) 
		throws IOException {
	    
	    String pkg = type.getPackageName();
	    File pd = packageDir(pkg);

        // We want 'sf' to appear into 'dest'
        // relative to 'root', not relative to the current dir
	    File sf = getSourceFile(pd, type.getUnqualifiedName());
	    
	    File out = new File(root, sf.getPath());
	    
	    mkdirs(out.getParentFile(), pkg, dest);	    	    
		IOHelper.doWrite(source, out);
		String k = type.getQualifiedName();
						
		dest.put(k, sf.getPath());
		files.put(sf, k);
	}

	private void mkdirs(File pd, String pkg, Properties dest) 
	    throws IOException {
        if (!pd.exists()) {
            pd.mkdirs();
            
            if (!pd.isDirectory()) {
                throw new IOException("unable to create directory: " + pd.getPath());
            }
            
            dest.put(pkg, pd.getPath());
        }        
    }

    public CharSequence generateInterface(BaseTable t, JavaType mt, TableMapper tm)
	    throws IOException {

	    String src = getTemplateFor(Part.INTERFACE);

	    final String et = getEnumTemplate();

	    src = replacePackageAndImports(src, mt);

	    src = replaceAll(src, PATTERN_TABLE_INTERFACE, mt.getUnqualifiedName());

	    {
    	    String type = createEnumType(et, "Attribute", attrs(t));
    	    src = replaceAll(src, "{{attribute-name-type}}", type);
	    }

        {
            String type = createEnumType(et, "Reference", refs(t));
            src = replaceAll(src, "{{reference-name-type}}", type);
        }

        {
            String type = createEnumType(et, "Query", queries(t));
            src = replaceAll(src, "{{query-name-type}}", type);
        }

        {
            String code = accessors(t, tm, false);
            src = replaceAll(src, "{{abstract-accessor-list}}", code);
        }

	    return src;
	}

	public CharSequence generateFactoryInterface(Schema s, JavaType factoryType, TableMapper tm, Collection<TypeInfo> types)
       throws IOException {

	   String src = getFactoryTemplateFor(Part.INTERFACE);

	   src = replacePackageAndImports(src, factoryType);

       src = replaceAll(src, "{{schema-factory}}", factoryType.getUnqualifiedName());

       StringBuffer code = new StringBuffer();

       for (TypeInfo info : types) {
           String m = formatFactoryMethod(info, false);
           a(code, m, 1);
       }

       src = replaceAll(src, "{{factory-method-list}}", code.toString());


       logger().debug("factory intf: " + src);

       return src;
   }

    private CharSequence generateAnonymousFactoryImplementation(Schema s, TableMapper tm, Collection<TypeInfo> types)
        throws IOException {

        JavaType intf = tm.factoryType(s, Part.INTERFACE);
        String src = getAnonymousFactoryTemplate();

        src = replaceAll(src, "{{schema-factory}}", intf.getUnqualifiedName());

        StringBuffer code = new StringBuffer();

        for (TypeInfo t : types) {
            String m = formatFactoryMethod(t, true);
            a(code, m, 1);
        }

        src = replaceAll(src, "{{factory-method-list}}", code.toString());

        logger().debug("factory impl: " + src);

        return src;
    }

//    private CharSequence generateFactoryImplementation(Schema s, JavaType impl, TableMapper tm, Collection<TypeInfo> types)
//        throws IOException {
//
//        JavaType intf = tm.factoryType(s, Part.INTERFACE);
//        String src = getFactoryTemplateFor(Part.IMPLEMENTATION);
//
//        List<String> il = new ArrayList<String>();
//
//        addImport(impl, intf, il);
//
//        for (TypeInfo t : types) {
//            JavaType returnType = getFactoryMethodReturnType(t);
//            addImport(impl, returnType, il);
//        }
//
//        src = replacePackageAndImports(src, impl, il);
//
//        src = replaceAll(src, "{{schema-factory-impl}}", impl.getUnqualifiedName());
//        src = replaceAll(src, "{{schema-factory}}", intf.getUnqualifiedName());
//
//        StringBuffer code = new StringBuffer();
//
//        for (TypeInfo t : types) {
//            String m = formatFactoryMethod(t, true);
//            a(code, m, 1);
//        }
//
//        src = replaceAll(src, "{{factory-method-list}}", code.toString());
//
//        logger().debug("factory impl: " + src);
//
//        return src;
//    }


    private JavaType getFactoryMethodReturnType(TypeInfo info) {
        JavaType hp = info.get(Part.HOOK);
        JavaType itfp = info.get(Part.INTERFACE);
        JavaType at = info.get(Part.ABSTRACT);

        return (hp != null) ? hp : (at != null) ? at : itfp;
    }


    private String formatFactoryMethod(TypeInfo info, boolean impl) {
        JavaType ap = info.get(Part.ABSTRACT);
        JavaType impp = info.get(Part.IMPLEMENTATION);
        JavaType returnType = getFactoryMethodReturnType(info);

        String signature = returnType.getUnqualifiedName() + " new" + ap.getUnqualifiedName() + "()";
        String src = null;

        if (!impl) {
            src = signature + ";";
        }
        else {
            src = "public " + signature + " { return new " + impp.getUnqualifiedName() + "(); } ";
        }

        return src;
    }

    private String getFactoryTemplateFor(Part p)
	    throws IOException {
        return read("FACTORY_" + p.toString() + ".in");
    }

    private String getAnonymousFactoryTemplate()
        throws IOException {
        return read("ANONYMOUS_FACTORY_IMPLEMENTATION.in");
    }

	private String read(String resource)
	    throws IOException {
        InputStream template = getClass().getResourceAsStream(resource);
        String src = new IOHelper().read(template, "UTF-8", 1024);
        return src;
	}


    private String createEnumType(String template, String name, String constants) {
        String src = replaceAll(template, "{{enum-type}}", name);
        src = replaceAll(src, "{{enum-constants}}", constants);
        return src;
	}

	private String getEnumTemplate() throws IOException {
       return read("enum-type.in");
   }

	private String getTemplateFor(Part p)
	    throws IOException {
        return read(p.toString() + ".in");
	}

	private String imports(Collection<String> imports) {
	    StringBuffer src = new StringBuffer();

	    TreeSet<String> ts = new TreeSet<String>(imports);

	    for (String imp : ts) {
	        if (imp != null) {
                src.append("import ");
                src.append(imp);
                src.append(";\n");
	        }
        }

	    return src.toString();
	}

	private String replaceAll(String text, String pattern, String replacement) {
	    if (replacement == null) {
	        return text;
	    }

	    String qp = Pattern.quote(pattern);
        return text.replaceAll(qp, replacement);
    }


    public CharSequence generateAbstract(BaseTable t, JavaType mt, TableMapper tm)
        throws IOException {

        String src = getTemplateFor(Part.ABSTRACT);
        JavaType intf = tm.entityType(t, Part.INTERFACE);

        List<String> implist = new ArrayList<String>();
        addImport(mt, intf, implist);

        src = replacePackageAndImports(src, mt, implist);

        src = replaceAll(src, PATTERN_TABLE_ABSTRACT, mt.getUnqualifiedName());
        src = replaceAll(src, PATTERN_TABLE_INTERFACE, intf.getUnqualifiedName());

        return src;
	}

	public CharSequence generateHook(BaseTable t, JavaType mt, TableMapper tm)
	    throws IOException {
        String src = getTemplateFor(Part.HOOK);

        final JavaType intf = tm.entityType(t, Part.INTERFACE);
        final JavaType base = tm.entityType(t, Part.ABSTRACT);

        List<String> implist = new ArrayList<String>();

        addImport(mt, intf, implist);
        addImport(mt, base, implist);

        src = replacePackageAndImports(src, mt, implist);

        src = replaceAll(src, PATTERN_TABLE_HOOK, mt.getUnqualifiedName());
        src = replaceAll(src, "{{table-hook-base-class}}", base.getUnqualifiedName());

        return src;
	}

	private void addImport(JavaType type, JavaType referenced, List<String> importList) {
	    if (referenced == null ||
	        referenced.getPackageName().equals(type.getPackageName())) {
	        return;
	    }
	    importList.add(referenced.getQualifiedName());
	}

	private CharSequence generateImplementation(BaseTable t, JavaType impl, TableMapper tm)
	    throws IOException {

        String src = getTemplateFor(Part.IMPLEMENTATION);

        JavaType intf = tm.entityType(t, Part.INTERFACE);
        JavaType base = tm.entityType(t, Part.HOOK);

        if (base == null) {
            base = tm.entityType(t, Part.ABSTRACT);
        }

        List<String> il = new ArrayList<String>();
        addImport(impl, intf, il);
        addImport(impl, base, il);

        src = replacePackageAndImports(src, impl, il);

        src = replaceAll(src, PATTERN_TABLE_INTERFACE, intf.getUnqualifiedName());
        src = replaceAll(src, "{{table-impl-class}}", impl.getUnqualifiedName());
        src = replaceAll(src, "{{table-impl-base}}", base.getUnqualifiedName());

        {
            String code = accessors(t, tm, true);
            src = replaceAll(src, "{{accessor-list}}", code);
        }

        return src;
	}

	private String accessors(BaseTable t, TableMapper tm, boolean impl) {
	    StringBuffer content = new StringBuffer();
	    accessors(t, content, tm, impl);
	    return content.toString();
	}

	private void accessors(BaseTable t, StringBuffer content, TableMapper tm, boolean impl) {
        Set<Identifier> fkcols = foreignKeyColumns(t);

        for (Column c : t.columns()) {
            // only non-fk-columns are included in attributes.
            // fk-columns  are not intended to be set individually,
            // but atomically with ref -methods
            if (!fkcols.contains(c.getColumnName())) {
                Class<?> jt = tm.getAttributeType(t, c);

                if (jt != null) {
                    String code = formatAccessors(c, jt, impl);
                    content.append(code);
                }
            }
        }
    }

    private String formatAccessors(Column c, Class<?> attributeType, boolean impl) {
        final String attributeName = attr(c);
        final String type = attributeType.getName();

        StringBuffer nb = new StringBuffer();
        final String n = name(c.getColumnName().getName());

        a(nb, "public ");
        a(nb, type);
        a(nb, " get");
        a(nb, n);
        a(nb, "()");


        if (!impl) {
            a(nb, ";", 1);
        }
        else {
            // getter implementation
            a(nb, " {", 1);

            if (!attributeType.isPrimitive()) {
                a(nb, "return ");
                // call super & cast:
                expr(nb, attributeType, attributeName);
                a(nb, ";", 1);
            }
            else {
                Class<?> wt = wrapper(attributeType);
                a(nb, wt.getName());

                a(nb, " o = ");
                // call super & cast:
                expr(nb, attributeType, attributeName);
                a(nb, ";", 2);

                a(nb, "return (o == null) ? ");

                // default value:
                if (attributeType.equals(Boolean.TYPE)) {
                    a(nb, " false ");
                }
                else {
                    // TODO: should we try to parse the column default value here?
                    a(nb, " 0 ");
                }

                a(nb, " : ");
                a(nb, "o.intValue()");
                a(nb, ";", 1);
            }

            a(nb, "}", 2);
        }

        a(nb, "public void set");
        a(nb, n);
        a(nb, "(");
        a(nb, type);
        a(nb, " ");
        a(nb, "newValue)");

        if (!impl) {
            a(nb, ";", 1);
        }
        else {
            a(nb, " {", 1);
            a(nb, "set(");
            a(nb, getAttributeType());
            a(nb, ".");
            a(nb, attributeName);
            a(nb, ", ");

            if (!attributeType.isPrimitive()) {
                a(nb, "newValue");
            }
            else {
                Class<?> wt = wrapper(attributeType);
                a(nb, wt.getName());
                a(nb, ".valueOf(");
                a(nb, "newValue");
                a(nb, ")");
            }
            a(nb, ");", 1);
            a(nb, "}", 2);
        }

        return nb.toString();
    }

    private void expr(StringBuffer sb, Class<?> attributeType, String attributeName) {
        Class<?> castTo = attributeType.isPrimitive() ?
                wrapper(attributeType) :
                attributeType;

        sb.append("(");
        sb.append(castTo.getName());
        sb.append(") super.get(");
        sb.append(getAttributeType());
        sb.append(".");
        sb.append(attributeName);
        sb.append(")");
    }

    public String name(String name) {
        int len = name.length();
        StringBuffer nb = new StringBuffer(len);
        boolean upper = true;

        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);

            if (c == '_') {
                upper = true;
                continue;
            }

            nb.append(upper ? Character.toUpperCase(c) : Character.toLowerCase(c));
            upper = false;
        }

        return nb.toString();
    }

    private String attrs(BaseTable t) {
        StringBuffer buf = new StringBuffer();
        attrs(t, buf);
        return buf.toString();
    }


	private void attrs(BaseTable t, StringBuffer content) {
//		content.append("public enum ");
//		content.append(getAttributeType());
//		content.append(" {\n");

//		List<String> elements = new ArrayList<String>();

		Set<Identifier> fkcols = foreignKeyColumns(t);

		for (Column c : t.columns()) {
			// only non-fk-columns are included in attributes.
			// fk-columns  are not intended to be set individually,
			// but atomically with ref -method
			if (!fkcols.contains(c.getColumnName())) {
//				elements.add(attr(c));
				content.append(attr(c));
				content.append(",");
			}
		}

//		content.append(enumMember(getAttributeType(), elements));
	}

	private String refs(BaseTable t) {
	    StringBuffer content = new StringBuffer();
	    refs(t, content);
	    return content.toString();
	}

	private void refs(BaseTable t, StringBuffer content) {
//		List<String> elements = new ArrayList<String>();

		for (ForeignKey fk : t.foreignKeys().values()) {
		    String r = format(fk);
		    if (r == null || r.equals("")) {
		        continue;
		    }
		    content.append(r);
		    content.append(",");
//			elements.add();
		}

//		content.append(enumMember(getReferenceType(), elements));
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


	private String queries(BaseTable t) {
	    StringBuffer content = new StringBuffer();
	    queries(t, content);
	    return content.toString();
	}

	private void queries(BaseTable t, StringBuffer content) {
//		List<String> elements = new ArrayList<String>();

		// TODO:
//		for (ForeignKey fk : t.foreignKeys().values()) {
//			elements.add(fk.getUnqualifiedName().getName());
//		}
//
//		content.append(enumMember(getQueryType(), elements));
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

//	private void indent(int indentLevel, StringBuffer dest) {
//		String indent = "  ";
//
//		for (int level = 0; level < indentLevel; level++) {
//			dest.append(indent);
//		}
//	}

	private File getSourceFile(File pd, String type) {
		return new File(pd, type + ".java");
	}
	
//	private File packageDir(File root, String pkg, Properties generated) 
//	    throws IOException {
//	    File pd = packageDir(pkg);	    
//	    pd = (pd == null) ? root : new File(root, pd.getPath());
//        return pd;	    
//	}
	
	private File packageDir(String pkg) {	    
		if (pkg == null) {
			return null;
		}

		String[] elems = pkg.split(Pattern.quote("."));
		StringBuffer path = new StringBuffer(elems[0]);

		for (int i = 1; i < elems.length; i++) {
			path.append(File.separatorChar);
			path.append(elems[i]);
		}
		
		return new File(path.toString());
	}

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

	private Class<?> wrapper(Class<?> primitiveType) {
	    if (primitiveType == null) {
            throw new NullPointerException();
        }

	    if (!primitiveType.isPrimitive()) {
	        throw new IllegalArgumentException("primitive type expected");
	    }

	    return getWrapperMap().get(primitiveType);
	}

	public Map<Class<?>, Class<?>> getWrapperMap() {
        if (this.wrapperMap == null) {
            Map<Class<?>, Class<?>> wm = this.wrapperMap = new HashMap<Class<?>, Class<?>>();

            wm.put(Boolean.TYPE, Boolean.class);
            wm.put(Byte.TYPE, Byte.class);
            wm.put(Character.TYPE, Character.class);
            wm.put(Double.TYPE, Double.class);
            wm.put(Float.TYPE, Float.class);
            wm.put(Integer.TYPE, Integer.class);
            wm.put(Long.TYPE, Long.class);
            wm.put(Short.TYPE, Short.class);
        }

        return this.wrapperMap;
    }

	private void a(StringBuffer dest, String s) {
	    a(dest, s, 0);
	}

	private void a(StringBuffer dest, String s, int eols) {
	    dest.append(s);

	    String sep = System.getProperty("line.separator");

	    for (int i = 0; i < eols; i++) {
            dest.append(sep);
        }
	}

	private String replacePackageAndImports(String src, JavaType type) {
	    List<String> importList = Collections.emptyList();
	    return replacePackageAndImports(src, type, importList);
	}

	private String replacePackageAndImports(String src, JavaType type, Collection<String> importList) {
	    src = replaceAll(src, PATTERN_PACKAGE, type.getPackageName());
	    src = replaceAll(src, PATTERN_IMPORTS, imports(importList));
	    return src;
	}
    
    public void setSourceDir(Part part, File dir) {
        if (part == null) {
            throw new NullPointerException();
        }
                        
        getSourceDirMap().put(part, dir); 
    }
        
    public File getSourceDir(Part part) {
        File dir = getSourceDirMap().get(part);        
        return (dir != null) ? dir : getSourceDir();        
    }

    public File getSourceDir() {
        return defaultSourceDir;
    }

    public void setSourceDir(File defaultDir) {
        this.defaultSourceDir = defaultDir;
    }    

    private EnumMap<Part, File> getSourceDirMap() {
        if (sourceDirMap == null) {
            sourceDirMap = new EnumMap<Part, File>(Part.class);            
        }

        return sourceDirMap;
    }

}
