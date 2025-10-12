/**
 * Clase CultivoController
 * 
 * Controlador para la gestión de operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * relacionadas con la entidad Cultivo. Esta clase actúa como intermediario entre
 * la capa de vista (interfaz de usuario) y la capa de acceso a datos (DAO),
 * aplicando la lógica de negocio necesaria.
 * 
 * Responsabilidades principales:
 * - Coordinar las operaciones de gestión de cultivos
 * - Proporcionar una interfaz unificada para las operaciones CRUD
 * - Manejar la comunicación con la capa de persistencia
 * - Aplicar validaciones y reglas de negocio (si las hubiera)
 
   */
package controlador;

import modelo.Cultivo;
import modelo.CultivoDAO;
import java.util.List;

public class CultivoController {
    
    /**
     * Instancia del Data Access Object (DAO) para operaciones de persistencia
     * de cultivos. Proporciona los métodos para interactuar con la base de datos.
     */
    private CultivoDAO cultivoDAO;

    /**
     * Constructor de la clase CultivoController.
     * Inicializa la instancia del DAO necesario para las operaciones de persistencia.
     * 
     * Flujo de inicialización:
     * 1. Crea una nueva instancia de CultivoDAO
     * 2. Establece la conexión con la base de datos
     * 3. Prepara los recursos necesarios para las operaciones CRUD
     */
    public CultivoController() {
        this.cultivoDAO = new CultivoDAO();
    }

    /**
     * Agrega un nuevo cultivo al sistema.
     * Valida y registra un cultivo en la base de datos mediante el DAO.
     * 
     * @param cultivo Objeto Cultivo con los datos a registrar. No debe ser nulo.
     * @return boolean - true si el cultivo fue agregado exitosamente, false en caso contrario
     * 
     * @throws IllegalArgumentException si el objeto cultivo es nulo
     * 
     * Precondiciones:
     * - El objeto cultivo debe ser válido y contener todos los datos requeridos
     * - No debe existir un cultivo con el mismo ID (si es auto-incremental, se genera automáticamente)
     * 
     * Postcondiciones:
     * - Si es exitoso: el cultivo queda persistido en la base de datos
     * - Si falla: se retorna false y el sistema permanece sin cambios
     */
    public boolean agregarCultivo(Cultivo cultivo) {
        return cultivoDAO.insertar(cultivo);
    }

    /**
     * Actualiza la información de un cultivo existente en el sistema.
     * Modifica los datos de un cultivo previamente registrado.
     * 
     * @param cultivo Objeto Cultivo con los datos actualizados. Debe contener un ID válido.
     * @return boolean - true si la actualización fue exitosa, false en caso contrario
     * 
     * @throws IllegalArgumentException si el objeto cultivo es nulo o no tiene ID válido
     * 
     * Precondiciones:
     * - El cultivo debe existir en la base de datos (ID válido)
     * - El objeto cultivo debe contener los datos actualizados válidos
     * 
     * Postcondiciones:
     * - Si es exitoso: los datos del cultivo son actualizados en la base de datos
     * - Si falla: se retorna false y los datos permanecen sin cambios
     */
    public boolean actualizarCultivo(Cultivo cultivo) {
        return cultivoDAO.actualizar(cultivo);
    }

    /**
     * Elimina un cultivo del sistema mediante su identificador.
     * Remueve permanentemente el registro del cultivo de la base de datos.
     * 
     * @param idCultivo Identificador único del cultivo a eliminar. Debe ser mayor a 0.
     * @return boolean - true si la eliminación fue exitosa, false en caso contrario
     * 
     * @throws IllegalArgumentException si el idCultivo no es válido (<= 0)
     * 
     * Precondiciones:
     * - El cultivo con el ID especificado debe existir en la base de datos
     * - No debe haber dependencias referenciales que impidan la eliminación
     * 
     * Postcondiciones:
     * - Si es exitoso: el cultivo es removido permanentemente de la base de datos
     * - Si falla: se retorna false y el cultivo permanece en el sistema
     * 
     * Nota: Esta operación es irreversible y debe usarse con precaución.
     */
    public boolean eliminarCultivo(int idCultivo) {
        return cultivoDAO.eliminar(idCultivo);
    }

