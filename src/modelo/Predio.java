
/**
 * Clase que representa un predio o propiedad en el sistema.
 * Contiene información básica sobre la propiedad, su identificación y relación con municipios.
 
 */
package modelo;

public class Predio {
    /**
     * Identificador único del predio en el sistema
     */
    private int idPredio;
    
    /**
     * Nombre descriptivo del predio o propiedad
     */
    private String nombrePredio;
    
    /**
     * Nombre completo del propietario del predio
     */
    private String nombrePropietario;
    
    /**
     * Identificador del municipio al que pertenece el predio
     */
    private int idMunicipio;

    // Constructores
    
    /**
     * Constructor por defecto que crea una instancia de Predio sin inicializar sus atributos
     */
    public Predio() {}

    /**
     * Constructor completo que inicializa todos los atributos del predio
     * 
     * @param idPredio Identificador único del predio
     * @param nombrePredio Nombre descriptivo del predio
     * @param nombrePropietario Nombre completo del propietario
     * @param idMunicipio Identificador del municipio asociado
     */
    public Predio(int idPredio, String nombrePredio, String nombrePropietario, int idMunicipio) {
        this.idPredio = idPredio;
        this.nombrePredio = nombrePredio;
        this.nombrePropietario = nombrePropietario;
        this.idMunicipio = idMunicipio;
    }

    // Getters y Setters
    
    /**
     * Obtiene el identificador único del predio
     * 
     * @return El ID del predio como entero
     */
    public int getIdPredio() { return idPredio; }
    
    /**
     * Establece el identificador único del predio
     * 
     * @param idPredio El nuevo ID del predio
     */
    public void setIdPredio(int idPredio) { this.idPredio = idPredio; }

    /**
     * Obtiene el nombre descriptivo del predio
     * 
     * @return El nombre del predio como String
     */
    public String getNombrePredio() { return nombrePredio; }
    
    /**
     * Establece el nombre descriptivo del predio
     * 
     * @param nombrePredio El nuevo nombre del predio
     */
    public void setNombrePredio(String nombrePredio) { this.nombrePredio = nombrePredio; }

    /**
     * Obtiene el nombre completo del propietario
     * 
     * @return El nombre del propietario como String
     */
    public String getNombrePropietario() { return nombrePropietario; }
    
    /**
     * Establece el nombre completo del propietario
     * 
     * @param nombrePropietario El nuevo nombre del propietario
     */
    public void setNombrePropietario(String nombrePropietario) { this.nombrePropietario = nombrePropietario; }

    /**
     * Obtiene el identificador del municipio asociado
     * 
     * @return El ID del municipio como entero
     */
    public int getIdMunicipio() { return idMunicipio; }
    
    /**
     * Establece el identificador del municipio asociado
     * 
     * @param idMunicipio El nuevo ID del municipio
     */
    public void setIdMunicipio(int idMunicipio) { this.idMunicipio = idMunicipio; }

    /**
     * Representación en formato String de los atributos del predio
     * 
     * @return String con la información completa del predio en formato legible
     */
    @Override
    public String toString() {
        return "Predio{" +
                "idPredio=" + idPredio +
                ", nombrePredio='" + nombrePredio + '\'' +
                ", nombrePropietario='" + nombrePropietario + '\'' +
                ", idMunicipio=" + idMunicipio +
                '}';
    }
}