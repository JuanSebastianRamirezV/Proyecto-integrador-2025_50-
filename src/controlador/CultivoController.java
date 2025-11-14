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
import modelo.Predio;
import modelo.PredioDAO;
import java.util.List;

public class CultivoController {
    
    /**
     * Instancia del Data Access Object (DAO) para operaciones de persistencia
     * de cultivos. Proporciona los métodos para interactuar con la base de datos.
     */
    private CultivoDAO cultivoDAO;
    private PredioDAO predioDAO;

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
        this.predioDAO = new PredioDAO();
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
     * Remueve permanentemente el registro del cultivo de la base de datos,
     * incluyendo todas las dependencias relacionadas mediante eliminación en cascada.
     * 
     * @param idCultivo Identificador único del cultivo a eliminar. Debe ser mayor a 0.
     * @return boolean - true si la eliminación fue exitosa, false en caso contrario
     * 
     * @throws IllegalArgumentException si el idCultivo no es válido (<= 0)
     * 
     * Precondiciones:
     * - El cultivo con el ID especificado debe existir en la base de datos
     * - Las dependencias referenciales se manejan automáticamente mediante cascada
     * 
     * Postcondiciones:
     * - Si es exitoso: el cultivo y todas sus dependencias son removidas permanentemente
     * - Si falla: se retorna false y el sistema permanece sin cambios
     * 
     * Comportamiento de eliminación en cascada:
     * - Se eliminan automáticamente todos los registros relacionados en PLAGA_CULTIVO
     * - Se mantiene la integridad referencial de la base de datos
     * - La operación es atómica (todo o nada) mediante transacciones
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

    /**
     * Obtiene todos los cultivos asociados a un predio específico.
     * Recupera la lista de cultivos que pertenecen a un predio particular.
     * 
     * @param idPredio Identificador único del predio. Debe ser mayor a 0.
     * @return List<Cultivo> - lista de cultivos del predio especificado
     * 
     * @throws IllegalArgumentException si el idPredio no es válido (<= 0)
     * 
     * Precondiciones:
     * - El predio debe existir en el sistema
     * 
     * Postcondiciones:
     * - Retorna una lista de cultivos asociados al predio
     * - Si el predio no tiene cultivos, retorna lista vacía
     * - Si el predio no existe, el comportamiento depende de la implementación del DAO
     */
    public List<Cultivo> obtenerCultivosPorPredio(int idPredio) {
        return cultivoDAO.obtenerPorPredio(idPredio);
    }

    /**
     * Verifica si ya existe un cultivo con el mismo nombre en el mismo predio.
     * Útil para prevenir duplicados y mantener la integridad de los datos.
     * 
     * @param nombreCultivo Nombre del cultivo a verificar. No debe ser nulo ni vacío.
     * @param idPredio Identificador del predio. Debe ser mayor a 0.
     * @return boolean - true si ya existe un cultivo con ese nombre en el predio, false en caso contrario
     * 
     * @throws IllegalArgumentException si el nombreCultivo es nulo/vacío o idPredio no es válido
     * 
     * Precondiciones:
     * - El nombre del cultivo debe ser una cadena válida
     * - El ID del predio debe ser válido
     * 
     * Postcondiciones:
     * - Retorna true si existe un cultivo con el mismo nombre en el mismo predio
     * - Retorna false si el nombre está disponible para ese predio
     */
    public boolean existeNombreCultivo(String nombreCultivo, int idPredio) {
        return cultivoDAO.existeNombreCultivo(nombreCultivo, idPredio);
    }

