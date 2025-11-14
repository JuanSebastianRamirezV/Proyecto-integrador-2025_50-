package modelo;

import database.ConexionBD;
import controlador.SesionUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LugarProduccionInspeccionDAO {
    
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
    
    public boolean insertar(LugarProduccionInspeccion lpi) {
        logOperacion("INSERTAR RELACIÓN LUGAR-INSPECCIÓN");
        String sql = "INSERT INTO LugarProduccion_Inspeccion (id_lugar_produccion, id_inspeccion) VALUES (?, ?)";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, lpi.getIdLugarProduccion());
            stmt.setInt(2, lpi.getIdInspeccion());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar relación lugar-producción-inspección: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idLugarProduccion, int idInspeccion) {
        logOperacion("ELIMINAR RELACIÓN LUGAR-INSPECCIÓN");
        String sql = "DELETE FROM LugarProduccion_Inspeccion WHERE id_lugar_produccion = ? AND id_inspeccion = ?";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idLugarProduccion);
            stmt.setInt(2, idInspeccion);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar relación lugar-producción-inspección: " + e.getMessage());
            return false;
        }
    }

    public List<LugarProduccionInspeccion> obtenerPorLugarProduccion(int idLugarProduccion) {
        logOperacion("CONSULTAR RELACIONES POR LUGAR PRODUCCIÓN");
        List<LugarProduccionInspeccion> relaciones = new ArrayList<>();
        String sql = "SELECT * FROM LugarProduccion_Inspeccion WHERE id_lugar_produccion = ?";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idLugarProduccion);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                LugarProduccionInspeccion lpi = new LugarProduccionInspeccion();
                lpi.setIdLugarProduccion(rs.getInt("id_lugar_produccion"));
                lpi.setIdInspeccion(rs.getInt("id_inspeccion"));
                relaciones.add(lpi);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener relaciones por lugar de producción: " + e.getMessage());
        }
        
        return relaciones;
    }

    public List<LugarProduccionInspeccion> obtenerPorInspeccion(int idInspeccion) {
        logOperacion("CONSULTAR RELACIONES POR INSPECCIÓN");
        List<LugarProduccionInspeccion> relaciones = new ArrayList<>();
        String sql = "SELECT * FROM LugarProduccion_Inspeccion WHERE id_inspeccion = ?";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idInspeccion);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                LugarProduccionInspeccion lpi = new LugarProduccionInspeccion();
                lpi.setIdLugarProduccion(rs.getInt("id_lugar_produccion"));
                lpi.setIdInspeccion(rs.getInt("id_inspeccion"));
                relaciones.add(lpi);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener relaciones por inspección: " + e.getMessage());
        }
        
        return relaciones;
    }
}