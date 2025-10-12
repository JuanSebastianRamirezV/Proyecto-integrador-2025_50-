package modelo;

/**
 * Clase que representa un Municipio con su identificador único y nombre.
 * Proporciona constructores, métodos de acceso y sobrescribe los métodos
 * toString, equals y hashCode basándose en el idMunicipio.
 */
public class Municipio {
    /**
     * Identificador único del municipio
     */
    private int idMunicipio;
    
    /**
     * Nombre del municipio
     */
    private String nombre;

    // Constructores
    
    /**
     * Constructor por defecto que crea una instancia de Municipio
     * con valores predeterminados
     */
    public Municipio() {}

    /**
     * Constructor que crea una instancia de Municipio con los valores especificados
     * 
     * @param idMunicipio Identificador único del municipio
     * @param nombre Nombre del municipio
     */
    public Municipio(int idMunicipio, String nombre) {
        this.idMunicipio = idMunicipio;
        this.nombre = nombre;
    }

    // Getters y Setters
    
    /**
     * Obtiene el identificador único del municipio
     * 
     * @return El id del municipio
     */
    public int getIdMunicipio() { return idMunicipio; }
    
    /**
     * Establece el identificador único del municipio
     * 
     * @param idMunicipio El nuevo id del municipio
     */
    public void setIdMunicipio(int idMunicipio) { this.idMunicipio = idMunicipio; }

    /**
     * Obtiene el nombre del municipio
     * 
     * @return El nombre del municipio
     */
    public String getNombre() { return nombre; }
    
    /**
     * Establece el nombre del municipio
     * 
     * @param nombre El nuevo nombre del municipio
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Devuelve una representación en formato String del objeto Municipio
     * 
     * @return String que representa el municipio con su id y nombre
     */
    @Override
    public String toString() {
        return "Municipio{" +
                "idMunicipio=" + idMunicipio +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    /**
     * Compara este municipio con otro objeto para determinar si son iguales
     * Dos municipios se consideran iguales si tienen el mismo idMunicipio
     * 
     * @param obj Objeto a comparar con este municipio
     * @return true si son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Municipio municipio = (Municipio) obj;
        return idMunicipio == municipio.idMunicipio;
    }

    /**
     * Devuelve el valor hash para este municipio, basado en el idMunicipio
     * 
     * @return El código hash del municipio
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(idMunicipio);
    }
}