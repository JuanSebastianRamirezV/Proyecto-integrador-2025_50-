package modelo;

import database.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InspectorDAO {
    
    public boolean insertar(Inspector inspector) {
        // ✅ PRIMERO validar si ya existe un inspector con ese número de documento
        if (existeNumeroDocumento(inspector.getNumeroDocumento())) {
            System.out.println("Error: Ya existe un inspector con el número de documento: " + inspector.getNumeroDocumento());
            return false;
        }

        String sql = "INSERT INTO INSPECTORES (ID_INSPECTOR, TIPO_DOCUMENTO, NUMERO_DOCUMENTO, NOMBRES_COMPLETOS, TELEFONO, NUMERO_TARJETA_PROFESIONAL, ID_SEDE) VALUES (SEQ_INSPECTORES.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inspector.getTipoDocumento());
            stmt.setString(2, inspector.getNumeroDocumento());
            stmt.setString(3, inspector.getNombresCompletos());
            stmt.setString(4, inspector.getTelefono());
            stmt.setString(5, inspector.getNumeroTarjetaProfesional());
            stmt.setInt(6, inspector.getIdSede());

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar inspector: " + e.getMessage());
            System.out.println("SQL: " + sql);
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
        // Primero verificar si hay registros relacionados en INSPECCION
        if (tieneRegistrosRelacionadosInspector(idInspector)) {
            System.out.println("No se puede eliminar el inspector porque tiene inspecciones relacionadas");
            return false;
        }

        // Si no hay registros relacionados, proceder con la eliminación
        String sql = "DELETE FROM inspectores WHERE id_inspector = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idInspector);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Inspector eliminado correctamente");
                return true;
            } else {
                System.out.println("No se encontró el inspector con ID: " + idInspector);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar inspector: " + e.getMessage());
            return false;
        }
    }

    private boolean tieneRegistrosRelacionadosInspector(int idInspector) {
        String sql = "SELECT COUNT(*) FROM inspeccion WHERE id_inspector = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idInspector);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Existen " + rs.getInt(1) + " inspección(es) relacionadas con este inspector");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar inspecciones relacionadas: " + e.getMessage());
            return true; // Por seguridad, asumimos que hay relaciones si hay error
        }
        return false;
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
        String sql = "SELECT * FROM INSPECTORES ORDER BY id_inspector asc";
        
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
    
    public boolean existeNumeroDocumento(String numeroDocumento) {
        String sql = "SELECT COUNT(*) FROM INSPECTORES WHERE NUMERO_DOCUMENTO = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numeroDocumento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar número de documento de inspector: " + e.getMessage());
        }

        return false;
    }
    
    public boolean existeInspector(String numeroDocumento) {
        String sql = "SELECT COUNT(*) FROM INSPECTORES WHERE NUMERO_DOCUMENTO = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numeroDocumento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar número de documento de inspector: " + e.getMessage());
        }

        return false;
    }
}