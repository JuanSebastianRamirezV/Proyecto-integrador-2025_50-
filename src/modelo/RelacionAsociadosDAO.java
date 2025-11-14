package modelo;

import database.ConexionBD;
import controlador.SesionUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la gestión de operaciones de base de datos 
 * relacionadas con la entidad RelacionAsociados.
 */
public class RelacionAsociadosDAO {
    
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
    
    // ========== MÉTODOS PARA LA GESTIÓN DEL INSPECTOR ==========

    /**
     * Elimina una relación por IDs compuestos
     */
    public boolean eliminar(int idPlaga, int idCultivo, int idInspeccion, int idLugarProduccion) {
        logOperacion("ELIMINAR RELACIÓN ASOCIADOS");
        String sql = "DELETE FROM RELACION_ASOCIADOS WHERE ID_PLAGA = ? AND ID_CULTIVO = ? AND ID_INSPECCION = ? AND ID_LUGARPRODUCCION = ?";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPlaga);
            stmt.setInt(2, idCultivo);
            stmt.setInt(3, idInspeccion);
            stmt.setInt(4, idLugarProduccion);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar relación asociados: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una relación por IDs compuestos
     */
    public RelacionAsociados obtenerPorIds(int idPlaga, int idCultivo, int idInspeccion, int idLugarProduccion) {
        logOperacion("CONSULTAR RELACIÓN ASOCIADOS POR IDS");
        String sql = "SELECT " +
                     "ra.ID_PLAGA, ra.ID_CULTIVO, ra.ID_INSPECCION, ra.ID_LUGARPRODUCCION, " +
                     "ra.TOTAL_PLANTAS, ra.NIVEL_INCIDENCIA, " +
                     "p.NOMBRE_PLAGA, c.NOMBRE_CULTIVO, lp.NOMBRE_LUGAR, " +
                     "TO_CHAR(i.FECHA_INSPECCION, 'YYYY-MM-DD') as FECHA_INSPECCION " +
                     "FROM RELACION_ASOCIADOS ra " +
                     "LEFT JOIN PLAGA p ON ra.ID_PLAGA = p.ID_PLAGA " +
                     "LEFT JOIN CULTIVO c ON ra.ID_CULTIVO = c.ID_CULTIVO " +
                     "LEFT JOIN LUGARPRODUCCION lp ON ra.ID_LUGARPRODUCCION = lp.ID_LUGARPRODUCCION " +
                     "LEFT JOIN INSPECCION i ON ra.ID_INSPECCION = i.ID_INSPECCION " +
                     "WHERE ra.ID_PLAGA = ? AND ra.ID_CULTIVO = ? AND ra.ID_INSPECCION = ? AND ra.ID_LUGARPRODUCCION = ?";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPlaga);
            stmt.setInt(2, idCultivo);
            stmt.setInt(3, idInspeccion);
            stmt.setInt(4, idLugarProduccion);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearRelacionCompleta(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener relación por IDs: " + e.getMessage());
        }

