create table categories
(
    id          bigint generated always as identity
        primary key,
    name        varchar(100)                        not null,
    description varchar(500),
    created_at  timestamp default CURRENT_TIMESTAMP not null,
    updated_at  timestamp default current_timestamp not null,
    constraint categories_name_pk unique (name)
);
