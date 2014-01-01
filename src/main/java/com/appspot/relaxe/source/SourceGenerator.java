/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
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
package com.appspot.relaxe.source;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.build.SchemaFilter;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.im.EntityIdentityMap;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.SchemaName;
import com.appspot.relaxe.map.AttributeInfo;
import com.appspot.relaxe.map.JavaType;
import com.appspot.relaxe.map.TableMapper;
import com.appspot.relaxe.map.TypeMapper;
import com.appspot.relaxe.map.TableMapper.Part;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeImpl;
import com.appspot.relaxe.meta.EmptyForeignKeyMap;
import com.appspot.relaxe.meta.Environment;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.IdentifierRules;
import com.appspot.relaxe.meta.ImmutableColumnMap;
import com.appspot.relaxe.meta.ImmutablePrimaryKey;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.SchemaElement;
import com.appspot.relaxe.meta.SchemaElementMap;
import com.appspot.relaxe.meta.Table;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.types.AbstractPrimitiveType;
import com.appspot.relaxe.types.PrimitiveType;

import fi.tnie.util.io.IOHelper;

public class SourceGenerator {

	public enum Tag {
		/**
		 * Pattern which is replaced with the simple name of the table interface
		 * in template files.
		 */
		TABLE_INTERFACE, HAS_KEY_INTERFACE, HAS_INTERFACE,

		REFERENCED_TABLE_INTERFACE_QUALIFIED, TABLE_INTERFACE_REF,
		/**
		 * Pattern which is replaced with the simple name of the abstract class
		 * in template files.
		 */
		TABLE_ABSTRACT_CLASS,
		/**
		 * Pattern which is replaced with the simple name of the hook class in
		 * template files.
		 */
		TABLE_HOOK_CLASS,

		/**
		 * Pattern which is replaced with the simple name of the class the
		 * hook-class inherits from in template files.
		 */
		TABLE_HOOK_BASE_CLASS,

		/**
	     *
	     */
		LITERAL_TABLE_ENUM,

		/**
		 * Pattern which is replaced with the simple name of the hook class in
		 * template files.
		 */
		TABLE_IMPL_CLASS,
		/**
		 * Pattern which is replaced with the simple name of the hook class in
		 * template files.
		 */
		TABLE_IMPL_BASE,

		TABLE_ENUM_INIT_TYPE,

		INIT_COLUMN_ENUM_LIST,
		/**
		 * TODO: which accessors
		 */
		ACCESSOR_LIST,
		
		ENTITY_ATTRIBUTE_ACCESSOR_SIGNATURE_LIST,
		
		ENTITY_ATTRIBUTE_ACCESSOR_LIST,

		/**
		 * static attribute key declarations in interface
		 */
		ATTRIBUTE_KEY_LIST, 
		REFERENCE_KEY_VARIABLE,

		REFERENCE_KEY_LIST,

		QUERY_ELEMENT_VARIABLE_LIST, QUERY_ELEMENT_GETTER_BODY, QUERY_ELEMENT_SETTER_BODY, QUERY_ELEMENT_ASSIGNMENT_LIST, QUERY_ELEMENT_NEW_BUILDER_BODY, QUERY_ELEMENT_DEFAULT_CONSTRUCTOR_BODY, QUERY_ELEMENT_BUILDER_DEFAULT_CONSTRUCTOR_BODY, PER_TYPE_QUERY_ELEMENT_GETTER_LIST,

		IMPLEMENTED_HAS_KEY_LIST,

		BUILDER_LINKER_INIT, FOREIGN_KEY_IMPLEMENTATION,

		/**
		 * Class declarations for reference keys
		 */
		REFERENCE_KEY_CLASS_LIST,

		ATTRIBUTE_KEY_TYPE_CANONICAL_NAME, ATTRIBUTE_KEY_TYPE_SIMPLE_NAME,
		/**
		 * 
		 */
		ATTRIBUTE_KEY_MAP_LIST, REFERENCE_KEY_MAP_LIST,

		/*
		 * Implements ... list for table interface.
		 */
		REFERENCE_LIST,

		/*
		 * Implements ... list for table interface.
		 */
		ATTRIBUTE_CONTAINER_INTERFACE_LIST,

		/*
		 * Implements ... list for table interface.
		 */
		ATTRIBUTE_KEY_CONTAINER_INTERFACE_LIST,

		/*
		 * Implements ... list for table interface.
		 */
		ATTRIBUTE_CONTAINER_IMPLEMENTATION,

		/*
		 * Implements ... list for table interface.
		 */
		ATTRIBUTE_KEY_CONTAINER_IMPLEMENTATION,

		ATTRIBUTES_METHOD_STATEMENT_LIST,

		REFERENCE_MAP_LIST,
		/**
		 * Pattern which is replaced with the simple name of the catalog context
		 * class in template files.
		 */
		CATALOG_CONTEXT_CLASS,
		/**
		 * Pattern which is replaced with the simple name of the catalog context
		 * class in template files.
		 */
		CATALOG_CONTEXT_PACKAGE_NAME,
		/**
		 * Pattern which is replaced with the package name of the type being
		 * generated in template files.
		 */
		PACKAGE_NAME, LITERAL_CONTEXT_PACKAGE_NAME,

		/**
		 * Full name of the literal catalog -class.
		 */
		LITERAL_CATALOG_NAME,

		/**
		 * Full name of the literal catalog -class.
		 */
		ENVIRONMENT_EXPRESSION,
		/**
		 * Pattern which is replaced with the package name of the type being
		 * generated in template files.
		 */
		PACKAGE_NAME_LITERAL,

		/**
	     * 
	     */
		CREATE_IDENTITY_MAP_METHOD,
		/**
		 * Pattern which is replaced with the package name of the type being
		 * generated in template files.
		 */
		ROOT_PACKAGE_NAME_LITERAL,

		FACTORY_METHOD_LIST,

		BASE_TABLE_COLUMN_VARIABLE_LIST, POPULATE_COLUMN_MAP_BLOCK, CREATE_GET_BASE_TABLE_BODY, CREATE_PRIMARY_KEY_BODY, CREATE_FOREIGN_KEYS_BODY, CREATE_FOREIGN_KEY_MAP_BODY,

		/**
		 * Generated imports
		 */
		IMPORTS, NEW_ENVIRONMENT_EXPR, SCHEMA_ENUM_LIST, BASE_TABLE_ENUM_LIST, VIEW_ENUM_LIST, COLUMN_ENUM_LIST, SCHEMA_TYPE_NAME, FOREIGN_KEY_ENUM_LIST, PRIMARY_KEY_ENUM_LIST, META_MAP_POPULATION, TABLE_COLUMN_ENUM_LIST,

		/**
		 * Pattern which is replaced package declaration using the package name
		 * of the type being generated in template files.
		 */
		PACKAGE_DECL,

