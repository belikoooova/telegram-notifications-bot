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
