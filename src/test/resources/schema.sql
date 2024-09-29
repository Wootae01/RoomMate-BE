drop table if exists notice CASCADE;
create table notice
(
    id bigint generated by default as identity,
    title varchar(200),
    content text,
    notice_date varchar(10),
    primary key (id)
);