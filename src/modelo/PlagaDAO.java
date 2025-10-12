package modelo;

import database.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlagaDAO {
    
    public boolean insertar(Plaga plaga) {
        String sql = "INSERT INTO PLAGA (NOMBRE_PLAGA, TIPO_PLAGA, CULTIVOS_ASOCIADOS) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // NO establecer ID_PLAGA - se generará automáticamente
            stmt.setString(1, plaga.getNombrePlaga());
            stmt.setString(2, plaga.getTipoPlaga());
            stmt.setString(3, plaga.getCultivosAsociados());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar plaga: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Plaga plaga) {
        String sql = "UPDATE PLAGA SET NOMBRE_PLAGA = ?, TIPO_PLAGA = ?, CULTIVOS_ASOCIADOS = ? WHERE ID_PLAGA = ?";
        
        try (Connection conn = ConexionBD.getConexion();
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
        String sql = "DELETE FROM PLAGA WHERE ID_PLAGA = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPlaga);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar plaga: " + e.getMessage());
            return false;
        }
    }

    public Plaga obtenerPorId(int idPlaga) {
        String sql = "SELECT * FROM PLAGA WHERE ID_PLAGA = ?";
        Plaga plaga = null;
        
        try (Connection conn = ConexionBD.getConexion();
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
        List<Plaga> plagas = new ArrayList<>();
        String sql = "SELECT * FROM PLAGA ORDER BY NOMBRE_PLAGA";
        
        try (Connection conn = ConexionBD.getConexion();
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
        List<Plaga> plagas = new ArrayList<>();
        String sql = "SELECT * FROM PLAGA WHERE UPPER(NOMBRE_PLAGA) LIKE UPPER(?) OR UPPER(TIPO_PLAGA) LIKE UPPER(?) OR UPPER(CULTIVOS_ASOCIADOS) LIKE UPPER(?) ORDER BY NOMBRE_PLAGA";
        
        try (Connection conn = ConexionBD.getConexion();
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
        List<Plaga> plagas = new ArrayList<>();
        String sql = "SELECT * FROM PLAGA WHERE UPPER(TIPO_PLAGA) = UPPER(?) ORDER BY NOMBRE_PLAGA";
        
        try (Connection conn = ConexionBD.getConexion();
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
        List<Plaga> plagas = new ArrayList<>();
        String sql = "SELECT * FROM PLAGA WHERE UPPER(CULTIVOS_ASOCIADOS) LIKE UPPER(?) ORDER BY NOMBRE_PLAGA";
        
        try (Connection conn = ConexionBD.getConexion();
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
        
        try (Connection conn = ConexionBD.getConexion();
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
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT DISTINCT TIPO_PLAGA FROM PLAGA WHERE TIPO_PLAGA IS NOT NULL ORDER BY TIPO_PLAGA";
        
        try (Connection conn = ConexionBD.getConexion();
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
        String sql = "SELECT COUNT(*) FROM PLAGA";
        
        try (Connection conn = ConexionBD.getConexion();
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