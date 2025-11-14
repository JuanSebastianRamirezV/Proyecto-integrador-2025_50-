package controlador;

import modelo.Municipio;
import modelo.MunicipioDAO;
import java.util.List;

public class MunicipioController {
    private MunicipioDAO municipioDAO;
    
    public MunicipioController() {
        this.municipioDAO = new MunicipioDAO();
    }
    
    /**
     * Agrega un nuevo municipio usando la secuencia SEQ_MUNICIPIO
     * RETORNA EL ID GENERADO en lugar de boolean
     */
    public int agregarMunicipio(Municipio municipio) {
        try {
            System.out.println("🎯 Controller: Iniciando inserción de municipio - " + municipio.getNombre());
            
            // Validar que el nombre no esté vacío
            if (municipio.getNombre() == null || municipio.getNombre().trim().isEmpty()) {
                System.out.println("❌ Controller: Nombre de municipio vacío");
                return -1;
            }
            
            // Verificar si ya existe un municipio con ese nombre
            if (municipioDAO.existeNombreMunicipio(municipio.getNombre().trim())) {
                System.out.println("❌ Controller: Ya existe municipio con nombre: " + municipio.getNombre());
                return -1;
            }
            
            // Insertar usando el DAO (que ahora retorna el ID generado)
            int idGenerado = municipioDAO.insertar(municipio);
            
            if (idGenerado > 0) {
                System.out.println("✅ Controller: Municipio insertado exitosamente - ID: " + idGenerado);
                return idGenerado;
            } else {
                System.out.println("❌ Controller: No se pudo insertar el municipio");
                return -1;
            }
            
        } catch (Exception e) {
            System.out.println("💥 Controller: Error inesperado al agregar municipio: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Método alternativo que mantiene compatibilidad con boolean (para la vista actual)
     */
    public boolean agregarMunicipioBoolean(Municipio municipio) {
        int idGenerado = agregarMunicipio(municipio);
        return idGenerado > 0;
    }
    
    public boolean actualizarMunicipio(Municipio municipio) {
        try {
            System.out.println("🎯 Controller: Actualizando municipio ID: " + municipio.getIdMunicipio());
            
            // Validaciones
            if (municipio.getIdMunicipio() <= 0) {
                System.out.println("❌ Controller: ID de municipio inválido");
                return false;
            }
            
            if (municipio.getNombre() == null || municipio.getNombre().trim().isEmpty()) {
                System.out.println("❌ Controller: Nombre de municipio vacío");
                return false;
            }
            
            // Verificar que el municipio existe
            Municipio existente = municipioDAO.obtenerPorId(municipio.getIdMunicipio());
            if (existente == null) {
                System.out.println("❌ Controller: Municipio no existe - ID: " + municipio.getIdMunicipio());
                return false;
            }
            
            boolean resultado = municipioDAO.actualizar(municipio);
            
            if (resultado) {
                System.out.println("✅ Controller: Municipio actualizado exitosamente");
            } else {
                System.out.println("❌ Controller: No se pudo actualizar el municipio");
            }
            
            return resultado;
            
        } catch (Exception e) {
            System.out.println("💥 Controller: Error inesperado al actualizar municipio: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarMunicipio(int idMunicipio) {
        try {
            System.out.println("🎯 Controller: Eliminando municipio ID: " + idMunicipio);
            
            if (idMunicipio <= 0) {
                System.out.println("❌ Controller: ID de municipio inválido");
                return false;
            }
            
            // Verificar que el municipio existe
            Municipio existente = municipioDAO.obtenerPorId(idMunicipio);
            if (existente == null) {
                System.out.println("❌ Controller: Municipio no existe - ID: " + idMunicipio);
                return false;
            }
            
            boolean resultado = municipioDAO.eliminar(idMunicipio);
            
            if (resultado) {
                System.out.println("✅ Controller: Municipio eliminado exitosamente");
            } else {
                System.out.println("❌ Controller: No se pudo eliminar el municipio");
            }
            
            return resultado;
            
        } catch (Exception e) {
            System.out.println("💥 Controller: Error inesperado al eliminar municipio: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public Municipio obtenerMunicipioPorId(int idMunicipio) {
        try {
            return municipioDAO.obtenerPorId(idMunicipio);
        } catch (Exception e) {
            System.out.println("💥 Controller: Error al obtener municipio por ID: " + e.getMessage());
            return null;
        }
    }
    
    public List<Municipio> obtenerTodosMunicipios() {
        try {
            System.out.println("🎯 Controller: Obteniendo todos los municipios");
            List<Municipio> municipios = municipioDAO.obtenerTodos();
            System.out.println("✅ Controller: Se obtuvieron " + municipios.size() + " municipios");
            return municipios;
        } catch (Exception e) {
            System.out.println("💥 Controller: Error al obtener todos los municipios: " + e.getMessage());
            return List.of(); // Retorna lista vacía en caso de error
        }
    }
    
    public List<Municipio> buscarMunicipios(String criterio) {
        try {
            System.out.println("🎯 Controller: Buscando municipios - criterio: " + criterio);
            
            if (criterio == null || criterio.trim().isEmpty()) {
                return obtenerTodosMunicipios();
            }
            
            List<Municipio> resultados = municipioDAO.buscar(criterio.trim());
            System.out.println("✅ Controller: Búsqueda encontró " + resultados.size() + " resultados");
            return resultados;
            
        } catch (Exception e) {
            System.out.println("💥 Controller: Error al buscar municipios: " + e.getMessage());
            return List.of();
        }
    }
    
    public boolean existeMunicipio(String nombre) {
        try {
            return municipioDAO.existeNombreMunicipio(nombre);
        } catch (Exception e) {
            System.out.println("💥 Controller: Error al verificar existencia de municipio: " + e.getMessage());
            return false;
        }
    }
    
    public int contarMunicipios() {
        try {
            return municipioDAO.contarMunicipios();
        } catch (Exception e) {
            System.out.println("💥 Controller: Error al contar municipios: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Método de diagnóstico para verificar conexión
     */
    public boolean verificarConexion() {
        try {
            return municipioDAO.verificarConexionYPermisos();
        } catch (Exception e) {
            System.out.println("💥 Controller: Error en verificación de conexión: " + e.getMessage());
            return false;
        }
    }
    public Municipio obtenerMunicipio(int idMunicipio) {
        return municipioDAO.obtenerPorId(idMunicipio);
    }
}