/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.feature;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ddl.BaseTableElement;
import com.appspot.relaxe.expr.ddl.ColumnDefinition;
import com.appspot.relaxe.expr.ddl.CreateTable;
import com.appspot.relaxe.expr.ddl.Int;
import com.appspot.relaxe.expr.ddl.Varchar;
import com.appspot.relaxe.meta.Environment;

/***
 * Helper class for creating table CreateTable -statements programmatically.
 * 
 * @author Administrator
 */

public class TableDesign {
        private Environment environment;
        private CreateTable table;
        
        TableDesign(Environment env, CreateTable table) {
            super();
            
            if (env == null) {
                throw new NullPointerException("'environment' must not be null");
            }
            
            if (table == null) {
                throw new NullPointerException("'table' must not be null");
            }
            
            this.environment = env;
            this.table = table;
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
            this.table.add(element);
        }
        
        public void varchar(String name, int len, boolean nullable) {
            add(new ColumnDefinition(name(name), Varchar.get(len)));
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
            add(new ColumnDefinition(name(name), new Int()));
        }
        
        public void integer(String name) {
            integer(name, false);
        }

        public CreateTable getTable() {
            return table;
        }
    }