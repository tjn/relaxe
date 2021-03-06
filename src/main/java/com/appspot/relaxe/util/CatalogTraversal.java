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
package com.appspot.relaxe.util;

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
		for (Column c : t.getColumnMap().values()) {
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
