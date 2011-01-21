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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import fi.tnie.db.QueryException;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.DoubleKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.map.AttributeInfo;
import fi.tnie.db.map.JavaType;
import fi.tnie.db.map.TableMapper;
import fi.tnie.db.map.TableMapper.Part;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.PrimaryKey;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.SchemaElement;
import fi.tnie.db.meta.SchemaElementMap;
import fi.tnie.db.meta.Table;
import fi.tnie.util.io.IOHelper;

public class SourceGenerator {
	
	
	public enum Tag {
	    /**
	     * Pattern which is replaced with the simple name of the table interface in template files.
	     */		
		TABLE_INTERFACE,
	    /**
	     * Pattern which is replaced with the simple name of the abstract class in template files.
	     */		
		TABLE_ABSTRACT_CLASS,
	    /** 
	     * Pattern which is replaced with the simple name of the hook class in template files.
	     */		
		TABLE_HOOK_CLASS,
		
	    /**
	     * Pattern which is replaced with the simple name of the class the hook-class inherits from in template files.
	     */		
		TABLE_HOOK_BASE_CLASS,
		
	    /**
	     *
	     */		
		LITERAL_TABLE_ENUM,
		
	    /**  
	     * Pattern which is replaced with the simple name of the hook class in template files.
	     */		
		TABLE_IMPL_CLASS,
	    /**  
	     * Pattern which is replaced with the simple name of the hook class in template files.
	     */		
		TABLE_IMPL_BASE,
		
		TABLE_ENUM_INIT_TYPE,		
		
		INIT_COLUMN_ENUM_LIST,
	    /**  
	     * TODO: which accessors 
	     */		
		ACCESSOR_LIST,		
		
		/**
		 * static attribute keys in meta-data implementation
		 */
		ATTRIBUTE_KEY_LIST,
		

		/**
		 * 
		 */
		ATTRIBUTE_KEY_MAP_LIST,
	    /**
	     * Pattern which is replaced with the simple name of the catalog context class in template files.
	     */		
		CATALOG_CONTEXT_CLASS,
	    /**
	     * Pattern which is replaced with the simple name of the catalog context class in template files.
	     */		
		CATALOG_CONTEXT_PACKAGE_NAME,
	    /**
	     * Pattern which is replaced with the package name of the type being generated in template files.
	     */
		PACKAGE_NAME,
		LITERAL_CONTEXT_PACKAGE_NAME,
		

	    /**
	     * Pattern which is replaced with the package name of the type being generated in template files.
	     */
		PACKAGE_NAME_LITERAL,
		
		/**
	     * Pattern which is replaced with the package name of the type being generated in template files.
	     */
		ROOT_PACKAGE_NAME_LITERAL,
		
		FACTORY_METHOD_LIST,
		/**
		 * Generated imports
		 */
		IMPORTS,
		NEW_ENVIRONMENT_EXPR, 
		SCHEMA_ENUM_LIST,
		BASE_TABLE_ENUM_LIST,
		VIEW_ENUM_LIST,
		COLUMN_ENUM_LIST,		
		SCHEMA_TYPE_NAME,
		FOREIGN_KEY_ENUM_LIST,
		PRIMARY_KEY_ENUM_LIST,
		META_MAP_POPULATION,
		TABLE_COLUMN_ENUM_LIST,
		
	    /**
	     * Pattern which is replaced package declaration using the package name of the type being generated in template files.
	     */
		PACKAGE_DECL, 
		
		META_DATA_INITIALIZATION,		
		KEY_ACCESSOR_LIST, 
		COLUMN_KEY_MAP_POPULATION, 
		VALUE_VARIABLE_LIST,
		VALUE_ACCESSOR_LIST,
		;	
		
					

		private Tag() {
			this.tag = tag(toString());
		}
		private Tag(String tag) {
			this.tag = tag;
		}		
		
		private String tag(String n) {
			return "{{" + n.toLowerCase().replace("_", "-") + "}}";
		}

		private String tag;

		public String getTag() {
			return tag;
		}		
	}

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
	
	private List<Class<?>> keyTypes;

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

	private static Logger logger() {
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

//        for (JavaType t : ccil) {
//            if (t != null) {
//                il.add(t.getQualifiedName());
//            }
//        }    

        {
        	
	        JavaType cc = tm.catalogContextType();
	        CharSequence src = generateContext(cc, tm, il, fm);            
	        write(getSourceDir(), cc, src, generated, gm);
        }

        {
	        JavaType lc = tm.literalContextType();
	        CharSequence src = generateLiteralContext(lc, cat, tm, il, fm);
	        logger().debug("generated lit ctx: src={" + src + "}");
	        writeIfGenerated(getSourceDir(), lc, src, generated, gm);
        }
        
        return generated;
    }

