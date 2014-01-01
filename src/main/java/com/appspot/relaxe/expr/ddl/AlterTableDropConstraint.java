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
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Constraint;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.PrimaryKey;

public class AlterTableDropConstraint
    extends SQLSchemaStatement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6604968506968220728L;
	private SchemaElementName table; 
    private SchemaElementName constraint;
    
    /**
	 * No-argument constructor for GWT Serialization
	 */
	protected AlterTableDropConstraint() {
	}

    public AlterTableDropConstraint(PrimaryKey pk) {
        this(pk, pk.getTable());
    }

    public AlterTableDropConstraint(ForeignKey fk) {
        this(fk, fk.getReferencing());
    }
    
    private AlterTableDropConstraint(Constraint constraint, BaseTable t) {
        super(Name.ALTER_TABLE);
        this.constraint = constraint.getName();        
        this.table = relativize(t.getName());                
    }
    
    @Override
    protected void traverseContent(VisitContext vc, ElementVisitor v) {
        SQLKeyword.ALTER.traverse(vc, v);
        SQLKeyword.TABLE.traverse(vc, v);        
        this.table.traverse(vc, v);
        SQLKeyword.DROP.traverse(vc, v);        
        SQLKeyword.CONSTRAINT.traverse(vc, v);
        this.constraint.getUnqualifiedName().traverse(vc, v);                
    }
    
    
    public SchemaElementName getTable() {
		return table;
	}
    
    public SchemaElementName getConstraint() {
		return constraint;
	}
}
