# huffman

Compresor huffman implementado en Java.

## Configuración y ejecución

A continuación se detallan los requisitos y configuraciones para ejecutar el proyecto

### Prerrequisitos

El proyecto fué generado en java 1.8 

### Compilación

Las clases pueden ser cargadas en un IDE o compiladas desde la terminal. La clase main es *Compressor.java* 

### Ejecución

Asumiendo que se ha generado el ejecutable *huffman.jar*

La forma de invocar el programa para comprimir es:

```
java -jar huffman -c [-w <n>] <archivo_a_comprimir>
```

donde *n* indica el número de bytes que se ocupan para dividir el archivo. Como resultado de la ejecución se generan dos archivos:

* *<archivo>.jhc* - que contiene toda la información (mapa) necesaria para reconstruir el archivo original.
* *<archivo>_nomap.jhc* - que contiene únicamente la compresión del archivo. No es recuperable (generado únicamente con fines didácticos)

La forma de invocar el programa para descomprimir es:

```
java -jar huffman -d  <archivo_a_descomprimir>
```


