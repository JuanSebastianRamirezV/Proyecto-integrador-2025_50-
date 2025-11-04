package modelo;

import database.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad LugarProduccion
 * Proporciona operaciones CRUD para la tabla LUGARPRODUCCION en la base de datos
 */
public class LugarProduccionDAO {
    
    /**
     * Inserta un nuevo lugar de producción en la base de datos
     * @param lugarProduccion Objeto LugarProduccion con los datos a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(LugarProduccion lugarProduccion) {
        // SQL para inserción - NO incluye ID_LUGARPRODUCCION ya que se genera automáticamente
        String sql = "INSERT INTO LUGARPRODUCCION (NOMBRE_LUGAR, NUMERO_REGISTROICA, DIRECCION, ID_PRODUCTOR, ID_CULTIVO) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();  // Obtiene conexión de la base de datos
             PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepara la sentencia SQL

            // Establece los parámetros en la sentencia SQL
            stmt.setString(1, lugarProduccion.getNombreLugar());
            stmt.setString(2, lugarProduccion.getNumeroRegistroICA());
            stmt.setString(3, lugarProduccion.getDireccionLugar());
            stmt.setInt(4, lugarProduccion.getIdProductor());
            stmt.setInt(5, lugarProduccion.getIdCultivo());

            // Ejecuta la inserción y retorna true si se afectó al menos una fila
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar lugar de producción: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza un registro existente de lugar de producción
     * @param lugar Objeto LugarProduccion con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizar(LugarProduccion lugar) {
        // SQL para actualizar todos los campos del lugar de producción
        String sql = "UPDATE LUGARPRODUCCION SET NOMBRE_LUGAR = ?, NUMERO_REGISTROICA = ?, DIRECCION = ?, ID_PRODUCTOR = ?, ID_CULTIVO = ? WHERE ID_LUGARPRODUCCION = ?";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Establece los parámetros para la actualización
            stmt.setString(1, lugar.getNombreLugar());
            stmt.setString(2, lugar.getNumeroRegistroICA());
            stmt.setString(3, lugar.getDireccionLugar());
            stmt.setInt(4, lugar.getIdProductor());
            stmt.setInt(5, lugar.getIdCultivo());
            stmt.setInt(6, lugar.getIdLugarProduccion());  // WHERE clause para identificar el registro
            
            // Ejecuta la actualización y verifica si se afectó alguna fila
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar lugar de producción: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un lugar de producción de la base de datos
     * @param idLugarProduccion ID del lugar de producción a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminar(int idLugarProduccion) {
        String sql = "{call eliminar_lugar_cascada(?)}";

        try (Connection conn = ConexionBD.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idLugarProduccion);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar lugar de producción en cascada: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene un lugar de producción por su ID
     * @param idLugarProduccion ID del lugar de producción a buscar
     * @return Objeto LugarProduccion si se encuentra, null si no existe
     */
    public LugarProduccion obtenerPorId(int idLugarProduccion) {
        // SQL para seleccionar un registro específico por ID
        String sql = "SELECT * FROM LUGARPRODUCCION WHERE ID_LUGARPRODUCCION = ?";
        LugarProduccion lugar = null;  // Inicializa como null por si no se encuentra
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idLugarProduccion);  // Establece el parámetro del ID
            ResultSet rs = stmt.executeQuery();  // Ejecuta la consulta
            
            // Si se encontró el registro, crea y popula el objeto
            if (rs.next()) {
                lugar = new LugarProduccion();
                lugar.setIdLugarProduccion(rs.getInt("ID_LUGARPRODUCCION"));
                lugar.setNombreLugar(rs.getString("NOMBRE_LUGAR"));
                lugar.setNumeroRegistroICA(rs.getString("NUMERO_REGISTROICA"));
                lugar.setDireccionLugar(rs.getString("DIRECCION"));
                lugar.setIdProductor(rs.getInt("ID_PRODUCTOR"));
                lugar.setIdCultivo(rs.getInt("ID_CULTIVO"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener lugar de producción: " + e.getMessage());
        }
        
        return lugar;  // Retorna el objeto encontrado o null
    }

    /**
     * Obtiene todos los lugares de producción de la base de datos
     * @return Lista de objetos LugarProduccion ordenados por nombre
     */
    public List<LugarProduccion> obtenerTodos() {
        List<LugarProduccion> lugares = new ArrayList<>();  // Lista para almacenar los resultados
        // SQL para seleccionar todos los registros ordenados por nombre
        String sql = "SELECT * FROM LugarProduccion ORDER BY ID_LUGARPRODUCCION ASC";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {  // try-with-resources cierra automáticamente
            
            // Itera sobre todos los registros del ResultSet
            while (rs.next()) {
                LugarProduccion lugar = new LugarProduccion();
                // Pobla el objeto con los datos de la base de datos
                lugar.setIdLugarProduccion(rs.getInt("ID_LUGARPRODUCCION"));
                lugar.setNombreLugar(rs.getString("nombre_lugar"));
                lugar.setNumeroRegistroICA(rs.getString("NUMERO_REGISTROICA"));
                lugar.setDireccionLugar(rs.getString("DIRECCION"));
                lugar.setIdProductor(rs.getInt("id_productor"));
                lugar.setIdCultivo(rs.getInt("id_cultivo"));
                lugares.add(lugar);  // Agrega el objeto a la lista
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener lugares de producción: " + e.getMessage());
        }
        
        return lugares;  // Retorna la lista de lugares (puede estar vacía)
    }

    /**
     * Busca lugares de producción que coincidan con el criterio en nombre, registro ICA o dirección
     * @param criterio Texto a buscar en los campos nombre_lugar, numero_registro_ica y direccion_lugar
     * @return Lista de objetos LugarProduccion que coinciden con el criterio de búsqueda
     */
    public List<LugarProduccion> buscar(String criterio) {
        List<LugarProduccion> lugares = new ArrayList<>();
        // SQL para búsqueda en múltiples campos usando LIKE
        String sql = "SELECT * FROM LUGARPRODUCCION WHERE NOMBRE_LUGAR LIKE ? OR NUMERO_REGISTROICA LIKE ? OR DIRECCION LIKE ? ORDER BY NOMBRE_LUGAR";
        
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Prepara el criterio de búsqueda con comodines para búsqueda parcial
            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);  // nombre_lugar
            stmt.setString(2, likeCriterio);  // numero_registro_ica
            stmt.setString(3, likeCriterio);  // direccion_lugar
            
            ResultSet rs = stmt.executeQuery();
            
            // Procesa todos los registros que coinciden con la búsqueda
            while (rs.next()) {
                LugarProduccion lugar = new LugarProduccion();
                lugar.setIdLugarProduccion(rs.getInt("ID_LUGARPRODUCCION"));
                lugar.setNombreLugar(rs.getString("nombre_lugar"));
                lugar.setNumeroRegistroICA(rs.getString("NUMERO_REGISTROICA"));
                lugar.setDireccionLugar(rs.getString("DIRECCION"));
                lugar.setIdProductor(rs.getInt("id_productor"));
                lugar.setIdCultivo(rs.getInt("id_cultivo"));
                lugares.add(lugar);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar lugares de producción: " + e.getMessage());
        }
        
        return lugares;
    }
}