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
package com.appspot.relaxe.pg.relaxetest;

import java.util.List;

import com.appspot.relaxe.ent.query.EntityQueryPredicate;
import com.appspot.relaxe.gen.pg.relaxetest.ent.pub.Tree;
import com.appspot.relaxe.pg.relaxetest.test.AbstractRelaxetestTestCase;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.EntitySession;

public class TreeTest
	extends AbstractRelaxetestTestCase {

	
	public void testToString() throws Exception {
		
		Tree.Mutable mt = newEntity(Tree.Type.TYPE);
		mt.setId(Integer.valueOf(1));
		mt.setRoot(mt);
		
		String s = mt.toString();
		logger().info("inserted: {}", s);
	}
	
	public void testTreeEntity() throws Exception {
		DataAccessSession das = newSession();
		testTreeEntity(das);
		das.commit();
		das.close();
	}
	
	
	public void testTreeEntity(DataAccessSession das) throws Exception {
		
				
		EntitySession es = das.asEntitySession();
		
		Tree.Mutable mt = newEntity(Tree.Type.TYPE);
		mt.setId(Integer.valueOf(2));			
		es.delete(mt.as());
		
		mt.setId(Integer.valueOf(1));			
		es.delete(mt.as());			
								
		mt.setRoot(mt);
		
		final Tree tree = mt.toImmutable();
				
		assertNotNull(tree);
		assertNotNull(tree.getRoot());		
		assertNotNull(tree.getRoot().value());		
		assertSame(tree, tree.getRoot().value());	
		assertNull(tree.getParent());
		
		Tree inserted = es.insert(tree);
		logger().info("inserted: {}", inserted);
		
		
		Tree.Mutable mn = newEntity(Tree.Type.TYPE);
		mn.setId(Integer.valueOf(2));			
		es.delete(mn.as());
		
		mn.setParent(tree);
		mn.setRoot(tree);
		
		Tree in = mn.toImmutable();
		assertNotNull(in.getParent());
		assertNotNull(in.getParent().value());
		assertSame(in.getParent().value(), in.getRoot().value());
		
		inserted = es.insert(in);
		logger().info("inserted: {}", inserted);		
	}
	
	
	public void testQuery() throws Exception {
		
		DataAccessSession das = newSession();		
		EntitySession es = das.asEntitySession();
		
		testTreeEntity(das);
				
		Tree.QueryElement.Builder reb = new Tree.QueryElement.Builder();
				
		Tree.QueryElement qe = reb.newQueryElement();
		reb.setQueryElement(Tree.PARENT, qe);
	
						
		EntityQueryPredicate qp = qe.self(Tree.ROOT);
		
		Tree.Query q = new Tree.Query(qe, qp);
		
		List<Tree> result = es.list(q, null);
		logger().info("result: {}", result.size());
		
		
				
		
		das.close();
	}	
	
}
