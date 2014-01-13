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
package com.appspot.relaxe;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.appspot.relaxe.types.OtherType;
import com.appspot.relaxe.types.AbstractValueType;
import com.appspot.relaxe.value.AbstractPrimitiveHolder;


public abstract class OtherAssignment<O extends OtherType<O>, V extends Serializable, T extends AbstractValueType<T>, H extends AbstractPrimitiveHolder<V, T, H>>
	extends AbstractParameterAssignment<V, T, H> {
		
	public OtherAssignment(H value) {
		super(value);
	}

	@Override
	public void assign(PreparedStatement ps, int ordinal, V newValue) throws SQLException {
		Object o = getObject(newValue);		
		int t = getType().getSqlType();
		ps.setObject(ordinal, o, t);
	}
	
	protected Object getObject(V v) {
		return v;		
	}
	
	protected abstract O getType();
}
