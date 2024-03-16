--liquibase formatted sql

--changeset belikoooova:1
create table link
(
    id              bigserial                   not null
                                                constraint pk_link
                                                    primary key,
    url             text                        not null,

    created_at      timestamp with time zone    not null,
    created_by      text                        not null
);

--changeset belikoooova:2
drop table link;

create table link
(
    id              bigserial                   not null
                                                constraint pk_link
                                                    primary key,
    url             text                        not null
);

--changeset belikoooova:3
alter table link
add column last_checked_at timestamp with time zone;
