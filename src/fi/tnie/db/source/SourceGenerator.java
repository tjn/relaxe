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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.im.EntityIdentityMap;
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
import fi.tnie.db.query.QueryException;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.util.io.IOHelper;

public class SourceGenerator {
	
	
	public enum Tag {
	    /**
	     * Pattern which is replaced with the simple name of the table interface in template files.
	     */		
		TABLE_INTERFACE,
		HAS_KEY_INTERFACE,
		
		REFERENCED_TABLE_INTERFACE_QUALIFIED,
		TABLE_INTERFACE_REF,
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
		 * static attribute key declarations in interface
		 */
		ATTRIBUTE_KEY_LIST,
		REFERENCE_KEY_VARIABLE,
		
		REFERENCE_KEY_LIST,
		
		IMPLEMENTED_HAS_KEY_LIST,
		
		
		BUILDER_LINKER_INIT,

		/**
		 * Class declarations for reference keys
		 */
		REFERENCE_KEY_CLASS_LIST,		

		/**
		 * 
		 */
		ATTRIBUTE_KEY_MAP_LIST,
		REFERENCE_KEY_MAP_LIST,
		
		/*
		 * Implements ... list for table interface. 
		 */
		REFERENCE_LIST,
		
		REFERENCE_MAP_LIST,
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
	     * 
	     */
		CREATE_IDENTITY_MAP_METHOD,
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
		ADD_ATTRIBUTE_KEY_METHOD,
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

//	private static boolean knownAbbr(String t) {
//		return t.equals("url") || t.equals("http") || t.equals("xml");
//	}
//
//	private static boolean knownProposition(String t) {
//		return
//			t.equals("of") || t.equals("in") || t.equals("with") ||
//			t.equals("at") || t.equals("for");
//	}

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
        
		Set<BaseTable> tts = new HashSet<BaseTable>();
		
		for (Schema s : cat.schemas().values()) {
			for (ForeignKey fk : s.foreignKeys().values()) {			
				tts.add(fk.getReferenced());			
			}			
		}
		
