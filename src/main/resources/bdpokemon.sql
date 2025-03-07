DROP DATABASE IF EXISTS BDPOKEMON;
CREATE DATABASE IF NOT EXISTS BDPOKEMON;
USE BDPOKEMON;

DROP TABLE IF EXISTS POKEMONS;
CREATE TABLE IF NOT EXISTS POKEMONS(
	ID INT UNSIGNED NOT NULL,
    NOMBRE VARCHAR(20) NOT NULL,
    IMAGEN BLOB NULL,
    IMAGEN_SHINY BLOB NULL,
    GIF BLOB NULL,
    TIPO_1 VARCHAR(15) NOT NULL,
    TIPO_2 VARCHAR(15) NULL,
    /************************************/
    PRIMARY KEY (ID),
    UNIQUE INDEX AK_NOMBRE(NOMBRE),
    /***********RELACION EVOLUCIONAR A *************/
    EVOLUCIONA_DE INT UNSIGNED NULL,
    METODO_EVOLUCION VARCHAR(50) NULL,
    FOREIGN KEY (EVOLUCIONA_DE) REFERENCES POKEMONS(ID)
		ON DELETE SET NULL
        ON UPDATE CASCADE,
	INDEX FK_EVOLUCIONA_DE(EVOLUCIONA_DE) 
    
)ENGINE INNODB;

DROP TABLE IF EXISTS RUTAS;
CREATE TABLE IF NOT EXISTS RUTAS(
	ID INT UNSIGNED auto_increment NOT NULL,
    NOMBRE VARCHAR(50) NOT NULL,
    REGION VARCHAR(20) NOT NULL,
    /************************************/
    PRIMARY KEY(ID),
    UNIQUE INDEX AK_NOMBRE_REGION(NOMBRE,REGION)
)ENGINE INNODB;

DROP TABLE IF EXISTS RUTAS_POKEMONS;
CREATE TABLE IF NOT EXISTS RUTAS_POKEMONS(
	POKEMON INT UNSIGNED NOT NULL,
    RUTA INT UNSIGNED NOT NULL,
    NIVEL_MINIMO INT not NULL,
    NIVEL_MAXIMO INT not NULL,
    /********************************/
    PRIMARY KEY(POKEMON,RUTA),
    FOREIGN KEY (POKEMON) REFERENCES POKEMONS(ID)
		ON DELETE CASCADE 
        ON UPDATE CASCADE,
    INDEX FK_POKEMON_CON_RUTA(POKEMON),
    FOREIGN KEY(RUTA) REFERENCES RUTAS(ID)
		ON DELETE CASCADE
        ON UPDATE CASCADE,
	INDEX FK_RUTA_CON_POKEMON(RUTA)
    
)engine InnoDB;