        return null;
    }

    /**
     * Verifica si existe una relación con los IDs especificados.
     */
    public boolean existeRelacion(int idPlaga, int idCultivo, int idInspeccion, int idLugarProduccion) {
        String sql = "SELECT COUNT(*) FROM RELACION_ASOCIADOS WHERE ID_PLAGA = ? AND ID_CULTIVO = ? AND ID_INSPECCION = ? AND ID_LUGARPRODUCCION = ?";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPlaga);
            stmt.setInt(2, idCultivo);
            stmt.setInt(3, idInspeccion);
            stmt.setInt(4, idLugarProduccion);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar existencia de relación: " + e.getMessage());
        }

        return false;
    }

    /**
     * Inserta una nueva relación de asociación en la base de datos.
     */
    public boolean insertar(RelacionAsociados relacion) {
        logOperacion("INSERTAR RELACIÓN ASOCIADOS");
        String sql = "INSERT INTO RELACION_ASOCIADOS (ID_PLAGA, ID_CULTIVO, ID_INSPECCION, ID_LUGARPRODUCCION, TOTAL_PLANTAS, NIVEL_INCIDENCIA) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, relacion.getIdPlaga());
            stmt.setInt(2, relacion.getIdCultivo());
            stmt.setInt(3, relacion.getIdInspeccion());
            stmt.setInt(4, relacion.getIdLugarProduccion());
            stmt.setInt(5, relacion.getTotalPlantas());
            stmt.setString(6, relacion.getNivelIncidencia());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar relación asociados: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza una relación de asociación existente.
     */
    public boolean actualizar(RelacionAsociados relacion) {
    logOperacion("ACTUALIZAR RELACIÓN ASOCIADOS");
    
    // Primero verificar si existe la relación
    if (!existeRelacion(relacion)) {
        System.out.println("❌ La relación NO EXISTE en la base de datos");
        System.out.println("   Plaga: " + relacion.getIdPlaga() + 
                         ", Cultivo: " + relacion.getIdCultivo() +
                         ", Inspección: " + relacion.getIdInspeccion() +
                         ", Lugar: " + relacion.getIdLugarProduccion());
        return false;
    }
    
    String sql = "UPDATE RELACION_ASOCIADOS " +
                 "SET TOTAL_PLANTAS = ?, " +
                 "    NIVEL_INCIDENCIA = ? " +
                 "WHERE ID_PLAGA = ? " +
                 "  AND ID_CULTIVO = ? " +
                 "  AND ID_INSPECCION = ? " +
                 "  AND ID_LUGARPRODUCCION = ?";

    try (Connection conn = getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, relacion.getTotalPlantas());
        stmt.setString(2, relacion.getNivelIncidencia());
        stmt.setInt(3, relacion.getIdPlaga());
        stmt.setInt(4, relacion.getIdCultivo());
        stmt.setInt(5, relacion.getIdInspeccion());
        stmt.setInt(6, relacion.getIdLugarProduccion());

        int filasAfectadas = stmt.executeUpdate();
        
        if (filasAfectadas > 0) {
            System.out.println("✅ Relación actualizada correctamente");
            return true;
        } else {
            System.out.println("❌ Error inesperado - la relación existe pero no se pudo actualizar");
            return false;
        }

    } catch (SQLException e) {
        System.out.println("❌ Error al actualizar relación asociados: " + e.getMessage());
        return false;
    }
}

