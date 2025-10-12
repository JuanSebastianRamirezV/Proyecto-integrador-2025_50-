package modelo;

/**
 * Representa un cultivo en el sistema con toda su información relacionada.
 * Esta clase modela los datos de un cultivo incluyendo su estado, incidencias
 * y relación con un predio específico.
 */
public class Cultivo {
    private int idCultivo;
    private String nombreCultivo;
    private int plantasAfectadas;
    private String estadoPlanta;
    private int totalPlantas;
    private String nivelIncidencia;
    private int idPredio;

    // Constructores

    /**
     * Constructor por defecto que crea una instancia de Cultivo sin inicializar sus atributos.
     * Útil para casos donde los valores se establecerán posteriormente mediante setters.
     */
    public Cultivo() {}

    /**
     * Constructor completo que inicializa todos los atributos del cultivo.
     * 
     * @param idCultivo Identificador único del cultivo
     * @param nombreCultivo Nombre o tipo del cultivo
     * @param plantasAfectadas Número de plantas que presentan afectaciones
     * @param estadoPlanta Estado general de salud de las plantas
     * @param totalPlantas Número total de plantas en el cultivo
     * @param nivelIncidencia Nivel de incidencia de plagas o enfermedades
     * @param idPredio Identificador del predio al que pertenece el cultivo
     */
    public Cultivo(int idCultivo, String nombreCultivo, int plantasAfectadas, 
                   String estadoPlanta, int totalPlantas, String nivelIncidencia, int idPredio) {
        this.idCultivo = idCultivo;
        this.nombreCultivo = nombreCultivo;
        this.plantasAfectadas = plantasAfectadas;
        this.estadoPlanta = estadoPlanta;
        this.totalPlantas = totalPlantas;
        this.nivelIncidencia = nivelIncidencia;
        this.idPredio = idPredio;
    }

    // Getters y Setters

    /**
     * Obtiene el identificador único del cultivo.
     * 
     * @return El ID del cultivo
     */
    public int getIdCultivo() { return idCultivo; }
    
    /**
     * Establece el identificador único del cultivo.
     * 
     * @param idCultivo El ID a asignar al cultivo
     */
    public void setIdCultivo(int idCultivo) { this.idCultivo = idCultivo; }

    /**
     * Obtiene el nombre o tipo del cultivo.
     * 
     * @return El nombre del cultivo
     */
    public String getNombreCultivo() { return nombreCultivo; }
    
    /**
     * Establece el nombre o tipo del cultivo.
     * 
     * @param nombreCultivo El nombre a asignar al cultivo
     */
    public void setNombreCultivo(String nombreCultivo) { this.nombreCultivo = nombreCultivo; }

    /**
     * Obtiene el número de plantas afectadas por plagas o enfermedades.
     * 
     * @return Número de plantas afectadas
     */
    public int getPlantasAfectadas() { return plantasAfectadas; }
    
    /**
     * Establece el número de plantas afectadas.
     * 
     * @param plantasAfectadas Número de plantas afectadas a registrar
     */
    public void setPlantasAfectadas(int plantasAfectadas) { this.plantasAfectadas = plantasAfectadas; }

    /**
     * Obtiene el estado general de salud de las plantas.
     * 
     * @return Estado actual de las plantas
     */
    public String getEstadoPlanta() { return estadoPlanta; }
    
    /**
     * Establece el estado general de salud de las plantas.
     * 
     * @param estadoPlanta Estado a asignar a las plantas
     */
    public void setEstadoPlanta(String estadoPlanta) { this.estadoPlanta = estadoPlanta; }

    /**
     * Obtiene el número total de plantas en el cultivo.
     * 
     * @return Total de plantas en el cultivo
     */
    public int getTotalPlantas() { return totalPlantas; }
    
    /**
     * Establece el número total de plantas en el cultivo.
     * 
     * @param totalPlantas Total de plantas a registrar
     */
    public void setTotalPlantas(int totalPlantas) { this.totalPlantas = totalPlantas; }

    /**
     * Obtiene el nivel de incidencia de plagas o enfermedades.
     * 
     * @return Nivel de incidencia actual
     */
    public String getNivelIncidencia() { return nivelIncidencia; }
    
    /**
     * Establece el nivel de incidencia de plagas o enfermedades.
     * 
     * @param nivelIncidencia Nivel de incidencia a registrar
     */
    public void setNivelIncidencia(String nivelIncidencia) { this.nivelIncidencia = nivelIncidencia; }

    /**
     * Obtiene el identificador del predio al que pertenece el cultivo.
     * 
     * @return ID del predio asociado
     */
    public int getIdPredio() { return idPredio; }
    
    /**
     * Establece el identificador del predio al que pertenece el cultivo.
     * 
     * @param idPredio ID del predio a asociar
     */
    public void setIdPredio(int idPredio) { this.idPredio = idPredio; }

    /**
     * Representación en formato String de todos los atributos del cultivo.
     * Útil para logging y debugging.
     * 
     * @return String con la información completa del cultivo
     */
    @Override
    public String toString() {
        return "Cultivo{" +
                "idCultivo=" + idCultivo +
                ", nombreCultivo='" + nombreCultivo + '\'' +
                ", plantasAfectadas=" + plantasAfectadas +
                ", estadoPlanta='" + estadoPlanta + '\'' +
                ", totalPlantas=" + totalPlantas +
                ", nivelIncidencia='" + nivelIncidencia + '\'' +
                ", idPredio=" + idPredio +
                '}';
    }
}