INSERT INTO POKEMONS (ID, NOMBRE, TIPO_1, TIPO_2, EVOLUCIONA_DE, METODO_EVOLUCION) VALUES
(1, 'bulbasaur', 'Planta', 'Veneno', NULL, NULL),
(2, 'ivysaur', 'Planta', 'Veneno', 1, 'Nivel 16'),
(3, 'venusaur', 'Planta', 'Veneno', 2, 'Nivel 32'),
(4, 'charmander', 'Fuego', NULL, NULL, NULL),
(5, 'charmeleon', 'Fuego', NULL, 4, 'Nivel 16'),
(6, 'charizard', 'Fuego', 'Volador', 5, 'Nivel 36'),
(7, 'squirtle', 'Agua', NULL, NULL, NULL),
(8, 'wartortle', 'Agua', NULL, 7, 'Nivel 16'),
(9, 'blastoise', 'Agua', NULL, 8, 'Nivel 36'),
(10, 'caterpie', 'Bicho', NULL, NULL, NULL),
(11, 'metapod', 'Bicho', NULL, 10, 'Nivel 7'),
(12, 'butterfree', 'Bicho', 'Volador', 11, 'Nivel 10'),
(13, 'weedle', 'Bicho', 'Veneno', NULL, NULL),
(14, 'kakuna', 'Bicho', 'Veneno', 13, 'Nivel 7'),
(15, 'beedrill', 'Bicho', 'Veneno', 14, 'Nivel 10'),
(16, 'pidgey', 'Normal', 'Volador', NULL, NULL),
(17, 'pidgeotto', 'Normal', 'Volador', 16, 'Nivel 18'),
(18, 'pidgeot', 'Normal', 'Volador', 17, 'Nivel 36'),
(19, 'rattata', 'Normal', NULL, NULL, NULL),
(20, 'raticate', 'Normal', NULL, 19, 'Nivel 20'),
(21, 'spearow', 'Normal', 'Volador', NULL, NULL),
(22, 'fearow', 'Normal', 'Volador', 21, 'Nivel 20'),
(23, 'ekans', 'Veneno', NULL, NULL, NULL),
(24, 'arbok', 'Veneno', NULL, 23, 'Nivel 22'),
(25, 'pikachu', 'Eléctrico', NULL, NULL, NULL),
(26, 'raichu', 'Eléctrico', NULL, 25, 'Piedra Trueno'),
(27, 'sandshrew', 'Tierra', NULL, NULL, NULL),
(28, 'sandslash', 'Tierra', NULL, 27, 'Nivel 22'),
(29, 'nidoran-f', 'Veneno', NULL, NULL, NULL),
(30, 'nidorina', 'Veneno', NULL, 29, 'Nivel 16'),
(31, 'nidoqueen', 'Veneno', 'Tierra', 30, 'Piedra Lunar'),
(32, 'nidoran-m', 'Veneno', NULL, NULL, NULL),
(33, 'nidorino', 'Veneno', NULL, 32, 'Nivel 16'),
(34, 'nidoking', 'Veneno', 'Tierra', 33, 'Piedra Lunar'),
(35, 'clefairy', 'Hada', NULL, NULL, NULL),
(36, 'clefable', 'Hada', NULL, 35, 'Piedra Lunar'),
(37, 'vulpix', 'Fuego', NULL, NULL, NULL),
(38, 'ninetales', 'Fuego', NULL, 37, 'Piedra Fuego'),
(39, 'jigglypuff', 'Normal', 'Hada', NULL, NULL),
(40, 'wigglytuff', 'Normal', 'Hada', 39, 'Piedra Lunar'),
(41, 'zubat', 'Veneno', 'Volador', NULL, NULL),
(42, 'golbat', 'Veneno', 'Volador', 41, 'Nivel 22'),
(43, 'oddish', 'Planta', 'Veneno', NULL, NULL),
(44, 'gloom', 'Planta', 'Veneno', 43, 'Nivel 21'),
(45, 'vileplume', 'Planta', 'Veneno', 44, 'Piedra Hoja'),
(46, 'paras', 'Bicho', 'Planta', NULL, NULL),
(47, 'parasect', 'Bicho', 'Planta', 46, 'Nivel 24'),
(48, 'venonat', 'Bicho', 'Veneno', NULL, NULL),
(49, 'venomoth', 'Bicho', 'Veneno', 48, 'Nivel 31'),
(50, 'diglett', 'Tierra', NULL, NULL, NULL),
(51, 'dugtrio', 'Tierra', NULL, 50, 'Nivel 26'),
(52, 'meowth', 'Normal', NULL, NULL, NULL),
(53, 'persian', 'Normal', NULL, 52, 'Nivel 28'),
(54, 'psyduck', 'Agua', NULL, NULL, NULL),
(55, 'golduck', 'Agua', NULL, 54, 'Nivel 33'),
(56, 'mankey', 'Lucha', NULL, NULL, NULL),
(57, 'primeape', 'Lucha', NULL, 56, 'Nivel 28'),
(58, 'growlithe', 'Fuego', NULL, NULL, NULL),
(59, 'arcanine', 'Fuego', NULL, 58, 'Piedra Fuego'),
(60, 'poliwag', 'Agua', NULL, NULL, NULL),
(61, 'poliwhirl', 'Agua', NULL, 59, 'Nivel 25'),
(62, 'poliwrath', 'Agua', 'Lucha', 61, 'Piedra Agua'),
(63, 'abra', 'Psíquico', NULL, NULL, NULL),
(64, 'kadabra', 'Psíquico', NULL, 63, 'Nivel 16'),
(65, 'alakazam', 'Psíquico', NULL, 64, 'Intercambio'),
(66, 'machop', 'Lucha', NULL, NULL, NULL),
(67, 'machoke', 'Lucha', NULL, 66, 'Nivel 28'),
(68, 'machamp', 'Lucha', NULL, 67, 'Intercambio'),
(69, 'bellsprout', 'Planta', 'Veneno', NULL, NULL),
(70, 'weepinbell', 'Planta', 'Veneno', 69, 'Nivel 21'),
(71, 'victreebel', 'Planta', 'Veneno', 70, 'Piedra Hoja'),
(72, 'tentacool', 'Agua', 'Veneno', NULL, NULL),
(73, 'tentacruel', 'Agua', 'Veneno', 72, 'Nivel 30'),
(74, 'geodude', 'Roca', 'Tierra', NULL, NULL),
(75, 'graveler', 'Roca', 'Tierra', 74, 'Nivel 25'),
(76, 'golem', 'Roca', 'Tierra', 75, 'Intercambio'),
(77, 'ponyta', 'Fuego', NULL, NULL, NULL),
(78, 'rapidash', 'Fuego', NULL, 77, 'Nivel 40'),
(79, 'slowpoke', 'Agua', 'Psíquico', NULL, NULL),
(80, 'slowbro', 'Agua', 'Psíquico', 79, 'Nivel 37'),
(81, 'magnemite', 'Eléctrico', 'Acero', NULL, NULL),
(82, 'magneton', 'Eléctrico', 'Acero', 81, 'Nivel 30'),
(83, 'farfetchd', 'Normal', 'Volador', NULL, NULL),
(84, 'doduo', 'Normal', 'Volador', NULL, NULL),
(85, 'dodrio', 'Normal', 'Volador', 84, 'Nivel 31'),
(86, 'seel', 'Agua', NULL, NULL, NULL),
(87, 'dewgong', 'Agua', 'Hielo', 86, 'Nivel 34'),
(88, 'grimer', 'Veneno', NULL, NULL, NULL),
(89, 'muk', 'Veneno', NULL, 88, 'Nivel 38'),
(90, 'shellder', 'Agua', NULL, NULL, NULL),
(91, 'cloyster', 'Agua', 'Hielo', 87, 'Nivel 30'),
(92, 'gastly', 'Fantasma', 'Veneno', NULL, NULL),
(93, 'haunter', 'Fantasma', 'Veneno', 92, 'Nivel 25'),
(94, 'gengar', 'Fantasma', 'Veneno', 93, 'Intercambio'),
(95, 'onix', 'Roca', 'Tierra', NULL, NULL),
(96, 'drowzee', 'Psíquico', NULL, NULL, NULL),
(97, 'hypno', 'Psíquico', NULL, 96, 'Nivel 26'),
(98, 'krabby', 'Agua', NULL, NULL, NULL),
(99, 'kingler', 'Agua', NULL, 98, 'Nivel 28'),
(100, 'voltorb', 'Eléctrico', NULL, NULL, NULL),
(101, 'electrode', 'Eléctrico', NULL, 100, 'Nivel 30'),
(102, 'exeggcute', 'Planta', 'Psíquico', NULL, NULL),
(103, 'exeggutor', 'Planta', 'Psíquico', 102, 'Nivel 30'),
(104, 'cubone', 'Tierra', NULL, NULL, NULL),
(105, 'marowak', 'Tierra', NULL, 104, 'Nivel 28'),
(106, 'hitmonlee', 'Lucha', NULL, NULL, NULL),
(107, 'hitmonchan', 'Lucha', NULL, NULL, NULL),
(108, 'lickitung', 'Normal', NULL, NULL, NULL),
(109, 'koffing', 'Veneno', NULL, NULL, NULL),
(110, 'weezing', 'Veneno', NULL, 109, 'Nivel 35'),
(111, 'rhyhorn', 'Roca', 'Tierra', NULL, NULL),
(112, 'rhydon', 'Roca', 'Tierra', 111, 'Nivel 42'),
(113, 'chansey', 'Normal', 'Hada', NULL, NULL),
(114, 'tangela', 'Planta', NULL, NULL, NULL),
(115, 'kangaskhan', 'Normal', NULL, NULL, NULL),
(116, 'horsea', 'Agua', 'Dragón', NULL, NULL),
(117, 'seadra', 'Agua', 'Dragón', 116, 'Nivel 32'),
(118, 'goldeen', 'Agua', NULL, NULL, NULL),
(119, 'seaking', 'Agua', NULL, 118, 'Nivel 33'),
(120, 'staryu', 'Agua', NULL, NULL, NULL),
(121, 'starmie', 'Agua', 'Psíquico', 120, 'Nivel 30'),
(122, 'mr-mime', 'Psíquico', 'Hada', NULL, NULL),
(123, 'scyther', 'Bicho', 'Volador', NULL, NULL),
(124, 'jynx', 'Hielo', 'Psíquico', NULL, NULL),
(125, 'electabuzz', 'Eléctrico', NULL, NULL, NULL),
(126, 'magmar', 'Fuego', NULL, NULL, NULL),
(127, 'pinsir', 'Bicho', NULL, NULL, NULL),
(128, 'tauros', 'Normal', NULL, NULL, NULL),
(129, 'magikarp', 'Agua', NULL, NULL, NULL),
(130, 'gyarados', 'Agua', 'Volador', 129, 'Nivel 20'),
(131, 'lapras', 'Agua', 'Hielo', NULL, NULL),
(132, 'ditto', 'Normal', NULL, NULL, NULL),
(133, 'eevee', 'Normal', NULL, NULL, NULL),
(134, 'vaporeon', 'Agua', NULL, 133, 'Piedra Agua'),
(135, 'jolteon', 'Eléctrico', NULL, 133, 'Piedra Trueno'),
(136, 'flareon', 'Fuego', NULL, 133, 'Piedra Fuego'),
(137, 'porygon', 'Normal', NULL, NULL, NULL),
(138, 'omanyte', 'Roca', 'Agua', NULL, NULL),
(139, 'omastar', 'Roca', 'Agua', 138, 'Nivel 40'),
(140, 'kabuto', 'Roca', 'Agua', NULL, NULL),
(141, 'kabutops', 'Roca', 'Agua', 140, 'Nivel 40'),
(142, 'aerodactyl', 'Roca', 'Volador', NULL, NULL),
(143, 'snorlax', 'Normal', 'Roca', NULL, NULL),
(144, 'articuno', 'Hielo', 'Volador', NULL, NULL),
(145, 'zapdos', 'Eléctrico', 'Volador', NULL, NULL),
(146, 'moltres', 'Fuego', 'Volador', NULL, NULL),
(147, 'dratini', 'Dragón', NULL, NULL, NULL),
(148, 'dragonair', 'Dragón', NULL, 147, 'Nivel 30'),
(149, 'dragonite', 'Dragón', 'Volador', 148, 'Nivel 55'),
(150, 'mewtwo', 'Psíquico', NULL, NULL, NULL),
(151, 'mew', 'Psíquico', NULL, NULL, NULL);


