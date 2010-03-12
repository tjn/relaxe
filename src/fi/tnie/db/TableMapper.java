/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Map;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;

public interface TableMapper {
	
	class Type {
		private String packageName; 
		private String unqualifiedName;
		private String qualifiedName;
				
		public Type(String packageName, String unqualifiedName) {
			super();
			this.packageName = packageName;									
			this.unqualifiedName = unqualifiedName;
			
			if (this.packageName == null) {
				this.qualifiedName = this.unqualifiedName;
			}
			else {
				StringBuffer buf = new StringBuffer(this.packageName);
				buf.append(".");
				buf.append(unqualifiedName);
				this.qualifiedName = buf.toString();
			}
		}
		
		public String getPackageName() {
			return this.packageName;
		}
		
		public String getUnqualifiedName() {			
			return this.unqualifiedName;
		}	
		
		public String getQualifiedName() {
			return this.qualifiedName;
		}
	}
	
	enum Part {
		INTERFACE,
		IMPLEMENTATION
	}
		
	Map<Part, Type> entityMetaDataType(BaseTable table);
	
	Class<?> getAttributeType(Table table, Column c);
			
}
