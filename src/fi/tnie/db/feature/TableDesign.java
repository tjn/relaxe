/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.feature;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.ddl.BaseTableElement;
import fi.tnie.db.expr.ddl.ColumnDefinition;
import fi.tnie.db.expr.ddl.CreateTable;
import fi.tnie.db.expr.ddl.Int;
import fi.tnie.db.expr.ddl.Varchar;
import fi.tnie.db.meta.Environment;

/***
 * Helper class for creating table CreateTable -statements programmatically.
 * 
 * @author Administrator
 */

public class TableDesign {
        private Environment environment;
        private CreateTable table;
        
        TableDesign(Environment environment, CreateTable table) {
            super();
            
            if (environment == null) {
                throw new NullPointerException("'environment' must not be null");
            }
            
            if (table == null) {
                throw new NullPointerException("'table' must not be null");
            }
            
            this.environment = environment;
            this.table = table;
        }
        
        public void primaryKey(String... columns) {
//            for (String c : columns) {
//                
//            }
        }

        private Identifier name(String name) {        
            return this.environment.createIdentifier(name);
        }

        public void add(BaseTableElement element) {
            this.table.add(element);
        }
        
        public void varchar(String name, int len, boolean nullable) {
            add(new ColumnDefinition(name(name), Varchar.get(len)));
        }
        
        public void serial(String name) {
            ColumnDefinition cd = env().serialColumnDefinition(name, false);                
            add(cd);            
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