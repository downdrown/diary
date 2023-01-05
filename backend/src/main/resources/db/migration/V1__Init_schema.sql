-- Initialize Schema
create table diary_users
(
    id identity not null primary key,
    username      varchar(500)  not null unique,
    firstname     varchar(1000) not null,
    lastname      varchar(1000) not null,
    birthdate     date,
    email         varchar(500)  not null unique,
    registered_at timestamp     not null default current_timestamp,
    last_login_at timestamp,
    password      varchar(1000) not null
)