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
    private int totalPlantas; // ✅ NUEVO CAMPO
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
     * @param totalPlantas Número total de plantas en el cultivo ✅ NUEVO PARÁMETRO
     * @param idPredio Identificador del predio al que pertenece el cultivo
     */
    public Cultivo(int idCultivo, String nombreCultivo, int plantasAfectadas, 
                   String estadoPlanta, int totalPlantas, int idPredio) {
        this.idCultivo = idCultivo;
        this.nombreCultivo = nombreCultivo;
        this.plantasAfectadas = plantasAfectadas;
        this.estadoPlanta = estadoPlanta;
        this.totalPlantas = totalPlantas; // ✅ INICIALIZAR NUEVO CAMPO
        this.idPredio = idPredio;
    }

    // Getters y Setters

    public int getIdCultivo() { return idCultivo; }
    public void setIdCultivo(int idCultivo) { this.idCultivo = idCultivo; }

    public String getNombreCultivo() { return nombreCultivo; }
    public void setNombreCultivo(String nombreCultivo) { this.nombreCultivo = nombreCultivo; }

    public int getPlantasAfectadas() { return plantasAfectadas; }
    public void setPlantasAfectadas(int plantasAfectadas) { this.plantasAfectadas = plantasAfectadas; }

    public String getEstadoPlanta() { return estadoPlanta; }
    public void setEstadoPlanta(String estadoPlanta) { this.estadoPlanta = estadoPlanta; }

    public int getTotalPlantas() { return totalPlantas; } // ✅ NUEVO GETTER
    public void setTotalPlantas(int totalPlantas) { this.totalPlantas = totalPlantas; } // ✅ NUEVO SETTER

    public int getIdPredio() { return idPredio; }
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
                ", totalPlantas=" + totalPlantas + // ✅ INCLUIR EN TOSTRING
                ", idPredio=" + idPredio +
                '}';
    }
}