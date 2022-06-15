-- Create admin teacher
-- Auth

-- admin:admin
insert into users(id, username, password, enabled)
    values (0, 'admin', '$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa', true);

insert into users_roles(user_id, role_id)
    values (0, 0);

insert into teachers(id, user_id, first_name, last_name, second_name, birthday)
    values (0, 0, 'Admin', 'Admin', 'Admin', '1970-01-01')