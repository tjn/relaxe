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
import fi.tnie.db.rpc.PrimitiveHolder;

public class ParameterAssignment extends ElementVisitorAdapter {
	private PreparedStatement preparedStatement;
	private int ordinal;
	
	private static Logger logger = Logger.getLogger(ParameterAssignment.class);

	public ParameterAssignment(PreparedStatement ps) {
		super();
		
		if (ps == null) {
			throw new NullPointerException("'ps' must not be null");
		}
		
		this.preparedStatement = ps;
		this.ordinal = 1;		
	}	

	@Override
	public VisitContext start(VisitContext vc, Parameter<?, ?> p) {								
		try {			
			PrimitiveHolder<?, ?> h = p.getValue();
			logger().debug(ordinal + " [" + p.getName() + "] => " + h.value());
			preparedStatement.setObject(ordinal, h.value(), p.getType());
						
			ordinal++;
		} 
		catch (SQLException e) {				
			throw new VisitException(e.getMessage(), e);
		}
		
		return vc;
	}
	
	private static Logger logger() {
		return ParameterAssignment.logger;
	}
	
	
	public void reset() 
	    throws SQLException {
	    this.ordinal = 1;
	    this.preparedStatement.clearParameters();
	}
}