package controlador;

public class SesionUsuario {
    private static SesionUsuario instance;
    private String usuario;
    private String tipoUsuario; // "PRODUCTOR", "INSPECTOR", "ADMIN"
    private boolean autenticado;
    
    private SesionUsuario() {
        this.autenticado = false;
    }
    
    public static synchronized SesionUsuario getInstance() {
        if (instance == null) {
            instance = new SesionUsuario();
        }
        return instance;
    }
    
    public void iniciarSesion(String usuario, String tipoUsuario) {
        this.usuario = usuario;
        this.tipoUsuario = tipoUsuario;
        this.autenticado = true;
        System.out.println("Sesión iniciada: " + usuario + " (" + tipoUsuario + ")");
    }
    
    public void cerrarSesion() {
        this.usuario = null;
        this.tipoUsuario = null;
        this.autenticado = false;
        System.out.println("Sesión cerrada");
    }
    
    public String getUsuario() { return usuario; }
    public String getTipoUsuario() { return tipoUsuario; }
    public boolean isAutenticado() { return autenticado; }
}