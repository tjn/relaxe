

create table obj
(
  id integer not null,
  name varchar(10),
  type integer
)
;

alter table obj add constraint pk_obj primary key(id)
;


create table subobj
(
  obj int not null,
  index int not null,
  name varchar(10),
  primary key (obj, index)
)
;

alter table subobj add constraint fk_subobj_obj foreign key (obj) references obj (id)
;

create table subobjref
(
  obj int not null,
  ref int not null,
  index int not null,
  name varchar(10),
  primary key (obj, ref)
)
;

alter table subobjref add constraint fk_subobjref_obj foreign key (obj) references obj (id)
;

alter table subobjref add constraint fk_subobjref_subobj foreign key (obj, index) references subobj (obj, index)
;
