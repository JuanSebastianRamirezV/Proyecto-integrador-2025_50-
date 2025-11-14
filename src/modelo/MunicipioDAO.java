package modelo;

import database.ConexionBD;
import controlador.SesionUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MunicipioDAO {
    
    /**
     * Obtiene conexión según el usuario en sesión
     */
    private Connection getConexion() throws SQLException {
        return ConexionBD.getConexionPorSesion();
    }
    
    /**
     * Verifica permisos antes de operaciones (opcional, para logging)
     */
    private void logOperacion(String operacion) {
        try {
            String tipoUsuario = SesionUsuario.getInstance().getTipoUsuario();
            System.out.println("🔐 " + operacion + " - Usuario: " + tipoUsuario);
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo obtener información de sesión");
        }
    }
    
    public int insertar(Municipio municipio) {
    logOperacion("INSERTAR MUNICIPIO");
    String sql = "INSERT INTO MUNICIPIO (ID_MUNICIPIO, NOMBRE) VALUES (SEQ_MUNICIPIO.NEXTVAL, ?)";
    int idGenerado = -1;

    try (Connection conn = getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID_MUNICIPIO"})) {

        stmt.setString(1, municipio.getNombre().trim());
        int filasAfectadas = stmt.executeUpdate();
        
        if (filasAfectadas > 0) {
            // OBTENER EL ID GENERADO - ESTA PARTE ES CLAVE
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idGenerado = generatedKeys.getInt(1);
                    municipio.setIdMunicipio(idGenerado);
                    System.out.println("✅ Municipio insertado con ID: " + idGenerado);
                } else {
                    System.out.println("⚠️ No se pudo obtener el ID generado");
                }
            }
        } else {
            System.out.println("❌ No se insertó ninguna fila");
        }

    } catch (SQLException e) {
        System.out.println("❌ Error al insertar municipio: " + e.getMessage());
        System.out.println("SQL State: " + e.getSQLState());
        System.out.println("Error Code: " + e.getErrorCode());
        
        // Manejo específico de errores Oracle
        if (e.getErrorCode() == 1) { // ORA-00001: unique constraint violated
            System.out.println("⚠️ Error: Ya existe un municipio con ese nombre");
        } else if (e.getErrorCode() == 1400) { // ORA-01400: cannot insert NULL
            System.out.println("⚠️ Error: El nombre del municipio no puede ser nulo");
        } else if (e.getErrorCode() == 2291) { // ORA-02291: integrity constraint violated
            System.out.println("⚠️ Error: Violación de clave foránea");
        }
    }
    
    return idGenerado;
}


    public boolean actualizar(Municipio municipio) {
        logOperacion("ACTUALIZAR MUNICIPIO");
        String sql = "UPDATE MUNICIPIO SET NOMBRE = ? WHERE ID_MUNICIPIO = ?";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, municipio.getNombre());
            stmt.setInt(2, municipio.getIdMunicipio());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar municipio: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idMunicipio) {
        // Verificar y obtener conteo de registros relacionados
        int countPredios = contarPrediosRelacionados(idMunicipio);

        if (countPredios > 0) {
            System.out.println("No se puede eliminar el municipio. Tiene 1 o mas predios relacionados");
            return false;
        }

        // Si no hay registros relacionados, proceder con la eliminación
        String sql = "DELETE FROM municipio WHERE id_municipio = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMunicipio);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Municipio eliminado correctamente");
                return true;
            } else {
                System.out.println("No se encontró el municipio con ID: " + idMunicipio);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar municipio: " + e.getMessage());
            return false;
        }
    }

    private int contarPrediosRelacionados(int idMunicipio) {
        String sql = "SELECT COUNT(*) FROM predio WHERE id_municipio = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMunicipio);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar predios relacionados: " + e.getMessage());
        }
        return 0;
    }

    public Municipio obtenerPorId(int idMunicipio) {
        logOperacion("CONSULTAR MUNICIPIO POR ID");
        String sql = "SELECT * FROM MUNICIPIO WHERE ID_MUNICIPIO = ?";
        Municipio municipio = null;
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMunicipio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                municipio = mapearMunicipio(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener municipio por ID: " + e.getMessage());
        }
        
        return municipio;
    }

    public List<Municipio> obtenerTodos() {
        logOperacion("CONSULTAR TODOS LOS MUNICIPIOS");
        List<Municipio> municipios = new ArrayList<>();
        String sql = "SELECT * FROM MUNICIPIO ORDER BY ID_MUNICIPIO asc";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Municipio municipio = mapearMunicipio(rs);
                municipios.add(municipio);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los municipios: " + e.getMessage());
        }
        
        return municipios;
    }

    public List<Municipio> buscar(String criterio) {
        logOperacion("BUSCAR MUNICIPIOS");
        List<Municipio> municipios = new ArrayList<>();
        String sql = "SELECT * FROM MUNICIPIO WHERE UPPER(NOMBRE) LIKE UPPER(?) ORDER BY NOMBRE";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Municipio municipio = mapearMunicipio(rs);
                municipios.add(municipio);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar municipios: " + e.getMessage());
        }
        
        return municipios;
    }

    public boolean existeNombreMunicipio(String nombre) {
        String sql = "SELECT COUNT(*) FROM MUNICIPIO WHERE UPPER(NOMBRE) = UPPER(?)";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar nombre de municipio: " + e.getMessage());
        }
        
        return false;
    }

    public int contarMunicipios() {
        logOperacion("CONTAR MUNICIPIOS");
        String sql = "SELECT COUNT(*) FROM MUNICIPIO";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar municipios: " + e.getMessage());
        }
        
        return 0;
    }

    public List<Municipio> obtenerMunicipiosConPredios() {
        logOperacion("CONSULTAR MUNICIPIOS CON PREDIOS");
        List<Municipio> municipios = new ArrayList<>();
        String sql = "SELECT DISTINCT m.* FROM MUNICIPIO m " +
                    "INNER JOIN PREDIO p ON m.ID_MUNICIPIO = p.ID_MUNICIPIO " +
                    "ORDER BY m.NOMBRE";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Municipio municipio = mapearMunicipio(rs);
                municipios.add(municipio);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener municipios con predios: " + e.getMessage());
        }
        
        return municipios;
    }

    private Municipio mapearMunicipio(ResultSet rs) throws SQLException {
        Municipio municipio = new Municipio();
        municipio.setIdMunicipio(rs.getInt("ID_MUNICIPIO"));
        municipio.setNombre(rs.getString("NOMBRE"));
        return municipio;
    }
    /**
     * Método para diagnóstico - verifica la conexión y permisos
     */
    public boolean verificarConexionYPermisos() {
        try (Connection conn = getConexion()) {
            System.out.println("✅ Conexión establecida correctamente");
            
            // Verificar permisos de inserción
            String testSql = "SELECT COUNT(*) FROM MUNICIPIO WHERE ROWNUM = 1";
            try (PreparedStatement stmt = conn.prepareStatement(testSql);
                 ResultSet rs = stmt.executeQuery()) {
                System.out.println("✅ Permisos de consulta verificados");
            }
            
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión/permisos: " + e.getMessage());
            return false;
        }
    }
}

