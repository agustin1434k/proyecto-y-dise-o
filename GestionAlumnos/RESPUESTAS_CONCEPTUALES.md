# Ejercicio 5 — Preguntas conceptuales

**1. ¿Qué función cumple JDBC?**
JDBC es la API de Java que nos permite conectarnos a una base de datos, mandarle consultas SQL y recibir los resultados desde nuestro programa.

**2. ¿Qué diferencia hay entre JDBC y MySQL Connector/J?**
JDBC es el estándar, o sea las interfaces y reglas que define Java. MySQL Connector/J es el driver que implementa ese estándar para poder hablar específicamente con MySQL.

**3. ¿Qué representa un objeto Connection?**
Representa la conexión abierta entre nuestro programa Java y la base de datos. A través de él creamos las consultas y las ejecutamos.

**4. ¿Para qué sirve PreparedStatement?**
Sirve para ejecutar consultas SQL con parámetros (los `?`). Es más seguro porque evita la inyección SQL y además permite reutilizar la consulta con distintos valores.

**5. ¿Para qué sirve ResultSet?**
Guarda el resultado de un SELECT. Lo recorremos fila por fila con `next()` y sacamos los datos de cada columna con métodos como `getInt()` o `getString()`.

**6. ¿Por qué debemos cerrar las conexiones?**
Porque cada conexión ocupa recursos del servidor y hay un límite de conexiones abiertas. Si no las cerramos, se puede saturar la base de datos. Con try-with-resources se cierran solas.
