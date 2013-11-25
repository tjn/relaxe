/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
/**
 * 
 */
package com.appspot.relaxe.feature;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.ddl.BaseTableElement;
import com.appspot.relaxe.expr.ddl.ColumnDefinition;
import com.appspot.relaxe.expr.ddl.CreateTable;
import com.appspot.relaxe.expr.ddl.types.IntTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.VarcharTypeDefinition;
import com.appspot.relaxe.meta.Environment;

/***
 * Helper class for creating table CreateTable -statements programmatically.
 * 
 * @author Administrator
 */

public class TableDesign {
        private Environment environment;
        private CreateTable.Builder builder;
        
        TableDesign(Environment env, SchemaElementName table) {
            super();
            
            if (env == null) {
                throw new NullPointerException("'environment' must not be null");
            }
            
            if (table == null) {
                throw new NullPointerException("'table' must not be null");
            }
            
            this.environment = env;
            this.builder = new CreateTable.Builder(table);
        }
        
        public CreateTable newCreateTable() {
        	return builder.newCreateTable();
        }
        
        public void primaryKey(String... columns) {
//            for (String c : columns) {
//                
//            }
        }

        private Identifier name(String name) {        
            return this.environment.getIdentifierRules().toIdentifier(name);
        }

        public void add(BaseTableElement element) {
            this.builder.add(element);
        }
        
        public void varchar(String name, int len, boolean nullable) {
            add(new ColumnDefinition(name(name), VarcharTypeDefinition.get(len)));
        }
        
        public void serial(String name) {
//        	TODO: FIX ME        	
//            ColumnDefinition cd = env().serialColumnDefinition(name, false);                
//            add(cd);            
        }
        
        public Environment env() {
            return this.environment;
        }
        
        public void varchar(String name, int len) {
            varchar(name, len, false);
        }
        
        public void integer(String name, boolean nullable) {
            add(new ColumnDefinition(name(name), IntTypeDefinition.DEFINITION));
        }
        
        public void integer(String name) {
            integer(name, false);
        }        
    }