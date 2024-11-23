drop schema if exists bdpokemon_users_test;
create schema if not exists bdpokemon_users_test;
use bdpokemon_users_test;

create table users(
usuario varchar(100) not null,
contrasinal varchar(100) not null,

primary key(usuario)
) engine InnoDB;
