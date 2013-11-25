-- MySQL dump 10.13  Distrib 5.1.48, for Win64 (unknown)
--


CREATE SCHEMA IF NOT EXISTS samples
;

USE samples
;

SET sql_mode = ANSI
;
-- 
-- -- drop table player
-- -- ;;
-- drop table "match"
-- ;;

-- drop table user_account
-- ;;
-- drop table  user_session
-- ;;

-- show tables
-- ;;

-- DROP TABLE IF EXISTS user_account
-- ;

CREATE TABLE IF NOT EXISTS user_account (
  id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(40) NOT NULL,
  password varchar(40) NOT NULL,
  session_token varchar(32) DEFAULT NULL,
  channel_token varchar(100) DEFAULT NULL,
  login_at varchar(23) DEFAULT NULL,
  logout_at varchar(23) DEFAULT NULL,
  last_activity_at varchar(23) DEFAULT NULL,
  request_count int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (id)
) 
ENGINE = InnoDB 
;


-- DROP TABLE IF EXISTS user_session
-- ;

CREATE TABLE IF NOT EXISTS user_session (
  id int(11) NOT NULL AUTO_INCREMENT,
  started_at varchar(23) DEFAULT NULL,
  user_account int(11) NOT NULL,
  PRIMARY KEY (id)
) 
ENGINE = InnoDB
;

CREATE TABLE IF NOT EXISTS game (
  id int(11) NOT NULL AUTO_INCREMENT,    
  name varchar(64) DEFAULT NULL,  
  modified_at char(23) default null,
  PRIMARY KEY (id)
) 
ENGINE=InnoDB 
;

-- DROP TABLE IF EXISTS play
-- ;

CREATE TABLE IF NOT EXISTS "match" (
  id int(11) NOT NULL AUTO_INCREMENT,
  initiator int(11) NOT NULL,
  game int NOT NULL,
  name varchar(64) DEFAULT NULL,  
  phase varchar(10) NOT NULL,
  modified_at char(23) default null,
  PRIMARY KEY (id)
) 
ENGINE=InnoDB 
;

-- DROP TABLE IF EXISTS player
-- ;

CREATE TABLE IF NOT EXISTS player (  
  "match" int(11) NOT NULL,
  ordinal int(11) NOT NULL,
  user_account int(11) NOT NULL,  
  modified_at varchar(23) DEFAULT NULL,
  PRIMARY KEY ("match", ordinal)
) 
ENGINE=InnoDB
;
