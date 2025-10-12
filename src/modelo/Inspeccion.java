package modelo;

import java.util.Date;

/**
 * Representa una inspección realizada a un cultivo por un inspector.
 * Contiene la información básica de la inspección incluyendo fecha, estado, observaciones
 * y las referencias al inspector y cultivo asociados.
 */
public class Inspeccion {
    private int idInspeccion;
    private Date fechaInspeccion;
    private String estado;
    private String observaciones;
    private int idInspector;
    private int idCultivo;

    /**
     * Constructor por defecto que inicializa una inspección sin parámetros.
     */
    public Inspeccion() {}

    /**
     * Constructor completo que inicializa todos los atributos de la inspección.
     * 
     * @param idInspeccion Identificador único de la inspección
     * @param fechaInspeccion Fecha en que se realizó la inspección
     * @param estado Estado actual de la inspección
     * @param observaciones Observaciones y comentarios de la inspección
     * @param idInspector Identificador del inspector que realizó la inspección
     * @param idCultivo Identificador del cultivo inspeccionado
     */
    public Inspeccion(int idInspeccion, Date fechaInspeccion, String estado, 
                     String observaciones, int idInspector, int idCultivo) {
        this.idInspeccion = idInspeccion;
        this.fechaInspeccion = fechaInspeccion;
        this.estado = estado;
        this.observaciones = observaciones;
        this.idInspector = idInspector;
        this.idCultivo = idCultivo;
    }

    // Getters y Setters
    public int getIdInspeccion() { return idInspeccion; }
    public void setIdInspeccion(int idInspeccion) { this.idInspeccion = idInspeccion; }

    public Date getFechaInspeccion() { return fechaInspeccion; }
    public void setFechaInspeccion(Date fechaInspeccion) { this.fechaInspeccion = fechaInspeccion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public int getIdInspector() { return idInspector; }
    public void setIdInspector(int idInspector) { this.idInspector = idInspector; }

    public int getIdCultivo() { return idCultivo; }
    public void setIdCultivo(int idCultivo) { this.idCultivo = idCultivo; }

    /**
     * Representación en texto de la inspección.
     * 
     * @return Cadena con el formato "Inspección [id] - [fecha]"
     */
    @Override
    public String toString() {
        return "Inspección " + idInspeccion + " - " + fechaInspeccion;
    }
}