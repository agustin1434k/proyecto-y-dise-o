# Entrega 2 – Preguntas teóricas

1. **¿Qué función cumple JDBC?** Es la API de Java que permite conectarse a una base de datos relacional y ejecutar sentencias SQL desde el código, de forma independiente del motor.
2. **¿Qué información contiene la URL de conexión?** El protocolo/motor (`jdbc:mysql`), el servidor (`localhost`), el puerto (`3306`), el nombre de la base (`finanzas_personales`) y parámetros opcionales (zona horaria, SSL).
3. **¿Para qué sirve Connection?** Representa la conexión abierta con la base; a partir de ella se crean los `PreparedStatement` y se controlan las transacciones.
4. **¿Diferencia entre PreparedStatement y ResultSet?** `PreparedStatement` es la sentencia SQL precompilada con parámetros `?` que se envía a la base; `ResultSet` es el conjunto de filas que devuelve una consulta SELECT.
5. **¿Por qué try-with-resources?** Cierra automáticamente Connection, PreparedStatement y ResultSet, incluso si ocurre un error, evitando fugas de recursos.
6. **¿Por qué BigDecimal para importes?** `double` comete errores de redondeo en decimales binarios; `BigDecimal` representa valores monetarios con exactitud.
7. **¿Qué devuelve executeUpdate()?** La cantidad de filas afectadas (en un INSERT exitoso, 1).
8. **¿Qué sucede si el id_cuenta no existe?** MySQL rechaza el INSERT por la clave foránea y Java lanza una `SQLException` (foreign key constraint fails); no se guarda nada.
9. **¿Diferencia entre modelo y DAO?** El modelo (`Movimiento`) representa los datos; el DAO (`MovimientoDAO`) contiene el acceso a la base (consultas e inserciones SQL).
10. **¿Qué se construye en la Entrega 3?** La interfaz gráfica con Swing que usará esta lógica.
