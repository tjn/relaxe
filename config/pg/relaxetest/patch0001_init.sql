

create schema if not exists meta
;

create table if not exists meta."schema"
(
  id serial,
  schema_name varchar(200),  
  primary key(id)
)
;

alter table meta."schema" add constraint ux_schema_name unique(schema_name)
;

create table if not exists meta."table"
(
  id serial,
   "schema" integer not null,
  schema_name varchar(200),  
  table_name varchar(200),  
  primary key(id)
)
;

alter table meta."table" add constraint ux_table_name unique(schema_name, table_name)
;
alter table meta."table" add constraint fk_table_name foreign key(schema_name) references meta."schema"(schema_name)
;
alter table meta."table" add constraint fk_table_schema foreign key("schema") references meta."schema"(id)
;



create table if not exists meta."column"
(
  id serial,
  "schema" integer not null,
  "table" integer not null,
  ordinal integer not null,
  schema_name varchar(200),  
  table_name varchar(200), 
  column_name varchar(200)
)
;

alter table meta."column" add constraint ux_column_name unique(schema_name, table_name, column_name)
;

alter table meta."column" add constraint fk_column_name foreign key(schema_name, table_name) references meta."table"(schema_name, table_name)
;

alter table meta."column" add constraint fk_column_table foreign key("table") references meta."table"(id)
;
