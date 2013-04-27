/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe;

import java.sql.PreparedStatement;
import java.sql.SQLException;


import org.apache.log4j.Logger;

import com.appspot.relaxe.expr.ElementVisitorAdapter;
import com.appspot.relaxe.expr.Parameter;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.VisitException;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;

public class AssignmentVisitor extends ElementVisitorAdapter {
	private PreparedStatement preparedStatement;
	private int ordinal;
	
	private static Logger logger = Logger.getLogger(AssignmentVisitor.class);
	
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
	public VisitContext start(VisitContext vc, Parameter<?, ?> p) {								
		try {			
			assign(ordinal, p.getValue(), p.getColumnType());			
//			logger().debug(ordinal + ": pname: " + p.getName());
//			logger().debug(ordinal + ": ph: " + h);
			
			
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
	<T extends PrimitiveType<T>, H extends PrimitiveHolder<?, T, H>>	
	void	
	assign(int ord, H h, DataType columnType) 
		throws SQLException {
						
		ParameterAssignment a = assignerFactory.create(h, columnType);

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