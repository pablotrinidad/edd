Estructuras de Datos
====================

Proyecto 3
----------

### Fecha de entrega: viernes 31 de mayo, 2019


Deben escribir un programa que genere reportes de varios archivos de texto. El
programa recibirá varios nombres de archivo por la línea de comandos, y un
directorio precedido de la bandera `-o`:

```sh
$ java -jar proyecto3.jar archivo1.txt archivo2.txt -o directorio archivo3.txt
```

Si el directorio de salida no existe, deben crearlo; y para cada archivo de
texto en la línea de comandos el programa generará un archivo HTML dentro del
directorio con la siguiente información:

* Un conteo de cuántas veces aparece cada palabra en el archivo, sin considerar
  mayúsculas/minúsculas ni acentos. Dicho de otra forma, `líquido` se considera
  la misma palabra que `Liquidó`. El conteo **debe** realizarse en *O*(*n*),
  donde *n* es el número de palabras en el archivo.
* Una gráfica de pastel de las palabras más comunes en el archivo y qué
  porcentaje del total de palabras ocupan; el resto de las palabras se juntarán
  en una sola rebanada.
* Una gráfica de barras con la misma información.
* Un árbol rojinegro con las 15 palabras más utilizadas en el archivo, donde el
  valor de cada palabra es el número de veces que aparece en el archivo.
* Un árbol AVL con los mismos datos del árbol de arriba.

Además del archivo HTML correspondiente a cada archivo especificado en la línea
de comandos, deben generar un archivo `index.html` con ligas a cada archivo HTML
generado, el número de palabras en cada uno, y una gráfica donde cada archivo es
un vértice, y dos archivos son vecinos si ambos contienen al menos una palabra
en común de al menos 7 caracteres cada una.

Su programa deberá cumplir las siguientes reglas:

* Todos los archivos generados deben estar dentro del directorio especificado
  por `-o`. No debe haber subdirectorios.
* Todos los gráficos deben de ser SVG generados por ustedes (sin usar ninguna
  biblioteca externa).
* Deben usar las clases vistas en el curso, y no pueden utilizar ninguna clase
  del paquete `java.util`; excepciones e interfaces están permitidas.
* No pueden modificar las clases del curso: no pueden agregarles ni métodos ni
  variables de clase, no importa el acceso. Tampoco pueden modificar los nombres
  de paquetes. Deben utilizar las clases con el comportamiento que se les ha
  definido a lo largo del curso.
* Las clases de su proyecto deben estar en el paquete
  `mx.unam.ciencias.edd.proyecto3`.
* Los archivos de entrada deben poder especificarse con rutas absolutas o
  relativas. Esto significa que potencialmente pueden estar donde sea en el
  sistema de archivos.
* Su programa debe ser robusto frente a errores; si alguno de los archivos no
  existe; o el directorio de salida es un archivo; o no tienen los permisos
  necesarios, el programa no debe lanzar una excepción: debe mostrar un mensaje
  (al error estándar) explicando qué ocurrió, y terminar con gracia.

Dada la calidad del código, puntos extras serán dados (o no) a discreción del
profesor; la decisión para ello es inapelable.

El proyecto se entregará **únicamente al profesor**.
