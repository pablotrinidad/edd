# Proyecto 1

Ordenador lexicográfico que simula el comportamiento del
comando `sort` en UNIX.

## Instalación

El proyecto puede construirse y probarse utilizando maven.
Los siguientes comandos están disponibles:

* `mvn test`: Ejecuta las pruebas unitarias.
* `mvn package`: Ejecuta pruebas unitarias y crea el paquete `sort.jar`
    dentro del folder objetivo `target/`.

## Uso

El jar puede ser ejecutado utilizando el comando

```
java -jar sort.jar
```

Además, se puede ejecutar junto con la bandera `-h` para
obtener más información acerca del uso del mismo.

Algunos ejemplos son:
* `... sort.jar -o out.txt input.txt`: Ordena el contenido de `input.txt` en `out.txt`
* `... sort.jar -r input.txt`: Ordena de forma descendentemente el contenido de `input.txt`
* `... sort.jar a.txt b.txt c.txt` Ordena el contenido concatenado de `a.txt`, `b.txt` y `c.txt`
* `cat input.txt | ... sort.jar` Ordena el contenido proveído a través de la entrada estándard.

## Autores

* [**Pablo Trinidad**](https://github.com/pablotrinidad): Número de cuenta 419004279