package controlador;

import modelo.Productor;
import modelo.ProductorDAO;
import java.util.List;

/**
 * Controlador para gestionar las operaciones relacionadas con Productores.
 * Actúa como intermediario entre la capa de vista y la capa de acceso a datos.
 * Proporciona métodos para realizar operaciones CRUD y búsquedas sobre productores.
 */
public class ProductorController {
    private ProductorDAO productorDAO;

    /**
     * Constructor por defecto que inicializa el DAO de Productor.
     * Crea una nueva instancia de ProductorDAO para interactuar con la base de datos.
     */
    public ProductorController() {
        this.productorDAO = new ProductorDAO();
    }

    /**
     * Agrega un nuevo productor al sistema.
     * 
     * @param productor El objeto Productor a ser agregado
     * @return true si el productor fue insertado exitosamente, false en caso contrario
     */
    public boolean agregarProductor(Productor productor) {
        return productorDAO.insertar(productor);
    }

    /**
     * Actualiza la información de un productor existente.
     * 
     * @param productor El objeto Productor con la información actualizada
     * @return true si el productor fue actualizado exitosamente, false en caso contrario
     */
    public boolean actualizarProductor(Productor productor) {
        return productorDAO.actualizar(productor);
    }

    /**
     * Elimina un productor del sistema basado en su ID.
     * 
     * @param idProductor El identificador único del productor a eliminar
     * @return true si el productor fue eliminado exitosamente, false en caso contrario
     */
    public boolean eliminarProductor(int idProductor) {
        return productorDAO.eliminar(idProductor);
    }

    /**
     * Obtiene un productor específico basado en su ID.
     * 
     * @param idProductor El identificador único del productor a buscar
     * @return El objeto Productor encontrado, o null si no existe
     */
    public Productor obtenerProductor(int idProductor) {
        return productorDAO.obtenerPorId(idProductor);
    }

    /**
     * Obtiene todos los productores registrados en el sistema.
     * 
     * @return Lista de todos los objetos Productor existentes
     */
    public List<Productor> obtenerTodosProductores() {
        return productorDAO.obtenerTodos();
    }

    /**
     * Busca productores que coincidan con un criterio específico.
     * 
     * @param criterio El término de búsqueda para filtrar productores
     * @return Lista de productores que coinciden con el criterio de búsqueda
     */
    public List<Productor> buscarProductores(String criterio) {
        return productorDAO.buscar(criterio);
    }
}