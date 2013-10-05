/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg.expr;

import java.util.Collection;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.InsertStatement;
import com.appspot.relaxe.expr.ValueRow;
import com.appspot.relaxe.meta.Table;

public class PGInsertStatement
	extends InsertStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5899030048028001816L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private PGInsertStatement() {
	}

	public PGInsertStatement(Table target,
			ElementList<Identifier> columnNameList, Collection<ValueRow> rows) {
		super(target, columnNameList, rows);
	}

	public PGInsertStatement(Table target, ElementList<Identifier> columnNameList, ValueRow valueRow) {
		super(target, columnNameList, valueRow);
	}

	

}
