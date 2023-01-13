-- Initialize Schema
create table diary_users
(
    id identity primary key not null,
    username      varchar(500) unique not null,
    firstname     varchar(1000)       not null,
    lastname      varchar(1000)       not null,
    birthdate     date,
    email         varchar(500) unique not null,
    registered_at timestamp           not null default current_timestamp,
    last_login_at timestamp,
    password      varchar(1000)       not null
);

create table diary_mood_check_in
(
    id identity primary key not null,
    fk_user_id     int       not null,
    score          int       not null,
    check_in_point timestamp not null,
    comment        varchar(5000),
    created_at     timestamp not null default current_timestamp,
    constraint fk_mood_check_in_user foreign key (fk_user_id) references diary_users (id) on delete cascade,
    constraint chk_mood_check_in_score check (score in (1, 2, 3, 4, 5))
);
create index mood_check_in_score on diary_mood_check_in (score);

-- Spring Related Tables
create table persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);
