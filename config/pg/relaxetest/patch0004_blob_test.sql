

create table blob_test
(
	id serial, 
	blob_oid bigint
)
;

alter table blob_test add constraint pk_blob_test primary key(id)
;