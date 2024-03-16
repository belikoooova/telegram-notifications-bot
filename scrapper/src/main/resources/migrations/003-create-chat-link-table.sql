--liquibase formatted sql

--changeset belikoooova:1
create table chat_link
(
    chat_id         bigint,
    link_id         bigint,
    constraint      pk_chat_link
        primary key (chat_id, link_id),
    constraint      fk_chat_link_chat
        foreign key (chat_id)
        references chat (id)
        on delete cascade,
    constraint      fk_chat_link_link
        foreign key (link_id)
        references link (id)
        on delete cascade
);
