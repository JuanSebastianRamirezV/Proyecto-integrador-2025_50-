package controlador;

import vista.Login;
import vista.MenuPrincipal;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador principal de la aplicación
 * Gestiona el flujo entre login y menú principal
 */
public class MainController {
    private static MainController instance;
    private Login loginView;
    private MenuPrincipal menuPrincipalView;
    
    /**
     * Patrón Singleton para una única instancia del controlador
     */
    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }
    
    /**
     * Constructor privado - inicializa el controlador
     */
    private MainController() {
        initController();
    }
    
    /**
     * Inicializa el controlador mostrando el login
     */
    private void initController() {
        mostrarLogin();
    }
    
    /**
     * Muestra la ventana de login
     */
    public void mostrarLogin() {
        if (loginView == null) {
            loginView = new Login();
            
            // Configurar listeners del login
            loginView.setLoginListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    procesarLogin(loginView.getUsuario(), loginView.getPassword());
                }
            });
            
            loginView.setSalirListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    salirAplicacion();
                }
            });
        }
        
        loginView.setVisible(true);
        if (menuPrincipalView != null) {
            menuPrincipalView.dispose();
            menuPrincipalView = null;
        }
    }
    
    /**
     * Procesa el intento de login
     */
    public void procesarLogin(String usuario, String password) {
        if (autenticarUsuario(usuario, password)) {
            // Login exitoso
            loginView.dispose();
            mostrarMenuPrincipal(usuario);
        } else {
            loginView.mostrarError("Usuario o contraseña incorrectos");
            loginView.limpiarCampos();
        }
    }
    
    /**
     * Autentica las credenciales del usuario
     */
    private boolean autenticarUsuario(String usuario, String password) {
        // Lógica de autenticación - modifica según tus necesidades
        return "admin".equals(usuario) && "1234".equals(password);
    }
    
    /**
     * Muestra el menú principal después del login exitoso
     */
    public void mostrarMenuPrincipal(String usuario) {
        if (menuPrincipalView == null) {
            menuPrincipalView = new MenuPrincipal();
        }
        menuPrincipalView.setVisible(true);
    }
    
    /**
     * Cierra la sesión y vuelve al login
     */
    public void cerrarSesion() {
        if (menuPrincipalView != null) {
            menuPrincipalView.dispose();
            menuPrincipalView = null;
        }
        mostrarLogin();
    }
    
    /**
     * Sale de la aplicación con confirmación
     */
    public void salirAplicacion() {
        int confirmacion = JOptionPane.showConfirmDialog(null,
            "¿Está seguro que desea salir de la aplicación?",
            "Confirmar salida",
            JOptionPane.YES_NO_OPTION);
            
        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    /**
     * Método principal - inicia la aplicación
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainController.getInstance();
            }
        });
    }
}