INSERT INTO RUTAS (NOMBRE, REGION) VALUES
('Ruta 1', 'Kanto'),
('Ruta 2', 'Kanto'),
('Ruta 3', 'Kanto'),
('Ruta 4', 'Kanto'),
('Ruta 5', 'Kanto'),
('Ruta 6', 'Kanto'),
('Ruta 7', 'Kanto'),
('Ruta 8', 'Kanto'),
('Ruta 9', 'Kanto'),
('Ruta 10', 'Kanto'),
('Ruta 11', 'Kanto'),
('Ruta 12', 'Kanto'),
('Ruta 13', 'Kanto'),
('Ruta 14', 'Kanto'),
('Ruta 15', 'Kanto'),
('Ruta 16', 'Kanto'),
('Ruta 17', 'Kanto'),
('Ruta 18', 'Kanto'),
('Ruta 19', 'Kanto'),
('Ruta 20', 'Kanto'),
('Ruta 21', 'Kanto'),
('Ruta 22', 'Kanto'),
('Ruta 23', 'Kanto'),
('Ruta 24', 'Kanto'),
('Ruta 25', 'Kanto'),
('Ruta 26', 'Kanto'),
('Ruta 27', 'Kanto'),
('Ruta 28', 'Kanto'),
('Pueblo Paleta', 'Kanto'),
('Ciudad Verde', 'Kanto'),
('Ciudad Plateada', 'Kanto'),
('Ciudad Celeste', 'Kanto'),
('Ciudad Azafrán', 'Kanto'),
('Ciudad Carmín', 'Kanto'),
('Ciudad Azulona', 'Kanto'),
('Ciudad Fucsia', 'Kanto'),
('Pueblo Lavanda', 'Kanto'),
('Isla Canela', 'Kanto'),
('Ruta 101', 'Johto');

