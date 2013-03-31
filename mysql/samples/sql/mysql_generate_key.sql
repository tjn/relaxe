
create schema if not exists samples
;

use samples
;

drop table if exists series
;

create table series
(
  schema_name varchar(30) not null,
  table_name varchar(30) not null,
  column_name varchar(30) not null,
  last_used_value int not null DEFAULT 0,
  modified_at timestamp DEFAULT CURRENT_TIMESTAMP,
  primary key (schema_name, table_name, column_name)
)
;

drop table if exists trigger_test
;

create table trigger_test (
	id integer,
	name varchar(100) not null,
	primary key (id)
)
ENGINE = InnoDB
;

DROP FUNCTION IF EXISTS generate_id_int
;

delimiter $$


CREATE FUNCTION generate_id_int(_schema_name varchar(60), _table_name varchar(60), _column_name varchar(60)) RETURNS INT
BEGIN
  DECLARE _x INT DEFAULT NULL;    
  	
  SET @_x = ( SELECT last_used_value FROM samples.series WHERE schema_name = _schema_name AND table_name = _table_name AND column_name = _column_name);
   
  IF @_x IS NULL THEN
		SET @_x = 1;
   	INSERT INTO samples.series(schema_name, table_name, column_name, last_used_value) VALUES (_schema_name, _table_name, _column_name, @_x);
  ELSE  	
    SET @_x = @_x + 1;
 	  UPDATE samples.series SET last_used_value = @_x WHERE schema_name = _schema_name AND table_name = _table_name AND column_name = _column_name;
  END IF;
    
  RETURN @_x;
END
$$


CREATE TRIGGER samples.tbi_trigger_test BEFORE INSERT ON samples.trigger_test FOR EACH ROW SET NEW.id = samples.generate_id_int('samples', 'trigger_test', 'id')
$$

-- call ins_abc;