create sequence hibernate_sequence start 4 increment 1;

create table user_role (
    user_id int4 not null,
    role_name varchar(32));

create table users (
    id int4 not null,
    login varchar(255) unique,
    name varchar(255),
    password varchar(255),
    primary key (id));

alter table user_role add constraint UserId_FK
    foreign key (user_id) references users;
