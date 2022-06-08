-- Auth tables
create table users
(
    id       serial8     not null primary key,
    username varchar(50) not null unique check (username <> ''),
    password varchar(60) not null check (length(password) = 60),
    enabled  boolean     not null default true
);


create table roles
(
    id   serial8     not null primary key,
    name varchar(50) not null check (name <> '')
);

create table users_roles
(
    user_id int8 not null references users (id) on delete cascade,
    role_id int8 not null references roles (id) on delete cascade,

    primary key (user_id, role_id)
);

create index users_roles_user_id_index on users_roles (user_id);
create index users_roles_role_id_index on users_roles (role_id);


-- Regular tables
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


create table course_category
(
    id        serial8      not null primary key,
    title     varchar(255) not null unique check (title <> ''),
    parent_id int8 default 0,

    foreign key (parent_id) references course_category (id)
);

create index course_category_parent_id_index on course_category (parent_id);


-- Create default course category
insert into course_category(id, title, parent_id)
    values (0, 'Default Category', null);


-- Forbid modifying default course category
create function check_deletion_default_category() returns trigger as
$body$
begin
    if old.id = 0 then
        raise exception 'Cannot modify default course category';
    end if;
    return old;
end;
$body$ language plpgsql;

create function check_updating_default_category() returns trigger as
$body$
begin
    if old.id = 0 then
        raise exception 'Cannot modify default course category';
    end if;
    return new;
end;
$body$ language plpgsql;


create trigger check_deletion_default_category
    before delete
    on course_category
    for each row
execute procedure check_deletion_default_category();

create trigger check_updating_default_category
    before update
    on course_category
    for each row
execute procedure check_updating_default_category();

create table courses
(
    id            serial8      not null primary key,
    title         varchar(255) not null check (title <> ''),
    description   text         not null check (description <> ''),
    category_id   int8         not null,
    curator_id    int8         not null,
    student_count int8         not null default 0 check (student_count >= 0),
    type          jsonb        not null default '{
      "type": "base"
    }' check (type::json ->> 'type' <> ''),

    foreign key (curator_id) references teachers (id),
    foreign key (category_id) references course_category (id)
);

create index courses_category_id_index on courses (category_id);
create index courses_curator_id_index on courses (curator_id);


create table lessons
(
    id             serial8      not null primary key,
    teacher_id     int8         not null,
    course_id      int8         not null,
    title          varchar(255) not null check (title <> ''),
    description    text         not null check (description <> ''),
    start_datetime timestamp,
    end_datetime   timestamp check (start_datetime is null
                                        or end_datetime is null
                                        or start_datetime < end_datetime),

    foreign key (teacher_id) references teachers (id),
    foreign key (course_id) references courses (id)
);

create index lessons_teacher_id_index on lessons (teacher_id);
create index lessons_course_id_index on lessons (course_id);