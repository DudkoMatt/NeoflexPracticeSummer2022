create table users
(
    id       serial8     not null primary key,
    username varchar(50) not null unique check (username <> ''),
    password varchar(60) not null check (length(password) = 60),
    enabled  boolean     not null default true
);