		META_DATA_INITIALIZATION, KEY_ACCESSOR_LIST, COLUMN_KEY_MAP_POPULATION, VALUE_VARIABLE_LIST, VALUE_ACCESSOR_LIST, ADD_ATTRIBUTE_KEY_METHOD, ;

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
	 * TODO: Much of the generate<X> -code should be same for all the parts.
	 * 
	 * Split code generation in to two distinct phases:
	 * 
	 * 1) generate all dynamic content 2) perform replacements for each
	 * generated type.
	 * 
	 * In this way we don't have to care which pattern is used in which
	 * template.
	 */

	private static Logger logger = LoggerFactory.getLogger(SourceGenerator.class);

	private SchemaFilter schemaFilter;
	private File defaultSourceDir;
	private EnumMap<Part, File> sourceDirMap;
	private Map<Class<?>, Class<?>> wrapperMap;
	// private Class<? extends Environment> environmentType;
	private Environment targetEnvironment;

	private boolean delimitAll = false;

	@SuppressWarnings("serial")
	private static class TypeInfo extends EnumMap<Part, JavaType> {

		public TypeInfo() {
			super(Part.class);
		}
	}

	public SourceGenerator(File defaultSourceDir) {
		this(defaultSourceDir, null);
	}

	public SourceGenerator(File defaultSourceDir, SchemaFilter sf) {
		this(defaultSourceDir, sf, false);
	}

	public SourceGenerator(File defaultSourceDir, SchemaFilter sf,
			boolean delimitAll) {
		super();
		this.defaultSourceDir = defaultSourceDir;
		this.schemaFilter = (sf == null) ? SchemaFilter.ALL_SCHEMAS : sf;
		this.delimitAll = delimitAll;
	}

	// private static boolean knownAbbr(String t) {
	// return t.equals("url") || t.equals("http") || t.equals("xml");
	// }
	//
	// private static boolean knownProposition(String t) {
	// return
	// t.equals("of") || t.equals("in") || t.equals("with") ||
	// t.equals("at") || t.equals("for");
	// }

	private static Logger logger() {
		return SourceGenerator.logger;
	}

	public int check(Catalog cat, TypeMapper tm, StringBuilder buf) {

		List<String> errorList = new ArrayList<String>();

		int total = 0;

		for (Schema s : cat.schemas().values()) {
			if (schemaFilter.accept(s)) {
				for (BaseTable table : s.baseTables().values()) {
					ColumnMap cm = table.columnMap();

					for (Column c : cm.values()) {
						errorList.clear();

						int errors = processAttributeInfo(table, c, tm, errorList);

						if (errors > 0) {
							buf.append(formatColumnMappingError(table, c, tm, errorList));
							total += errors;
						}
					}
				}
			}
		}

		if (total > 0) {

		}

		return total;
	}

	/**
	 * @param tm 
	 * @param table
	 * @param c
	 * @param errorList
	 * @return
	 */
	private String formatColumnMappingError(BaseTable table, Column c, TypeMapper tm, 
			List<String> errorList) {

		StringBuilder buf = new StringBuilder();
		SchemaElementName n = table.getName();
		
		Map<String, String> data = new LinkedHashMap<String, String>();

		data.put("type-mapper", tm.getClass().getCanonicalName());
		data.put("schema", n.getQualifier().getSchemaName().getName());
		data.put("table", n.getUnqualifiedName().getName());
		data.put("column", c.getColumnName().getName());

		DataType type = c.getDataType();
		
		data.put("jdbc-data-type", toString(type.getDataType()));
		data.put("jdbc-type-name", AbstractPrimitiveType.getSQLTypeName(type.getDataType()));
		data.put("type-name", type.getTypeName());
		data.put("size", toString(type.getSize()));
		data.put("char-octet-length", toString(type.getCharOctetLength()));
		data.put("decimal-digits", toString(type.getDecimalDigits()));
		data.put("num-prec-radix", toString(type.getNumPrecRadix()));

		for (Map.Entry<String, String> e : data.entrySet()) {
			buf.append(e.getKey());
			buf.append("\t");
			buf.append(e.getValue());
			buf.append("\n");
		}

		buf.append("\n");
		buf.append("error-count\t");
		buf.append(errorList.size());
		buf.append("\n");

		int ordinal = 0;

		for (String e : errorList) {
			buf.append(Integer.toString(++ordinal));
			buf.append("\t");
			buf.append(e);
			buf.append("\n");
		}

		buf.append("\n");

		return buf.toString();
	}

	private String toString(Integer value) {
		return (value == null) ? null : value.toString();
	}

	private int processAttributeInfo(Table table, Column column,
			TypeMapper mapper, List<String> errorList) {

		AttributeInfo info = mapper.getAttributeInfo(column.getDataType());

		if (info == null) {
			errorList.add("no attribute info");
			return 1;
		}

		int count = 0;

		if (info.getAttributeType() == null) {
			errorList.add("no attribute type (value type)");
			count++;
		}

		if (info.getHolderType() == null) {
			errorList.add("no holder type");
			count++;
		}

		if (info.getPrimitiveType() == null) {
			errorList.add("no primitive type");
			count++;
		}

		if (info.getAccessorType() == null) {
			errorList.add("no accessor type");
			count++;
		}

		if (info.getKeyType() == null) {
			errorList.add("no key type");
			count++;
		}

		if (info.getContainerType() == null) {
			errorList.add("no container type");
			count++;
		}

		if (info.getContainerMetaType() == null) {
			errorList.add("no container meta type");
			count++;
		}

		// if (column.isPrimaryKeyColumn() && info.getIdentityMapType() == null)
		// {
		// errorList.add("no identity map type for primary key column");
		// count++;
		// }

		return count;

	}

	/**
	 * Generates the Java sources for the catalog.
	 * 
	 * Source files are placed into
	 * 
	 * Returns a mapping: name of the class of the generated type =&gt;
	 * 
	 * @param cat
	 * @param tm
	 * @param class1
	 * @return
	 * @throws QueryException
	 * @throws IOException
	 */
	public Properties run(Catalog cat, TableMapper tm, TypeMapper typeMapper,
			Environment targetEnvironment) throws QueryException, IOException {
		
		logger().debug("run - enter");
		
		logger().debug("catalog: {}", cat.getName());
		logger().debug("environment: {}", cat.getEnvironment());
		logger().debug("target environment: {}", targetEnvironment);

		Map<File, String> gm = new HashMap<File, String>();
		Properties generated = new Properties();

		this.targetEnvironment = targetEnvironment;

		List<JavaType> ccil = new ArrayList<JavaType>();
		Map<JavaType, CharSequence> fm = new HashMap<JavaType, CharSequence>();

		// SerializableEnvironment env = cat.getEnvironment();

		for (Schema s : cat.schemas().values()) {
			if (schemaFilter.accept(s)) {
				process(cat, s, tm, typeMapper, ccil, fm, generated, gm);
			}
		}

		Set<BaseTable> tts = new HashSet<BaseTable>();

		// for (Schema s : cat.schemas().values()) {
		// for (ForeignKey fk : s.foreignKeys().values()) {
		// tts.add(fk.getReferenced());
		// }
		// }

		for (Schema s : cat.schemas().values()) {
			if (schemaFilter.accept(s)) {
				tts.addAll(s.baseTables().values());
			}
		}

		for (BaseTable t : tts) {
			JavaType hki = tm.entityType(t, Part.HAS_KEY);
			String src = generateHasKeyInterface(hki, t, tm);
			writeIfGenerated(getSourceDir(), hki, src, generated, gm);
		}

		List<String> il = new ArrayList<String>();

		// for (JavaType t : ccil) {
		// if (t != null) {
		// il.add(t.getQualifiedName());
		// }
		// }

		// {
		// JavaType cc = tm.catalogContextType();
		// CharSequence src = generateContext(cc, tm, il, fm);
		// write(getSourceDir(), cc, src, generated, gm);
		// }

		// {
		// JavaType lc = tm.literalContextType();
		// CharSequence src = generateLiteralContext(lc, cat, tm, il, fm);
		// logger().debug("generated lit ctx: src={" + src + "}");
		// writeIfGenerated(getSourceDir(), lc, src, generated, gm);		
		// }
		
		logger().debug("run - exit");

		return generated;
	}

	private String generateHasKeyInterface(JavaType hki, BaseTable t,
			TableMapper tm) throws IOException {
		if (hki == null) {
			return null;
		}

		JavaType intf = tm.entityType(t, Part.INTERFACE);

		if (intf == null) {
			return null;
		}

		JavaType hp = tm.entityType(t, Part.HAS);

		String src = getTemplateForHasKeyInterface();

//		logger().debug("generateHasKeyInterface: src 1=" + src);

		src = replaceAllWithComment(src, Tag.PACKAGE_NAME, hki.getPackageName());
		src = replaceAll(src, Tag.HAS_KEY_INTERFACE, hki.getUnqualifiedName());
		src = replaceAll(src, Tag.HAS_INTERFACE, hp.getUnqualifiedName());
		src = replaceAll(src, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());

//		logger().info("generateHasKeyInterface: src 2=" + src);

		return src;
	}

	// private CharSequence generateLiteralContext(JavaType lc, Catalog cat,
	// TableMapper tm, List<String> il, Map<JavaType, CharSequence> fm) throws
	// IOException {
	//
	// logger().debug("generateLiteralContext - enter");
	// String src = getTemplateForLiteralCatalog();
	//
	// src = replaceAllWithComment(src, Tag.PACKAGE_NAME, lc.getPackageName());
	//
	// Environment env = cat.getEnvironment();
	// String e = env.getClass().getName();
	// src = replaceAllWithComment(src, Tag.NEW_ENVIRONMENT_EXPR, "new " + e +
	// "()");
	//
	// {
	// String list = generateSchemaList(cat);
	// src = replaceAllWithComment(src, Tag.SCHEMA_ENUM_LIST, list);
	// }
	//
	// {
	// String list = generateBaseTableList(cat, tm);
	// src = replaceAllWithComment(src, Tag.BASE_TABLE_ENUM_LIST, list);
	// }
	//
	// {
	// String list = generateViewList(cat, tm);
	// src = replaceAllWithComment(src, Tag.VIEW_ENUM_LIST, list);
	// }
	//
	// {
	// String list = generateColumnList(cat, tm);
	// src = replaceAllWithComment(src, Tag.COLUMN_ENUM_LIST, list);
	// }
	//
	// {
	// String list = generatePrimaryKeyList(cat, tm);
	// src = replaceAllWithComment(src, Tag.PRIMARY_KEY_ENUM_LIST, list);
	// }
	//
	// {
	// String list = generateForeignKeyList(cat, tm);
	// src = replaceAllWithComment(src, Tag.FOREIGN_KEY_ENUM_LIST, list);
	// }
	//
	// {
	// String list = generateMetaMapPopulation(cat, tm);
	// src = replaceAllWithComment(src, Tag.META_MAP_POPULATION, list);
	// }
	//
	// {
	// String list = generateFactoryMethodList(fm);
	// src = replaceAllWithComment(src, Tag.FACTORY_METHOD_LIST, list);
	// }
	//
	// final String tsrc = getTemplateForLiteralInnerTable();
	//
	// StringBuilder buf = new StringBuilder();
	//
	// EnumSet<NameQualification> nq = EnumSet.of(NameQualification.COLUMN);
	// List<String> initList = new ArrayList<String>();
	//
	// for (Schema s : cat.schemas().values()) {
	// if (!schemaFilter.accept(s)) {
	// continue;
	// }
	//
	// String sn = name(s.getUnqualifiedName().getName());
	//
	// for (Table t : s.tables().values()) {
	// String tn = getSimpleName(t);
	//
	// StringBuilder ebuf = new StringBuilder();
	// generateColumnListElements(t, ebuf, nq, tm);
	// String cl = ebuf.toString();
	// String tc = replaceAll(tsrc, Tag.COLUMN_ENUM_LIST, cl);
	// tc = replaceAll(tc, Tag.SCHEMA_TYPE_NAME, sn);
	// tc = replaceAll(tc, Tag.TABLE_INTERFACE, tn);
	//
	// // if inner class:
	// tc = replaceAll(tc, Tag.PACKAGE_DECL, "");
	// tc = replaceAll(tc, Tag.IMPORTS, "");
	//
	// buf.append(tc);
	// buf.append("\n\n");
	//
	// JavaType tet = tm.entityType(t, Part.LITERAL_TABLE_ENUM);
	//
	// logger().info("generateLiteralContext: tet = " + tet + " for " +
	// t.getQualifiedName());
	//
	// if (tet != null) {
	// initList.add(tet.getQualifiedName());
	// }
	// }
	// }
	//
	// final String tin = getTemplateForTableEnumInit();
	// StringBuilder initCode = new StringBuilder();
	//
	// for (String it : initList) {
	// initCode.append(replaceAll(tin, Tag.TABLE_ENUM_INIT_TYPE, it));
	// }
	//
	// src = replaceAll(src, Tag.INIT_COLUMN_ENUM_LIST, initCode.toString());
	//
	// src = replaceAllWithComment(src, Tag.TABLE_COLUMN_ENUM_LIST,
	// buf.toString());
	//
	// logger().debug("generateLiteralContext - exit");
	//
	// return src;
	// }

	private String generateMetaMapPopulation(Catalog cat, TableMapper tm) {
		StringBuilder buf = new StringBuilder();

		for (Schema s : cat.schemas().values()) {
			if (!schemaFilter.accept(s)) {
				continue;
			}

			for (BaseTable t : s.baseTables().values()) {
				String tn = tableEnumeratedName(tm, t);

				JavaType impl = tm.entityType(t, Part.IMPLEMENTATION);
				JavaType intf = tm.entityType(t, Part.INTERFACE);

				// add(mm, LiteralBaseTable.PERSONAL_HOUR_REPORT,
				// com.appspot.relaxe.gen.personal.HourReportImpl.HourReportMetaData.getInstance());

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
		StringBuilder buf = new StringBuilder();

		EnumSet<NameQualification> nq = EnumSet.of(NameQualification.COLUMN);

		for (Schema s : cat.schemas().values()) {
			if (!schemaFilter.accept(s)) {
				continue;
			}

			for (BaseTable t : s.baseTables().values()) {
				SchemaElementMap<ForeignKey> fm = t.foreignKeys();

				for (ForeignKey fk : fm.values()) {
					String n = foreignKeyEnumeratedName(tm, fk);
					String sn = schemaEnumeratedName(s);
					Identifier un = fk.getUnqualifiedName();

					buf.append(n);
					buf.append("(LiteralSchema.");
					buf.append(sn);
					buf.append(", ");
					buf.append(literal(un));

					BaseTable kt = fk.getReferencing();
					JavaType jkt = tm.entityType(kt, Part.LITERAL_TABLE_ENUM);

					BaseTable rt = fk.getReferenced();
					JavaType jrt = tm.entityType(rt, Part.LITERAL_TABLE_ENUM);

					// for (Map.Entry<Column, Column> e :
					// fk.columns().entrySet()) {
					// buf.append(", ");
					// buf.append(jkt.getQualifiedName());
					// buf.append(".");
					// buf.append(columnEnumeratedName(kt, e.getKey(), nq));
					// buf.append(", ");
					// buf.append(jrt.getQualifiedName());
					// buf.append(".");
					// buf.append(columnEnumeratedName(rt, e.getValue(), nq));
					// }

					for (Column a : fk.getColumnMap().values()) {
						Column b = fk.getReferenced(a);
						buf.append(", ");
						buf.append(jkt.getQualifiedName());
						buf.append(".");
						buf.append(columnEnumeratedName(kt, a, nq, tm));
						buf.append(", ");
						buf.append(jrt.getQualifiedName());
						buf.append(".");
						buf.append(columnEnumeratedName(rt, b, nq, tm));
					}

					buf.append("),\n");
				}
			}
		}

		return buf.toString();
	}

	private String generatePrimaryKeyList(Catalog cat, TableMapper tm) {
		StringBuilder buf = new StringBuilder();

		EnumSet<NameQualification> nq = EnumSet.of(NameQualification.COLUMN);

		for (Schema s : cat.schemas().values()) {
			if (!schemaFilter.accept(s)) {
				continue;
			}

			for (BaseTable t : s.baseTables().values()) {
				PrimaryKey pk = t.getPrimaryKey();

				if (pk == null) {
					logger().warn(
							"table without primary key: "
									+ t.getQualifiedName());
					continue;
				}

				JavaType jt = tm.entityType(t, Part.LITERAL_TABLE_ENUM);

				String n = primaryKeyEnumeratedName(tm, pk);
				String tn = tableEnumeratedName(tm, t);
				Identifier un = pk.getUnqualifiedName();

				buf.append(n);
				buf.append("(LiteralBaseTable.");
				buf.append(tn);
				buf.append(", ");
				buf.append(literal(un));
				buf.append("");

				for (Column c : pk.getColumnMap().values()) {
					buf.append(", ");
					// buf.append("LiteralCatalogColumn.");
					buf.append(jt.getQualifiedName());
					buf.append(".");
					buf.append(columnEnumeratedName(t, c, nq, tm));
				}

				buf.append("),\n");
			}
		}

		return buf.toString();
	}

	// private String generateColumnList(Table t) {
	// StringBuilder buf = new StringBuilder();
	//
	// Identifier sn = t.getSchema().getUnqualifiedName();
	// Identifier tn = t.getUnqualifiedName();
	// String ce = name(sn.getName()) + name(tn.getName());
	//
	// buf.append("enum ");
	// buf.append(ce);
	// buf.append(" {");
	// buf.append("\n");
	//
	// EnumSet<NameQualification> fq = EnumSet.allOf(NameQualification.class);
	// generateColumnListElements(t, buf, fq);
	//
	// buf.append("\n");
	// buf.append("}");
	//
	// return buf.toString();
	// }

	enum NameQualification {
		SCHEMA, TABLE, COLUMN
	}

	private String columnConstantName(Column c, TableMapper tm) {
		return columnEnumeratedName(null, c,
				EnumSet.of(NameQualification.COLUMN), tm);
	}

	private void generateColumnListElements(Table t, StringBuilder buf,
			EnumSet<NameQualification> nq, TableMapper tm) {
		boolean b = t.isBaseTable();
		String te = b ? "LiteralBaseTable" : "LiteralView";

		Environment env = t.getEnvironment();

		String et = env.getClass().getName();

		for (Column c : t.columnMap().values()) {
			String cn = columnEnumeratedName(t, c, nq, tm);
			// String ten = tableEnumeratedName(tm, t);
			Identifier un = c.getUnqualifiedName();

			buf.append(cn);
			buf.append("(");
			buf.append(et);
			buf.append(".environment().getIdentifierRules().");

			// delimitAll

			buf.append("toIdentifier(");
			buf.append(literal(un));
			buf.append(")");
			buf.append(", ");
			generateNewDataType(buf, c.getDataType());
			buf.append(", ");
			buf.append(definitelyNotNullableConstant(c));
			buf.append(", ");
			buf.append(autoIncrementBooleanConstant(c));
			buf.append(", ");
			buf.append(columnDefault(c));
			buf.append("),\n");

			// TODO: add 'autoinc' -info etc

			// buf.append(cn);
			// buf.append("(");
			// buf.append("new LiteralCatalog.ColumnInitializer(");
			// buf.append("LiteralCatalog.");
			// buf.append(te);
			// buf.append(".");
			// buf.append(ten);
			// buf.append(", \"");
			// buf.append(un.getName());
			// buf.append("\", ");
			// generateNewDataType(buf, c.getDataType());
			// buf.append(", ");
			// buf.append(autoIncrementConstant(c));
			// buf.append(")),\n");
		}

		buf.append(";\n");
	}

	private String columnDefault(Column c) {
		String def = c.getColumnDefault();
		return (def == null) ? null : literal(def);
	}

	private String autoIncrementBooleanConstant(Column c) {
		Boolean ai = c.isAutoIncrement();

		if (ai == null) {
			return "null";
		}

		StringBuilder buf = new StringBuilder(Boolean.class.getName());
		buf.append('.');
		buf.append(ai.booleanValue() ? "TRUE" : "FALSE");
		return buf.toString();
	}

	private String definitelyNotNullableConstant(Column c) {
		return booleanConstant(c.isDefinitelyNotNullable());
	}

	private String booleanConstant(boolean v) {
		return v ? "true" : "false";
	}

	private String autoIncrementConstant(Column c) {
		Boolean ai = c.isAutoIncrement();
		return (ai == null) ? "null"
				: literal(ai.booleanValue() ? "YES" : "NO");
	}

	private String generateColumnList(Catalog cat, TableMapper tm) {
		StringBuilder buf = new StringBuilder();

		for (Schema s : cat.schemas().values()) {
			if (!schemaFilter.accept(s)) {
				continue;
			}

			for (Table t : s.tables().values()) {
				boolean b = t.isBaseTable();
				String te = b ? "LiteralBaseTable" : "LiteralView";

				for (Column c : t.columnMap().values()) {
					String cn = columnEnumeratedName(t, c, tm);
					String tn = tableEnumeratedName(tm, t);
					Identifier un = c.getUnqualifiedName();

					buf.append(cn);
					buf.append("(");
					buf.append(te);
					buf.append(".");
					buf.append(tn);
					buf.append(", ");
					buf.append(literal(un));
					buf.append(", ");
					generateNewDataType(buf, c.getDataType());
					buf.append(", ");
					buf.append(autoIncrementConstant(c));
					buf.append(")),\n");
				}
			}
		}

		return buf.toString();
	}

	private String generateNewDataType(DataType t) {
		StringBuilder buf = new StringBuilder();
		generateNewDataType(buf, t);
		return buf.toString();

	}

	/**
	 * Formats the expression: new DataTypeImpl(...)
	 * 
	 * @param buf
	 * @param t
	 */

	private void generateNewDataType(StringBuilder buf, DataType t) {
		buf.append("new ");
		buf.append(DataTypeImpl.class.getCanonicalName());
		buf.append("(");

		// call: new DataTypeImpl(int dataType, String typeName, Integer
		// charOctetLength, Integer decimalDigits, Integer numPrecRadix, Integer
		// size)
		buf.append(t.getDataType());
		buf.append(", ");
		literal(buf, t.getTypeName());
		buf.append(", ");
		buf.append(t.getCharOctetLength());
		buf.append(", ");
		buf.append(t.getDecimalDigits());
		buf.append(", ");
		buf.append(t.getNumPrecRadix());
		buf.append(", ");
		buf.append(t.getSize());
		buf.append(")");
	}

	private String generateBaseTableList(Catalog cat, TableMapper tm) {
		StringBuilder buf = new StringBuilder();

		for (Schema s : cat.schemas().values()) {
			if (!schemaFilter.accept(s)) {
				continue;
			}

			for (BaseTable t : s.baseTables().values()) {
				String tn = tableEnumeratedName(tm, t);
				String sn = schemaEnumeratedName(s);
				Identifier un = t.getUnqualifiedName();

				buf.append(tn);
				buf.append("(LiteralSchema.");
				buf.append(sn);
				buf.append(", ");
				buf.append(literal(un));
				buf.append("),\n");
			}
		}

		return buf.toString();
	}

	private String generateViewList(Catalog cat, TableMapper tm) {
		logger().debug("generateViewList - enter");

		StringBuilder buf = new StringBuilder();

		for (Schema s : cat.schemas().values()) {
			if (!schemaFilter.accept(s)) {
				continue;
			}

			for (Table t : s.tables().values()) {
				String tt = t.getTableType();

				logger().debug(
						"generateViewList: tt=" + tt + " in table "
								+ t.getQualifiedName());

				if (tt.equals(Table.VIEW) || tt.equals(Table.SYSTEM_TABLE)) {
					String tn = tableEnumeratedName(tm, t);
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
		}

		logger().debug("generateViewList - exit: " + buf);

		return buf.toString();
	}

	private String generateSchemaList(Catalog cat) {
		StringBuilder buf = new StringBuilder();

		for (Schema s : cat.schemas().values()) {
			if (!schemaFilter.accept(s)) {
				continue;
			}

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

	private String tableEnumeratedName(TableMapper tm, Table t) {
		return enumeratedName(tm, t);
	}

	private String foreignKeyEnumeratedName(TableMapper tm, ForeignKey k) {
		return enumeratedName(tm, k);
	}

	private String primaryKeyEnumeratedName(TableMapper tm, PrimaryKey k) {
		return enumeratedName(tm, k);
	}

	private String enumeratedName(TableMapper tm, SchemaElement e) {
		StringBuilder buf = new StringBuilder();
		// buf.append(e.getSchema().getUnqualifiedName().getName());

		buf.append(e.getName().getQualifier().getSchemaName().getName());
		buf.append("_");
		buf.append(e.getUnqualifiedName().getName());

		String uc = buf.toString().toUpperCase();
		return tm.toJavaIdentifier(uc);
	}

	private String columnEnumeratedName(Table t, Column c, TableMapper tm) {
		return columnEnumeratedName(t, c,
				EnumSet.allOf(NameQualification.class), tm);
	}

	private String columnEnumeratedName(Table t, Column c,
			EnumSet<NameQualification> nq, TableMapper tm) {
		StringBuilder buf = new StringBuilder();

		if (nq.contains(NameQualification.SCHEMA)) {
			// t.getSchema().getUnqualifiedName().getName();
			buf.append(t.getName().getQualifier().getSchemaName().getName());
			buf.append("_");
		}

		if (nq.contains(NameQualification.TABLE)) {
			buf.append(t.getUnqualifiedName().getName());
			buf.append("_");
		}

		if (nq.contains(NameQualification.COLUMN)) {
			buf.append(c.getUnqualifiedName().getName());
		}

		String n = buf.toString().toUpperCase();

		n = normalize(n);
		n = tm.toJavaIdentifier(n);

		return n;
	}

	private String normalize(String n) {
		return n.replace(' ', '_');
	}

	private CharSequence generateContext(JavaType cc, TableMapper tm,
			Collection<String> il, Map<JavaType, CharSequence> fm)
			throws IOException {

		logger().debug("generateContext - enter");
		String src = getTemplateForCatalogContext();

		// src = replacePackageAndImports(src, cc, il);

		src = replacePackageName(src, cc);
		src = replaceAll(src, Tag.PACKAGE_NAME_LITERAL,
				"\"" + cc.getPackageName() + "\"");
		src = replaceImportList(src, il);
		src = replaceAll(src, Tag.ROOT_PACKAGE_NAME_LITERAL,
				"\"" + tm.getRootPackage() + "\"");
		src = replaceAll(src, Tag.CATALOG_CONTEXT_PACKAGE_NAME,
				cc.getPackageName());
		src = replaceAll(src, Tag.CATALOG_CONTEXT_CLASS,
				cc.getUnqualifiedName());

		String list = generateFactoryMethodList(fm);
		src = replaceAll(src, Tag.FACTORY_METHOD_LIST, list);

		logger().debug("generateContext - exit");

		return src;
	}

	private String generateFactoryMethodList(Map<JavaType, CharSequence> fm) {
		StringBuilder buf = new StringBuilder();

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

	// private String getTemplateForLiteralCatalog() throws IOException {
	// return read("LITERAL_CATALOG.in");
	// }

	private String getTemplateForHasKeyInterface() throws IOException {
		return read("HAS_KEY.in");
	}

	private String getTemplateForBuilderLinkerInit() throws IOException {
		return read("BUILDER_LINKER_INIT.in");
	}

	private String getTemplateForKeyRegistration() throws IOException {
		return read("KEY_REGISTRATION.in");
	}

	// private String getTemplateForAttributesMethodStatement() throws
	// IOException {
	// return read("ATTRIBUTES_METHOD_STATEMENT.in");
	// }

	private String read(Tag tag) throws IOException {
		return read(tag.toString() + ".in");
	}

	// private String getTemplateForLiteralInnerTable() throws IOException {
	// return read("LITERAL_INNER_TABLE.in");
	// }
	//
	// private String getTemplateForTableEnumInit() throws IOException {
	// return read("TABLE_ENUM_INIT.in");
	// }

	private void process(Catalog cat, Schema s, final TableMapper tm,
			TypeMapper tym, Collection<JavaType> ccil,
			Map<JavaType, CharSequence> factories, Properties generated,
			Map<File, String> gm) throws IOException {

		List<TypeInfo> types = new ArrayList<TypeInfo>();

		for (BaseTable t : s.baseTables().values()) {
			final JavaType intf = tm.entityType(t, Part.INTERFACE);
			final JavaType at = tm.entityType(t, Part.ABSTRACT);
			final JavaType hp = tm.entityType(t, Part.HOOK);
			final JavaType impl = tm.entityType(t, Part.IMPLEMENTATION);
			final JavaType te = tm.entityType(t, Part.LITERAL_TABLE_ENUM);
			final JavaType ref = tm.entityType(t, Part.HAS);

			if (intf == null || impl == null) {
				continue;
			} else {
				// final Schema schema = t.getSchema();

				{
					CharSequence source = generateInterface(cat, t, intf, tm,
							tym);
					File root = getSourceDir(s, Part.INTERFACE);
					// logger().debug("interface: " + source);
					write(root, intf, source, generated, gm);
				}

				{
					CharSequence source = generateHasRef(t, ref, intf, tm);
					File root = getSourceDir(s, Part.HAS);
					logger().debug("ref: " + source);
					write(root, ref, source, generated, gm);
				}

				if (at != null) {
					CharSequence source = generateAbstract(t, at, tm);
					File root = getSourceDir(s, Part.ABSTRACT);
					write(root, at, source, generated, gm);
				}

				if (te != null) {
					CharSequence source = generateTableEnum(t, te,
							Tag.LITERAL_TABLE_ENUM, tm);
					File root = getSourceDir(s, Part.LITERAL_TABLE_ENUM);
					write(root, te, source, generated, gm);
				}

				if (hp != null) {
					CharSequence source = generateHook(t, hp, tm);

					if (source != null) {
						File root = getSourceDir(s, Part.HOOK);
						File sourceFile = getSourceFile(root, hp);

						if (!sourceFile.exists()) {
							write(root, hp, source, generated, gm);
						}
					}
				}

				{
					CharSequence source = generateImplementation(cat, t, impl,
							tm, tym);
					File root = getSourceDir(s, Part.IMPLEMENTATION);
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

		for (Table t : s.tables().values()) {
			String tt = t.getTableType();
			if (tt.equals(Table.VIEW) || tt.equals(Table.SYSTEM_TABLE)) {
				final JavaType te = tm.entityType(t, Part.LITERAL_TABLE_ENUM);

				if (te != null) {
					CharSequence source = generateViewEnum(t, te, tm);
					File root = getSourceDir(s, Part.LITERAL_TABLE_ENUM);
					write(root, te, source, generated, gm);
				}
			}
		}

		// {
		// final JavaType intf = tm.factoryType(s, Part.INTERFACE);
		// final JavaType fimp = tm.factoryType(s, Part.IMPLEMENTATION);
		// // final JavaType impl = tm.factoryType(s, Part.IMPLEMENTATION);
		//
		// if (intf != null) {
		// if (types.isEmpty()) {
		//
		// }
		// else {
		// File root = getSourceDir(s, Part.INTERFACE);
		// write(root, intf, generateFactoryInterface(s, intf, tm, tym, types),
		// generated, gm);
		//
		// root = getSourceDir(s, Part.IMPLEMENTATION);
		// write(root, fimp, generateFactoryImplementation(s, fimp, tm, types),
		// generated, gm);
		//
		// CharSequence src = generateSchemaFactoryMethodImplementation(s, tm,
		// types);
		// // CharSequence src = generateAnonymousFactoryImplementation(s, tm,
		// types);
		// factories.put(intf, src);
		//
		// ccil.add(intf);
		// // ccil.add(impl);
		//
		// for (TypeInfo info : types) {
		// ccil.add(getFactoryMethodReturnType(info));
		// ccil.add(info.get(Part.IMPLEMENTATION));
		// }
		// }
		// }
		// }
	}

	private CharSequence generateHasRef(BaseTable t, JavaType ref,
			JavaType intf, TableMapper tm) throws IOException {
		String src = getTemplateFor(Part.HAS);
		// src = replaceAll(src, Tag.TABLE_INTERFACE_REF,
		// ref.getUnqualifiedName());
		src = replacePackageAndImports(src, intf);
		src = replaceAll(src, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());
		return src;
	}

	private File getSourceDir(Schema s, Part part) {
		return getSourceDir(part);
	}

	private File getSourceFile(File root, JavaType type) throws IOException {
		File pd = packageDir(type.getPackageName());
		pd = (pd == null) ? root : new File(root, pd.getPath());
		return getSourceFile(pd, type.getUnqualifiedName());
	}

	private void writeIfGenerated(File root, JavaType type,
			CharSequence source, Properties dest, Map<File, String> files)
			throws IOException {

		if (source == null) {
			return;
		}

		write(root, type, source, dest, files);
	}

	private void write(File root, JavaType type, CharSequence source,
			Properties dest, Map<File, String> files) throws IOException {

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
				throw new IOException("unable to create directory: "
						+ pd.getPath());
			}

			dest.put(pkg, pd.getPath());
		}
	}

	public CharSequence generateInterface(Catalog cat, BaseTable t,
			JavaType mt, TableMapper tm, TypeMapper tym) throws IOException {

		String src = getTemplateFor(Part.INTERFACE);

		src = replacePackageAndImports(src, mt);

		src = replaceAll(src, Tag.TABLE_INTERFACE, mt.getUnqualifiedName());

		{
			logger().debug(
					"generateInterface: attributeKeyList: "
							+ t.getQualifiedName());
			String code = attributeKeyList(cat, t, tm, tym);
			logger().debug("generateInterface: attributeKeyList=" + code);
			src = replaceAll(src, Tag.ATTRIBUTE_KEY_LIST, code);
		}

		{
			String type = createAttributeType(getAttributeTemplate(),
					getAttributeType(), attrs(cat, t, tm, tym));
			src = replaceAll(src, "{{attribute-name-type}}", type);
		}

		{
			String type = createReferenceType(getReferenceTemplate(),
					getReferenceType(), refs(t, tm));
			src = replaceAll(src, "{{reference-name-type}}", type);
			src = replaceAll(src, Tag.TABLE_INTERFACE, mt.getUnqualifiedName());
		}

		// {
		// String type = createEnumType(getEnumTemplate(), getReferenceType(),
		// refs(t));
		// src = replaceAll(src, "{{reference-name-type}}", type);
		// }

		{
			String type = referenceKeyList(t, tm);
			src = replaceAll(src, Tag.REFERENCE_KEY_LIST, type);
		}

		{
			String hkl = implementedHasKeyList(t, tm);
			src = replaceAll(src, Tag.IMPLEMENTED_HAS_KEY_LIST, hkl);
		}

		// {
		// String type = createEnumType(getEnumTemplate(), "Query", queries(t));
		// src = replaceAll(src, "{{query-name-type}}", type);
		// }
		
		
		{
			String code = entityAttributeAccessorList(cat, t, tm, tym, false);
			src = replaceAllWithComment(src, Tag.ENTITY_ATTRIBUTE_ACCESSOR_SIGNATURE_LIST, code);
		}		
		
		

		{
			String code = contentAccessors(cat, t, tm, tym, false);
			src = replaceAll(src, "{{abstract-accessor-list}}", code);
		}

		{
			String code = queryElementVariableList(cat, t, tm, tym, false);
			src = replaceAllWithComment(src, Tag.QUERY_ELEMENT_VARIABLE_LIST, code);
		}

		{
			String code = queryElementGetterBody(cat, t, tm, tym, false);
			src = replaceAllWithComment(src, Tag.QUERY_ELEMENT_GETTER_BODY,
					code);
		}

		{
			String code = queryElementSetterBody(cat, t, tm, tym, false);
			src = replaceAllWithComment(src, Tag.QUERY_ELEMENT_SETTER_BODY,
					code);
		}

		{
			String code = queryElementAssignmentList(cat, t, tm, tym, false);
			src = replaceAllWithComment(src, Tag.QUERY_ELEMENT_ASSIGNMENT_LIST,
					code);
		}

		{
			String code = queryElementNewBuilderBody(cat, t, tm, tym, false);
			src = replaceAllWithComment(src,
					Tag.QUERY_ELEMENT_NEW_BUILDER_BODY, code);
		}

		{
			String code = queryElementDefaultConstructorBody(cat, t, tm, tym,
					false);
			src = replaceAllWithComment(src,
					Tag.QUERY_ELEMENT_DEFAULT_CONSTRUCTOR_BODY, code);
			// same content can applied for Builder, too
			src = replaceAllWithComment(src,
					Tag.QUERY_ELEMENT_BUILDER_DEFAULT_CONSTRUCTOR_BODY, code);
		}

		{
			String code = perTypeQueryElementGetterList(cat, t, tm, tym, false);
			src = replaceAllWithComment(src,
					Tag.PER_TYPE_QUERY_ELEMENT_GETTER_LIST, code);
		}

		{
			String code = valueAccessorList(cat, t, tm, tym, false);
			src = replaceAll(src, Tag.VALUE_ACCESSOR_LIST, code);
		}

		{
			String code = referenceList(t, tm);
			src = replaceAll(src, Tag.REFERENCE_LIST, code);
		}

		{
			String code = attributeContainerInterfaceList(cat, t, tm, tym);
			logger().debug(
					"generateInterface: attributeContainerInterfaceList: "
							+ code);
			logger().debug(
					"generateInterface: tag: "
							+ Tag.ATTRIBUTE_CONTAINER_INTERFACE_LIST.getTag());

			src = replaceAll(src, Tag.ATTRIBUTE_CONTAINER_INTERFACE_LIST, code);
		}

		{
			String code = attributeKeyContainerInterfaceList(cat, t, tm, tym);
			src = replaceAll(src, Tag.ATTRIBUTE_KEY_CONTAINER_INTERFACE_LIST,
					code);
		}

		return src;
	}

	private String queryElementVariableList(Catalog cat, BaseTable t,
			TableMapper tm, TypeMapper tym, boolean b) {
		Collection<ForeignKey> fks = t.foreignKeys().values();

		if (fks.isEmpty()) {
			return "";
		}

		StringBuilder buf = new StringBuilder();

		for (ForeignKey fk : fks) {
			// SAMPLE: private EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?>
			// languageQueryElement;

			line(buf, "private ", EntityQueryElement.class.getCanonicalName(),
					"<?, ?, ?, ?, ?, ?, ?, ?, ?> ",
					queryElementVariableName(fk), ";");
		}

		return buf.toString();
	}

	protected String queryElementVariableName(ForeignKey fk) {
		return variableName(referenceName(fk)) + "QueryElement";
	}

	private String queryElementGetterBody(Catalog cat, BaseTable t,
			TableMapper tm, TypeMapper tym, boolean b) {
		Collection<ForeignKey> fks = t.foreignKeys().values();

		StringBuilder buf = new StringBuilder();

		line(buf, "if (key == null) {");
		line(buf, "  throw new NullPointerException(\"key\");");
		line(buf, "}", 2);

		if (fks.isEmpty()) {
			line(buf, "return null;");
		} else {
			line(buf, "switch(key.name()) {");

			for (ForeignKey fk : fks) {
				// SAMPLE: private EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?>
				// languageQueryElement;

				line(buf, "case ", referenceName(fk), ":");
				line(buf, "return this.", queryElementVariableName(fk), ";");
			}

			line(buf, "default: ");
			line(buf, "  break;");

			line(buf, "}", 2);

			line(buf, "return null; ");
		}

		return buf.toString();
	}

	private String queryElementSetterBody(Catalog cat, BaseTable t,
			TableMapper tm, TypeMapper tym, boolean b) {
		Collection<ForeignKey> fks = t.foreignKeys().values();

		StringBuilder buf = new StringBuilder();

		line(buf, "if (key == null) {");
		line(buf, "  throw new NullPointerException(\"key\");");
		line(buf, "}", 2);

		if (!fks.isEmpty()) {
			line(buf, "switch(key.name()) {");

			for (ForeignKey fk : fks) {
				// SAMPLE: private EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?>
				// languageQueryElement;

				line(buf, "case ", referenceName(fk), ":");
				line(buf, "this.", queryElementVariableName(fk), " = qe;");
				line(buf, "break;");
			}

			line(buf, "default: ");
			line(buf, "  break;");

			line(buf, "}", 2);
		}

		return buf.toString();
	}

	private String queryElementAssignmentList(Catalog cat, BaseTable t,
			TableMapper tm, TypeMapper tym, boolean b) {
		Collection<ForeignKey> fks = t.foreignKeys().values();

		if (fks.isEmpty()) {
			return "";
		}

		StringBuilder buf = new StringBuilder();

		for (ForeignKey fk : fks) {
			String var = queryElementVariableName(fk);
			line(buf, "qe.", var, " = this.", var, ";");
		}

		return buf.toString();
	}

	private String queryElementNewBuilderBody(Catalog cat, BaseTable t,
			TableMapper tm, TypeMapper tym, boolean b) {
		Collection<ForeignKey> fks = t.foreignKeys().values();

		StringBuilder buf = new StringBuilder();

		line(buf, "QueryElement.Builder qeb = new QueryElement.Builder();");
		line(buf,
				"qeb.attributes = (this.attributes == null) ? null : new java.util.TreeSet<Attribute>(this.attributes);");

		for (ForeignKey fk : fks) {
			String var = queryElementVariableName(fk);
			line(buf, "qeb.", var, " = this.", var, ";");
		}

		line(buf, "return qeb;");

		return buf.toString();
	}

	private String queryElementDefaultConstructorBody(Catalog cat, BaseTable t,
			TableMapper tm, TypeMapper tym, boolean b) {

		PrimaryKey pk = t.getPrimaryKey();

		if (pk == null) {
			return "";
		}

		StringBuilder buf = new StringBuilder();

		ColumnMap pkcm = pk.getColumnMap();

		int pkcols = pkcm.size();

		List<String> pkattrs = new ArrayList<String>(pkcols);
		Map<String, ForeignKey> pkkeys = null;

		List<Column> acols = getAttributeColumnList(cat, t, tym);

		for (Column ac : acols) {
			if (pkcm.contains(ac.getUnqualifiedName())) {								
				AttributeInfo ai = tym.getAttributeInfo(ac.getDataType());

				if (ai == null) {
					throw new NullPointerException(
							"attribute info for primary key column: "
									+ ac.getUnqualifiedName());
				}

				pkattrs.add(attr(ac));
			}
		}

		if (pkattrs.size() < pkcols) {
			pkkeys = new TreeMap<String, ForeignKey>();

			// Map<Identifier, ForeignKey> fkmap = foreignKeyColumnMap(cat, t);

			for (ForeignKey fk : t.foreignKeys().values()) {
				for (Column fkcol : fk.getColumnMap().values()) {
					if (pkcm.contains(fkcol.getUnqualifiedName())) {
						pkkeys.put(fk.getQualifiedName(), fk);
						continue;
					}
				}
			}
		}

		if (!pkattrs.isEmpty()) {
			line(buf, "this.attributes = new java.util.TreeSet<Attribute>();");

			for (String attr : pkattrs) {
				line(buf, "this.attributes.add(Attribute.", attr, ");");
			}
		}

		if (pkkeys != null) {
			for (ForeignKey fk : pkkeys.values()) {
				String var = queryElementVariableName(fk);
				JavaType te = tm.entityType(fk.getReferenced(), Part.INTERFACE);
				line(buf, "this.", var, " = new ", te.getQualifiedName(),
						".QueryElement();");
			}
		}

		return buf.toString();
	}

	private String perTypeQueryElementGetterList(Catalog cat, BaseTable t,
			TableMapper tm, TypeMapper tym, boolean b) {

		Collection<ForeignKey> fks = t.foreignKeys().values();

		if (fks.isEmpty()) {
			return "";
		}

		StringBuilder buf = new StringBuilder();

		Map<BaseTable, List<ForeignKey>> groups = group(fks);

		for (Map.Entry<BaseTable, List<ForeignKey>> e : groups.entrySet()) {
			BaseTable ref = e.getKey();
			List<ForeignKey> keys = e.getValue();
			String src = perTypeQueryElementGetter(t, ref, keys, tm);
			line(buf, src);
		}

		return buf.toString();
	}

	private String perTypeQueryElementGetter(BaseTable t, BaseTable ref,
			List<ForeignKey> keys, TableMapper tm) {

		StringBuilder buf = new StringBuilder();

		JavaType itf = tm.entityType(t, Part.INTERFACE);

		JavaType type = tm.entityType(ref, Part.INTERFACE);

		String qn = type.getQualifiedName();

		line(buf, "public ", qn, ".QueryElement getQueryElement(", qn,
				".Key<Attribute, Reference, Type, ", itf.getUnqualifiedName(),
				", Holder, Factory, MetaData, Content> key) {");

		line(buf, "if (key == null) {");
		line(buf, "throw new java.lang.NullPointerException(\"key\");");
		line(buf, "}");

		line(buf, "switch (key.name()) {");

		for (ForeignKey fk : keys) {
			line(buf, "case ", referenceName(fk), ":");
			line(buf, "return (", qn, ".QueryElement) this.",
					queryElementVariableName(fk), ";");
		}

		line(buf, "default: ");
		line(buf, "break;");
		line(buf, "}");

		line(buf, "return null;");

		line(buf, "}", 2);

		return buf.toString();
	}

	protected Map<BaseTable, List<ForeignKey>> group(Collection<ForeignKey> fks) {
		Map<BaseTable, List<ForeignKey>> grouped = new HashMap<BaseTable, List<ForeignKey>>();

		for (ForeignKey fk : fks) {
			BaseTable ref = fk.getReferenced();
			List<ForeignKey> elems = grouped.get(ref);

			if (elems == null) {
				elems = new LinkedList<ForeignKey>();
				grouped.put(ref, elems);
			}

			elems.add(fk);
		}

		return grouped;
	}

	private String implementedHasKeyList(BaseTable t, TableMapper tm) {
		SchemaElementMap<ForeignKey> fkm = t.foreignKeys();
		Set<BaseTable> ts = new HashSet<BaseTable>();

		for (ForeignKey k : fkm.values()) {
			ts.add(k.getReferenced());
		}

		StringBuilder buf = new StringBuilder();

		// JavaType intf = tm.entityType(t, Part.INTERFACE);

		// HasProjectKey<Reference, Type, HourReport, MetaData>,
		// HasOrganizationKey<Reference, Type, HourReport, MetaData>

		String args = referenceKeyTypeArgs(tm, t);

		for (BaseTable r : ts) {
			buf.append(", ");
			JavaType kt = tm.entityType(r, Part.HAS_KEY);
			buf.append(kt.getQualifiedName());
			buf.append("<");
			buf.append(args);
			buf.append("> ");
		}

		return buf.toString();
	}

	/**
	 * Returns a comma separated list of the Ref -interfaces which the table
	 * interface for <code>t</code> implements.
	 * 
	 * @param t
	 * @param tm
	 * @return
	 */

	private String referenceList(BaseTable t, TableMapper tm) {
		StringBuilder buf = new StringBuilder();

		Set<BaseTable> rs = referencedTables(t);

		String args = referenceKeyTypeArgs(tm, t);

		for (BaseTable rt : rs) {
			buf.append(", ");
			JavaType e = tm.entityType(rt, Part.HAS);
			buf.append(e.getQualifiedName());
			buf.append("<");
			buf.append(args);
			buf.append(">");
		}

		String code = buf.toString();
		return code;
	}

	/**
	 * Returns a comma separated list of the <code>Has&lt;Type&gt;</code>
	 * -interfaces which the table interface for <code>t</code> implements.
	 * 
	 * @param t
	 * @param tm
	 * @return
	 */

	private String attributeContainerInterfaceList(Catalog cat, BaseTable t,
			TableMapper tm, TypeMapper tym) {
		StringBuilder buf = new StringBuilder();

		Set<Class<?>> implemented = new LinkedHashSet<Class<?>>();

		List<Column> cl = getAttributeColumnList(cat, t, tym);

		for (Column c : cl) {
			AttributeInfo ai = tym.getAttributeInfo(c.getDataType());

			if (ai != null) {
				Class<?> ct = ai.getContainerType();

				if (ct != null) {
					implemented.add(ct);
				} else {
					columnName(t, c);
					logger().error(
							columnName(
									new StringBuilder(
											"no container type for attribute for column: "),
									t, c));
				}
			}
		}

		JavaType et = tm.entityType(t, Part.INTERFACE);

		for (Class<?> i : implemented) {
			buf.append(", ");
			buf.append(i.getCanonicalName());
			buf.append("<");
			buf.append(et.getUnqualifiedName());
			buf.append(".");
			buf.append(getAttributeType());
			buf.append(",");
			buf.append(et.getUnqualifiedName());
			buf.append(">\n");
		}

		String code = buf.toString();
		return code;
	}

	/**
	 * Returns a comma separated list of the <code>Has&lt;Type&gt;Key</code>
	 * -interfaces which the table meta interface for <code>t</code> implements.
	 * 
	 * @param t
	 * @param tm
	 * @return
	 */

	private String attributeKeyContainerInterfaceList(Catalog cat, BaseTable t,
			TableMapper tm, TypeMapper tym) {
		StringBuilder buf = new StringBuilder();

		Set<Class<?>> implemented = new LinkedHashSet<Class<?>>();

		List<Column> cl = getAttributeColumnList(cat, t, tym);

		for (Column c : cl) {
			AttributeInfo ai = tym.getAttributeInfo(c.getDataType());

			if (ai != null) {
				Class<?> ct = ai.getContainerMetaType();

				if (ct != null) {
					implemented.add(ct);
				} else {
					columnName(t, c);
					logger().error(
							columnName(
									new StringBuilder(
											"no container type for attribute for column: "),
									t, c));
				}
			}
		}

		JavaType et = tm.entityType(t, Part.INTERFACE);

		for (Class<?> i : implemented) {
			buf.append(", ");
			buf.append(i.getCanonicalName());
			buf.append("<");
			buf.append(et.getUnqualifiedName());
			buf.append(".");
			buf.append(getAttributeType());
			buf.append(",");
			buf.append(et.getUnqualifiedName());
			buf.append(">\n");
		}

		String code = buf.toString();
		return code;
	}

	/**
	 * Returns a comma separated list of the <code>Has&lt;Type&gt;</code>
	 * -interfaces which the table interface for <code>t</code> implements.
	 * 
	 * @param t
	 * @param tm
	 * @return
	 */

	private String attributeContainerImplementation(Catalog cat, BaseTable t,
			TableMapper tam, TypeMapper tym) {
		StringBuilder buf = new StringBuilder();

		Map<Class<?>, List<Column>> attributeTypeMap = new HashMap<Class<?>, List<Column>>();

		List<Column> cl = getAttributeColumnList(cat, t, tym);

		for (Column c : cl) {
			AttributeInfo ai = tym.getAttributeInfo(c.getDataType());

			if (ai != null) {
				Class<?> kt = ai.getKeyType();

				if (kt == null) {
					continue;
				}

				List<Column> list = attributeTypeMap.get(kt);

				if (list == null) {
					attributeTypeMap.put(kt, list = new ArrayList<Column>());
				}

				list.add(c);
			}
		}

		for (Class<?> kt : attributeTypeMap.keySet()) {
			List<Column> attributes = attributeTypeMap.get(kt);
			formatAttributeHolderAccessors(cat, kt, t, attributes, tam, tym,
					buf);
		}

		String code = buf.toString();
		return code;
	}

	private void formatAttributeHolderAccessors(Catalog cat, Class<?> kt,
			BaseTable table, List<Column> columns, TableMapper tam,
			TypeMapper tym, StringBuilder buf) {

		// Sample output: "private IntegerHolder a = null;"
		// Sample output: "private IntegerHolder b = null;, etc..."

		buf.append("\n// formatAttributeHolderAccessors - enter\n");

		List<Column> cols = columns;

		for (Column ac : cols) {

			AttributeInfo info = tym.getAttributeInfo(ac.getDataType());

			if (info == null) {
				continue;
			}

			Class<?> ht = info.getHolderType();

			if (ht == null) {
				logger().error("no holder type: " + columnName(table, ac));
				continue;
			}

			String n = valueVariableName(table, ac);

			buf.append("private ");
			buf.append(ht.getCanonicalName());
			buf.append(" ");
			buf.append(n);
			buf.append(" = null;\n");
			buf.append("\n");
		}

		// tym.getAttributeInfo(table, attributes.get(index));
		JavaType itf = tam.entityType(table, Part.INTERFACE);

		// all the attributes are expected to have same type, infer type by the
		// first:
		Column column = columns.get(0);

		AttributeInfo ai = tym.getAttributeInfo(column.getDataType());
		String n = getKeyName(ai.getKeyType());
		n = removeSuffix(n, "Key");

		Class<?> ht = ai.getHolderType();

		{
			final String methodName = "set" + n;
			final String newValueVariable = "newValue";

			buf.append("public void ");
			buf.append(methodName);
			buf.append("(");
			buf.append(kt.getCanonicalName());
			buf.append("<");
			buf.append(getAttributeType());
			buf.append(", ");
			buf.append(itf.getQualifiedName());
			buf.append(">");
			buf.append(" key, ");
			buf.append(ht.getCanonicalName());
			buf.append(" ");
			buf.append(newValueVariable);
			buf.append(") {\n\n");

			buf.append("\n");
			appendThrowNpe(buf, itf.getQualifiedName(), methodName, "key");
			buf.append("\n\n");

			buf.append("switch (key.name()) {\n");

			for (Column c : columns) {
				buf.append("case ");
				buf.append(attr(c));
				buf.append(": { this.");
				buf.append(valueVariableName(table, c));
				buf.append(" = ");
				buf.append(newValueVariable);
				buf.append(";\n");
				buf.append("\nbreak;\n}");
				buf.append("\n");
			}

			buf.append("default:\n");
			buf.append("\n}\n");

			// end of method:
			buf.append("\n}\n");
		}

		{
			final String methodName = "get" + n;

			buf.append("public ");
			buf.append(ht.getCanonicalName());
			buf.append(" ");
			buf.append(methodName);
			buf.append("(");
			buf.append(kt.getCanonicalName());
			buf.append("<");
			buf.append(getAttributeType());
			buf.append(", ");
			buf.append(itf.getQualifiedName());
			buf.append(">");
			buf.append(" key");
			buf.append(") {\n\n");
			appendThrowNpe(buf, itf.getQualifiedName(), methodName, "key");
			buf.append("\n\n");

			buf.append("switch (key.name()) {\n");

			for (Column c : columns) {
				buf.append("case ");
				buf.append(attr(c));
				buf.append(": return this.");
				buf.append(valueVariableName(table, c));
				buf.append(";");
				buf.append("\n");
			}

			buf.append("default:\n");

			// end-switch
			buf.append("\n}\n");

			buf.append("\nreturn null;\n");

			// end-of-method
			buf.append("\n}\n");
		}

		buf.append("\n// formatAttributeHolderAccessors - exit\n");
	}

	private void appendThrowNpe(StringBuilder buf, String className,
			final String methodName, String argument) {
		if (argument == null) {
			throw new NullPointerException("argument");
		}

		line(buf, "if (", argument, " == null) {");
		buf.append("throw new java.lang.NullPointerException(\"");

		if (className != null) {
			buf.append(className);
			buf.append(".");
		}

		if (methodName != null) {
			buf.append(methodName);
			buf.append(": ");
		}

		buf.append(argument);
		buf.append("\");\n");
		line(buf, "}");
	}

	private String columnName(BaseTable t, Column c) {
		return columnName(new StringBuilder(), t, c).toString();
	}

	/**
	 * Returns a comma separated list of the <code>Has&lt;Type&gt;</code>
	 * -interfaces which the table interface for <code>t</code> implements.
	 * 
	 * @param t
	 * @param tm
	 * @return
	 * @throws IOException
	 */

	private String attributeKeyContainerImplementation(Catalog cat,
			BaseTable t, TableMapper tam, TypeMapper tym) throws IOException {
		StringBuilder buf = new StringBuilder();

		buf.append("\n// attributeKeyContainerImplementation() - enter\n");

		Set<Class<?>> attributeKeyTypes = new HashSet<Class<?>>();

		List<Column> cl = getAttributeColumnList(cat, t, tym);

		for (Column c : cl) {
			AttributeInfo ai = tym.getAttributeInfo(c.getDataType());

			if (ai != null) {
				Class<?> kt = ai.getKeyType();

				if (kt == null) {
					continue;
				}

				attributeKeyTypes.add(kt);

				// List<Column> list = attributeTypeMap.get(kt);
				//
				// if (list == null) {
				// attributeTypeMap.put(kt, list = new ArrayList<Column>());
				// }
				//
				// list.add(c);
			}
		}

		String template = getTemplateForKeyRegistration();

		JavaType intf = tam.entityType(t, Part.INTERFACE);

		for (Class<?> kt : attributeKeyTypes) {
			String kn = getKeyName(kt);
			String s = template;
			s = replaceAll(s, Tag.ATTRIBUTE_KEY_TYPE_CANONICAL_NAME,
					kt.getCanonicalName());
			s = replaceAll(s, Tag.ATTRIBUTE_KEY_TYPE_SIMPLE_NAME, kn);
			s = replaceAll(s, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());
			buf.append(s);
		}

		buf.append("\n// attributeKeyContainerImplementation() - exit\n");

		String code = buf.toString();
		return code;
	}

	private String removeSuffix(String n, String suffix) {
		if (n.endsWith(suffix)) {
			n = n.substring(0, n.length() - suffix.length());
		}

		return n;
	}
	
	private String columnName(StringBuilder buf, BaseTable t, Column c) {
		SchemaElementName n = t.getName();

		buf.append(n.getQualifier().getSchemaName().getName());
		buf.append(".");
		buf.append(n.getUnqualifiedName().getName());
		buf.append(".");
		buf.append(c.getColumnName().getName());

		return buf.toString();
	}

	public CharSequence generateTableEnum(Table t, JavaType mt, Tag tableEnum,
			TableMapper tm) throws IOException {

		String src = getTemplateFor(Part.LITERAL_TABLE_ENUM);

		src = replacePackageName(src, mt);

		ArrayList<String> il = new ArrayList<String>();
		addImport(mt, tm.literalContextType(), il);
		src = replaceImportList(src, il);

		src = replaceAll(src, tableEnum, mt.getUnqualifiedName());

		EnumSet<NameQualification> nq = EnumSet.of(NameQualification.COLUMN);

		StringBuilder ebuf = new StringBuilder();
		generateColumnListElements(t, ebuf, nq, tm);
		String cl = ebuf.toString();
		src = replaceAll(src, Tag.COLUMN_ENUM_LIST, cl);

		return src;
	}

	public CharSequence generateViewEnum(Table t, JavaType mt, TableMapper tm)
			throws IOException {

		return generateTableEnum(t, mt, Tag.LITERAL_TABLE_ENUM, tm);
	}

	public CharSequence generateFactoryInterface(Schema s,
			JavaType factoryType, TableMapper tm, TypeMapper typeMapper,
			Collection<TypeInfo> types) throws IOException {

		String src = getFactoryTemplateFor(Part.INTERFACE);

		src = replacePackageAndImports(src, factoryType);

		src = replaceAll(src, "{{schema-factory}}",
				factoryType.getUnqualifiedName());

		StringBuilder code = new StringBuilder();

		for (TypeInfo info : types) {
			String m = generateFactoryMethod(info, false);
			line(code, m, 1);
		}

		src = replaceAll(src, "{{factory-method-list}}", code.toString());

		logger().debug("factory intf: " + src);

		return src;
	}

	private CharSequence generateSchemaFactoryMethodImplementation(Schema s,
			TableMapper tm, Collection<TypeInfo> types) throws IOException {

		// JavaType intf = tm.factoryType(s, Part.INTERFACE);
		// String src = getFactoryTemplateFor(Part.INTERFACE);

		JavaType impl = tm.factoryType(s, Part.IMPLEMENTATION);

		String src = " new " + impl.getQualifiedName() + "(); ";

		// src = replaceAll(src, "{{schema-factory}}",
		// intf.getUnqualifiedName());
		//
		// StringBuilder code = new StringBuilder();
		//
		// for (TypeInfo t : types) {
		// String m = formatFactoryMethod(t, true);
		// a(code, m, 1);
		// }
		//
		// src = replaceAll(src, FACTORY_METHOD_LIST, code.toString());
		//
		// logger().debug("factory impl: " + src);

		return src;
	}

	private JavaType getFactoryMethodReturnType(TypeInfo info) {
		// JavaType hp = info.get(Part.HOOK);
		// JavaType itfp = info.get(Part.INTERFACE);
		// JavaType at = info.get(Part.ABSTRACT);
		//
		// return (hp != null) ? hp : (at != null) ? at : itfp;

		JavaType itfp = info.get(Part.INTERFACE);
		return itfp;
	}

	/**
	 * Formats a factory method for JavaType.
	 * 
	 * @param info
	 * @param impl
	 * @return
	 */

	private String generateFactoryMethod(TypeInfo info, boolean impl) {
		JavaType itf = info.get(Part.INTERFACE);

		JavaType returnType = getFactoryMethodReturnType(info);

		String signature = returnType.getQualifiedName() + " new"
				+ itf.getUnqualifiedName() + "()";
		String src = null;

		if (!impl) {
			src = signature + ";";
		} else {
			src = "public " + signature + " { " + "return "
					+ itf.getQualifiedName()
					+ ".Type.TYPE.getMetaData().getFactory().newInstance(); "
					+ "} ";
		}

		return src;
	}

	private String getFactoryTemplateFor(Part p) throws IOException {
		return read("FACTORY_" + p.toString() + ".in");
	}

	private String read(String resource) throws IOException {

		logger.debug("reading toString(): " + resource);
		InputStream template = getClass().getResourceAsStream(resource);
		String src = new IOHelper().read(template, "UTF-8", 1024);
		return src;
	}

	private String createAttributeType(String template, String name,
			String constants) {
		String src = replaceAll(template, "{{attribute-type}}", name);
		src = replaceAll(src, "{{attribute-constants}}", constants);
		return src;
	}

	private String createReferenceType(String template, String name,
			String constants) {
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

	private String getTemplateFor(Part p) throws IOException {
		return read(p.toString() + ".in");
	}

	private String imports(Collection<String> imports) {
		StringBuilder src = new StringBuilder();

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

	private String replaceAllWithComment(String text, Tag pattern,
			String replacement) {
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

	public CharSequence generateAbstract(BaseTable t, JavaType mt,
			TableMapper tm) throws IOException {

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
		src = replaceAll(src, Tag.TABLE_HOOK_BASE_CLASS,
				base.getUnqualifiedName());

		return src;
	}

	private void addImport(JavaType importingType, JavaType imported,
			List<String> importList) {
		if (imported == null
				|| imported.getPackageName().equals(
						importingType.getPackageName())) {
			return;
		}
		importList.add(imported.getQualifiedName());
	}

	private CharSequence generateImplementation(Catalog cat, BaseTable t,
			JavaType impl, TableMapper tam, TypeMapper tym) throws IOException {

		String src = getTemplateFor(Part.IMPLEMENTATION);

		JavaType intf = tam.entityType(t, Part.INTERFACE);
		JavaType base = tam.entityType(t, Part.HOOK);

		if (base == null) {
			base = tam.entityType(t, Part.ABSTRACT);
		}

		String lt = tableEnumeratedName(tam, t);

		List<String> il = new ArrayList<String>();
		addImport(impl, intf, il);
		addImport(impl, base, il);

		Tag tag = null;

		src = replacePackageAndImports(src, impl, il);

		tag = Tag.BASE_TABLE_COLUMN_VARIABLE_LIST;
		src = replaceAll(src, tag,
				generateBaseTableColumnVariableList(t, tag, tam));

		src = replaceAll(src, Tag.POPULATE_COLUMN_MAP_BLOCK,
				generatePopulateColumnMapBlock(t, tam));
		src = replaceAll(src, Tag.CREATE_GET_BASE_TABLE_BODY,
				generateGetBaseTableBody(t, tam));
		src = replaceAll(src, Tag.CREATE_PRIMARY_KEY_BODY,
				generateCreatePrimaryKeyBody(t, tam));
		src = replaceAll(src, Tag.CREATE_FOREIGN_KEY_MAP_BODY,
				generateCreateForeignKeyMapBody(t, tam));

		boolean qualify = hasAmbiguousSimpleNamesForReferenceKeys(t, tam);

		{
			String code = contentAccessors(cat, t, tam, tym, true);
			src = replaceAll(src, Tag.ACCESSOR_LIST, code);
		}

		{
			String code = referenceKeyClassList(t, tam, qualify);
			logger().debug(
					"generateImplementation: referenceKeyClassList=" + code);
			src = replaceAll(src, Tag.REFERENCE_KEY_CLASS_LIST, code);
		}

		{
			String code = attributeKeyMapList(cat, t, tam, tym);
			logger().debug("generateImplementation: code=" + code);
			src = replaceAll(src, Tag.ATTRIBUTE_KEY_MAP_LIST, code);
		}
		
		{
			String code = entityAttributeAccessorList(cat, t, tam, tym, true);
			src = replaceAllWithComment(src, Tag.ENTITY_ATTRIBUTE_ACCESSOR_LIST, code);
		}				

		{
			String code = referenceKeyMapList(t, tam, qualify);
			logger().debug("generateImplementation: code=" + code);
			src = replaceAll(src, Tag.REFERENCE_KEY_MAP_LIST, code);
		}

		{
			src = t.foreignKeys().isEmpty() ? replaceAllWithComment(src,
					Tag.FOREIGN_KEY_IMPLEMENTATION, "") : readAndReplace(src,
					Tag.FOREIGN_KEY_IMPLEMENTATION, false);
		}

		{
			String code = builderLinkerInit(t, tam, qualify);
			logger().debug("builderLinkerInit: code=" + code);
			src = replaceAllWithComment(src, Tag.BUILDER_LINKER_INIT, code);
		}

		{
			String code = generateCreateIdentityMapMethod(t, tam, tym);
			logger().debug("generateCreateIdentityMapMethod: code=" + code);
			src = replaceAll(src, Tag.CREATE_IDENTITY_MAP_METHOD, code);
		}

		{
			String code = referenceMapList(t, tam, qualify);
			logger().debug("generateImplementation: code=" + code);
			src = replaceAll(src, Tag.REFERENCE_MAP_LIST, code);
		}

		{
			String code = metaDataInitialization(cat, t, tam, tym, qualify);
			src = replaceAll(src, Tag.META_DATA_INITIALIZATION, code);
		}

		{
			String code = attributeContainerImplementation(cat, t, tam, tym);
			src = replaceAll(src, Tag.ATTRIBUTE_CONTAINER_IMPLEMENTATION, code);
		}

		{
			String code = attributeKeyContainerImplementation(cat, t, tam, tym);
			src = replaceAll(src, Tag.ATTRIBUTE_KEY_CONTAINER_IMPLEMENTATION,
					code);
		}

		{
			String code = attributesMethodStatementList(cat, t, tam, tym);
			src = replaceAll(src, Tag.ATTRIBUTES_METHOD_STATEMENT_LIST, code);
		}

		{
			String code = valueVariableList(cat, t, tam, tym);
			src = replaceAll(src, Tag.VALUE_VARIABLE_LIST, code);
		}

		{
			String code = valueAccessorList(cat, t, tam, tym, true);
			logger().debug("generateImplementation: code=" + code);
			src = replaceAll(src, Tag.VALUE_ACCESSOR_LIST, code);
		}

		src = replaceAll(src, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());
		src = replaceAll(src, Tag.TABLE_IMPL_CLASS, impl.getUnqualifiedName());
		src = replaceAll(src, Tag.TABLE_IMPL_BASE, base.getUnqualifiedName());

		src = replaceAll(src, Tag.LITERAL_CATALOG_NAME, tam
				.literalContextType().getQualifiedName());

		Environment te = getTargetEnvironment(cat.getEnvironment());
		src = replaceAll(src, Tag.ENVIRONMENT_EXPRESSION,
				generateEnvironmentExpression(te));

		src = replaceAll(src, Tag.LITERAL_TABLE_ENUM, lt);

		return src;
	}

	protected String readAndReplace(String src, Tag tag, boolean withComment)
			throws IOException {

		String content = read(tag);

		logger().debug(tag + ": code=" + content);

		src = withComment ? replaceAllWithComment(src, tag, content)
				: replaceAll(src, tag, content);

		return src;
	}

	private String generateEnvironmentExpression(final Environment env) {

		StringBuilder buf = new StringBuilder();
		buf.append(env.getClass().getCanonicalName());
		buf.append(".environment()");
		return buf.toString();
	}

	private String generateIdentifierRulesExpression(final Environment env) {
		StringBuilder buf = new StringBuilder();
		buf.append(generateEnvironmentExpression(env));
		buf.append(".getIdentifierRules()");
		return buf.toString();
	}

	private Environment getTargetEnvironment(final Environment catenv) {
		return (this.targetEnvironment == null) ? catenv
				: this.targetEnvironment;
	}

	private String generatePopulateColumnMapBlock(BaseTable t, TableMapper tm) {
		StringBuilder buf = new StringBuilder();

		line(buf, "// generatePopulateColumnMapBlock //");

		line(buf, "{");

		int ordinal = 1;

		for (Column c : t.columnMap().values()) {
			line(buf, "cmb.add(", columnConstantName(c, tm), ", ",
					Integer.toString(ordinal), ", ",
					generateNewDataType(c.getDataType()), ", ",
					autoIncrementBooleanConstant(c), ", ", null, ", ",
					definitelyNotNullableConstant(c), ", ", null, ");");

			ordinal++;
		}

		line(buf, "}");

		return buf.toString();
	}

	private String generateBaseTableColumnVariableList(BaseTable t, Tag tag,
			TableMapper tm) {
		StringBuilder buf = new StringBuilder();

		tag(buf, tag);

		ColumnMap columnMap = t.columnMap();

		Environment te = getTargetEnvironment(t.getEnvironment());

		for (Column c : columnMap.values()) {
			Identifier cn = c.getColumnName();
			String cc = columnConstantName(c, tm);
			line(buf, "private final ", identifierDeclaration(cc, cn, te));
		}

		return buf.toString();
	}

	private void tag(StringBuilder buf, Tag tag) {
		line(buf, "// ", tag.getTag());
	}

	private String generateGetBaseTableBody(BaseTable t, TableMapper tam) {
		StringBuilder buf = new StringBuilder();

		line(buf, "// generateGetBaseTableBody //");

		line(buf, "if (this.table == null) {");

		/**
		 * Environment env = PGEnvironment.environment(); IdentifierRules irls =
		 * env.getIdentifierRules();
		 * 
		 * com.appspot.relaxe.expr.Identifier c = null;
		 * com.appspot.relaxe.expr.Identifier s = irls.toIdentifier("public");
		 * com.appspot.relaxe.expr.Identifier t = irls.toIdentifier("test");
		 * 
		 * com.appspot.relaxe.expr.SchemaName schemaName = new
		 * com.appspot.relaxe.expr.SchemaName(c, s); SchemaElementName sen = new
		 * SchemaElementName(schemaName, t);
		 * 
		 * this.table = new ActorTable(env, sen);
		 */
		// line(buf, "Environment env = ",
		// t.getEnvironment().getClass().getCanonicalName(), ".environment();");

		String irvar = "irls";

		Environment te = getTargetEnvironment(t.getEnvironment());
		line(buf, Environment.class.getCanonicalName(), " env = ",
				generateEnvironmentExpression(te), ";");
		line(buf, IdentifierRules.class.getCanonicalName(), " ", irvar,
				" = env.getIdentifierRules();");

		SchemaElementName sen = t.getName();
		
		

		line(buf, identifierDeclaration("c", null, irvar));
		line(buf, identifierDeclaration("s", sen.getQualifier().getSchemaName(), irvar));
		line(buf, identifierDeclaration("t", sen.getUnqualifiedName(), irvar));

		JavaType intf = tam.entityType(t, Part.INTERFACE);

		String snt = SchemaName.class.getCanonicalName();
		String ent = SchemaElementName.class.getCanonicalName();

		line(buf, snt, " schemaName = new ", snt, "(c, s);");
		line(buf, ent, " sen = new ", ent, "(schemaName, t);");
		line(buf, "this.table = new ", intf.getUnqualifiedName(),
				"Table(env, sen);");

		line(buf, "}", 2);

		line(buf, "return this.table;", 1);

		return buf.toString();
	}

	private String generateCreateForeignKeyMapBody(BaseTable t, TableMapper tam) {
		StringBuilder buf = new StringBuilder();

		line(buf, "// generateCreateForeignKeyMapBody //");

		JavaType intf = tam.entityType(t, Part.INTERFACE);

		SchemaElementMap<ForeignKey> fm = t.foreignKeys();

		if (fm.isEmpty()) {
			line(buf, "return new ",
					EmptyForeignKeyMap.class.getCanonicalName(), "(env);");
		} else {
			// Map<Identifier, ForeignKey> fkmap = new TreeMap<Identifier,
			// ForeignKey>(env.getIdentifierRules().comparator());
			// fkmap.put(env.createIdentifier(""), new
			// EntityTableForeignKey(Film.Type.TYPE));
			// return new <X>ForeignKeyMap(env, fkmap);

			String idt = Identifier.class.getCanonicalName();
			String fkt = ForeignKey.class.getCanonicalName();

			line(buf, "java.util.Map<", idt, ", ", fkt,
					"> fkmap = new java.util.TreeMap<", idt, ", ", fkt,
					">(env.getIdentifierRules().comparator());");

			for (ForeignKey fk : fm.values()) {
				generateCreateForeignKeyBlock(buf, fk, t, tam);
			}

			line(buf, "return new ", intf.getUnqualifiedName(),
					"ForeignKeyMap(env, fkmap);");
		}

		return buf.toString();
	}

	private void generateCreateForeignKeyBlock(StringBuilder buf,
			ForeignKey fk, BaseTable t, TableMapper tam) {

		// {
		// Identifier constraintName = env.createIdentifier("FK_NAME");
		//
		// ImmutableColumnMap.Builder cmi = new ImmutableColumnMap.Builder(env);
		//
		// Identifier c1 = ID;
		// Identifier r1 = null;
		// Identifier c2 = null;
		// Identifier r2 = null;
		//
		// cmi.add(columnMap.get(c1));
		// cmi.add(columnMap.get(c2));
		//
		// Map<Identifier, Identifier> cm = new TreeMap<Identifier,
		// Identifier>(env.getIdentifierRules().comparator());
		// cm.put(c1, r1);
		// cm.put(c2, r2);
		//
		// ColumnMap fkcm = cmi.newColumnMap();
		//
		// ForeignKey fk = new ActorForeignKey(
		// this,
		// constraintName,
		// fkcm,
		// cm,
		// Film.Type.TYPE);
		//
		// fkmap.put(fk.getUnqualifiedName(), fk);
		// }

		line(buf, "{");

		Environment te = getTargetEnvironment(t.getEnvironment());
		line(buf,
				identifierDeclaration("constraintName",
						fk.getUnqualifiedName(), te));

		String bi = ImmutableColumnMap.Builder.class.getCanonicalName();

		line(buf, bi, " cmi = new ", bi, "(env);");

		final String ii = Identifier.class.getCanonicalName();

		line(buf, "java.util.Map<", ii, ", ", ii,
				"> cm = new java.util.TreeMap<", ii, ", ", ii,
				">(env.getIdentifierRules().comparator());");

		Collection<Column> cl = fk.getColumnMap().values();

		int ordinal = 1;

		for (Column column : cl) {
			String o = Integer.toString(ordinal);

			line(buf, ii, " c", o, " = ", columnConstantName(column, tam), ";");

			String rn = "r" + o;
			Identifier rc = fk.getReferencedColumnName(column
					.getUnqualifiedName());

			line(buf, identifierDeclaration(rn, rc, te), ";");

			line(buf, "cmi.add(columnMap.get(c", o, "));");
			line(buf, "cm.put(c", o, ", r", o, ");");

			ordinal++;
		}

		String cmi = ColumnMap.class.getCanonicalName();

		line(buf, cmi, " fkcm = cmi.newColumnMap();");
		line(buf, "", 1);

		// ForeignKey fk = new ActorForeignKey(
		// this,
		// constraintName,
		// fkcm,
		// cm,
		// Film.Type.TYPE);

		JavaType ti = tam.entityType(t, Part.INTERFACE);
		JavaType rt = tam.entityType(fk.getReferenced(), Part.INTERFACE);

		String fki = ForeignKey.class.getCanonicalName();

		line(buf, fki, " fk = new ", ti.getUnqualifiedName(), "ForeignKey(",
				"this, constraintName, fkcm, cm, ", rt.getQualifiedName(),
				".Type.TYPE);");

		line(buf, "fkmap.put(constraintName, fk);");

		line(buf, "}");
	}

	private String generateCreatePrimaryKeyBody(BaseTable t, TableMapper tm) {
		StringBuilder buf = new StringBuilder();

		line(buf, "// generateCreatePrimaryKeyBody: ");

		PrimaryKey pk = t.getPrimaryKey();

		if (pk == null) {
			line(buf, "return null; // Table does not have a primary key");
			return buf.toString();
		}

		// ImmutablePrimaryKey.Builder pkb = new
		// ImmutablePrimaryKey.Builder(this);
		// pkb.add(columnMap.get(env.createIdentifier("A")));
		// pkb.add(columnMap.get(env.createIdentifier("B")));
		// this.primaryKey = pkb.newConstraint();

		String kbi = ImmutablePrimaryKey.Builder.class.getCanonicalName();
		
		line(buf, kbi, " pkb = new ", kbi, "(this);");
		
		Identifier constraintName = pk.getUnqualifiedName();
		
		if (constraintName == null) {
			line(buf, "pkb.setConstraintName(null);");
		}
		else {
			line(buf, "pkb.setConstraintName(env.getIdentifierRules().toIdentifier(", literal(constraintName), "));");
		}
		
		
		ColumnMap cm = pk.getColumnMap();

		int size = cm.size();

		for (int i = 0; i < size; i++) {
			Column c = cm.get(i);
			// ColumnName n = c.getColumnName();
			// columnConstantName(c);

			// line(buf, "{");
			line(buf, "pkb.add(columnMap.get(", columnConstantName(c, tm),
					"));");
			// line(buf, "}");
		}

		line(buf, "return pkb.newConstraint();", 1);

		return buf.toString();
	}

	private String identifierDeclaration(String variableName, Identifier n,
			Environment te) {
		return identifierDeclaration(variableName, n,
				generateIdentifierRulesExpression(te));
	}

	private String identifierDeclaration(String variableName, Identifier n,
			String identifierRulesExpression) {
		StringBuilder buf = new StringBuilder();

		buf.append(Identifier.class.getCanonicalName());
		buf.append(" ");
		buf.append(variableName);
		buf.append(" = ");

		if (n == null) {
			buf.append("null");
		} else {
			n = normalize(n);

			// Class<?> ii = n.isDelimited() ? DelimitedIdentifier.class :
			// OrdinaryIdentifier.class;

			buf.append(identifierRulesExpression);
			buf.append(".");
			buf.append(n.isDelimited() ? "toDelimitedIdentifier"
					: "toIdentifier");
			buf.append("(");
			literal(buf, n.getName());
			buf.append(")");
		}

		buf.append(";");

		return buf.toString();
	}

	private String literal(Identifier name) {
		return literal(name == null ? null : name.getName());
	}

	private String literal(String name) {
		StringBuilder buf = new StringBuilder();
		literal(buf, name);
		return buf.toString();
	}

	private void literal(StringBuilder buf, String name) {

		if (name == null) {
			buf.append("null");
			return;
		}

		buf.append('\"');

		int len = name.length();

		for (int i = 0; i < len; i++) {
			char c = name.charAt(i);

			boolean escape = (c == '\b') && (c == '\t') && (c == '\n')
					&& (c == '\f') && (c == '\r') && (c == '\\') && (c == '\"');

			if (escape) {
				buf.append('\\');
			}

			buf.append(c);
		}

		buf.append('\"');
	}

	private void line(StringBuilder buf, String lc) {
		line(buf, lc, 1);
	}

	private void line(StringBuilder buf, String ... elems) {
		for (int i = 0; i < elems.length; i++) {
			boolean eol = (i == (elems.length - 1));
			line(buf, elems[i], eol ? 1 : 0);
		}
	}

	private String attributesMethodStatementList(Catalog cat, BaseTable t,
			TableMapper tam, TypeMapper tym) {
		StringBuilder buf = new StringBuilder();

		buf.append("\n// attributesMethodStatementList() - enter\n");

		JavaType intf = tam.entityType(t, Part.INTERFACE);
		String et = intf.getQualifiedName();

		List<Column> cols = getAttributeColumnList(cat, t, tym);

		for (Column c : cols) {
			AttributeInfo ai = tym.getAttributeInfo(c.getDataType());

			if (ai == null) {
				logger().warn(
						"no attribute-info for column : " + columnName(t, c));
				continue;
			}

			if (ai.getKeyType() == null) {
				logger().warn(
						"no key type for column : "
								+ c.getColumnName().getName());
				logger().warn(
						"column-data-type : " + c.getDataType().getDataType());
				logger().warn(
						"column-type-name : " + c.getDataType().getTypeName());
				continue;
			}

			if (ai.getHolderType() == null) {
				logger().warn(
						"no holder type for column : "
								+ c.getColumnName().getName());
				logger().warn(
						"column-data-type : " + c.getDataType().getDataType());
				logger().warn(
						"column-type-name : " + c.getDataType().getTypeName());
				continue;
			}

			String vv = valueVariableName(t, c);

			buf.append("if (").append(vv).append(" != null) {\n\t");
			buf.append("attrs.add(").append(et).append(".")
					.append(getAttributeType()).append(".").append(attr(c))
					.append(");\n");
			buf.append("}\n");
		}

		buf.append("\n// attributesMethodStatementList() - exit\n");

		String code = buf.toString();
		return code;
	}

	private String builderLinkerInit(BaseTable table, TableMapper tm,
			boolean qualify) throws IOException {
		StringBuilder buf = new StringBuilder();

		Collection<ForeignKey> fks = table.foreignKeys().values();

		if (!fks.isEmpty()) {
			line(buf, "MetaData m = getMetaData();");
			line(buf,
					"java.util.List<Linker> ll = new java.util.ArrayList<Linker>();",
					2);

			for (ForeignKey fk : fks) {
				String c = formatBuilderLinkerInit(fk, tm);
				buf.append(c);
				buf.append("\n\n");
			}

			line(buf, "this.linkerList = ll.toArray(this.linkerList);", 2);
		}

		return buf.toString();
	}

	private String generateCreateIdentityMapMethod(BaseTable t,
			TableMapper tam, TypeMapper tm) {
		PrimaryKey pk = t.getPrimaryKey();

		StringBuilder buf = new StringBuilder();

		if (pk != null) {
			// List<Column> cl = pk.columns();
			ColumnMap cm = pk.getColumnMap();

			if (cm.size() == 1) {
				Column col = cm.get(0);
				AttributeInfo ai = tm.getAttributeInfo(col.getDataType());

				// Sample output:
				// @Override
				// public IdentityMap<Attribute, Reference, Type,
				// TestGeneratedKey, TestGeneratedKey.Holder,
				// TestGeneratedKey.MetaData> createIdentityMap() {
				// return new IntIdentityMap<Attribute, Reference, Type,
				// TestGeneratedKey, TestGeneratedKey.Holder,
				// TestGeneratedKey.MetaData>(TestGeneratedKey.ABC);
				// }

				Class<?> aim = ai.getIdentityMapType();

				if (aim != null) {
					JavaType intf = tam.entityType(t, Part.INTERFACE);

					String kv = keyConstantVariable(t, col, tam);

					buf.append("@Override\n");
					buf.append("public ");
					buf.append(EntityIdentityMap.class.getCanonicalName());
					buf.append("<");
					buf.append(getAttributeType());
					buf.append(", ");
					buf.append(getReferenceType());
					buf.append(", Type, ");
					buf.append(intf.getUnqualifiedName());
					buf.append(", ");
					buf.append(intf.getUnqualifiedName());
					buf.append(".Holder");
					buf.append(", ");
					buf.append(intf.getUnqualifiedName());
					buf.append(".MetaData");
					buf.append("> createIdentityMap() {\n");
					buf.append("return new ");
					buf.append(aim.getCanonicalName());
					buf.append("<");
					buf.append(getAttributeType());
					buf.append(", ");
					buf.append(getReferenceType());
					buf.append(", Type, ");
					buf.append(intf.getUnqualifiedName());
					buf.append(", ");
					buf.append(intf.getUnqualifiedName());
					buf.append(".Holder");
					buf.append(", ");
					buf.append(intf.getUnqualifiedName());
					buf.append(".MetaData");
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
		StringBuilder buf = new StringBuilder();

		Map<BaseTable, JavaType> map = referenced(t, tm);

		for (Entry<BaseTable, JavaType> e : map.entrySet()) {
			String c = formatReferenceValueMap(t, e.getKey(), e.getValue(), tm,
					qualify);
			buf.append(c);
		}

		return buf.toString();
	}

	private String formatReferenceValueMap(BaseTable t, BaseTable referenced,
			JavaType target, TableMapper tm, boolean qualify) {

		// private Map<Reference, Organization.Holder> om = new
		// HashMap<Reference, Organization.Holder>();
		//
		// @Override
		// public com.appspot.relaxe.gen.ent.personal.Organization.Holder
		// getOrganization(
		// com.appspot.relaxe.gen.ent.personal.Organization.Key<?, ?, ?, ?, ?>
		// ok) {
		// return om.get(ok);
		// }

		StringBuilder buf = new StringBuilder();

		JavaType ti = tm.entityType(referenced, Part.INTERFACE);

		final StringBuilder ktbuf = new StringBuilder();
		ktbuf.append(ti.getQualifiedName());
		ktbuf.append(".Key<");
		ktbuf.append(referenceKeyTypeArgs(tm, t));
		ktbuf.append(">");

		final String kt = ktbuf.toString();

		String vm = referenceValueMapVariable(referenced, target, qualify);

		String hn = target.getQualifiedName() + ".Holder";

		buf.append("private java.util.Map<");
		buf.append(kt);
		buf.append(", ");
		buf.append(hn);
		buf.append(">");
		buf.append(" ");
		buf.append(vm);
		buf.append(" = new java.util.HashMap<");
		buf.append(ktbuf);
		buf.append(", ");
		buf.append(hn);
		buf.append(">();\n\n");

		buf.append("@Override\npublic ");
		buf.append(hn);
		buf.append(" get");
		buf.append(target.getUnqualifiedName());
		buf.append("(");
		buf.append(kt);
		buf.append(" key) {\n");
		buf.append("\treturn this.");
		buf.append(vm);
		buf.append(".get(key);\n");
		buf.append("}\n\n");

		buf.append("@Override\npublic void ");
		buf.append("set");
		buf.append(target.getUnqualifiedName());
		buf.append("(");
		buf.append(kt);
		buf.append(" key, ");
		buf.append(target.getQualifiedName());
		buf.append(".Holder newValue) {\n");
		buf.append("\tthis.");
		buf.append(vm);
		buf.append(".put(key, newValue);\n");
		buf.append("}\n\n");

		return buf.toString();
	}

	/***
	 * @param table
	 * @return
	 */
	private boolean hasAmbiguousSimpleNamesForReferenceKeys(BaseTable table,
			TableMapper tm) {
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
				} else {
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

	private Set<BaseTable> referencedTables(BaseTable table) {
		Set<BaseTable> rs = new HashSet<BaseTable>();

		for (ForeignKey k : table.foreignKeys().values()) {
			BaseTable referenced = k.getReferenced();
			rs.add(referenced);
		}

		return rs;
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

	private String referenceKeyClassList(BaseTable table, TableMapper tm,
			boolean qualify) {
		StringBuilder buf = new StringBuilder();
		Map<BaseTable, JavaType> map = referenced(table, tm);

		for (Entry<BaseTable, JavaType> r : map.entrySet()) {
			buf.append(formatReferenceKeyClass(table, r.getKey(), qualify, tm));
		}

		return buf.toString();
	}

	private String formatReferenceKeyClass(BaseTable referencing,
			BaseTable referenced, boolean qualify, TableMapper tm) {

		// // Sample output:
		// public static class OrganizationKey
		// extends Organization.Key<Person.Reference, Person.Type, Person,
		// Person.MetaData, Content> {
		//
		// private static final long serialVersionUID = 1L;
		//
		// protected OrganizationKey(
		// EntityMetaData<com.appspot.relaxe.gen.ent.personal.Person.Attribute,
		// Reference, Type, Person, Content> meta, Reference name) {
		// super(Person.TYPE, name);
		// }

		// TODO: implement get()
		// @Override
		// public com.appspot.relaxe.gen.ent.personal.Organization.Holder
		// get(Person e) {
		// return e.getOrganization(this);
		// }

		//
		// @Override
		// public OrganizationKey self() {
		// return this;
		// }

		// @Override
		// public void set(Person e,
		// com.appspot.relaxe.gen.ent.personal.Organization.Holder newValue) {
		// e.setOrganization(self(), newValue);
		// }
		//
		// }

		JavaType src = tm.entityType(referencing, Part.INTERFACE);
		JavaType target = tm.entityType(referenced, Part.INTERFACE);

		StringBuilder buf = new StringBuilder();

		// Key implementation must be public to be GWT serializable
		buf.append("public static class ");

		String n = referenceKeyImplementationName(referenced, target, qualify);

		// String kta = keyTypeArgs(tm, referencing, true);
		String kta = referenceKeyTypeArgs(tm, referencing);

		buf.append(n);
		buf.append(" extends ");
		buf.append(target.getQualifiedName());
		buf.append(".Key<");
		buf.append(kta);
		buf.append("> {\n");
		buf.append("private static final long serialVersionUID = 1L;\n");

		// no-arg constructor
		buf.append("private ");
		buf.append(n);
		buf.append("() {\n");
		buf.append("}\n\n");

		buf.append("private ");
		buf.append(n);
		buf.append("(");
		buf.append(getReferenceType());
		buf.append(" name) {\n");
		buf.append("super(");
		buf.append(src.getUnqualifiedName());
		buf.append(".Type.TYPE, ");
		buf.append("name);");
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

	private String referenceKeyReferenceTypeName(BaseTable referencing,
			BaseTable referenced, TableMapper tm, boolean qualify) {
		StringBuilder nb = new StringBuilder();

		if (qualify) {
			appendSchemaPrefix(referenced, nb);
		}

		// Project.Key<HourReport.Reference, Type, HourReport,
		// HourReport.MetaData, ?>

		JavaType source = tm.entityType(referencing, Part.INTERFACE);
		JavaType target = tm.entityType(referenced, Part.INTERFACE);

		nb.append(target.getQualifiedName());
		nb.append(".");
		nb.append("Key<");
		nb.append(source.getUnqualifiedName());
		nb.append(".");
		nb.append(getAttributeType());
		nb.append(", ");

		nb.append(source.getUnqualifiedName());
		nb.append(".");
		nb.append(getReferenceType());
		nb.append(", ");

		nb.append(source.getUnqualifiedName());
		nb.append(".Type, ");

		nb.append(source.getUnqualifiedName());
		nb.append(", ");

		nb.append(source.getUnqualifiedName());
		nb.append(".");
		nb.append("Holder");
		nb.append(", ");

		nb.append(source.getUnqualifiedName());
		nb.append(".");
		nb.append("Factory");
		nb.append(", ");

		nb.append(source.getUnqualifiedName());
		nb.append(".MetaData");
		nb.append(", ");

		nb.append(source.getUnqualifiedName());
		nb.append(".Content");

		nb.append(">");

		return nb.toString();
	}

	private void appendSchemaPrefix(SchemaElement element, StringBuilder nb) {
		SchemaName n = element.getName().getQualifier();
		String prefix = (n == null) ? "" : name(n.getSchemaName().getName());
		nb.append(prefix);
	}

	private String referenceKeyImplementationName(BaseTable referenced,
			JavaType target, boolean qualify) {
		StringBuilder nb = new StringBuilder();

		if (qualify) {
			appendSchemaPrefix(referenced, nb);
		}

		nb.append(target.getUnqualifiedName());
		nb.append("Key");

		return nb.toString();
	}

	private String referenceKeyMapVariable(BaseTable referenced,
			JavaType target, boolean qualify) {
		StringBuilder nb = new StringBuilder();

		if (qualify) {
			appendSchemaPrefix(referenced, nb);
		}

		nb.append(decapitalize(target.getUnqualifiedName()));
		nb.append("KeyMap");

		return nb.toString();
	}

	private String referenceValueMapVariable(BaseTable referenced,
			JavaType target, boolean qualify) {
		StringBuilder nb = new StringBuilder();

		if (qualify) {
			appendSchemaPrefix(referenced, nb);
		}

		nb.append(decapitalize(target.getUnqualifiedName()));
		nb.append("Map");

		return nb.toString();
	}

	private String valueVariableList(Catalog cat, BaseTable t, TableMapper tm,
			TypeMapper tym) {
		List<Column> acl = getAttributeColumnList(cat, t, tym);
		StringBuilder content = new StringBuilder();

		for (Column c : acl) {
			String code = formatValueVariable(t, c, tm, tym);
			content.append(code);
		}

		return content.toString();
	}

	private String valueAccessorList(Catalog cat, BaseTable t, TableMapper tm,
			TypeMapper tym, boolean impl) {
		List<Column> acl = getAttributeColumnList(cat, t, tym);
		StringBuilder content = new StringBuilder();

		for (Column c : acl) {
			String code = formatValueAccessor(t, c, tm, tym, impl);
			content.append(code);
		}

		return content.toString();
	}

	private String formatValueVariable(BaseTable t, Column c, TableMapper tm,
			TypeMapper tym) {

		// private transient VarcharValue<Person.Attribute, Person> lastName;

		StringBuilder buf = new StringBuilder();

		AttributeInfo a = tym.getAttributeInfo(c.getDataType());

		if (a == null) {
			return "";
		}

		Class<?> at = a.getAccessorType();
		Class<?> kt = a.getKeyType();

		if (kt == null || at == null) {
			return "";
		}

		// JavaType intf = tm.entityType(t, Part.INTERFACE);

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
		StringBuilder buf = new StringBuilder();
		buf.append(reference ? getReferenceType() : getAttributeType());
		buf.append(", ");
		buf.append(intf.getUnqualifiedName());
		return buf.toString();
	}

	private String referenceKeyTypeArgs(TableMapper tm, BaseTable t) {
		JavaType intf = tm.entityType(t, Part.INTERFACE);
		StringBuilder buf = new StringBuilder();

		String q = intf.getUnqualifiedName();

		buf.append(q);
		buf.append(".");
		buf.append(getAttributeType());
		buf.append(", ");

		buf.append(q);
		buf.append(".");
		buf.append(getReferenceType());
		buf.append(", ");

		buf.append(q);
		buf.append(".");
		buf.append("Type");
		buf.append(", ");

		buf.append(q);
		buf.append(", ");

		buf.append(q);
		buf.append(".Holder");
		buf.append(", ");

		buf.append(q);
		buf.append(".Factory");
		buf.append(", ");

		buf.append(q);
		buf.append(".MetaData");
		buf.append(", ");

		buf.append(q);
		buf.append(".Content");

		return buf.toString();
	}

	private String formatValueAccessor(BaseTable t, Column c, TableMapper tm,
			TypeMapper tym, boolean impl) {

		// // output example:
		// public VarcharValue<Attribute, Person> lastName() {
		// if (this.lastName == null) {
		// // this.lastName = varcharValue(Person.LAST_NAME);
		// this.lastName = new VarcharValue<Person, Attribute>(self(),
		// Person.LAST_NAME);
		// }
		//
		// return this.lastName;
		// }

		StringBuilder buf = new StringBuilder();

		AttributeInfo a = tym.getAttributeInfo(c.getDataType());

		if (a == null) {
			return "";
		}

		Class<?> at = a.getAccessorType();
		Class<?> kt = a.getKeyType();

		if (kt == null || at == null) {
			return "";
		}

		logger().debug("formatValueAccessor: value-type=" + at);

		JavaType intf = tm.entityType(t, Part.INTERFACE);
		String vv = valueVariableName(t, c);
		String ke = keyConstantExpression(t, c, intf, tm);

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

		if (!impl) {
			buf.append(";\n\n");
		} else {
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
		return variableName(c.getColumnName().getName());
	}

	private String variableName(String n) {
		return decapitalize(name(n));
	}

	private String metaDataInitialization(Catalog cat, BaseTable t,
			TableMapper tm, TypeMapper tym, boolean qualify) {
		StringBuilder buf = new StringBuilder();
		buf.append(attributeKeyInitialization(cat, t, tm, tym));
		buf.append(referenceKeyInitialization(cat, t, tm, qualify));
		return buf.toString();
	}

	private String attributeKeyInitialization(Catalog cat, BaseTable t,
			TableMapper tm, TypeMapper tym) {
		List<Column> acl = getAttributeColumnList(cat, t, tym);
		StringBuilder content = new StringBuilder();

		for (Column c : acl) {
			AttributeInfo a = tym.getAttributeInfo(c.getDataType());

			if (a == null) {
				continue;
			}

			Class<?> kt = a.getKeyType();

			if (kt == null) {
				logger().warn(
						"no key type for column " + c.getUnqualifiedName());
			} else {
				content.append(kt.getCanonicalName());
				content.append(".get(this, Attribute.");
				content.append(attr(c));
				content.append(");\n");
			}
		}

		return content.toString();
	}

	private String attributeKeyMapList(Catalog cat, BaseTable t,
			TableMapper tm, TypeMapper tym) {
		StringBuilder buf = new StringBuilder();
		Map<Class<?>, Integer> tom = keyTypeOccurenceMap(cat, t, tm, tym);
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

	private String referenceKeyMapList(BaseTable referencing, TableMapper tm,
			boolean qualify) {
		StringBuilder buf = new StringBuilder();

		Map<BaseTable, JavaType> map = referenced(referencing, tm);

		for (Entry<BaseTable, JavaType> e : map.entrySet()) {
			String c = formatReferenceKeyMap(referencing, e.getKey(),
					e.getValue(), tm, qualify);
			buf.append(c);
		}

		return buf.toString();
	}

	private Map<Class<?>, Integer> keyTypeOccurenceMap(Catalog cat,
			BaseTable t, TableMapper tm, TypeMapper tym) {
		List<Column> acl = getAttributeColumnList(cat, t, tym);

		// key -type => occurences:
		Map<Class<?>, Integer> tom = new HashMap<Class<?>, Integer>();

		for (Column c : acl) {
			AttributeInfo a = tym.getAttributeInfo(c.getDataType());

			if (a == null) {
				continue;
			}

			Class<?> k = a.getKeyType();

			if (k == null) {
				logger().warn(
						"no key type for column : "
								+ c.getColumnName().getName());
				logger().warn(
						"column-data-type : " + c.getDataType().getDataType());
				logger().warn(
						"column-type-name : " + c.getDataType().getTypeName());
			} else {
				Integer o = tom.get(k);
				o = (o == null) ? Integer.valueOf(1) : Integer.valueOf(o
						.intValue() + 1);
				tom.put(k, o);
			}
		}

		return tom;
	}

	// only non-fk-columns are included in attributes.
	// fk-columns are not intended to be set individually,
	// but atomically with ref -methods

	private List<Column> getAttributeColumnList(Catalog cat, BaseTable t,
			TypeMapper tym) {
		Set<Identifier> fkcols = foreignKeyColumnMap(cat, t).keySet();
		List<Column> attrs = new ArrayList<Column>();

		for (Column c : t.columnMap().values()) {
			if (!fkcols.contains(c.getColumnName())) {
				attrs.add(c);
			}
		}

		return attrs;
	}

	private String attributeKeyList(Catalog cat, BaseTable t, TableMapper tm,
			TypeMapper tym) {
		List<Column> acl = getAttributeColumnList(cat, t, tym);
		StringBuilder content = new StringBuilder();

		for (Column c : acl) {
			String code = formatAttributeKey(t, c, tm, tym);
			content.append(code);
		}

		return content.toString();
	}

	private String referenceKeyList(BaseTable t, TableMapper tm) {
		StringBuilder content = new StringBuilder();

		for (ForeignKey fk : t.foreignKeys().values()) {
			String r = formatReferenceKey(fk, tm);

			if (r == null || r.equals("")) {
				continue;
			}

			content.append(r);
		}

		return content.toString();
	}

	private String referenceKeyInitialization(Catalog cat, BaseTable t,
			TableMapper tm, boolean qualify) {
		StringBuilder content = new StringBuilder();

		// // sample output:
		// {
		// ProjectKey pk = new ProjectKey(this,
		// HourReport.Reference.FK_HHR_PROJECT);
		// projectKeyMap.put(pk.name(), pk);
		// entityKeyMap.put(pk.name(), pk);
		// }

		JavaType jt = tm.entityType(t, Part.INTERFACE);

		for (ForeignKey fk : t.foreignKeys().values()) {
			content.append("{\n");

			BaseTable r = fk.getReferenced();
			JavaType kt = tm.entityType(r, Part.INTERFACE);

			String n = referenceKeyImplementationName(r, kt, qualify);
			content.append(n);
			content.append(" k = new ");
			content.append(n);
			content.append("(");
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

		// Organization.Key<Attribute, Reference, Type, Person, ?> EMPLOYER =
		// com.appspot.relaxe.gen.ent.personal.PersonImpl.PersonMetaData.getInstance().getOrganizationKey(Reference.EMPLOYER);
		String referenceName = referenceName(fk);

		JavaType tt = tm.entityType(fk.getReferenced(), Part.INTERFACE);
		JavaType rt = tm.entityType(fk.getReferencing(), Part.INTERFACE);
		JavaType it = tm.entityType(fk.getReferencing(), Part.IMPLEMENTATION);

		StringBuilder buf = new StringBuilder();

		buf.append("public static final ");

		buf.append(tt.getQualifiedName());
		buf.append(".Key");
		buf.append("<");
		buf.append(rt.getUnqualifiedName());
		buf.append(".");
		buf.append(getAttributeType());
		buf.append(",");

		buf.append(rt.getUnqualifiedName());
		buf.append(".");
		buf.append(getReferenceType());
		buf.append(", ");

		buf.append(it.getUnqualifiedName());
		buf.append(".");
		buf.append("Type, ");

		buf.append(rt.getUnqualifiedName());
		buf.append(", ");

		buf.append(rt.getUnqualifiedName());
		buf.append(".");
		buf.append("Holder");
		buf.append(", ");

		buf.append(rt.getUnqualifiedName());
		buf.append(".");
		buf.append("Factory");
		buf.append(", ");

		buf.append(rt.getUnqualifiedName());
		buf.append(".MetaData");
		buf.append(", ");

		buf.append(rt.getUnqualifiedName());
		buf.append(".Content");

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

	private String formatAttributeKeyMap(BaseTable t, Class<?> keyType,
			int occurences, TableMapper tm) {
		StringBuilder buf = new StringBuilder();

		// JavaType intf = tm.entityType(t, Part.INTERFACE);

		// goal:
		// private Map<Attribute, IntegerKey<Attribute, Person>> integerKeyMap =
		// new HashMap<Attribute, IntegerKey<Attribute,Person>>();

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

		String kt = getKeyName(keyType);

		// buf.append("@Override\n");
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

	private String getKeyName(Class<?> keyType) {
		String kt = null;

		if (keyType.getEnclosingClass() == null) {
			kt = getSimpleName(keyType);
		} else {
			kt = getSimpleName(keyType, true)
					+ getSimpleName(keyType.getEnclosingClass());
		}
		return kt;
	}

	private String formatReferenceKeyMap(BaseTable t, BaseTable referenced,
			JavaType target, TableMapper tm, boolean qualify) {
		StringBuilder buf = new StringBuilder();

		JavaType intf = tm.entityType(t, Part.INTERFACE);

		// sample output:
		// private java.util.Map<HourReport.Reference, ProjectKey> projectKeyMap
		// = new java.util.HashMap<HourReport.Reference, ProjectKey>();

		// private java.util.Map<HourReport.Reference, ProjectKey> projectKeyMap
		// = new java.util.HashMap<HourReport.Reference, ProjectKey>();
		//
		// public com.appspot.relaxe.gen.ent.personal.Project.Key<Attribute,
		// Reference, Type, HourReport, ?> getProjectKey(Reference ref) {
		// if (ref == null) {
		// throw new NullPointerException("ref");
		// }
		//
		// return this.projectKeyMap.get(ref);
		// }

		// private java.util.Map<HourReport.Reference, Project.Key<Reference,
		// Type, HourReport, HourReport.MetaData, ?>> projectKeyMap =
		// new java.util.HashMap<HourReport.Reference, Project.Key<Reference,
		// Type, HourReport, HourReport.MetaData, ?>>();
		//
		// public com.appspot.relaxe.gen.ent.personal.Project.Key<Reference,
		// Type, HourReport, HourReport.MetaData, ?> getProjectKey( Reference
		// ref) {
		// if (ref == null) {
		// throw new NullPointerException("ref");
		// }
		//
		// return this.projectKeyMap.get(ref);
		// }

		String rki = referenceKeyReferenceTypeName(t, referenced, tm, qualify);
		String rkimp = referenceKeyImplementationName(referenced, target,
				qualify);
		String rkv = referenceKeyMapVariable(referenced, target, qualify);

		String ta = referenceKeyTypeArgs(tm, t);

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
		buf.append(ta);
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
		// {
		// final Project.Key<Reference, Type, HourReport, HourReport.MetaData,
		// HourReport.Content> pk = FK_HHR_PROJECT;
		// ForeignKey fk = m.getForeignKey(pk.name());
		// TableReference tref = ctx.getQuery().getReferenced(tableRef, fk);
		//
		// Project.MetaData pm = ProjectImpl.ProjectMetaData.getInstance();
		// final com.appspot.relaxe.gen.ent.personal.Project.Builder nb =
		// pm.newBuilder();
		//
		// ll.add(new Linker() {
		// @Override
		// public void link(DataObject src, HourReport dest) {
		// Project np = nb.read(src);
		// pk.set(dest, np);
		// }
		// });
		// }

		JavaType intf = tm.entityType(fk.getReferencing(), Part.INTERFACE);
		JavaType ritf = tm.entityType(fk.getReferenced(), Part.INTERFACE);

		String src = getTemplateForBuilderLinkerInit();

		src = replaceAll(src, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());
		src = replaceAll(src, Tag.REFERENCED_TABLE_INTERFACE_QUALIFIED,
				ritf.getQualifiedName());

		src = replaceAll(src, Tag.REFERENCE_KEY_VARIABLE, referenceName(fk));

		return src;
	}

	private String formatAttributeKey(BaseTable t, Column c, TableMapper tm,
			TypeMapper tym) {
		StringBuilder buf = new StringBuilder();

		AttributeInfo a = tym.getAttributeInfo(c.getDataType());

				
		if (a == null) {
			String msg = columnDescription("no attribute-info for ", t, c);
			logger().warn(msg);
			return "";
		}

		Class<?> kc = a.getKeyType();

		if (kc == null) {
			String msg = columnDescription("no attribute-info key type for ", t, c);
			logger().warn(msg);
			return "";
		}

		JavaType intf = tm.entityType(t, Part.INTERFACE);

		JavaType impl = tm.entityType(t, Part.IMPLEMENTATION);

		String keyTypeName = null;

		if (kc.getEnclosingClass() == null) {
			keyTypeName = getSimpleName(kc);
		} else {
			keyTypeName = getSimpleName(kc, true)
					+ getSimpleName(kc.getEnclosingClass());
		}

		// public static final IntegerKey<Attribute, Person> ID = new
		// IntegerKey<Attribute, Person>(Attribute.ID);"

		buf.append("public static final ");
		buf.append(kc.getCanonicalName());
		buf.append("<");
		buf.append(keyTypeArgs(tm, t, false));
		buf.append("> ");
		buf.append(keyConstantVariable(t, c, tm));
		buf.append(" = ");
		buf.append(impl.getQualifiedName());
		buf.append(".");
		buf.append(intf.getUnqualifiedName());
		buf.append("MetaData.getInstance().get");
		buf.append(keyTypeName);
		buf.append("(");
		buf.append("Attribute");
		buf.append(".");
		buf.append(columnEnumeratedName(t, c, NameQualification.COLUMN, tm));
		buf.append(");\n");

		return buf.toString();
	}

	protected String columnDescription(String prefix, BaseTable t, Column c) {
		DataType ct = c.getDataType();		
		int type = ct.getDataType();
		
		String cdesc = "column " + columnName(t, c) + " of type (" + type
				+ ") (" + AbstractPrimitiveType.getSQLTypeName(type) + ") ("
				+ ct.getTypeName() + ")";
		
		return cdesc;
	}

	private String keyConstantExpression(BaseTable t, Column c, JavaType intf,
			TableMapper tm) {
		return intf.getQualifiedName() + "." + keyConstantVariable(t, c, tm);
	}

	// private String keyConstantExpression(BaseTable t, Column c, TableMapper
	// tm) {
	// JavaType intf = tm.entityType(t, Part.INTERFACE);
	// return keyConstantExpression(t, c, intf);
	// }

	private String keyConstantVariable(BaseTable t, Column c, TableMapper tm) {
		return columnEnumeratedName(t, c, NameQualification.COLUMN, tm);
	}

	private String columnEnumeratedName(BaseTable t, Column c,
			NameQualification nq, TableMapper tm) {
		return columnEnumeratedName(t, c, EnumSet.of(nq), tm);
	}
	
	private String entityAttributeAccessorList(Catalog cat, BaseTable t, TableMapper tam, TypeMapper tym, boolean impl) {
		
		StringBuilder buf = new StringBuilder();
		
		List<Column> cols = getAttributeColumnList(cat, t, tym);

		for (Column c : cols) {
			AttributeInfo a = tym.getAttributeInfo(c.getDataType());

			if (a == null) {
				continue;
			}

			Class<?> at = a.getAttributeType();
			Class<?> ht = a.getHolderType();

			if (at != null && ht != null) {
				String code = formatEntityAttributeAccessor(t, c, a, tam, tym, impl);
				buf.append(code);
			}
		}
		
		return buf.toString();
	}

	private String formatEntityAttributeAccessor(Table table, Column c, AttributeInfo info, TableMapper tam, TypeMapper tym, boolean impl) {
		// final String attributeName = attr(c);
		Class<?> attributeType = info.getAttributeType();
		Class<?> holderType = info.getHolderType();
		
		String htname = holderType.getCanonicalName();

		final String type = attributeType.getCanonicalName();

		StringBuilder nb = new StringBuilder();
		final String n = name(c.getColumnName().getName());

		boolean b = attributeType.equals(Boolean.class);
		
		JavaType intf = tam.entityType(table, Part.INTERFACE);

		// String vv = valueVariableName(table, c);			

		String prefix = b ? "is" : "get";
		
		String typeName = getAccessorNameSuffix(c, tym);
		
		if (impl) {
			line(nb, "@Override");
		}
				

		a(nb, "public ");
		a(nb, type);
		a(nb, " ");
		a(nb, prefix);
		a(nb, n);
		a(nb, "()");

		if (!impl) {
			line(nb, ";", 1);
		} else {
			// getter implementation
			line(nb, " {", 1);
					
			// example: 
			// IntegerHolder h = getInteger(Film.FILM_ID);
			// return (h == null) ? null : h.value();
								
			line(nb, htname, " h = ", "get", typeName, "(", intf.getQualifiedName(), ".", attr(c), ");");			
			line(nb, "return (h == null) ? null : h.value();");
			
			line(nb, "}", 2);
		}
		

		if (holderType != null) {
			
//		    sample:
//			@Override
//		    public void setFilmId(java.lang.Integer newValue) {
//		    	setInteger(FILM_ID, IntegerHolder.valueOf(newValue));    	
//		    }
			
			if (impl) {
				line(nb, "@Override");
			}			
			
			line(nb, "public void set", n, "(", type, " newValue)");
			
			if (!impl) {
				line(nb, ";", 1);
			} else {
				line(nb, " {", 1);
				line(nb, "set", typeName, "(", intf.getQualifiedName(), ".", attr(c), ", ", htname, ".valueOf(newValue));");				
				line(nb, "}", 2);
			}

//			sample:
//		    @Override
//		    public void setFilmId(IntegerHolder newValue) {
//		    	setInteger(FILM_ID, newValue);    	
//		    }    		
			
			if (impl) {
				line(nb, "@Override");
			}			
			line(nb, "public void set", n, "(", htname, " newValue)");
			
			if (!impl) {
				line(nb, ";", 1);
			} else {
				line(nb, " {", 1);
				line(nb, "set", typeName, "(", intf.getQualifiedName(), ".", attr(c), ", newValue);");				
				line(nb, "}", 2);
			}			
		}

		return nb.toString();
	}	

	private String contentAccessors(Catalog cat, BaseTable t, TableMapper tm,
			TypeMapper tym, boolean impl) {
		StringBuilder content = new StringBuilder();
		contentAccessors(cat, t, content, tm, tym, impl);
		return content.toString();
	}

	private void contentAccessors(Catalog cat, BaseTable t, StringBuilder content,
			TableMapper tm, TypeMapper tym, boolean impl) {
		List<Column> cols = getAttributeColumnList(cat, t, tym);

		for (Column c : cols) {
			AttributeInfo a = tym.getAttributeInfo(c.getDataType());

			if (a == null) {
				continue;
			}

			Class<?> at = a.getAttributeType();
			Class<?> ht = a.getHolderType();

			if (at != null && ht != null) {
				String code = formatContentAccessors(t, c, a, impl);
				content.append(code);
			}
		}
	}

	private String formatContentAccessors(Table table, Column c, AttributeInfo info,
			boolean impl) {
		// final String attributeName = attr(c);
		Class<?> attributeType = info.getAttributeType();
		Class<?> holderType = info.getHolderType();

		final String type = attributeType.getCanonicalName();

		StringBuilder nb = new StringBuilder();
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
			line(nb, ";", 1);
		} else {
			// getter implementation
			line(nb, " {", 1);

			// example: return id().get();

			a(nb, "return ");
			a(nb, vv);
			line(nb, "().get();", 1);
			line(nb, "}", 2);
		}

		if (holderType != null) {
			a(nb, "public void set");
			a(nb, n);
			a(nb, "(");
			a(nb, type);
			a(nb, " ");
			a(nb, "newValue)");

			if (!impl) {
				line(nb, ";", 1);
			} else {
				line(nb, " {", 1);
				a(nb, vv);
				line(nb, "().set(newValue);", 1);
				line(nb, "}", 2);
			}
		}

		return nb.toString();
	}

	private String getSimpleName(Class<?> attributeType) {
		return getSimpleName(attributeType, false);
	}

	private String getSimpleName(Class<?> attributeType, boolean canon) {
		String tn = canon ? attributeType.getCanonicalName() : attributeType
				.getName();
		int pos = tn.lastIndexOf(".");
		tn = tn.substring(pos < 0 ? 0 : pos + 1);
		return tn;
	}

	private String getKeyMapVariable(Class<?> keyType) {
		StringBuilder buf = new StringBuilder();

		String n = getSimpleName(keyType) + "Map";

		n = n.replace("$", "");
		// n = n.replace('$', '_');

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
		StringBuilder nb = new StringBuilder(len);
		boolean upper = true;

		for (int i = 0; i < len; i++) {
			char c = identifier.charAt(i);

			if (c == '_' || c == ' ') {
				upper = true;
				continue;
			}
			
			nb.append(upper ? Character.toUpperCase(c) : Character.toLowerCase(c));
			upper = false;
		}

		return nb.toString();
	}

	private String attrs(Catalog cat, BaseTable t, TableMapper tm,
			TypeMapper tym) {
		StringBuilder buf = new StringBuilder();
		attrs(cat, t, tm, tym, buf);
		return buf.toString();
	}

	protected String decapitalize(String identifier) {
		if ((identifier == null) || identifier.equals("")) {
			return identifier;
		}

		StringBuilder buf = new StringBuilder(identifier);
		buf.setCharAt(0, Character.toLowerCase(identifier.charAt(0)));
		return buf.toString();
	}

	private void attrs(Catalog cat, BaseTable t, TableMapper tm,
			TypeMapper tym, StringBuilder content) {
		List<Column> cols = getAttributeColumnList(cat, t, tym);

		for (Column c : cols) {
			AttributeInfo ai = tym.getAttributeInfo(c.getDataType());

			if (ai == null) {
				logger().warn(
						"no attribute-info for column : " + columnName(t, c));
				continue;
			}

			if (ai.getKeyType() == null) {
				logger().warn(
						"no key type for column : "
								+ c.getColumnName().getName());
				logger().warn(
						"column-data-type : " + c.getDataType().getDataType());
				logger().warn(
						"column-type-name : " + c.getDataType().getTypeName());
			}

			content.append(attr(c));
			content.append("(");
			PrimitiveType<?> pt = ai.getPrimitiveType();

			if (pt == null) {
				content.append("null");
			} else {
				content.append(pt.getClass().getCanonicalName());
				content.append(".TYPE");
			}

			content.append("),\n");
		}
	}

	private String refs(BaseTable t, TableMapper tm) {
		StringBuilder content = new StringBuilder();
		refs(t, content, tm);
		return content.toString();
	}

	private void refs(BaseTable t, StringBuilder content, TableMapper tm) {
		// List<String> elements = new ArrayList<String>();

		for (ForeignKey fk : t.foreignKeys().values()) {
			String r = formatReferenceConstant(fk, tm);

			if (r == null || r.equals("")) {
				continue;
			}
			content.append(r);
			content.append(",\n");
			// elements.add();
		}

		// content.append(enumMember(getReferenceType(), elements));
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
		} else {
			StringBuilder buf = new StringBuilder(n);
			buf.append("(");
			buf.append('"');
			buf.append(kn);
			buf.append('"');
			buf.append(")");

			buf.append(" {");
			buf.append("\n@Override\npublic ");
			buf.append(ref.getQualifiedName());
			buf.append(".Type type() {\n");
			buf.append("return ");
			buf.append(ref.getQualifiedName());
			buf.append(".");
			buf.append("Type");
			buf.append(".TYPE;\n");
			buf.append("}\n}");

			// buf.append(", ");
			// buf.append(ref.getQualifiedName());
			// buf.append(".TYPE");

			expr = buf.toString();
		}

		return expr;
	}

	protected String referenceName(ForeignKey fk) {
		String n = fk.getUnqualifiedName().getName();
		return referenceName(
				fk.getReferencing().getUnqualifiedName().getName(), n);
	}

	protected String referenceName(final String table,
			final String constraintName) {
		String t = table.toUpperCase();
		String n = constraintName.toUpperCase();
		String p = "^(FK(EY)?_)?(" + Pattern.quote(t)
				+ "_)?(.+?)(_ID)?(_FK(EY)?)?$";

		Pattern pattern = Pattern.compile(p);
		Matcher matcher = pattern.matcher(n);

		n = matcher.matches() ? matcher.group(4) : n;

		logger().debug("input {" + constraintName + "}");
		logger().debug("p {" + p + "}");

		return n;
	}

	// private String queries(BaseTable t) {
	// StringBuilder content = new StringBuilder();
	// queries(t, content);
	// return content.toString();
	// }

	private Map<Identifier, ForeignKey> foreignKeyColumnMap(Catalog cat,
			BaseTable t) {
		Comparator<Identifier> icmp = cat.getEnvironment().getIdentifierRules()
				.comparator();
		Map<Identifier, ForeignKey> cm = new TreeMap<Identifier, ForeignKey>(
				icmp);

		logger().debug("table: " + t.getQualifiedName());

		for (ForeignKey fk : t.foreignKeys().values()) {
			for (Column c : fk.getColumnMap().values()) {
				cm.put(c.getUnqualifiedName(), fk);
			}
		}

		return cm;
	}

	private String attr(Column c) {
		Identifier n = c.getColumnName();

		String attr = n.getName().toUpperCase();

		logger().debug("attr (" + attr + ") ordinary ? " + n.isOrdinary());

		if (!n.isOrdinary()) {
			attr = normalize(attr);
			attr = attr.replace('-', '_');
		}

		return attr;
	}

	// private void indent(int indentLevel, StringBuilder dest) {
	// String indent = "  ";
	//
	// for (int level = 0; level < indentLevel; level++) {
	// dest.append(indent);
	// }
	// }

	private File getSourceFile(File pd, String type) {
		return new File(pd, type + ".java");
	}

	// private File packageDir(File root, String pkg, Properties generated)
	// throws IOException {
	// File pd = packageDir(pkg);
	// pd = (pd == null) ? root : new File(root, pd.getPath());
	// return pd;
	// }

	private File packageDir(String pkg) {
		if (pkg == null) {
			return null;
		}

		String[] elems = pkg.split(Pattern.quote("."));
		StringBuilder path = new StringBuilder(elems[0]);

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

	private void a(StringBuilder dest, String s) {
		line(dest, s, 0);
	}

	private void line(StringBuilder dest, String s, int eols) {
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

	private String replacePackageAndImports(String src, JavaType type,
			Collection<String> importList) {
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

	// public void setSourceDir(Part part, File dir) {
	// if (part == null) {
	// throw new NullPointerException();
	// }
	//
	// getSourceDirMap().put(part, dir);
	// }

	private File getSourceDir(Part part) {
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

		char[] ca = n.toCharArray();

		StringBuilder buf = new StringBuilder();

		char f = ca[0];

		if (!Character.isJavaIdentifierStart(f)) {
			buf.append("_");
		}

		buf.append(f);

		for (int i = 1; i < ca.length; i++) {
			char c = ca[i];

			if (Character.isJavaIdentifierPart(c)) {
				buf.append(c);
			}
		}

		n = buf.toString();

		String[] tokens = n.split("_");

		buf.setLength(0);

		for (int i = 0; i < tokens.length; i++) {
			capitalize(tokens[i], buf);
		}

		return buf.toString();
	}

	private void capitalize(String t, StringBuilder dest) {
		dest.append(Character.toUpperCase(t.charAt(0)));

		if (t.length() > 1) {
			dest.append(t.substring(1).toLowerCase());
		}
	}

	private void decapitalize(String t, StringBuilder dest) {
		dest.append(Character.toLowerCase(t.charAt(0)));
		dest.append(t.substring(1));
	}

	private Identifier normalize(Identifier id) {
		if (id == null) {
			return null;
		}

		Environment te = this.targetEnvironment;

		if (te != null) {
			id = te.getIdentifierRules().toIdentifier(id.getName());
		}

		return id;
	}
	
	
	
	private String getAccessorNameSuffix(Column column, TypeMapper typeMapper) {
		AttributeInfo ai = typeMapper.getAttributeInfo(column.getDataType());
		String n = getKeyName(ai.getKeyType());
		return removeSuffix(n, "Key");		
	}

}
