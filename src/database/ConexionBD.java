package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    // Configuración para Oracle
    private static final String URL = "jdbc:oracle:thin:@//DESKTOP-C132LPO:1521/XE";
    private static final String USER = "proyecto_integrador";
    private static final String PASSWORD = "proyecto_integrador";
    
    static {
        try {
            // Cargar el driver de Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Driver de Oracle cargado correctamente");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver de Oracle: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static Connection getConexion() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a Oracle Database");
        } catch (SQLException e) {
            System.out.println("Error al conectar con Oracle Database: " + e.getMessage());
            e.printStackTrace();
            
            // Mostrar información de diagnóstico
            System.out.println("URL: " + URL);
            System.out.println("Usuario: " + USER);
            System.out.println("Verifique que:");
            System.out.println("1. Oracle esté ejecutándose");
            System.out.println("2. El listener esté activo");
            System.out.println("3. Las credenciales sean correctas");
            System.out.println("4. La base de datos exista");
        }
        return conn;
    }
    
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexión a Oracle cerrada");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    // Método para probar la conexión
    public static void probarConexion() {
        try (Connection conn = getConexion()) {
            if (conn != null) {
                System.out.println("✓ Conexión a Oracle establecida correctamente");
                System.out.println("✓ Base de datos: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("✓ Versión: " + conn.getMetaData().getDatabaseProductVersion());
            } else {
                System.out.println("✗ No se pudo establecer conexión con Oracle");
            }
        } catch (SQLException e) {
            System.out.println("✗ Error al probar conexión: " + e.getMessage());
        }
    }
}
