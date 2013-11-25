


set sql_mode = ansi
;

ALTER TABLE samples.player DROP FOREIGN KEY fk_player_match
;
ALTER TABLE samples.player DROP FOREIGN KEY fk_player_user
;
ALTER TABLE samples."match" DROP FOREIGN KEY fk_match_initiator
;
ALTER TABLE samples."match" DROP FOREIGN KEY fk_match_game
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