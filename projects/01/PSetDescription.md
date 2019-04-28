Estructuras de Datos
====================

Proyecto 1
----------

### Fecha de entrega: viernes 22 de marzo, 2019

Deben implementar un ordenador lexicográfico (una versión más sencilla del
programa `sort` en Unix) que funcione con uno o más archivos de texto o la
entrada estándar y que imprima su salida en la salida estándar.

Por ejemplo, si tienen el achivo `hombres.txt`:

```
Hombres necios que acusáis
    a la mujer sin razón,
sin ver que sois la ocasión
    de lo mismo que culpáis.

Si con ansia sin igual
    solicitáis su desdén,
¿por qué queréis que obren bien
    si las incitáis al mal?

Combatís su resistencia
    y luego con gravedad
decís que fue liviandad
    lo que hizo la diligencia.

Parecer quiere el denuedo
    de vuestro parecer loco
al niño que pone el coco
    y luego le tiene miedo.

Queréis con presunción necia
    hallar a la que buscáis,
para pretendida, Tais,
    y en la posesión, Lucrecia.

¿Qué humor puede ser más raro
    que el que, falto de consejo,
él mismo empaña el espejo
    y siente que no esté claro?
```

Entonces al correr su proyecto de la siguiente manera:

```shell
$ java -jar proyecto1.jar hombre.txt
```

La salida estándar del programa debe ser:

```





    a la mujer sin razón,
al niño que pone el coco
Combatís su resistencia
decís que fue liviandad
    de lo mismo que culpáis.
    de vuestro parecer loco
él mismo empaña el espejo
    hallar a la que buscáis,
Hombres necios que acusáis
    lo que hizo la diligencia.
para pretendida, Tais,
Parecer quiere el denuedo
¿por qué queréis que obren bien
    que el que, falto de consejo,
¿Qué humor puede ser más raro
Queréis con presunción necia
Si con ansia sin igual
    si las incitáis al mal?
sin ver que sois la ocasión
    solicitáis su desdén,
    y en la posesión, Lucrecia.
    y luego con gravedad
    y luego le tiene miedo.
    y siente que no esté claro?
```

Noten que el archivo se ordena por líneas, no por palabras; que las líneas
vacías se ordenan *antes* que las no vacías; que los espacios son ignorados al
ordenar pero preservados al imprimir; y que los acentos, eñes y diéresis se
toman como vocales y la letra `n` se. La misma salida debe ocurrir si le damos
al programa el archivo a través de la entrada estándar:

```shell
$ cat hombres.txt | java -jar proyecto1.jar
```

El programa tiene que funcionar con la entrada estándar, de otra forma no
recibirá más de 5 de calificación.

El programa debe poder recibir varios archivos, ya sea en la línea de comandos o
por la entrada estándar, en cuyo caso los trata a todos como un único archivo
grande. Reglas:

* No pueden usar ninguna clase de Java de `java.util`, excepto por las pocas
  interfaces usadas durante el curso (`java.util.Iterator`, etc.) y excepciones.
* Pueden (y deben) usar las clases vistas durante el curso.
* No pueden modificar ninguna de las interfaces públicas de las clases vistas
  durante el curso. Esto incluye los nombres de los paquetes.
* Si el usuario pasa la bandera `-r`, el programa debe imprimir las líneas en
  orden inverso.
* Si el usuario pasa la bandera `-o` seguida de un identificador, el programa
  debe guardar su salida en un archivo llamado como el identificador. La
  operación es desctructiva y supone que el usuario sabe lo que hace.
* Las clases de su proyecto (no las de las prácticas) **deben** usar el paquete
  `mx.unam.ciencias.edd.proyecto1`.
* Los archivos de entrada deben poder estar en cualquier lugar en el sistema de
  archivos, no tienen por qué estar en el mismo directorio del archivo Jar. En
  otras palabras, el programa debe poder invocarse de la siguiente manera:

  ```shell
  $ java -jar proyecto1.jar /ruta/cualquiera/hombres.txt
  ```

  y si el archivo `/ruta/cualquiera/hombres.txt` existe, el programa debe
  funcionar.
* La complejidad en tiempo del programa debe ser *O*(*n* log *n*), siendo *n* el
  número de líneas en el o los archivos de entrada.

Las banderas son como en los comandos de Unix, argumentos de la línea de
comandos, y pueden ir en cualquier orden. Dada la calidad, legibilidad y
presentación del código entregado, el profesor otorgará (o no) puntos extra de
manera discrecional.

El proyecto se entregará **únicamente al profesor**.
