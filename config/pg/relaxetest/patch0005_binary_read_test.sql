create table file
(
	id serial,
	block_size integer not null,
	name varchar(64)	
)
;

alter table file add constraint pk_file primary key(id)
;

create table file_block
(
	id serial,
	file_id int not null,
	index integer not null,	
	content bytea not null 
)
;

alter table file_block add constraint pk_file_block primary key(id)
;

alter table file_block add constraint fk_file_block_file foreign key (file_id) references file(id) on delete cascade
;

alter table file_block add constraint uk_file_block_index unique (file_id, index)
;
