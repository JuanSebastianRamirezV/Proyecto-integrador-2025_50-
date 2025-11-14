package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexionBD {
    // Configuración base
    private static final String BASE_URL = "jdbc:oracle:thin:@192.168.254.215:1521:orcl";
    
    // Configuración por tipo de usuario (nombres con _ICA)
    private enum TipoUsuario {
        PRODUCTOR("usuario_productor_ICA", "productor123"),
        INSPECTOR("usuario_inspector_ICA", "inspector123"),
        ADMIN("usuario_admin_ICA", "admin123");
        
        private final String usuario;
        private final String password;
        
        TipoUsuario(String usuario, String password) {
            this.usuario = usuario;
            this.password = password;
        }
        
        public String getUsuario() { return usuario; }
        public String getPassword() { return password; }
    }
    
    // Configuración original (para mantener compatibilidad)
    private static final String USER_ORIGINAL = "proyecto_integrador2025";
    private static final String PASSWORD_ORIGINAL = "proyecto_integrador2025";
    
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
    
    /**
     * MÉTODO ORIGINAL - Mantiene compatibilidad con código existente
     */
    public static Connection getConexion() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(BASE_URL, USER_ORIGINAL, PASSWORD_ORIGINAL);
            System.out.println("Conexión exitosa a Oracle Database (Usuario original)");
            
            // Mostrar información del usuario de BD
            mostrarInfoUsuarioBD(conn, "ORIGINAL");
            
        } catch (SQLException e) {
            System.out.println("Error al conectar con Oracle Database: " + e.getMessage());
            e.printStackTrace();
            
            // Mostrar información de diagnóstico
            System.out.println("URL: " + BASE_URL);
            System.out.println("Usuario: " + USER_ORIGINAL);
            System.out.println("Verifique que:");
            System.out.println("1. Oracle esté ejecutándose");
            System.out.println("2. El listener esté activo");
            System.out.println("3. Las credenciales sean correctas");
            System.out.println("4. La base de datos exista");
        }
        return conn;
    }
    
    /**
     * NUEVO MÉTODO - Conexión por tipo de usuario CON LOGS DETALLADOS
     */
    public static Connection getConexion(String tipoUsuario) {
        try {
            TipoUsuario tipo = TipoUsuario.valueOf(tipoUsuario.toUpperCase());
            
            System.out.println("\n=== SOLICITUD DE CONEXIÓN ===");
            System.out.println("Tipo Usuario Solicitado: " + tipoUsuario);
            System.out.println("Usuario BD a Usar: " + tipo.getUsuario());
            
            Connection conn = DriverManager.getConnection(BASE_URL, tipo.getUsuario(), tipo.getPassword());
            
            // Mostrar información detallada del usuario de BD
            mostrarInfoUsuarioBD(conn, tipoUsuario);
            
            return conn;
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Tipo de usuario no válido: " + tipoUsuario + " - Usando conexión por defecto");
            return getConexion(); // Fallback a conexión original
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar como " + tipoUsuario + ": " + e.getMessage());
            System.out.println("🔄 Fallando a conexión por defecto...");
            return getConexion(); // Fallback a conexión original
        }
    }
    
    /**
     * Método para obtener conexión basada en la sesión del usuario CON LOGS
     */
    public static Connection getConexionPorSesion() {
        try {
            // Intentar obtener el tipo de usuario de la sesión
            String tipoUsuario = controlador.SesionUsuario.getInstance().getTipoUsuario();
            if (tipoUsuario != null) {
                System.out.println("\n🔑 OBTENIENDO CONEXIÓN POR SESIÓN");
                System.out.println("Usuario en Sesión: " + controlador.SesionUsuario.getInstance().getUsuario());
                System.out.println("Tipo en Sesión: " + tipoUsuario);
                
                return getConexion(tipoUsuario);
            } else {
                System.out.println("⚠️ No hay sesión activa, usando conexión por defecto");
            }
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo obtener sesión, usando conexión por defecto: " + e.getMessage());
        }
        
        // Fallback a conexión original
        System.out.println("🔄 Usando conexión por defecto (fallback)");
        return getConexion();
    }
    
    /**
     * Método para mostrar información detallada del usuario de BD
     */
    private static void mostrarInfoUsuarioBD(Connection conn, String tipoUsuarioApp) {
        try {
            // Consulta 1: Usuario actual
            String sqlUsuario = "SELECT USER AS USUARIO_ACTUAL FROM DUAL";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String usuarioBD = rs.getString("USUARIO_ACTUAL");
                    System.out.println("✅ CONEXIÓN ESTABLECIDA EXITOSAMENTE");
                    System.out.println("📊 USUARIO BASE DE DATOS: " + usuarioBD);
                    System.out.println("👤 TIPO USUARIO APP: " + tipoUsuarioApp);
                    System.out.println("🔗 ESTADO: Conectado correctamente");
                    System.out.println("=====================================");
                    
                    // Verificar roles del usuario
                    verificarRolesUsuario(conn, usuarioBD);
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ No se pudo obtener información del usuario BD: " + e.getMessage());
        }
    }
    
    /**
     * Método para verificar los roles del usuario conectado
     */
    private static void verificarRolesUsuario(Connection conn, String usuarioBD) {
        try {
            String sqlRoles = "SELECT GRANTED_ROLE, ADMIN_OPTION " +
                             "FROM USER_ROLE_PRIVS " +
                             "WHERE USERNAME = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlRoles)) {
                pstmt.setString(1, usuarioBD);
                try (ResultSet rs = pstmt.executeQuery()) {
                    System.out.println("🎭 ROLES ASIGNADOS:");
                    boolean tieneRoles = false;
                    while (rs.next()) {
                        System.out.println("   - " + rs.getString("GRANTED_ROLE") + 
                                         " (Admin: " + rs.getString("ADMIN_OPTION") + ")");
                        tieneRoles = true;
                    }
                    if (!tieneRoles) {
                        System.out.println("   ⚠️ No se encontraron roles asignados");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ No se pudo verificar roles: " + e.getMessage());
        }
    }
    
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("🔒 Conexión a Oracle cerrada");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    // Método para probar la conexión (original)
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
    
    // Método para probar conexión por tipo de usuario
    public static void probarConexion(String tipoUsuario) {
        try (Connection conn = getConexion(tipoUsuario)) {
            if (conn != null) {
                System.out.println("✓ Conexión como " + tipoUsuario + " establecida correctamente");
                System.out.println("✓ Base de datos: " + conn.getMetaData().getDatabaseProductName());
            } else {
                System.out.println("✗ No se pudo establecer conexión como " + tipoUsuario);
            }
        } catch (SQLException e) {
            System.out.println("✗ Error al probar conexión como " + tipoUsuario + ": " + e.getMessage());
        }
    }
}