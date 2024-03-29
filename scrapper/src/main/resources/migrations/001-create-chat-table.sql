--liquibase formatted sql

--changeset belikoooova:1
create table chat
(
    id              bigserial                   not null
                                                constraint pk_chat
                                                    primary key,

    created_at      timestamp with time zone    not null,
    created_by      text                        not null
);

--changeset belikoooova:2
drop table chat;

create table chat
(
    id              bigserial                   not null
                                                constraint pk_chat
                                                    primary key
);

