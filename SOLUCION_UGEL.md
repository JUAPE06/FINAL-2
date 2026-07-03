[SOLUCION_UGEL.md](SOLUCION_UGEL.md)### Propuesta Tecnológica: Sistema de Gestión de Deudas Administrativas - UGEL Ilo

#### 1. Identificación de Necesidades y Requerimientos
*   **RF1: Gestión de Docentes:** El sistema permite registrar y consultar la información básica de los docentes (DNI, Nombres, Especialidad, Condición).
*   **RF2: Registro de Deudas:** Los administradores pueden registrar nuevas deudas asociadas a un docente, especificando concepto, monto y fecha.
*   **RF3: Edición de Deudas:** Permite actualizar el estado de una deuda (Pendiente -> Pagado) o corregir información errónea.
*   **RF4: Consulta y Filtros:** Búsqueda avanzada de deudas por docente, concepto o estado para facilitar la toma de decisiones.
*   **RF5: Eliminación Masiva:** Funcionalidad para depurar registros seleccionados de manera eficiente.

#### 2. Modelo de Base de Datos
Se ha diseñado un modelo relacional en SQL Server con integridad referencial:
*   **Tabla Docente:** Almacena la entidad principal de los beneficiarios.
*   **Tabla Deuda:** Almacena los compromisos económicos, vinculada a `Docente` mediante una clave foránea (`idDocente`).

#### 3. Arquitectura del Sistema
El sistema sigue un patrón de arquitectura en capas (N-Tier) para asegurar la escalabilidad y el mantenimiento:
*   **Capa de Presentación:** JavaFX (FXML + Controllers).
*   **Capa de Negocio (Service):** Interfaces y servicios que encapsulan la lógica de gestión.
*   **Capa de Persistencia (Repository):** Implementación de JDBC para interactuar con la base de datos SQL Server.
*   **Inyección de Dependencias:** Gestionada manualmente a través de la clase `AppContext`.

#### 4. Algoritmo de Flujo: Registro de Deuda
1.  **Inicio**
2.  Solicitar datos: `Docente`, `Concepto`, `Monto`, `Fecha`.
3.  **Validación:**
    *   ¿El Docente está seleccionado? (No -> Error)
    *   ¿El Concepto no está vacío? (No -> Error)
    *   ¿El Monto es un número positivo? (No -> Error)
4.  Si las validaciones fallan, mostrar alertas y volver al paso 2.
5.  Si los datos son correctos:
    *   Crear objeto `Deuda`.
    *   Ejecutar sentencia SQL `INSERT`.
    *   Actualizar vista de la tabla.
6.  **Fin**

#### 5. Alineación con los Objetivos de Desarrollo Sostenible (ODS)
*   **ODS 4 - Educación de Calidad:** Al optimizar la gestión administrativa, se reduce la carga burocrática sobre el sistema educativo, indirectamente beneficiando la calidad del servicio docente.
*   **ODS 8 - Trabajo Decente:** Asegura el cumplimiento de los compromisos financieros con los docentes, protegiendo sus derechos laborales.
*   **ODS 16 - Instituciones Sólidas:** Promueve la transparencia y la eficiencia en la administración pública mediante el uso de tecnologías de la información.
