package controlador;

import modelo.LugarProduccion;
import modelo.LugarProduccionDAO;
import java.util.List;
import modelo.Productor;
import modelo.ProductorDAO;

/**
 * Controlador para la gestión de lugares de producción
 * Actúa como intermediario entre la vista y el modelo para operaciones CRUD
 */
public class LugarProduccionController {
    private LugarProduccionDAO lugarProduccionDAO;
    private ProductorDAO productorDAO;

    /**
     * Constructor - inicializa el DAO de lugares de producción
     */
    public LugarProduccionController() {
        this.lugarProduccionDAO = new LugarProduccionDAO();
        this.productorDAO = new ProductorDAO();
    }

    /**
     * Agrega un nuevo lugar de producción
     */
    public boolean agregarLugarProduccion(LugarProduccion lugar) {
        return lugarProduccionDAO.insertar(lugar);
    }

    /**
     * Actualiza un lugar de producción existente
     */
    public boolean actualizarLugarProduccion(LugarProduccion lugar) {
        return lugarProduccionDAO.actualizar(lugar);
    }

    /**
     * Elimina un lugar de producción por ID
     */
    public boolean eliminarLugarProduccion(int idLugarProduccion) {
        return lugarProduccionDAO.eliminar(idLugarProduccion);
    }

    /**
     * Obtiene un lugar de producción por ID
     */
    public LugarProduccion obtenerLugarProduccion(int idLugarProduccion) {
        return lugarProduccionDAO.obtenerPorId(idLugarProduccion);
    }

    /**
     * Obtiene todos los lugares de producción
     */
    public List<LugarProduccion> obtenerTodosLugaresProduccion() {
        return lugarProduccionDAO.obtenerTodos();
    }

    /**
     * Busca lugares de producción por criterio
     */
    public List<LugarProduccion> buscarLugaresProduccion(String criterio) {
        return lugarProduccionDAO.buscar(criterio);
    }
        
    /**
     * Obtiene todos los productores para los combobox
     */
    public List<Productor> obtenerTodosProductores() {
        return productorDAO.obtenerTodos();
    }

    /**
     * Obtiene un productor por ID
     */
    public Productor obtenerProductor(int idProductor) {
        return productorDAO.obtenerPorId(idProductor);
    }
    
    /**
    * Verifica si ya existe un lugar con el mismo nombre
    */
   public boolean existeNombreLugar(String nombreLugar) {
       return lugarProduccionDAO.existeNombreLugar(nombreLugar);
   }

   /**
    * Verifica si ya existe un lugar con el mismo registro ICA
    */
   public boolean existeRegistroICA(String registroICA) {
       return lugarProduccionDAO.existeRegistroICA(registroICA);
   }

   /**
    * Verifica si el nombre ya existe excluyendo un ID específico
    */
   public boolean existeNombreLugarExcluyendoId(String nombreLugar, int idExcluir) {
       return lugarProduccionDAO.existeNombreLugarExcluyendoId(nombreLugar, idExcluir);
   }

   /**
    * Verifica si el registro ICA ya existe excluyendo un ID específico
    */
   public boolean existeRegistroICAExcluyendoId(String registroICA, int idExcluir) {
       return lugarProduccionDAO.existeRegistroICAExcluyendoId(registroICA, idExcluir);
   }
}