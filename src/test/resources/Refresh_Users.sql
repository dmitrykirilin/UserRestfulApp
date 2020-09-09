delete from user_role;
delete from users;
drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start 4 increment 1;
insert into users (id, name, login, password)
values(1, 'admin', 'admin', '1A'),
      (2, 'Vasya', 'Vasya', '2B'),
      (3, 'Petya', 'Petya', '3B');

insert into user_role (user_id, role_name)
values(1, 'ADMIN'), (1, 'OPERATOR'), (1, 'STATISTIC'),
       (2, 'OPERATOR'), (2, 'STATISTIC'), (3, 'STATISTIC');