package modelo;

import database.ConexionBD;
import controlador.SesionUsuario;
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
     * Inserta una nueva sede ICA en la base de datos
     * @param sede Objeto SedeICA con los datos a insertar (correo y teléfono)
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(SedeICA sede) {
        logOperacion("INSERTAR SEDE ICA");
        String sql = "INSERT INTO SEDE_ICA (ID_SEDE, CORREO_ELECTRONICO, TELEFONO) VALUES (SEQ_SEDE_ICA.NEXTVAL, ?, ?)";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sede.getCorreoElectronico());
            stmt.setString(2, sede.getTelefono());

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar sede ICA: " + e.getMessage());
            System.out.println("SQL: " + sql);
            return false;
        }
    }

    /**
     * Actualiza un registro existente de sede ICA
     * @param sede Objeto SedeICA con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizar(SedeICA sede) {
        logOperacion("ACTUALIZAR SEDE ICA");
        // SQL para actualizar los campos de correo y teléfono de la sede ICA
        String sql = "UPDATE SEDE_ICA SET correo_electronico = ?, telefono = ? WHERE id_sede = ?";
        
        try (Connection conn = getConexion();
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
        // Verificar y obtener información de inspectores relacionados
        List<String> inspectores = obtenerInspectoresRelacionados(idSede);

        if (!inspectores.isEmpty()) {
            System.out.println("No se puede eliminar la sede ICA. Tiene " + inspectores.size() + " inspector(es) relacionado(s):");
            for (String info : inspectores) {
                System.out.println("  - " + info);
            }
            return false;
        }

        // Si no hay registros relacionados, proceder con la eliminación
        String sql = "DELETE FROM sede_ica WHERE id_sede = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSede);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Sede ICA eliminada correctamente");
                return true;
            } else {
                System.out.println("No se encontró la sede ICA con ID: " + idSede);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar sede ICA: " + e.getMessage());
            return false;
        }
    }

    private List<String> obtenerInspectoresRelacionados(int idSede) {
        List<String> inspectores = new ArrayList<>();
        String sql = "SELECT id_inspector, nombres_completos, numero_tarjeta_profesional FROM inspectores WHERE id_sede = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSede);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String info = "ID: " + rs.getInt("id_inspector") + 
                             ", Nombre: " + rs.getString("nombres_completos") + 
                             ", Tarjeta Profesional: " + rs.getString("numero_tarjeta_profesional");
                inspectores.add(info);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener inspectores relacionados: " + e.getMessage());
        }
        return inspectores;
    }

    /**
     * Obtiene una sede ICA por su ID
     * @param idSede ID de la sede ICA a buscar
     * @return Objeto SedeICA si se encuentra, null si no existe
     */
    public SedeICA obtenerPorId(int idSede) {
        logOperacion("CONSULTAR SEDE ICA POR ID");
        // SQL para seleccionar un registro específico por ID
        String sql = "SELECT * FROM SEDE_ICA WHERE id_sede = ?";
        SedeICA sede = null;  // Inicializa como null por si no se encuentra
        
        try (Connection conn = getConexion();
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
        logOperacion("CONSULTAR TODAS LAS SEDES ICA");
        List<SedeICA> sedes = new ArrayList<>();  // Lista para almacenar los resultados
        // SQL para seleccionar todos los registros ordenados por ID
        String sql = "SELECT * FROM SEDE_ICA ORDER BY ID_SEDE asc";
        
        try (Connection conn = getConexion();
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
        logOperacion("BUSCAR SEDES ICA");
        List<SedeICA> sedes = new ArrayList<>();
        // SQL para búsqueda en múltiples campos usando LIKE
        String sql = "SELECT * FROM SEDE_ICA WHERE correo_electronico LIKE ? OR telefono LIKE ? ORDER BY id_sede";
        
        try (Connection conn = getConexion();
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
    
    public boolean existeCorreoElectronico(String correoElectronico) {
        String sql = "SELECT COUNT(*) FROM SEDE_ICA WHERE UPPER(CORREO_ELECTRONICO) = UPPER(?)";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correoElectronico);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar correo electrónico de sede ICA: " + e.getMessage());
        }

        return false;
    }
    
    public boolean existeTelefono(String telefono) {
        String sql = "SELECT COUNT(*) FROM SEDE_ICA WHERE TELEFONO = ?";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, telefono);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar teléfono de sede ICA: " + e.getMessage());
        }

        return false;
    }
}