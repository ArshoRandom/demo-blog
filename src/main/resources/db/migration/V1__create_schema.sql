create table if not exists users
(
    id        int                not null primary key auto_increment,
    email     varchar(50) unique not null,
    password  varchar(255)       not null,
    name      varchar(255)       not null,
    last_name varchar(255)       not null,
    image     mediumblob         not null,
    active    tinyint            not null default 0
);

create table if not exists posts
(
    id         int                not null primary key auto_increment,
    title      varchar(255)       not null,
    anons      varchar(255)       not null,
    full_text  text               not null,
    creator_id int                not null,
    image      mediumblob         not null,
    created_at date               not null,
    constraint fk_creator foreign key (creator_id) references users(id) on delete cascade on update cascade
);


create table if not exists roles
(
    id   tinyint     not null primary key auto_increment,
    role varchar(50) not null unique
);

create table if not exists user_roles
(
    user_id int     not null,
    role_id tinyint not null,
    constraint user_id_foreign_key foreign key (user_id) references users (id),
    constraint role_id_foreign_key foreign key (role_id) references roles (id)

);
