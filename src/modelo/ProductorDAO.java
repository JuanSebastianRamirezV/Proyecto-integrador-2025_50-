package modelo;

import database.ConexionBD;
import controlador.SesionUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductorDAO {
    
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
    
    public boolean insertar(Productor productor) {
        logOperacion("INSERTAR PRODUCTOR");
        // ✅ PRIMERO validar si ya existe
        if (existeNombreProductor(productor.getNombreCompleto())) {
            System.out.println("Error: Ya existe un productor con el nombre: " + productor.getNombreCompleto());
            return false;
        }

        String sql = "INSERT INTO PRODUCTORES (ID_PRODUCTOR, NOMBRE_COMPLETO, TIPO_IDENTIFICACION, ROL) VALUES (SEQ_PRODUCTORES.NEXTVAL, ?, ?, ?)";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productor.getNombreCompleto());
            stmt.setString(2, productor.getTipoIdentificacion());
            stmt.setString(3, productor.getRol());

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar productor: " + e.getMessage());
            System.out.println("SQL: " + sql);
            return false;
        }
    }

    public boolean actualizar(Productor productor) {
        logOperacion("ACTUALIZAR PRODUCTOR");
        String sql = "UPDATE PRODUCTORES SET nombre_completo = ?, tipo_identificacion = ?, rol = ? WHERE id_productor = ?";
        
        try (Connection conn = getConexion();
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
        // Primero verificar si hay registros relacionados en LUGARPRODUCCION
        if (tieneRegistrosRelacionadosProductor(idProductor)) {
            System.out.println("No se puede eliminar el productor porque tiene lugares de producción relacionados");
            return false;
        }

        // Si no hay registros relacionados, proceder con la eliminación
        String sql = "DELETE FROM productores WHERE id_productor = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProductor);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Productor eliminado correctamente");
                return true;
            } else {
                System.out.println("No se encontró el productor con ID: " + idProductor);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar productor: " + e.getMessage());
            return false;
        }
    }

    private boolean tieneRegistrosRelacionadosProductor(int idProductor) {
        String sql = "SELECT COUNT(*) FROM lugarproduccion WHERE id_productor = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProductor);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Existen " + rs.getInt(1) + " lugar(es) de producción relacionados con este productor");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar lugares de producción relacionados: " + e.getMessage());
            return true; // Por seguridad, asumimos que hay relaciones si hay error
        }
        return false;
    }

    public Productor obtenerPorId(int idProductor) {
        logOperacion("CONSULTAR PRODUCTOR POR ID");
        String sql = "SELECT * FROM PRODUCTORES WHERE id_productor = ?";
        Productor productor = null;
        
        try (Connection conn = getConexion();
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
        logOperacion("CONSULTAR TODOS LOS PRODUCTORES");
        List<Productor> productores = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTORES ORDER BY ID_PRODUCTOR asc";
        
        try (Connection conn = getConexion();
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
        logOperacion("BUSCAR PRODUCTORES");
        List<Productor> productores = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTORES WHERE nombre_completo LIKE ? OR tipo_identificacion LIKE ? OR rol LIKE ? ORDER BY nombre_completo";
        
        try (Connection conn = getConexion();
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
    
   public boolean existeNombreProductor(String nombreCompleto) {
        String sql = "SELECT COUNT(*) FROM PRODUCTORES WHERE UPPER(NOMBRE_COMPLETO) = UPPER(?)";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreCompleto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar nombre de productor: " + e.getMessage());
        }

        return false;
    }
}