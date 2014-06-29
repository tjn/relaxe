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
/**
 * 
 */
package com.appspot.relaxe.rdbms.mysql;

import java.sql.ResultSetMetaData;

import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.ent.ColumnResolver;
import com.appspot.relaxe.ent.ConstantColumnResolver;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.rdbms.DefaultGeneratedKeyHandler;


public final class MySQLGeneratedKeyHandler 
    extends DefaultGeneratedKeyHandler {
    
	public MySQLGeneratedKeyHandler(ValueExtractorFactory valueExtractorFactory) {
		super(valueExtractorFactory);
	}

	@Override
	protected 
	<		
		M extends EntityMetaData<?, ?, ?, ?, ?, ?, ?, M>
	>
	ColumnResolver createColumnResolver(ResultSetMetaData meta, M em)
		throws RuntimeException {
//			ResultSet is expected to contain single column: GENERATED_KEY
		BaseTable table = em.getBaseTable();

		// MySQL supports max one auto-increment column per table:
		Column col = findAutoIncrementColumn(table);
		//
		if (col == null) {
			throw new RuntimeException(
					"unable to find AUTO_INCREMENT column from table " +
					em.getBaseTable());
		}

		ConstantColumnResolver cr = new ConstantColumnResolver(col);
		return cr;
	}

	private Column findAutoIncrementColumn(BaseTable tbl) {
		for (Column col : tbl.getColumnMap().values()) {
			if (Boolean.TRUE.equals(col.isAutoIncrement())) {
				return col;
			}
		}

		return null;
	}
}