/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.util;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;

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
		for (Column c : t.columns()) {
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
