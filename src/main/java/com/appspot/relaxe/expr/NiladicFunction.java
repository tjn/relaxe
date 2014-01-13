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
package com.appspot.relaxe.expr;

import com.appspot.relaxe.types.ValueType;

public class NiladicFunction    
    implements ValueExpression {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4927882086659272826L;
	private SQLKeyword name;
    private int dataType;    
    
    /** Niladic function CURRENT_USER.
     * 
     * AbstractType of this value expression is VARCHAR.
     * That may not be generally "right", but good enough. 
     */
    public static final NiladicFunction CURRENT_USER = 
        new NiladicFunction(SQLKeyword.CURRENT_USER, ValueType.VARCHAR);

    /** Niladic function CURRENT_DATE. 
     *  AbstractType of this value expression is Types.DATE  
     */
    public static final NiladicFunction CURRENT_DATE = 
        new NiladicFunction(SQLKeyword.CURRENT_DATE, ValueType.DATE);
    
    /** Niladic function CURRENT_TIME. 
     *  AbstractType of this value expression is Types.TIME  
     */    
    public static final NiladicFunction CURRENT_TIME = 
        new NiladicFunction(SQLKeyword.CURRENT_TIME, ValueType.TIME);
    
    /** Niladic function CURRENT_TIMESTAMP. 
     *  AbstractType of this value expression is Types.TIMESTAMP  
     */        
    public static final NiladicFunction CURRENT_TIMESTAMP = 
        new NiladicFunction(SQLKeyword.CURRENT_TIMESTAMP, ValueType.TIMESTAMP);    
    
    /**
	 * No-argument constructor for GWT Serialization
	 */
	protected NiladicFunction() {
	}
    
    private NiladicFunction(SQLKeyword name, int dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public SQLKeyword getName() {        
        return this.name;
    }

    @Override
    public int getType() {
        return this.dataType;
    }

    @Override
    public Identifier getColumnName() {
        return null;
    }

    @Override
    public String getTerminalSymbol() {
        return null;
    }

    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
        this.name.traverse(vc, v);
    }
    
    
}