INSERT INTO RUTAS_POKEMONS (POKEMON, RUTA, NIVEL_MINIMO, NIVEL_MAXIMO) VALUES
                                                                           (1, 1, 2, 5),  -- Bulbasaur aparece en la Ruta 1 entre los niveles 2 y 5
                                                                           (2, 2, 16, 18), -- Ivysaur aparece en la Ruta 2 entre los niveles 16 y 18
                                                                           (4, 3, 3, 6),   -- Charmander aparece en la Ruta 3 entre los niveles 3 y 6
                                                                           (7, 4, 5, 8),   -- Squirtle aparece en la Ruta 4 entre los niveles 5 y 8
                                                                           (10, 5, 2, 3),  -- Caterpie aparece en la Ruta 5 entre los niveles 2 y 3
                                                                           (13, 5, 2, 4),  -- Weedle aparece en la Ruta 5 entre los niveles 2 y 4
                                                                           (16, 6, 4, 7),  -- Pidgey aparece en la Ruta 6 entre los niveles 4 y 7
                                                                           (19, 7, 2, 5),  -- Rattata aparece en la Ruta 7 entre los niveles 2 y 5
                                                                           (21, 8, 7, 9),  -- Spearow aparece en la Ruta 8 entre los niveles 7 y 9
                                                                           (23, 9, 6, 10), -- Ekans aparece en la Ruta 9 entre los niveles 6 y 10
                                                                           (25, 10, 10, 12), -- Pikachu aparece en la Ruta 10 entre los niveles 10 y 12
                                                                           (25, 39, 10, 12), -- Pikachu aparece en la Ruta 101 de Johto entre los niveles 10 y 12
                                                                           (29, 11, 3, 5), -- Nidoran♀ aparece en la Ruta 11 entre los niveles 3 y 5
                                                                           (32, 11, 3, 5), -- Nidoran♂ aparece en la Ruta 11 entre los niveles 3 y 5
                                                                           (37, 12, 6, 8), -- Vulpix aparece en la Ruta 12 entre los niveles 6 y 8
                                                                           (41, 13, 4, 6), -- Zubat aparece en la Ruta 13 entre los niveles 4 y 6
                                                                           (60, 14, 10, 12), -- Poliwag aparece en la Ruta 14 entre los niveles 10 y 12
                                                                           (69, 15, 12, 15), -- Bellsprout aparece en la Ruta 15 entre los niveles 12 y 15
                                                                           (96, 18, 22, 25), -- Drowzee aparece en la Ruta 18 entre los niveles 22 y 25
                                                                           (129, 19, 5, 10); -- Magikarp aparece en la Ruta 19 entre los niveles 5 y 10


