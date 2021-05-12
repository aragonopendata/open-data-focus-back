# visual-data-back

Una aplicación desarrollada en Java con Spring Boot que ofrece una API REST.

> PENDIENTE: explicar lo que hace y los sistemas con los que interactúa

> PENDIENTE: mini-explicación de cómo está estructurado el código

## Desarrollo local

Requisitos:

- Java 8

> PENDIENTE: explicar cómo configurarlo

## Puesta en marcha

### Compilación

> PENDIENTE: explicar cómo se compila. pasar del doc interno

### Despliegue

> PENDIENTE: pasos para desplegarlo

## Endpoints

- `/services`
    - `/charts`
        - `/` *GET*
        - `/type_chart` *GET*
        - `/{chart_id}` *GET*
        - `/save_chart` *POST*
        - `/save_process` *POST*
        - `/download_process/{chart_id}` *GET*
        - `/download_all_process` *GET*
        - `/remove_graph/{idProcess}` *GET*
        - `/saveTitle/{idProcess}` *GET*
    - `/ckan`
        - `/packageInfo` *POST*
        - `/packageResource` *POST*
        - `/packageList` *GET*
    - `/gaodc`
        - `/packageList` *GET*
        - `/packageInfo` *POST*
    - `/histories`
        - `/send_save_admin_mail` *POST*
        - `/send_save_user_mail` *POST*
        - `/send_public_user_mail` *POST*
        - `send_return_borrador_user_mail` *POST*
    - `/url`
        - `/packageInfo` *POST*
    - `/virtuoso`
        - `/packageResource` *POST*

## Mejoras de cara a futuro

- Comentar el código
- Documentar
- Cambiar ajustes de archivo a variables de entorno
- Caché
- Rate-limit
