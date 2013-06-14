


ALTER TABLE samples.player ADD CONSTRAINT fk_player_game FOREIGN KEY (game) REFERENCES game(id)
;
ALTER TABLE samples.player ADD CONSTRAINT fk_player_user FOREIGN KEY (user_account) REFERENCES user_account(id)
;
ALTER TABLE samples.game ADD CONSTRAINT fk_game_initiator FOREIGN KEY (initiator) REFERENCES user_account(id)
;

ALTER TABLE samples.user_session ADD CONSTRAINT fk_user_session_user_account FOREIGN KEY (user_account) REFERENCES user_account(id)
;

