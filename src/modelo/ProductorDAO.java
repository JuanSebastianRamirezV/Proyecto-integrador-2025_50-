package modelo;

import database.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductorDAO {
    
    public boolean insertar(Productor productor) {
        String sql = "INSERT INTO PRODUCTORES (NOMBRE_COMPLETO, TIPO_IDENTIFICACION, ROL) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // NO establecer ID_PRODUCTOR - se generará automáticamente
            stmt.setString(1, productor.getNombreCompleto());
            stmt.setString(2, productor.getTipoIdentificacion());
            stmt.setString(3, productor.getRol());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar productor: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Productor productor) {
        String sql = "UPDATE PRODUCTORES SET nombre_completo = ?, tipo_identificacion = ?, rol = ? WHERE id_productor = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, productor.getNombreCompleto());
            stmt.setString(2, productor.getTipoIdentificacion());
            stmt.setString(3, productor.getRol());
            stmt.setInt(4, productor.getIdProductor());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar productor: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idProductor) {
        String sql = "DELETE FROM PRODUCTORES WHERE id_productor = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProductor);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar productor: " + e.getMessage());
            return false;
        }
    }

    public Productor obtenerPorId(int idProductor) {
        String sql = "SELECT * FROM PRODUCTORES WHERE id_productor = ?";
        Productor productor = null;
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProductor);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                productor = new Productor();
                productor.setIdProductor(rs.getInt("id_productor"));
                productor.setNombreCompleto(rs.getString("nombre_completo"));
                productor.setTipoIdentificacion(rs.getString("tipo_identificacion"));
                productor.setRol(rs.getString("rol"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener productor: " + e.getMessage());
        }
        
        return productor;
    }

    public List<Productor> obtenerTodos() {
        List<Productor> productores = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTORES ORDER BY ID_PRODUCTOR asc";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Productor productor = new Productor();
                productor.setIdProductor(rs.getInt("id_productor"));
                productor.setNombreCompleto(rs.getString("nombre_completo"));
                productor.setTipoIdentificacion(rs.getString("tipo_identificacion"));
                productor.setRol(rs.getString("rol"));
                productores.add(productor);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener productores: " + e.getMessage());
        }
        
        return productores;
    }

    public List<Productor> buscar(String criterio) {
        List<Productor> productores = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTORES WHERE nombre_completo LIKE ? OR tipo_identificacion LIKE ? OR rol LIKE ? ORDER BY nombre_completo";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);
            stmt.setString(2, likeCriterio);
            stmt.setString(3, likeCriterio);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Productor productor = new Productor();
                productor.setIdProductor(rs.getInt("id_productor"));
                productor.setNombreCompleto(rs.getString("nombre_completo"));
                productor.setTipoIdentificacion(rs.getString("tipo_identificacion"));
                productor.setRol(rs.getString("rol"));
                productores.add(productor);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar productores: " + e.getMessage());
        }
        
        return productores;
    }
}