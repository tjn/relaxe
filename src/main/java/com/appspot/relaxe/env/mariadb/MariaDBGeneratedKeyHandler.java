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
package com.appspot.relaxe.env.mariadb;

import java.sql.ResultSetMetaData;

import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.ColumnResolver;
import com.appspot.relaxe.ent.ConstantColumnResolver;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.env.DefaultGeneratedKeyHandler;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.types.ReferenceType;


public final class MariaDBGeneratedKeyHandler 
    extends DefaultGeneratedKeyHandler {
    
	public MariaDBGeneratedKeyHandler(ValueExtractorFactory valueExtractorFactory) {
		super(valueExtractorFactory);
	}

	@Override
	protected 
	<
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, ?, ?, M, ?>,
		E extends Entity<A, R, T, E, ?, ?, M, ?>,
		M extends EntityMetaData<A, R, T, E, ?, ?, M, ?>
	>
	ColumnResolver createColumnResolver(ResultSetMetaData meta, M em)
		throws RuntimeException {
//			ResultSet is expected to contain single column: insert_id
		BaseTable table = em.getBaseTable();

		// TODO: find out if MariaDB supports max one auto-increment column per table like MySQL:
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
		for (Column col : tbl.columnMap().values()) {
			if (Boolean.TRUE.equals(col.isAutoIncrement())) {
				return col;
			}
		}

		return null;
	}
}