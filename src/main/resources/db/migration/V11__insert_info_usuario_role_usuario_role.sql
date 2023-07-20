insert into usuario (nome, email, password) values ('admin', 'admin@email.com', '$2a$12$U6kF0pEurfYlnWIXweAOL.ruyD5qzTlKKKuKQqkKyWrfKs0BBXfgK');
insert into role (nome) values ('ADMIN');
insert into usuario_role (usuario_id, role_id) values (2, 2);