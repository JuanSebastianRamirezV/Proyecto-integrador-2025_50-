/**
 * Controlador para la gestión de Inspectores
 * 
 * Actúa como intermediario entre la capa de vista y el modelo de datos
 * para las operaciones CRUD de inspectores.
 */
package controlador;

import modelo.Inspector;
import modelo.InspectorDAO;
import java.util.List;

public class InspectorController {
    private InspectorDAO inspectorDAO;

    /**
     * Constructor - inicializa el DAO de inspectores
     */
    public InspectorController() {
        this.inspectorDAO = new InspectorDAO();
    }

    /**
     * Agrega un nuevo inspector al sistema
     * @param inspector Objeto Inspector a registrar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean agregarInspector(Inspector inspector) {
        return inspectorDAO.insertar(inspector);
    }

    /**
     * Actualiza la información de un inspector existente
     * @param inspector Objeto Inspector con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarInspector(Inspector inspector) {
        return inspectorDAO.actualizar(inspector);
    }

    /**
     * Elimina un inspector del sistema
     * @param idInspector ID del inspector a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarInspector(int idInspector) {
        return inspectorDAO.eliminar(idInspector);
    }

    /**
     * Obtiene un inspector por su ID
     * @param idInspector ID del inspector a buscar
     * @return Objeto Inspector encontrado o null si no existe
     */
    public Inspector obtenerInspector(int idInspector) {
        return inspectorDAO.obtenerPorId(idInspector);
    }

    /**
     * Obtiene todos los inspectores registrados en el sistema
     * @return Lista de todos los inspectores
     */
    public List<Inspector> obtenerTodosInspectores() {
        return inspectorDAO.obtenerTodos();
    }

    /**
     * Busca inspectores según un criterio
     * @param criterio Texto para buscar en los campos del inspector
     * @return Lista de inspectores que coinciden con el criterio
     */
    public List<Inspector> buscarInspectores(String criterio) {
        return inspectorDAO.buscar(criterio);
    }
    public boolean existeInspector(String numeroDocumento) {
        return inspectorDAO.existeNumeroDocumento(numeroDocumento);
    }
}