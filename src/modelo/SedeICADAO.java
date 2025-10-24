package modelo;

import database.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad SedeICA
 * Proporciona operaciones CRUD para la tabla SEDE_ICA en la base de datos
 * Maneja la gestión de sedes del ICA con información de contacto
 */
public class SedeICADAO {
    
    /**
     * Inserta una nueva sede ICA en la base de datos
     * @param sede Objeto SedeICA con los datos a insertar (correo y teléfono)
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(SedeICA sede) {
        // SQL para inserción - NO incluye ID_SEDE ya que se genera automáticamente
        String sql = "INSERT INTO SEDE_ICA (CORREO_ELECTRONICO, TELEFONO) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConexion();  // Obtiene conexión de la base de datos
             PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepara la sentencia SQL

            // Establece los parámetros en la sentencia SQL
            stmt.setString(1, sede.getCorreoElectronico());
            stmt.setString(2, sede.getTelefono());

            // Ejecuta la inserción y retorna true si se afectó al menos una fila
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar sede ICA: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza un registro existente de sede ICA
     * @param sede Objeto SedeICA con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizar(SedeICA sede) {
        // SQL para actualizar los campos de correo y teléfono de la sede ICA
        String sql = "UPDATE SEDE_ICA SET correo_electronico = ?, telefono = ? WHERE id_sede = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Establece los parámetros para la actualización
            stmt.setString(1, sede.getCorreoElectronico());
            stmt.setString(2, sede.getTelefono());
            stmt.setInt(3, sede.getIdSede());  // WHERE clause para identificar el registro
            
            // Ejecuta la actualización y verifica si se afectó alguna fila
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar sede ICA: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una sede ICA de la base de datos
     * @param idSede ID de la sede ICA a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminar(int idSede) {
        // SQL para eliminar un registro por su ID
        String sql = "DELETE FROM SEDE_ICA WHERE id_sede = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Establece el ID como parámetro para la eliminación
            stmt.setInt(1, idSede);
            
            // Ejecuta la eliminación y verifica si se afectó alguna fila
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar sede ICA: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una sede ICA por su ID
     * @param idSede ID de la sede ICA a buscar
     * @return Objeto SedeICA si se encuentra, null si no existe
     */
    public SedeICA obtenerPorId(int idSede) {
        // SQL para seleccionar un registro específico por ID
        String sql = "SELECT * FROM SEDE_ICA WHERE id_sede = ?";
        SedeICA sede = null;  // Inicializa como null por si no se encuentra
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idSede);  // Establece el parámetro del ID
            ResultSet rs = stmt.executeQuery();  // Ejecuta la consulta
            
            // Si se encontró el registro, crea y popula el objeto
            if (rs.next()) {
                sede = new SedeICA();
                sede.setIdSede(rs.getInt("id_sede"));
                sede.setCorreoElectronico(rs.getString("correo_electronico"));
                sede.setTelefono(rs.getString("telefono"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener sede ICA: " + e.getMessage());
        }
        
        return sede;  // Retorna el objeto encontrado o null
    }

    /**
     * Obtiene todas las sedes ICA de la base de datos
     * @return Lista de objetos SedeICA ordenados por ID de sede
     */
    public List<SedeICA> obtenerTodos() {
        List<SedeICA> sedes = new ArrayList<>();  // Lista para almacenar los resultados
        // SQL para seleccionar todos los registros ordenados por ID
        String sql = "SELECT * FROM SEDE_ICA ORDER BY ID_SEDE asc";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {  // try-with-resources cierra automáticamente
            
            // Itera sobre todos los registros del ResultSet
            while (rs.next()) {
                SedeICA sede = new SedeICA();
                // Pobla el objeto con los datos de la base de datos
                sede.setIdSede(rs.getInt("id_sede"));
                sede.setCorreoElectronico(rs.getString("correo_electronico"));
                sede.setTelefono(rs.getString("telefono"));
                sedes.add(sede);  // Agrega el objeto a la lista
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener sedes ICA: " + e.getMessage());
        }
        
        return sedes;  // Retorna la lista de sedes (puede estar vacía)
    }

    /**
     * Busca sedes ICA que coincidan con el criterio en correo electrónico o teléfono
     * @param criterio Texto a buscar en los campos correo_electronico y telefono
     * @return Lista de objetos SedeICA que coinciden con el criterio de búsqueda
     */
    public List<SedeICA> buscar(String criterio) {
        List<SedeICA> sedes = new ArrayList<>();
        // SQL para búsqueda en múltiples campos usando LIKE
        String sql = "SELECT * FROM SEDE_ICA WHERE correo_electronico LIKE ? OR telefono LIKE ? ORDER BY id_sede";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Prepara el criterio de búsqueda con comodines para búsqueda parcial
            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);  // correo_electronico
            stmt.setString(2, likeCriterio);  // telefono
            
            ResultSet rs = stmt.executeQuery();
            
            // Procesa todos los registros que coinciden con la búsqueda
            while (rs.next()) {
                SedeICA sede = new SedeICA();
                sede.setIdSede(rs.getInt("id_sede"));
                sede.setCorreoElectronico(rs.getString("correo_electronico"));
                sede.setTelefono(rs.getString("telefono"));
                sedes.add(sede);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar sedes ICA: " + e.getMessage());
        }
        
        return sedes;
    }
}