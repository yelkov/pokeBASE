DELIMITER $$

use bdpokemon_test$$

drop procedure if exists MODIFCIAR_NIVELES_EN_RUTA$$
create procedure MODIFCIAR_NIVELES_EN_RUTA(IN RUTA_ID INT, IN SUBIDA INT)
BEGIN
	UPDATE RUTAS_POKEMONS
    SET NIVEL_MINIMO = NIVEL_MINIMO + SUBIDA,
    NIVEL_MAXIMO = NIVEL_MAXIMO + SUBIDA
    WHERE RUTA = RUTA_ID;
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




