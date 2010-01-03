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
	public VisitContext start(VisitContext vc, Parameter p) {								
		try {
			logger().debug(ordinal + " [" + p.getName() + "] => " + p.getValue());
			
			preparedStatement.setObject(ordinal, p.getValue(), p.getType());
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
}