package controlador;

import modelo.Plaga;
import modelo.PlagaDAO;
import java.util.List;

/**
 * Controlador para gestionar las operaciones de plagas
 * Coordina las interacciones entre la vista y el modelo PlagaDAO
 */
public class PlagaController {
    private PlagaDAO plagaDAO;

    /**
     * Constructor - inicializa el DAO de plagas
     */
    public PlagaController() {
        this.plagaDAO = new PlagaDAO();
    }

    /**
     * Agrega una nueva plaga al sistema
     * @param plaga Objeto Plaga a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean agregarPlaga(Plaga plaga) {
        return plagaDAO.insertar(plaga);
    }

    /**
     * Actualiza los datos de una plaga existente
     * @param plaga Objeto Plaga con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarPlaga(Plaga plaga) {
        return plagaDAO.actualizar(plaga);
    }

    /**
     * Elimina una plaga del sistema
     * @param idPlaga ID de la plaga a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarPlaga(int idPlaga) {
        return plagaDAO.eliminar(idPlaga);
    }

    /**
     * Obtiene una plaga específica por su ID
     * @param idPlaga ID de la plaga a buscar
     * @return Objeto Plaga encontrado o null si no existe
     */
    public Plaga obtenerPlaga(int idPlaga) {
        return plagaDAO.obtenerPorId(idPlaga);
    }

    /**
     * Obtiene todas las plagas registradas en el sistema
     * @return Lista de todas las plagas
     */
    public List<Plaga> obtenerTodasPlagas() {
        return plagaDAO.obtenerTodos();
    }

    /**
     * Busca plagas según un criterio específico
     * @param criterio Texto de búsqueda
     * @return Lista de plagas que coinciden con el criterio
     */
    public List<Plaga> buscarPlagas(String criterio) {
        return plagaDAO.buscar(criterio);
    }
}