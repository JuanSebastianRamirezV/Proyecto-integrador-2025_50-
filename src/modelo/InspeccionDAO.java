/**
 * Data Access Object (DAO) para la gestión de operaciones de base de datos relacionadas con la entidad Inspección.
 * Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) y consultas específicas
 * sobre la tabla INSPECCION en la base de datos.
 * 
 * @author [Nombre del autor]
 * @version 1.0
 * @since [Fecha de creación]
 */
package modelo;

import database.ConexionBD;
import controlador.SesionUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InspeccionDAO {
    
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
     * Inserta una nueva inspección en la base de datos.
     * El ID_INSPECCION se genera automáticamente en la base de datos.
     * 
     * @param inspeccion El objeto Inspeccion con la información a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(Inspeccion inspeccion) {
        logOperacion("INSERTAR INSPECCIÓN");
        String sql = "INSERT INTO INSPECCION (ID_INSPECCION, FECHA_INSPECCION, ESTADO, OBSERVACIONES, ID_INSPECTOR) VALUES (SEQ_INSPECCION.NEXTVAL, ?, ?, ?, ?)";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(inspeccion.getFechaInspeccion().getTime()));
            stmt.setString(2, inspeccion.getEstado());
            stmt.setString(3, inspeccion.getObservaciones());
            stmt.setInt(4, inspeccion.getIdInspector());
            // Se eliminó el parámetro de cultivo

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar inspección: " + e.getMessage());
            System.out.println("SQL: " + sql);
            return false;
        }
    }

    /**
     * Actualiza la información de una inspección existente en la base de datos.
     * 
     * @param inspeccion El objeto Inspeccion con la información actualizada
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizar(Inspeccion inspeccion) {
        logOperacion("ACTUALIZAR INSPECCIÓN");
        String sql = "UPDATE INSPECCION SET fecha_inspeccion = ?, estado = ?, observaciones = ?, id_inspector = ? WHERE id_inspeccion = ?";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, new java.sql.Date(inspeccion.getFechaInspeccion().getTime()));
            stmt.setString(2, inspeccion.getEstado());
            stmt.setString(3, inspeccion.getObservaciones());
            stmt.setInt(4, inspeccion.getIdInspector());
            // Se eliminó el parámetro de cultivo
            stmt.setInt(5, inspeccion.getIdInspeccion());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar inspección: " + e.getMessage());
            return false;
        }
    }

    /**
    * Elimina una inspección de la base de datos por su ID con eliminación en cascada.
    * 
    * @param idInspeccion El identificador único de la inspección a eliminar
    * @return true si la eliminación fue exitosa, false en caso contrario
    */
   public boolean eliminar(int idCultivo) {
    // Primero verificar si hay registros relacionados en RELACION_ASOCIADOS
    if (tieneRelacionesEnAsociados(idCultivo)) {
        System.out.println("No se puede eliminar el cultivo porque tiene registros relacionados en RELACION_ASOCIADOS");
        return false;
    }

    // Si no hay registros relacionados, proceder con la eliminación
    String sql = "DELETE FROM cultivo WHERE id_cultivo = ?";

    try (Connection conn = ConexionBD.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idCultivo);
        int filasAfectadas = stmt.executeUpdate();
        
        if (filasAfectadas > 0) {
            System.out.println("Cultivo eliminado correctamente");
            return true;
        } else {
            System.out.println("No se encontró el cultivo con ID: " + idCultivo);
            return false;
        }

    } catch (SQLException e) {
        System.out.println("Error al eliminar cultivo: " + e.getMessage());
        return false;
    }
}

