package modelo;

import database.ConexionBD;
import controlador.SesionUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PredioDAO {
    
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
    
    /**
     * Inserta un nuevo predio en la base de datos
     */
   public boolean insertar(Predio predio) {
        logOperacion("INSERTAR PREDIO");
        String sql = "INSERT INTO PREDIO (ID_PREDIO, NOMBRE_PREDIO, NOMBRE_PROPIETARIO, ID_MUNICIPIO) VALUES (SEQ_PREDIO.NEXTVAL, ?, ?, ?)";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, predio.getNombrePredio());
            stmt.setString(2, predio.getNombrePropietario());
            stmt.setInt(3, predio.getIdMunicipio());

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar predio: " + e.getMessage());
            System.out.println("SQL: " + sql);
            return false;
        }
    }
    
    public boolean actualizar(Predio predio) {
        logOperacion("ACTUALIZAR PREDIO");
        String sql = "UPDATE PREDIO SET NOMBRE_PREDIO = ?, NOMBRE_PROPIETARIO = ?, ID_MUNICIPIO = ? WHERE ID_PREDIO = ?";
        
        try (Connection conn = getConexion();
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
        // Primero verificar si hay registros relacionados en CULTIVO
        if (tieneRegistrosRelacionadosPredio(idPredio)) {
            System.out.println("No se puede eliminar el predio porque tiene cultivos relacionados");
            return false;
        }

        // Si no hay registros relacionados, proceder con la eliminación
        String sql = "DELETE FROM predio WHERE id_predio = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPredio);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Predio eliminado correctamente");
                return true;
            } else {
                System.out.println("No se encontró el predio con ID: " + idPredio);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar predio: " + e.getMessage());
            return false;
        }
    }

    private boolean tieneRegistrosRelacionadosPredio(int idPredio) {
        String sql = "SELECT COUNT(*) FROM cultivo WHERE id_predio = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPredio);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Existen " + rs.getInt(1) + " cultivo(s) relacionados con este predio");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar cultivos relacionados: " + e.getMessage());
            return true; // Por seguridad, asumimos que hay relaciones si hay error
        }
        return false;
    }

    public Predio obtenerPorId(int idPredio) {
        logOperacion("CONSULTAR PREDIO POR ID");
        String sql = "SELECT p.*, m.NOMBRE as NOMBRE_MUNICIPIO FROM PREDIO p INNER JOIN MUNICIPIO m ON p.ID_MUNICIPIO = m.ID_MUNICIPIO WHERE p.ID_PREDIO = ?";
        Predio predio = null;
        
        try (Connection conn = getConexion();
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

    /**
     * Obtiene un predio por su nombre
     */
    public Predio obtenerPorNombre(String nombrePredio) {
        logOperacion("CONSULTAR PREDIO POR NOMBRE");
        String sql = "SELECT p.*, m.NOMBRE as NOMBRE_MUNICIPIO FROM PREDIO p INNER JOIN MUNICIPIO m ON p.ID_MUNICIPIO = m.ID_MUNICIPIO WHERE UPPER(p.NOMBRE_PREDIO) = UPPER(?)";
        Predio predio = null;
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombrePredio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                predio = mapearPredio(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener predio por nombre: " + e.getMessage());
        }
        
        return predio;
    }

    public List<Predio> obtenerTodos() {
        logOperacion("CONSULTAR TODOS LOS PREDIOS");
        List<Predio> predios = new ArrayList<>();
        String sql = "SELECT p.*, m.NOMBRE as NOMBRE_MUNICIPIO FROM PREDIO p INNER JOIN MUNICIPIO m ON p.ID_MUNICIPIO = m.ID_MUNICIPIO ORDER BY p.ID_PREDIO asc";
        
        try (Connection conn = getConexion();
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
        logOperacion("BUSCAR PREDIOS");
        List<Predio> predios = new ArrayList<>();
        String sql = "SELECT p.*, m.NOMBRE as NOMBRE_MUNICIPIO FROM PREDIO p INNER JOIN MUNICIPIO m ON p.ID_MUNICIPIO = m.ID_MUNICIPIO WHERE UPPER(p.NOMBRE_PREDIO) LIKE UPPER(?) OR UPPER(p.NOMBRE_PROPIETARIO) LIKE UPPER(?) ORDER BY p.NOMBRE_PREDIO";
        
        try (Connection conn = getConexion();
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
        logOperacion("CONSULTAR PREDIOS POR MUNICIPIO");
        List<Predio> predios = new ArrayList<>();
        String sql = "SELECT p.*, m.NOMBRE as NOMBRE_MUNICIPIO FROM PREDIO p INNER JOIN MUNICIPIO m ON p.ID_MUNICIPIO = m.ID_MUNICIPIO WHERE p.ID_MUNICIPIO = ? ORDER BY p.NOMBRE_PREDIO";
        
        try (Connection conn = getConexion();
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
        
        try (Connection conn = getConexion();
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
        logOperacion("CONTAR PREDIOS POR MUNICIPIO");
        String sql = "SELECT COUNT(*) FROM PREDIO WHERE ID_MUNICIPIO = ?";
        
        try (Connection conn = getConexion();
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
        predio.setNombreMunicipio(rs.getString("NOMBRE_MUNICIPIO")); // Esta línea estaba faltando
        return predio;
    }
}