    private CharSequence generateLiteralContext(JavaType lc, Catalog cat, TableMapper tm, List<String> il, Map<JavaType, CharSequence> fm) throws IOException {
    	   	
    	logger().debug("generateLiteralContext - enter");
    	String src = getTemplateForLiteralCatalog();    	
    	    	    	
    	src = replaceAllWithComment(src, Tag.PACKAGE_NAME, lc.getPackageName());
    	    	    	
    	Environment env = cat.getEnvironment();
    	String e = env.getClass().getName();
    	src = replaceAllWithComment(src, Tag.NEW_ENVIRONMENT_EXPR, "new " + e + "()");
    	
    	{
	    	String list = generateSchemaList(cat);
	    	src = replaceAllWithComment(src, Tag.SCHEMA_ENUM_LIST, list);
    	}
    	
    	{
	    	String list = generateBaseTableList(cat);
	    	src = replaceAllWithComment(src, Tag.BASE_TABLE_ENUM_LIST, list);
    	}
    	
    	{
	    	String list = generateColumnList(cat);
	    	src = replaceAllWithComment(src, Tag.COLUMN_ENUM_LIST, list);
    	}
    	
    	{
	    	String list = generatePrimaryKeyList(cat, tm);
	    	src = replaceAllWithComment(src, Tag.PRIMARY_KEY_ENUM_LIST, list);
    	}
    	
    	{
	    	String list = generateForeignKeyList(cat, tm);
	    	src = replaceAllWithComment(src, Tag.FOREIGN_KEY_ENUM_LIST, list);
    	}    	
    	
    	{
	    	String list = generateMetaMapPopulation(cat, tm);
	    	src = replaceAllWithComment(src, Tag.META_MAP_POPULATION, list);
    	}

    	{
	        String list = generateFactoryMethodList(fm);
	        src = replaceAllWithComment(src, Tag.FACTORY_METHOD_LIST, list);
    	}
    	
    	final String tsrc = getTemplateForLiteralInnerTable();  
    	
    	StringBuffer buf = new StringBuffer();
    	
    	EnumSet<NameQualification> nq = EnumSet.of(NameQualification.COLUMN);
    	List<String> initList = new ArrayList<String>(); 
    	
    	for (Schema s : cat.schemas().values()) {
    		String sn = name(s.getUnqualifiedName().getName());
    		
    		for (Table t : s.tables().values()) {
    			String tn = getSimpleName(t);
    			
    			StringBuffer ebuf = new StringBuffer();    			    			
    			generateColumnListElements(t, ebuf, nq);
    			String cl = ebuf.toString();
    			String tc = replaceAll(tsrc, Tag.COLUMN_ENUM_LIST, cl);    			
    			tc = replaceAll(tc, Tag.SCHEMA_TYPE_NAME, sn);
    			tc = replaceAll(tc, Tag.TABLE_INTERFACE, tn);			
    			
    			// if inner class:
    			tc = replaceAll(tc, Tag.PACKAGE_DECL, "");
    			tc = replaceAll(tc, Tag.IMPORTS, "");
    			    			
    			buf.append(tc);
    			buf.append("\n\n");
    			    			
    			JavaType tet = tm.entityType(t, Part.LITERAL_TABLE_ENUM);
    			
    			if (tet != null) {
    				initList.add(tet.getQualifiedName());
    			}
			}			
		}
    	
    	final String tin = getTemplateForTableEnumInit();
    	StringBuffer initCode = new StringBuffer();
    	
    	for (String it : initList) {
    		initCode.append(replaceAll(tin, Tag.TABLE_ENUM_INIT_TYPE, it));
		}
    	
    	src = replaceAll(src, Tag.INIT_COLUMN_ENUM_LIST, initCode.toString());
    	
    	src = replaceAllWithComment(src, Tag.TABLE_COLUMN_ENUM_LIST, buf.toString());
    	    	
    	logger().debug("generateLiteralContext - exit");
    	
		return src;
	}
	
	private String generateMetaMapPopulation(Catalog cat, TableMapper tm) {
		StringBuffer buf = new StringBuffer();
				
		for (Schema s : cat.schemas().values()) {
			for (BaseTable t : s.baseTables().values()) {				
				String tn = tableEnumeratedName(t);
				
				JavaType impl = tm.entityType(t, Part.IMPLEMENTATION);
				JavaType intf = tm.entityType(t, Part.INTERFACE);
				
				// add(mm, LiteralBaseTable.PERSONAL_HOUR_REPORT, fi.tnie.db.gen.personal.HourReportImpl.HourReportMetaData.getInstance());
				
				buf.append("add(mm, LiteralBaseTable.");
				buf.append(tn);
				buf.append(", ");
				buf.append(impl.getQualifiedName());				
				buf.append(".");
				buf.append(intf.getUnqualifiedName());
				buf.append("MetaData.getInstance()");
				buf.append(");\n");				
			}
		}		
		
		return buf.toString();
	}

	private String generateForeignKeyList(Catalog cat, TableMapper tm) {
		StringBuffer buf = new StringBuffer();
		
		EnumSet<NameQualification> nq = EnumSet.of(NameQualification.COLUMN);
		
		for (Schema s : cat.schemas().values()) {
			for (BaseTable t : s.baseTables().values()) {
				SchemaElementMap<ForeignKey> fm = t.foreignKeys();
								
				for (ForeignKey fk : fm.values()) {					
					String n = foreignKeyEnumeratedName(fk);
					String sn = schemaEnumeratedName(s);
					Identifier un = fk.getUnqualifiedName();
									
					buf.append(n);
					buf.append("(LiteralSchema.");
					buf.append(sn);
					buf.append(", \"");
					buf.append(un.getName());
					buf.append("\"");
					
					BaseTable kt = fk.getReferencing();					
					JavaType jkt = tm.entityType(kt, Part.LITERAL_TABLE_ENUM);
															
					BaseTable rt = fk.getReferenced();
					JavaType jrt = tm.entityType(rt, Part.LITERAL_TABLE_ENUM);
					
					for (Map.Entry<Column, Column> e : fk.columns().entrySet()) {
												
						// buf.append(", LiteralCatalogColumn.");						
						buf.append(", ");
						buf.append(jkt.getQualifiedName());
						buf.append(".");
						buf.append(columnEnumeratedName(kt, e.getKey(), nq));
						buf.append(", ");
						buf.append(jrt.getQualifiedName());
						buf.append(".");
						buf.append(columnEnumeratedName(rt, e.getValue(), nq));
					}
					
					buf.append("),\n");									
				}
			}
		}		
		
		return buf.toString();	
	}
	
	private String generatePrimaryKeyList(Catalog cat, TableMapper tm) {
		StringBuffer buf = new StringBuffer();

		EnumSet<NameQualification> nq = EnumSet.of(NameQualification.COLUMN);
		
		for (Schema s : cat.schemas().values()) {
			for (BaseTable t : s.baseTables().values()) {
				PrimaryKey pk = t.getPrimaryKey();
				
				if (pk == null) {
					logger().warn("table without primary key: " + t.getQualifiedName());
					continue;
				}
				
				JavaType jt = tm.entityType(t, Part.LITERAL_TABLE_ENUM);
								
				String n = primaryKeyEnumeratedName(pk);
				String tn = tableEnumeratedName(t);
				Identifier un = pk.getUnqualifiedName();
								
				buf.append(n);
				buf.append("(LiteralBaseTable.");
				buf.append(tn);
				buf.append(", \"");
				buf.append(un.getName());
				buf.append("\"");
				
				for (Column c : pk.columns()) {
					buf.append(", ");
					// buf.append("LiteralCatalogColumn.");
					buf.append(jt.getQualifiedName());
					buf.append(".");
					buf.append(columnEnumeratedName(t, c, nq));
				}
				
				buf.append("),\n");									
			}				
		}		
		
		return buf.toString();	
	}
	