		for (BaseTable t : tts) {
			JavaType hki = tm.entityType(t, Part.HAS_KEY);
			String src = generateHasKeyInterface(hki, t, tm);			
			writeIfGenerated(getSourceDir(), hki, src, generated, gm);			
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

    private String generateHasKeyInterface(JavaType hki, BaseTable t, TableMapper tm) throws IOException {
    	if (hki == null) {
    		return null;
    	}
    	
    	JavaType intf = tm.entityType(t, Part.INTERFACE);
    	
    	if (intf == null) {
    		return null;
    	}
    	    	
    	String src = getTemplateForHasKeyInterface();
    	
    	logger().info("generateHasKeyInterface: src 1=" + src);
    	
    	src = replaceAllWithComment(src, Tag.PACKAGE_NAME, hki.getPackageName());
    	src = replaceAll(src, Tag.HAS_KEY_INTERFACE, hki.getUnqualifiedName());    	
    	src = replaceAll(src, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());
    	
    	logger().info("generateHasKeyInterface: src 2=" + src);
    	
    	return src;
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
	
//	private String generateColumnList(Table t) {
//		StringBuffer buf = new StringBuffer();
//			
//		Identifier sn = t.getSchema().getUnqualifiedName();
//		Identifier tn = t.getUnqualifiedName();
//		String ce = name(sn.getName()) + name(tn.getName());			
//		
//		buf.append("enum ");
//		buf.append(ce);
//		buf.append(" {");
//		buf.append("\n");
//		
//		EnumSet<NameQualification> fq = EnumSet.allOf(NameQualification.class);		
//		generateColumnListElements(t, buf, fq);
//		
//		buf.append("\n");
//		buf.append("}");
//		
//		return buf.toString();
//	}
	
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
    
    private String getTemplateForHasKeyInterface() throws IOException {
        return read("HAS_KEY.in");
    }

    private String getTemplateForBuilderLinkerInit() throws IOException {
        return read("BUILDER_LINKER_INIT.in");
    }

    
    private String getTemplateForLiteralInnerTable() throws IOException {
        return read("LITERAL_INNER_TABLE.in");
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
		    final JavaType ref = tm.entityType(t, Part.REF);
		    
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
		        
		        {	
                    CharSequence source = generateRef(t, ref, intf, tm);                    
                    File root = getSourceDir(schema, Part.REF);
                    logger().debug("ref: " + source);                   
                    write(root, ref, source, generated, gm);
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

    private CharSequence generateRef(BaseTable t, JavaType ref, JavaType intf, TableMapper tm) 
    	throws IOException {
    	String src = getTemplateFor(Part.REF);
    	src = replaceAll(src, Tag.TABLE_INTERFACE_REF, ref.getUnqualifiedName());    	
	    src = replacePackageAndImports(src, intf);
	    src = replaceAll(src, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());    	
		return src;
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
	    
	    src = replacePackageAndImports(src, mt);

	    src = replaceAll(src, Tag.TABLE_INTERFACE, mt.getUnqualifiedName());
	    
        {
            String code = attributeKeyList(t, tm);
            logger().debug("generateInterface: attributeKeyList=" + code);            
            src = replaceAll(src, Tag.ATTRIBUTE_KEY_LIST, code);
        }

	    {
    	    String type = createAttributeType(getAttributeTemplate(), getAttributeType(), attrs(t, tm));
    	    src = replaceAll(src, "{{attribute-name-type}}", type);
	    }
	    
        {
            String type = createReferenceType(getReferenceTemplate(), getReferenceType(), refs(t, tm));
            src = replaceAll(src, "{{reference-name-type}}", type);
        }	    

//        {
//            String type = createEnumType(getEnumTemplate(), getReferenceType(), refs(t));
//            src = replaceAll(src, "{{reference-name-type}}", type);
//        }
        
        {
            String type = referenceKeyList(t, tm);
            src = replaceAll(src, Tag.REFERENCE_KEY_LIST, type);
        }
        

        {
            String hkl = implementedHasKeyList(t, tm);
            src = replaceAll(src, Tag.IMPLEMENTED_HAS_KEY_LIST, hkl);
        }

//        {
//            String type = createEnumType(getEnumTemplate(), "Query", queries(t));
//            src = replaceAll(src, "{{query-name-type}}", type);
//        }

        {
            String code = accessors(t, tm, false);
            src = replaceAll(src, "{{abstract-accessor-list}}", code);
        }
        
        {
            String code = valueAccessorList(t, tm, false);     
            src = replaceAll(src, Tag.VALUE_ACCESSOR_LIST, code);
        }

        
        {
        	String code = referenceList(t, tm);        	                 
            src = replaceAll(src, Tag.REFERENCE_LIST, code);
        }
        

	    return src;
	}

    private String implementedHasKeyList(BaseTable t, TableMapper tm) {    	    	
    	SchemaElementMap<ForeignKey> fkm = t.foreignKeys();
    	Set<BaseTable> ts = new HashSet<BaseTable>();
    	
    	for (ForeignKey k : fkm.values()) {
    		ts.add(k.getReferenced());    		
		}
    	
    	StringBuffer buf = new StringBuffer();
    	
    	JavaType intf = tm.entityType(t, Part.INTERFACE);
    	
    	// HasProjectKey<Reference, Type, HourReport, MetaData>, HasOrganizationKey<Reference, Type, HourReport, MetaData>
    	   	
    	
    	for (BaseTable r : ts) {
    		buf.append(", ");    		
    		JavaType kt = tm.entityType(r, Part.HAS_KEY);    		
    		buf.append(kt.getQualifiedName());
    		buf.append("<");
    		buf.append(getReferenceType());
    		buf.append(", Type, ");
    		buf.append(intf.getUnqualifiedName());
    		buf.append(", MetaData");
    		buf.append("> ");
		}
    	
		return buf.toString();
	}

	/**
     * Returns a comma separated list of the Ref -interfaces 
     * which the table interface for <code>t</code> implements.  
     * @param t
     * @param tm
     * @return
     */
    
	private String referenceList(BaseTable t, TableMapper tm) {
		StringBuffer buf = new StringBuffer();
		Collection<JavaType> rl = referenced(t, tm).values();
		
		JavaType intf = tm.entityType(t, Part.INTERFACE);
		
		for (JavaType jt : rl) {
			buf.append(", ");
			buf.append(jt.getQualifiedName());			
			buf.append("Ref");
			buf.append("<");
			buf.append(intf.getUnqualifiedName());
			buf.append(".");
			buf.append(getReferenceType());
			buf.append(">");
		}        	
		
		String code = buf.toString();
		return code;
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
//        JavaType hp = info.get(Part.HOOK);
//        JavaType itfp = info.get(Part.INTERFACE);
//        JavaType at = info.get(Part.ABSTRACT);
//
//        return (hp != null) ? hp : (at != null) ? at : itfp;
        
        JavaType itfp = info.get(Part.INTERFACE);
        return itfp;
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

	private String read(String resource)
	    throws IOException {
        InputStream template = getClass().getResourceAsStream(resource);
        String src = new IOHelper().read(template, "UTF-8", 1024);
        return src;
	}

    private String createAttributeType(String template, String name, String constants) {    	
        String src = replaceAll(template, "{{attribute-type}}", name);
        src = replaceAll(src, "{{attribute-constants}}", constants);
        return src;
	}
    
    private String createReferenceType(String template, String name, String constants) {    	
        String src = replaceAll(template, "{{reference-type}}", name);
        src = replaceAll(src, "{{reference-constants}}", constants);
        return src;
	}	

	private String getAttributeTemplate() throws IOException {
	       return read("attribute-type.in");
	}
	
	private String getReferenceTemplate() throws IOException {
	       return read("reference-type.in");
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
                                
        boolean qualify = hasAmbiguousSimpleNamesForReferenceKeys(t, tm);
        
        {
            String code = accessors(t, tm, true);
            src = replaceAll(src, Tag.ACCESSOR_LIST, code);
        }
        
        {
            String code = referenceKeyClassList(t, tm, qualify);
            logger().debug("generateImplementation: referenceKeyClassList=" + code);            
            src = replaceAll(src, Tag.REFERENCE_KEY_CLASS_LIST, code);
        }        

        {
            String code = attributeKeyMapList(t, tm);
            logger().debug("generateImplementation: code=" + code);            
            src = replaceAll(src, Tag.ATTRIBUTE_KEY_MAP_LIST, code);
        }
        
        {
            String code = referenceKeyMapList(t, tm, qualify);
            logger().debug("generateImplementation: code=" + code);            
            src = replaceAll(src, Tag.REFERENCE_KEY_MAP_LIST, code);        	
        }
        
        {
            String code = builderLinkerInitList(t, tm, qualify);
            logger().debug("builderLinkerInitList: code=" + code);            
            src = replaceAll(src, Tag.BUILDER_LINKER_INIT, code);        	
        }
        
        {
            String code = generateCreateIdentityMapMethod(t, tm);
            logger().debug("generateCreateIdentityMapMethod: code=" + code);            
            src = replaceAll(src, Tag.CREATE_IDENTITY_MAP_METHOD, code);        	
        }
        
        
        {
            String code = referenceMapList(t, tm, qualify);
            logger().debug("generateImplementation: code=" + code);            
            src = replaceAll(src, Tag.REFERENCE_MAP_LIST, code);        	
        }

                
        {
            String code = metaDataInitialization(t, tm, qualify);
            src = replaceAll(src, Tag.META_DATA_INITIALIZATION, code);
        }        
        
        
        
//        {
//            String code = columnKeyMapPopulation(t, tm);
//            src = replaceAll(src, Tag.COLUMN_KEY_MAP_POPULATION, code);
//        }
        
//        {
//            String code = keyAccessorList(t, tm);           
//            src = replaceAll(src, Tag.KEY_ACCESSOR_LIST, code);
//        }
        
        {
            String code = valueVariableList(t, tm);           
            src = replaceAll(src, Tag.VALUE_VARIABLE_LIST, code);
        }
        
        {
            String code = valueAccessorList(t, tm, true);
            logger().debug("generateImplementation: code=" + code);
            src = replaceAll(src, Tag.VALUE_ACCESSOR_LIST, code);
        }

        return src;
	}
	
	private String builderLinkerInitList(BaseTable referencing, TableMapper tm,
			boolean qualify) throws IOException {
		StringBuffer buf = new StringBuffer();
				
		for (ForeignKey fk : referencing.foreignKeys().values()) {			
			String c = formatBuilderLinkerInit(fk, tm);
			buf.append(c);			
			buf.append("\n\n");
		}
		
		
		return buf.toString();			
	}

	private String generateCreateIdentityMapMethod(BaseTable t, TableMapper tm) {
		PrimaryKey pk = t.getPrimaryKey();
		
		StringBuffer buf = new StringBuffer();
		
		if (pk != null) {
			List<? extends Column> cl = pk.columns();
			
			if (cl.size() == 1) {
				Column col = cl.get(0);				
				AttributeInfo ai = tm.getAttributeInfo(t, col);

				
//				Sample output: 
//				@Override
//				public IdentityMap<Attribute, Reference, Type, TestGeneratedKey> createIdentityMap() {
//					return new IntIdentityMap<Attribute, Reference, Type, TestGeneratedKey>(TestGeneratedKey.ABC);
//				} 
				
				Class<?> aim = ai.getIdentityMapType();
								
				if (aim != null) {
					JavaType intf = tm.entityType(t, Part.INTERFACE);
					
					String kv = keyConstantVariable(t, col);
															
					buf.append("@Override\n");
					buf.append("public ");
					buf.append(EntityIdentityMap.class.getCanonicalName());
					buf.append("<");
					buf.append(getAttributeType());
					buf.append(", ");
					buf.append(getReferenceType());
					buf.append(", Type, ");
					buf.append(intf.getUnqualifiedName());
					buf.append("> createIdentityMap() {\n");
					buf.append("return new ");
					buf.append(aim.getCanonicalName());
					buf.append("<");
					buf.append(getAttributeType());
					buf.append(", ");
					buf.append(getReferenceType());
					buf.append(", Type, ");
					buf.append(intf.getUnqualifiedName());
					buf.append(">(");
					buf.append(intf.getUnqualifiedName());
					buf.append(".");
					buf.append(kv);					
					buf.append(");\n");					
					buf.append("}\n");							
				}				
			}			
		}
		
		
				
		return buf.toString();
	}

	private String referenceMapList(BaseTable t, TableMapper tm, boolean qualify) {
		StringBuffer buf = new StringBuffer();
		
		Map<BaseTable, JavaType> map = referenced(t, tm);
		
		for (Entry<BaseTable, JavaType> e : map.entrySet()) {
			String c = formatReferenceValueMap(t, e.getKey(), e.getValue(), tm, qualify);
			buf.append(c);			
		}
		
		
		return buf.toString();
	}

	private String formatReferenceValueMap(BaseTable t, BaseTable referenced,
			JavaType target, TableMapper tm, boolean qualify) {
		
		
//		private Map<Reference, Organization.Holder> om = new HashMap<Reference, Organization.Holder>(); 
//
//		@Override
//		public fi.tnie.db.gen.ent.personal.Organization.Holder getOrganization(
//				fi.tnie.db.gen.ent.personal.Organization.Key<?, ?, ?, ?, ?> ok) {
//			return om.get(ok.name());
//		}		
		
		StringBuffer buf = new StringBuffer();
		
		String vm = referenceValueMapVariable(referenced, target, qualify);
		
		String hn = target.getQualifiedName() + ".Holder";
						
		buf.append("private java.util.Map<");
		buf.append(getReferenceType());
		buf.append(", ");
		buf.append(hn);		
		buf.append(">");
		buf.append(" ");
		buf.append(vm);		
		buf.append(" = new java.util.HashMap<");
		buf.append(getReferenceType());
		buf.append(", ");
		buf.append(hn);		
		buf.append(">();\n\n");
		
		buf.append("@Override\npublic ");
		buf.append(hn);
		buf.append(" get");
		buf.append(target.getUnqualifiedName());
		buf.append("(");
		buf.append(target.getUnqualifiedName());
		buf.append(".Key<");
		buf.append(getReferenceType());
		buf.append(", ?, ?, ?> key) {\n");
		buf.append("\treturn this.");
		buf.append(vm);
		buf.append(".get(key.name());\n");
		buf.append("}\n\n");
		
		buf.append("@Override\npublic void ");
		buf.append("set");
		buf.append(target.getUnqualifiedName());
		buf.append("(");
		buf.append(target.getQualifiedName());
		buf.append(".Key<");
		buf.append(getReferenceType());
		buf.append(", ?, ?, ?> key, ");
		buf.append(target.getQualifiedName());
		buf.append(".Holder newValue) {\n");		
		buf.append("\tthis.");
		buf.append(vm);
		buf.append(".put(key.name(), newValue);\n");
		buf.append("}\n\n");		
		
		return buf.toString();
	}

	/***  
	 * @param table
	 * @return
	 */	
	private boolean hasAmbiguousSimpleNamesForReferenceKeys(BaseTable table, TableMapper tm) {
		boolean ambiguous = false;
		
		Map<String, String> names = new HashMap<String, String>();
		
		for (ForeignKey k : table.foreignKeys().values()) {
			BaseTable referenced = k.getReferenced();			
			JavaType t = tm.entityType(referenced, Part.INTERFACE);
			
			if (t != null) {
				String uin = t.getUnqualifiedName();								
				String qtbl = names.get(uin);
				
				if (qtbl == null) {
					names.put(uin, referenced.getQualifiedName());
				}
				else {
					String tn = referenced.getQualifiedName();
					
					if (!tn.equals(qtbl)) {
						ambiguous = true;
						break;
					}
				}				
			}			
		}		
		
		return ambiguous;
	}
	
	
	private Map<BaseTable, JavaType> referenced(BaseTable table, TableMapper tm) {			
		Map<BaseTable, JavaType> map = new HashMap<BaseTable, JavaType>();
		
		for (ForeignKey k : table.foreignKeys().values()) {
			BaseTable referenced = k.getReferenced();
						
			if (!map.containsKey(referenced)) {
				JavaType t = tm.entityType(referenced, Part.INTERFACE);
												
				if (t != null) {					
					map.put(referenced, t);
				}
			}
		}
		
		return map;
	}	
	
	private String referenceKeyClassList(BaseTable table, TableMapper tm, boolean qualify) {
		StringBuffer buf = new StringBuffer();		
		Map<BaseTable, JavaType> map = referenced(table, tm);
		
		for (Entry<BaseTable, JavaType> r : map.entrySet()) {
			buf.append(formatReferenceKeyClass(table, r.getKey(), qualify, tm));
		}
				
		return buf.toString();
	}

	private String formatReferenceKeyClass(BaseTable referencing, BaseTable referenced, boolean qualify, TableMapper tm) {

//		// Sample output:
//		private static class OrganizationKey
//		extends Organization.Key<Person.Reference, Person.Type, Person, Person.MetaData> {
//
//		private static final long serialVersionUID = 1L;
//
//		protected OrganizationKey(
//				EntityMetaData<fi.tnie.db.gen.ent.personal.Person.Attribute, Reference, Type, Person> meta, Reference name) {
//			super(meta, name);
//		}
		
		// TODO: implement get()
//		@Override
//		public fi.tnie.db.gen.ent.personal.Organization.Holder get(Person e) {
//			return e.getOrganization(this);
//		}

//
//		@Override
//		public OrganizationKey self() {
//			return this;		
//		}	

//		@Override
//		public void set(Person e, fi.tnie.db.gen.ent.personal.Organization.Holder newValue) {		
//			e.setOrganization(self(), newValue);
//		}		
//		
//	}		
		
		JavaType src = tm.entityType(referencing, Part.INTERFACE);		
		JavaType target = tm.entityType(referenced, Part.INTERFACE);
				
		StringBuffer buf = new StringBuffer();
		buf.append("private static class ");
		
		String n = referenceKeyImplementationName(referenced, target, qualify);
		
//		String kta = keyTypeArgs(tm, referencing, true);
		String kta = referenceKeyTypeArgs(tm, referencing, true);
				
		buf.append(n);
		buf.append(" extends ");
		buf.append(target.getQualifiedName());
		buf.append(".Key<");
		buf.append(kta);
		buf.append("> {\n");
		buf.append("private static final long serialVersionUID = 1L;\n");		
		buf.append("private ");		
		buf.append(n);
		buf.append("(");
		buf.append(src.getUnqualifiedName());
		buf.append(".MetaData");
		buf.append(" meta, ");
		buf.append(getReferenceType());
		buf.append(" name) {\n");
		buf.append("super(meta, name);");
		buf.append("}\n\n");
		
		buf.append("@Override\n");
		buf.append("public ");
		buf.append(target.getQualifiedName());
		buf.append(".Holder get(");
		buf.append(src.getUnqualifiedName());
		buf.append(" e) {\n");		
		buf.append("return e.get");
		buf.append(target.getUnqualifiedName());		
		buf.append("(this);");
		buf.append("}\n");
		
		buf.append("@Override\n");
		buf.append("public ");
		buf.append(n);
		buf.append(" self() {\n");
		buf.append("return this;\n");
		buf.append("}\n");
				
		buf.append("@Override\n");
		buf.append("public void ");
		buf.append("set(");
		buf.append(src.getUnqualifiedName());
		buf.append(" e, ");		
		buf.append(target.getQualifiedName());
		buf.append(".Holder newValue");
		buf.append(" ) {\n");
		buf.append("\te.set");
		buf.append(target.getUnqualifiedName());
		buf.append("(self(), newValue);");
		buf.append("}\n");		
		
		buf.append("}");
		
		return buf.toString();
	}
	
	private String referenceKeyReferenceTypeName(BaseTable referencing, BaseTable referenced, TableMapper tm, boolean qualify) {
		StringBuffer nb = new StringBuffer();
		
		if (qualify) {
			Schema s = referenced.getSchema();
			String prefix = (s == null) ? "" : name(s.getUnqualifiedName().getName());
			nb.append(prefix);
		}
		
		// Project.Key<HourReport.Reference, Type, HourReport, HourReport.MetaData, ?>
		
		JavaType source = tm.entityType(referencing, Part.INTERFACE);
		JavaType target = tm.entityType(referenced, Part.INTERFACE);
		
		nb.append(target.getUnqualifiedName());
		nb.append(".");
		nb.append("Key<");
		nb.append(source.getUnqualifiedName());
		nb.append(".");
		nb.append(getReferenceType());
		nb.append(", ");
		nb.append(source.getUnqualifiedName());
		nb.append(".Type, ");
		nb.append(source.getUnqualifiedName());
		nb.append(", ");
		nb.append(source.getUnqualifiedName());
		nb.append(".MetaData");		
		nb.append(">");
		
		return nb.toString();
	}
	
		

	private String referenceKeyImplementationName(BaseTable referenced, JavaType target, boolean qualify) {
		StringBuffer nb = new StringBuffer();
		
		if (qualify) {
			Schema s = referenced.getSchema();
			String prefix = (s == null) ? "" : name(s.getUnqualifiedName().getName());
			nb.append(prefix);
		}
		
		nb.append(target.getUnqualifiedName());
		nb.append("Key");
		
		return nb.toString();
	}
	
	private String referenceKeyMapVariable(BaseTable referenced, JavaType target, boolean qualify) {
		StringBuffer nb = new StringBuffer();
		
		if (qualify) {
			Schema s = referenced.getSchema();
			String prefix = (s == null) ? "" : name(s.getUnqualifiedName().getName());
			nb.append(prefix);
		}
		
		nb.append(decapitalize(target.getUnqualifiedName()));
		nb.append("KeyMap");		
				
		return nb.toString();
	}
	
	private String referenceValueMapVariable(BaseTable referenced, JavaType target, boolean qualify) {
		StringBuffer nb = new StringBuffer();
		
		if (qualify) {
			Schema s = referenced.getSchema();
			String prefix = (s == null) ? "" : name(s.getUnqualifiedName().getName());
			nb.append(prefix);
		}
		
		nb.append(decapitalize(target.getUnqualifiedName()));
		nb.append("Map");		
				
		return nb.toString();
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
		Class<?> at = a.getAccessorType();
        Class<?> kt = a.getKeyType();
                        
        if (kt == null || at == null) {
        	return "";
        }
        
//        JavaType intf = tm.entityType(t, Part.INTERFACE);
        
        buf.append("private transient ");
        buf.append(at.getCanonicalName());
        buf.append("<");
        buf.append(keyTypeArgs(tm, t, false));
        buf.append(">");
        buf.append(" ");
        buf.append(valueVariableName(t, c));
        buf.append(" = null;\n");

		return buf.toString();
	}

	private String keyTypeArgs(TableMapper tm, BaseTable t, boolean reference) {
		JavaType intf = tm.entityType(t, Part.INTERFACE);
		StringBuffer buf = new StringBuffer();		
		buf.append(reference ? getReferenceType() : getAttributeType());        
        buf.append(", ");
//        buf.append(getReferenceType());        
//        buf.append(", ");        
        buf.append("Type");        
        buf.append(", ");        
        buf.append(intf.getUnqualifiedName());
        return buf.toString();
	}
	
	private String referenceKeyTypeArgs(TableMapper tm, BaseTable t, boolean reference) {
		JavaType intf = tm.entityType(t, Part.INTERFACE);
		StringBuffer buf = new StringBuffer();		
		buf.append(getReferenceType());        
        buf.append(", ");
        buf.append("Type");        
        buf.append(", ");        
        buf.append(intf.getUnqualifiedName());
        buf.append(", ");        
        buf.append(intf.getUnqualifiedName());
        buf.append(".MetaData");
        return buf.toString();
	}
	
	
	private String formatValueAccessor(BaseTable t, Column c, TableMapper tm, boolean impl) {
		
//		// output example: 
//	    public VarcharValue<Attribute, Person> lastName() {
//	    	if (this.lastName == null) {
//	    		// this.lastName = varcharValue(Person.LAST_NAME);
//				this.lastName = new VarcharValue<Person, Attribute>(self(), Person.LAST_NAME);
//	    	}
//	    	
//	    	return this.lastName;
//	    }		
		
		StringBuffer buf = new StringBuffer();

		AttributeInfo a = tm.getAttributeInfo(t, c);
		Class<?> at = a.getAccessorType();
		Class<?> kt = a.getKeyType();
                        
        if (kt == null || at == null) {
        	return "";
        }
        
        logger().debug("formatValueAccessor: value-type=" + at);
        
        JavaType intf = tm.entityType(t, Part.INTERFACE);
        String vv = valueVariableName(t, c);
        String ke = keyConstantExpression(t, c, intf);
                    
        if (impl) {
        	buf.append("public ");
        }
        
        String ktargs = keyTypeArgs(tm, t, false);
        
        buf.append(at.getCanonicalName());
        buf.append("<");        
        buf.append(ktargs);                
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
	        buf.append(" = new ");
	        buf.append(at.getCanonicalName());
	        buf.append("<");
	        buf.append(ktargs);
	        buf.append(">(self(), ");
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

	private String metaDataInitialization(BaseTable t, TableMapper tm, boolean qualify) {
		StringBuffer buf = new StringBuffer();		
		buf.append(attributeKeyInitialization(t, tm));		
		buf.append(referenceKeyInitialization(t, tm, qualify));		
        return buf.toString();	
     }

	private String attributeKeyInitialization(BaseTable t, TableMapper tm) {
		List<Column> acl = getAttributeColumnList(t, tm);        
        StringBuffer content = new StringBuffer();        
        
        for (Column c : acl) {
        	AttributeInfo a = tm.getAttributeInfo(t, c);
        	        	        	
        	Class<?> kt = a.getKeyType();
        	
        	if (kt == null) {
        		logger().warn("no key type for column " + c.getUnqualifiedName());
        	}
        	else {
	        	content.append(kt.getCanonicalName());
	        	content.append(".get(this, Attribute.");        	
	        	content.append(attr(c));
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
			if (o.intValue() >= 1) {
				if (k == null) {
					throw new NullPointerException("no key-type");		
				}				
								
				String c = formatAttributeKeyMap(t, k, o.intValue(), tm);
				buf.append(c);
			}
		}
		
		
		return buf.toString();
	}
	
	
	private String referenceKeyMapList(BaseTable referencing, TableMapper tm, boolean qualify) {
		StringBuffer buf = new StringBuffer();
		
		Map<BaseTable, JavaType> map = referenced(referencing, tm);
		
		for (Entry<BaseTable, JavaType> e : map.entrySet()) {
			String c = formatReferenceKeyMap(referencing, e.getKey(), e.getValue(), tm, qualify);
			buf.append(c);			
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
        		logger().warn("no key type for column : " + c.getColumnName().getName());
        		logger().warn("column-data-type : " + c.getDataType().getDataType());
        		logger().warn("column-type-name : " + c.getDataType().getTypeName());        		
        	}
        	else {
            	Integer o = tom.get(k);
            	o = (o == null) ? Integer.valueOf(1) : Integer.valueOf(o.intValue() + 1);
            	tom.put(k, o);        		
        	}
        }
        
        return tom;
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
	
	private String referenceKeyList(BaseTable t, TableMapper tm) {
		StringBuffer content = new StringBuffer();
		
		for (ForeignKey fk : t.foreignKeys().values()) {
		    String r = formatReferenceKey(fk, tm);
		    
		    if (r == null || r.equals("")) {
		        continue;
		    }
		    
		    content.append(r);		    
		}

		return content.toString();
    }	
	
	private String referenceKeyInitialization(BaseTable t, TableMapper tm, boolean qualify) {
		StringBuffer content = new StringBuffer();
		
//		// sample output:
//		{
//			ProjectKey pk = new ProjectKey(this, HourReport.Reference.FK_HHR_PROJECT);
//			projectKeyMap.put(pk.name(), pk);
//			entityKeyMap.put(pk.name(), pk);
//		}
		
		JavaType jt = tm.entityType(t, Part.INTERFACE);
		
		for (ForeignKey fk : t.foreignKeys().values()) {
			content.append("{\n");
			
			BaseTable r = fk.getReferenced();			
			JavaType kt = tm.entityType(r, Part.INTERFACE);
			
			String n = referenceKeyImplementationName(r, kt, qualify);
			content.append(n);
			content.append(" k = new ");
			content.append(n);
			content.append("(this, ");
			content.append(jt.getQualifiedName());
			content.append(".");
			content.append(getReferenceType());
			content.append(".");
			content.append(referenceName(fk));
			content.append(");\n");
			
			String mv = referenceKeyMapVariable(t, kt, qualify);
			content.append(mv);
			content.append(".put(k.name(), k);\n");
			content.append("entityKeyMap.put(k.name(), k);\n");			
									
		    content.append("}\n");	    
		}

		return content.toString();
    }	
	
	private String formatReferenceKey(ForeignKey fk, TableMapper tm) {
		
		// Organization.Key<Attribute, Reference, Type, Person, ?> EMPLOYER = fi.tnie.db.gen.ent.personal.PersonImpl.PersonMetaData.getInstance().getOrganizationKey(Reference.EMPLOYER);
		String referenceName = referenceName(fk);
				
		JavaType tt = tm.entityType(fk.getReferenced(), Part.INTERFACE);
		JavaType rt = tm.entityType(fk.getReferencing(), Part.INTERFACE);
		JavaType it = tm.entityType(fk.getReferencing(), Part.IMPLEMENTATION);
		
		StringBuffer buf = new StringBuffer();
						
		buf.append("public static final ");
			
		
		buf.append(tt.getQualifiedName());
		buf.append(".Key");
		buf.append("<");
		buf.append(it.getUnqualifiedName());		
		buf.append(".");
		buf.append(getReferenceType());
		buf.append(", ");
		buf.append(it.getUnqualifiedName());		
		buf.append(".");
		buf.append("Type, ");
		buf.append(rt.getUnqualifiedName());
		buf.append(", ");
		buf.append(rt.getUnqualifiedName());
		buf.append(".MetaData");
		buf.append("> ");
		buf.append(referenceName);
		buf.append(" = ");
		buf.append(it.getQualifiedName());
		buf.append(".");
		buf.append(rt.getUnqualifiedName());
		buf.append("MetaData.getInstance().get");
		buf.append(tt.getUnqualifiedName());
		buf.append("Key(");
		buf.append(getReferenceType());
		buf.append(".");
		buf.append(referenceName);	
		buf.append(");\n");
		
		return buf.toString();
	}

	private String formatAttributeKeyMap(BaseTable t, Class<?> keyType, int occurences, TableMapper tm) {
		StringBuffer buf = new StringBuffer();
		
//        JavaType intf = tm.entityType(t, Part.INTERFACE);
                        
        // goal:
        // private Map<Attribute, IntegerKey<Attribute, Person>> integerKeyMap = new HashMap<Attribute, IntegerKey<Attribute,Person>>();
        
        String ktargs = keyTypeArgs(tm, t, false);
        
        buf.append("private java.util.Map<");
        buf.append("Attribute");
        buf.append(", ");
        buf.append(keyType.getCanonicalName());
        buf.append("<");
        buf.append(ktargs);
        buf.append(">> ");
        buf.append(getKeyMapVariable(keyType));        
        buf.append(" = new java.util.HashMap<");
        buf.append("Attribute");
        buf.append(", ");
        buf.append(keyType.getCanonicalName());
        buf.append("<");        
        buf.append(ktargs);
        buf.append(">>();\n");

        
        
        String kt = null;
        
        if (keyType.getEnclosingClass() == null) {
        	kt = getSimpleName(keyType);
        }
        else {
        	kt = getSimpleName(keyType, true) + getSimpleName(keyType.getEnclosingClass());
        }
                
        buf.append("@Override\n");
        buf.append("protected java.util.Map<");
        buf.append("Attribute");
        buf.append(", ");
        buf.append(keyType.getCanonicalName());
        buf.append("<");
        buf.append(ktargs);        
        buf.append(">> get");
        buf.append(kt);
        buf.append("Map() {");
        buf.append("return this.");
        buf.append(getKeyMapVariable(keyType));
        buf.append("; }\n\n");
                
        return buf.toString();		
	}
	
	private String formatReferenceKeyMap(BaseTable t, BaseTable referenced, JavaType target, TableMapper tm, boolean qualify) {
		StringBuffer buf = new StringBuffer();
		
        JavaType intf = tm.entityType(t, Part.INTERFACE);
        

// sample output:
// private java.util.Map<HourReport.Reference, ProjectKey> projectKeyMap = new java.util.HashMap<HourReport.Reference, ProjectKey>();

//		private java.util.Map<HourReport.Reference, ProjectKey> projectKeyMap = new java.util.HashMap<HourReport.Reference, ProjectKey>();
//
//		public fi.tnie.db.gen.ent.personal.Project.Key<Attribute, Reference, Type, HourReport, ?> getProjectKey(Reference ref) {
//			if (ref == null) {
//				throw new NullPointerException("ref");
//			}
//			
//			return this.projectKeyMap.get(ref);
//		}
        
//        private java.util.Map<HourReport.Reference, Project.Key<Reference, Type, HourReport, HourReport.MetaData, ?>> projectKeyMap = 
//        	new java.util.HashMap<HourReport.Reference, Project.Key<Reference, Type, HourReport, HourReport.MetaData, ?>>();
//
//        public fi.tnie.db.gen.ent.personal.Project.Key<Reference, Type, HourReport, HourReport.MetaData, ?> getProjectKey( Reference ref) {
//        	if (ref == null) {
//        		throw new NullPointerException("ref");
//        	}
//        	
//        	return this.projectKeyMap.get(ref);
//        }
        
        
        String rki = referenceKeyReferenceTypeName(t, referenced, tm, qualify);
        String rkimp = referenceKeyImplementationName(referenced, target, qualify);
        String rkv = referenceKeyMapVariable(referenced, target, qualify);
                        
        buf.append("private java.util.Map<");
        buf.append(intf.getUnqualifiedName());
        buf.append(".");
        buf.append(getReferenceType());
        buf.append(", ");
        buf.append(rki);
        buf.append("> ");
        buf.append(rkv);
        buf.append(" = new java.util.HashMap<");
        buf.append(intf.getUnqualifiedName());
        buf.append(".");
        buf.append(getReferenceType());
        buf.append(", ");
        buf.append(rki);
        buf.append(">();\n\n");
        
        buf.append("public ");
        buf.append(target.getQualifiedName());
        buf.append(".Key<");
        buf.append(getReferenceType());
        buf.append(", Type, ");
        buf.append(intf.getUnqualifiedName());
        buf.append(", ");
        buf.append(intf.getUnqualifiedName());
        buf.append(".MetaData");
        buf.append(">");
        buf.append(" get");
        buf.append(rkimp);        
        buf.append("( ");
        buf.append(getReferenceType());
        buf.append(" ref) {\n");
        buf.append("if (ref == null) {\n");
        buf.append("throw new NullPointerException(\"ref\");");
        buf.append("}\n");        
        buf.append("return this.");
        buf.append(rkv);
        buf.append(".get(ref);\n");
        buf.append("}\n\n");
                
        return buf.toString();		
	}	
	
	private String formatBuilderLinkerInit(ForeignKey fk, TableMapper tm) 
		throws IOException {
		
		// sample output:
//      {
//      	final Project.Key<Reference, Type, HourReport, HourReport.MetaData> pk = FK_HHR_PROJECT;					
//      	ForeignKey fk = m.getForeignKey(pk.name());				
//      	TableReference tref = ctx.getQuery().getReferenced(tableRef, fk);
//
//      	if (tref != null) {
//      		Project.MetaData pm = ProjectImpl.ProjectMetaData.getInstance();
//      		final fi.tnie.db.gen.ent.personal.Project.Builder nb = pm.newBuilder();						
//
//      		ll.add(new Linker() {							
//      			@Override
//      			public void link(DataObject src, HourReport dest) {
//      				Project np = nb.read(src);								
//      				pk.set(dest, np);
//      			}
//      		});
//      	} 
//      }
		
        JavaType intf = tm.entityType(fk.getReferencing(), Part.INTERFACE);
        JavaType ritf = tm.entityType(fk.getReferenced(), Part.INTERFACE);
        
        String src = getTemplateForBuilderLinkerInit();
                
        src = replaceAll(src, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());        
        src = replaceAll(src, Tag.REFERENCED_TABLE_INTERFACE_QUALIFIED, ritf.getQualifiedName());
        
        src = replaceAll(src, Tag.REFERENCE_KEY_VARIABLE, referenceName(fk));
                        
        return src;		
	}	

	private String formatAttributeKey(BaseTable t, Column c, TableMapper tm) {
		StringBuffer buf = new StringBuffer();

		AttributeInfo a = tm.getAttributeInfo(t, c);		
        Class<?> kc = a.getKeyType();
        
        JavaType intf = tm.entityType(t, Part.INTERFACE);
                        
        if (kc == null) {
        	return "";
        }
        
        JavaType impl = tm.entityType(t, Part.IMPLEMENTATION);
        
        String n = null;
        
        if (kc.getEnclosingClass() == null) {
        	n = getSimpleName(kc);
        }
        else {
        	n = getSimpleName(kc, true) + getSimpleName(kc.getEnclosingClass());
        }
        
        // public static final IntegerKey<Attribute, Person> ID = new IntegerKey<Attribute, Person>(Attribute.ID);"
        
        buf.append("public static final ");
        buf.append(kc.getCanonicalName());
        buf.append("<");
        buf.append(keyTypeArgs(tm, t, false));
        buf.append("> ");
        buf.append(keyConstantVariable(t, c));
        buf.append(" = ");
        buf.append(impl.getQualifiedName());
        buf.append(".");
        buf.append(intf.getUnqualifiedName());
        buf.append("MetaData.getInstance().get");        
        buf.append(n);
        buf.append("(");
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
//        final String attributeName = attr(c);
        Class<?> attributeType = info.getAttributeType();
        Class<?> holderType = info.getHolderType();
        
        final String type = attributeType.getCanonicalName();

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
    	return getSimpleName(attributeType, false);
    }

	private String getSimpleName(Class<?> attributeType, boolean canon) {
		String tn = canon ? attributeType.getCanonicalName() : attributeType.getName(); 
		int pos = tn.lastIndexOf(".");                
		tn = tn.substring(pos < 0 ? 0 : pos + 1);
		return tn;
	}
	
	
	private String getKeyMapVariable(Class<?> keyType) {
		StringBuffer buf = new StringBuffer();
		
		String n = getSimpleName(keyType) + "Map";
		
		n = n.replace("$", "");		
//		n = n.replace('$', '_');		
		
		decapitalize(n, buf);
		return buf.toString();
	}
    
    /**
     * Converts SQL style name to camel case 
     * 
     * @param identifier
     * @return
     */
    public String name(CharSequence identifier) {
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

    private String attrs(BaseTable t, TableMapper tm) {
        StringBuffer buf = new StringBuffer();
        attrs(t, tm, buf);
        return buf.toString();
    }
    
    private String decapitalize(String identifier) {
    	if ((identifier == null) || identifier.equals("")) {			
    		return identifier;
    	}
    	
    	StringBuffer buf = new StringBuffer(identifier);
    	buf.setCharAt(0, Character.toLowerCase(identifier.charAt(0)));
    	return buf.toString();
    }

	private void attrs(BaseTable t, TableMapper tm, StringBuffer content) {
		List<Column> cols = getAttributeColumnList(t, tm);
		
 
		
		for (Column c : cols) {
			AttributeInfo ai = tm.getAttributeInfo(t, c);
			
			
			if (ai.getKeyType() == null) {
	    		logger().warn("no key type for column : " + c.getColumnName().getName());
	    		logger().warn("column-data-type : " + c.getDataType().getDataType());
	    		logger().warn("column-type-name : " + c.getDataType().getTypeName());
			}
			
			
			content.append(attr(c));
			content.append("(");			
			PrimitiveType<?> pt = ai.getPrimitiveType();
			
			if (pt == null) {
				content.append("null");
			}
			else {
				content.append(pt.getClass().getCanonicalName());
				content.append(".TYPE");
			}
			
			content.append("),");
		}
	}

	private String refs(BaseTable t, TableMapper tm) {
	    StringBuffer content = new StringBuffer();
	    refs(t, content, tm);
	    return content.toString();
	}

	private void refs(BaseTable t, StringBuffer content, TableMapper tm) {
//		List<String> elements = new ArrayList<String>();
		
		
		for (ForeignKey fk : t.foreignKeys().values()) {
		    String r = formatReferenceConstant(fk, tm);
		    
		    if (r == null || r.equals("")) {
		        continue;
		    }
		    content.append(r);
		    content.append(",");
//			elements.add();
		}

//		content.append(enumMember(getReferenceType(), elements));
	}

	private String formatReferenceConstant(ForeignKey fk, TableMapper tm) {
		final String kn = fk.getUnqualifiedName().getName();
		
		String n = referenceName(fk);
						
		JavaType ref = tm.entityType(fk.getReferenced(), Part.INTERFACE);
		
		if (ref == null) {
			return null;
		}

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
			buf.append(", ");
			buf.append(ref.getQualifiedName());
			buf.append(".TYPE");
			buf.append(")");

			expr = buf.toString();
		}

		return expr;
	}

	private String referenceName(ForeignKey fk) {
		final String kn = fk.getUnqualifiedName().getName();
		String t = fk.getReferencing().getUnqualifiedName().getName().toUpperCase();
		String p = "^(FK_)?(" + Pattern.quote(t) + "_)";
		logger().debug("input {" + kn.toUpperCase() + "}");
		logger().debug("p {" + p + "}");
		String n = kn.toUpperCase().replaceFirst(p, "");
		return n;
	}


//	private String queries(BaseTable t) {
//	    StringBuffer content = new StringBuffer();
//	    queries(t, content);
//	    return content.toString();
//	}



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

