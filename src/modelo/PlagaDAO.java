package modelo;

import database.ConexionBD;
import controlador.SesionUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlagaDAO {
    
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
    
    public boolean insertar(Plaga plaga) {
        logOperacion("INSERTAR PLAGA");
        // ✅ PRIMERO validar si ya existe
        if (existeNombrePlaga(plaga.getNombrePlaga())) {
            System.out.println("Error: Ya existe una plaga con el nombre: " + plaga.getNombrePlaga());
            return false;
        }

        String sql = "INSERT INTO PLAGA (ID_PLAGA, NOMBRE_PLAGA, TIPO_PLAGA, CULTIVOS_ASOCIADOS) VALUES (SEQ_PLAGA.NEXTVAL, ?, ?, ?)";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plaga.getNombrePlaga());
            stmt.setString(2, plaga.getTipoPlaga());
            stmt.setString(3, plaga.getCultivosAsociados());

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar plaga: " + e.getMessage());
            System.out.println("SQL: " + sql);
            return false;
        }
    }

    public boolean actualizar(Plaga plaga) {
        logOperacion("ACTUALIZAR PLAGA");
        String sql = "UPDATE PLAGA SET NOMBRE_PLAGA = ?, TIPO_PLAGA = ?, CULTIVOS_ASOCIADOS = ? WHERE ID_PLAGA = ?";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, plaga.getNombrePlaga());
            stmt.setString(2, plaga.getTipoPlaga());
            stmt.setString(3, plaga.getCultivosAsociados());
            stmt.setInt(4, plaga.getIdPlaga());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar plaga: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idPlaga) {
        // Primero verificar si hay registros relacionados en RELACION_ASOCIADOS
        if (tieneRegistrosRelacionadosPlaga(idPlaga)) {
            System.out.println("No se puede eliminar la plaga porque tiene registros relacionados");
            return false;
        }

        // Si no hay registros relacionados, proceder con la eliminación
        String sql = "DELETE FROM plaga WHERE id_plaga = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPlaga);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Plaga eliminada correctamente");
                return true;
            } else {
                System.out.println("No se encontró la plaga con ID: " + idPlaga);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar plaga: " + e.getMessage());
            return false;
        }
    }

    private boolean tieneRegistrosRelacionadosPlaga(int idPlaga) {
        String sql = "SELECT COUNT(*) FROM relacion_asociados WHERE id_plaga = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPlaga);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Existen " + rs.getInt(1) + " registro(s) relacionados en RELACION_ASOCIADOS");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar registros relacionados: " + e.getMessage());
            return true; // Por seguridad, asumimos que hay relaciones si hay error
        }
        return false;
    }

    public Plaga obtenerPorId(int idPlaga) {
        logOperacion("CONSULTAR PLAGA POR ID");
        String sql = "SELECT * FROM PLAGA WHERE ID_PLAGA = ?";
        Plaga plaga = null;
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPlaga);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                plaga = mapearPlaga(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener plaga por ID: " + e.getMessage());
        }
        
        return plaga;
    }

    public List<Plaga> obtenerTodos() {
        logOperacion("CONSULTAR TODAS LAS PLAGAS");
        List<Plaga> plagas = new ArrayList<>();
        String sql = "SELECT * FROM PLAGA ORDER BY ID_PLAGA asc";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Plaga plaga = mapearPlaga(rs);
                plagas.add(plaga);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todas las plagas: " + e.getMessage());
        }
        
        return plagas;
    }

    public List<Plaga> buscar(String criterio) {
        logOperacion("BUSCAR PLAGAS");
        List<Plaga> plagas = new ArrayList<>();
        String sql = "SELECT * FROM PLAGA WHERE UPPER(NOMBRE_PLAGA) LIKE UPPER(?) OR UPPER(TIPO_PLAGA) LIKE UPPER(?) OR UPPER(CULTIVOS_ASOCIADOS) LIKE UPPER(?) ORDER BY NOMBRE_PLAGA";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);
            stmt.setString(2, likeCriterio);
            stmt.setString(3, likeCriterio);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Plaga plaga = mapearPlaga(rs);
                plagas.add(plaga);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar plagas: " + e.getMessage());
        }
        
        return plagas;
    }

    public List<Plaga> obtenerPorTipo(String tipoPlaga) {
        logOperacion("CONSULTAR PLAGAS POR TIPO");
        List<Plaga> plagas = new ArrayList<>();
        String sql = "SELECT * FROM PLAGA WHERE UPPER(TIPO_PLAGA) = UPPER(?) ORDER BY NOMBRE_PLAGA";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipoPlaga);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Plaga plaga = mapearPlaga(rs);
                plagas.add(plaga);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener plagas por tipo: " + e.getMessage());
        }
        
        return plagas;
    }

    public List<Plaga> obtenerPorCultivo(String cultivo) {
        logOperacion("CONSULTAR PLAGAS POR CULTIVO");
        List<Plaga> plagas = new ArrayList<>();
        String sql = "SELECT * FROM PLAGA WHERE UPPER(CULTIVOS_ASOCIADOS) LIKE UPPER(?) ORDER BY NOMBRE_PLAGA";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + cultivo + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Plaga plaga = mapearPlaga(rs);
                plagas.add(plaga);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener plagas por cultivo: " + e.getMessage());
        }
        
        return plagas;
    }

    public boolean existeNombrePlaga(String nombrePlaga) {
        String sql = "SELECT COUNT(*) FROM PLAGA WHERE UPPER(NOMBRE_PLAGA) = UPPER(?)";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombrePlaga);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar nombre de plaga: " + e.getMessage());
        }
        
        return false;
    }

    public List<String> obtenerTiposPlaga() {
        logOperacion("CONSULTAR TIPOS DE PLAGA");
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT DISTINCT TIPO_PLAGA FROM PLAGA WHERE TIPO_PLAGA IS NOT NULL ORDER BY TIPO_PLAGA";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tipos.add(rs.getString("TIPO_PLAGA"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener tipos de plaga: " + e.getMessage());
        }
        
        return tipos;
    }

    public int contarPlagas() {
        logOperacion("CONTAR PLAGAS");
        String sql = "SELECT COUNT(*) FROM PLAGA";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar plagas: " + e.getMessage());
        }
        
        return 0;
    }

    private Plaga mapearPlaga(ResultSet rs) throws SQLException {
        Plaga plaga = new Plaga();
        plaga.setIdPlaga(rs.getInt("ID_PLAGA"));
        plaga.setNombrePlaga(rs.getString("NOMBRE_PLAGA"));
        plaga.setTipoPlaga(rs.getString("TIPO_PLAGA"));
        plaga.setCultivosAsociados(rs.getString("CULTIVOS_ASOCIADOS"));
        return plaga;
    }
}