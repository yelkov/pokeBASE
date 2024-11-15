import os

dir_imagenes = 'cache'

archivo_salida = 'imagenes_pokemon.sql'

def generar_sql():
    with open(archivo_salida,'w') as f_sql:
        imagenes_por_tipo = {'png':[], 'gif':[]}

        for archivo in os.listdir(dir_imagenes):
            nombre_imagen = os.path.splitext(archivo)[0] #quitar extension
            nombre_pokemon = nombre_imagen.split('_')[0] #quitar _image o _animated
            extension_imagen = archivo.split('.')[-1]

            if extension_imagen in imagenes_por_tipo:
                if extension_imagen == 'png':
                    imagenes_por_tipo[extension_imagen].append((nombre_pokemon,f"'src/main/resources/images/{nombre_pokemon}_image.png'"))
                elif extension_imagen == 'gif':
                    imagenes_por_tipo[extension_imagen].append((nombre_pokemon,f"'src/main/resources/images/{nombre_pokemon}_animated.gif'"))

        for tipo, imagenes in imagenes_por_tipo.items():
            if imagenes:
                f_sql.write(f"/*Imagenes de tipo {tipo.upper()}*/\n")
                if tipo == 'png':
                    for nombre_pokemon, ruta_png in imagenes:
                        f_sql.write(f"UPDATE POKEMONS SET IMAGEN = {ruta_png} WHERE NOMBRE = '{nombre_pokemon}';\n")
                    f_sql.write("\n")
                if tipo == 'gif':
                    for nombre_pokemon, ruta_gif in imagenes:
                        f_sql.write(f"UPDATE POKEMONS SET GIF = {ruta_gif} WHERE NOMBRE = '{nombre_pokemon}';\n")
                    f_sql.write("\n")
        print("Archivo generado")

generar_sql()