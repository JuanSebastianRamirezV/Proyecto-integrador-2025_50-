package modelo;

import database.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Data Access Object (DAO) para la gestión de relaciones entre Plagas y Cultivos en la base de datos.
 * Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre la tabla Plaga_Cultivo.
 * 
 * @author TuNombre
 * @version 1.0
 */
public class PlagaCultivoDAO {
    
    /**
     * Inserta una nueva relación plaga-cultivo en la base de datos.
     * 
     * @param pc Objeto PlagaCultivo que contiene los datos de la relación a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(PlagaCultivo pc) {
        String sql = "INSERT INTO Plaga_Cultivo (id_plaga, id_cultivo, nivel_afectacion, recomendaciones) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pc.getIdPlaga());
            stmt.setInt(2, pc.getIdCultivo());
            stmt.setString(3, pc.getNivelAfectacion());
            stmt.setString(4, pc.getRecomendaciones());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar relación plaga-cultivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza una relación plaga-cultivo existente en la base de datos.
     * 
     * @param pc Objeto PlagaCultivo que contiene los datos actualizados de la relación
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizar(PlagaCultivo pc) {
        String sql = "UPDATE Plaga_Cultivo SET nivel_afectacion = ?, recomendaciones = ? WHERE id_plaga = ? AND id_cultivo = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, pc.getNivelAfectacion());
            stmt.setString(2, pc.getRecomendaciones());
            stmt.setInt(3, pc.getIdPlaga());
            stmt.setInt(4, pc.getIdCultivo());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar relación plaga-cultivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una relación plaga-cultivo de la base de datos.
     * 
     * @param idPlaga Identificador de la plaga
     * @param idCultivo Identificador del cultivo
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminar(int idPlaga, int idCultivo) {
        String sql = "DELETE FROM Plaga_Cultivo WHERE id_plaga = ? AND id_cultivo = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPlaga);
            stmt.setInt(2, idCultivo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar relación plaga-cultivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una relación plaga-cultivo específica por sus identificadores.
     * 
     * @param idPlaga Identificador de la plaga
     * @param idCultivo Identificador del cultivo
     * @return Objeto PlagaCultivo con los datos de la relación, o null si no se encuentra
     */
    public PlagaCultivo obtenerPorIds(int idPlaga, int idCultivo) {
        String sql = "SELECT * FROM Plaga_Cultivo WHERE id_plaga = ? AND id_cultivo = ?";
        PlagaCultivo pc = null;
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPlaga);
            stmt.setInt(2, idCultivo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                pc = new PlagaCultivo();
                pc.setIdPlaga(rs.getInt("id_plaga"));
                pc.setIdCultivo(rs.getInt("id_cultivo"));
                pc.setNivelAfectacion(rs.getString("nivel_afectacion"));
                pc.setRecomendaciones(rs.getString("recomendaciones"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener relación plaga-cultivo: " + e.getMessage());
        }
        
        return pc;
    }

    /**
     * Obtiene todas las relaciones plaga-cultivo asociadas a una plaga específica.
     * Incluye información del nombre del cultivo mediante un JOIN.
     * 
     * @param idPlaga Identificador de la plaga
     * @return Lista de objetos PlagaCultivo asociados a la plaga especificada
     */
    public List<PlagaCultivo> obtenerPorPlaga(int idPlaga) {
        List<PlagaCultivo> relaciones = new ArrayList<>();
        String sql = "SELECT pc.*, c.nombre as nombre_cultivo FROM Plaga_Cultivo pc " +
                    "JOIN Cultivo c ON pc.id_cultivo = c.id_cultivo " +
                    "WHERE pc.id_plaga = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPlaga);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                PlagaCultivo pc = new PlagaCultivo();
                pc.setIdPlaga(rs.getInt("id_plaga"));
                pc.setIdCultivo(rs.getInt("id_cultivo"));
                pc.setNivelAfectacion(rs.getString("nivel_afectacion"));
                pc.setRecomendaciones(rs.getString("recomendaciones"));
                relaciones.add(pc);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener relaciones por plaga: " + e.getMessage());
        }
        
        return relaciones;
    }

    /**
     * Obtiene todas las relaciones plaga-cultivo asociadas a un cultivo específico.
     * Incluye información del nombre de la plaga mediante un JOIN.
     * 
     * @param idCultivo Identificador del cultivo
     * @return Lista de objetos PlagaCultivo asociados al cultivo especificado
     */
    public List<PlagaCultivo> obtenerPorCultivo(int idCultivo) {
        List<PlagaCultivo> relaciones = new ArrayList<>();
        String sql = "SELECT pc.*, p.nombre_plaga FROM Plaga_Cultivo pc " +
                    "JOIN Plaga p ON pc.id_plaga = p.id_plaga " +
                    "WHERE pc.id_cultivo = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCultivo);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                PlagaCultivo pc = new PlagaCultivo();
                pc.setIdPlaga(rs.getInt("id_plaga"));
                pc.setIdCultivo(rs.getInt("id_cultivo"));
                pc.setNivelAfectacion(rs.getString("nivel_afectacion"));
                pc.setRecomendaciones(rs.getString("recomendaciones"));
                relaciones.add(pc);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener relaciones por cultivo: " + e.getMessage());
        }
        
        return relaciones;
    }
}