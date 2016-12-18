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

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ValueHolder;



public abstract class AbstractParameterAssignment<
	V extends Serializable, 
	T extends ValueType<T>, 
	H extends ValueHolder<V, T, H>
>
	implements ParameterAssignment {	
	private H holder;
	
	public AbstractParameterAssignment(H holder) {
		super();
		
		if (holder == null) {
			throw new NullPointerException("holder");
		}
		
		this.holder = holder;
	}
	
	protected H holder() {
		return holder;
	}

	@Override
	public void assign(PreparedStatement ps, int ordinal) 
		throws SQLException, IOException {
								
		V v = holder.value();
						
		if (v == null) {
			ps.setNull(ordinal, holder.getSqlType());
		}
		else {
			assign(ps, ordinal, v);
		}
	}
	
	public abstract void assign(PreparedStatement ps, int ordinal, V newValue)
		throws SQLException, IOException;
}