private boolean existeRelacion(RelacionAsociados relacion) {
    String sql = "SELECT COUNT(*) FROM RELACION_ASOCIADOS " +
                 "WHERE ID_PLAGA = ? " +
                 "  AND ID_CULTIVO = ? " +
                 "  AND ID_INSPECCION = ? " +
                 "  AND ID_LUGARPRODUCCION = ?";
    
    try (Connection conn = getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, relacion.getIdPlaga());
        stmt.setInt(2, relacion.getIdCultivo());
        stmt.setInt(3, relacion.getIdInspeccion());
        stmt.setInt(4, relacion.getIdLugarProduccion());
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            System.out.println("🔍 Relaciones encontradas: " + count);
            return count > 0;
        }
    } catch (SQLException e) {
        System.out.println("Error al verificar existencia: " + e.getMessage());
    }
    return false;
}
    /**
     * Obtiene todas las relaciones de asociación con información completa para el informe.
     */
    public List<RelacionAsociados> obtenerInformeCompleto() {
        logOperacion("CONSULTAR INFORME COMPLETO ASOCIADOS");
        List<RelacionAsociados> relaciones = new ArrayList<>();
        String sql = "SELECT " +
                     "ra.ID_PLAGA, ra.ID_CULTIVO, ra.ID_INSPECCION, ra.ID_LUGARPRODUCCION, " +
                     "ra.TOTAL_PLANTAS, ra.NIVEL_INCIDENCIA, " +
                     "p.NOMBRE_PLAGA, c.NOMBRE_CULTIVO, lp.NOMBRE_LUGAR, " +
                     "TO_CHAR(i.FECHA_INSPECCION, 'YYYY-MM-DD') as FECHA_INSPECCION " +
                     "FROM RELACION_ASOCIADOS ra " +
                     "LEFT JOIN PLAGA p ON ra.ID_PLAGA = p.ID_PLAGA " +
                     "LEFT JOIN CULTIVO c ON ra.ID_CULTIVO = c.ID_CULTIVO " +
                     "LEFT JOIN LUGARPRODUCCION lp ON ra.ID_LUGARPRODUCCION = lp.ID_LUGARPRODUCCION " +
                     "LEFT JOIN INSPECCION i ON ra.ID_INSPECCION = i.ID_INSPECCION " +
                     "ORDER BY ra.NIVEL_INCIDENCIA DESC, ra.TOTAL_PLANTAS DESC";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RelacionAsociados relacion = mapearRelacionCompleta(rs);
                relaciones.add(relacion);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener informe completo: " + e.getMessage());
        }

        return relaciones;
    }

    /**
     * Obtiene estadísticas generales para el dashboard.
     */
    public String obtenerEstadisticasGenerales() {
        logOperacion("CONSULTAR ESTADÍSTICAS ASOCIADOS");
        StringBuilder estadisticas = new StringBuilder();
        
        String sqlTotal = "SELECT COUNT(*) FROM RELACION_ASOCIADOS";
        String sqlPlantas = "SELECT SUM(TOTAL_PLANTAS) FROM RELACION_ASOCIADOS";
        String sqlIncidencias = "SELECT NIVEL_INCIDENCIA, COUNT(*) FROM RELACION_ASOCIADOS GROUP BY NIVEL_INCIDENCIA";

        try (Connection conn = getConexion()) {
            
            // Total de relaciones
            try (PreparedStatement stmt = conn.prepareStatement(sqlTotal);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    estadisticas.append("Total de asociaciones: ").append(rs.getInt(1)).append("\n");
                }
            }

            // Total de plantas
            try (PreparedStatement stmt = conn.prepareStatement(sqlPlantas);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int totalPlantas = rs.getInt(1);
                    estadisticas.append("Total de plantas monitoreadas: ").append(totalPlantas).append("\n");
                }
            }

            // Distribución de niveles de incidencia
            estadisticas.append("\nDistribución por nivel de incidencia:\n");
            try (PreparedStatement stmt = conn.prepareStatement(sqlIncidencias);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nivel = rs.getString(1) != null ? rs.getString(1) : "No especificado";
                    int count = rs.getInt(2);
                    estadisticas.append("- ").append(nivel).append(": ").append(count).append(" registros\n");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener estadísticas: " + e.getMessage());
            return "Error al cargar estadísticas";
        }

        return estadisticas.toString();
    }

    /**
     * Busca relaciones por criterio (nombre de plaga, cultivo o nivel de incidencia).
     */
    public List<RelacionAsociados> buscar(String criterio) {
        logOperacion("BUSCAR RELACIONES ASOCIADOS");
        List<RelacionAsociados> relaciones = new ArrayList<>();
        String sql = "SELECT " +
                     "ra.ID_PLAGA, ra.ID_CULTIVO, ra.ID_INSPECCION, ra.ID_LUGARPRODUCCION, " +
                     "ra.TOTAL_PLANTAS, ra.NIVEL_INCIDENCIA, " +
                     "p.NOMBRE_PLAGA, c.NOMBRE_CULTIVO, lp.NOMBRE_LUGAR, " +
                     "TO_CHAR(i.FECHA_INSPECCION, 'YYYY-MM-DD') as FECHA_INSPECCION " +
                     "FROM RELACION_ASOCIADOS ra " +
                     "LEFT JOIN PLAGA p ON ra.ID_PLAGA = p.ID_PLAGA " +
                     "LEFT JOIN CULTIVO c ON ra.ID_CULTIVO = c.ID_CULTIVO " +
                     "LEFT JOIN LUGARPRODUCCION lp ON ra.ID_LUGARPRODUCCION = lp.ID_LUGARPRODUCCION " +
                     "LEFT JOIN INSPECCION i ON ra.ID_INSPECCION = i.ID_INSPECCION " +
                     "WHERE UPPER(p.NOMBRE_PLAGA) LIKE UPPER(?) OR UPPER(c.NOMBRE_CULTIVO) LIKE UPPER(?) OR UPPER(ra.NIVEL_INCIDENCIA) LIKE UPPER(?) " +
                     "ORDER BY ra.NIVEL_INCIDENCIA DESC";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);
            stmt.setString(2, likeCriterio);
            stmt.setString(3, likeCriterio);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RelacionAsociados relacion = mapearRelacionCompleta(rs);
                relaciones.add(relacion);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar relaciones: " + e.getMessage());
        }

        return relaciones;
    }

    /**
     * Mapea un ResultSet a un objeto RelacionAsociados con información completa.
     */
    private RelacionAsociados mapearRelacionCompleta(ResultSet rs) throws SQLException {
        RelacionAsociados relacion = new RelacionAsociados();
        
        relacion.setIdPlaga(rs.getInt("ID_PLAGA"));
        relacion.setIdCultivo(rs.getInt("ID_CULTIVO"));
        relacion.setIdInspeccion(rs.getInt("ID_INSPECCION"));
        relacion.setIdLugarProduccion(rs.getInt("ID_LUGARPRODUCCION"));
        relacion.setTotalPlantas(rs.getInt("TOTAL_PLANTAS"));
        relacion.setNivelIncidencia(rs.getString("NIVEL_INCIDENCIA"));
        
        // Información relacionada
        relacion.setNombrePlaga(rs.getString("NOMBRE_PLAGA"));
        relacion.setNombreCultivo(rs.getString("NOMBRE_CULTIVO"));
        relacion.setNombreLugarProduccion(rs.getString("NOMBRE_LUGAR"));
        relacion.setFechaInspeccion(rs.getString("FECHA_INSPECCION"));
        
        return relacion;
    }

    /**
     * Obtiene todas las relaciones sin información adicional (para gestión básica)
     */
    public List<RelacionAsociados> obtenerTodasRelaciones() {
        logOperacion("CONSULTAR TODAS LAS RELACIONES ASOCIADOS");
        List<RelacionAsociados> relaciones = new ArrayList<>();
        String sql = "SELECT ID_PLAGA, ID_CULTIVO, ID_INSPECCION, ID_LUGARPRODUCCION, TOTAL_PLANTAS, NIVEL_INCIDENCIA " +
                 "FROM RELACION_ASOCIADOS " +
                 "ORDER BY ID_PLAGA, ID_CULTIVO";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RelacionAsociados relacion = new RelacionAsociados();
                relacion.setIdPlaga(rs.getInt("ID_PLAGA"));
                relacion.setIdCultivo(rs.getInt("ID_CULTIVO"));
                relacion.setIdInspeccion(rs.getInt("ID_INSPECCION"));
                relacion.setIdLugarProduccion(rs.getInt("ID_LUGARPRODUCCION"));
                relacion.setTotalPlantas(rs.getInt("TOTAL_PLANTAS"));
                relacion.setNivelIncidencia(rs.getString("NIVEL_INCIDENCIA"));
                relaciones.add(relacion);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todas las relaciones: " + e.getMessage());
        }

        return relaciones;
    }

    /**
     * Busca relaciones por criterio (IDs o nivel de incidencia)
     */
    public List<RelacionAsociados> buscarRelaciones(String criterio) {
        logOperacion("BUSCAR RELACIONES ASOCIADOS POR CRITERIO");
        List<RelacionAsociados> relaciones = new ArrayList<>();
        String sql = "SELECT ID_PLAGA, ID_CULTIVO, ID_INSPECCION, ID_LUGARPRODUCCION, TOTAL_PLANTAS, NIVEL_INCIDENCIA " +
                 "FROM RELACION_ASOCIADOS " +
                 "WHERE TO_CHAR(ID_PLAGA) LIKE ? OR TO_CHAR(ID_CULTIVO) LIKE ? OR " +
                 "TO_CHAR(ID_INSPECCION) LIKE ? OR TO_CHAR(ID_LUGARPRODUCCION) LIKE ? OR " +
                 "UPPER(NIVEL_INCIDENCIA) LIKE UPPER(?) " +
                 "ORDER BY ID_PLAGA, ID_CULTIVO";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);
            stmt.setString(2, likeCriterio);
            stmt.setString(3, likeCriterio);
            stmt.setString(4, likeCriterio);
            stmt.setString(5, likeCriterio);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RelacionAsociados relacion = new RelacionAsociados();
                relacion.setIdPlaga(rs.getInt("ID_PLAGA"));
                relacion.setIdCultivo(rs.getInt("ID_CULTIVO"));
                relacion.setIdInspeccion(rs.getInt("ID_INSPECCION"));
                relacion.setIdLugarProduccion(rs.getInt("ID_LUGARPRODUCCION"));
                relacion.setTotalPlantas(rs.getInt("TOTAL_PLANTAS"));
                relacion.setNivelIncidencia(rs.getString("NIVEL_INCIDENCIA"));
                relaciones.add(relacion);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar relaciones: " + e.getMessage());
        }

        return relaciones;
    }
}