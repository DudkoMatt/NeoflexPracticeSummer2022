create table roles
(
    id   serial8     not null primary key,
    name varchar(50) not null check (name <> '')
);
