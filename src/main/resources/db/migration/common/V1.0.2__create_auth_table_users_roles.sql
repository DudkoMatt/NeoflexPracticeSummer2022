create table users_roles
(
    user_id int8 not null references users (id) on delete cascade,
    role_id int8 not null references roles (id) on delete cascade,

    primary key (user_id, role_id)
);

create index users_roles_user_id_index on users_roles (user_id);
create index users_roles_role_id_index on users_roles (role_id);
