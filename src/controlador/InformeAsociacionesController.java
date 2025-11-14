package controlador;

import modelo.RelacionAsociados;
import modelo.RelacionAsociadosDAO;
import java.util.List;

/**
 * Controlador para la gestión de informes de relaciones de asociación.
 * Proporciona métodos para generar y gestionar informes que pueden ser 
 * utilizados tanto por productores como inspectores.
 */
public class InformeAsociacionesController {
    
    private RelacionAsociadosDAO relacionDAO;

    public InformeAsociacionesController() {
        this.relacionDAO = new RelacionAsociadosDAO();
    }

    /**
     * Obtiene el informe completo de todas las relaciones de asociación.
     */
    public List<RelacionAsociados> obtenerInformeCompleto() {
        return relacionDAO.obtenerInformeCompleto();
    }

    /**
     * Obtiene estadísticas generales para mostrar en el dashboard.
     */
    public String obtenerEstadisticasGenerales() {
        return relacionDAO.obtenerEstadisticasGenerales();
    }

    /**
     * Busca relaciones por criterio de búsqueda.
     */
    public List<RelacionAsociados> buscarRelaciones(String criterio) {
        return relacionDAO.buscar(criterio);
    }

    /**
     * Genera un resumen ejecutivo del informe.
     */
    public String generarResumenEjecutivo() {
        List<RelacionAsociados> relaciones = relacionDAO.obtenerInformeCompleto();
        
        if (relaciones.isEmpty()) {
            return "No hay datos suficientes para generar el resumen ejecutivo.";
        }

        int totalAsociaciones = relaciones.size();
        int totalPlantas = relaciones.stream().mapToInt(RelacionAsociados::getTotalPlantas).sum();
        
        long altaIncidencia = relaciones.stream()
                .filter(r -> "Alta".equalsIgnoreCase(r.getNivelIncidencia()))
                .count();
        
        long mediaIncidencia = relaciones.stream()
                .filter(r -> "Media".equalsIgnoreCase(r.getNivelIncidencia()))
                .count();
        
        long bajaIncidencia = relaciones.stream()
                .filter(r -> "Baja".equalsIgnoreCase(r.getNivelIncidencia()))
                .count();

        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN EJECUTIVO DE ASOCIACIONES ===\n\n");
        resumen.append("Total de asociaciones registradas: ").append(totalAsociaciones).append("\n");
        resumen.append("Total de plantas monitoreadas: ").append(totalPlantas).append("\n");
        resumen.append("\nDistribución por nivel de incidencia:\n");
        resumen.append("- Alta: ").append(altaIncidencia).append(" (").append((altaIncidencia * 100 / totalAsociaciones)).append("%)\n");
        resumen.append("- Media: ").append(mediaIncidencia).append(" (").append((mediaIncidencia * 100 / totalAsociaciones)).append("%)\n");
        resumen.append("- Baja: ").append(bajaIncidencia).append(" (").append((bajaIncidencia * 100 / totalAsociaciones)).append("%)\n");

        return resumen.toString();
    }
    /**
 * Método de diagnóstico para verificar el estado de los datos
 */
public String diagnosticarProblemas() {
    StringBuilder diagnostico = new StringBuilder();
    diagnostico.append("=== DIAGNÓSTICO DEL INFORME ===\n\n");
    
    List<RelacionAsociados> relaciones = relacionDAO.obtenerInformeCompleto();
    diagnostico.append("Total de relaciones encontradas: ").append(relaciones.size()).append("\n\n");
    
    if (relaciones.isEmpty()) {
        diagnostico.append("PROBLEMAS IDENTIFICADOS:\n");
        diagnostico.append("1. No hay registros en la tabla RELACION_ASOCIADOS\n");
        diagnostico.append("2. O no existen los datos relacionados (plagas, cultivos, etc.)\n");
        diagnostico.append("3. Verifica que todas las tablas tengan datos\n");
    } else {
        diagnostico.append("DATOS ENCONTRADOS:\n");
        for (int i = 0; i < Math.min(relaciones.size(), 5); i++) {
            RelacionAsociados rel = relaciones.get(i);
            diagnostico.append("- ").append(rel.getNombrePlaga())
                      .append(" | ").append(rel.getNombreCultivo())
                      .append(" | ").append(rel.getNivelIncidencia())
                      .append("\n");
        }
    }
    
    return diagnostico.toString();
}
}