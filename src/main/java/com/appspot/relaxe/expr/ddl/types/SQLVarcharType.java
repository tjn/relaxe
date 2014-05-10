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
package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;


public class SQLVarcharType
    extends AbstractSQLCharacterType {
    
//    public static final Varchar VARCHAR_10 = new Varchar(10);
//    public static final Varchar VARCHAR_20 = new Varchar(20);
//    public static final Varchar VARCHAR_30 = new Varchar(30);
//    public static final Varchar VARCHAR_40 = new Varchar(40);
//    public static final Varchar VARCHAR_50 = new Varchar(50);
//    public static final Varchar VARCHAR_100 = new Varchar(100);	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2301149943314207588L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected SQLVarcharType() {
		
	}
	
    public static SQLVarcharType get(int length) {
        return SQLVarcharType.get(Integer.valueOf(length));
    }
    
    public static SQLVarcharType get(Integer length) {
        return new SQLVarcharType(length);
    }
    
    public SQLVarcharType(Integer length) {
        super(length);
    }
    
    
    @Override
	protected void traverseName(VisitContext vc, ElementVisitor v) {
		SQLKeyword.VARCHAR.traverse(vc, v);		
	}    
}
