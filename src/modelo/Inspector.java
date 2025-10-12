package modelo;

/**
 * Representa a un inspector del sistema con su información personal y profesional.
 * Almacena los datos de identificación, contacto y afiliación a sede.
 */
public class Inspector {
    private int idInspector;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombresCompletos;
    private String telefono;
    private String numeroTarjetaProfesional;
    private int idSede;

    /**
     * Constructor por defecto que inicializa un inspector sin parámetros.
     */
    public Inspector() {}

    /**
     * Constructor completo que inicializa todos los atributos del inspector.
     * 
     * @param idInspector Identificador único del inspector
     * @param tipoDocumento Tipo de documento de identificación
     * @param numeroDocumento Número del documento de identificación
     * @param nombresCompletos Nombres y apellidos del inspector
     * @param telefono Número de teléfono de contacto
     * @param numeroTarjetaProfesional Número de tarjeta profesional
     * @param idSede Identificador de la sede a la que pertenece el inspector
     */
    public Inspector(int idInspector, String tipoDocumento, String numeroDocumento, String nombresCompletos, String telefono, String numeroTarjetaProfesional, int idSede) {
        this.idInspector = idInspector;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombresCompletos = nombresCompletos;
        this.telefono = telefono;
        this.numeroTarjetaProfesional = numeroTarjetaProfesional;
        this.idSede = idSede;
    }

    // Getters y Setters
    public int getIdInspector() { return idInspector; }
    public void setIdInspector(int idInspector) { this.idInspector = idInspector; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getNombresCompletos() { return nombresCompletos; }
    public void setNombresCompletos(String nombresCompletos) { this.nombresCompletos = nombresCompletos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getNumeroTarjetaProfesional() { return numeroTarjetaProfesional; }
    public void setNumeroTarjetaProfesional(String numeroTarjetaProfesional) { this.numeroTarjetaProfesional = numeroTarjetaProfesional; }

    public int getIdSede() { return idSede; }
    public void setIdSede(int idSede) { this.idSede = idSede; }

    /**
     * Representación en texto del inspector mostrando solo los nombres completos.
     * 
     * @return Nombres completos del inspector
     */
    @Override
    public String toString() {
        return nombresCompletos;
    }
}