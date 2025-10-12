package modelo;

public class Productor {
    private int idProductor;
    private String nombreCompleto;
    private String tipoIdentificacion;
    private String rol;

    public Productor() {}

    public Productor(int idProductor, String nombreCompleto, String tipoIdentificacion, String rol) {
        this.idProductor = idProductor;
        this.nombreCompleto = nombreCompleto;
        this.tipoIdentificacion = tipoIdentificacion;
        this.rol = rol;
    }

    // Getters y Setters
    public int getIdProductor() { return idProductor; }
    public void setIdProductor(int idProductor) { this.idProductor = idProductor; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return nombreCompleto;
    }
}