	private String generateColumnList(Table t) {
		StringBuffer buf = new StringBuffer();
			
		Identifier sn = t.getSchema().getUnqualifiedName();
		Identifier tn = t.getUnqualifiedName();
		String ce = name(sn.getName()) + name(tn.getName());			
		
		buf.append("enum ");
		buf.append(ce);
		buf.append(" {");
		buf.append("\n");
		
		EnumSet<NameQualification> fq = EnumSet.allOf(NameQualification.class);		
		generateColumnListElements(t, buf, fq);
		
		buf.append("\n");
		buf.append("}");
		
		return buf.toString();
	}
	
	enum NameQualification {
		SCHEMA,
		TABLE,
		COLUMN
	}

	private void generateColumnListElements(Table t, StringBuffer buf, EnumSet<NameQualification> nq) {
		boolean b = t.isBaseTable();
		String te = b ? "LiteralBaseTable" : "LiteralView"; 				
		
		for (Column c : t.columns()) {
			String cn = columnEnumeratedName(t, c, nq);			
			String ten = tableEnumeratedName(t);					
			Identifier un = c.getUnqualifiedName();
			
			// TODO: add 'autoinc' -info etc
			
			buf.append(cn);
			buf.append("(");			
			buf.append("new LiteralCatalog.ColumnInitializer(");
			buf.append("LiteralCatalog.");
			buf.append(te);
			buf.append(".");
			buf.append(ten);
			buf.append(", \"");
			buf.append(un.getName());
			buf.append("\", ");										
			generateNewDataType(buf, c.getDataType());
			buf.append(")),\n");									
		}
		
		buf.append(";\n");
	}	

	private String generateColumnList(Catalog cat) {
		StringBuffer buf = new StringBuffer();

		for (Schema s : cat.schemas().values()) {
			for (Table t : s.tables().values()) {
				boolean b = t.isBaseTable();
				String te = b ? "LiteralBaseTable" : "LiteralView"; 				
				
				for (Column c : t.columns()) {
					String cn = columnEnumeratedName(t, c);
					String tn = tableEnumeratedName(t);					
					Identifier un = c.getUnqualifiedName();
					
					// TODO: add 'autoinc' -info etc
					
					buf.append(cn);
					buf.append("(");
					buf.append(te);
					buf.append(".");
					buf.append(tn);
					buf.append(", \"");
					buf.append(un.getName());
					buf.append("\", ");										
					generateNewDataType(buf, c.getDataType());
					buf.append("),\n");									
				}
			}
		}		
		
		return buf.toString();
	}
	/**
	 * Formats the expression: new DataTypeImpl(...)
	 * @param buf
	 * @param t
	 */

	private void generateNewDataType(StringBuffer buf, DataType t) {
		buf.append("new DataTypeImpl(");				
//		call: new DataTypeImpl(int dataType, String typeName, int charOctetLength, int decimalDigits, int numPrecRadix, int size)
		buf.append(t.getDataType());
		buf.append(", \"");
		buf.append(t.getTypeName());
		buf.append("\", ");
		buf.append(t.getCharOctetLength());
		buf.append(", ");
		buf.append(t.getDecimalDigits());
		buf.append(", ");
		buf.append(t.getNumPrecRadix());
		buf.append(", ");
		buf.append(t.getSize());					
		buf.append(")");
	}

	private String generateBaseTableList(Catalog cat) {
		StringBuffer buf = new StringBuffer();

		for (Schema s : cat.schemas().values()) {
			for (BaseTable t : s.baseTables().values()) {				
				String tn = tableEnumeratedName(t);
				String sn = schemaEnumeratedName(s);
				Identifier un = t.getUnqualifiedName();
								
				buf.append(tn);
				buf.append("(LiteralSchema.");
				buf.append(sn);
				buf.append(", \"");
				buf.append(un.getName());
				buf.append("\"),\n");				
			}
		}		
		
		return buf.toString();
	}

	private String generateSchemaList(Catalog cat) {
		StringBuffer buf = new StringBuffer();

		for (Schema s : cat.schemas().values()) {
			Identifier un = s.getUnqualifiedName();
			String n = un.getName();
			buf.append(schemaEnumeratedName(s));
			buf.append("(\"");
			buf.append(n);
			buf.append("\"),\n");
		}		
		
		return buf.toString();
	}
	
	private String schemaEnumeratedName(Schema s) {
		Identifier un = s.getUnqualifiedName();
		return un.getName().toUpperCase();				
	}
	
	private String tableEnumeratedName(Table t) {		
		return enumeratedName(t);
	}
	
	private String foreignKeyEnumeratedName(ForeignKey k) {		
		return enumeratedName(k);
	}
	
	private String primaryKeyEnumeratedName(PrimaryKey k) {		
		return enumeratedName(k);
	}
	
	private String enumeratedName(SchemaElement e) {
		StringBuffer buf = new StringBuffer();
		buf.append(e.getSchema().getUnqualifiedName().getName());
		buf.append("_");
		buf.append(e.getUnqualifiedName().getName());		
		
		return buf.toString().toUpperCase();
	}
	
	private String columnEnumeratedName(Table t, Column c) {
		return columnEnumeratedName(t, c, EnumSet.allOf(NameQualification.class));
	}
	
	private String columnEnumeratedName(Table t, Column c, EnumSet<NameQualification> nq) {
		StringBuffer buf = new StringBuffer();
		
		if (nq.contains(NameQualification.SCHEMA)) {						
			buf.append(t.getSchema().getUnqualifiedName().getName());
			buf.append("_");
		}
		
		if (nq.contains(NameQualification.TABLE)) {
			buf.append(t.getUnqualifiedName().getName());		
			buf.append("_");			
		}

		if (nq.contains(NameQualification.COLUMN)) {
			buf.append(c.getUnqualifiedName().getName());
		}
		
		return buf.toString().toUpperCase();			
		
	}

