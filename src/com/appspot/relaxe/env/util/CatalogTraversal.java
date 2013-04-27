/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.util;

import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.Table;

public class CatalogTraversal	
	implements CatalogVisitor
{	
	private CatalogVisitor visitor;
			
	public CatalogTraversal(CatalogVisitor visitor) {		
		this.visitor = visitor;
	}
	public CatalogTraversal() {		
		this.visitor = this;
	}
	
	public void traverse(Catalog catalog) {
		CatalogVisitor v = this.visitor; 
		
		for (Schema s : catalog.schemas().values()) {
			if (v.visit(s)) {
				if (v.visitBaseTables(s)) {
					for (BaseTable t : s.baseTables().values()) {
						if (v.visitBaseTable(t)) {							
							traverse(t, v);
						}
					}					
				}
			}
		}		
	}

	private void traverse(Table t, CatalogVisitor v) {
		for (Column c : t.columnMap().values()) {
			v.visit(c);
		}
	}


	@Override
	public boolean visit(Schema s) {
		return true;
	}


	@Override
	public void visit(Column c) {
	}


	@Override
	public boolean visitBaseTable(BaseTable t) {
		return true;
	}


	@Override
	public boolean visitBaseTables(Schema s) {
		return true;
	}


	@Override
	public boolean visitColumns(BaseTable t) {
		return true;
	}	
}
