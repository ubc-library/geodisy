PRAGMA foreign_keys=off;

BEGIN TRANSACTION;

alter table records RENAME TO _records_old;
create table if not exists records (
	record_id INTEGER PRIMARY KEY NOT NULL,
	repository_id INTEGER NOT NULL,
	title TEXT,pub_date TEXT,
	modified_timestamp INTEGER DEFAULT 0,
	source_url TEXT,
	deleted NUMERIC DEFAULT 0,
	local_identifier TEXT,
	series TEXT);

alter table records add column item_url TEXT;

insert into records (record_id, repository_id, title, modified_timestamp, source_url, deleted, local_identifier, series, item_url)
    select record_id, repository_id, title, modified_timestamp, source_url, deleted, local_identifier, series, item_url
    from _records_old;

drop table _records_old;
commit;

PRAGMA foreign_keys=on;
