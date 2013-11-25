


use samples
;

set sql_mode = ANSI
;

ALTER TABLE "match" ADD CONSTRAINT fk_match_game FOREIGN KEY (game) REFERENCES game(id)
;
ALTER TABLE player ADD CONSTRAINT fk_player_match FOREIGN KEY ("match") REFERENCES "match"(id)
;
ALTER TABLE player ADD CONSTRAINT fk_player_user FOREIGN KEY (user_account) REFERENCES user_account(id)
;
ALTER TABLE "match" ADD CONSTRAINT fk_match_initiator FOREIGN KEY (initiator) REFERENCES user_account(id)
;
ALTER TABLE user_session ADD CONSTRAINT fk_user_session_user_account FOREIGN KEY (user_account) REFERENCES user_account(id)
;

