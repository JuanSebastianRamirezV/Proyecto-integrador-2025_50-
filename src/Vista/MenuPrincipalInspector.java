package vista;

import controlador.SesionUsuario;
import javax.swing.*;
import java.awt.*;

public class MenuPrincipalInspector extends JFrame {
    // Módulos que el inspector puede usar
    private JButton btnInspecciones;
    private JButton btnLugaresProduccion;
    private JButton btnPredios;
    private JButton btnRelacionesAsociados; // NUEVO BOTÓN
    
    // Botones de control del sistema
    private JButton btnSalir;
    private JButton btnCerrarSesion;
    
    // Componentes de etiqueta para información
    private JLabel lblTitulo;
    private JLabel lblUsuario;

    public MenuPrincipalInspector() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Gestión - Modo Inspector");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(240, 240, 240));

        JPanel panelHeader = crearPanelHeader();
        JPanel panelBotones = crearPanelBotones();
        JPanel panelInferior = crearPanelInferior();

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);
        agregarActionListeners();
    }

    private JPanel crearPanelHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(0, 102, 204));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        lblTitulo = new JLabel("SISTEMA DE GESTIÓN - MODO INSPECTOR");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        lblUsuario = new JLabel("Usuario: inspector");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setForeground(Color.WHITE);

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(lblUsuario, BorderLayout.EAST);

        return panelHeader;
    }

    private JPanel crearPanelBotones() {
        // Grid 2x2 para los 4 módulos del inspector
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelBotones.setBackground(new Color(240, 240, 240));

        btnInspecciones = crearBotonMenu("Gestión de Inspecciones", new Color(230, 126, 34));
        btnLugaresProduccion = crearBotonMenu("Lugares de Producción", new Color(155, 89, 182));
        btnPredios = crearBotonMenu("Gestión de Predios", new Color(41, 128, 185));
        btnRelacionesAsociados = crearBotonMenu("Relaciones de Asociados", new Color(46, 204, 113)); // NUEVO BOTÓN

        // Agregar los 4 botones en el grid 2x2
        panelBotones.add(btnInspecciones);
        panelBotones.add(btnLugaresProduccion);
        panelBotones.add(btnPredios);
        panelBotones.add(btnRelacionesAsociados);

        return panelBotones;
    }

    private JPanel crearPanelInferior() {
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelInferior.setBackground(new Color(240, 240, 240));
        
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnSalir = new JButton("Salir del Sistema");
        
        btnCerrarSesion.setBackground(new Color(52, 152, 219));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setPreferredSize(new Dimension(150, 35));
        
        btnSalir.setBackground(new Color(192, 57, 43));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalir.setPreferredSize(new Dimension(150, 35));

        panelInferior.add(btnCerrarSesion);
        panelInferior.add(btnSalir);

        return panelInferior;
    }

    private JButton crearBotonMenu(String texto, Color color) {
        JButton boton = new JButton("<html><center>" + texto.replace(" ", "<br>") + "</center></html>");
        
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }

    /**
     * Actualiza la etiqueta del usuario actual en la interfaz
     * @param usuario Nombre del usuario que ha iniciado sesión
     */
    public void setUsuarioActual(String usuario) {
        lblUsuario.setText("Usuario: " + usuario);
    }

    private void agregarActionListeners() {
        btnInspecciones.addActionListener(e -> abrirGestionInspecciones());
        btnLugaresProduccion.addActionListener(e -> abrirGestionLugaresProduccion());
        btnPredios.addActionListener(e -> abrirGestionPredios());
        btnRelacionesAsociados.addActionListener(e -> abrirGestionRelacionesAsociados()); // NUEVO LISTENER
        
        btnCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cerrarSesion();
            }
        });
        
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea salir del sistema?",
                "Salir del Sistema",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void abrirGestionInspecciones() {
        // Usar la clase GestionInspecciones estándar que ya incluye todas las funcionalidades
        new vista.GestionInspecciones().setVisible(true);
    }

    private void abrirGestionLugaresProduccion() {
        // Versión estándar de gestión de lugares de producción
        new vista.GestionLugaresProduccion().setVisible(true);
    }

    private void abrirGestionPredios() {
        // Versión estándar de gestión de predios
        new vista.GestionPredios().setVisible(true);
    }

    // NUEVO MÉTODO: Abrir gestión de relaciones de asociados
    private void abrirGestionRelacionesAsociados() {
        new vista.GestionRelacionesAsociados().setVisible(true);
    }

    // MÉTODO CORREGIDO: Cerrar sesión correctamente
    private void cerrarSesion() {
        // Cerrar sesión en el sistema
        SesionUsuario.getInstance().cerrarSesion();
        
        // Cerrar esta ventana actual
        this.dispose();
        
        // Abrir ventana de login
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}