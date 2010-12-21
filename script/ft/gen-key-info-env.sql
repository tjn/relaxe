
create schema feature_test
;

create table feature_test.test_generated_key
(
	abc serial,
	name varchar(20),
	primary key (abc)
)
;

