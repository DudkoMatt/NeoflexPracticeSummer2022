insert into course_category(id, title, parent_id) values (1, 'Programming', 0);
insert into course_category(id, title, parent_id) values (2, 'Java', 1);
insert into course_category(id, title, parent_id) values (3, 'Python', 1);
insert into course_category(id, title, parent_id) values (4, 'Data Science', 0);
insert into course_category(id, title, parent_id) values (5, 'Python ML', 4);

-- user:user
insert into users(id, username, password, enabled)
    values (1, 'user', '$2a$12$DC6JhVUEHWVjmclCHIod8.KWGRpNdDbSMhYSlQT1d.E2WsldvaxpO', true);

insert into users_roles(user_id, role_id)
    values (1, 1);

insert into teachers(id, user_id, first_name, last_name, second_name, birthday)
    values (1, 1, 'Name', 'Surname', 'Second-Name', '2000-01-01');


insert into courses(id, title, description, category_id, curator_id, student_count, type)
    values (1, 'Base Course', 'Base Course', 0, 1, 0, '{"type": "base"}');

insert into courses(id, title, description, category_id, curator_id, student_count, type)
    values (2, 'Programming Course', 'Programming Course', 1, 1, 0, '{"type": "base"}');

insert into courses(id, title, description, category_id, curator_id, student_count, type)
    values (3, 'Java Course', 'Java Course', 2, 1, 0, '{"type": "base"}');

insert into courses(id, title, description, category_id, curator_id, student_count, type)
    values (4, 'Python Course', 'Python Course', 3, 1, 0, '{"type": "base"}');

insert into courses(id, title, description, category_id, curator_id, student_count, type)
    values (5, 'Data Science Course', 'Data Science Course', 4, 1, 0, '{"type": "offline", "universityName": "ITMO", "address": "Kronverksky Pr. 49"}');

insert into courses(id, title, description, category_id, curator_id, student_count, type)
    values (6, 'Python ML Course', 'Python ML Course', 5, 1, 0, '{"type": "online", "lessonUrl": "http://some.url"}');


insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
    values (1, 1, 3, 'First lesson', 'First lesson description', '2022-05-19T08:00:00Z', '2022-05-19T09:00:00Z');

insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
    values (2, 1, 3, 'Second lesson', 'Second lesson description', '2022-05-19T09:00:00Z', '2022-05-19T10:00:00Z');

insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
    values (3, 1, 4, 'Another lesson', 'Another lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');

insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
    values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');

-- Update sequence id
SELECT setval('users_id_seq', (select max(id) from users));
SELECT setval('roles_id_seq', (select max(id) from roles));
SELECT setval('course_category_id_seq', (select max(id) from course_category));
SELECT setval('courses_id_seq', (select max(id) from courses));
SELECT setval('teachers_id_seq', (select max(id) from teachers));
SELECT setval('lessons_id_seq', (select max(id) from lessons));