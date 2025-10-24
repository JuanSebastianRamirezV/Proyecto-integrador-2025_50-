package modelo;

import database.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MunicipioDAO {
    
    public boolean insertar(Municipio municipio) {
        String sql = "INSERT INTO MUNICIPIO (NOMBRE) VALUES (?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // NO establecer ID_MUNICIPIO - se generará automáticamente
            stmt.setString(1, municipio.getNombre());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar municipio: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Municipio municipio) {
        String sql = "UPDATE MUNICIPIO SET NOMBRE = ? WHERE ID_MUNICIPIO = ?";
        
        try (Connection conn = ConexionBD.getConexion();
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
        String sql = "DELETE FROM MUNICIPIO WHERE ID_MUNICIPIO = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMunicipio);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar municipio: " + e.getMessage());
            return false;
        }
    }

    public Municipio obtenerPorId(int idMunicipio) {
        String sql = "SELECT * FROM MUNICIPIO WHERE ID_MUNICIPIO = ?";
        Municipio municipio = null;
        
        try (Connection conn = ConexionBD.getConexion();
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
        List<Municipio> municipios = new ArrayList<>();
        String sql = "SELECT * FROM MUNICIPIO ORDER BY ID_MUNICIPIO asc";
        
        try (Connection conn = ConexionBD.getConexion();
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
        List<Municipio> municipios = new ArrayList<>();
        String sql = "SELECT * FROM MUNICIPIO WHERE UPPER(NOMBRE) LIKE UPPER(?) ORDER BY NOMBRE";
        
        try (Connection conn = ConexionBD.getConexion();
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
        
        try (Connection conn = ConexionBD.getConexion();
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
        String sql = "SELECT COUNT(*) FROM MUNICIPIO";
        
        try (Connection conn = ConexionBD.getConexion();
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
        List<Municipio> municipios = new ArrayList<>();
        String sql = "SELECT DISTINCT m.* FROM MUNICIPIO m " +
                    "INNER JOIN PREDIO p ON m.ID_MUNICIPIO = p.ID_MUNICIPIO " +
                    "ORDER BY m.NOMBRE";
        
        try (Connection conn = ConexionBD.getConexion();
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
}