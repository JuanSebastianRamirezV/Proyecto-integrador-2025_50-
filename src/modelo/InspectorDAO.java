package modelo;

import database.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InspectorDAO {
    
    public boolean insertar(Inspector inspector) {
        String sql = "INSERT INTO INSPECTORES (TIPO_DOCUMENTO, NUMERO_DOCUMENTO, NOMBRES_COMPLETOS, TELEFONO, NUMERO_TARJETA_PROFESIONAL, ID_SEDE) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // NO establecer ID_INSPECTOR - se generará automáticamente
            stmt.setString(1, inspector.getTipoDocumento());
            stmt.setString(2, inspector.getNumeroDocumento());
            stmt.setString(3, inspector.getNombresCompletos());
            stmt.setString(4, inspector.getTelefono());
            stmt.setString(5, inspector.getNumeroTarjetaProfesional());
            stmt.setInt(6, inspector.getIdSede());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar inspector: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Inspector inspector) {
        String sql = "UPDATE INSPECTORES SET tipo_documento = ?, numero_documento = ?, nombres_completos = ?, telefono = ?, numero_tarjeta_profesional = ?, id_sede = ? WHERE id_inspector = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, inspector.getTipoDocumento());
            stmt.setString(2, inspector.getNumeroDocumento());
            stmt.setString(3, inspector.getNombresCompletos());
            stmt.setString(4, inspector.getTelefono());
            stmt.setString(5, inspector.getNumeroTarjetaProfesional());
            stmt.setInt(6, inspector.getIdSede());
            stmt.setInt(7, inspector.getIdInspector());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar inspector: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idInspector) {
        String sql = "DELETE FROM INSPECTORES WHERE id_inspector = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idInspector);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar inspector: " + e.getMessage());
            return false;
        }
    }

    public Inspector obtenerPorId(int idInspector) {
        String sql = "SELECT * FROM INSPECTORES WHERE id_inspector = ?";
        Inspector inspector = null;
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idInspector);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                inspector = new Inspector();
                inspector.setIdInspector(rs.getInt("id_inspector"));
                inspector.setTipoDocumento(rs.getString("tipo_documento"));
                inspector.setNumeroDocumento(rs.getString("numero_documento"));
                inspector.setNombresCompletos(rs.getString("nombres_completos"));
                inspector.setTelefono(rs.getString("telefono"));
                inspector.setNumeroTarjetaProfesional(rs.getString("numero_tarjeta_profesional"));
                inspector.setIdSede(rs.getInt("id_sede"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener inspector: " + e.getMessage());
        }
        
        return inspector;
    }

    public List<Inspector> obtenerTodos() {
        List<Inspector> inspectores = new ArrayList<>();
        String sql = "SELECT * FROM INSPECTORES ORDER BY nombres_completos";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Inspector inspector = new Inspector();
                inspector.setIdInspector(rs.getInt("id_inspector"));
                inspector.setTipoDocumento(rs.getString("tipo_documento"));
                inspector.setNumeroDocumento(rs.getString("numero_documento"));
                inspector.setNombresCompletos(rs.getString("nombres_completos"));
                inspector.setTelefono(rs.getString("telefono"));
                inspector.setNumeroTarjetaProfesional(rs.getString("numero_tarjeta_profesional"));
                inspector.setIdSede(rs.getInt("id_sede"));
                inspectores.add(inspector);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener inspectores: " + e.getMessage());
        }
        
        return inspectores;
    }

    public List<Inspector> buscar(String criterio) {
        List<Inspector> inspectores = new ArrayList<>();
        String sql = "SELECT * FROM INSPECTORES WHERE nombres_completos LIKE ? OR numero_documento LIKE ? OR numero_tarjeta_profesional LIKE ? ORDER BY nombres_completos";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);
            stmt.setString(2, likeCriterio);
            stmt.setString(3, likeCriterio);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Inspector inspector = new Inspector();
                inspector.setIdInspector(rs.getInt("id_inspector"));
                inspector.setTipoDocumento(rs.getString("tipo_documento"));
                inspector.setNumeroDocumento(rs.getString("numero_documento"));
                inspector.setNombresCompletos(rs.getString("nombres_completos"));
                inspector.setTelefono(rs.getString("telefono"));
                inspector.setNumeroTarjetaProfesional(rs.getString("numero_tarjeta_profesional"));
                inspector.setIdSede(rs.getInt("id_sede"));
                inspectores.add(inspector);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar inspectores: " + e.getMessage());
        }
        
        return inspectores;
    }
}