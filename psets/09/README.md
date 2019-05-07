Estructuras de Datos
====================

Práctica 8: Trayectoria mínima y algoritmo de Dijkstra.
-------------------------------------------------------

### Fecha de entrega: martes 30 de abril, 2019

Deben modificar su clase
[`Grafica`](https://aztlan.fciencias.unam.mx/gitlab/2019-2-edd/practica8/blob/master/src/main/java/mx/unam/ciencias/edd/Grafica.java)
para poder implementar los algoritmos de trayectoria mínima y de Dijkstra. Esto implica hacer que la clase interna
[`Vertice`](https://aztlan.fciencias.unam.mx/gitlab/2019-2-edd/practica8/blob/master/src/main/java/mx/unam/ciencias/edd/Grafica.java#L35)
implemente la interfaz
[`ComparableIndexable`](https://aztlan.fciencias.unam.mx/gitlab/2019-2-edd/practica8/blob/master/src/main/java/mx/unam/ciencias/edd/ComparableIndexable.java),
implementar los métodos de la clase interna
[`Vecino`](https://aztlan.fciencias.unam.mx/gitlab/2019-2-edd/practica8/blob/master/src/main/java/mx/unam/ciencias/edd/Grafica.java#L91),
que utilizaremos como adaptador de la clase `Vertice` para que sirvan como elementos de las listas de vecinos de los vértices, e implementar los métodos
[`trayectoriaMinima()`](https://aztlan.fciencias.unam.mx/gitlab/2019-2-edd/practica8/blob/master/src/main/java/mx/unam/ciencias/edd/Grafica.java#L396)
y
[`dijsktra()`](https://aztlan.fciencias.unam.mx/gitlab/2019-2-edd/practica8/blob/master/src/main/java/mx/unam/ciencias/edd/Grafica.java#L411).

Una vez que hayan terminado con sus clases, además de que deben de pasar todas
sus pruebas unitarias, se debe ejecutar correctamente el programa escrito en la
clase
[`Practica8`](https://aztlan.fciencias.unam.mx/gitlab/2019-2-edd/practica8/blob/master/src/main/java/mx/unam/ciencias/edd/Practica8.java).

Los únicos archivos que deben modificar son:

* `ArbolAVL.java`,
* `ArbolBinarioCompleto.java`,
* `ArbolBinarioOrdenado.java`,
* `ArbolBinario.java`,
* `ArbolRojinegro.java`,
* `Arreglos.java`,
* `Cola.java`,
* `Grafica.java`,
* `Lista.java`,
* `MeteSaca.java`,
* `MonticuloArreglo.java`,
* `MonticuloMinimo.java`,
* `Pila.java` y
* `ValorIndexable.java`.

*No deben modificar de ninguna manera ninguno de los otros archivos de la
práctica*.

### Repositorio

Pueden clonar la práctica con el siguiente comando:

```shell
$ git clone https://aztlan.fciencias.unam.mx/gitlab/2019-2-edd/practica8.git
```

### Documentación

La documentación generada por JavaDoc la pueden consultar aquí:

[Documentación generada por JavaDoc para la práctica 8](https://aztlan.fciencias.unam.mx/~canek/2019-2-edd/practica8/apidocs/index.html)

### Capítulos del libro

El capítulo
[del libro](https://tienda.fciencias.unam.mx/es/home/437-estructuras-de-datos-con-java-moderno-9786073009157.html)
relacionado a esta práctica es:

19. Algoritmo de Dijkstra
