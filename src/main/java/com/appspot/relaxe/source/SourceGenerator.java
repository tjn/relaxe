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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.env.IdentifierRules;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.SchemaName;
import com.appspot.relaxe.map.AttributeDescriptor;
import com.appspot.relaxe.map.JavaType;
import com.appspot.relaxe.map.TableMapper;
import com.appspot.relaxe.map.AttributeTypeMap;
import com.appspot.relaxe.map.TableMapper.Part;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeImpl;
import com.appspot.relaxe.meta.EmptyForeignKeyMap;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.ImmutableColumnMap;
import com.appspot.relaxe.meta.ImmutablePrimaryKey;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.SchemaElement;
import com.appspot.relaxe.meta.SchemaElementMap;
import com.appspot.relaxe.meta.Table;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.types.AbstractValueType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.io.IOHelper;


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
		 * Pattern which is replaced with the simple name of the entity implementation container class in
		 * template files.
		 */
		TABLE_IMPL_CLASS,
		
		/** 
		 * Pattern which is replaced with the simple name of the immutable entity implementation class in
		 * template files.
		 */
		IMMUTABLE_ENTITY_IMPL_CLASS,
		
		/** 
		 * Pattern which is replaced with the simple name of the immutable entity implementation class in
		 * template files.
		 */
		IMMUTABLE_ENTITY_IMPL_NO_ARG_CONSTRUCTOR_BODY,
		
		/** 
		 * Pattern which is replaced with the body of the copy constructor of the immutable entity implementation class in
		 * template files.
		 */
		IMMUTABLE_ENTITY_IMPL_COPY_CONSTRUCTOR_BODY,		
				 		
		/**
		 * Pattern which is replaced with the simple name of the mutable entity implementation class in
		 * template files.
		 */
		MUTABLE_ENTITY_IMPL_CLASS,

		/** 
		 * Pattern which is replaced with the reference assignment blocks
		 */
		REFERENCE_ASSIGNMENT_LIST,
		
		/**
		 * Pattern which is replaced with the simple name of the hook class in
		 * template files.
		 */
		TABLE_IMPL_BASE,

		TABLE_ENUM_INIT_TYPE,

		/**
		 * Pattern which is replaced with the toPrimaryKey method body of immutable entity  
		 */		
		IMMUTABLE_ENTITY_TO_PRIMARY_KEY_BODY,
		
		/**
		 * Pattern which is replaced with the toPrimaryKey method body of mutable entity  
		 */		
		MUTABLE_ENTITY_TO_PRIMARY_KEY_BODY,
		
		/**
		 * Pattern which is replaced with the static of method body of primary key entity  
		 */		
		PRIMARY_KEY_ENTITY_OF_METHOD_BODY,
		/**
		 * Pattern which is replaced with the method implementations for entity primary key type 
		 */
		PRIMARY_KEY_ENTITY_VARIABLES,
		
		/**
		 * Pattern which is replaced with the method implementations for entity primary key type 
		 */
		PRIMARY_KEY_ENTITY_CONSTRUCTOR_BODY,
		
		/**
		 * Pattern which is replaced with the method implementations for entity primary key type 
		 */
		PRIMARY_KEY_ENTITY_NO_ARG_CONSTRUCTOR_BODY,		
		
		/**
		 * Pattern which is replaced with the method implementations for entity primary key type 
		 */
		PRIMARY_KEY_ENTITY_METHODS,
		
		
		PRIMARY_KEY_ENTITY_ATTRIBUTE_SET,						
		INIT_COLUMN_ENUM_LIST,
