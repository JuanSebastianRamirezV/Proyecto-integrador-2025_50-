package modelo;

import database.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LugarProduccionInspeccionDAO {
    
    public boolean insertar(LugarProduccionInspeccion lpi) {
        String sql = "INSERT INTO LugarProduccion_Inspeccion (id_lugar_produccion, id_inspeccion) VALUES (?, ?)";
        
        try (Connection conn = ConexionBD.getConexion();
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
        String sql = "DELETE FROM LugarProduccion_Inspeccion WHERE id_lugar_produccion = ? AND id_inspeccion = ?";
        
        try (Connection conn = ConexionBD.getConexion();
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
        List<LugarProduccionInspeccion> relaciones = new ArrayList<>();
        String sql = "SELECT * FROM LugarProduccion_Inspeccion WHERE id_lugar_produccion = ?";
        
        try (Connection conn = ConexionBD.getConexion();
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
        List<LugarProduccionInspeccion> relaciones = new ArrayList<>();
        String sql = "SELECT * FROM LugarProduccion_Inspeccion WHERE id_inspeccion = ?";
        
        try (Connection conn = ConexionBD.getConexion();
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