	private CharSequence generateContext(JavaType cc, TableMapper tm,
            Collection<String> il, Map<JavaType, CharSequence> fm) throws IOException {

		logger().debug("generateContext - enter");
        String src = getTemplateForCatalogContext();

//        src = replacePackageAndImports(src, cc, il);
        
        src = replacePackageName(src, cc);
	    src = replaceAll(src, Tag.PACKAGE_NAME_LITERAL, "\"" + cc.getPackageName() + "\"");
	    src = replaceImportList(src, il);        
        src = replaceAll(src, Tag.ROOT_PACKAGE_NAME_LITERAL, "\"" + tm.getRootPackage() + "\"");        
        src = replaceAll(src, Tag.CATALOG_CONTEXT_PACKAGE_NAME, cc.getPackageName());
        src = replaceAll(src, Tag.CATALOG_CONTEXT_CLASS, cc.getUnqualifiedName());

        String list = generateFactoryMethodList(fm);
        src = replaceAll(src, Tag.FACTORY_METHOD_LIST, list);
        
        logger().debug("generateContext - exit");

        return src;
    }

	private String generateFactoryMethodList(
			Map<JavaType, CharSequence> fm) {
		StringBuffer buf = new StringBuffer();
                
        for (Map.Entry<JavaType, CharSequence> e : fm.entrySet()) {
            buf.append(formatSchemaFactoryMethod(e.getKey(), e.getValue()));
        }
		return buf.toString();
	}

    private String formatSchemaFactoryMethod(JavaType key, CharSequence value) {
        String n = key.getUnqualifiedName();
        String qn = key.getQualifiedName();
        return "public " + qn + " new" + n + "() { return " + value + " } ";
    }


    private String getTemplateForCatalogContext() throws IOException {
        return read("CATALOG_CONTEXT.in");
    }
    
    private String getTemplateForLiteralCatalog() throws IOException {
        return read("LITERAL_CATALOG.in");
    }
    
    private String getTemplateForLiteralInnerTable() throws IOException {
        return read("LITERAL_INNER_TABLE.in");
    }
    
    private String getTemplateForLiteralTable() throws IOException {
        return read("LITERAL_TABLE_ENUM.in");
    }

    private String getTemplateForTableEnumInit() throws IOException {
        return read("TABLE_ENUM_INIT.in");
    }

