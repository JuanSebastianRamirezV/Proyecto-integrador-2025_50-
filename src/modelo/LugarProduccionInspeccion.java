package modelo;

public class LugarProduccionInspeccion {
    private int idLugarProduccion;
    private int idInspeccion;

    public LugarProduccionInspeccion() {}

    public LugarProduccionInspeccion(int idLugarProduccion, int idInspeccion) {
        this.idLugarProduccion = idLugarProduccion;
        this.idInspeccion = idInspeccion;
    }

    // Getters y Setters
    public int getIdLugarProduccion() { return idLugarProduccion; }
    public void setIdLugarProduccion(int idLugarProduccion) { this.idLugarProduccion = idLugarProduccion; }

    public int getIdInspeccion() { return idInspeccion; }
    public void setIdInspeccion(int idInspeccion) { this.idInspeccion = idInspeccion; }
}
