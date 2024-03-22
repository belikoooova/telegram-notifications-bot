create table link
(
    id              bigserial                   not null
                                                constraint pk_link
                                                    primary key,
    url             text                        not null,

    created_at      timestamp with time zone    not null,
    created_by      text                        not null
)
