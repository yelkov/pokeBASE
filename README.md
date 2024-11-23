# PokeAPI App
___
## Índice
- [Introducción](#introducción)
- [Manual técnico para desarrolladores](#manual-técnico-para-desarrolladores)
    - [Requisitos previos](#requisitos-previos)
    - [Estructura](#estructura)
    - [Metodología](#metodología)
    - [Configuración de Maven](#configuración-de-maven)
    - [Configuración de JavaFx](#configuración-de-javafx)
    - [Ejecución del proyecto](#ejecución-del-proyecto)
    - [Manejo de la caché y último estado](#manejo-de-la-caché-y-último-estado)
    - [Manejo de errores](#manejo-de-errores)
    - [Exportación de datos](#exportación-de-datos)
- [Manual de usuario](#manual-de-usuario)
    - [Búsquedas sencillas](#búsquedas-sencillas)
    - [Consultar la información del Pokémon](#consultar-la-información-del-pokémon)
    - [Otras opciones (limpiar datos y borrar caché)](#otras-opciones-limpiar-datos-y-borrar-caché)
    - [Registro](#registro)
    - [Exportaciones](#exportaciones)
    - [Guardado del último estado](#guardado-del-último-estado)
- [Reparto de tareas](#reparto-de-tareas)
- [Extras](#extras)
- [Mejoras](#mejoras)
- [Conclusiones](#conclusiones)
- [Autores](#autores)

## Introducción
[Volver al índice](#índice)

Esta es una aplicación que permite realizar peticiones a la [API de Pokémon](https://pokeapi.co/) [(repositorio de GitHub)](https://github.com/PokeAPI/pokeapi).

Tras una búsqueda por nombre, el programa mostrará los siguientes campos: número de identificación del pokémon, sprite (imagen) frontal por defecto, sus diferentes nombres en varios idiomas así como las áreas del juego en las que se encuentra el pokémon en estado salvaje.

También es posible registrarse, de modo que tras el registro (o inicio de sesión posterior) el programa permite recuperar la última búsqueda del usuario y también exportar a diferentes formatos la información recopilada.

___

## Manual técnico para desarrolladores
[Volver al índice](#índice)

### Requisitos previos

- **Java SE 17 o superior**: El proyecto está desarrollado usando Java 17, por lo que necesitarás tener una versión igual o superior instalada. ([descargar](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- **JavaFX 21.0.5**: El proyecto usa JavaFX para la interfaz gráfica, por lo que deberás incluir el SDK de JavaFX. ([descargar](https://gluonhq.com/products/javafx/))
- **Maven**: La gestión de dependencias se hace con Maven, por lo que deberás tener Maven instalado.([descargar](https://maven.apache.org/download.cgi))
- **IDE recomendado**: Se recomienda el uso de IntelliJ IDEA para un desarrollo más sencillo, pero se puede usar cualquier otro IDE compatible con Java. ([descargar](https://www.jetbrains.com/idea/download/?section=windows))



### Estructura
El proyecto está planteado siguiendo el patrón [Modelo-Vista-Controlador.](https://es.wikipedia.org/wiki/Modelo%E2%80%93vista%E2%80%93controlador)

![MVC](media/images/mvc.jpg)

#### Modelo
___
El modelo contiene los datos del programa y define cómo estos deben ser manipulados, es decir, contiene la lógica que se necesita para gestionar el estado y las reglas del negocio.
Interactúa respondiendo a las solicitudes del controlador para acceder o actualizar los datos.  Notifica indirectamente a la vista cuando los datos han cambiado para que se actualice la presentación.

En nuestra aplicación cuenta con los siguientes paquetes:

- **<u>edu.badpals.pokeapi.model</u>**: Contiene las clases de modelos de datos como `PokemonData`, `Area`, `PokemonImage`(entre otras). Se utilizan estas clases para realizar el mapeado a los .json de respuesta de la API.


- **<u>edu.badpals.pokeapi.service</u>**: Gestiona las operaciones como peticiones API `APIPetitions`, manejo de caché `CacheManager`, exportación de datos `DocumentExporter` y guardado del último estado `StateManager`.


- **<u>edu.badpals.pokeapi.auth</u>**: la clase `LogInManager` se encarga tanto de verificar la autencidad de un usuario (comprobando sus credenciales en un fichero .properties) como de registrar nuevos usuarios. Las credenciales de los usuarios se guardan cifradas en el archivo.

![](media/images/encriptados.png)

- **<u>/cache</u>**: en el directorio caché se guardan las respuestas a las peticiones que se han realizado a la API en sesiones previas, que son cargadas en el programa antes de hacer nuevas consultas, para mejorar el rendimiento. Los datos están almacenados en formato .json y las imágenes en .png .


- **<u>.appData</u>**: en este directorio se almacena en formato .bin la última búsqueda que realiza un usuario tras hacer logIn y salir de la aplicación. Cuando el usuario regresa puede restablecer esa búsqueda.

#### Controlador
___
El controlador recibe las entradas del usuario desde la vista y las traduce en acciones que el modelo debe ejecutar. Se encarga de interpretar las acciones del usuario, manejar los eventos, y de actualizar tanto el modelo como la vista.

- **<u>edu.badpals.pokeapi.controller</u>**: Coordina la interacción entre los diferentes componentes, y controla la lógica de la aplicación. Se gestiona todo desde la clase `AppController`.

#### Vista
___
Se encarga de la visualización de los datos del modelo de manera que el usuario los entienda. No contiene lógica de negocio, solo muestra lo que el modelo le proporciona.. La vista recibe entradas del usuario (clics, teclas, etc.) y las envía al controlador.

- `edu.badpals.pokeapi.Application.java`: Contiene la clase principal del programa, relacionada con la generación de la interfaz gráfica de usuario (con JavaFX). En ella se crean las escenas cuando se ejecuta el programa.


- **<u>resources</u>**: en el directorio resources se almacenan los recursos necesarios para construir la interfaz de usuario, desde los archivos .fxml en los que se diseñan las vistas, hasta la hoja de estilos css e imágenes.

### Metodología
**Uso de Git**

Este proyecto sigue una metodología de desarrollo incremental basado en ramas, lo que facilita la gestión de versiones y la colaboración entre desarrolladores. Las ramas principales del proyecto son `main` y `develop`, mientras que el desarrollo se llevó a cabo en paralelo en las ramas `controller`, `service` y `model`.

El flujo de trabajo del desarrollo es el siguiente:

1. **Añadir Nuevas Funcionalidades**: Cuando se desea implementar una nueva funcionalidad, se trabaja en la rama propia al paquete que pertenece. Cada desarrollador trabaja en una única rama, permitiendo que el trabajo avance de manera independiente.

2. **Testeo**: Una vez que se ha completado la funcionalidad, se realizan pruebas para asegurar que todo funciona correctamente y cumple con los requisitos establecidos.

3. **Merge a Develop**: Después de las pruebas exitosas, se realiza un "merge" de la rama de funcionalidad a `develop`. Este paso es crucial para comprobar la integración de la nueva funcionalidad con el resto del código del proyecto.

4. **Merge a Main**: Finalmente, cuando la versión en `develop` ha sido probada y se confirma que es estable, se realiza un "merge" a la rama `main`. Esto marca el lanzamiento de una nueva versión del proyecto.

Este enfoque permite una colaboración fluida entre los dos desarrolladores, asegurando que el código sea de alta calidad y esté bien integrado antes de ser lanzado.

### Configuración de Maven

El archivo `pom.xml` incluye las siguientes dependencias importantes:

```xml
<dependencies>
    <!-- Dependencias para JavaFX -->
    <dependency>
      <groupId>org.openjfx</groupId>
      <artifactId>javafx-fxml</artifactId>
      <version>17.0.6</version>
    </dependency>

    <!-- Dependencias para JSON (Jackson) -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.18.0-rc1</version>
    </dependency>

  <!-- También se incluyen las siguientes dependencias para realizar testing, 
  pero no se han añadido test durante el desarrollo -->
  
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>${junit.version}</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>${junit.version}</version>
      <scope>test</scope>
    </dependency>
</dependencies>
```
### Configuración de JavaFX

Para ejecutar el proyecto con JavaFX, tras [descargar el SDK]((https://gluonhq.com/products/javafx/)) se necesitan añadir los siguientes módulos en la configuración de tu IDE:

```
--module-path="ruta\directorio\javaFx\lib" --add-modules="javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.swing,javafx.media"
```
Para ejecutar el programa directamente en el IDE, es necesario añadirlos en *File>Project Structure>Global Libraries>New Global Library>Java* y a continuación cargar los .jar del directorio lib donde se haya guardado javaFx.

También es posible preparar la ejecución del .jar del programa desde la ventana de *Run /Debug Configurations* añadiendo en *VM options* las líneas del cuadro anterior.

### Ejecución del proyecto

#### Desde el IDE (IntelliJ IDEA):

1. Importar el proyecto como un proyecto Maven.
2. Asegurarse de que las dependencias estén instaladas en el `pom.xml`.
3. Configurar los módulos de JavaFX como se describió anteriormente.
4. Ejecutar la clase `Main` para iniciar la aplicación.

#### Desde la terminal:
Una vez que el JAR esté generado, se ejecuta el siguiente comando **desde el directorio donde se encuentre el .jar generado**:
``` bash
java --module-path="/ruta/al/javafx/lib" --add-modules="javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.swing,javafx.media" -jar nombre-del-archivo.jar
```

En la siguiente captura mostramos la ejecución desde el terminal.

![](media/images/jar.png)


### Manejo de la caché y último estado

El proyecto almacena los datos de los Pokémon consultados y sus imágenes localmente en la carpeta de caché (`/cache`).

- Los datos JSON se guardan usando la clase `CacheManager`, que guarda las respuestas de la API localmente para evitar llamadas repetidas.
- Las imágenes de los Pokémon se descargan y guardan en formato `.png` en el mismo directorio.

Se puede limpiar la caché con el método `deleteCache()` de la clase `CacheManager`.

El último estado del usuario loggeado se almacena en formato `.bin` en el directorio oculto `.appData`. Cuando se inicia sesión se muestra este estado y se almacena cuando el usuario loggeado sale del programa.

### Manejo de errores

En la aplicación, los errores se gestionan de dos maneras:
- Los errores que ocurren en las clases del **modelo** o de **service** (relacionadas con la lógica interna) se manejan de forma interna y se registran en un archivo de log. Así cuando se captura una excepción, se llama a una función de la clase ErrorLogger, que se encarga de escribir el mensaje en el fichero log.

![](media/images/log.png)

- Por otro lado, los errores que se producen en la **interfaz de usuario** se presentan directamente en pantalla, para que el usuario pueda comprender qué ha fallado.

Para registrar los errores del modelo, existe una clase llamada `ErrorLogger`, ubicada en el paquete `service`. Esta clase es responsable de guardar los errores en un archivo llamado `error.log`, que se encuentra en la carpeta oculta `.appData`.


### Exportación de datos

El proyecto permite exportar la información de los Pokémon a diferentes formatos, como `.json`, `.xml`, `.txt`, y `.bin`.

- La exportación se maneja dentro de la clase `DocumentExporter`, y se puede seleccionar el formato que se prefiera tras hacer una consulta (aunque esto sólo se mostrará en la vista cuando el usuario se autentifique).

## Manual de usuario
[Volver al índice](#índice)

### Log In
Para acceder a nuestra aplicación, es necesario estar logeado o registrado, por lo que al abrir la aplicación, la primera ventana que verá el usuario será un menú de inicio de sesión como la que se muestra a continuación. Desde el mismo menú podrá registrarse en caso de que aún no lo haya hecho, o iniciar sesión si ya lo está.

![](media/01_intro.png)

Si nos intentamos registrar con un nombre de usuario que ya está almacenado, el sistema nos mostrará un mensaje de error, y nos impide avanzar en el proceso.

![](media/03_registro_mal.png)

Del mismo modo, si tratamos de acceder con credenciales que no son correctas, nos muestra el correspondiente aviso, y tampoco nos permite continuar.

![](media/02_usuario_mal.png)

Finalmente, si introducimos un usuario y contraseña que coinciden con lo almacenado en el sistema, accederemos ya al menú principal de la aplicación, desde el que podemos realizar búsquedas de rutas y pokemon, que nos llevarán a las ventanas dónde tendremos acceso a las diversas acciones que se pueden realizar en cada caso y que se explicarán a continuación.

![](media/03_inicio_sesion.png)

### Búsqueda de Pokémon


### Filtrar Pokémon


### Búsqueda Ruta
Podemos buscar la información de una ruta en concreto. Para ello, debemos introducir el nombre de la ruta, y seleccionar la región a la que pertenece, ya que puede haber varias rutas con el mismo nombre, siempre que sean de regiones distintas. Para lo segundo, se carga un combo box con las regiones que ya existen en la base de datos, aunque luego se pueden añadir a mayores.

![](media/16_ruta_001.png)

Al pulsar buscar, se nos abre el menú de Ruta, con la información cargada de la ruta. En ella podremos modificar su información, borrarla, modificar los pokemon que se encuentran en ella y acceder al menú de crear una nueva ruta

![](media/16_Rutas_2.png)

Si modificamos la ruta con un nombre y región que ya existen, nos saltará un mensaje de error.

![](media/16_Rutas_3.png)

Al darle a crear ruta, se limpiará la información cargada en ese momento, para mostrar solamente los campos imprescindibles que se deben rellenar. A este vista se accede igualmente desde el botón de crear ruta en el menú principal.

![](media/18_crear.png)

Al introducir los campos y pulsar guardar, se registra en la base de datos.

![](media/18_crear_2.png)

Además, se nos vuelve a mostrar un menú para realizar acciones sobre ella, como modificarla o añadir pokémons.

![](media/18_crear_3.png)

Pulsando el botón borrar, la eliminaremos de la base de datos, y se nos vuelve a mostrar el menú de crear ruta, porque no hay ninguna cargada.

![](media/18_crear_4.png)
![](media/19_borrar.png)

Por otro lado, también desde la vista de rutas podremos gestionar los pokemons que se pueden encontrar en una determinada ruta. Para ello, tendremos que introducir el nombre del pokemon, el nivel mínimo y el nivel máximo.

![](media/17_pokemon_ruta_1.png)

Al darle al botón de añadir pokémon, vemos como ya nos aparece en la lista. Por otro lado, podemos modificar los niveles de los pokémon para ello escribimos el número de niveles, y pulsamos + para subirlos y - para bajarlos.

![](media/17_pokemon_ruta_2.png)

Nótese que en este caso Bulbasaur no ha modificado sus niveles, esto ocurre porque el nivel debe ser en todo momento entre 1 y 100.

![](media/17_pokemon_ruta_4.png)

Análogamente al intentar bajar 10 niveles a los pokémon de esta ruta, pidgey baja de nivel, pero no puede bajar por debajo de 1.

![](media/17_pokemon_ruta_5.png)

También podemos seleccionar los pokemon de la lista. Al hacerlo, se activan los dos botones de abajo de todo de la pantalla. El de la derechar nos permite eliminar el pokémon de la ruta (pero seguirá registrado en la tabla de pokemons de la base de datos).

![](media/17_pokemon_ruta_6.png)

El de la izquierda, como su nombre indica, nos permite acceder a la vista de pokemon explicada anteriormente, como se muestra en las siguientes imágenes.

![](media/17_pokemon_ruta_7.png)
![](media/17_pokemon_ruta_8.png)

Finalmente, solo podremos registrar pokémon en una ruta si ya se han guardado previamente en la base de datos. Por ejemplo, si tratamos de añadir a pidove, que no ha sido registrado en la base de datos, nos muestra el siguiente mensaje.

![](media/17_pokemon_ruta_9.png)

### Filtrar Rutas

Desde el menú principal también podemos solicitar un listado de las rutas que cumplan unos determinados criterios. En primer lugar, podemos filtrarlas por la región en la que se encuentran. Cómo al buscar una única ruta, se carga un combo box con las que ya existen en la base de datos, además de la opción de considerar todas las regiones.

![](media/20_listar_rutas.png)

Por otro lado, podemos solicitar solamente aquellas rutas en las que se encuentre un determinado pokémon. Si el pokémon no está registrado en la base de datos no dará un mensaje de error, simplemente devolverá una lista vacía de rutas. Por otro lado, si se deja vacía no filtrará por este criterio.

![](media/20_listar_rutas_2.png)

Tras pulsar el botón buscar nos muestra una vista de lista de rutas. Con los datos introducimos en la captura anterior, por ejemplo, obtenemos la siguiente lista, que en este caso consta de un solo elemento. En la barra superior de esta pantalla tenemos cargados los criterios por los que se está realizando la búsqueda actualmente, pero podemos modificarlos para realizar otras peticiones o añadir criterios de ordenación diferentes a los defectivos.

![](media/20_listar_rutas_3.png)

Con lo explicado anteriormente, podemos recuperar la lista de todas las rutas existentes dejando en blanco el apartado de pokémon y seleccionando la opción Todas en regiones.

![](media/20_listar_rutas_4.png)

Si seleccionamos una ruta, se activará el botón inferior, que nos permite ir a la vista individual de ruta.

![](media/20_listar_rutas_7.png)

No obstante, al cargar una ruta desde esa pantalla, se nos muestra un mensaje en la parte inferior de la pantalla recordándonos el criterio que estábamos usando para filtrar las rutas, y permitiéndonos movernos solamente entre las que se cargaron en la vista anterior.

![](media/20_listar_rutas_5.png)

Por ejemplo, pulsando siguiente vamos a la segunda ruta de la lista anterior.

![](media/20_listar_rutas_6.png)

### Otras funciones

Salvo el menú principal, todas las demás ventanas tienen cuatro botones que permiten realizar funciones comunes.

El botón volver permite acceder a la ventana inmediatamente anterior, manteniéndose la información que estaba cargada cuando esta llamó a la siguiente.

![]()

El botón volver al menú principal vuelve a la pantalla de inicio que se carga tras iniciar sesión de forma correcta.

![]()

El botón limpiar borra la información cargada en la interfaz en un determinado momento. Además, en algunas pantallas bloquea algunos botones, pues su funcionalidad solo tiene sentido si hay algo cargado en la pantalla.

![]()

Finalmente, el botón exportar permite generar un archivo tipo json con la información del objeto o la lista, según corresponda que se está mostrando en ese momento.

![](media/21_Exportar.png)

Al pulsarlo, se nos abre una ventana de diálogo dónde podemos seleccionar la ruta y el nombre de dicho archivo

![](media/21_Exportar_2.png)

Al crearlo, nos informa de que la exportación se ha realizado correctamente.

![](media/21_Exportar_3.png)

Finalmente, si consultamos el archivo, vemos como la exportación se ha realizado de forma correcta.

![](media/21_Exportar_4.png)





## Reparto de tareas
[Volver al índice](#índice)

El trabajo en equipo para el desarrollo de esta aplicación se realizó mediante dos técnicas diferentes: el uso de pair programming y el trabajo en paralelo mediante git trabajando con diversas ramas, según fuese más apropiado en cada parte del proyecto. Para las etapas iniciales de creación de la base de datos e interfaz principal del programa, se usó el pair programming, de forma que ambos integrantes acuerden las funcionalidades básicas. Asimismo, durante todo el proceso de desarrollo se empleó esta técnica también para corregir errores y realizar algunas refactorizaciones, pues permite detectar fallos más fácilmente.

Por otro lado, como nuestra aplicación tiene dos partes bien diferenciadas, la correspondiente al tratamiento de datos tipo pokemon y la de los datos tipo ruta, usar git con diferentes ramas fue la estrategia más conveniente. Yelko Veiga se encargó del desarrollo relacionado con la primera y David Búa fue el responsable principal de la segunda, incluyendo la parte de mostrar los pokemon hallados en cada ruta. Además, se reutilizaron algunas clases del método anterior, como el Log in y las exportaciones, y la adaptación de estas a este programa se realizaron según quien estuviese más disponible en el momento en el que fueron necesarias. Lo relacionado con la autentificación fue desarrollada principalmente por David, mientras que las exportaciones a formato json corrieron por parte de Yelko. La clase SceneManagar, que se encarga de gestionar las transiciones entre escenas, es fruto de refactorizaciones sucesivas realizadas por ambos integrantes.

Finalmente, la documentación de las clases y este mismo README fue repartido en partes aproximadamente iguales


## Extras
[Volver al índice](#índice)

En la realización de este trabajo se incluyeron varios extras a mayores de los requisitos mínimos del proyecto. Los más destacables son los siquientes:

- Registros de usuario, mediante el guardado de los valores de usuario y contraseña (esta última encriptada) en una base de datos, diferente a la empleada para las demás funcionalidades del progema. Es necesario logearse o registrarse para acceder a las funcionalidades de la aplicación.
- Ordenación de los resultados de búsqueda, tanto para rutas como para pokemon, según al menos dos criterios en cada caso, tanto en orden ascendente como descendente, a escoger por el usuario. 
- Manejo de errores en dos niveles, uno mediante mensaje de error por la interfaz gráfica para errores a nivel usuario que este debería conocer (como tratar de crear un pokemon que ya está guardado en la base de datos o fallos al introducir las credenciales del usuario) y otro mediante un registro de los errores en un archivo de tipo log para errores de la lógica interna (cómo los relacionados con el acceso a base de datos). Para más información sobre el manejo de errores, consultar el Manual Técnico de desarrolladores, sección [Manejo de errores](#manejo-de-errores).


## Mejoras
[Volver al índice](#índice)

Como en todo proyecto, existen numerosas características que por tiempo o recursos no se han podido implementar o que no se han podido perfeccionar. En nuestro caso hemos considerado las siguientes posibilidades de mejora, aunque siempre abiertos a la posibilidad de otras que no hemos detectado:

- Crear la base de datos de forma defectiva si no está creada en el equipo.
- Buscar soluciones más eficientes para el envío de datos entre escenas, y corregir los pequeños casos en los que no se realizan correctamente.
- Poder modificar de forma más pormenorizada los niveles de un pokemon en una ruta.
- Poder acceder desde la información de un pokemon a las rutas en las que aparece.
- Añadir más criterios de búsqueda a las listas.
- Mostrar información de a qué pokemon evoluciona otro determinado
- Mejorar el manejo de errores con imágenes que ocurren con ciertos formatos o ciertos tamaños de imágenes.
- Refactorizar nombres de variables y métodos para homogeneizar más el código.


## Conclusiones
[Volver al índice](#índice)

En total, el proyecto ha requerido la inversión de unas 23 horas por parte de cada miembro, aunque no hemos llevado cuenta rigurosa de este. Consideramos que tanto el reparto de tareas como la dedicación temporal ha sido bastante equilibrada entre los dos.

La aplicación que presentamos cumple todos los criterios propuestos, tanto los obligatorios como los extras. Hemos creado una base de datos con dos entidades y una relación de muchos a muchos, y desde la interfaz gráfica el usuario puede hacer un CRUD a cualquiera de estas tres tablas. Además, puede buscar mediante filtros tanto pokemons como rutas, y exportar los resultados obtenidos a formato json.

No obstsante, hemos sentido que en este caso existía una mayor cantidad de funcionalidades que debíamos implementar, y nos hemos sentido más apurados respecto al tiempo. Tal y como hemos planteado la aplicación, la configuración de la interfaz gráfica y el cambio de escenas ha llevado más tiempo del que teníamos previsto, y las funcionalidades son relativamente limitadas respecto a lo que en un primer momento nos imaginamos cuando se propuso el proyecto. Ambos nos quedamos con las ganas de plantear este proyecto más a largo plazo e ir incorporando gradualmente nuevas funcionalidades, porque sabemos que esa aplicación podría llegar a ser uno de nuestros mejores proyectos, sino el mejor. Aún así, el producto final se corresponde con lo que se esperaría desarrollar en un plazo de una semana, y ha sido muy eficaz a la hora de practicar y asimilar mejor los conceptos de acceso a base de datos dados en clase, objetivos que hemos cumplido satisfactoriamente, por lo que lo consideramos digno de un sobresaliente.


## Autores
[Volver al índice](#índice)

Yelko Veiga Quintas [@yelkov](https://github.com/yelkov)

David Búa Teijeiro [@BuaTeijeiro](https://github.com/BuaTeijeiro)