 /**
 * Data Access Object (DAO) para la gestión de operaciones de base de datos relacionadas con la entidad Cultivo.
 * Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) y consultas específicas
 * sobre la tabla CULTIVO en la base de datos.
 */
package modelo;

import database.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CultivoDAO {
    
    /**
     * Inserta un nuevo cultivo en la base de datos.
     * 
     * @param cultivo El objeto Cultivo con la información a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(Cultivo cultivo) {
        // ✅ INSERTAR CON TOTAL_PLANTAS
        String sql = "INSERT INTO CULTIVO (ID_CULTIVO, NOMBRE_CULTIVO, PLANTAS_AFECTADAS, ESTADO_PLANTA, TOTAL_PLANTAS, ID_PREDIO) VALUES (SEQ_CULTIVO.NEXTVAL, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cultivo.getNombreCultivo());
            stmt.setInt(2, cultivo.getPlantasAfectadas());
            stmt.setString(3, cultivo.getEstadoPlanta());
            stmt.setInt(4, cultivo.getTotalPlantas()); // ✅ NUEVO PARÁMETRO
            stmt.setInt(5, cultivo.getIdPredio());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar cultivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza la información de un cultivo existente en la base de datos.
     * 
     * @param cultivo El objeto Cultivo con la información actualizada
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizar(Cultivo cultivo) {
        // ✅ ACTUALIZAR CON TOTAL_PLANTAS
        String sql = "UPDATE CULTIVO SET NOMBRE_CULTIVO = ?, PLANTAS_AFECTADAS = ?, ESTADO_PLANTA = ?, TOTAL_PLANTAS = ?, ID_PREDIO = ? WHERE ID_CULTIVO = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cultivo.getNombreCultivo());
            stmt.setInt(2, cultivo.getPlantasAfectadas());
            stmt.setString(3, cultivo.getEstadoPlanta());
            stmt.setInt(4, cultivo.getTotalPlantas()); // ✅ NUEVO PARÁMETRO
            stmt.setInt(5, cultivo.getIdPredio());
            stmt.setInt(6, cultivo.getIdCultivo());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar cultivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un cultivo de la base de datos por su ID con eliminación en cascada.
     * 
     * @param idCultivo El identificador único del cultivo a eliminar
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

    private boolean tieneRegistrosRelacionados(int idCultivo) {
        String[] tablasRelacionadas = {"INSPECCION", "RELACION_ASOCIADOS"};

        for (String tabla : tablasRelacionadas) {
            String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE id_cultivo = ?";
            try (Connection conn = ConexionBD.getConexion();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idCultivo);
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
     * Obtiene un cultivo específico por su ID.
     * 
     * @param idCultivo El identificador único del cultivo a buscar
     * @return El objeto Cultivo correspondiente al ID especificado, o null si no se encuentra
     */
    public Cultivo obtenerPorId(int idCultivo) {
        // ✅ CONSULTAR CON TOTAL_PLANTAS
        String sql = "SELECT c.* FROM CULTIVO c WHERE c.ID_CULTIVO = ?";
        Cultivo cultivo = null;
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCultivo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                cultivo = mapearCultivo(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cultivo por ID: " + e.getMessage());
        }
        
        return cultivo;
    }

    /**
     * Obtiene todos los cultivos registrados en la base de datos, ordenados por nombre.
     * 
     * @return Una lista de todos los objetos Cultivo existentes en la base de datos
     */
    public List<Cultivo> obtenerTodos() {
        List<Cultivo> cultivos = new ArrayList<>();
        // ✅ CONSULTAR CON TOTAL_PLANTAS
        String sql = "SELECT c.* FROM CULTIVO c ORDER BY c.ID_CULTIVO asc";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Cultivo cultivo = mapearCultivo(rs);
                cultivos.add(cultivo);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los cultivos: " + e.getMessage());
        }
        
        return cultivos;
    }

    /**
     * Busca cultivos que coincidan con el criterio especificado en nombre, estado de planta.
     * La búsqueda es case-insensitive y utiliza coincidencias parciales.
     * 
     * @param criterio El texto de búsqueda para filtrar los cultivos
     * @return Una lista de objetos Cultivo que coinciden con el criterio de búsqueda
     */
    public List<Cultivo> buscar(String criterio) {
        List<Cultivo> cultivos = new ArrayList<>();
        // ✅ BUSCAR SOLO EN CAMPOS RELEVANTES DE CULTIVO
        String sql = "SELECT c.* FROM CULTIVO c " +
                     "WHERE UPPER(c.NOMBRE_CULTIVO) LIKE UPPER(?) OR UPPER(c.ESTADO_PLANTA) LIKE UPPER(?) " +
                     "ORDER BY c.NOMBRE_CULTIVO";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);
            stmt.setString(2, likeCriterio);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cultivo cultivo = mapearCultivo(rs);
                cultivos.add(cultivo);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cultivos: " + e.getMessage());
        }
        
        return cultivos;
    }

    /**
     * Obtiene todos los cultivos asociados a un predio específico.
     * 
     * @param idPredio El identificador único del predio
     * @return Una lista de objetos Cultivo asociados al predio especificado
     */
    public List<Cultivo> obtenerPorPredio(int idPredio) {
        List<Cultivo> cultivos = new ArrayList<>();
        // ✅ CONSULTAR CON TOTAL_PLANTAS
        String sql = "SELECT c.* FROM CULTIVO c WHERE c.ID_PREDIO = ? ORDER BY c.ID_CULTIVO asc";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPredio);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cultivo cultivo = mapearCultivo(rs);
                cultivos.add(cultivo);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cultivos por predio: " + e.getMessage());
        }
        
        return cultivos;
    }

    /**
     * Verifica si ya existe un cultivo con el mismo nombre en el mismo predio.
     * Útil para evitar duplicados al insertar nuevos cultivos.
     * 
     * @param nombreCultivo El nombre del cultivo a verificar
     * @param idPredio El identificador del predio
     * @return true si ya existe un cultivo con ese nombre en el predio, false en caso contrario
     */
    public boolean existeNombreCultivo(String nombreCultivo, int idPredio) {
        String sql = "SELECT COUNT(*) FROM CULTIVO WHERE UPPER(NOMBRE_CULTIVO) = UPPER(?) AND ID_PREDIO = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreCultivo);
            stmt.setInt(2, idPredio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar nombre de cultivo: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Verifica si ya existe un cultivo con el mismo nombre en el mismo predio, excluyendo un ID específico.
     * Útil para validaciones durante la actualización de cultivos existentes.
     * 
     * @param nombreCultivo El nombre del cultivo a verificar
     * @param idPredio El identificador del predio
     * @param idCultivoExcluir ID del cultivo a excluir de la verificación
     * @return true si ya existe otro cultivo con ese nombre en el predio, false en caso contrario
     */
    public boolean existeNombreCultivoExcluyendoId(String nombreCultivo, int idPredio, int idCultivoExcluir) {
        String sql = "SELECT COUNT(*) FROM CULTIVO WHERE UPPER(NOMBRE_CULTIVO) = UPPER(?) AND ID_PREDIO = ? AND ID_CULTIVO != ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreCultivo);
            stmt.setInt(2, idPredio);
            stmt.setInt(3, idCultivoExcluir);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar nombre de cultivo excluyendo ID: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Calcula el total de plantas afectadas para todos los cultivos de un predio específico.
     * 
     * @param idPredio El identificador único del predio
     * @return La suma total de plantas afectadas en el predio especificado
     */
    public int contarPlantasAfectadasPorPredio(int idPredio) {
        String sql = "SELECT SUM(PLANTAS_AFECTADAS) FROM CULTIVO WHERE ID_PREDIO = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPredio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar plantas afectadas: " + e.getMessage());
        }
        
        return 0;
    }

    /**
     * Calcula el total de plantas para todos los cultivos de un predio específico.
     * 
     * @param idPredio El identificador único del predio
     * @return La suma total de plantas en el predio especificado
     */
    public int contarTotalPlantasPorPredio(int idPredio) {
        String sql = "SELECT SUM(TOTAL_PLANTAS) FROM CULTIVO WHERE ID_PREDIO = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPredio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar total de plantas: " + e.getMessage());
        }
        
        return 0;
    }

    /**
     * Verifica si existe un predio con el ID especificado en la base de datos.
     * Útil para validar referencias externas antes de realizar operaciones con cultivos.
     * 
     * @param idPredio El identificador único del predio a verificar
     * @return true si el predio existe, false en caso contrario
     */
    public boolean existePredio(int idPredio) {
        String sql = "SELECT COUNT(*) FROM PREDIO WHERE ID_PREDIO = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPredio);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar predio: " + e.getMessage());
        }

        return false;
    }

    /**
     * Mapea un ResultSet a un objeto Cultivo.
     * Método auxiliar privado para convertir filas de la base de datos en objetos Java.
     * 
     * @param rs El ResultSet que contiene los datos del cultivo
     * @return Un objeto Cultivo poblado con los datos del ResultSet
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet
     */
    private Cultivo mapearCultivo(ResultSet rs) throws SQLException {
        Cultivo cultivo = new Cultivo();
        cultivo.setIdCultivo(rs.getInt("ID_CULTIVO"));
        cultivo.setNombreCultivo(rs.getString("NOMBRE_CULTIVO"));
        cultivo.setPlantasAfectadas(rs.getInt("PLANTAS_AFECTADAS"));
        cultivo.setEstadoPlanta(rs.getString("ESTADO_PLANTA"));
        cultivo.setTotalPlantas(rs.getInt("TOTAL_PLANTAS")); // ✅ MAPEAR NUEVO CAMPO
        cultivo.setIdPredio(rs.getInt("ID_PREDIO"));
        return cultivo;
    }

    /**
     * Obtiene estadísticas de cultivos por predio.
     * 
     * @param idPredio El identificador único del predio
     * @return Array con [totalCultivos, totalPlantas, totalPlantasAfectadas]
     */
    public int[] obtenerEstadisticasPorPredio(int idPredio) {
        String sql = "SELECT COUNT(*), SUM(TOTAL_PLANTAS), SUM(PLANTAS_AFECTADAS) FROM CULTIVO WHERE ID_PREDIO = ?";
        int[] estadisticas = new int[3]; // [totalCultivos, totalPlantas, totalPlantasAfectadas]
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPredio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                estadisticas[0] = rs.getInt(1); // Total cultivos
                estadisticas[1] = rs.getInt(2); // Total plantas
                estadisticas[2] = rs.getInt(3); // Total plantas afectadas
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener estadísticas por predio: " + e.getMessage());
        }
        
        return estadisticas;
    }

    /**
     * Obtiene el porcentaje de plantas afectadas para un cultivo específico.
     * 
     * @param idCultivo El identificador único del cultivo
     * @return Porcentaje de plantas afectadas (0-100), o 0 si no hay plantas totales
     */
    public double obtenerPorcentajeAfectacion(int idCultivo) {
        String sql = "SELECT TOTAL_PLANTAS, PLANTAS_AFECTADAS FROM CULTIVO WHERE ID_CULTIVO = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCultivo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int totalPlantas = rs.getInt("TOTAL_PLANTAS");
                int plantasAfectadas = rs.getInt("PLANTAS_AFECTADAS");
                
                if (totalPlantas > 0) {
                    return (plantasAfectadas * 100.0) / totalPlantas;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener porcentaje de afectación: " + e.getMessage());
        }
        
        return 0.0;
    }
}