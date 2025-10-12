/**
 * Paquete controlador - Contiene las clases controladoras que actúan como intermediarias
 * entre la capa de presentación (vista) y la capa de acceso a datos (modelo)
 */
package controlador;

import modelo.Inspeccion;
import modelo.InspeccionDAO;
import java.util.List;

/**
 * Clase InspeccionController - Controlador para la gestión de inspecciones
 * 
 * Esta clase actúa como intermediario entre la capa de presentación (GUI) y la capa de acceso a datos (DAO).
 * Implementa el patrón Controller del MVC (Modelo-Vista-Controlador) para separar las responsabilidades
 * y centralizar la lógica de negocio relacionada con las operaciones de inspecciones.
 * 
 * Responsabilidades principales:
 * - Delegar operaciones CRUD al DAO correspondiente
 * - Centralizar la lógica de negocio de inspecciones
 * - Proporcionar una interfaz simplificada para la capa de presentación
 * - Manejar la transformación y validación de datos entre capas
 * 
 * @author Sistema de Gestión Agrícola
 * @version 1.0
 */
public class InspeccionController {
    
    /**
     * Objeto DAO (Data Access Object) para realizar operaciones de persistencia
     * sobre la entidad Inspeccion en la base de datos
     */
    private InspeccionDAO inspeccionDAO;

    /**
     * Constructor por defecto de la clase InspeccionController
     * Inicializa el objeto InspeccionDAO para permitir el acceso a datos
     */
    public InspeccionController() {
        this.inspeccionDAO = new InspeccionDAO();
    }

    /**
     * Agrega una nueva inspección al sistema
     * 
     * @param inspeccion Objeto Inspeccion con los datos de la inspección a registrar
     * @return true si la inspección fue agregada exitosamente, false en caso contrario
     */
    public boolean agregarInspeccion(Inspeccion inspeccion) {
        return inspeccionDAO.insertar(inspeccion);
    }

    /**
     * Actualiza los datos de una inspección existente en el sistema
     * 
     * @param inspeccion Objeto Inspeccion con los datos actualizados
     * @return true si la inspección fue actualizada exitosamente, false en caso contrario
     */
    public boolean actualizarInspeccion(Inspeccion inspeccion) {
        return inspeccionDAO.actualizar(inspeccion);
    }

    /**
     * Elimina una inspección del sistema basado en su identificador único
     * 
     * @param idInspeccion Identificador único de la inspección a eliminar
     * @return true si la inspección fue eliminada exitosamente, false en caso contrario
     */
    public boolean eliminarInspeccion(int idInspeccion) {
        return inspeccionDAO.eliminar(idInspeccion);
    }

    /**
     * Obtiene una inspección específica basada en su identificador único
     * 
     * @param idInspeccion Identificador único de la inspección a recuperar
     * @return Objeto Inspeccion con todos sus datos, o null si no se encuentra
     */
    public Inspeccion obtenerInspeccion(int idInspeccion) {
        return inspeccionDAO.obtenerPorId(idInspeccion);
    }

    /**
     * Obtiene todas las inspecciones registradas en el sistema
     * 
     * @return Lista de objetos Inspeccion con todas las inspecciones existentes
     *         Lista vacía si no hay inspecciones registradas
     */
    public List<Inspeccion> obtenerTodasInspecciones() {
        return inspeccionDAO.obtenerTodos();
    }

    /**
     * Busca inspecciones que coincidan con un criterio específico
     * 
     * @param criterio Texto de búsqueda para filtrar las inspecciones. Puede buscar
     *                 en múltiples campos como fecha, estado, observaciones, etc.
     * @return Lista de objetos Inspeccion que coinciden con el criterio de búsqueda
     *         Lista vacía si no se encuentran coincidencias
     */
    public List<Inspeccion> buscarInspecciones(String criterio) {
        return inspeccionDAO.buscar(criterio);
    }
}