private boolean tieneRelacionesEnAsociados(int idCultivo) {
    String sql = "SELECT COUNT(*) FROM relacion_asociados WHERE id_cultivo = ?";
    
    try (Connection conn = ConexionBD.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, idCultivo);
        ResultSet rs = stmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            System.out.println("Existen " + rs.getInt(1) + " registro(s) en RELACION_ASOCIADOS");
            return true;
        }
    } catch (SQLException e) {
        System.out.println("Error al verificar RELACION_ASOCIADOS: " + e.getMessage());
        return true; // Por seguridad, asumimos que hay relaciones si hay error
    }
    return false;
}
    private boolean tieneRegistrosRelacionadosInspeccion(int idInspeccion) {
        String[] tablasRelacionadas = {"RELACION_ASOCIADOS", "LUGARPRODUCCION_INSPECCION"};

        for (String tabla : tablasRelacionadas) {
            String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE id_inspeccion = ?";
            try (Connection conn = ConexionBD.getConexion();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idInspeccion);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Existen registros relacionados en la tabla: " + tabla);
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("Error al verificar tabla " + tabla + ": " + e.getMessage());
                return true; // Por seguridad, asumimos que hay relaciones si hay error
            }
        }
        return false;
    }

    /**
     * Obtiene una inspección específica por su ID.
     * 
     * @param idInspeccion El identificador único de la inspección a buscar
     * @return El objeto Inspeccion correspondiente al ID especificado, o null si no se encuentra
     */
    public Inspeccion obtenerPorId(int idInspeccion) {
    logOperacion("CONSULTAR INSPECCIÓN POR ID");
    String sql = "SELECT * FROM INSPECCION WHERE id_inspeccion = ?";
    Inspeccion inspeccion = null;
    
    try (Connection conn = getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, idInspeccion);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            inspeccion = new Inspeccion();
            inspeccion.setIdInspeccion(rs.getInt("id_inspeccion"));
            inspeccion.setFechaInspeccion(rs.getDate("fecha_inspeccion"));
            inspeccion.setEstado(rs.getString("estado"));
            inspeccion.setObservaciones(rs.getString("observaciones"));
            inspeccion.setIdInspector(rs.getInt("id_inspector"));
            // Se eliminó la línea que asignaba id_cultivo
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener inspección: " + e.getMessage());
    }
    
    return inspeccion;
}

/**
 * Obtiene todas las inspecciones registradas en la base de datos, ordenadas por fecha descendente.
 * 
 * @return Una lista de todos los objetos Inspeccion existentes en la base de datos
 */
public List<Inspeccion> obtenerTodos() {
    logOperacion("CONSULTAR TODAS LAS INSPECCIONES");
    List<Inspeccion> inspecciones = new ArrayList<>();
    String sql = "SELECT * FROM INSPECCION ORDER BY ID_INSPECCION asc";
    
    try (Connection conn = getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            Inspeccion inspeccion = new Inspeccion();
            inspeccion.setIdInspeccion(rs.getInt("id_inspeccion"));
            inspeccion.setFechaInspeccion(rs.getDate("fecha_inspeccion"));
            inspeccion.setEstado(rs.getString("estado"));
            inspeccion.setObservaciones(rs.getString("observaciones"));
            inspeccion.setIdInspector(rs.getInt("id_inspector"));
            // Se eliminó la línea que asignaba id_cultivo
            inspecciones.add(inspeccion);
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener inspecciones: " + e.getMessage());
    }
    
    return inspecciones;
}

/**
 * Busca inspecciones que coincidan con el criterio especificado en estado u observaciones.
 * La búsqueda utiliza coincidencias parciales y no distingue entre mayúsculas y minúsculas.
 * 
 * @param criterio El texto de búsqueda para filtrar las inspecciones
 * @return Una lista de objetos Inspeccion que coinciden con el criterio de búsqueda, ordenadas por fecha descendente
 */
public List<Inspeccion> buscar(String criterio) {
    logOperacion("BUSCAR INSPECCIONES");
    List<Inspeccion> inspecciones = new ArrayList<>();
    String sql = "SELECT * FROM INSPECCION WHERE estado LIKE ? OR observaciones LIKE ? ORDER BY id_inspeccion asc";
    
    try (Connection conn = getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        String likeCriterio = "%" + criterio + "%";
        stmt.setString(1, likeCriterio);
        stmt.setString(2, likeCriterio);
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Inspeccion inspeccion = new Inspeccion();
            inspeccion.setIdInspeccion(rs.getInt("id_inspeccion"));
            inspeccion.setFechaInspeccion(rs.getDate("fecha_inspeccion"));
            inspeccion.setEstado(rs.getString("estado"));
            inspeccion.setObservaciones(rs.getString("observaciones"));
            inspeccion.setIdInspector(rs.getInt("id_inspector"));
            // Se eliminó la línea que asignaba id_cultivo
            inspecciones.add(inspeccion);
        }
    } catch (SQLException e) {
        System.out.println("Error al buscar inspecciones: " + e.getMessage());
    }
    
    return inspecciones;
}}