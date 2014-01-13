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
package com.appspot.relaxe;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.expr.ElementVisitorAdapter;
import com.appspot.relaxe.expr.Parameter;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.VisitException;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ValueHolder;

public class AssignmentVisitor extends ElementVisitorAdapter {
	private PreparedStatement preparedStatement;
	private int ordinal;
	
	private static Logger logger = LoggerFactory.getLogger(AssignmentVisitor.class);
	
	private ValueAssignerFactory assignerFactory;
	
//	public AssignmentVisitor(PreparedStatement ps) {
//		this(new DefaultValueAssignerFactory(), ps);
//	}

	public AssignmentVisitor(ValueAssignerFactory assignerFactory, PreparedStatement ps) {
		super();
		
		if (assignerFactory == null) {
			throw new NullPointerException("assignerFactory");
		} 
		
		if (ps == null) {
			throw new NullPointerException("'ps' must not be null");
		}
		
		this.assignerFactory = assignerFactory;		
		this.preparedStatement = ps;
		this.ordinal = 1;
	}	

	@Override
	public VisitContext start(VisitContext vc, Parameter<?, ?, ?> p) {								
		try {			
			assign(ordinal, p);
			logger().debug(ordinal + ": " + p.getName() + " =>"+ ((p.getValue() == null) ? "<null>" : p.getValue()));
			
//			preparedStatement.setObject(ordinal, h.value(), p.getType());
			ordinal++;
		} 
		catch (SQLException e) {				
			throw new VisitException(e.getMessage(), e);
		}
		
		return vc;
	}
	
	
	
	private	
	<		
		V extends Serializable,
		T extends ValueType<T>, 
		H extends ValueHolder<V, T, H>
	>	
	void	
	assign(int ord, Parameter<V, T, H> param) 
		throws SQLException {
		
		H h = param.getValue();
		ParameterAssignment a = assignerFactory.create(h, param.getColumnType());

		if (a == null) {
			throw new NullPointerException("no assignment for parameter[" + ord + "] of type " + h.getType() + ": " + h + " with assigner factory: " + assignerFactory);
		}
		
		a.assign(preparedStatement, ord);
	}

	protected static Logger logger() {
		return AssignmentVisitor.logger;
	}	
	
	public void reset() 
	    throws SQLException {
	    this.ordinal = 1;
	    this.preparedStatement.clearParameters();
	}
	
}