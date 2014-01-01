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
package com.appspot.relaxe.meta;

import java.io.Serializable;
import java.util.Map;

import com.appspot.relaxe.expr.Identifier;

public class ImmutableCatalog
	implements Catalog, Serializable	{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4112469617210800545L;
	
	private Environment environment;
	private Identifier name;
	private SchemaMap schemaMap;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ImmutableCatalog() {
	}
	
	protected ImmutableCatalog(Environment environment, Identifier name, SchemaMap schemaMap) {
		super();
		this.environment = environment;
		this.name = name;
		this.schemaMap = schemaMap;
	}

	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	@Override
	public Identifier getName() {
		return this.name;
	}

	@Override
	public SchemaMap schemas() {	
		return schemaMap;
	}

	@Override
	public Identifier getUnqualifiedName() {
		return this.name;
	}
	
	public static class Builder
		extends MetaObjectBuilder {
		
		private Identifier identifier;		
		private Map<Identifier, Schema> schemaMap;
		
		public Builder(Environment environment, Identifier identifier) {
			super(environment);
			this.identifier = identifier;			
		}		
		
		private Map<Identifier, Schema> getSchemaMap() {
			if (schemaMap == null) {
				schemaMap = createMap();
			}

			return schemaMap;
		}
		
		
				
		public void add(Schema schema) {
			if (schema == null) {
				throw new NullPointerException("schema");
			}
			
			Identifier cat = schema.getCatalogName();
			
			if (!equal(cat, this.identifier)) {
				String cn = (this.identifier == null) ? null : this.identifier.getName();
				throw new IllegalArgumentException("schema " + schema.getName().generate() + " does not belong to the catalog (" + cn + ")");				
			}			
			
			Map<Identifier, Schema> sm = getSchemaMap();			
			Identifier key = schema.getUnqualifiedName();
			
			if (sm.containsKey(key)) {
				throw new IllegalArgumentException("duplicate schema name: " + schema.getName().generate());
			}
			
			sm.put(key, schema);			
		}
				
		public Catalog newCatalog() {		
			Environment env = getEnvironment();
			ImmutableSchemaMap sm = new ImmutableSchemaMap(env, getSchemaMap());			
			ImmutableCatalog cat = new ImmutableCatalog(getEnvironment(), this.identifier, sm);
			return cat;
		}		
	}
	
	
		
	public static class ImmutableSchemaMap
		extends AbstractMetaObjectMap<Schema>
		implements SchemaMap {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5479661203587222276L;

		/**
		 * 	No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private ImmutableSchemaMap() {	
		}
		
		public ImmutableSchemaMap(Environment environment, Map<Identifier, Schema> content) {
			super(environment, content);		
		}
	}
}
