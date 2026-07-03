package com.example.final2.config;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DatabaseConfig implements AutoCloseable {

    /*
     * authenticationScheme=NTLM  → autenticación Windows pura Java (sin DLL nativa).
     * trustServerCertificate=true → evita error de certificado SSL en entornos locales.
     *
     * Si sigue fallando por TCP/IP deshabilitado en SQL Express:
     *   1. Abre "Administrador de configuración de SQL Server"
     *   2. Configuración de red de SQL Server -> Protocolos de SQLEXPRESS
     *   3. Habilita "TCP/IP" y reinicia el servicio SQL Server (SQLEXPRESS).
     */
    private static final String URL =
            "jdbc:sqlserver://localhost:1433;"
            + "databaseName=giann;"
            + "user=JUAPE;"
            + "password=123456789;"
            + "trustServerCertificate=true;"
            + "encrypt=false;";

    /*
     * OPCIÓN B: Autenticación de Windows (si la anterior falla)
     * Requiere que el usuario JUAPE tenga permisos en SQL Server.
     * private static final String URL =
     *         "jdbc:sqlserver://localhost:1433;databaseName=giann;integratedSecurity=true;trustServerCertificate=true;encrypt=false;";
     *
     * OPCIÓN C: Instancia SQLEXPRESS (o el nombre de tu instancia como SQLSERVER)
     * private static final String URL =
     *         "jdbc:sqlserver://localhost;instanceName=SQLEXPRESS;databaseName=giann;user=JUAPE;password=123456789;trustServerCertificate=true;encrypt=false;";
     */

    public Connection getConnection() throws SQLException {
        /*Connection conn = null;
        try{
            conn = DriverManager.getConnection(URL);
        }        catch (Exception e) {
            e.printStackTrace();
        }

        return conn;*/
        return DriverManager.getConnection(URL);
    }

    @Override
    public void close() {
        // Deregistrar drivers registrados por DriverManager para evitar fugas al parar la app
        try {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ignored) {
                    // Ignorar errores individuales al cerrar drivers
                }
            }
        } catch (Exception ignored) {
            // No se pudo limpiar drivers, pero la aplicación puede continuar
        }
    }
}
