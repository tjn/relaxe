
-- CREATE ROLE samples LOGIN PASSWORD 'password' VALID UNTIL 'infinity'
-- ;


-- CREATE DATABASE samples OWNER samples
-- ;


-- drop table eee
-- ;

create table eee (
  id integer,
  name varchar(20)
)  
;


alter table eee add constraint eee1 check(id > 0)
;


alter table eee add constraint eee1 check(id < 5)
;;

create unique index eee on eee(id)
;;



-- create schema public
-- ;;

-- show all
-- ;;
-- 
-- show search_path
-- ;;
-- drop table abc
-- ;
-- create table abc
-- (
--   "ID" INTEGER,
--   "name" VARCHAR(20),
--   "Full Name" VARCHAR(20),
--   name2  VARCHAR(20),
--   NAME3  VARCHAR(20)
-- )
-- ;;

select * from information_schema.columns
;;

select * from information_schema.columns
where 
table_name = 'abc'
;;


select * from information_schema.tables
w
;;


table_privileges