delimiter $$
drop procedure if exists MODIFCIAR_NIVELES_EN_RUTA$$
create procedure MODIFCIAR_NIVELES_EN_RUTA(IN RUTA_ID INT, IN SUBIDA INT)
BEGIN
	UPDATE RUTAS_POKEMONS
    SET NIVEL_MINIMO = NIVEL_MINIMO + SUBIDA,
    NIVEL_MAXIMO = NIVEL_MAXIMO + SUBIDA
    WHERE RUTA = RUTA_ID and nivel_maximo is not null and nivel_minimo is not null;
END$$

DROP FUNCTION IF EXISTS FN_GET_ID_POKEMON$$
CREATE FUNCTION FN_GET_ID_POKEMON(NOMBRE_POKEMON VARCHAR(20))
RETURNS INT UNSIGNED
READS SQL DATA
BEGIN
	RETURN	(
				SELECT ID 
					FROM POKEMONS
					WHERE NOMBRE = NOMBRE_POKEMON
            );
END$$

DROP FUNCTION IF EXISTS FN_GET_NOMBRE_POKEMON$$
CREATE FUNCTION FN_GET_NOMBRE_POKEMON(ID_POKEMON INT UNSIGNED)
RETURNS VARCHAR(20)
READS SQL DATA
BEGIN
	RETURN	(
				SELECT NOMBRE
					FROM POKEMONS
                    WHERE ID = ID_POKEMON
            );
END$$

DROP FUNCTION IF EXISTS FN_GET_ID_RUTA$$
CREATE FUNCTION  FN_GET_ID_RUTA(NOMBRE_RUTA VARCHAR(50), REGION_RUTA VARCHAR(20))
RETURNS INT UNSIGNED
READS SQL DATA
BEGIN
	RETURN 	(
				SELECT ID
					FROM RUTAS
                    WHERE NOMBRE = NOMBRE_RUTA
						AND
					REGION = REGION_RUTA
            );
END$$

DROP FUNCTION if exists countAllRoutes$$
create function countAllRoutes()
    returns int
    reads sql data
begin
RETURN (select count(*) from rutas);
end$$

DROP FUNCTION if exists countRoutesInRegion$$
create function countRoutesInRegion(regionName varchar(30))
    returns int
    reads sql data
begin
RETURN (select count(*) from rutas where region = regionName);
end$$

DROP TRIGGER IF EXISTS limites_Niveles$$
CREATE TRIGGER limites_Niveles
before update ON rutas_pokemons
FOR EACH ROW
BEGIN
      if new.NIVEL_MAXIMO < 1 
		then set new.NIVEL_MAXIMO = 1;
      end if;
      if new.NIVEL_MAXIMO > 100 
		then set new.NIVEL_MAXIMO = 100;
      end if;
      if new.NIVEL_MINIMO < 1 
		then set new.NIVEL_MINIMO = 1;
      end if;
      if new.NIVEL_MINIMO > 100 
		then set new.NIVEL_MINIMO = 100;
      end if;

END$$


