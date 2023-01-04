-- Initialize Schema
create table diary_users
(
    id           bigint auto_increment,
    username     varchar(500)  not null unique,
    firstname    varchar(1000) not null,
    lastname     varchar(1000) not null,
    birthdate    date,
    email        varchar(500)  not null unique,
    registeredAt timestamp     not null default current_timestamp,
    lastLoginAt  timestamp,
    password     varchar(1000) not null
)