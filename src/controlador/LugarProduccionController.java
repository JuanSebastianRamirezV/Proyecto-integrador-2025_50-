package controlador;

import modelo.LugarProduccion;
import modelo.LugarProduccionDAO;
import java.util.List;

/**
 * Controlador para la gestión de lugares de producción
 * Actúa como intermediario entre la vista y el modelo para operaciones CRUD
 */
public class LugarProduccionController {
    private LugarProduccionDAO lugarProduccionDAO;

    /**
     * Constructor - inicializa el DAO de lugares de producción
     */
    public LugarProduccionController() {
        this.lugarProduccionDAO = new LugarProduccionDAO();
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
}