//		/**
//		 * TODO: which accessors
//		 */
//		ACCESSOR_LIST,
		
		ENTITY_ATTRIBUTE_READ_ACCESSOR_SIGNATURE_LIST,
		ENTITY_ATTRIBUTE_READ_WRITE_ACCESSOR_SIGNATURE_LIST,
		
		ENTITY_ATTRIBUTE_READ_ACCESSOR_LIST,
		ENTITY_ATTRIBUTE_READ_WRITE_ACCESSOR_LIST,
		
		PRIMARY_KEY_ENTITY_ATTRIBUTE_ACCESSOR_LIST,

		/**
		 * static attribute key declarations in interface
		 */
		ATTRIBUTE_KEY_LIST, 
		REFERENCE_KEY_VARIABLE,

		REFERENCE_KEY_LIST,
		
		QUERY_ELEMENT_CONSTRUCTOR_LIST,

		QUERY_ELEMENT_VARIABLE_LIST,
		QUERY_ELEMENT_GETTER_BODY, 
		QUERY_ELEMENT_SETTER_BODY, 
		QUERY_ELEMENT_ASSIGNMENT_LIST, 
		QUERY_ELEMENT_NEW_BUILDER_BODY, 
		QUERY_ELEMENT_DEFAULT_CONSTRUCTOR_BODY, 
		QUERY_ELEMENT_BUILDER_DEFAULT_CONSTRUCTOR_BODY, 
		PER_TYPE_QUERY_ELEMENT_GETTER_LIST,

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
		HAS_REFERENCE_READ_LIST,
		
		/*
		 * Implements ... list for table interface.
		 */
		HAS_REFERENCE_WRITE_LIST,		

		/*
		 * Implements ... list for table interface.
		 */
		READABLE_ATTRIBUTE_CONTAINER_INTERFACE_LIST,
		
		/*
		 * Implements ... list for table interface.
		 */
		WRITABLE_ATTRIBUTE_CONTAINER_INTERFACE_LIST,
		

		/*
		 * Implements ... list for table interface.
		 */
		ATTRIBUTE_KEY_CONTAINER_INTERFACE_LIST,

		/*
		 * 
		 */
		ATTRIBUTE_CONTAINER_IMPLEMENTATION_READ,
				
		
		/*
		 * 
		 */
		ATTRIBUTE_CONTAINER_IMPLEMENTATION_READ_WRITE,
		
		REFERENCE_CONTAINER_READ,
		REFERENCE_CONTAINER_READ_WRITE,
		REFERENCE_CONTAINER_IMPLEMENTATION_READ,
		REFERENCE_CONTAINER_IMPLEMENTATION_READ_WRITE,
		PRIMARY_KEY_ENTITY_REFERENCE_CONTAINER_IMPLEMENTATION,
		/*
		 * Implements ... list for table interface.
		 */
		ATTRIBUTE_KEY_CONTAINER_IMPLEMENTATION,

		ATTRIBUTES_METHOD_STATEMENT_LIST,

		REFERENCE_MAP_LIST_READ,
		
		REFERENCE_MAP_LIST_READ_WRITE,
		
		
		PRIMARY_KEY_ENTITY_REFERENCE_MAP_LIST,
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

		BASE_TABLE_COLUMN_VARIABLE_LIST, 
		POPULATE_COLUMN_MAP_BLOCK, 
		CREATE_GET_BASE_TABLE_BODY,
		GET_ENVIRONMENT_BODY,
		CREATE_PRIMARY_KEY_BODY, 
		CREATE_FOREIGN_KEYS_BODY, 
		CREATE_FOREIGN_KEY_MAP_BODY,

		/**
		 * Generated imports
		 */
		IMPORTS, NEW_ENVIRONMENT_EXPR, SCHEMA_ENUM_LIST, BASE_TABLE_ENUM_LIST, VIEW_ENUM_LIST, COLUMN_ENUM_LIST, SCHEMA_TYPE_NAME, 
		FOREIGN_KEY_ENUM_LIST, PRIMARY_KEY_ENUM_LIST, META_MAP_POPULATION, TABLE_COLUMN_ENUM_LIST,

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
	private Environment targetEnvironment;

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
		this.defaultSourceDir = defaultSourceDir;
		this.schemaFilter = (sf == null) ? SchemaFilter.ALL_SCHEMAS : sf;		
	}

	private static Logger logger() {
		return SourceGenerator.logger;
	}

	public int check(Catalog cat, AttributeTypeMap tm, StringBuilder buf) {

		List<String> errorList = new ArrayList<String>();

		int total = 0;

		for (Schema s : cat.schemas().values()) {
			if (schemaFilter.accept(s)) {
				for (BaseTable table : s.baseTables().values()) {
					ColumnMap cm = table.getColumnMap();

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
	private String formatColumnMappingError(BaseTable table, Column c, AttributeTypeMap tm, 
			List<String> errorList) {

		StringBuilder buf = new StringBuilder();
		SchemaElementName n = table.getName();
		
		Map<String, String> data = new LinkedHashMap<String, String>();

		data.put("type-mapper", tm.getClass().getCanonicalName());
		data.put("schema", n.getQualifier().getSchemaName().getContent());
		data.put("table", n.getUnqualifiedName().getContent());
		data.put("column", c.getColumnName().getContent());

		DataType type = c.getDataType();
		
		data.put("jdbc-data-type", toString(type.getDataType()));
		data.put("jdbc-type-name", AbstractValueType.getSQLTypeName(type.getDataType()));
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
			AttributeTypeMap mapper, List<String> errorList) {

		AttributeDescriptor info = mapper.getAttributeDescriptor(column.getDataType());

		if (info == null) {
			errorList.add("no attribute info");
			return 1;
		}

		int count = 0;

		if (info.getValueType() == null) {
			errorList.add("no value type");
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

		if (info.getAttributeType() == null) {
			errorList.add("no key type");
			count++;
		}

		if (info.getReadableContainerType() == null) {
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
	public Properties run(Catalog cat, TableMapper tm, AttributeTypeMap typeMapper,
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

	
	enum NameQualification {
		SCHEMA, TABLE, COLUMN
	}

	private String columnConstantName(Column c, TableMapper tm) {
		return columnEnumeratedName(null, c,
				EnumSet.of(NameQualification.COLUMN), tm);
	}

	private void generateColumnListElements(Table t, StringBuilder buf,
			EnumSet<NameQualification> nq, TableMapper tm) {
		Environment env = t.getEnvironment();

		String et = env.getClass().getName();

		for (Column c : t.getColumnMap().values()) {
			String cn = columnEnumeratedName(t, c, nq, tm);
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

	private String tableEnumeratedName(TableMapper tm, Table t) {
		return enumeratedName(tm, t);
	}

	private String enumeratedName(TableMapper tm, SchemaElement e) {
		StringBuilder buf = new StringBuilder();
		// buf.append(e.getSchema().getUnqualifiedName().getName());

		buf.append(e.getName().getQualifier().getSchemaName().getContent());
		buf.append("_");
		buf.append(e.getUnqualifiedName().getContent());

		String uc = buf.toString().toUpperCase();
		return tm.toJavaIdentifier(uc);
	}

	private String columnEnumeratedName(Table t, Column c,
			EnumSet<NameQualification> nq, TableMapper tm) {
		StringBuilder buf = new StringBuilder();

		if (nq.contains(NameQualification.SCHEMA)) {
			// t.getSchema().getUnqualifiedName().getName();
			buf.append(t.getName().getQualifier().getSchemaName().getContent());
			buf.append("_");
		}

		if (nq.contains(NameQualification.TABLE)) {
			buf.append(t.getUnqualifiedName().getContent());
			buf.append("_");
		}

		if (nq.contains(NameQualification.COLUMN)) {
			buf.append(c.getUnqualifiedName().getContent());
		}

		String n = buf.toString().toUpperCase();

		n = normalize(n);
		n = tm.toJavaIdentifier(n);

		return n;
	}

	private String normalize(String n) {
		return n.replace(' ', '_');
	}

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
			AttributeTypeMap tym, Collection<JavaType> ccil,
			Map<JavaType, CharSequence> factories, Properties generated,
			Map<File, String> gm) throws IOException {

		List<TypeInfo> types = new ArrayList<TypeInfo>();

		for (BaseTable t : s.baseTables().values()) {
			final JavaType intf = tm.entityType(t, Part.INTERFACE);
			final JavaType at = tm.entityType(t, Part.ABSTRACT);
			final JavaType hp = tm.entityType(t, Part.HOOK);
			final JavaType ii = tm.entityType(t, Part.IMPLEMENTATION);			
//			final JavaType te = tm.entityType(t, Part.LITERAL_TABLE_ENUM);
			final JavaType ref = tm.entityType(t, Part.HAS);
			final JavaType mdi = tm.entityType(t, Part.METADATA_IMPLEMENTATION);

			if (intf == null) {
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

//				if (at != null) {
//					CharSequence source = generateAbstract(t, at, tm);
//					File root = getSourceDir(s, Part.ABSTRACT);
//					write(root, at, source, generated, gm);
//				}

//				if (te != null) {
//					CharSequence source = generateTableEnum(t, te,
//							Tag.LITERAL_TABLE_ENUM, tm);
//					File root = getSourceDir(s, Part.LITERAL_TABLE_ENUM);
//					write(root, te, source, generated, gm);
//				}

//				if (hp != null) {
//					CharSequence source = generateHook(t, hp, tm);
//
//					if (source != null) {
//						File root = getSourceDir(s, Part.HOOK);
//						File sourceFile = getSourceFile(root, hp);
//
//						if (!sourceFile.exists()) {
//							write(root, hp, source, generated, gm);
//						}
//					}
//				}

				{
					CharSequence source = generateImplementation(cat, t, ii, tm, tym);
					File root = getSourceDir(s, Part.IMPLEMENTATION);
					write(root, ii, source, generated, gm);
				}
				
				{
					CharSequence source = generateMetaDataImplementation(cat, t, mdi, tm, tym);
					File root = getSourceDir(s, Part.METADATA_IMPLEMENTATION);
					write(root, mdi, source, generated, gm);
				}

				TypeInfo info = new TypeInfo();

				info.put(Part.INTERFACE, intf);
				info.put(Part.ABSTRACT, at);
				info.put(Part.HOOK, hp);
				info.put(Part.IMPLEMENTATION, ii);				
				info.put(Part.METADATA_IMPLEMENTATION, mdi);

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
		src = replacePackageAndImports(src, intf);
		src = replaceAll(src, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());
		return src;
	}

	private File getSourceDir(Schema s, Part part) {
		return getSourceDir(part);
	}

//	private File getSourceFile(File root, JavaType type) throws IOException {
//		File pd = packageDir(type.getPackageName());
//		pd = (pd == null) ? root : new File(root, pd.getPath());
//		return getSourceFile(pd, type.getUnqualifiedName());
//	}

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
			JavaType mt, TableMapper tm, AttributeTypeMap tym) throws IOException {

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
			String code = entityAttributeAccessorList(cat, t, tm, tym, false, false, false);
			src = replaceAllWithComment(src, Tag.ENTITY_ATTRIBUTE_READ_ACCESSOR_SIGNATURE_LIST, code);
		}		
		
		{
			String code = entityAttributeAccessorList(cat, t, tm, tym, false, false, true);
			src = replaceAllWithComment(src, Tag.ENTITY_ATTRIBUTE_READ_WRITE_ACCESSOR_SIGNATURE_LIST, code);
		}				
		
		

		{
			String code = contentAccessors(cat, t, tm, tym, false);
			src = replaceAll(src, "{{abstract-accessor-list}}", code);
		}
		
		{
			String code = queryElementConstructorList(cat, t, tm, 5);
			src = replaceAllWithComment(src, Tag.QUERY_ELEMENT_CONSTRUCTOR_LIST, code);
		}		

		{
			String code = queryElementVariableList(cat, t, tm, tym);
			src = replaceAllWithComment(src, Tag.QUERY_ELEMENT_VARIABLE_LIST, code);
		}

		{
			String code = queryElementGetterBody(cat, t, tm, tym);
			src = replaceAllWithComment(src, Tag.QUERY_ELEMENT_GETTER_BODY,
					code);
		}

		{
			String code = queryElementSetterBody(cat, t, tm, tym);
			src = replaceAllWithComment(src, Tag.QUERY_ELEMENT_SETTER_BODY,
					code);
		}

		{
			String code = queryElementAssignmentList(cat, t, tm, tym);
			src = replaceAllWithComment(src, Tag.QUERY_ELEMENT_ASSIGNMENT_LIST, code);
		}

		{
			String code = queryElementNewBuilderBody(cat, t, tm, tym);
			src = replaceAllWithComment(src,
					Tag.QUERY_ELEMENT_NEW_BUILDER_BODY, code);
		}

		{
			String code = queryElementDefaultConstructorBody(cat, t, tm, tym);
			src = replaceAllWithComment(src,
					Tag.QUERY_ELEMENT_DEFAULT_CONSTRUCTOR_BODY, code);
			// same content can applied for Builder, too
			src = replaceAllWithComment(src,
					Tag.QUERY_ELEMENT_BUILDER_DEFAULT_CONSTRUCTOR_BODY, code);
		}

		{
			String code = perTypeQueryElementGetterList(cat, t, tm, tym);
			src = replaceAllWithComment(src, Tag.PER_TYPE_QUERY_ELEMENT_GETTER_LIST, code);
		}

		{
			String code = valueAccessorList(cat, t, tm, tym, false);
			src = replaceAll(src, Tag.VALUE_ACCESSOR_LIST, code);
		}

		{
			String code = referenceList(t, tm, "Read");
			src = replaceAll(src, Tag.HAS_REFERENCE_READ_LIST, code);
		}
		
		{
			String code = referenceList(t, tm, "Write");
			src = replaceAll(src, Tag.HAS_REFERENCE_WRITE_LIST, code);
		}
		
		{
			String code = referenceContainerImplementation(cat, t, tm, true, false, false);
			src = replaceAll(src, Tag.REFERENCE_CONTAINER_READ, code);
		}
		
		{
			String code = referenceContainerImplementation(cat, t, tm, true, false, true);
			src = replaceAll(src, Tag.REFERENCE_CONTAINER_READ_WRITE, code);
		}


		{
			String code = attributeContainerInterfaceList(cat, t, tm, tym, true);
			logger().debug("generateInterface: attributeContainerInterfaceList: {}", code);
			logger().debug("generateInterface: tag: ", Tag.READABLE_ATTRIBUTE_CONTAINER_INTERFACE_LIST.getTag());
			src = replaceAll(src, Tag.READABLE_ATTRIBUTE_CONTAINER_INTERFACE_LIST, code);
		}
		
		{
			String code = attributeContainerInterfaceList(cat, t, tm, tym, false);
			logger().debug("attributeContainerInterfaceList: {}", code);
			logger().debug("generateInterface: tag: ", Tag.READABLE_ATTRIBUTE_CONTAINER_INTERFACE_LIST.getTag());
			src = replaceAll(src, Tag.WRITABLE_ATTRIBUTE_CONTAINER_INTERFACE_LIST, code);
		}		

		{
			String code = attributeKeyContainerInterfaceList(cat, t, tm, tym);
			src = replaceAll(src, Tag.ATTRIBUTE_KEY_CONTAINER_INTERFACE_LIST,
					code);
		}

		return src;
	}

	private String queryElementVariableList(Catalog cat, BaseTable t, TableMapper tm, AttributeTypeMap tym) {
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
	
	private String queryElementConstructorList(Catalog cat, BaseTable t, TableMapper tam, int count) {
		StringBuilder buf = new StringBuilder();
		
//		Sample: 
//		public QueryElement(
//				ValueAttribute<Attribute, Language, ?, ?, ?, ?> a1, 
//				ValueAttribute<Attribute, Language, ?, ?, ?, ?> a2) {
//			this.attributes = new java.util.TreeSet<Attribute>();
//			this.attributes.add(a1.name());
//			this.attributes.add(a2.name());
//		}
		
		String intf = tam.entityType(t, Part.INTERFACE).getUnqualifiedName();
		String attr = getAttributeType();
								
		for (int i = 0; i < count; i++) {
			line(buf, "public QueryElement(");
			
			int ac = (i + 1);
			
			for (int p = 0; p < ac; p++) {
				String delim = (p < (ac - 1)) ? "," : "";
				line(buf, Attribute.class.getCanonicalName(), 
						"<", attr, ", ", intf, ", ?, ?, ?, ?, ?> a", Integer.toString(p + 1), delim);
			}
						
			line(buf, ") {");			
			line(buf, "this.attributes = new java.util.TreeSet<", getAttributeType(), ">();");
			
			for (int p = 0; p < ac; p++) {
				line(buf, "this.attributes.add(a", Integer.toString(p + 1), ".name());");
			}			
			
			line(buf, "}");			
		}

		return buf.toString();
	}	

	protected String queryElementVariableName(ForeignKey fk) {
		return variable(fk) + "QueryElement";
	}

	private String queryElementGetterBody(Catalog cat, BaseTable t, TableMapper tm, AttributeTypeMap tym) {
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
				// SAMPLE: private EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?>
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

	private String queryElementSetterBody(Catalog cat, BaseTable t, TableMapper tm, AttributeTypeMap tym) {
		Collection<ForeignKey> fks = t.foreignKeys().values();

		StringBuilder buf = new StringBuilder();

		line(buf, "if (key == null) {");
		line(buf, "  throw new NullPointerException(\"key\");");
		line(buf, "}", 2);

		if (!fks.isEmpty()) {
			line(buf, "switch(key.name()) {");

			for (ForeignKey fk : fks) {
				// SAMPLE: private EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?>
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
			TableMapper tm, AttributeTypeMap tym) {
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
			TableMapper tm, AttributeTypeMap tym) {
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
			TableMapper tm, AttributeTypeMap tym) {

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
				AttributeDescriptor ai = tym.getAttributeDescriptor(ac.getDataType());

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

	private String perTypeQueryElementGetterList(Catalog cat, BaseTable t, TableMapper tm, AttributeTypeMap tym) {

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

		JavaType type = tm.entityType(ref, Part.INTERFACE);
		String qn = type.getQualifiedName();
		
		line(buf, "public ", qn, ".QueryElement getQueryElement(", qn, ".Key<");
		buf.append(referenceKeyTypeArgs(tm, t));
		line(buf, "> key) {");
		
		
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

	private String referenceList(BaseTable t, TableMapper tm, String suffix) {
		StringBuilder buf = new StringBuilder();

		Set<BaseTable> rs = referencedTables(t);

		String args = referenceKeyTypeArgs(tm, t);

		for (BaseTable rt : rs) {
			buf.append(", ");
			JavaType e = tm.entityType(rt, Part.HAS);
			buf.append(e.getQualifiedName());
			buf.append(".");
			buf.append(suffix);
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
			TableMapper tm, AttributeTypeMap tym, boolean read) {
		StringBuilder buf = new StringBuilder();

		Set<Class<?>> implemented = new LinkedHashSet<Class<?>>();

		List<Column> cl = getAttributeColumnList(cat, t, tym);

		for (Column c : cl) {
			AttributeDescriptor ai = tym.getAttributeDescriptor(c.getDataType());

			if (ai != null) {
				{
					Class<?> ct = read ? 
							ai.getReadableContainerType() : 
							ai.getWritableContainerType(); 
	
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
			buf.append(",");
			buf.append(et.getUnqualifiedName());
			buf.append(".Mutable");
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
			TableMapper tm, AttributeTypeMap tym) {
		StringBuilder buf = new StringBuilder();

		Set<Class<?>> implemented = new LinkedHashSet<Class<?>>();

		List<Column> cl = getAttributeColumnList(cat, t, tym);

		for (Column c : cl) {
			AttributeDescriptor ai = tym.getAttributeDescriptor(c.getDataType());

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
			buf.append(",");
			buf.append(et.getUnqualifiedName());
			buf.append(".Mutable");
			buf.append(">\n");
		}

		String code = buf.toString();
		return code;
	}

	/**
	 * @param t
	 * @param tm
	 * @return
	 */
	private String attributeContainerImplementation(Catalog cat, BaseTable t,
			TableMapper tam, AttributeTypeMap tym, List<Column> columnList, boolean pkcols, boolean rw) {
		StringBuilder buf = new StringBuilder();

		Map<Class<?>, List<Column>> attributeTypeMap = new HashMap<Class<?>, List<Column>>();
		
		for (Column c : columnList) {
			AttributeDescriptor ai = tym.getAttributeDescriptor(c.getDataType());

			if (ai != null) {
				Class<?> kt = ai.getAttributeType();

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
			formatAttributeHolderAccessors(cat, kt, t, attributes, tam, tym, buf, pkcols, rw);
		}

		String code = buf.toString();
		return code;
	}

	private void formatAttributeHolderAccessors(Catalog cat, Class<?> kt,
			BaseTable table, List<Column> columns, TableMapper tam,
			AttributeTypeMap tym, StringBuilder buf, boolean pkcols, boolean rw) {

		// Sample output: "private IntegerHolder a = null;"
		// Sample output: "private IntegerHolder b = null;, etc..."

		buf.append("\n// formatAttributeHolderAccessors - enter\n");

		List<Column> cols = columns;

		for (Column ac : cols) {

			AttributeDescriptor info = tym.getAttributeDescriptor(ac.getDataType());

			if (info == null) {
				continue;
			}

			Class<?> ht = info.getHolderType();

			if (ht == null) {
				logger().error("no holder type: " + columnName(table, ac));
				continue;
			}
			
			if (pkcols && (!table.isPrimaryKeyColumn(ac))) {
				continue;
			}			

			String n = valueVariableName(table, ac);

			buf.append("private ");
			
			if (pkcols) {
				buf.append("final ");	
			}
			
			buf.append(ht.getCanonicalName());
			buf.append(" ");
			buf.append(n);
			
			if (!pkcols) {
				buf.append(" = null");	
			}
			buf.append(";");
			buf.append("\n");
		}

		// tym.getAttributeInfo(table, attributes.get(index));
		JavaType itf = tam.entityType(table, Part.INTERFACE);
		
		String q = itf.getQualifiedName();
		

		// all the attributes are expected to have same type, infer type by the
		// first:
		Column column = columns.get(0);

		AttributeDescriptor ai = tym.getAttributeDescriptor(column.getDataType());
		String n = getKeyName(ai.getAttributeType());
		n = removeSuffix(n, "Attribute");

		Class<?> ht = ai.getHolderType();
		
		if (rw) {			
			final String methodName = "set" + n;
			final String newValueVariable = "newValue";

			buf.append("public void ");
			buf.append(methodName);
			buf.append("(");
			buf.append(kt.getCanonicalName());
			buf.append("<");
			buf.append(q).append(".").append(getAttributeType());
			buf.append(", ");
			buf.append(q);
			buf.append(", ");
			buf.append(q).append(".Mutable");
			buf.append(">");
			buf.append(" key, ");
			buf.append(ht.getCanonicalName());
			buf.append(" ");
			buf.append(newValueVariable);
			buf.append(") {\n\n");

			buf.append("\n");
			appendThrowNpe(buf, q, methodName, "key");
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
			buf.append(q).append(".").append(getAttributeType());
			buf.append(", ");
			buf.append(q);
			buf.append(", ");
			buf.append(q).append(".Mutable");
			buf.append(">");
			buf.append(" key");
			buf.append(") {\n\n");
			appendThrowNpe(buf, itf.getQualifiedName(), methodName, "key");
			buf.append("\n\n");

			buf.append("switch (key.name()) {\n");

			for (Column c : columns) {
				if (pkcols && (!table.isPrimaryKeyColumn(c))) {
					continue;
				}				
				
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
	
	
	/**
	 * @param t
	 * @param tm
	 * @return
	 */
	private String referenceContainerImplementation(Catalog cat, BaseTable t, TableMapper tam, boolean intf, boolean pk, boolean rw) {
		StringBuilder buf = new StringBuilder();
									
//		Collection<ForeignKey> keys = pk ? primaryKeyForeignKeys(t).values() : t.foreignKeys().values();  
		Collection<ForeignKey> keys = t.foreignKeys().values();
								
		for (ForeignKey fk : keys) {				
			String s = referenceHolderAccessor(cat, fk, tam, intf, pk, rw);
			buf.append(s);
		}
	
		String code = buf.toString();
		
		return code;
	}	

	private String referenceHolderAccessor(Catalog cat, ForeignKey fk, TableMapper tam, boolean intf, boolean pk, boolean rw) {
		
		StringBuilder buf = new StringBuilder();
		BaseTable b = fk.getReferenced();
		
		JavaType t = tam.entityType(b, Part.INTERFACE);				
		final String var = variable(fk);
		
		String ht = t.getQualifiedName() + ".Holder";
		
		final String getter = getter(fk);
		final String setter = setter(fk);
		
		if (intf) {
			
			line(buf, "public ", ht, " ", getter, "();");
			
			if (rw) {				
				line(buf, "public void ", setter, "(", ht, " ", var, ");");				
				line(buf, "public void ", setter, "(", t.getQualifiedName(), " ", var, ");");
			}
		}
		else {			
			boolean overlapping = overlapsWithPrimaryKey(fk);
						
			if (pk && (!overlapping)) {
				line(buf, "public ", ht, " ", getter, "() {");
				line(buf, "return null;");
				line(buf, "}");				
			}
			else {
				line(buf, "private ", pk ? "final " : "", ht, " ", var, ";");		
				
				line(buf, "public ", ht, " ", getter, "() {");
				line(buf, "return ", var, ";");
				line(buf, "}");		
				
				if (rw) {
					line(buf, "public void ", setter, "(", ht, " ", var, ") {");
					line(buf, "this.", var, " = ", var + ";");
					line(buf, "}");
											
					line(buf, "public void ", setter, "(", t.getQualifiedName(), " ", var, ") {");
					line(buf, "this.", var, " = ", ht, ".valueOf(", var, "); ");
					line(buf, "}");
				}
			}
		}
		
		return buf.toString();
	}	
	
	
	private boolean overlapsWithPrimaryKey(ForeignKey fk) {
		BaseTable t = fk.getReferencing();
		PrimaryKey pk = t.getPrimaryKey();
		
		if (pk == null) {
			return false;
		}
		
		ColumnMap cm = pk.getColumnMap();
		
		for (int i = 0; i < cm.size(); i++) {
			Column c = cm.get(i);
			
			if (fk.getColumnMap().contains(c.getColumnName())) {
				return true;
			}
		}
		
		return false;
	}

	private String entityHolderExpression(String refvar, ForeignKey fk, TableMapper tam) {
		StringBuilder buf = new StringBuilder();
		
		if (refvar != null) {
			buf.append(refvar);
			buf.append(".");
		}
		
		JavaType st = tam.entityType(fk.getReferencing(), Part.INTERFACE);
		JavaType rt = tam.entityType(fk.getReferenced(), Part.INTERFACE);		
		
				
		buf.append("get");
		buf.append(rt.getUnqualifiedName());
		buf.append("(");	
		
		buf.append(st.getQualifiedName());
		buf.append(".");
		buf.append(referenceName(fk));		
		buf.append(")");
		
		return buf.toString();
	}

	private String variable(ForeignKey fk) {
		return variableName(referenceName(fk));
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
			BaseTable t, TableMapper tam, AttributeTypeMap tym) throws IOException {
		StringBuilder buf = new StringBuilder();

		buf.append("\n// attributeKeyContainerImplementation() - enter\n");

		Set<Class<?>> attributeKeyTypes = new HashSet<Class<?>>();

		List<Column> cl = getAttributeColumnList(cat, t, tym);

		for (Column c : cl) {
			AttributeDescriptor ai = tym.getAttributeDescriptor(c.getDataType());

			if (ai != null) {
				Class<?> kt = ai.getAttributeType();

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

		buf.append(n.getQualifier().getSchemaName().getContent());
		buf.append(".");
		buf.append(n.getUnqualifiedName().getContent());
		buf.append(".");
		buf.append(c.getColumnName().getContent());

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
			JavaType factoryType, TableMapper tm, AttributeTypeMap typeMapper,
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
			JavaType impl, TableMapper tam, AttributeTypeMap tym) throws IOException {

		String src = getTemplateFor(Part.IMPLEMENTATION);

		JavaType intf = tam.entityType(t, Part.INTERFACE);
				
		
		String lt = tableEnumeratedName(tam, t);

		List<String> il = new ArrayList<String>();
		addImport(impl, intf, il);
				
		src = replacePackageAndImports(src, impl, il);
		
//		{
//			src = t.foreignKeys().isEmpty() ? replaceAllWithComment(src,
//					Tag.FOREIGN_KEY_IMPLEMENTATION, "") : readAndReplace(src,
//					Tag.FOREIGN_KEY_IMPLEMENTATION, false);
//		}
//		
//
//		tag = Tag.BASE_TABLE_COLUMN_VARIABLE_LIST;
//		src = replaceAll(src, tag,
//				generateBaseTableColumnVariableList(t, tag, tam));
//
//		src = replaceAll(src, Tag.POPULATE_COLUMN_MAP_BLOCK,
//				generatePopulateColumnMapBlock(t, tam));
//		src = replaceAll(src, Tag.CREATE_GET_BASE_TABLE_BODY,
//				generateGetBaseTableBody(t, tam));
//		src = replaceAll(src, Tag.GET_ENVIRONMENT_BODY,
//				generateGetEnvironmentBody(t, tam));		
//		src = replaceAll(src, Tag.CREATE_PRIMARY_KEY_BODY,
//				generateCreatePrimaryKeyBody(t, tam));
//		src = replaceAll(src, Tag.CREATE_FOREIGN_KEY_MAP_BODY,
//				generateCreateForeignKeyMapBody(t, tam));
//
		
		boolean qualify = hasAmbiguousSimpleNamesForReferenceKeys(t, tam);

//		{
//			String code = contentAccessors(cat, t, tam, tym, true);
//			src = replaceAll(src, Tag.ACCESSOR_LIST, code);
//		}

//		{
//			String code = referenceKeyClassList(t, tam, qualify);
//			logger().debug(
//					"generateImplementation: referenceKeyClassList=" + code);
//			src = replaceAll(src, Tag.REFERENCE_KEY_CLASS_LIST, code);
//		}

//		{
//			String code = attributeKeyMapList(cat, t, tam, tym);
//			logger().debug("generateImplementation: code=" + code);
//			src = replaceAll(src, Tag.ATTRIBUTE_KEY_MAP_LIST, code);
//		}
		
		{
			String code = entityConstructorBody(cat, t, tam, tym, false);
			src = replaceAllWithComment(src, Tag.IMMUTABLE_ENTITY_IMPL_NO_ARG_CONSTRUCTOR_BODY, code);
		}								
		
		{
			String code = entityConstructorBody(cat, t, tam, tym, true);
			src = replaceAllWithComment(src, Tag.IMMUTABLE_ENTITY_IMPL_COPY_CONSTRUCTOR_BODY, code);
		}						
		{
			String code = referenceAssignmentList(cat, t, tam, tym, qualify);
			src = replaceAllWithComment(src, Tag.REFERENCE_ASSIGNMENT_LIST, code);
		}						

		
		{
			String code = entityAttributeAccessorList(cat, t, tam, tym, true, false, false);
			src = replaceAllWithComment(src, Tag.ENTITY_ATTRIBUTE_READ_ACCESSOR_LIST, code);
		}
		
		{
			String code = entityAttributeAccessorList(cat, t, tam, tym, true, false, true);
			src = replaceAllWithComment(src, Tag.ENTITY_ATTRIBUTE_READ_WRITE_ACCESSOR_LIST, code);
		}						

		{
			String code = referenceKeyMapList(t, tam, qualify);
			logger().debug("generateImplementation: code=" + code);
			src = replaceAll(src, Tag.REFERENCE_KEY_MAP_LIST, code);
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
			String code = referenceMapList(t, tam, qualify, false, false);
			logger().debug("generateImplementation: code=" + code);
			src = replaceAll(src, Tag.REFERENCE_MAP_LIST_READ, code);
		}
		
		{
			String code = referenceMapList(t, tam, qualify, false, true);
			logger().debug("generateImplementation: code=" + code);
			src = replaceAll(src, Tag.REFERENCE_MAP_LIST_READ_WRITE, code);
		}		

		{
			String code = metaDataInitialization(cat, t, tam, tym, qualify);
			src = replaceAll(src, Tag.META_DATA_INITIALIZATION, code);
		}

		List<Column> cl = getAttributeColumnList(cat, t, tym);
		
		{						
			String code = attributeContainerImplementation(cat, t, tam, tym, cl, false, false);
			src = replaceAll(src, Tag.ATTRIBUTE_CONTAINER_IMPLEMENTATION_READ, code);
		}
		
		{
			String code = attributeContainerImplementation(cat, t, tam, tym, cl, false, true);
			src = replaceAll(src, Tag.ATTRIBUTE_CONTAINER_IMPLEMENTATION_READ_WRITE, code);
		}
		
		{
			String code = referenceContainerImplementation(cat, t, tam, false, false, false);
			src = replaceAll(src, Tag.REFERENCE_CONTAINER_IMPLEMENTATION_READ, code);
		}		
		
		{						
			String code = referenceContainerImplementation(cat, t, tam, false, true, false);
			src = replaceAll(src, Tag.PRIMARY_KEY_ENTITY_REFERENCE_CONTAINER_IMPLEMENTATION, code);
		}		
		
		{						
			String code = referenceContainerImplementation(cat, t, tam, false, false, true);
			src = replaceAll(src, Tag.REFERENCE_CONTAINER_IMPLEMENTATION_READ_WRITE, code);
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


//		{
//			String code = immutableEntityToPrimaryKeyBody(cat, t, tam, tym);
//			src = replaceAll(src, Tag.IMMUTABLE_ENTITY_TO_PRIMARY_KEY_BODY, code);
//		}
//		
//		{
//			String code = mutableEntityToPrimaryKeyBody(cat, t, tam, tym);
//			src = replaceAll(src, Tag.MUTABLE_ENTITY_TO_PRIMARY_KEY_BODY, code);
//		}

		{
			String code = primaryKeyEntityOfMethodBody(cat, t, tam, tym);
			src = replaceAll(src, Tag.PRIMARY_KEY_ENTITY_OF_METHOD_BODY, code);
		}
		
		{
			String code = primaryKeyEntityConstructorBody(cat, t, tam, tym, true);
			src = replaceAll(src, Tag.PRIMARY_KEY_ENTITY_NO_ARG_CONSTRUCTOR_BODY, code);
		}		

		{
			String code = primaryKeyEntityConstructorBody(cat, t, tam, tym, false);
			src = replaceAll(src, Tag.PRIMARY_KEY_ENTITY_CONSTRUCTOR_BODY, code);
		}

		{
			String code = primaryKeyEntityAttributeSet(cat, t, tam, tym);
			src = replaceAll(src, Tag.PRIMARY_KEY_ENTITY_ATTRIBUTE_SET, code);
		}
		
		{
			String code = entityAttributeAccessorList(cat, t, tam, tym, true, true, false);
			src = replaceAllWithComment(src, Tag.PRIMARY_KEY_ENTITY_ATTRIBUTE_ACCESSOR_LIST, code);
		}

	
//		{	
//			String code = attributeContainerImplementation(cat, t, tam, tym, cl, true, false);
//			src = replaceAll(src, Tag.PRIMARY_KEY_ENTITY_ATTRIBUTE_CONTAINER_IMPLEMENTATION, code);
//		}
		

		{
			String code = primaryKeyEntityVariables(cat, t, tam, tym);
			src = replaceAll(src, Tag.PRIMARY_KEY_ENTITY_VARIABLES, code);
		}

		
		{
			String code = referenceMapList(t, tam, qualify, true, false);
			logger().debug("generateImplementation: code=" + code);
			src = replaceAll(src, Tag.PRIMARY_KEY_ENTITY_REFERENCE_MAP_LIST, code);
		}		

		src = replaceAll(src, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());
		src = replaceAll(src, Tag.TABLE_IMPL_CLASS, impl.getUnqualifiedName());		
		src = replaceAll(src, Tag.IMMUTABLE_ENTITY_IMPL_CLASS, impl.getUnqualifiedName());
		src = replaceAll(src, Tag.MUTABLE_ENTITY_IMPL_CLASS, mutableImplementationSimpleName(intf));
		

		src = replaceAll(src, Tag.LITERAL_CATALOG_NAME, tam
				.literalContextType().getQualifiedName());

		Environment te = getTargetEnvironment(cat.getEnvironment());
		src = replaceAll(src, Tag.ENVIRONMENT_EXPRESSION,
				generateEnvironmentExpression(te));

		src = replaceAll(src, Tag.LITERAL_TABLE_ENUM, lt);

		return src;
	}	
	
	private String primaryKeyEntityOfMethodBody(Catalog cat, BaseTable t, TableMapper tam, AttributeTypeMap tym) {
		
		Comparator<Identifier> cmp = cat.getEnvironment().getIdentifierRules().comparator();
		Map<Identifier, List<ForeignKey>> fklm = new TreeMap<>(cmp);
					
		
		List<Column> cl = groupPrimaryKeyColumns(t, fklm);
		
		StringBuilder buf = new StringBuilder();
		
		int ordinal = 0;
		
		JavaType inft = tam.entityType(t, Part.INTERFACE);
		
		for (Column c : cl) {
			String hvar = "h" + ordinal;
			
			String init = holderVariableInitialization(hvar, "src", inft, tym, c);
			line(buf, init);
								
			line(buf, "if (", hvar, " == null || ", hvar, ".isNull()) {");
			line(buf, "return null;");
			line(buf, "}");
			
			ordinal++;
		}
		
		// TODO: reference check
		
//		Film.Holder film = src.getFilm(FilmActor.FILM);
//		
//		if (film == null || film.isNull()) {
//			return null;				
//		}		
		
		Collection<ForeignKey> pkfks = primaryKeyForeignKeys(t).values();
		
		for (ForeignKey fk : pkfks) {
			BaseTable rt = fk.getReferenced();
						
			JavaType tt = tam.entityType(rt, Part.INTERFACE);
			String hvar = "h" + ordinal;
			
												
			line(buf, tt.getQualifiedName(), ".Holder ", hvar, " = ", entityHolderExpression("src", fk, tam), ";");
						
			line (buf, "if (", hvar, " == null || ", hvar, ".isNull() || (!", hvar, ".value().isIdentified())) {");
			line (buf, "return null;");
			line (buf, "}");
			
			ordinal++;
		}
		
		line(buf, "PrimaryKeyEntity pk = new PrimaryKeyEntity(src, ctx);");
		line(buf, "return pk;");
		
		return buf.toString();
	}
	
	
	
	
	


	private String primaryKeyEntityVariables(Catalog cat, BaseTable t, TableMapper tam, AttributeTypeMap tym) {
//		PrimaryKey pk = t.getPrimaryKey();
//		
//		if (pk == null) {
//			return "";		
//		}
		
//		Comparator<Identifier> cmp = t.getEnvironment().getIdentifierRules().comparator();		
//		Map<Identifier, List<ForeignKey>> fkmap = new TreeMap<Identifier, List<ForeignKey>>(cmp);
//		List<Column> al = groupPrimaryKeyColumns(t, fkmap);
		
		StringBuilder buf = new StringBuilder();
		
		List<Column> cl = getAttributeColumnList(cat, t, tym);		
		String attr = attributeContainerImplementation(cat, t, tam, tym, cl, true, false);
		
		buf.append(attr);
		
		return buf.toString();
	}
	
	private List<Column> groupPrimaryKeyColumns(BaseTable t, Map<Identifier, List<ForeignKey>> fkcols) {
		PrimaryKey pk = t.getPrimaryKey();
		
		if (pk == null) {
			return Collections.emptyList();
		}
		
		List<Column> pkcols = new LinkedList<Column>();
		
		Collection<ForeignKey> fks = t.foreignKeys().values();		
				
		ColumnMap cm = pk.getColumnMap();
		
		for (Column pkcol : cm.values()) {			
			Identifier n = pkcol.getColumnName();						
						
			boolean fkcol = false;
						
			for (ForeignKey fk : fks) {
				if (fk.getColumnMap().contains(n)) {
					fkcol = true;
					
					if (fkcols != null) {
						List<ForeignKey> fklist = fkcols.get(n);
						
						if (fklist == null) {
							fklist = new LinkedList<ForeignKey>();
							fkcols.put(n, fklist);
						}
						
						fklist.add(fk);
					}
				}				
			}			
			
			if (!fkcol) {
				pkcols.add(pkcol);
			}
		}
		
		
		return pkcols;
	}
	
	private Map<Identifier, ForeignKey> primaryKeyForeignKeys(BaseTable t) {
		PrimaryKey pk = t.getPrimaryKey();
				
		if (pk == null) {
			return Collections.emptyMap();
		}
		
		Environment env = t.getEnvironment();
		IdentifierRules ir = env.getIdentifierRules();
		Comparator<Identifier> cmp = ir.comparator();
		
		Map<Identifier, ForeignKey> result = new TreeMap<Identifier, ForeignKey>(cmp);		
		Collection<ForeignKey> fks = t.foreignKeys().values();		
				
		ColumnMap cm = pk.getColumnMap();
		
		for (Column pkcol : cm.values()) {			
			Identifier n = pkcol.getColumnName();						
						
			for (ForeignKey fk : fks) {
				if (fk.getColumnMap().contains(n)) {
					result.put(fk.getUnqualifiedName(), fk);					
				}				
			}		
		}
				
		return result;
	}	
	
	

	private String primaryKeyEntityAttributeSet(Catalog cat, BaseTable t,
			TableMapper tam, AttributeTypeMap tym) {
		
//		private static final java.util.Set<Film.Attribute> attributes = java.util.Collections.emptySet();
//		
//		@Override
//		public java.util.Set<Film.Attribute> attributes() {
//			return FilmImpl.PrimaryKeyEntity.attributes;
//		}
		
		StringBuilder buf = new StringBuilder();
		
		List<Column> attrs = groupPrimaryKeyColumns(t, null);
		
		JavaType inft = tam.entityType(t, Part.INTERFACE);
		
		
		String at = getAttributeType();
		
		line(buf, "private static final java.util.Set<",
				inft.getQualifiedName(), ".", at, "> ", "attributes = ");
		
		
		if (attrs.isEmpty()) {
			line(buf, "java.util.Collections.emptySet()");
		}
		else {
			String q = inft.getQualifiedName();
			
			if (attrs.size() == 1) {
				String n = attr(attrs.get(0));				
				line(buf, "java.util.Collections.singleton(", q, ".", at, ".", n, ")");
			}
			else {
				line(buf, "java.util.EnumSet.of(");
				
				int i = 0;
				
				for (Column c : attrs) {
					String n = attr(c);				
					line(buf, "java.util.Collections.singleton(", q, ".", getAttributeType(), ".", n, ")");
					
					if ((i + 1) < attrs.size()) {
						buf.append(", ");
					}
					
					i++;
				}
				
				line(buf, ")");
			}			
		}		
		
		line(buf, ";");		
						
		
		return buf.toString();
	}

	private String primaryKeyEntityConstructorBody(Catalog cat, BaseTable t, TableMapper tam, AttributeTypeMap tym, boolean noArgConstructor) {
		StringBuilder buf = new StringBuilder();
		
		Comparator<Identifier> cmp = t.getEnvironment().getIdentifierRules().comparator();		
		Map<Identifier, List<ForeignKey>> fkmap = new TreeMap<Identifier, List<ForeignKey>>(cmp);
		List<Column> cols = groupPrimaryKeyColumns(t, fkmap);
		
		for (Column c : cols) {
			AttributeDescriptor a = tym.getAttributeDescriptor(c.getDataType());

			if (a == null) {
				continue;
			}

			Class<?> vt = a.getValueType();
			Class<?> ht = a.getHolderType();

			if (vt != null && ht != null) {				
				final String n = variableName(c.getColumnName().getContent());
								
				if (noArgConstructor) {
					line(buf, "this.", n, " = null;");	
				} 
				else {
					line(buf, "this.", n, " = src.", attributeGetter(c, tym), "();");	
				}
				
//				attributeContainerImplementation(cat, t, tam, tym, columnList, pkcols, rw)
			}
		}		
		
		Collection<ForeignKey> keys =  primaryKeyForeignKeys(t).values();
		
		if (noArgConstructor) {
			for (ForeignKey fk : keys) {			
				final String n = variable(fk);
				line(buf, "this.", n, " = null;");	
			}			
		}
		else {
			int i = 0;
			
			for (ForeignKey fk : keys) {			
				final String n = variable(fk);
				
				JavaType rt = tam.entityType(fk.getReferenced(), Part.INTERFACE);
				
				String var = "h" + i;
				
				line(buf, rt.getQualifiedName(), " ", var, " = ", entityHolderExpression("src", fk, tam), ".value();");
				line(buf, "this.", n, " = ", var, ".toPrimaryKey(ctx).ref();");
				
				i++;
			}
			
		}
		
		
		return buf.toString();
	}

	private String referenceAssignmentList(Catalog cat, BaseTable t, TableMapper tam, AttributeTypeMap tym, boolean qualify) {

		StringBuilder buf = new StringBuilder();		
		
		for (ForeignKey fk : t.foreignKeys().values()) {
			String c = formatReferenceAssignment(fk, tam, qualify);
			buf.append(c);			
		}
		
		return buf.toString();
	}

	private String entityConstructorBody(Catalog cat, BaseTable t, TableMapper tam, AttributeTypeMap tym, boolean copyConstructor) {

		StringBuilder buf = new StringBuilder();
		
		List<Column> cols = getAttributeColumnList(cat, t, tym);

		for (Column c : cols) {
			AttributeDescriptor a = tym.getAttributeDescriptor(c.getDataType());

			if (a == null) {
				continue;
			}

			Class<?> vt = a.getValueType();
			Class<?> ht = a.getHolderType();

			if (vt != null && ht != null) {				
				final String n = variableName(c.getColumnName().getContent());
								
				if (copyConstructor) {					
					line(buf, "this.", n, " = src.", n, ";");
				}
				else {
					line(buf, "this.", n, " = null;");										
				}
			}
		}
				
		return buf.toString();		
	}

	private CharSequence generateMetaDataImplementation(Catalog cat, BaseTable t,
			JavaType impl, TableMapper tam, AttributeTypeMap tym) throws IOException {

		String src = getTemplateFor(Part.METADATA_IMPLEMENTATION);

		JavaType intf = tam.entityType(t, Part.INTERFACE);
		JavaType eimp = tam.entityType(t, Part.IMPLEMENTATION);
		
		List<String> il = new ArrayList<String>();
		addImport(impl, intf, il);

		Tag tag = null;

		src = replacePackageAndImports(src, impl, il);
		
		{
			src = t.foreignKeys().isEmpty() ? replaceAllWithComment(src,
					Tag.FOREIGN_KEY_IMPLEMENTATION, "") : readAndReplace(src,
					Tag.FOREIGN_KEY_IMPLEMENTATION, false);
		}
		

		tag = Tag.BASE_TABLE_COLUMN_VARIABLE_LIST;
		src = replaceAll(src, tag,
				generateBaseTableColumnVariableList(t, tag, tam));

		src = replaceAll(src, Tag.POPULATE_COLUMN_MAP_BLOCK,
				generatePopulateColumnMapBlock(t, tam));
		src = replaceAll(src, Tag.CREATE_GET_BASE_TABLE_BODY,
				generateGetBaseTableBody(t, tam));
		src = replaceAll(src, Tag.GET_ENVIRONMENT_BODY,
				generateGetEnvironmentBody(t, tam));		
		src = replaceAll(src, Tag.CREATE_PRIMARY_KEY_BODY,
				generateCreatePrimaryKeyBody(t, tam));
		src = replaceAll(src, Tag.CREATE_FOREIGN_KEY_MAP_BODY,
				generateCreateForeignKeyMapBody(t, tam));

		boolean qualify = hasAmbiguousSimpleNamesForReferenceKeys(t, tam);

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
			String code = referenceKeyMapList(t, tam, qualify);
			logger().debug("generateImplementation: code=" + code);
			src = replaceAll(src, Tag.REFERENCE_KEY_MAP_LIST, code);
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

//		{
//			String code = referenceMapList(t, tam, qualify);
//			logger().debug("generateImplementation: code=" + code);
//			src = replaceAll(src, Tag.REFERENCE_MAP_LIST, code);
//		}

		{
			String code = metaDataInitialization(cat, t, tam, tym, qualify);
			src = replaceAll(src, Tag.META_DATA_INITIALIZATION, code);
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

		src = replaceAll(src, Tag.TABLE_INTERFACE, intf.getUnqualifiedName());
		src = replaceAll(src, Tag.TABLE_IMPL_CLASS, eimp.getUnqualifiedName());		
		src = replaceAll(src, Tag.IMMUTABLE_ENTITY_IMPL_CLASS, eimp.getUnqualifiedName());
		src = replaceAll(src, Tag.MUTABLE_ENTITY_IMPL_CLASS, mutableImplementationSimpleName(intf));

		Environment te = getTargetEnvironment(cat.getEnvironment());
		src = replaceAll(src, Tag.ENVIRONMENT_EXPRESSION,
				generateEnvironmentExpression(te));
		
		return src;
	}

	private String mutableImplementationSimpleName(JavaType intf) {				
		return "Mutable" + intf.getUnqualifiedName() + "Impl";
	}	

	private String generateGetEnvironmentBody(BaseTable t, TableMapper tam) {

		Environment te = getTargetEnvironment(t.getEnvironment());		
		StringBuilder buf = new StringBuilder();		
		line(buf, " return ", generateEnvironmentExpression(te), ";");		
		return buf.toString();
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

		for (Column c : t.getColumnMap().values()) {
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

		ColumnMap columnMap = t.getColumnMap();

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
				"Table(sen);");

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
					"ForeignKeyMap(fkmap);");
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
			literal(buf, n.getContent());
			buf.append(")");
		}

		buf.append(";");

		return buf.toString();
	}

	private String literal(Identifier name) {
		return literal(name == null ? null : name.getContent());
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

	private StringBuilder concat(StringBuilder buf, String e1, String e2) {
		buf.append(e1);
		buf.append(e2);
		return buf;
	}
	
	private StringBuilder concat(StringBuilder buf, String e1, String e2, String ... rest) {
		concat(buf, e1, e2);
		
		for (int i = 0; i < rest.length; i++) {
			buf.append(rest[i]);
		}
		
		return buf;
	}

	private String attributesMethodStatementList(Catalog cat, BaseTable t,
			TableMapper tam, AttributeTypeMap tym) {
		StringBuilder buf = new StringBuilder();

		buf.append("\n// attributesMethodStatementList() - enter\n");

		JavaType intf = tam.entityType(t, Part.INTERFACE);
		String et = intf.getQualifiedName();

		List<Column> cols = getAttributeColumnList(cat, t, tym);

		for (Column c : cols) {
			AttributeDescriptor ai = tym.getAttributeDescriptor(c.getDataType());

			if (ai == null) {
				logger().warn(
						"no attribute-info for column : " + columnName(t, c));
				continue;
			}

			if (ai.getAttributeType() == null) {
				logger().warn(
						"no key type for column : "
								+ c.getColumnName().getContent());
				logger().warn(
						"column-data-type : " + c.getDataType().getDataType());
				logger().warn(
						"column-type-name : " + c.getDataType().getTypeName());
				continue;
			}

			if (ai.getHolderType() == null) {
				logger().warn(
						"no holder type for column : "
								+ c.getColumnName().getContent());
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
		
		JavaType intf = tm.entityType(table, Part.INTERFACE);
		

		if (!fks.isEmpty()) {
			qualified(buf, intf.getQualifiedName(), "MetaData").append(" m = getMetaData();");			
			
			line(buf, "java.util.List<Linker> ll = new java.util.ArrayList<Linker>();", 2);

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
			TableMapper tam, AttributeTypeMap tm) {
		PrimaryKey pk = t.getPrimaryKey();

		StringBuilder buf = new StringBuilder();

		if (pk != null) {
			// List<Column> cl = pk.columns();
			ColumnMap cm = pk.getColumnMap();

			if (cm.size() == 1) {
				Column col = cm.get(0);
				AttributeDescriptor ai = tm.getAttributeDescriptor(col.getDataType());

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
					
					String q = intf.getQualifiedName();
					
					
					String kv = keyConstantVariable(t, col, tam);

					buf.append("@Override\n");
					buf.append("public ");
					buf.append(EntityIdentityMap.class.getCanonicalName());
					buf.append("<");
					qualified(buf, q, getAttributeType());
					buf.append(", ");
					qualified(buf, q, getReferenceType());
					buf.append(", ");
					qualified(buf, q, "Type");
					buf.append(", ");
					buf.append(q);
					buf.append(", ");
					qualified(buf, q, "Holder");
					buf.append(", ");
					qualified(buf, q, "MetaData");										
					buf.append("> createIdentityMap() {\n");
					buf.append("return new ");
					buf.append(aim.getCanonicalName());
					buf.append("<");
					entityTypeArgs(buf, q, true, false);
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

	private String referenceMapList(BaseTable t, TableMapper tm, boolean qualify, boolean pk, boolean rw) {
		StringBuilder buf = new StringBuilder();

		Map<BaseTable, JavaType> map = referenced(t, tm);

		for (Entry<BaseTable, JavaType> e : map.entrySet()) {
			String c = formatReferenceValueMap(t, e.getKey(), e.getValue(), tm, qualify, pk, rw);
			buf.append(c);
		}

		return buf.toString();
	}

	private String formatReferenceValueMap(BaseTable t, BaseTable referenced,
			JavaType target, TableMapper tm, boolean qualify, boolean pk, boolean rw) {

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
				
		Collection<ForeignKey> keys = pk ? primaryKeyForeignKeys(t).values() : t.foreignKeys().values();
				
		Collection<ForeignKey> mks = new ArrayList<ForeignKey>();
		
		
		for (ForeignKey fk : keys) {
			if (fk.getReferenced().equals(referenced)) {
				mks.add(fk);
			}
		}		
								
		final StringBuilder ktbuf = new StringBuilder();
		
		
		ktbuf.append(ti.getQualifiedName());
		ktbuf.append(".Key<");
		ktbuf.append(referenceKeyTypeArgs(tm, t));
		ktbuf.append(">");

		final String kt = ktbuf.toString();


		String hn = target.getQualifiedName() + ".Holder";
		
		
		
		line(buf, "@Override");
		line(buf, "public ", hn, " get", target.getUnqualifiedName(), "(", kt, " key) {");
		
		line(buf, "");
		
		line(buf, "if (key == null) {");
		line(buf, "throw new java.lang.NullPointerException();");
		line(buf, "}");
				
		line(buf, "switch(key.name()) {");
		
		
		for (ForeignKey fk : mks) {
			line(buf, "case ", referenceName(fk), ":");
			line(buf, "return this.", variable(fk), "; ");			
		}
		
		
		
		line(buf, "default:");
		line(buf, "}");
		
		line(buf, "return null;");
		
		line(buf, "}");

		
		
		if (rw) {
			line(buf, "@Override");						
			buf.append("public void ");
			buf.append("set");
			buf.append(target.getUnqualifiedName());
			buf.append("(");
			buf.append(kt);
			buf.append(" key, ");
			buf.append(target.getQualifiedName());
			buf.append(".Holder newValue) {\n");

			line(buf, "if (key == null) {");
			line(buf, "throw new java.lang.NullPointerException();");
			line(buf, "}");
			
			line(buf, "switch(key.name()) {");

			for (ForeignKey fk : mks) {
				line(buf, "case ", referenceName(fk), ":");
				line(buf, "this.", variable(fk), " = newValue; ");			
				line(buf, "break;");
			}

			line(buf, "default:");
			line(buf, "}");
			
			
			buf.append("}\n\n");
		}

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
		// Person.MetaData> {
		//
		// private static final long serialVersionUID = 1L;
		//
		// protected OrganizationKey(
		// EntityMetaData<com.appspot.relaxe.gen.ent.personal.Person.Attribute,
		// Reference, Type, Person> meta, Reference name) {
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
				
		qualified(buf, src.getQualifiedName(), getReferenceType());
				
		buf.append(" name) {\n");
		buf.append("super(");		
		qualified(buf, src.getQualifiedName(), "Type.TYPE");		
		buf.append(", ");
		buf.append("name);");
		buf.append("}\n\n");

		buf.append("@Override\n");
		buf.append("public ");
		buf.append(target.getQualifiedName());
		buf.append(".Holder get(");
		buf.append(src.getQualifiedName());
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
		qualified(buf, src.getQualifiedName(), "Mutable");		
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
		
		final String q = source.getUnqualifiedName();

		nb.append(target.getQualifiedName());
		nb.append(".");
		nb.append("Key<");
		entityTypeArgs(nb, q);
		nb.append(">");

		return nb.toString();
	}

	private void entityTypeArgs(StringBuilder nb, String q, boolean mutable, boolean factory) {		
		qualified(nb, q, getAttributeType());
		nb.append(", ");
		qualified(nb, q, getReferenceType());
		nb.append(", ");
		qualified(nb, q, "Type");
		nb.append(", ");
		nb.append(q);
		
		if (mutable) {
			nb.append(", ");		
			qualified(nb, q, "Mutable");
		}
		
		nb.append(", ");
		qualified(nb, q, "Holder");		
		
		if (factory) {
			nb.append(", ");
			qualified(nb, q, "Factory");
		}
		
		nb.append(", ");
		qualified(nb, q, "MetaData");
	}
	
	private void entityTypeArgs(StringBuilder nb, String q) {		
		entityTypeArgs(nb, q, true, true);
	}	

	private StringBuilder qualified(StringBuilder nb, String qualifier, String unqualified) {
		return nb.append(qualifier).append(".").append(unqualified);
	}

	private void appendSchemaPrefix(SchemaElement element, StringBuilder nb) {
		SchemaName n = element.getName().getQualifier();
		String prefix = (n == null) ? "" : name(n.getSchemaName().getContent());
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

	private String valueVariableList(Catalog cat, BaseTable t, TableMapper tm, AttributeTypeMap tym) {
		List<Column> acl = getAttributeColumnList(cat, t, tym);
		StringBuilder content = new StringBuilder();

		for (Column c : acl) {
			String code = formatValueVariable(t, c, tm, tym);
			content.append(code);
		}

		return content.toString();
	}

	private String valueAccessorList(Catalog cat, BaseTable t, TableMapper tm, AttributeTypeMap tym, boolean impl) {
		List<Column> acl = getAttributeColumnList(cat, t, tym);
		StringBuilder content = new StringBuilder();

		for (Column c : acl) {
			String code = formatValueAccessor(t, c, tm, tym, impl);
			content.append(code);
		}

		return content.toString();
	}

	private String formatValueVariable(BaseTable t, Column c, TableMapper tm,
			AttributeTypeMap tym) {

		// private transient VarcharValue<Person.Attribute, Person> lastName;

		StringBuilder buf = new StringBuilder();

		AttributeDescriptor a = tym.getAttributeDescriptor(c.getDataType());

		if (a == null) {
			return "";
		}

		Class<?> at = a.getAccessorType();
		Class<?> kt = a.getAttributeType();

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
				
		String q = intf.getUnqualifiedName();
		
		StringBuilder buf = new StringBuilder();
		buf.append(q).append(".").append(reference ? getReferenceType() : getAttributeType());
		buf.append(", ");
		buf.append(q);
		buf.append(", ");
		buf.append(q).append(".Mutable");
		
		return buf.toString();
	}

	private String referenceKeyTypeArgs(TableMapper tm, BaseTable t) {
		JavaType intf = tm.entityType(t, Part.INTERFACE);
		StringBuilder buf = new StringBuilder();
		String q = intf.getQualifiedName();		
		entityTypeArgs(buf, q);		

		return buf.toString();
	}

	private String formatValueAccessor(BaseTable t, Column c, TableMapper tm,
			AttributeTypeMap tym, boolean impl) {

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

		AttributeDescriptor a = tym.getAttributeDescriptor(c.getDataType());

		if (a == null) {
			return "";
		}

		Class<?> at = a.getAccessorType();
		Class<?> kt = a.getAttributeType();

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
		return valueVariableName(c.getColumnName());
	}
	
	private String valueVariableName(Identifier c) {
		return variableName(c.getContent());
	}

	private String variableName(String n) {
		return name(n, false);
	}
	
	private String getter(ForeignKey fk) {
		return accessor(fk, "get");
	}	

	private String setter(ForeignKey fk) {
		return accessor(fk, "set");
	}	

	private String accessor(ForeignKey fk, String prefix) {						
		String n = name(referenceName(fk), true);		
		StringBuilder buf = new StringBuilder(n.length() + prefix.length());
		buf.append(prefix);
		buf.append(n);
		return buf.toString();
	}

	private String metaDataInitialization(Catalog cat, BaseTable t,
			TableMapper tm, AttributeTypeMap tym, boolean qualify) {
		StringBuilder buf = new StringBuilder();
		buf.append(attributeKeyInitialization(cat, t, tm, tym));
		buf.append(referenceKeyInitialization(cat, t, tm, qualify));
		return buf.toString();
	}

	private String attributeKeyInitialization(Catalog cat, BaseTable t,
			TableMapper tm, AttributeTypeMap tym) {
		List<Column> acl = getAttributeColumnList(cat, t, tym);
		StringBuilder content = new StringBuilder();
		
		JavaType intf = tm.entityType(t, Part.INTERFACE);
		String q = intf.getQualifiedName();

		for (Column c : acl) {
			AttributeDescriptor a = tym.getAttributeDescriptor(c.getDataType());

			if (a == null) {
				continue;
			}

			Class<?> kt = a.getAttributeType();

			if (kt == null) {
				logger().warn(
						"no key type for column " + c.getUnqualifiedName());
			} else {
				content.append(kt.getCanonicalName());
				content.append(".get(this, ");
				content.append(q);
				content.append(".Attribute.");
				content.append(attr(c));
				content.append(");\n");
			}
		}

		return content.toString();
	}

	private String attributeKeyMapList(Catalog cat, BaseTable t,
			TableMapper tm, AttributeTypeMap tym) {
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
			BaseTable t, TableMapper tm, AttributeTypeMap tym) {
		List<Column> acl = getAttributeColumnList(cat, t, tym);

		// key -type => occurences:
		Map<Class<?>, Integer> tom = new HashMap<Class<?>, Integer>();

		for (Column c : acl) {
			AttributeDescriptor a = tym.getAttributeDescriptor(c.getDataType());

			if (a == null) {
				continue;
			}

			Class<?> k = a.getAttributeType();

			if (k == null) {
				logger().warn(
						"no key type for column : "
								+ c.getColumnName().getContent());
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
			AttributeTypeMap tym) {
		Set<Identifier> fkcols = foreignKeyColumnMap(cat, t).keySet();
		List<Column> attrs = new ArrayList<Column>();

		for (Column c : t.getColumnMap().values()) {
			if (!fkcols.contains(c.getColumnName())) {
				attrs.add(c);
			}
		}

		return attrs;
	}

	private String attributeKeyList(Catalog cat, BaseTable t, TableMapper tm,
			AttributeTypeMap tym) {
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
		
		JavaType src = tm.entityType(fk.getReferencing(), Part.INTERFACE);
		
		JavaType mdi = tm.entityType(fk.getReferencing(), Part.METADATA_IMPLEMENTATION);

		StringBuilder buf = new StringBuilder();

		buf.append("public static final ");

		buf.append(tt.getQualifiedName());
		buf.append(".Key");
		buf.append("<");
		buf.append(referenceKeyTypeArgs(tm, fk.getReferencing()));
		buf.append("> ");
		buf.append(referenceName);
		buf.append(" = ");
		buf.append(mdi.getQualifiedName());		
		buf.append(".getInstance().get");
		buf.append(tt.getUnqualifiedName());
		buf.append("Key(");
		qualified(buf, src.getQualifiedName(), getReferenceType());		
		buf.append(".");
		buf.append(referenceName);
		buf.append(");\n");

		return buf.toString();
	}

	private String formatAttributeKeyMap(BaseTable t, Class<?> keyType,
			int occurences, TableMapper tm) {
		StringBuilder buf = new StringBuilder();

		JavaType intf = tm.entityType(t, Part.INTERFACE);

		// goal:
		// private Map<Attribute, IntegerKey<Attribute, Person>> integerKeyMap =
		// new HashMap<Attribute, IntegerKey<Attribute,Person>>();

		String ktargs = keyTypeArgs(tm, t, false);

		buf.append("private java.util.Map<");		
		buf.append(intf.getQualifiedName());
		buf.append(".Attribute");
		buf.append(", ");
		buf.append(keyType.getCanonicalName());
		buf.append("<");
		buf.append(ktargs);
		buf.append(">> ");
		buf.append(getKeyMapVariable(keyType));
		buf.append(" = new java.util.HashMap<");
		buf.append(intf.getQualifiedName());
		buf.append(".Attribute");
		buf.append(", ");
		buf.append(keyType.getCanonicalName());
		buf.append("<");
		buf.append(ktargs);
		buf.append(">>();\n");

		String kt = getKeyName(keyType);

		// buf.append("@Override\n");
		buf.append("protected java.util.Map<");
		buf.append(intf.getQualifiedName());
		buf.append(".Attribute");
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
		
		Class<?> ec = keyType.getEnclosingClass();

		if (ec == null) {
			kt = getSimpleName(keyType);
		} else {
			kt = getSimpleName(keyType, true) + getSimpleName(ec);
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
		buf.append(intf.getQualifiedName());
		buf.append(".");
		buf.append(getReferenceType());
		buf.append(", ");
		buf.append(rki);
		buf.append("> ");
		buf.append(rkv);
		buf.append(" = new java.util.HashMap<");
		buf.append(intf.getQualifiedName());
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
		buf.append(intf.getQualifiedName());
		buf.append(".");
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
	
	private String formatReferenceAssignment(ForeignKey key, TableMapper tm, boolean qualify) {
		StringBuilder buf = new StringBuilder();
		
		BaseTable t = key.getReferencing();
		BaseTable r = key.getReferenced();
		
		JavaType intf = tm.entityType(t, Part.INTERFACE);		
		JavaType target = tm.entityType(key.getReferenced(), Part.INTERFACE);		
		String ktn = referenceKeyReferenceTypeName(t, r, tm, qualify);
		
		
//		SAMPLE OUTPUT
//		{
//    	Language.Holder lh = me.getLanguage(Film.LANGUAGE);
//    	
//    	if (lh != null) {
//    		 if (lh.isNull()) {
//    			 ne.languageMap.put(Film.LANGUAGE, lh);	 
//    		 }
//    		 else {
//    			 Language value = lh.value();    			 
//    			 ne.languageMap.put(Film.LANGUAGE, value.toImmutable(ctx));
//    		 }    		 
//    	}
//		}
		
		final String tqn = target.getQualifiedName();
		
		// String mvar = referenceValueMapVariable(r, target, qualify);		
		
		line(buf, "{");
		line(buf, ktn, " key = ");
		line(buf, intf.getQualifiedName(), ".", referenceName(key), ";");
		
		line(buf, tqn, ".Holder holder = me.get", target.getUnqualifiedName(), "(key);");
		
		String refvar = variable(key);
		
		
		line(buf, "if (holder != null) {");
		line(buf, "if (holder.isNull()) {");		
		line(buf, "ne.", refvar, " = holder;");
		line(buf, "} else {");
		line(buf, tqn, " se = holder.value();");
		line(buf, tqn, " result = se.toImmutable(ctx);");
		line(buf, tqn, ".Holder nref = result.ref();");
		line(buf, "ne.", refvar, " = nref;");
		
		// ne.languageMap.put(key, ref);
		
		line(buf, "}");
		line(buf, "}");
		line(buf, "}");
		
		return buf.toString();

	}
		

	private String formatBuilderLinkerInit(ForeignKey fk, TableMapper tm)
			throws IOException {

		// sample output:
		// {
		// final Project.Key<Reference, Type, HourReport, HourReport.MetaData> pk = FK_HHR_PROJECT;
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
			AttributeTypeMap tym) {
		StringBuilder buf = new StringBuilder();

		AttributeDescriptor a = tym.getAttributeDescriptor(c.getDataType());

				
		if (a == null) {
			String msg = columnDescription("no attribute-info for ", t, c);
			logger().warn(msg);
			return "";
		}

		Class<?> kc = a.getAttributeType();

		if (kc == null) {
			String msg = columnDescription("no attribute-info key type for ", t, c);
			logger().warn(msg);
			return "";
		}

		JavaType mdi = tm.entityType(t, Part.METADATA_IMPLEMENTATION);

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
		buf.append(mdi.getQualifiedName());
		buf.append(".getInstance().get");
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
				+ ") (" + AbstractValueType.getSQLTypeName(type) + ") ("
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
	
	private String entityAttributeAccessorList(Catalog cat, BaseTable t, TableMapper tam, AttributeTypeMap tym, boolean impl, boolean pkcols, boolean rw) {
		
		StringBuilder buf = new StringBuilder();
		
		List<Column> cols = getAttributeColumnList(cat, t, tym);

		for (Column c : cols) {
			AttributeDescriptor a = tym.getAttributeDescriptor(c.getDataType());

			if (a == null) {
				continue;
			}

			Class<?> vt = a.getValueType();
			Class<?> ht = a.getHolderType();

			if (vt != null && ht != null) {
				String code = formatEntityAttributeAccessor(t, c, a, tam, tym, impl, pkcols, rw);
				buf.append(code);
			}
		}
		
		return buf.toString();
	}

	private String formatEntityAttributeAccessor(Table table, Column c, AttributeDescriptor info, TableMapper tam, AttributeTypeMap tym, boolean impl, boolean pkcol, boolean rw) {
		// final String attributeName = attr(c);
		Class<?> valueType = info.getValueType();
		Class<?> holderType = info.getHolderType();
		
		String htname = holderType.getCanonicalName();

		final String type = valueType.getCanonicalName();

		StringBuilder nb = new StringBuilder();
//		final String n = name(c.getColumnName().getContent());
		
		JavaType intf = tam.entityType(table, Part.INTERFACE);
		
		BaseTable btbl = table.asBaseTable();
		
		boolean ispkcol = (btbl != null && btbl.isPrimaryKeyColumn(c)); 
		
		// String vv = valueVariableName(table, c);			

//		boolean b = valueType.equals(Boolean.class);
//		String prefix = b ? "is" : "get";		
		String typeName = getAccessorNameSuffix(c, tym);
		
		
		String getter = accessorMethodName(tym, c, false);
		
		if (impl) {
			line(nb, "@Override");
		}
		
		String aget = attributeGetter(c, tym);
				
		line(nb, "public ", htname, " ", aget, "()");

		if (!impl) {
			line(nb, ";", 1);
		} else {
			// getter implementation
			line(nb, " {", 1);
					
			// example: 
			// IntegerHolder h = getInteger(Film.FILM_ID);
			// return h;
			
			if (pkcol && (!ispkcol)) {
				line(nb, "return null;");
			}
			else {			
				line(nb, htname, " h = ", getter, "(", intf.getQualifiedName(), ".", attr(c), ");");
				line(nb, "return h;");
			}
			
			
			
			
			
			line(nb, "}", 2);
		}
		

		if (holderType != null) {
			Method vom = null;
			
			try {
				Method m = holderType.getMethod("valueOf", valueType);
																
				if (((m.getModifiers() & Modifier.STATIC) != Modifier.STATIC)) {
					throw new Exception("valueOf -method is not static"); 
				}
				
				if (((m.getModifiers() & Modifier.PUBLIC) != Modifier.PUBLIC)) {
					throw new Exception("valueOf -method is not public"); 
				}
				
				vom = m;
			}
			catch (NoSuchMethodException e) {
				logger().debug("Holder type {} does not contain applicable valueOf -method", holderType);
			}
			catch (Exception e) {
				logger().warn(e.getMessage(), holderType);
			}
			
			
//		    sample:
//			@Override
//		    public void setFilmId(java.lang.Integer newValue) {
//		    	setInteger(FILM_ID, IntegerHolder.valueOf(newValue));    	
//		    }
			
			
			String aset = attributeSetter(c);
			
			
			if (rw) {			
				if (vom != null) {
					if (impl) {
						line(nb, "@Override");
					}			
					
					line(nb, "public void ", aset, "(", type, " newValue)");
					
					if (!impl) {
						line(nb, ";", 1);
					} else {
						line(nb, " {", 1);
						line(nb, "set", typeName, "(", intf.getQualifiedName(), ".", attr(c), ", ", htname, ".valueOf(newValue));");				
						line(nb, "}", 2);
					}
				}
	
	//			sample:
	//		    @Override
	//		    public void setFilmId(IntegerHolder newValue) {
	//		    	setInteger(FILM_ID, newValue);    	
	//		    }    		
				
				if (impl) {
					line(nb, "@Override");
				}			
				
				line(nb, "public void ", aset, "(", htname, " newValue)");
				
				if (!impl) {
					line(nb, ";", 1);
				} else {
					line(nb, " {", 1);
					line(nb, "set", typeName, "(", intf.getQualifiedName(), ".", attr(c), ", newValue);");				
					line(nb, "}", 2);
				}
			}
		}

		return nb.toString();
	}	
		
	public String attributeExpression(JavaType intf, AttributeTypeMap tym, Column c) {
		StringBuilder buf = new StringBuilder();
				
		String getter = accessorMethodName(tym, c, false);
		
		buf.append(getter);		
		buf.append("(");
		buf.append(intf.getQualifiedName());
		buf.append(".");
		buf.append(attr(c));
		buf.append(")");	
		
		return buf.toString();
		
	}
	
	public String holderVariableInitialization(String hvar, String srcvar, JavaType intf, AttributeTypeMap tym, Column c) {		
		AttributeDescriptor ad = tym.getAttributeDescriptor(c.getDataType());		
		Class<?> holderType = ad.getHolderType();
		
		String htname = holderType.getCanonicalName();
		
		StringBuilder buf = new StringBuilder();

		concat(buf, htname, " ", hvar, " = "); 
		
		if (srcvar != null) {
			concat(buf, srcvar, ".");
		}
						
		concat(buf, attributeGetter(c, tym), "();\n");
		
		return buf.toString();
	}
	
	public String holderAssignment(String destvar, String srcvar, JavaType intf, AttributeTypeMap tym, Column c) {		
		StringBuilder buf = new StringBuilder();
				
		concat(buf, destvar, ".", valueVariableName(c.getColumnName()), " = "); 
		
		if (srcvar != null) {
			concat(buf, srcvar, ".");
		}
						
		concat(buf, attributeExpression(intf, tym, c), ";\n");
		
		return buf.toString();
	}	
	
	
	public String accessorMethodName(AttributeTypeMap tym, Column c, boolean write) {
		String prefix = write ? "set" : "get";		
		String typeName = getAccessorNameSuffix(c, tym);		
		return prefix + typeName;
	}
	

	private String contentAccessors(Catalog cat, BaseTable t, TableMapper tm,
			AttributeTypeMap tym, boolean impl) {
		StringBuilder content = new StringBuilder();
		contentAccessors(cat, t, content, tm, tym, impl);
		return content.toString();
	}

	private void contentAccessors(Catalog cat, BaseTable t, StringBuilder content,
			TableMapper tm, AttributeTypeMap tym, boolean impl) {
		List<Column> cols = getAttributeColumnList(cat, t, tym);

		for (Column c : cols) {
			AttributeDescriptor a = tym.getAttributeDescriptor(c.getDataType());

			if (a == null) {
				continue;
			}

			Class<?> vt = a.getValueType();
			Class<?> ht = a.getHolderType();

			if (vt != null && ht != null) {
				String code = formatContentAccessors(t, c, a, impl);
				content.append(code);
			}
		}
	}

	private String formatContentAccessors(Table table, Column c, AttributeDescriptor info,
			boolean impl) {
		// final String attributeName = attr(c);
		Class<?> attributeType = info.getValueType();
		Class<?> holderType = info.getHolderType();

		final String type = attributeType.getCanonicalName();

		StringBuilder nb = new StringBuilder();
		final String n = name(c.getColumnName().getContent());

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
	
	public String name(CharSequence identifier) {
		return name(identifier, true);
	}

	/**
	 * Converts SQL style name to camel case
	 * 
	 * @param identifier
	 * @return
	 */
	public String name(CharSequence identifier, final boolean upperFirst) {
		int len = identifier.length();
		StringBuilder nb = new StringBuilder(len);
		boolean upper = upperFirst;

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
			AttributeTypeMap tym) {
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
			AttributeTypeMap tym, StringBuilder content) {
		List<Column> cols = getAttributeColumnList(cat, t, tym);

		for (Column c : cols) {
			AttributeDescriptor ai = tym.getAttributeDescriptor(c.getDataType());

			if (ai == null) {
				logger().warn(
						"no attribute-info for column : " + columnName(t, c));
				continue;
			}

			if (ai.getAttributeType() == null) {
				logger().warn(
						"no key type for column : "
								+ c.getColumnName().getContent());
				logger().warn(
						"column-data-type : " + c.getDataType().getDataType());
				logger().warn(
						"column-type-name : " + c.getDataType().getTypeName());
			}

			content.append(attr(c));
			content.append("(");
			ValueType<?> pt = ai.getPrimitiveType();

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
		final String kn = fk.getUnqualifiedName().getContent();

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
		String n = fk.getUnqualifiedName().getContent();
		return referenceName(fk.getReferencing().getUnqualifiedName().getContent(), n);
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

		String attr = n.getContent().toUpperCase();

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
		return translate(identifier.getContent());
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
			id = te.getIdentifierRules().toIdentifier(id.getContent());
		}

		return id;
	}
	
	
	
	private String getAccessorNameSuffix(Column column, AttributeTypeMap typeMapper) {
		AttributeDescriptor ai = typeMapper.getAttributeDescriptor(column.getDataType());
		String n = getKeyName(ai.getAttributeType());
		return removeSuffix(n, "Attribute");		
	}
	
	
//	private Collection<ForeignKey> foreignKeysWithPrimaryKeyColumns(Collection<ForeignKey> fks) {
//		List<ForeignKey> out = new ArrayList<ForeignKey>();
//		
//		for (ForeignKey fk : fks) {
//			BaseTable t = fk.getReferencing();
//			
//			for (Column c : fk.getColumnMap().values()) {
//				if (t.isPrimaryKeyColumn(c)) {
//					out.add(fk);
//					break;
//				}
//			}
//		}
//		
//		return out;
//	}
	

	public String attributeGetter(Column c, AttributeTypeMap tam) {
		AttributeDescriptor info = tam.getAttributeDescriptor(c.getDataType());		
		Class<?> valueType = info.getValueType();		
		boolean b = valueType.equals(Boolean.class);
		String prefix = b ? "is" : "get";		
		String n = name(c.getColumnName().getContent());		
		return prefix + n;
		
	}
	
	public String attributeSetter(Column c) {
		String prefix = "set";		
		String n = name(c.getColumnName().getContent());		
		return prefix + n;
		
	}

	
}
