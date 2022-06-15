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
