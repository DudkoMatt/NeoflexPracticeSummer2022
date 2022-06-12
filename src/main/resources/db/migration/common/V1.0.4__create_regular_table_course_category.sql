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