    /**
     * Obtiene un cultivo específico mediante su identificador único.
     * Recupera toda la información de un cultivo particular desde la base de datos.
     * 
     * @param idCultivo Identificador único del cultivo a consultar. Debe ser mayor a 0.
     * @return Cultivo - objeto Cultivo con todos sus datos, o null si no se encuentra
     * 
     * @throws IllegalArgumentException si el idCultivo no es válido (<= 0)
     * 
     * Precondiciones:
     * - El ID debe corresponder a un cultivo existente en la base de datos
     * 
     * Postcondiciones:
     * - Retorna el objeto Cultivo completo si existe
     * - Retorna null si no se encuentra ningún cultivo con ese ID
     */
    public Cultivo obtenerCultivo(int idCultivo) {
        return cultivoDAO.obtenerPorId(idCultivo);
    }

    /**
     * Obtiene una lista de todos los cultivos registrados en el sistema.
     * Recupera todos los registros de cultivos sin filtros aplicados.
     * 
     * @return List<Cultivo> - lista de todos los objetos Cultivo en el sistema
     * 
     * Postcondiciones:
     * - Retorna una lista de objetos Cultivo (puede estar vacía si no hay registros)
     * - La lista no es nula, si no hay registros retorna una lista vacía
     * - Los objetos Cultivo en la lista contienen todos sus atributos poblados
     * 
     * Nota: Para sistemas con muchos registros, considerar implementar paginación
     */
    public List<Cultivo> obtenerTodosCultivos() {
        return cultivoDAO.obtenerTodos();
    }

    /**
     * Busca cultivos que coincidan con un criterio específico.
     * Realiza una búsqueda flexible en los atributos de los cultivos.
     * 
     * @param criterio Texto de búsqueda para filtrar cultivos. No debe ser nulo.
     * @return List<Cultivo> - lista de cultivos que coinciden con el criterio
     * 
     * @throws IllegalArgumentException si el criterio es nulo
     * 
     * Comportamiento de búsqueda:
     * - La búsqueda es case-insensitive (no distingue mayúsculas/minúsculas)
     * - Busca en múltiples campos del cultivo (nombre, tipo, descripción, etc.)
     * - Retorna resultados parciales (like %criterio%)
     * 
     * Precondiciones:
     * - El criterio debe ser una cadena válida (puede estar vacía)
     * 
     * Postcondiciones:
     * - Retorna una lista de cultivos que coinciden con el criterio
     * - Si no hay coincidencias, retorna una lista vacía
     * - Si el criterio está vacío, podría retornar todos los cultivos o ninguno (depende de la implementación del DAO)
     */
    public List<Cultivo> buscarCultivos(String criterio) {
        return cultivoDAO.buscar(criterio);
    }

    /**
     * Verifica la existencia de un predio en el sistema.
     * Comprueba si un predio específico está registrado, útil para validar relaciones.
     * 
     * @param idPredio Identificador único del predio a verificar. Debe ser mayor a 0.
     * @return boolean - true si el predio existe, false en caso contrario
     * 
     * @throws IllegalArgumentException si el idPredio no es válido (<= 0)
     * 
     * Uso típico:
     * - Validar integridad referencial antes de operaciones
     * - Verificar existencia para relaciones cultivo-predio
     * - Validaciones en la capa de negocio
     * 
     * Precondiciones:
     * - El ID debe ser un identificador válido de predio
     * 
     * Postcondiciones:
     * - Retorna true si existe un predio con el ID especificado
     * - Retorna false si no existe el predio
     */
    public boolean existePredio(int idPredio) {
        return cultivoDAO.existePredio(idPredio);
    }
}