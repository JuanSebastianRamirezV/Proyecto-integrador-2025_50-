package modelo;

import database.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PredioDAO {
    
    public boolean insertar(Predio predio) {
        String sql = "INSERT INTO PREDIO (NOMBRE_PREDIO, NOMBRE_PROPIETARIO, ID_MUNICIPIO) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // NO establecer ID_PREDIO - se generará automáticamente
            stmt.setString(1, predio.getNombrePredio());
            stmt.setString(2, predio.getNombrePropietario());
            stmt.setInt(3, predio.getIdMunicipio());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar predio: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizar(Predio predio) {
        String sql = "UPDATE PREDIO SET NOMBRE_PREDIO = ?, NOMBRE_PROPIETARIO = ?, ID_MUNICIPIO = ? WHERE ID_PREDIO = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, predio.getNombrePredio());
            stmt.setString(2, predio.getNombrePropietario());
            stmt.setInt(3, predio.getIdMunicipio());
            stmt.setInt(4, predio.getIdPredio());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar predio: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idPredio) {
        String sql = "{call eliminar_predio_cascada(?)}";

        try (Connection conn = ConexionBD.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idPredio);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar predio en cascada: " + e.getMessage());
            return false;
        }
    }

    public Predio obtenerPorId(int idPredio) {
        String sql = "SELECT * FROM PREDIO WHERE ID_PREDIO = ?";
        Predio predio = null;
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPredio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                predio = mapearPredio(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener predio por ID: " + e.getMessage());
        }
        
        return predio;
    }

    public List<Predio> obtenerTodos() {
        List<Predio> predios = new ArrayList<>();
        String sql = "SELECT * FROM PREDIO ORDER BY ID_PREDIO asc";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Predio predio = mapearPredio(rs);
                predios.add(predio);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los predios: " + e.getMessage());
        }
        
        return predios;
    }

    public List<Predio> buscar(String criterio) {
        List<Predio> predios = new ArrayList<>();
        String sql = "SELECT * FROM PREDIO WHERE UPPER(NOMBRE_PREDIO) LIKE UPPER(?) OR UPPER(NOMBRE_PROPIETARIO) LIKE UPPER(?) ORDER BY NOMBRE_PREDIO";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);
            stmt.setString(2, likeCriterio);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Predio predio = mapearPredio(rs);
                predios.add(predio);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar predios: " + e.getMessage());
        }
        
        return predios;
    }

    public List<Predio> obtenerPorMunicipio(int idMunicipio) {
        List<Predio> predios = new ArrayList<>();
        String sql = "SELECT * FROM PREDIO WHERE ID_MUNICIPIO = ? ORDER BY NOMBRE_PREDIO";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMunicipio);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Predio predio = mapearPredio(rs);
                predios.add(predio);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener predios por municipio: " + e.getMessage());
        }
        
        return predios;
    }

    public boolean existeNombrePredio(String nombrePredio) {
        String sql = "SELECT COUNT(*) FROM PREDIO WHERE UPPER(NOMBRE_PREDIO) = UPPER(?)";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombrePredio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar nombre de predio: " + e.getMessage());
        }
        
        return false;
    }

    public int contarPrediosPorMunicipio(int idMunicipio) {
        String sql = "SELECT COUNT(*) FROM PREDIO WHERE ID_MUNICIPIO = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMunicipio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar predios por municipio: " + e.getMessage());
        }
        
        return 0;
    }

    private Predio mapearPredio(ResultSet rs) throws SQLException {
        Predio predio = new Predio();
        predio.setIdPredio(rs.getInt("ID_PREDIO"));
        predio.setNombrePredio(rs.getString("NOMBRE_PREDIO"));
        predio.setNombrePropietario(rs.getString("NOMBRE_PROPIETARIO"));
        predio.setIdMunicipio(rs.getInt("ID_MUNICIPIO"));
        return predio;
    }
}