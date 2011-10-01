

drop table if exists continent cascade
;

create table continent (
	id serial not null,
	name char(20) not null,
	primary key (id)
)
;

alter table continent add constraint ux_continent_name unique(name)
;

drop table if exists country
;

create table country (
	id serial not null,
  code char(3) not null,
  name char(52) not null,
  continent int null,
  continent_name char(20) null,
  region char(26) not null default '',
  surface_area decimal(10,2) null,
  indep_year integer default null,
  population integer not null default '0',
  life_expectancy decimal(3,1) default null,
  gnp decimal(10,2) default null,
  gnpold decimal(10,2) default null,
  localname char(45) not null default '',
  government_form char(45) not null default '',
  head_of_state char(60) default null,
  capital integer default null,
  constraint fk_country_continent foreign key (continent) references continent(id) on delete no action on update no action,
  primary key (id)
) 
;

alter table country add constraint ux_country_code unique(code)
;

alter table country add constraint ux_country_name unique(name)
;