    /**
     * Verifica si ya existe un cultivo con el mismo nombre en el mismo predio, excluyendo un ID específico.
     * Útil para validaciones durante la actualización de cultivos existentes.
     * 
     * @param nombreCultivo Nombre del cultivo a verificar. No debe ser nulo ni vacío.
     * @param idPredio Identificador del predio. Debe ser mayor a 0.
     * @param idCultivoExcluir ID del cultivo a excluir de la verificación (normalmente el que se está actualizando).
     * @return boolean - true si ya existe otro cultivo con ese nombre en el predio, false en caso contrario
     * 
     * @throws IllegalArgumentException si el nombreCultivo es nulo/vacío o idPredio no es válido
     * 
     * Precondiciones:
     * - El nombre del cultivo debe ser una cadena válida
     * - El ID del predio debe ser válido
     * - El ID a excluir debe ser mayor a 0
     * 
     * Postcondiciones:
     * - Retorna true si existe otro cultivo (diferente al excluido) con el mismo nombre en el mismo predio
     * - Retorna false si el nombre está disponible para ese predio
     */
    public boolean existeNombreCultivoExcluyendoId(String nombreCultivo, int idPredio, int idCultivoExcluir) {
        return cultivoDAO.existeNombreCultivoExcluyendoId(nombreCultivo, idPredio, idCultivoExcluir);
    }

    /**
     * Calcula el total de plantas afectadas para todos los cultivos de un predio específico.
     * Proporciona un resumen estadístico del impacto en un predio particular.
     * 
     * @param idPredio Identificador único del predio. Debe ser mayor a 0.
     * @return int - suma total de plantas afectadas en el predio
     * 
     * @throws IllegalArgumentException si el idPredio no es válido (<= 0)
     * 
     * Precondiciones:
     * - El predio debe existir en el sistema
     * 
     * Postcondiciones:
     * - Retorna la suma de plantas afectadas de todos los cultivos del predio
     * - Retorna 0 si el predio no tiene cultivos o no existen plantas afectadas
     */
    public int contarPlantasAfectadasPorPredio(int idPredio) {
        return cultivoDAO.contarPlantasAfectadasPorPredio(idPredio);
    }

    /**
     * Obtiene todos los predios para mostrar en el ComboBox de cultivos.
     * Recupera todos los predios registrados en el sistema para su selección.
     * 
     * @return List<Predio> - lista de todos los objetos Predio en el sistema
     * 
     * Postcondiciones:
     * - Retorna una lista de objetos Predio (puede estar vacía si no hay registros)
     * - La lista no es nula, si no hay registros retorna una lista vacía
     * - Los objetos Predio en la lista contienen todos sus atributos poblados
     */
    public List<Predio> obtenerTodosPrediosParaCultivo() {
        return predioDAO.obtenerTodos();
    }

    /**
     * Obtiene un predio específico mediante su identificador único.
     * Recupera toda la información de un predio particular desde la base de datos.
     * 
     * @param idPredio Identificador único del predio a consultar. Debe ser mayor a 0.
     * @return Predio - objeto Predio con todos sus datos, o null si no se encuentra
     * 
     * @throws IllegalArgumentException si el idPredio no es válido (<= 0)
     * 
     * Precondiciones:
     * - El ID debe corresponder a un predio existente en la base de datos
     * 
     * Postcondiciones:
     * - Retorna el objeto Predio completo si existe
     * - Retorna null si no se encuentra ningún predio con ese ID
     */
    public Predio obtenerPredio(int idPredio) {
        return predioDAO.obtenerPorId(idPredio);
    }

    /**
     * Obtiene un predio por su nombre.
     * Busca un predio específico utilizando su nombre como criterio.
     * 
     * @param nombrePredio Nombre del predio a buscar. No debe ser nulo ni vacío.
     * @return Predio - objeto Predio con todos sus datos, o null si no se encuentra
     * 
     * @throws IllegalArgumentException si el nombrePredio es nulo o vacío
     * 
     * Precondiciones:
     * - El nombre debe corresponder a un predio existente en la base de datos
     * 
     * Postcondiciones:
     * - Retorna el objeto Predio completo si existe
     * - Retorna null si no se encuentra ningún predio con ese nombre
     */
    public Predio obtenerPredioPorNombre(String nombrePredio) {
        return predioDAO.obtenerPorNombre(nombrePredio);
    }
}