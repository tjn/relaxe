

create table tree
(
  id integer not null,
  root integer not null,
  parent integer null,
  name varchar(100) null
)
;

alter table tree add constraint pk_tree primary key(id)
;

alter table tree add constraint fk_tree_parent foreign key(parent) references tree(id)
;

alter table tree add constraint fk_tree_root foreign key(root) references tree(id)
;

alter table tree add constraint ux_name unique(name)
;
