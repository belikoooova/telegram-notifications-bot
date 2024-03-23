--liquebase formatted sql

--changeset belikoooova:1
create type ChatState as enum ('NONE', 'AWAITING_TRACK_URL', 'AWAITING_UNTRACK_URL');

create table chat
(
    id              bigserial                   not null
                                                constraint pk_chat
                                                    primary key,
    state           ChatState                   not null
                                                default 'NONE',

    created_at      timestamp with time zone    not null,
    created_by      text                        not null
);

--changeset belikoooova:2
drop table chat;

create table chat_state
(
    id              bigserial                   not null
                                                constraint pk_chat
                                                    primary key,
    state           ChatState                   not null
                                                    default 'NONE'
);

--changeset belikoooova:3
drop table chat_state;

create table chat_state
(
    id              bigserial                   not null
        constraint pk_chat
            primary key,
    state           varchar(255)                not null
                                                    default 'NONE'
);
