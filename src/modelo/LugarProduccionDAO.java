package modelo;

import database.ConexionBD;
import controlador.SesionUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad LugarProduccion
 * Proporciona operaciones CRUD para la tabla LUGARPRODUCCION en la base de datos
 */
public class LugarProduccionDAO {
    
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
     * Inserta un nuevo lugar de producción en la base de datos
     * @param lugarProduccion Objeto LugarProduccion con los datos a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    
    public boolean insertar(LugarProduccion lugar) {
        logOperacion("INSERTAR LUGAR PRODUCCIÓN");
        Connection conn = null;
        try {
            conn = getConexion();
            conn.setAutoCommit(false);

            // Obtener el próximo ID de la secuencia
            int nextId = obtenerProximoIdSecuencia(conn, "SEQ_LUGARPRODUCCION");
            System.out.println("Próximo ID de secuencia: " + nextId);

            // Verificar si el ID ya existe
            if (idExiste(conn, nextId)) {
                System.out.println("¡ADVERTENCIA! El ID " + nextId + " ya existe en la tabla.");
                // Buscar el próximo ID disponible
                nextId = encontrarProximoIdDisponible(conn);
                System.out.println("Usando ID disponible: " + nextId);
            }

            // Insertar con el ID verificado
            String sql = "INSERT INTO LUGARPRODUCCION (ID_LUGARPRODUCCION, NOMBRE_LUGAR, NUMERO_REGISTROICA, DIRECCION, ID_PRODUCTOR) " +
                        "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, nextId);
                stmt.setString(2, lugar.getNombreLugar());
                stmt.setString(3, lugar.getNumeroRegistroICA());
                stmt.setString(4, lugar.getDireccionLugar());
                stmt.setInt(5, lugar.getIdProductor());

                int result = stmt.executeUpdate();
                if (result > 0) {
                    conn.commit();
                    System.out.println("¡Éxito! Insertado con ID: " + nextId);
                    return true;
                }
            }

            conn.rollback();
            return false;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            System.out.println("Error al insertar: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true);
                    conn.close(); 
                } catch (SQLException e) {}
            }
        }
    }

    private int obtenerProximoIdSecuencia(Connection conn, String nombreSecuencia) throws SQLException {
        String sql = "SELECT " + nombreSecuencia + ".NEXTVAL FROM DUAL";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("No se pudo obtener valor de la secuencia: " + nombreSecuencia);
        }
    }

    private boolean idExiste(Connection conn, int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM LUGARPRODUCCION WHERE ID_LUGARPRODUCCION = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private int encontrarProximoIdDisponible(Connection conn) throws SQLException {
        // Encontrar el primer ID disponible después del máximo existente
        String sql = "SELECT MAX(ID_LUGARPRODUCCION) + 1 FROM LUGARPRODUCCION";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1; // Si la tabla está vacía
        }
    }

    public List<LugarProduccion> obtenerTodos() {
        logOperacion("CONSULTAR TODOS LOS LUGARES PRODUCCIÓN");
        List<LugarProduccion> lugares = new ArrayList<>();
        String sql = "SELECT lp.*, p.NOMBRE_COMPLETO as NOMBRE_PRODUCTOR " +
                    "FROM LUGARPRODUCCION lp " +
                    "JOIN PRODUCTORES p ON lp.ID_PRODUCTOR = p.ID_PRODUCTOR " +
                    "ORDER BY lp.ID_LUGARPRODUCCION ASC";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LugarProduccion lugar = new LugarProduccion();
                lugar.setIdLugarProduccion(rs.getInt("ID_LUGARPRODUCCION"));
                lugar.setNombreLugar(rs.getString("NOMBRE_LUGAR"));
                lugar.setNumeroRegistroICA(rs.getString("NUMERO_REGISTROICA"));
                lugar.setDireccionLugar(rs.getString("DIRECCION"));
                lugar.setIdProductor(rs.getInt("ID_PRODUCTOR"));
                lugar.setNombreProductor(rs.getString("NOMBRE_PRODUCTOR"));
                lugares.add(lugar);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener lugares de producción: " + e.getMessage());
        }

        return lugares;
    }

    /**
     * Actualiza un registro existente de lugar de producción con validación de duplicados
     */
    public boolean actualizar(LugarProduccion lugar) {
        logOperacion("ACTUALIZAR LUGAR PRODUCCIÓN");
        // Primero validar que no existan duplicados (excluyendo el registro actual)
        if (existeNombreLugarExcluyendoId(lugar.getNombreLugar(), lugar.getIdLugarProduccion())) {
            System.out.println("Error: Ya existe otro lugar con el nombre: " + lugar.getNombreLugar());
            return false;
        }

        if (lugar.getNumeroRegistroICA() != null && !lugar.getNumeroRegistroICA().trim().isEmpty() &&
            existeRegistroICAExcluyendoId(lugar.getNumeroRegistroICA(), lugar.getIdLugarProduccion())) {
            System.out.println("Error: Ya existe otro lugar con el registro ICA: " + lugar.getNumeroRegistroICA());
            return false;
        }

        String sql = "UPDATE LUGARPRODUCCION SET NOMBRE_LUGAR = ?, NUMERO_REGISTROICA = ?, DIRECCION = ?, ID_PRODUCTOR = ? WHERE ID_LUGARPRODUCCION = ?";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lugar.getNombreLugar());
            stmt.setString(2, lugar.getNumeroRegistroICA());
            stmt.setString(3, lugar.getDireccionLugar());
            stmt.setInt(4, lugar.getIdProductor());
            stmt.setInt(5, lugar.getIdLugarProduccion());

            int filasAfectadas = stmt.executeUpdate();
            System.out.println("Actualización completada. Filas afectadas: " + filasAfectadas);

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar lugar de producción: " + e.getMessage());

            // Información adicional para debugging
            if (e.getErrorCode() == 1) { // ORA-00001
                System.out.println("Violación de constraint único durante actualización");
            }
            return false;
        }
    }
    /**
     * Elimina un lugar de producción de la base de datos
     * @param idLugarProduccion ID del lugar de producción a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminar(int idLugarProduccion) {
        // Verificar conteo de registros relacionados
        int totalRelaciones = contarRelacionesLugarProduccion(idLugarProduccion);

        if (totalRelaciones > 0) {
            System.out.println("No se puede eliminar el lugar de producción. Tiene " + totalRelaciones + " registro(s) relacionado(s)");
            return false;
        }

        // Si no hay registros relacionados, proceder con la eliminación
        String sql = "DELETE FROM lugarproduccion WHERE id_lugarproduccion = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLugarProduccion);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Lugar de producción eliminado correctamente");
                return true;
            } else {
                System.out.println("No se encontró el lugar de producción con ID: " + idLugarProduccion);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar lugar de producción: " + e.getMessage());
            return false;
        }
    }

    private int contarRelacionesLugarProduccion(int idLugarProduccion) {
        int total = 0;
        String[] tablasRelacionadas = {"RELACION_ASOCIADOS", "LUGARPRODUCCION_INSPECCION"};

        for (String tabla : tablasRelacionadas) {
            String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE id_lugarproduccion = ?";
            try (Connection conn = ConexionBD.getConexion();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idLugarProduccion);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    total += rs.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println("Error al contar relaciones en " + tabla + ": " + e.getMessage());
            }
        }
        return total;
    }

    /**
     * Obtiene un lugar de producción por su ID
     * @param idLugarProduccion ID del lugar de producción a buscar
     * @return Objeto LugarProduccion si se encuentra, null si no existe
     */
    public LugarProduccion obtenerPorId(int idLugarProduccion) {
        logOperacion("CONSULTAR LUGAR PRODUCCIÓN POR ID");
        String sql = "SELECT lp.*, p.NOMBRE_COMPLETO as NOMBRE_PRODUCTOR " +
                    "FROM LUGARPRODUCCION lp " +
                    "JOIN PRODUCTORES p ON lp.ID_PRODUCTOR = p.ID_PRODUCTOR " +
                    "WHERE lp.ID_LUGARPRODUCCION = ?";
        LugarProduccion lugar = null;

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLugarProduccion);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lugar = new LugarProduccion();
                lugar.setIdLugarProduccion(rs.getInt("ID_LUGARPRODUCCION"));
                lugar.setNombreLugar(rs.getString("NOMBRE_LUGAR"));
                lugar.setNumeroRegistroICA(rs.getString("NUMERO_REGISTROICA"));
                lugar.setDireccionLugar(rs.getString("DIRECCION"));
                lugar.setIdProductor(rs.getInt("ID_PRODUCTOR"));
                lugar.setNombreProductor(rs.getString("NOMBRE_PRODUCTOR"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener lugar de producción: " + e.getMessage());
        }

        return lugar;
    }

    /**
     * Busca lugares de producción que coincidan con el criterio en nombre, registro ICA o dirección
     * @param criterio Texto a buscar en los campos nombre_lugar, numero_registro_ica y direccion_lugar
     * @return Lista de objetos LugarProduccion que coinciden con el criterio de búsqueda
     */
    public List<LugarProduccion> buscar(String criterio) {
        logOperacion("BUSCAR LUGARES PRODUCCIÓN");
        List<LugarProduccion> lugares = new ArrayList<>();
        String sql = "SELECT lp.*, p.NOMBRE_COMPLETO as NOMBRE_PRODUCTOR " +
                    "FROM LUGARPRODUCCION lp " +
                    "JOIN PRODUCTORES p ON lp.ID_PRODUCTOR = p.ID_PRODUCTOR " +
                    "WHERE lp.NOMBRE_LUGAR LIKE ? OR lp.NUMERO_REGISTROICA LIKE ? OR lp.DIRECCION LIKE ? OR p.NOMBRE_COMPLETO LIKE ? " +
                    "ORDER BY lp.NOMBRE_LUGAR";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);
            stmt.setString(2, likeCriterio);
            stmt.setString(3, likeCriterio);
            stmt.setString(4, likeCriterio);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LugarProduccion lugar = new LugarProduccion();
                lugar.setIdLugarProduccion(rs.getInt("ID_LUGARPRODUCCION"));
                lugar.setNombreLugar(rs.getString("NOMBRE_LUGAR"));
                lugar.setNumeroRegistroICA(rs.getString("NUMERO_REGISTROICA"));
                lugar.setDireccionLugar(rs.getString("DIRECCION"));
                lugar.setIdProductor(rs.getInt("ID_PRODUCTOR"));
                lugar.setNombreProductor(rs.getString("NOMBRE_PRODUCTOR"));
                lugares.add(lugar);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar lugares de producción: " + e.getMessage());
        }

        return lugares;
    }
    
    /**
 * Verifica si ya existe un lugar de producción con el mismo nombre
 */
    public boolean existeNombreLugar(String nombreLugar) {
        String sql = "SELECT COUNT(*) FROM LUGARPRODUCCION WHERE UPPER(NOMBRE_LUGAR) = UPPER(?)";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreLugar);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar nombre de lugar: " + e.getMessage());
        }
        return false;
    }

