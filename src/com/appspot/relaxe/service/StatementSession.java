/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.service;

import com.appspot.relaxe.exec.QueryProcessor;
import com.appspot.relaxe.exec.ResultSetProcessor;
import com.appspot.relaxe.exec.UpdateProcessor;
import com.appspot.relaxe.expr.SQLDataChangeStatement;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.ddl.SQLSchemaStatement;
import com.appspot.relaxe.query.QueryException;

public interface StatementSession {

	void execute(Statement statement, QueryProcessor qp)
			throws QueryException;
	
	void executeSelect(SelectStatement statement, ResultSetProcessor qp)
			throws QueryException;	
	
	void executeUpdate(SQLDataChangeStatement statement, UpdateProcessor qp)
			throws QueryException;
	
	void execute(SQLSchemaStatement statement)
			throws QueryException;	
}
