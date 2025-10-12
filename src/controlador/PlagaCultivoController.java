package controlador;

import modelo.PlagaCultivo;
import modelo.PlagaCultivoDAO;
import java.util.List;

/**
 * Controlador para gestionar las operaciones entre plagas y cultivos.
 * Actúa como intermediario entre la capa de presentación y el acceso a datos.
 * 
 * @author [Nombre del autor]
 * @version 1.0
 */
public class PlagaCultivoController {
    
    private PlagaCultivoDAO plagaCultivoDAO;

    /**
     * Constructor que inicializa el DAO para operaciones de base de datos.
     */
    public PlagaCultivoController() {
        this.plagaCultivoDAO = new PlagaCultivoDAO();
    }

    /**
     * Agrega una nueva relación entre una plaga y un cultivo.
     * 
     * @param plagaCultivo Objeto que contiene la relación plaga-cultivo a agregar
     * @return true si la operación fue exitosa, false en caso contrario
     */
    public boolean agregarPlagaCultivo(PlagaCultivo plagaCultivo) {
        return plagaCultivoDAO.insertar(plagaCultivo);
    }

    /**
     * Actualiza la información de una relación plaga-cultivo existente.
     * 
     * @param plagaCultivo Objeto con los datos actualizados de la relación
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarPlagaCultivo(PlagaCultivo plagaCultivo) {
        return plagaCultivoDAO.actualizar(plagaCultivo);
    }

    /**
     * Elimina la relación entre una plaga específica y un cultivo.
     * 
     * @param idPlaga Identificador único de la plaga
     * @param idCultivo Identificador único del cultivo
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarPlagaCultivo(int idPlaga, int idCultivo) {
        return plagaCultivoDAO.eliminar(idPlaga, idCultivo);
    }

    /**
     * Obtiene una relación específica entre plaga y cultivo por sus IDs.
     * 
     * @param idPlaga Identificador único de la plaga
     * @param idCultivo Identificador único del cultivo
     * @return Objeto PlagaCultivo con la información de la relación, null si no existe
     */
    public PlagaCultivo obtenerPlagaCultivo(int idPlaga, int idCultivo) {
        return plagaCultivoDAO.obtenerPorIds(idPlaga, idCultivo);
    }

    /**
     * Obtiene todas las plagas asociadas a un cultivo específico.
     * 
     * @param idCultivo Identificador único del cultivo
     * @return Lista de relaciones PlagaCultivo asociadas al cultivo
     */
    public List<PlagaCultivo> obtenerPlagasPorCultivo(int idCultivo) {
        return plagaCultivoDAO.obtenerPorCultivo(idCultivo);
    }

    /**
     * Obtiene todos los cultivos afectados por una plaga específica.
     * 
     * @param idPlaga Identificador único de la plaga
     * @return Lista de relaciones PlagaCultivo asociadas a la plaga
     */
    public List<PlagaCultivo> obtenerCultivosPorPlaga(int idPlaga) {
        return plagaCultivoDAO.obtenerPorPlaga(idPlaga);
    }
}