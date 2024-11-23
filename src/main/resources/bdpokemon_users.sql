drop schema if exists bdpokemon_users;
create schema if not exists bdpokemon_users;
use bdpokemon_users;

create table users(
usuario varchar(100) not null,
contrasinal varchar(100) not null,

primary key(usuario)
) engine InnoDB;
