package modelo;

/**
 * Representa un lugar de producción donde se cultiva un producto específico.
 * Contiene información del registro ICA, ubicación y relaciones con productor y cultivo.
 */
public class LugarProduccion {
    private int idLugarProduccion;
    private String nombreLugar;
    private String numeroRegistroICA;
    private String direccionLugar;
    private int idProductor;
    private int idCultivo;

    /**
     * Constructor por defecto para crear un lugar de producción sin parámetros.
     */
    public LugarProduccion() {}

    /**
     * Constructor completo para inicializar todos los atributos del lugar de producción.
     * 
     * @param idLugarProduccion Identificador único del lugar de producción
     * @param nombreLugar Nombre descriptivo del lugar
     * @param numeroRegistroICA Número de registro ICA del lugar
     * @param direccionLugar Dirección física del lugar de producción
     * @param idProductor Identificador del productor dueño del lugar
     * @param idCultivo Identificador del cultivo que se produce en el lugar
     */
    public LugarProduccion(int idLugarProduccion, String nombreLugar, String numeroRegistroICA, String direccionLugar, int idProductor, int idCultivo) {
        this.idLugarProduccion = idLugarProduccion;
        this.nombreLugar = nombreLugar;
        this.numeroRegistroICA = numeroRegistroICA;
        this.direccionLugar = direccionLugar;
        this.idProductor = idProductor;
        this.idCultivo = idCultivo;
    }

    // Getters y Setters
    public int getIdLugarProduccion() { return idLugarProduccion; }
    public void setIdLugarProduccion(int idLugarProduccion) { this.idLugarProduccion = idLugarProduccion; }

    public String getNombreLugar() { return nombreLugar; }
    public void setNombreLugar(String nombreLugar) { this.nombreLugar = nombreLugar; }

    public String getNumeroRegistroICA() { return numeroRegistroICA; }
    public void setNumeroRegistroICA(String numeroRegistroICA) { this.numeroRegistroICA = numeroRegistroICA; }

    public String getDireccionLugar() { return direccionLugar; }
    public void setDireccionLugar(String direccionLugar) { this.direccionLugar = direccionLugar; }

    public int getIdProductor() { return idProductor; }
    public void setIdProductor(int idProductor) { this.idProductor = idProductor; }

    public int getIdCultivo() { return idCultivo; }
    public void setIdCultivo(int idCultivo) { this.idCultivo = idCultivo; }

    /**
     * Representación en texto del lugar de producción mostrando solo el nombre.
     * 
     * @return Nombre del lugar de producción
     */
    @Override
    public String toString() {
        return nombreLugar;
    }
}