    private void process(Schema s, final TableMapper tm, Collection<JavaType> ccil, Map<JavaType, CharSequence> factories, Properties generated, Map<File, String> gm) 
		throws IOException {

	    List<TypeInfo> types = new ArrayList<TypeInfo>();
	    
		for (BaseTable t : s.baseTables().values()) {
		    final JavaType intf = tm.entityType(t, Part.INTERFACE);
		    final JavaType at = tm.entityType(t, Part.ABSTRACT);
		    final JavaType hp = tm.entityType(t, Part.HOOK);
		    final JavaType impl = tm.entityType(t, Part.IMPLEMENTATION);
		    final JavaType te = tm.entityType(t, Part.LITERAL_TABLE_ENUM);
		    
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
                
                if (te != null) {
                    CharSequence source = generateTableEnum(t, te, tm);
                    File root = getSourceDir(schema, Part.LITERAL_TABLE_ENUM);
                    write(root, te, source, generated, gm);
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
            final JavaType fimp = tm.factoryType(s, Part.IMPLEMENTATION);
//            final JavaType impl = tm.factoryType(s, Part.IMPLEMENTATION);

            if (intf != null) {
                if (types.isEmpty()) {

                }
                else {
                    File root = getSourceDir(s, Part.INTERFACE);
                    write(root, intf, generateFactoryInterface(s, intf, tm, types), generated, gm);

                    root = getSourceDir(s, Part.IMPLEMENTATION);
                    write(root, fimp, generateFactoryImplementation(s, fimp, tm, types), generated, gm);
                                       
                    CharSequence src = generateSchemaFactoryMethodImplementation(s, tm, types);
//                    CharSequence src = generateAnonymousFactoryImplementation(s, tm, types);
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
    
    private void writeIfGenerated(File root, JavaType type, CharSequence source, Properties dest, Map<File, String> files) 
    	throws IOException {
    	
		if (source == null) {
			return;
		}
		
		write(root, type, source, dest, files);
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

	    src = replaceAll(src, Tag.TABLE_INTERFACE, mt.getUnqualifiedName());
	    
        {
            String code = attributeKeyList(t, tm);
            src = replaceAll(src, Tag.ATTRIBUTE_KEY_LIST, code);
        }

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
        
        {
            String code = valueAccessorList(t, tm, false);     
            src = replaceAll(src, Tag.VALUE_ACCESSOR_LIST, code);
        }
        
        

	    return src;
	}
    
    public CharSequence generateTableEnum(BaseTable t, JavaType mt, TableMapper tm)
    	throws IOException {

	    String src = getTemplateFor(Part.LITERAL_TABLE_ENUM);			
	    
	    src = replacePackageName(src, mt);

	    ArrayList<String> il = new ArrayList<String>();
	    addImport(mt, tm.literalContextType(), il);
	    src = replaceImportList(src, il);
	    
	    src = replaceAll(src, Tag.LITERAL_TABLE_ENUM, mt.getUnqualifiedName());
	
	    EnumSet<NameQualification> nq = EnumSet.of(NameQualification.COLUMN);
	    
	    StringBuffer ebuf = new StringBuffer();
		generateColumnListElements(t, ebuf, nq);
		String cl = ebuf.toString();
		src = replaceAll(src, Tag.COLUMN_ENUM_LIST, cl);
		
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
    
	private CharSequence generateSchemaFactoryMethodImplementation(Schema s, TableMapper tm, Collection<TypeInfo> types)
	    throws IOException {
	
//	    JavaType intf = tm.factoryType(s, Part.INTERFACE);
//	    String src = getFactoryTemplateFor(Part.INTERFACE);
	    
	    JavaType impl = tm.factoryType(s, Part.IMPLEMENTATION);
	    
	    String src = " new " + impl.getQualifiedName() + "(); ";
	
//	    src = replaceAll(src, "{{schema-factory}}", intf.getUnqualifiedName());
//	
//	    StringBuffer code = new StringBuffer();
//	
//	    for (TypeInfo t : types) {
//	        String m = formatFactoryMethod(t, true);
//	        a(code, m, 1);
//	    }
//	
//	    src = replaceAll(src, FACTORY_METHOD_LIST, code.toString());
//	
//	    logger().debug("factory impl: " + src);
	
	    return src;
	}

    private CharSequence generateFactoryImplementation(Schema s, JavaType impl, TableMapper tm, Collection<TypeInfo> types)
        throws IOException {

        JavaType intf = tm.factoryType(s, Part.INTERFACE);
        String src = getFactoryTemplateFor(Part.IMPLEMENTATION);

        List<String> il = new ArrayList<String>();

        addImport(impl, intf, il);

        for (TypeInfo t : types) {
            JavaType returnType = getFactoryMethodReturnType(t);
            addImport(impl, returnType, il);
        }
            
        // addImport(impl, tm.literalContextType(), il);        
        src = replaceAll(src, Tag.LITERAL_CONTEXT_PACKAGE_NAME, tm.literalContextType().getPackageName());                

        src = replacePackageAndImports(src, impl, il);

        src = replaceAll(src, "{{schema-factory-impl}}", impl.getUnqualifiedName());
        src = replaceAll(src, "{{schema-factory}}", intf.getQualifiedName());

        StringBuffer code = new StringBuffer();

        for (TypeInfo t : types) {
            String m = formatFactoryMethod(t, true);
            a(code, m, 1);
        }

        src = replaceAll(src, "{{factory-method-list}}", code.toString());

        logger().debug("factory impl: " + src);

        return src;
    }


    private JavaType getFactoryMethodReturnType(TypeInfo info) {
        JavaType hp = info.get(Part.HOOK);
        JavaType itfp = info.get(Part.INTERFACE);
        JavaType at = info.get(Part.ABSTRACT);

        return (hp != null) ? hp : (at != null) ? at : itfp;
    }

    /**
     * Formats a factory method for JavaType.
     * @param info
     * @param impl
     * @return
     */

    private String formatFactoryMethod(TypeInfo info, boolean impl) {
        JavaType itf = info.get(Part.INTERFACE);
        JavaType impp = info.get(Part.IMPLEMENTATION);
        
        JavaType returnType = getFactoryMethodReturnType(info);

        String signature = returnType.getQualifiedName() + " new" + itf.getUnqualifiedName() + "()";
        String src = null;

        if (!impl) {
            src = signature + ";";
        }
        else {
            src = "public " + signature + " { " +
            		"return " + 
            		impp.getQualifiedName() + "." +            		
            		itf.getUnqualifiedName() + "MetaData.getInstance().getFactory().newInstance" +
            		"(); } ";
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
	
	private String replaceAllWithComment(String text, Tag pattern, String replacement) {
		replacement = "// " + pattern.getTag() + "\n" + replacement;
		return replaceAll(text, pattern, replacement);
	}
	
	private String replaceAll(String text, Tag pattern, String replacement) {
		return replaceAll(text, pattern.getTag(), replacement);
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

        src = replaceAll(src, Tag.TABLE_ABSTRACT_CLASS, mt.getUnqualifiedName());
        src = replaceAll(src, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());

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

        src = replaceAll(src, Tag.TABLE_HOOK_CLASS, mt.getUnqualifiedName());
        src = replaceAll(src, Tag.TABLE_HOOK_BASE_CLASS, base.getUnqualifiedName());

        return src;
	}

	private void addImport(JavaType importingType, JavaType imported, List<String> importList) {
	    if (imported == null ||
	        imported.getPackageName().equals(importingType.getPackageName())) {
	        return;
	    }
	    importList.add(imported.getQualifiedName());
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

        src = replaceAll(src, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());
        src = replaceAll(src, Tag.TABLE_IMPL_CLASS, impl.getUnqualifiedName());
        src = replaceAll(src, Tag.TABLE_IMPL_BASE, base.getUnqualifiedName());
        
        {
            String code = accessors(t, tm, true);
            src = replaceAll(src, Tag.ACCESSOR_LIST, code);
        }

        {
            String code = attributeKeyMapList(t, tm);
            src = replaceAll(src, Tag.ATTRIBUTE_KEY_MAP_LIST, code);
        }
                
        {
            String code = metaDataInitialization(t, tm);
            src = replaceAll(src, Tag.META_DATA_INITIALIZATION, code);
        }
        
        {
            String code = columnKeyMapPopulation(t, tm);
            src = replaceAll(src, Tag.COLUMN_KEY_MAP_POPULATION, code);
        }
        
        {
            String code = keyAccessorList(t, tm);           
            src = replaceAll(src, Tag.KEY_ACCESSOR_LIST, code);
        }
        
        {
            String code = valueVariableList(t, tm);           
            src = replaceAll(src, Tag.VALUE_VARIABLE_LIST, code);
        }
        
        {
            String code = valueAccessorList(t, tm, true);     
            src = replaceAll(src, Tag.VALUE_ACCESSOR_LIST, code);
        }

        return src;
	}
	
	private String columnKeyMapPopulation(BaseTable t, TableMapper tm) {
		List<Column> acl = getAttributeColumnList(t, tm);        
        StringBuffer content = new StringBuffer();
        
        JavaType intf = tm.entityType(t, Part.INTERFACE);
        
        for (Column c : acl) {
        	AttributeInfo a = tm.getAttributeInfo(t, c);
        	
        	if (a.getKeyType() != null) {        	
	        	// example: add(PersonMetaData.ID, columnKeyMap);
	        	content.append("add(");
	        	content.append(keyConstantExpression(t, c, intf));
	        	content.append(", columnKeyMap);\n");
        	}
        }
        
        return content.toString();
	}

	private String keyAccessorList(BaseTable t, TableMapper tm) {
		Map<Class<?>, Integer> tom = keyTypeOccurenceMap(t, tm);		
		StringBuffer content = new StringBuffer();
		
		List<Class<?>> ktl = getKeyTypes();
		
		logger().debug("type-count: " + ktl.size());
		
		for (Class<?> kt : ktl) {
			Integer v = tom.get(kt);
			int occurences = (v == null) ? 0 : v.intValue();
			
			logger().debug("keyAccessorList: kt=" + occurences);
			String ka = metaDataKeyAccessor(kt, occurences, t, tm);
			logger().debug("keyAccessorList: ka=" + ka);						
			content.append(ka);			
		}
				
		
		return content.toString();
	}
	
	private String valueVariableList(BaseTable t, TableMapper tm) {
		List<Column> acl = getAttributeColumnList(t, tm);        
        StringBuffer content = new StringBuffer();

        for (Column c : acl) {        	        	
            String code = formatValueVariable(t, c, tm);
            content.append(code);
        }
        
        return content.toString();
	}
	
	private String valueAccessorList(BaseTable t, TableMapper tm, boolean impl) {
		List<Column> acl = getAttributeColumnList(t, tm);        
        StringBuffer content = new StringBuffer();

        for (Column c : acl) {        	        	
            String code = formatValueAccessor(t, c, tm, impl);
            content.append(code);
        }
        
        return content.toString();
	}


	private String formatValueVariable(BaseTable t, Column c, TableMapper tm) {
		
		// private transient VarcharValue<Person.Attribute, Person> lastName;
		
		StringBuffer buf = new StringBuffer();

		AttributeInfo a = tm.getAttributeInfo(t, c);
		Class<?> vt = a.getValueType();
        Class<?> kt = a.getKeyType();
                        
        if (kt == null || vt == null) {
        	return "";
        }
        
        JavaType intf = tm.entityType(t, Part.INTERFACE);
        
        buf.append("private transient ");
        buf.append(vt.getName());
        buf.append("<");
        buf.append("Attribute");        
        buf.append(", ");
        buf.append(intf.getUnqualifiedName());        
        buf.append("> ");
        buf.append(valueVariableName(t, c));
        buf.append(" = null;\n");

		return buf.toString();
	}
	
	
	private String formatValueAccessor(BaseTable t, Column c, TableMapper tm, boolean impl) {
		
//		// output example: 
//	    public VarcharValue<Attribute, Person> lastName() {
//	    	if (this.lastName == null) {
//	    		this.lastName = varcharValue(Person.LAST_NAME);
//	    	}
//	    	
//	    	return this.lastName;
//	    }		
		
		StringBuffer buf = new StringBuffer();

		AttributeInfo a = tm.getAttributeInfo(t, c);
		Class<?> vt = a.getValueType();
		Class<?> kt = a.getKeyType();
                        
        if (kt == null || vt == null) {
        	return "";
        }
        
        logger().debug("formatValueAccessor: value-type=" + vt);
        
        JavaType intf = tm.entityType(t, Part.INTERFACE);
        String vv = valueVariableName(t, c);
        String ke = keyConstantExpression(t, c, intf);
                    
        if (impl) {
        	buf.append("public ");
        }
        
        buf.append(vt.getName());
        buf.append("<");
        buf.append("Attribute");        
        buf.append(", ");
        buf.append(intf.getUnqualifiedName());        
        buf.append("> ");
        buf.append(vv);
        buf.append("()");
        
        if(!impl) {
        	buf.append(";\n\n");
        }
        else {        
	        buf.append(" {\n");
	        buf.append("if (this.");
	        buf.append(vv);
	        buf.append(" == null) {\n");
	        buf.append("this.");
	        buf.append(vv);
	        buf.append(" = ");
	        buf.append(decapitalize(getSimpleName(vt)));
	        buf.append("(");
	        buf.append(ke);        
	        buf.append(");\n}\n");        
	        buf.append("return this.");
	        buf.append(vv);
	        buf.append(";\n");
	        buf.append("}\n\n");
        }
        
		return buf.toString();
	}	

	private String valueVariableName(Table t, Column c) {
		return decapitalize(name(c.getColumnName().getName()));
	}

	private String metaDataKeyAccessor(Class<?> kt, int occurences, BaseTable table, TableMapper tm) {
		StringBuffer content = new StringBuffer();
		JavaType intf = tm.entityType(table, Part.INTERFACE);

//      Example of desired output:
//		==============================================================
//		@Override
//		public IntegerKey<Attribute, Person> getIntegerKey(Attribute attribute) {
//			if (attribute == null) {
//				throw new NullPointerException("attribute");
//			}
//			
//			return integerKeyMap.get(attribute);
//		}
		
		content.append("@Override public ");		
		content.append(kt.getName());
		content.append("<Attribute, ");
		content.append(intf.getUnqualifiedName());
		content.append("> get");
		content.append(getSimpleName(kt));
		content.append("(Attribute attribute) {\n");		
		content.append("if (attribute == null) {\n");
		content.append("throw new NullPointerException(\"attribute\");\n}\n\n");
		
		if (occurences == 0) {
			content.append("return null;\n");
		}
		
		if (occurences == 1) {
			Column col = getFirstWithKeyType(table, tm, kt);
			
			if (col == null) {
				content.append("return null;\n");
			}
			else {
				content.append("return ");;
				content.append(keyConstantExpression(table, col, intf));				
				content.append(";\n");				
			}			
		}
		
		if (occurences > 1) {			
			content.append("return ");
			content.append(getKeyMapVariable(kt));			
			content.append(".get(attribute);\n");
		}
		
		
		content.append("}\n");
				
		return content.toString();
	}

	private List<Class<?>> getKeyTypes() {
		if (keyTypes == null) {
			keyTypes = new ArrayList<Class<?>>();
			
			keyTypes.add(VarcharKey.class);
			keyTypes.add(CharKey.class);
			keyTypes.add(IntegerKey.class);						
			keyTypes.add(DoubleKey.class);
			keyTypes.add(DateKey.class);
			keyTypes.add(TimestampKey.class);			
		}

		return keyTypes;
	}

	private String metaDataInitialization(BaseTable t, TableMapper tm) {
		List<Column> acl = getAttributeColumnList(t, tm);        
        StringBuffer content = new StringBuffer();        
        Map<Class<?>, Integer> tom = keyTypeOccurenceMap(t, tm);
        
        JavaType intf = tm.entityType(t, Part.INTERFACE);
        
        for (Column c : acl) {
        	AttributeInfo a = tm.getAttributeInfo(t, c);
        	
        	Class<?> kt = a.getKeyType();
        	Integer v = tom.get(kt);
        	int occurences = (v == null) ? 0 : v.intValue();
        	
        	if (occurences > 1) {
            	getKeyMapVariable(a.getKeyType());        	
            	content.append("addAttributeKey(");
            	content.append(keyConstantExpression(t, c, intf));
            	content.append(", ");
            	content.append(getKeyMapVariable(kt));
            	content.append(");\n");            	        		
        	}        	
        }
        
        return content.toString();	
     }

	private String attributeKeyMapList(BaseTable t, TableMapper tm) {
		StringBuffer buf = new StringBuffer();
		Map<Class<?>, Integer> tom = keyTypeOccurenceMap(t, tm);		
		// Map<Integer, Class<?>> ktm = typeKeyMap(t, tm);
		
		logger().debug(t.getQualifiedName() + " - keyTypeOccurenceMap: " + tom);
		
		for (Class<?> k : tom.keySet()) {
			Integer o = tom.get(k);

			// if there's only one occurence we don't need map,
			// we can return the only one directly in get<X>Key -methods
			if (o.intValue() > 1) {
				if (k == null) {
					throw new NullPointerException("no key-type");		
				}				
								
				String c = formatAttributeKeyMap(t, k, o.intValue(), tm);
				buf.append(c);
			}
		}
		
		
		return buf.toString();
	}
	
	
	private Map<Class<?>, Integer> keyTypeOccurenceMap(BaseTable t, TableMapper tm) {
		List<Column> acl = getAttributeColumnList(t, tm);
           
        // key -type => occurences:
        Map<Class<?>, Integer> tom = new HashMap<Class<?>, Integer>();
        
        for (Column c : acl) {
        	AttributeInfo a = tm.getAttributeInfo(t, c);
        	Class<?> k = a.getKeyType();
        	
        	if (k == null) {
        		logger().warn("no key type for column-type : " + c.getDataType().getTypeName());        		
        	}
        	else {
            	Integer o = tom.get(k);
            	o = (o == null) ? Integer.valueOf(1) : Integer.valueOf(o.intValue() + 1);
            	tom.put(k, o);        		
        	}
        }
        
        return tom;
	}
	
	private Column getFirstWithKeyType(BaseTable t, TableMapper tm, Class<?> keyType) {
		List<Column> acl = getAttributeColumnList(t, tm);
		
		for (Column column : acl) {
			AttributeInfo a = tm.getAttributeInfo(t, column);
			Class<?> akt = a.getKeyType();
			
			if (akt != null && akt.equals(keyType)) {
				return column;
			}
		}
		
		return null;
	}
	

    // only non-fk-columns are included in attributes.
    // fk-columns  are not intended to be set individually,
    // but atomically with ref -methods

	private List<Column> getAttributeColumnList(BaseTable t, TableMapper tm) {
		Set<Identifier> fkcols = foreignKeyColumns(t);
		List<Column> attrs = new ArrayList<Column>();
		
		for (Column c : t.columns()) {
            if (!fkcols.contains(c.getColumnName())) {
                attrs.add(c);
            }			
		}
		
		return attrs;
	}

	private String attributeKeyList(BaseTable t, TableMapper tm) {
		List<Column> acl = getAttributeColumnList(t, tm);        
        StringBuffer content = new StringBuffer();

        for (Column c : acl) {
            String code = formatAttributeKey(t, c, tm);
            content.append(code);
        }
        
        return content.toString();
    }
	
	private String formatAttributeKeyMap(BaseTable t, Class<?> keyType, int occurences, TableMapper tm) {
		StringBuffer buf = new StringBuffer();
		
        JavaType intf = tm.entityType(t, Part.INTERFACE);
                        
        // goal:
        // private Map<Attribute, IntegerKey<Attribute, Person>> integerKeyMap = new HashMap<Attribute, IntegerKey<Attribute,Person>>();
        
        buf.append("private java.util.Map<");
        buf.append("Attribute");
        buf.append(", ");
        buf.append(keyType.getName());
        buf.append("<");
        buf.append("Attribute");
        buf.append(", ");        
        buf.append(intf.getUnqualifiedName());        
        buf.append(">> ");
        buf.append(getKeyMapVariable(keyType));        
        buf.append(" = new java.util.HashMap<");
        buf.append("Attribute");
        buf.append(", ");
        buf.append(keyType.getName());
        buf.append("<");
        buf.append("Attribute");
        buf.append(", ");        
        buf.append(intf.getUnqualifiedName());        
        buf.append(">>();\n");
        
        return buf.toString();		
	}
	

	private String formatAttributeKey(BaseTable t, Column c, TableMapper tm) {
		StringBuffer buf = new StringBuffer();

		AttributeInfo a = tm.getAttributeInfo(t, c);		
        Class<?> kt = a.getKeyType();
        
        JavaType intf = tm.entityType(t, Part.INTERFACE);
                        
        if (kt == null) {
        	return "";
        }
        
        // public static final IntegerKey<Attribute, Person> ID = new IntegerKey<Attribute, Person>(Attribute.ID);"
        
        buf.append("public static final ");
        buf.append(kt.getName());
        buf.append("<");
        buf.append("Attribute");        
        buf.append(", ");
        buf.append(intf.getUnqualifiedName());        
        buf.append("> ");
        buf.append(keyConstantVariable(t, c));
        buf.append(" = new ");
        buf.append(kt.getName());
        buf.append("<");
        buf.append("Attribute");
        buf.append(", ");
        buf.append(intf.getUnqualifiedName());        
        buf.append("> (");
        buf.append("Attribute");
        buf.append(".");
        buf.append(columnEnumeratedName(t, c, NameQualification.COLUMN));
        buf.append(");\n");

		return buf.toString();
	}
	
	private String keyConstantExpression(BaseTable t, Column c, JavaType intf) {		
		return intf.getQualifiedName() + "." + keyConstantVariable(t, c);
	}
	
//	private String keyConstantExpression(BaseTable t, Column c, TableMapper tm) {
//		JavaType intf = tm.entityType(t, Part.INTERFACE);
//		return keyConstantExpression(t, c, intf);
//	}

	private String keyConstantVariable(BaseTable t, Column c) {
		return columnEnumeratedName(t, c, NameQualification.COLUMN);
	}
	
	

	private String columnEnumeratedName(BaseTable t, Column c, NameQualification nq) {
		return columnEnumeratedName(t, c, EnumSet.of(nq));
	}

	private String accessors(BaseTable t, TableMapper tm, boolean impl) {
	    StringBuffer content = new StringBuffer();
	    accessors(t, content, tm, impl);
	    return content.toString();
	}

	private void accessors(BaseTable t, StringBuffer content, TableMapper tm, boolean impl) {
		List<Column> cols = getAttributeColumnList(t, tm);

        for (Column c : cols) {
        	AttributeInfo a = tm.getAttributeInfo(t, c);
        	
            Class<?> at = a.getAttributeType();
            Class<?> ht = a.getHolderType();

            if (at != null && ht != null) {
                String code = formatAccessors(t, c, a, impl);
                content.append(code);
            }
        }
    }

    private String formatAccessors(Table table, Column c, AttributeInfo info, boolean impl) {
        final String attributeName = attr(c);
        Class<?> attributeType = info.getAttributeType();
        Class<?> holderType = info.getHolderType();
        
        final String type = attributeType.getName();

        StringBuffer nb = new StringBuffer();
        final String n = name(c.getColumnName().getName());
        
        boolean b = attributeType.equals(Boolean.class);
        
        String vv = valueVariableName(table, c);
        
        String prefix = b ? "is" : "get";
        
        a(nb, "public ");
        a(nb, type);
        a(nb, " ");
        a(nb, prefix);
        a(nb, n);
        a(nb, "()");

        if (!impl) {
            a(nb, ";", 1);
        }
        else {
            // getter implementation
            a(nb, " {", 1);
            
            // example: return id().get();
            
            a(nb, "return ");
            a(nb, vv);
            a(nb, "().get();", 1);            

//            if (!attributeType.isPrimitive()) {
//                a(nb, "return ");
//                // call super & cast:
////                expr(nb, attributeType, attributeName);
//                
//                nb.append(prefix);
//                
//                String tn = getSimpleName(attributeType);
//                
//                nb.append(tn);
//                nb.append("(");
//                nb.append(getAttributeType());
//                nb.append(".");
//                nb.append(attributeName);                                                
//                a(nb, ");", 1);
//            }
//            else {
//                Class<?> wt = wrapper(attributeType);
//                a(nb, wt.getName());
//                
//                a(nb, " o = ");
//                // call super & cast:
////                expr(nb, attributeType, attributeName);
//                a(nb, prefix);
//                a(nb, getSimpleName(wt));                
//                a(nb, "(");
//                a(nb, getAttributeType());
//                a(nb, ".");
//                a(nb, attributeName);                
//                a(nb, ");", 2);
//
//                a(nb, "return (o == null) ? ");
//
//                // default value:
//                if (attributeType.equals(Boolean.TYPE)) {
//                    a(nb, " false ");
//                }
//                else {
//                    // TODO: should we try to parse the column default value here?
//                    a(nb, " 0 ");
//                }
//
//                a(nb, " : ");
//                a(nb, "o.intValue()");
//                a(nb, ";", 1);
//            }

            a(nb, "}", 2);
        }
        
        if (holderType != null) {
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
	            a(nb, vv);
	            a(nb, "().set(newValue);", 1);            
	            
//	            String hn = getSimpleName(holderType);
//	            
//	            a(nb, "set");
//	
//	            if (!attributeType.isPrimitive()) {
//		            a(nb, hn);
//		            a(nb, "(");
//		            a(nb, getAttributeType());
//		            a(nb, ".");
//		            a(nb, attributeName);
//		            a(nb, ", ");	            	
//	            	a(nb, holderType.getName());
//	                a(nb, ".valueOf(newValue)");
//	            }
//	            else {
//	                Class<?> wt = wrapper(attributeType);
//	            	a(nb, getSimpleName(wt));
//		            a(nb, "(");
//		            a(nb, getAttributeType());
//		            a(nb, ".");
//		            a(nb, attributeName);
//		            a(nb, ", ");	            	
//	                a(nb, wt.getName());
//	                a(nb, ".valueOf(");
//	                a(nb, "newValue");
//	                a(nb, ")");
//	            }
//	            a(nb, ");", 1);
	            a(nb, "}", 2);
	        }
        }

        return nb.toString();
    }

	private String getSimpleName(Class<?> attributeType) {
		String tn = attributeType.getName();
		int pos = tn.lastIndexOf(".");                
		tn = tn.substring(pos < 0 ? 0 : pos + 1);
		return tn;
	}
	
	
	private String getKeyMapVariable(Class<?> keyType) {
		StringBuffer buf = new StringBuffer();
		String n = getSimpleName(keyType) + "Map";
		decapitalize(n, buf);
		return buf.toString();
	}

    private void expr(StringBuffer sb, Class<?> attributeType, String attributeName) {
        Class<?> castTo = attributeType.isPrimitive() ?
                wrapper(attributeType) :
                attributeType;

        sb.append(" getString(");
        sb.append(getAttributeType());
        sb.append(".");
        sb.append(attributeName);
        sb.append(").value()");
    }
    
    /**
     * Converts SQL style name to camel case 
     * 
     * @param identifier
     * @return
     */
    public String name(String identifier) {
        int len = identifier.length();
        StringBuffer nb = new StringBuffer(len);
        boolean upper = true;

        for (int i = 0; i < len; i++) {
            char c = identifier.charAt(i);

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
    
    public String decapitalize(String identifier) {
    	if ((identifier == null) || identifier.equals("")) {			
    		return identifier;
    	}
    	
    	StringBuffer buf = new StringBuffer(identifier);
    	buf.setCharAt(0, Character.toLowerCase(identifier.charAt(0)));
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
	    src = replacePackageName(src, type);
	    src = replaceImportList(src, importList);
	    return src;
	}

	private String replacePackageName(String src, JavaType type) {
		src = replaceAll(src, Tag.PACKAGE_NAME, type.getPackageName());
		return src;
	}

	private String replaceImportList(String src, Collection<String> importList) {
		src = replaceAllWithComment(src, Tag.IMPORTS, imports(importList));
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
    
	public String getSimpleName(Table table) {                
        return getSimpleName(table.getUnqualifiedName());	    
	}
	
	public String getSimpleName(Identifier identifier) {	            
	    return translate(identifier.getName());        
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
	
	private void decapitalize(String t, StringBuffer dest) {		
		dest.append(Character.toLowerCase(t.charAt(0)));
		dest.append(t.substring(1));		
	}
}
