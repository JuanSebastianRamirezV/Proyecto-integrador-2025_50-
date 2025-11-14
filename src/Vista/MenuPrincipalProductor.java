package vista;

import controlador.SesionUsuario;
import javax.swing.*;
import java.awt.*;

public class MenuPrincipalProductor extends JFrame {
    // Módulos que el productor puede usar
    private JButton btnPredios;
    private JButton btnLugaresProduccion;
    private JButton btnCultivos;
    private JButton btnConsultarInspecciones;
    private JButton btnProximasInspecciones;
    private JButton btnInformeAsociaciones; // NUEVO: Botón para informe general
    
    // Botones de control del sistema
    private JButton btnSalir;
    private JButton btnCerrarSesion;
    
    // Componentes de etiqueta para información
    private JLabel lblTitulo;
    private JLabel lblUsuario;

    public MenuPrincipalProductor() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Gestión - Modo Productor");
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

        lblTitulo = new JLabel("SISTEMA DE GESTIÓN - MODO PRODUCTOR");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        lblUsuario = new JLabel("Usuario: productor");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setForeground(Color.WHITE);

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(lblUsuario, BorderLayout.EAST);

        return panelHeader;
    }

    private JPanel crearPanelBotones() {
        // Grid 3x2 para los 6 módulos del productor (3 filas x 2 columnas)
        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelBotones.setBackground(new Color(240, 240, 240));

        btnPredios = crearBotonMenu("Gestión de Predios", new Color(41, 128, 185));
        btnLugaresProduccion = crearBotonMenu("Lugares de Producción", new Color(155, 89, 182));
        btnCultivos = crearBotonMenu("Gestión de Cultivos", new Color(46, 204, 113));
        btnConsultarInspecciones = crearBotonMenu("Consultar Inspecciones", new Color(230, 126, 34));
        btnProximasInspecciones = crearBotonMenu("Próximas Inspecciones", new Color(39, 174, 96));
        btnInformeAsociaciones = crearBotonMenu("Informe General", new Color(52, 152, 219)); // NUEVO

        // Agregar botones en orden
        panelBotones.add(btnPredios);
        panelBotones.add(btnLugaresProduccion);
        panelBotones.add(btnCultivos);
        panelBotones.add(btnConsultarInspecciones);
        panelBotones.add(btnProximasInspecciones);
        panelBotones.add(btnInformeAsociaciones); // NUEVO

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
        btnPredios.addActionListener(e -> abrirGestionPredios());
        btnLugaresProduccion.addActionListener(e -> abrirGestionLugaresProduccion());
        btnCultivos.addActionListener(e -> abrirGestionCultivos());
        btnConsultarInspecciones.addActionListener(e -> abrirConsultarInspecciones());
        btnProximasInspecciones.addActionListener(e -> abrirProximasInspecciones());
        btnInformeAsociaciones.addActionListener(e -> abrirInformeAsociaciones()); // NUEVO
        
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

    private void abrirGestionPredios() {
        try {
            new vista.GestionPrediosProductor().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al abrir Gestión de Predios: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirGestionLugaresProduccion() {
        try {
            new vista.GestionLugaresProduccionProductor().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al abrir Lugares de Producción: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirGestionCultivos() {
        try {
            new vista.GestionCultivosProductor().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al abrir Gestión de Cultivos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirConsultarInspecciones() {
        try {
            new vista.ConsultaInspeccionesProductor().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al abrir Consultar Inspecciones: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirProximasInspecciones() {
        try {
            new vista.ProximasInspeccionesProductor().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al abrir Próximas Inspecciones: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // NUEVO: Método para abrir el informe general de asociaciones
    private void abrirInformeAsociaciones() {
        try {
            new vista.InformeAsociaciones().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al abrir Informe General: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método main para pruebas independientes
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipalProductor menu = new MenuPrincipalProductor();
            menu.setUsuarioActual("productor_test");
            menu.setVisible(true);
        });
    }
}