create table teachers
(
    id          serial8      not null primary key,
    user_id     int8         not null unique,
    first_name  varchar(255) not null check (first_name <> ''),
    last_name   varchar(255) not null check (last_name <> ''),
    second_name varchar(255) not null default '',
    birthday    date         not null check (birthday < current_date),

    foreign key (user_id) references users (id)
);


create or replace function remove_user_on_deleting_connection_function()
    returns trigger as
$BODY$
begin
    delete from users as u
    where u.id = old.user_id;

    return old;
end;
$BODY$
    language plpgsql;


create trigger remove_user_on_deleting_connection
    after delete on teachers for each row
execute procedure remove_user_on_deleting_connection_function();
