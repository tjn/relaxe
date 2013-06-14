


ALTER TABLE samples.player DROP FOREIGN KEY fk_player_game
;
ALTER TABLE samples.player DROP FOREIGN KEY fk_player_user
;
ALTER TABLE samples.game DROP FOREIGN KEY fk_game_initiator
;
ALTER TABLE samples.user_session DROP FOREIGN KEY fk_user_session_user_account
;

  

-- ALTER TABLE pairs.pairs_card DROP FOREIGN KEY fk_card_content
-- ;
-- ALTER TABLE pairs.pairs_card DROP FOREIGN KEY fk_card_game
-- ;
-- 
-- ALTER TABLE pairs.pairs_game_event DROP FOREIGN KEY fk_event_game
-- ;
-- ALTER TABLE pairs.pairs_game_event DROP FOREIGN KEY fk_event_player
-- ;
-- ALTER TABLE pairs.pairs_game_event DROP FOREIGN KEY fk_event_card
-- ;