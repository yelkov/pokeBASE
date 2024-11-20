DELIMITER $$
USE bdpokemon $$
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