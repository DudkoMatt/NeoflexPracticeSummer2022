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
