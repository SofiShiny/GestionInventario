# GestionInventario
Sistema de Gestion de inventario que incluye CRUD de productos, proveedores, compras y ventas


Sobre la arquitectura del sistema

-Abrir la carpeta app para ver los contenedores del sistema (java y res)

-Dentro de java se encuentran varios componentes, productos, proveedores, ventas, compras y navegacion. Dentro de cada componente se encuentra el codigo relacionado a cada aspecto

-Dentro de res se enuentran los componentes relacionados con la interfaz del sistema, solo incluye xml y algunos png

-Utiliza una base de datos RealtimeDatabase de Firebase




Sobre los requisitos funcionales del sistema

-Registrar, modificar, eliminar, buscar y consultar productos, proveedores, compras y ventas

-Las ventas y compras se registran automaticamente cuando el usuario agrega o elimina existencias de un producto

-Los usuarios se registran en la aplicacion a traves de los servicios de Google





Sobre los requisitos no funcionales del sistema

-El usuario puede agregar imagenes a los productos y proveedores si lo desea y tambien modificarlas

-Aparecen mensajes de confirmacion en los procesos delicados o importantes

-Aparecen mensajes de error detallados cuando las verificaciones y validaciones implementadas no se cumplen





Limitaciones

-La aplicacion solo funciona para android

-No se realizaron suficientes casos de prueba

-El usuario debe tener conexion a internet al leer o escribir informacion en la base de datos