/**
 * Verifica si ya existe un lugar de producción con el mismo registro ICA
 */
    public boolean existeRegistroICA(String registroICA) {
        if (registroICA == null || registroICA.trim().isEmpty()) {
            return false; // No validar si está vacío
        }

        String sql = "SELECT COUNT(*) FROM LUGARPRODUCCION WHERE UPPER(NUMERO_REGISTROICA) = UPPER(?)";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, registroICA.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar registro ICA: " + e.getMessage());
        }
        return false;
    }

/**
 * Verifica si el nombre ya existe excluyendo un ID específico (para actualizaciones)
 */
    public boolean existeNombreLugarExcluyendoId(String nombreLugar, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM LUGARPRODUCCION WHERE UPPER(NOMBRE_LUGAR) = UPPER(?) AND ID_LUGARPRODUCCION != ?";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreLugar);
            stmt.setInt(2, idExcluir);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar nombre de lugar: " + e.getMessage());
        }
        return false;
    }

/**
 * Verifica si el registro ICA ya existe excluyendo un ID específico (para actualizaciones)
 */
    public boolean existeRegistroICAExcluyendoId(String registroICA, int idExcluir) {
        if (registroICA == null || registroICA.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM LUGARPRODUCCION WHERE UPPER(NUMERO_REGISTROICA) = UPPER(?) AND ID_LUGARPRODUCCION != ?";

        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, registroICA.trim());
            stmt.setInt(2, idExcluir);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar registro ICA: " + e.getMessage());
        }
        return false;
    }
}