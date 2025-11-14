package modelo;

/**
 * Representa una relación de asociación entre plagas, cultivos, inspecciones y lugares de producción.
 * Esta clase modela los datos de la tabla RELACION_ASOCIADOS que conecta múltiples entidades del sistema.
 */
public class RelacionAsociados {
    private int idPlaga;
    private int idCultivo;
    private int idInspeccion; // ✅ CAMBIADO: Ya no es Integer, es int (NOT NULL)
    private int idLugarProduccion; // ✅ CAMBIADO: Ya no es Integer, es int (NOT NULL)
    private int totalPlantas;
    private String nivelIncidencia;

    // Información relacionada para el informe
    private String nombrePlaga;
    private String nombreCultivo;
    private String nombreLugarProduccion;
    private String fechaInspeccion;

    // Constructores
    public RelacionAsociados() {}

    // ✅ CONSTRUCTOR ACTUALIZADO: Todos los IDs son obligatorios
    public RelacionAsociados(int idPlaga, int idCultivo, int idInspeccion, 
                           int idLugarProduccion, int totalPlantas, String nivelIncidencia) {
        this.idPlaga = idPlaga;
        this.idCultivo = idCultivo;
        this.idInspeccion = idInspeccion;
        this.idLugarProduccion = idLugarProduccion;
        this.totalPlantas = totalPlantas;
        this.nivelIncidencia = nivelIncidencia;
    }

    // Getters y Setters
    public int getIdPlaga() { return idPlaga; }
    public void setIdPlaga(int idPlaga) { this.idPlaga = idPlaga; }

    public int getIdCultivo() { return idCultivo; }
    public void setIdCultivo(int idCultivo) { this.idCultivo = idCultivo; }

    // ✅ CAMBIADO: int en lugar de Integer
    public int getIdInspeccion() { return idInspeccion; }
    public void setIdInspeccion(int idInspeccion) { this.idInspeccion = idInspeccion; }

    // ✅ CAMBIADO: int en lugar de Integer
    public int getIdLugarProduccion() { return idLugarProduccion; }
    public void setIdLugarProduccion(int idLugarProduccion) { this.idLugarProduccion = idLugarProduccion; }

    public int getTotalPlantas() { return totalPlantas; }
    public void setTotalPlantas(int totalPlantas) { this.totalPlantas = totalPlantas; }

    public String getNivelIncidencia() { return nivelIncidencia; }
    public void setNivelIncidencia(String nivelIncidencia) { this.nivelIncidencia = nivelIncidencia; }

    // Getters y Setters para información relacionada
    public String getNombrePlaga() { return nombrePlaga; }
    public void setNombrePlaga(String nombrePlaga) { this.nombrePlaga = nombrePlaga; }

    public String getNombreCultivo() { return nombreCultivo; }
    public void setNombreCultivo(String nombreCultivo) { this.nombreCultivo = nombreCultivo; }

    public String getNombreLugarProduccion() { return nombreLugarProduccion; }
    public void setNombreLugarProduccion(String nombreLugarProduccion) { this.nombreLugarProduccion = nombreLugarProduccion; }

    public String getFechaInspeccion() { return fechaInspeccion; }
    public void setFechaInspeccion(String fechaInspeccion) { this.fechaInspeccion = fechaInspeccion; }

    @Override
    public String toString() {
        return "RelacionAsociados{" +
                "idPlaga=" + idPlaga +
                ", idCultivo=" + idCultivo +
                ", idInspeccion=" + idInspeccion +
                ", idLugarProduccion=" + idLugarProduccion +
                ", totalPlantas=" + totalPlantas +
                ", nivelIncidencia='" + nivelIncidencia + '\'' +
                '}';
    }
}