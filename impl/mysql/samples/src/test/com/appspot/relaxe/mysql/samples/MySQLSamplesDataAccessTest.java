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
package com.appspot.relaxe.mysql.samples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.ent.query.EntityQueryPredicate;
import com.appspot.relaxe.ent.query.EntityQueryPredicates;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.mysql.MySQLImplementation;
import com.appspot.relaxe.env.mysql.MySQLPersistenceContext;
import com.appspot.relaxe.gen.mysql.samples.ent.samples.Game;
import com.appspot.relaxe.gen.mysql.samples.ent.samples.Match;
import com.appspot.relaxe.gen.mysql.samples.ent.samples.Player;
import com.appspot.relaxe.gen.mysql.samples.ent.samples.UserAccount;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.EntitySession;


public class MySQLSamplesDataAccessTest
	extends AbstractMySQLSamplesTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(MySQLSamplesDataAccessTest.class);
		
	private UnificationContext unificationContext = new SimpleUnificationContext();
	
	
		
    public void testQuery() 
	    throws Exception {
	    
    	DataAccessSession das = newSession();
    	
    	try {    	    	
    		execute(das);    	   	
    	}
    	finally {
    		das.close();
    	}	    
	}
    
    
	protected void execute(DataAccessSession das) throws Exception {
		
		EntitySession es = das.asEntitySession();
		
		Match.QueryElement.Builder gq = new Match.QueryElement.Builder();
		gq.addAllAttributes();
		
		UserAccount.QueryElement.Builder uqb = new UserAccount.QueryElement.Builder();
		uqb.add(UserAccount.ID);
		uqb.add(UserAccount.USERNAME);
		
		FetchOptions options = new FetchOptions(10, 0);
		
		UserAccount.QueryElement uq = uqb.newQueryElement();
		
		UserAccount user = null;
		
		{			
			List<UserAccount> ualist = es.load(new UserAccount.Query(uq), options);			
			user = ualist.isEmpty() ? newEntity(UserAccount.Type.TYPE) : ualist.get(0);
			
			if (!user.isIdentified()) {				
				user.setString(UserAccount.USERNAME, "user-" + System.currentTimeMillis());
				user.setString(UserAccount.PASSWORD, "secret");
				user = es.merge(user);
			}			
		}
		
		assertTrue(user.isIdentified());
		
		
		
		
//		es.load(q, opts)
//		es.query(query)
		
		
		// gq.addPredicate(PredicateAttributeTemplate.eq(Match.Attribute.PHASE, "NEW"));
		
		Match.Query.Builder b = new Match.Query.Builder(gq.newQueryElement());
		b.addPredicate(Match.PHASE, "NEW");
				
		gq.setQueryElement(Match.INITIATOR, uq);
		
		Match.Query qo = b.newQuery();
		
		
		logger().debug("execute: game=" + gq);
		logger().debug("execute: user=" + uq);
				
		List<Match> matches = es.load(qo, options);
		
		Game game = getGame(es, "pairs");
				
		if (matches.isEmpty()) {			
			insertMatch(es, game, user);
			insertMatch(es, game, user);
		}
		
		matches = es.load(qo, options);
				
		if (matches.isEmpty()) {
			throw new RuntimeException("no sufficient test data available");			
		}
		
		{
			List<Player> pl = loadInitiatorPlayerList(es, gq, matches);		
			assertNotNull(pl);
			
			logger().debug("execute: pl.size()=" + pl.size());
			
			Map<Integer, Integer> km = new HashMap<Integer, Integer>();
			
			// FIX : restore FK's
			
			for (Player ip : pl) {
				assertNotNull(ip);
				Match match = ip.getMatch(Player.MATCH).value();
				Integer mid = match.getContent().getId();
								
				assertFalse("duplicate game id: " + mid, km.containsKey(mid));
				
				km.put(mid, null);
								
				assertNotNull(game);
				UserAccount ua = ip.getUserAccount(Player.USER).value();
				assertNotNull(ua);
					
//				UserAccount.Holder ih = game.getUserAccount(Match.FK_GAME_INITIATOR);
//				assertNotNull(ih);
//				UserAccount gi = ih.value();
//				assertNotNull(gi);
//				assertTrue(gi.match(UserAccount.ID, ua));
				
				// Integer iid = ua.getContent().getId();
				
				logger().debug("execute: game=" + game.getContent().getId());
				logger().debug("execute: player.ordinal=" + ip.getContent().getOrdinal());
				logger().debug("execute: player.user.id=" + ua.getContent().getId());
//				logger().debug("execute: game.initiator.is=" + gi.getContent().getId());
//				logger().debug("execute: initiator=" + );
			}
		}
		
		
		
		
		
	}


	protected void insertMatch(EntitySession es, Game game, UserAccount user)
			throws EntityException {
		Match m = newEntity(Match.Type.TYPE);
		m.setUserAccount(Match.INITIATOR, user.ref());
		m.setGame(Match.GAME, game.ref());
		m.setString(Match.PHASE, "NEW");
				
		es.insert(m);
	}



	private List<Player> loadInitiatorPlayerList(EntitySession es, Match.QueryElement.Builder gq, List<Match> games)
			throws EntityException {
		
		Player.QueryElement.Builder pq = new Player.QueryElement.Builder();
		pq.addAllAttributes();		
				
		UserAccount.QueryElement.Builder iq = new UserAccount.QueryElement.Builder();
		iq.add(UserAccount.USERNAME);
		UserAccount.QueryElement ue = iq.newQueryElement();
		
		Match.QueryElement.Builder mb = new Match.QueryElement.Builder();
		Match.QueryElement me = mb.newQueryElement();
				
		pq.setQueryElement(Player.MATCH, me);
		pq.setQueryElement(Player.USER, ue);
			
				
		Player.Query.Builder qb = new Player.Query.Builder(pq.newQueryElement());
		
		if (!games.isEmpty()) {						
			List<EntityQueryPredicate> mps = new ArrayList<EntityQueryPredicate>();
			
			for (Match match : games) {			
				mps.add(qb.newPredicate(me, Match.ID, match));
			}
			
			EntityQueryPredicates.Or mp = new EntityQueryPredicates.Or(mps);
									
			qb.addPredicate(mp);
		}
		
		qb.asc(me, Match.ID);
		qb.asc(Player.ORDINAL);
				
		Player.Query qo = qb.newQuery();
						
		return es.load(qo, null);
	}

	@Override
	protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
		return new MySQLPersistenceContext();
	}

	
	private Game getGame(EntitySession es, String name) throws EntityException {
				
		Game.QueryElement qe = new Game.QueryElement(Game.Type.TYPE.getMetaData().attributes());
		Game.Query.Builder qb = new Game.Query.Builder(qe);
				
		qb.addPredicate(qe.newEquals(Game.NAME, name));
				
		List<Game> gl = es.load(qb.newQuery(), null);			
		Game game = gl.isEmpty() ? newEntity(Game.Type.TYPE) : gl.get(0);
		
		if (!game.isIdentified()) {				
			game.setString(Game.NAME, name);
			game = es.merge(game);
		}			
		
		return game;		
	}
	
	
}
