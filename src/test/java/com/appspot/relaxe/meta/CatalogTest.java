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
package com.appspot.relaxe.meta;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashSet;

import com.appspot.relaxe.env.IdentifierRules;
import com.appspot.relaxe.expr.DelimitedIdentifier;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.IllegalIdentifierException;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.Constraint;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.SchemaElementMap;
import com.appspot.relaxe.meta.SchemaMap;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rdbms.Implementation;



public abstract class CatalogTest<I extends Implementation<I>> 
    extends DBMetaTestCase<I> {

    
    protected void testBaseTable(BaseTable t) {
    			
    	assertNotNull(t);
    	assertNotNull(t.getName());		
    	assertNotNull(t.getName().getUnqualifiedName());				
    	assertNotNull(t.getQualifiedName());
    			
    	assertFalse(t.getQualifiedName().trim().equals(""));
    	logger().warn("table: " + t.getQualifiedName());
    	
    	assertNotNull(t.getUnqualifiedName());		
    	assertNotNull(t.getUnqualifiedName().getContent());
    	assertTrue(t.getQualifiedName().contains(t.getUnqualifiedName().getContent()));
    			
//    	assertNotNull(t.getSchema());
    	
    	assertNotNull(t.getName());
    	assertNotNull(t.getName().getQualifier());
    	assertNotNull(t.getName().getQualifier().getSchemaName());
    	assertNotNull(t.getName().getQualifier().getSchemaName().getContent());
    	assertNotNull(t.getQualifiedName());    	
    			
    	PrimaryKey pk = t.getPrimaryKey();	
    	
    	if (pk == null) {
    		logger().warn("no primary key in table: " + t.getQualifiedName());
    	}
    	else {
    		testPrimaryKey(pk);
    		assertNotNull(pk.getTable());
    		assertSame(pk.getTable(), t);			
    	}
    			
    	SchemaElementMap<ForeignKey> fks = t.foreignKeys();
    	assertNotNull(fks);
    	assertNotNull(fks.values());
    	
    	for (ForeignKey fk : fks.values()) {
    		testForeignKey(fk);
    	}
    }

    public void testBaseTables() throws Exception {		
    	SchemaMap sm = getCatalog().schemas();
    	assertNotNull(sm);
    	
    	for (Schema s : sm.values()) {			
    		for (BaseTable t : s.baseTables().values()) {				
    			testBaseTable(t);				
    		}
    	}
    }

    public void testColumns() throws Exception {
    		BaseTable t = getFilmTable(getCatalog());
    		
    		assertNotNull(t.getColumnMap());
//    		assertNotNull(t.columns());
    		
    		assertTrue(t.getColumnMap().keySet().size() > 1);
//    		assertTrue(t.columns().size() > 1);
    		
//    		PrimaryKey pk = t.getPrimaryKey();
    //		pk.getColumn(name)
    					
    		for (Column c : t.getColumnMap().values()) {
    //			System.err.println(c.getUnqualifiedName() + ": " + c.getClass());
    			assertNotNull(c);			
    			assertNotNull(c.getColumnName());
    			assertNotNull(c.getDataType());
    			assertNotNull(c.getDataType().getTypeName());			
    			assertNotNull(c.getUnqualifiedName());
    						
    		}
    	}

    public void testConstraints() throws Exception {
    			
    	SchemaMap sm = getCatalog().schemas();
    	assertNotNull(sm);
    	
    	assertFalse(sm.values().isEmpty());
    	
    	for (Schema s : sm.values()) {
    		SchemaElementMap<? extends Constraint> cm = s.constraints();
    					
    		for (PrimaryKey pk : s.primaryKeys().values()) {
    			assertSame(pk, cm.get(pk.getUnqualifiedName()));
    		}
    		
    		for (ForeignKey fk : s.foreignKeys().values()) {
    			assertSame(fk, cm.get(fk.getUnqualifiedName()));
    		}
    	
    		HashSet<Constraint> cs = new HashSet<Constraint>(cm.values());
    		HashSet<Constraint> pks = new HashSet<Constraint>(s.primaryKeys().values());
    		HashSet<Constraint> fks = new HashSet<Constraint>(s.foreignKeys().values());
    		
    		HashSet<Constraint> all = new HashSet<Constraint>();
    		all.addAll(pks);
    		all.addAll(fks);
    	
    		cs.equals(all);
    	}
    	
    	
    }

    public void testForeignKeys() throws Exception {		
    	SchemaMap sm = getCatalog().schemas();
    	assertNotNull(sm);		
    	
    	for (Schema s : sm.values()) {
    		if (s.getUnqualifiedName().equals(id(SCHEMA_PUBLIC))) {
    			assertFalse(s.foreignKeys().values().isEmpty());
    		}
    		
    		for (ForeignKey fk : s.foreignKeys().values()) {
    			testForeignKey(fk);				
    		}
    	}
    }

    public void testIdentifierComparator() 
    	throws SQLException {
    	
    	Implementation<?> env = getEnvironmentContext().getImplementation();
    	IdentifierRules ir = env.environment().getIdentifierRules();
    	Comparator<Identifier> icmp = ir.comparator();
    		
    	{
    		final Identifier a = ir.toIdentifier("abc");
    		assertTrue(a.isOrdinary());
    		
    		// test PostgreSQL-specific identifier folding:
    		final Identifier b = new DelimitedIdentifier("abc");
    		assertTrue(icmp.compare(a, b) == 0);
    		
    		final Identifier c = new DelimitedIdentifier("ABC");
    		assertTrue(icmp.compare(a, c) != 0);
    		
    		assertTrue(icmp.compare(b, c) != 0);
    		
    		assertTrue(icmp.compare(b, new DelimitedIdentifier(b.getContent())) == 0);
    		assertTrue(icmp.compare(c, new DelimitedIdentifier(c.getContent())) == 0);
    	}			
    }

    public void testPrimaryKeys() throws Exception {		
    	SchemaMap sm = getCatalog().schemas();
    	assertNotNull(sm);
    	
    	assertFalse(sm.values().isEmpty());
    	
    	for (Schema s : sm.values()) {
    		if (s.getUnqualifiedName().equals(id(SCHEMA_PUBLIC))) {
    			assertFalse(s.primaryKeys().values().isEmpty());
    		}
    		
    		for (PrimaryKey pk : s.primaryKeys().values()) {
    			testPrimaryKey(pk);
    		}
    	}
    }

    protected Identifier id(String name) 
    	throws IllegalIdentifierException, NullPointerException, QueryException, SQLException {
    	return getCatalog().getEnvironment().getIdentifierRules().toIdentifier(name);
    }
}
