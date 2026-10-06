# La guía de Jerry

App Android para consultar todos los personajes de Rick and Morty, hecha con Kotlin y Jetpack Compose sobre la Rick and Morty API.

<p align="center">
  <img width="260" alt="Listado de personajes" src="https://github.com/user-attachments/assets/06c416cf-b5d4-40ea-a139-55f830eb83b2" />
  &nbsp;&nbsp;
</p>

## Qué hace

- Listado paginado de personajes en un grid que se adapta al tamaño de pantalla.
- Detalle de cada personaje con su información y la lista de episodios en los que aparece.
- Búsqueda por nombre.
- Funciona sin conexión: lo que ya se ha cargado se puede seguir consultando con el modo avión activado.
- Tema propio con modo claro y oscuro.

## Arquitectura

MVVM con capas `data`, `domain` y `presentation`, e inyección de dependencias con Hilt. Las interfaces de los repositorios están en `domain` y sus implementaciones en `data`, así que los ViewModels no dependen de Retrofit ni de Room.

He usado un solo módulo porque, con este tamaño, modularizar no aportaba demasiado.

## Decisiones técnicas

- **Offline-first:** la lista se lee siempre de Room y un `RemoteMediator` de Paging 3 se encarga de descargar y guardar las páginas. Los datos se refrescan como mucho una vez al día.
- **Caché de episodios:** en el detalle solo se piden a la API los episodios que aún no están guardados, en una única llamada.
- **Búsqueda solo online:** buscar en Room solo encontraría los personajes ya cargados, así que la búsqueda consulta directamente la API.
- **Errores tipados:** los errores de red se traducen a errores de dominio, y la UI muestra un mensaje distinto para "sin conexión", "error del servidor" o "sin resultados".

## Stack

Kotlin, Jetpack Compose, Material 3, Hilt, Retrofit, Kotlin Serialization, Room, Paging 3, Coil y Navigation Compose.

## Tests

Tests unitarios del mapper, que es donde se interpretan los datos de la API.

## Cómo ejecutarlo

Abre el proyecto con una versión reciente de Android Studio (AGP 9, JDK 17) y ejecútalo. No hace falta ninguna API key.
