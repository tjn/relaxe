/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;


import org.apache.log4j.Logger;
import fi.tnie.db.expr.ElementVisitorAdapter;
import fi.tnie.db.expr.Parameter;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.expr.VisitException;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.Interval.DayTime;
import fi.tnie.db.types.IntervalType;
import fi.tnie.db.types.PrimitiveType;

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
			assign(ordinal, p.getValue());			
//			logger().debug(ordinal + ": pname: " + p.getName());
//			logger().debug(ordinal + ": ph: " + h);
//			logger().debug(ordinal + ": value: " + ((h == null) ? "" : h.value()));
//			preparedStatement.setObject(ordinal, h.value(), p.getType());
			ordinal++;
		} 
		catch (SQLException e) {				
			throw new VisitException(e.getMessage(), e);
		}
		
		return vc;
	}
	
	private	
	<T extends PrimitiveType<T>, H extends PrimitiveHolder<?, T>>	
	void	
	assign(int ord, H h) 
		throws SQLException {
		
		ParameterAssignment a = assignerFactory.create(h);

		if (a == null) {
			throw new NullPointerException("no assignment for parameter[" + ord + "] of type " + h.getType() + ": " + h);
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