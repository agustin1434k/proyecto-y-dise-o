# Entrega 3 — Preguntas conceptuales

1. **¿Qué significa CRUD?**
   Son las cuatro operaciones básicas sobre datos: Create (crear/insertar), Read (leer/consultar), Update (actualizar) y Delete (eliminar).

2. **¿Qué diferencia existe entre executeQuery() y executeUpdate()?**
   `executeQuery()` se usa con consultas SELECT y devuelve un `ResultSet` con las filas. `executeUpdate()` se usa con INSERT, UPDATE y DELETE y devuelve un número entero con la cantidad de filas afectadas.

3. **¿Por qué UPDATE debe utilizar una condición WHERE?**
   Porque el WHERE indica qué registro se modifica. Sin WHERE, el UPDATE cambia todas las filas de la tabla.

4. **¿Qué riesgo existe al ejecutar DELETE sin WHERE?**
   Se borran todos los registros de la tabla. Es una pérdida masiva de datos que, sin copia de seguridad, no se puede recuperar.

5. **¿Qué función cumple Alumno.java?**
   Es el modelo (POJO). Representa a un alumno con sus atributos (id, nombre, apellido, email, edad y curso) y permite transportar esos datos entre el menú, el DAO y la base de datos.

6. **¿Qué responsabilidad tiene AlumnoDAO?**
   Concentra todo el acceso a la base de datos: insertar, listar, actualizar, eliminar, buscar y calcular estadísticas. Así el resto del programa no necesita saber SQL.

7. **¿Por qué PreparedStatement es preferible a concatenar datos?**
   Porque evita la inyección SQL: los datos viajan como parámetros (`?`) y no como parte del texto de la consulta. Además es más claro, maneja bien los tipos de datos y la base puede reutilizar la consulta.

## Arquitectura

Usuario → Main → Alumno → AlumnoDAO → JDBC → MySQL/MariaDB

- **Main** (vista): muestra el menú, pide datos y valida entradas.
- **Alumno** (modelo): guarda los datos de un alumno.
- **AlumnoDAO**: ejecuta las consultas SQL.
- **ConexionBD**: entrega la conexión JDBC a la base `escuela_java`.

No se escribe SQL en Main para separar responsabilidades: si cambia la base o la interfaz (Swing en la Entrega 4), no hay que reescribir todo. El patrón DAO aísla el acceso a datos, facilita el mantenimiento, evita repetir código y permite reutilizar el mismo DAO con otra interfaz.
