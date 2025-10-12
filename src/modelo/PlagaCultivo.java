package modelo;

public class PlagaCultivo {
    private int idPlaga;
    private int idCultivo;
    private String nivelAfectacion;
    private String recomendaciones;

    public PlagaCultivo() {}

    public PlagaCultivo(int idPlaga, int idCultivo, String nivelAfectacion, String recomendaciones) {
        this.idPlaga = idPlaga;
        this.idCultivo = idCultivo;
        this.nivelAfectacion = nivelAfectacion;
        this.recomendaciones = recomendaciones;
    }

    // Getters y Setters
    public int getIdPlaga() { return idPlaga; }
    public void setIdPlaga(int idPlaga) { this.idPlaga = idPlaga; }

    public int getIdCultivo() { return idCultivo; }
    public void setIdCultivo(int idCultivo) { this.idCultivo = idCultivo; }

    public String getNivelAfectacion() { return nivelAfectacion; }
    public void setNivelAfectacion(String nivelAfectacion) { this.nivelAfectacion = nivelAfectacion; }

    public String getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(String recomendaciones) { this.recomendaciones = recomendaciones; }
}
