/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mysql.samples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.PredicateAttributeTemplate;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.mysql.MySQLImplementation;
import com.appspot.relaxe.env.mysql.MySQLPersistenceContext;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.gen.samples.ent.samples.Match;
import com.appspot.relaxe.gen.samples.ent.samples.Player;
import com.appspot.relaxe.gen.samples.ent.samples.UserAccount;
import com.appspot.relaxe.rpc.IntegerHolder;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.EntitySession;


public class MySQLSamplesDataAccessTest
	extends AbstractMySQLSamplesTestCase {
	
	private static Logger logger = Logger.getLogger(MySQLSamplesDataAccessTest.class);
		
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
		
		Match.QueryTemplate gq = new Match.QueryTemplate();
		gq.addAllAttributes();					
		gq.addPredicate(PredicateAttributeTemplate.eq(Match.Attribute.PHASE, "NEW"));
		
		UserAccount.QueryTemplate uq = new UserAccount.QueryTemplate();
		uq.add(UserAccount.ID);
		uq.add(UserAccount.USERNAME);		
		gq.setTemplate(Match.FK_GAME_INITIATOR, uq);
		
		FetchOptions options = new FetchOptions(10, 0);
		
		
		logger().debug("execute: game=" + gq);
		logger().debug("execute: user=" + uq);
				
		List<Match> games = es.load(gq, options);
				
		if (games.isEmpty()) {
			throw new RuntimeException("no sufficient test data available");			
		}
					
		PredicateAttributeTemplate<Match.Attribute> gp = getGamePredicate(games);
		
		{
			List<Player> pl = loadInitiatorPlayerList(es, gq, gp);		
			assertNotNull(pl);
			
			logger().debug("execute: pl.size()=" + pl.size());
			
			Map<Integer, Integer> km = new HashMap<Integer, Integer>();
			
			for (Player ip : pl) {
				assertNotNull(ip);
				Match game = ip.getMatch(Player.GAME).value();
				Integer gid = game.getContent().getId();
								
				assertFalse("duplicate game id: " + gid, km.containsKey(gid));
				
				km.put(gid, null);
								
				assertNotNull(game);
				UserAccount ua = ip.getUserAccount(Player.USER).value();
				assertNotNull(ua);
				
				UserAccount.Holder ih = game.getUserAccount(Match.FK_GAME_INITIATOR);
				assertNotNull(ih);
				UserAccount gi = ih.value();
				assertNotNull(gi);
				assertTrue(gi.match(UserAccount.ID, ua));
				
				// Integer iid = ua.getContent().getId();
				
				logger().debug("execute: game=" + game.getContent().getId());
				logger().debug("execute: player.ordinal=" + ip.getContent().getOrdinal());
				logger().debug("execute: player.user.id=" + ua.getContent().getId());
				logger().debug("execute: game.initiator.is=" + gi.getContent().getId());
//				logger().debug("execute: initiator=" + );
			}
		}
		
		
		
		
		
	}


	private PredicateAttributeTemplate<Match.Attribute> getGamePredicate(
			List<Match> games) {
		List<ValueExpression> gids = new ArrayList<ValueExpression>();
		
		for (Match game : games) {
			IntegerHolder id = game.getInteger(Match.ID);			
			gids.add(new IntLiteral(id.value().intValue()));
		}
		
		PredicateAttributeTemplate<Match.Attribute> gp = PredicateAttributeTemplate.in(Match.Attribute.ID, gids);
		return gp;
	}


	private List<Player> loadInitiatorPlayerList(EntitySession es, Match.QueryTemplate gq, PredicateAttributeTemplate<Match.Attribute> gp)
			throws EntityException {
		
		Player.QueryTemplate pq = new Player.QueryTemplate();
		pq.addAllAttributes();		
				
		UserAccount.QueryTemplate iq = new UserAccount.QueryTemplate();
		iq.add(UserAccount.USERNAME);
		
		Match.QueryTemplate gt = new Match.QueryTemplate();
		gt.addPredicate(gp);
			
		gt.setTemplate(Match.FK_GAME_INITIATOR, iq);		
		pq.setTemplate(Player.GAME, gt);		
		pq.setTemplate(Player.USER, iq);		
		
		gq.asc(Match.Attribute.ID);		
		pq.asc(Player.Attribute.ORDINAL);
		
						
		return es.load(pq, null);
	}

	@Override
	protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
		return new MySQLPersistenceContext();
	}
	
	
	
}
