package vista;

import controlador.SesionUsuario;
import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {
    // Componentes de botones para los módulos del sistema
    private JButton btnPredios;              // Botón para gestión de predios
    private JButton btnMunicipios;           // Botón para gestión de municipios
    private JButton btnPlagas;               // Botón para gestión de plagas
    private JButton btnLugaresProduccion;    // Botón para lugares de producción
    private JButton btnProductores;          // Botón para gestión de productores
    private JButton btnSedesICA;             // Botón para gestión de sedes ICA
    private JButton btnInspectores;          // Botón para gestión de inspectores
    private JButton btnCultivos;             // Botón para gestión de cultivos
    private JButton btnInspecciones;         // Botón para registro de inspecciones
    
    // Botones de control del sistema
    private JButton btnSalir;                // Botón para salir completamente del sistema
    private JButton btnCerrarSesion;         // Botón para cerrar sesión actual
    
    // Componentes de etiqueta para información
    private JLabel lblTitulo;                // Etiqueta del título del sistema
    private JLabel lblUsuario;               // Etiqueta que muestra el usuario actual

    /**
     * Constructor de la clase MenuPrincipal.
     * Inicializa la interfaz gráfica y configura todos los componentes
     * del menú principal del sistema.
     * 
     * Flujo de ejecución:
     * 1. Llama a initComponents() para inicializar la interfaz
     * 2. Configura la ventana principal y sus componentes
     * 3. Establece los action listeners para la interactividad
     */
    public MenuPrincipal() {
        initComponents();
    }

    /**
     * Inicializa todos los componentes de la interfaz gráfica.
     * Configura la ventana principal, paneles, botones y disposición
     * de los elementos en la pantalla.
     * 
     * Estructura de la interfaz:
     * - Header superior con título y información de usuario
     * - Panel central con botones organizados en grid 3x3
     * - Panel inferior con botones de control del sistema
     * 
     * Diseño visual:
     * - Colores corporativos azul para el header
     * - Botones con colores distintivos para cada módulo
     * - Efectos hover para mejorar la interactividad
     */
    private void initComponents() {
        // Configuración básica de la ventana principal
        setTitle("Sistema de Gestión de Predios y Plagas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Cierra la aplicación completamente
        setSize(900, 700);                               // Tamaño optimizado para 9 botones
        setLocationRelativeTo(null);                     // Centra la ventana en la pantalla

        // Panel principal que contiene todos los elementos
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(240, 240, 240));  // Fondo gris claro

        // Header de la aplicación
        JPanel panelHeader = crearPanelHeader();
        
        // Panel central con botones de módulos organizados en grid 3x3
        JPanel panelBotones = crearPanelBotones();
        
        // Panel inferior con botones de control del sistema
        JPanel panelInferior = crearPanelInferior();

        // Ensamblaje de todos los paneles en el panel principal
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // Agregar panel principal a la ventana y configurar listeners
        add(panelPrincipal);
        agregarActionListeners();
    }

    /**
     * Crea y configura el panel header de la aplicación.
     * Contiene el título del sistema y la información del usuario actual.
     * 
     * @return JPanel configurado como header de la aplicación
     * 
     * Características:
     * - Fondo azul corporativo (#0066CC)
     * - Título en fuente grande y negrita
     * - Información del usuario alineada a la derecha
     * - Márgenes internos para mejor espaciado
     */
    private JPanel crearPanelHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(0, 102, 204));  // Azul corporativo
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Configuración del título principal del sistema
        lblTitulo = new JLabel("SISTEMA DE GESTIÓN DE PREDIOS Y PLAGAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        // Configuración de la etiqueta de usuario (debería ser dinámica en producción)
        lblUsuario = new JLabel("Usuario: Admin");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setForeground(Color.WHITE);

        // Agregar componentes al header
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(lblUsuario, BorderLayout.EAST);

        return panelHeader;
    }

    /**
     * Crea el panel central con los botones de acceso a los módulos del sistema.
     * Organiza los botones en una cuadrícula 3x3 con espaciado uniforme.
     * 
     * @return JPanel configurado con GridLayout conteniendo los 9 botones de módulos
     * 
     * Organización de botones:
     * Fila 1: Predios, Municipios, Plagas
     * Fila 2: Lugares Producción, Productores, Sedes ICA  
     * Fila 3: Inspectores, Cultivos, Inspecciones
     */
    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new GridLayout(3, 3, 20, 20));  // Grid 3x3 con espaciado
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelBotones.setBackground(new Color(240, 240, 240));

        // Creación de botones para cada módulo del sistema con colores distintivos
        btnPredios = crearBotonMenu("Gestión de Predios", new Color(41, 128, 185));        // Azul
        btnMunicipios = crearBotonMenu("Gestión de Municipios", new Color(39, 174, 96));   // Verde
        btnPlagas = crearBotonMenu("Gestión de Plagas", new Color(231, 76, 60));          // Rojo
        btnLugaresProduccion = crearBotonMenu("Lugares de Producción", new Color(155, 89, 182)); // Púrpura
        btnProductores = crearBotonMenu("Gestión de Productores", new Color(243, 156, 18));     // Naranja
        btnSedesICA = crearBotonMenu("Sedes ICA", new Color(22, 160, 133));               // Verde ICA
        btnInspectores = crearBotonMenu("Gestión de Inspectores", new Color(142, 68, 173));    // Púrpura oscuro
        btnCultivos = crearBotonMenu("Gestión de Cultivos", new Color(39, 174, 96));      // Verde
        btnInspecciones = crearBotonMenu("Registro de Inspecciones", new Color(230, 126, 34)); // Naranja oscuro

        // Agregar botones al panel en orden de grid
        panelBotones.add(btnPredios);
        panelBotones.add(btnMunicipios);
        panelBotones.add(btnPlagas);
        panelBotones.add(btnLugaresProduccion);
        panelBotones.add(btnProductores);
        panelBotones.add(btnSedesICA);
        panelBotones.add(btnInspectores);
        panelBotones.add(btnCultivos);
        panelBotones.add(btnInspecciones);

        return panelBotones;
    }

    /**
     * Crea el panel inferior con botones de control del sistema.
     * Contiene botones para cerrar sesión y salir completamente del sistema.
     * 
     * @return JPanel configurado con FlowLayout conteniendo botones de control
     */
    private JPanel crearPanelInferior() {
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelInferior.setBackground(new Color(240, 240, 240));
        
        // Creación de botones de control
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnSalir = new JButton("Salir del Sistema");
        
        // Estilizado del botón Cerrar Sesión
        btnCerrarSesion.setBackground(new Color(52, 152, 219));  // Azul
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setPreferredSize(new Dimension(150, 35));
        
        // Estilizado del botón Salir del Sistema
        btnSalir.setBackground(new Color(192, 57, 43));  // Rojo
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalir.setPreferredSize(new Dimension(150, 35));

        // Agregar botones al panel inferior
        panelInferior.add(btnCerrarSesion);
        panelInferior.add(btnSalir);

        return panelInferior;
    }

    /**
     * Método de utilidad para crear botones de menú con estilo consistente.
     * 
     * @param texto Texto que se mostrará en el botón (se formatea con HTML para multilínea)
     * @param color Color de fondo del botón que identifica visualmente el módulo
     * @return JButton configurado con el estilo uniforme de la aplicación
     * 
     * Características del botón:
     * - Texto multilínea centrado usando HTML
     * - Color de fondo específico para cada módulo
     * - Texto en blanco para contraste
     * - Fuente Segoe UI en negrita
     * - Sin borde de foco
     * - Cursor de mano al pasar el mouse
     * - Efecto hover que oscurece el color al pasar el mouse
     * - Márgenes internos para mejor apariencia
     */
    private JButton crearBotonMenu(String texto, Color color) {
        // Formatear texto con HTML para permitir múltiples líneas
        JButton boton = new JButton("<html><center>" + texto.replace(" ", "<br>") + "</center></html>");
        
        // Configuración de estilo básico
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);  // Elimina el borde de foco
        boton.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));  // Márgenes internos
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Cursor de mano para indicar interactividad
        
        // Agregar efecto hover para mejorar la experiencia de usuario
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            /**
             * Se ejecuta cuando el mouse entra en el área del botón.
             * Oscurece el color del botón para dar feedback visual.
             */
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.darker());
            }
            
            /**
             * Se ejecuta cuando el mouse sale del área del botón.
             * Restaura el color original del botón.
             */
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }

    /**
     * Configura todos los ActionListeners para los botones del menú.
     * Asocia cada botón con su método correspondiente para manejar
     * las acciones del usuario.
     * 
     * Listeners configurados:
     * - 9 botones de módulos del sistema
     * - 2 botones de control (Cerrar Sesión y Salir)
     * 
     * Características de los listeners:
     * - Confirmación para acciones críticas (salir, cerrar sesión)
     * - Integración con el MainController para gestión de sesión
     * - Apertura de ventanas específicas para cada módulo
     */
    private void agregarActionListeners() {
        // Listeners para botones de módulos del sistema
        btnPredios.addActionListener(e -> abrirGestionPredios());
        btnMunicipios.addActionListener(e -> abrirGestionMunicipios());
        btnPlagas.addActionListener(e -> abrirGestionPlagas());
        btnLugaresProduccion.addActionListener(e -> abrirGestionLugaresProduccion());
        btnProductores.addActionListener(e -> abrirGestionProductores());
        btnSedesICA.addActionListener(e -> abrirGestionSedesICA());
        btnInspectores.addActionListener(e -> abrirGestionInspectores());
        btnCultivos.addActionListener(e -> abrirGestionCultivos());
        btnInspecciones.addActionListener(e -> abrirGestionInspecciones());
        
        // Listener para cerrar sesión con confirmación
        btnCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cerrarSesion();
            }
        });
        
        // Listener para salir del sistema con confirmación
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea salir del sistema?",
                "Salir del Sistema",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);  // Termina la aplicación completamente
            }
        });
    }

    /**
     * Actualiza la etiqueta del usuario actual en la interfaz
     * @param usuario Nombre del usuario que ha iniciado sesión
     */
    public void setUsuarioActual(String usuario) {
        lblUsuario.setText("Usuario: " + usuario);
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
        new GestionPredios().setVisible(true);
    }

    private void abrirGestionMunicipios() {
        new GestionMunicipios().setVisible(true);
    }

    private void abrirGestionPlagas() {
        new GestionPlagas().setVisible(true);
    }

    private void abrirGestionLugaresProduccion() {
        new GestionLugaresProduccion().setVisible(true);
    }

    private void abrirGestionProductores() {
        new GestionProductores().setVisible(true);
    }

    private void abrirGestionSedesICA() {
        new GestionSedesICA().setVisible(true);
    }

    private void abrirGestionInspectores() {
        new GestionInspectores().setVisible(true);
    }

    private void abrirGestionCultivos() {
        new GestionCultivos().setVisible(true);
    }

    private void abrirGestionInspecciones() {
        new GestionInspecciones().setVisible(true);
    }
}