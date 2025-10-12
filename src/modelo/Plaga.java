package modelo;

/**
 * Clase que representa una Plaga con su identificador único, nombre, tipo y cultivos asociados.
 * Proporciona constructores, métodos de acceso y sobrescribe los métodos
 * toString, equals y hashCode basándose en el idPlaga.
 * 
 * @author TuNombre
 * @version 1.0
 */
public class Plaga {
    /**
     * Identificador único de la plaga
     */
    private int idPlaga;
    
    /**
     * Nombre de la plaga
     */
    private String nombrePlaga;
    
    /**
     * Tipo o categoría de la plaga
     */
    private String tipoPlaga;
    
    /**
     * Cultivos que pueden ser afectados por esta plaga
     */
    private String cultivosAsociados;

    // Constructores
    
    /**
     * Constructor por defecto que crea una instancia de Plaga
     * con valores predeterminados
     */
    public Plaga() {}

    /**
     * Constructor que crea una instancia de Plaga con los valores especificados
     * 
     * @param idPlaga Identificador único de la plaga
     * @param nombrePlaga Nombre de la plaga
     * @param tipoPlaga Tipo o categoría de la plaga
     * @param cultivosAsociados Cultivos que pueden ser afectados por esta plaga
     */
    public Plaga(int idPlaga, String nombrePlaga, String tipoPlaga, String cultivosAsociados) {
        this.idPlaga = idPlaga;
        this.nombrePlaga = nombrePlaga;
        this.tipoPlaga = tipoPlaga;
        this.cultivosAsociados = cultivosAsociados;
    }

    // Getters y Setters
    
    /**
     * Obtiene el identificador único de la plaga
     * 
     * @return El id de la plaga
     */
    public int getIdPlaga() { return idPlaga; }
    
    /**
     * Establece el identificador único de la plaga
     * 
     * @param idPlaga El nuevo id de la plaga
     */
    public void setIdPlaga(int idPlaga) { this.idPlaga = idPlaga; }

    /**
     * Obtiene el nombre de la plaga
     * 
     * @return El nombre de la plaga
     */
    public String getNombrePlaga() { return nombrePlaga; }
    
    /**
     * Establece el nombre de la plaga
     * 
     * @param nombrePlaga El nuevo nombre de la plaga
     */
    public void setNombrePlaga(String nombrePlaga) { this.nombrePlaga = nombrePlaga; }

    /**
     * Obtiene el tipo o categoría de la plaga
     * 
     * @return El tipo de plaga
     */
    public String getTipoPlaga() { return tipoPlaga; }
    
    /**
     * Establece el tipo o categoría de la plaga
     * 
     * @param tipoPlaga El nuevo tipo de plaga
     */
    public void setTipoPlaga(String tipoPlaga) { this.tipoPlaga = tipoPlaga; }

    /**
     * Obtiene los cultivos asociados a la plaga
     * 
     * @return Los cultivos que pueden ser afectados por esta plaga
     */
    public String getCultivosAsociados() { return cultivosAsociados; }
    
    /**
     * Establece los cultivos asociados a la plaga
     * 
     * @param cultivosAsociados Los nuevos cultivos asociados
     */
    public void setCultivosAsociados(String cultivosAsociados) { this.cultivosAsociados = cultivosAsociados; }

    /**
     * Devuelve una representación en formato String del objeto Plaga
     * 
     * @return String que representa la plaga con todos sus atributos
     */
    @Override
    public String toString() {
        return "Plaga{" +
                "idPlaga=" + idPlaga +
                ", nombrePlaga='" + nombrePlaga + '\'' +
                ", tipoPlaga='" + tipoPlaga + '\'' +
                ", cultivosAsociados='" + cultivosAsociados + '\'' +
                '}';
    }

    /**
     * Compara esta plaga con otro objeto para determinar si son iguales
     * Dos plagas se consideran iguales si tienen el mismo idPlaga
     * 
     * @param obj Objeto a comparar con esta plaga
     * @return true si son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Plaga plaga = (Plaga) obj;
        return idPlaga == plaga.idPlaga;
    }

    /**
     * Devuelve el valor hash para esta plaga, basado en el idPlaga
     * 
     * @return El código hash de la plaga
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(idPlaga);
    }
}