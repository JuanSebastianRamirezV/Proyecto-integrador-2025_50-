
package controlador;

import javax.swing.*;

/**
 * MainController — versión robusta y compatible con cambios en la clase Login.
 * Añadidos métodos públicos para cerrar sesión (estático y de instancia)
 * de forma que MenuPrincipal y MenuPrincipalProductor puedan usarlos sin error.
 */
public class MainController {

    private static MainController instance;

    private MainController() {
        // Constructor privado para singleton si lo necesitas
        // NOTA: no arrancamos UI aquí para permitir control externo en tests si es necesario
    }

    public static synchronized MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    /**
     * Inicializa y muestra la UI (login).
     */
    public void initUI() {
        // Intentamos crear la clase de login de forma segura
        JFrame loginFrame = createLoginFrameSafely();
        if (loginFrame != null) {
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null,
                    "No se pudo encontrar ninguna clase de Login válida.\n" +
                            "Busqué: vista.Login, Login_mejorado, vista.Login_mejorado",
                    "Error al iniciar", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * Método que intenta crear una instancia de la ventana de login.
     */
    private JFrame createLoginFrameSafely() {
        // Intento directo: la clase esperada
        try {
            Class<?> clazz = Class.forName("vista.Login");
            Object obj = clazz.getDeclaredConstructor().newInstance();
            if (obj instanceof JFrame) return (JFrame) obj;
        } catch (ClassNotFoundException cnfe) {
            // ignorar y probar siguientes opciones
        } catch (Exception ex) {
            showWarning("Error al instanciar vista.Login: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        }

        // Opción fallback 1: Login_mejorado en el paquete por defecto
        try {
            Class<?> clazz = Class.forName("Login_mejorado");
            Object obj = clazz.getDeclaredConstructor().newInstance();
            if (obj instanceof JFrame) return (JFrame) obj;
        } catch (ClassNotFoundException cnfe) {
            // seguir
        } catch (Exception ex) {
            showWarning("Error al instanciar Login_mejorado: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        }

        // Opción fallback 2: vista.Login_mejorado
        try {
            Class<?> clazz = Class.forName("vista.Login_mejorado");
            Object obj = clazz.getDeclaredConstructor().newInstance();
            if (obj instanceof JFrame) return (JFrame) obj;
        } catch (ClassNotFoundException cnfe) {
            // ninguna encontrada
        } catch (Exception ex) {
            showWarning("Error al instanciar vista.Login_mejorado: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        }

        // Si llegamos aquí: no se pudo instanciar ninguna clase de login
        return null;
    }

    private void showWarning(String msg) {
        System.err.println(msg);
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, msg, "Aviso", JOptionPane.WARNING_MESSAGE));
    }

    /**
     * ----------- Métodos nuevos para cerrar sesión -------------
     *
     * Uso recomendado desde otras clases:
     *
     *  - Llamada estática (si antes usabas MainController.cerrarSesion()):
     *       MainController.cerrarSesion();
     *
     *  - Llamada de instancia, pasando la ventana actual (para que se cierre):
     *       MainController.getInstance().cerrarSesion(miFrame);
     *
     * Ambos métodos abren la ventana de login de forma segura.
     */

    /**
     * Método estático conveniencia para cerrar sesión desde cualquier clase.
     * Cierra la sesión y muestra el login. No requiere instancia previa.
     */
    public static void cerrarSesion() {
        // Asegurarse de que exista una instancia para manejar el flujo
        MainController ctrl = getInstance();
        ctrl.cerrarSesion(null);
    }

    /**
     * Cierra la ventana origen (si se pasa) y vuelve a mostrar el login.
     * @param origen ventana que solicita cerrar sesión (puede ser null)
     */
    public void cerrarSesion(JFrame origen) {
        SwingUtilities.invokeLater(() -> {
            // Cerrar la ventana que solicitó el cierre si se dio
            try {
                if (origen != null) {
                    origen.dispose();
                }
            } catch (Exception ex) {
                // no fatal; continuar para mostrar login
                System.err.println("Error cerrando la ventana de origen: " + ex.getMessage());
            }

            // Mostrar el login (usando la misma lógica segura)
            JFrame loginFrame = createLoginFrameSafely();
            if (loginFrame != null) {
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se encontró una clase de Login para volver a iniciar sesión.",
                        "Cerrar sesión", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Helpers para abrir menús si decides que el controlador sea quien abra las ventanas.
     */
    public void abrirMenuPrincipal() {
        tryOpenAndShow("vista.MenuPrincipal");
    }

    public void abrirMenuPrincipalProductor() {
        tryOpenAndShow("vista.MenuPrincipalProductor");
    }

    private void tryOpenAndShow(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object obj = clazz.getDeclaredConstructor().newInstance();
            if (obj instanceof JFrame) {
                JFrame frame = (JFrame) obj;
                SwingUtilities.invokeLater(() -> {
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                });
            } else {
                showWarning("La clase " + className + " no es un JFrame.");
            }
        } catch (ClassNotFoundException e) {
            showWarning("Clase no encontrada: " + className);
        } catch (Exception e) {
            showWarning("Error al abrir " + className + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    /**
     * main — arranque de la aplicación en el hilo de eventos de Swing
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Iniciamos la UI a través de la instancia
            MainController.getInstance().initUI();
        });
    }
}
