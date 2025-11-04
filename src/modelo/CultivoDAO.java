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
        String sql = "INSERT INTO CULTIVO (NOMBRE_CULTIVO, PLANTAS_AFECTADAS, ESTADO_PLANTA, TOTAL_PLANTAS, NIVEL_INCIDENCIA, ID_PREDIO) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cultivo.getNombreCultivo());
            stmt.setInt(2, cultivo.getPlantasAfectadas());
            stmt.setString(3, cultivo.getEstadoPlanta());
            stmt.setInt(4, cultivo.getTotalPlantas());
            stmt.setString(5, cultivo.getNivelIncidencia());
            stmt.setInt(6, cultivo.getIdPredio());

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
        String sql = "UPDATE CULTIVO SET NOMBRE_CULTIVO = ?, PLANTAS_AFECTADAS = ?, ESTADO_PLANTA = ?, TOTAL_PLANTAS = ?, NIVEL_INCIDENCIA = ?, ID_PREDIO = ? WHERE ID_CULTIVO = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cultivo.getNombreCultivo());
            stmt.setInt(2, cultivo.getPlantasAfectadas());
            stmt.setString(3, cultivo.getEstadoPlanta());
            stmt.setInt(4, cultivo.getTotalPlantas());
            stmt.setString(5, cultivo.getNivelIncidencia());
            stmt.setInt(6, cultivo.getIdPredio());
            stmt.setInt(7, cultivo.getIdCultivo());
            
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
       String sql = "{call eliminar_cultivo_cascada(?)}";

       try (Connection conn = ConexionBD.getConexion();
            CallableStatement stmt = conn.prepareCall(sql)) {

           stmt.setInt(1, idCultivo);
           stmt.execute();
           return true;

       } catch (SQLException e) {
           System.out.println("Error al eliminar cultivo en cascada: " + e.getMessage());
           return false;
       }
   }

    /**
     * Obtiene un cultivo específico por su ID.
     * 
     * @param idCultivo El identificador único del cultivo a buscar
     * @return El objeto Cultivo correspondiente al ID especificado, o null si no se encuentra
     */
    public Cultivo obtenerPorId(int idCultivo) {
        String sql = "SELECT * FROM CULTIVO WHERE ID_CULTIVO = ?";
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
        String sql = "SELECT * FROM CULTIVO ORDER BY ID_CULTIVO asc";
        
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
     * Busca cultivos que coincidan con el criterio especificado en nombre, estado de planta o nivel de incidencia.
     * La búsqueda es case-insensitive y utiliza coincidencias parciales.
     * 
     * @param criterio El texto de búsqueda para filtrar los cultivos
     * @return Una lista de objetos Cultivo que coinciden con el criterio de búsqueda
     */
    public List<Cultivo> buscar(String criterio) {
        List<Cultivo> cultivos = new ArrayList<>();
        String sql = "SELECT * FROM CULTIVO WHERE UPPER(NOMBRE_CULTIVO) LIKE UPPER(?) OR UPPER(ESTADO_PLANTA) LIKE UPPER(?) OR UPPER(NIVEL_INCIDENCIA) LIKE UPPER(?) ORDER BY NOMBRE_CULTIVO";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);
            stmt.setString(2, likeCriterio);
            stmt.setString(3, likeCriterio);
            
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
        String sql = "SELECT * FROM CULTIVO WHERE ID_PREDIO = ? ORDER BY ID_CULTIVO asc";
        
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
        cultivo.setTotalPlantas(rs.getInt("TOTAL_PLANTAS"));
        cultivo.setNivelIncidencia(rs.getString("NIVEL_INCIDENCIA"));
        cultivo.setIdPredio(rs.getInt("ID_PREDIO"));
        return cultivo;
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
}