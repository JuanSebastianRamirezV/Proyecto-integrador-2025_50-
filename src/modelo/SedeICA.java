package modelo;

public class SedeICA {
    private int idSede;
    private String correoElectronico;
    private String telefono;

    public SedeICA() {}

    public SedeICA(int idSede, String correoElectronico, String telefono) {
        this.idSede = idSede;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getIdSede() { return idSede; }
    public void setIdSede(int idSede) { this.idSede = idSede; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return "Sede " + idSede;
    }
}