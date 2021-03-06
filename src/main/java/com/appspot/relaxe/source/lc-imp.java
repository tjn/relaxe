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

// Putting all the columns in the database into single enum type might not scale well.
// 
// We might want to split columns into per-table-enums and
// only provide schema or catalog level column-enum-type if 
// the count of the columns is limited.  

//
//	public enum LiteralPersonalHourReport
//		implements Column {
//		
//		ID(LiteralBaseTable.PERSONAL_HOUR_REPORT, "id", new DataTypeImpl(4, "serial", 10, 0, 0, 0)),
//		COMMENT(LiteralBaseTable.PERSONAL_HOUR_REPORT, "comment", new DataTypeImpl(12, "varchar", 200, 0, 0, 0)),
//		STARTED_AT(LiteralBaseTable.PERSONAL_HOUR_REPORT, "started_at", new DataTypeImpl(92, "time", 15, 0, 0, 0)),
//		FINISHED_AT(LiteralBaseTable.PERSONAL_HOUR_REPORT, "finished_at", new DataTypeImpl(92, "time", 15, 0, 0, 0)),		
//		REPORT_DATE(LiteralBaseTable.PERSONAL_HOUR_REPORT, "report_date", new DataTypeImpl(91, "date", 13, 0, 0, 0)),
//		TRAVEL_TIME(LiteralBaseTable.PERSONAL_HOUR_REPORT, "travel_time", new DataTypeImpl(1111, "interval", 49, 0, 0, 0)),
//		PROJECT(LiteralBaseTable.PERSONAL_HOUR_REPORT, "project", new DataTypeImpl(4, "int4", 10, 0, 0, 0)),
//		TASK(LiteralBaseTable.PERSONAL_HOUR_REPORT, "task", new DataTypeImpl(4, "int4", 10, 0, 0, 0)),
//		EMPLOYER(LiteralBaseTable.PERSONAL_HOUR_REPORT, "employer", new DataTypeImpl(4, "int4", 10, 0, 0, 0)),
//		CREATED_AT(LiteralBaseTable.PERSONAL_HOUR_REPORT, "created_at", new DataTypeImpl(93, "timestamp", 29, 0, 0, 0)),
//		;
//				
//		private LiteralTable table;		
//		private DefaultMutableColumn column;
//		private String name;
//		private DataTypeImpl type;
//				
//		LiteralTableHourReport(LiteralTable table, String name, DataTypeImpl type) {
//			this.name = name;
//			this.table = table;
//			this.type = type;
//		}
//		
//		private void bind() {
//			Identifier n = getInstance().env().createIdentifier(this.name);
//			DefaultMutableTable mt = getInstance().getMutableTableMap().get(this.table);									
//			this.column = new DefaultMutableColumn(mt, n, this.type);			
//		}
//
//		@Override
//		public ColumnName getColumnName() {
//			return column.getColumnName();
//		}
//
//		@Override
//		public DataType getDataType() {
//			return column.getDataType();
//		}
//
//		@Override
//		public Boolean isAutoIncrement() {
//			return column.isAutoIncrement();
//		}
//
//		@Override
//		public boolean isDefinitelyNotNullable() {
//			return column.isDefinitelyNotNullable();
//		}
//
//		@Override
//		public boolean isPrimaryKeyColumn() {
//			return column.isPrimaryKeyColumn();
//		}
//
//		@Override
//		public Identifier getUnqualifiedName() {
//			return column.getUnqualifiedName();
//		}		
//	}
//	
//	public static enum LiteralTableHourReport2
//		implements Column {
//		
//		ID(LiteralColumn.PERSONAL_HOUR_REPORT_ID),
//		COMMENT(LiteralColumn.PERSONAL_HOUR_REPORT_COMMENT),
//		;
//		
//		private Column column;		
//				
//		LiteralTableHourReport2(Column column) {
//			this.column = column;
//		}
//		
//		@Override
//		public ColumnName getColumnName() {
//			return column.getColumnName();
//		}
//	
//		@Override
//		public DataType getDataType() {
//			return column.getDataType();
//		}
//	
//		@Override
//		public Boolean isAutoIncrement() {
//			return column.isAutoIncrement();
//		}
//	
//		@Override
//		public boolean isDefinitelyNotNullable() {
//			return column.isDefinitelyNotNullable();
//		}
//	
//		@Override
//		public boolean isPrimaryKeyColumn() {
//			return column.isPrimaryKeyColumn();
//		}
//	
//		@Override
//		public Identifier getUnqualifiedName() {
//			return column.getUnqualifiedName();
//		}	
//	}