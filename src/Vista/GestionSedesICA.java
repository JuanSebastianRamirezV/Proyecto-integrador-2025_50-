package vista;

import controlador.SedeICAController;
import modelo.SedeICA;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class GestionSedesICA extends JFrame {
    
    // Componentes de la interfaz gráfica
    private JTable tablaSedes;                    // Tabla para mostrar la lista de sedes ICA
    private DefaultTableModel modeloTabla;        // Modelo de datos para la tabla
    private JTextField txtId;                     // Campo para ID de la sede (oculto)
    private JTextField txtCorreo;                 // Campo para correo electrónico de la sede
    private JTextField txtTelefono;               // Campo para teléfono de la sede
    
    // Botones de acción
    private JButton btnAgregar;                   // Botón para agregar nueva sede ICA
    private JButton btnActualizar;                // Botón para actualizar sede existente
    private JButton btnEliminar;                  // Botón para eliminar sede seleccionada
    private JButton btnLimpiar;                   // Botón para limpiar el formulario
    private JButton btnBuscar;                    // Botón para buscar sedes
    private JButton btnRefrescar;                 // Botón para refrescar los datos de la tabla
    
    // Controlador para la lógica de negocio
    private SedeICAController controller;         // Controlador que maneja las operaciones con la base de datos

    /**
     * Constructor de la clase GestionSedesICA.
     * Inicializa la interfaz gráfica y carga los datos iniciales de las sedes ICA.
     * 
     * Flujo de ejecución:
     * 1. Inicializa el controlador de sedes ICA
     * 2. Configura los componentes de la interfaz
     * 3. Carga los datos desde la base de datos
     */
    public GestionSedesICA() {
        this.controller = new SedeICAController();
        initComponents();
        cargarDatos();
    }

    /**
     * Inicializa todos los componentes de la interfaz gráfica.
     * Configura la ventana principal, paneles, formularios, tabla y botones.
     * 
     * Estructura de la interfaz:
     * - Panel principal con BorderLayout
     * - Panel de formulario (Oeste) para ingreso de datos
     * - Tabla de sedes (Centro) para visualización
     * - Panel de botones principales (Sur) para operaciones CRUD
     * 
     * Colores corporativos: Verde (#16A085) como color principal temático del ICA
     */
    private void initComponents() {
        // Configuración básica de la ventana
        setTitle("Gestión de Sedes ICA - CRUD Completo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Cierra solo esta ventana
        setSize(1200, 700);                                 // Tamaño inicial optimizado para visualización de datos
        setLocationRelativeTo(null);                        // Centra la ventana en la pantalla
        setResizable(true);                                 // Permite redimensionar la ventana

        // Panel principal con márgenes y color de fondo
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(240, 240, 240));  // Fondo gris claro

        // Creación de los componentes principales
        JPanel panelFormulario = crearPanelFormulario();           // Panel del formulario izquierdo
        JPanel panelBotonesPrincipales = crearPanelBotonesPrincipales(); // Panel de botones inferiores
        JScrollPane scrollTabla = crearScrollTabla();              // Tabla con scroll para los datos

        // Ensamblaje de los componentes en el panel principal
        panelPrincipal.add(panelFormulario, BorderLayout.WEST);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotonesPrincipales, BorderLayout.SOUTH);

        // Agregar panel principal a la ventana y configurar listeners de eventos
        add(panelPrincipal);
        agregarActionListeners();
    }

    /**
     * Crea el panel del formulario para ingreso y edición de datos de sedes ICA.
     * 
     * @return JPanel configurado con GridBagLayout conteniendo todos los campos del formulario
     * 
     * Campos incluidos:
     * - ID Sede (campo oculto, auto-generado)
     * - Correo Electrónico (campo obligatorio)
     * - Teléfono (campo obligatorio)
     * - Botones de Limpiar y Buscar
     * 
     * Diseño: GridBagLayout para alineación precisa y responsive
     */
    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        // Configuración del borde con color temático verde ICA
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(22, 160, 133), 2),
            "Datos de la Sede ICA"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(400, 0));  // Ancho fijo de 400px

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);    // Márgenes entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los componentes se expanden horizontalmente
        gbc.weightx = 1.0;                      // Distribución equitativa del espacio

        // CAMBIO: Campo ID Sede OCULTO
        txtId = new JTextField();
        txtId.setVisible(false);  // Campo oculto para uso interno

        // Campo Correo Electrónico (obligatorio)
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Correo Electrónico:*"), gbc);
        gbc.gridx = 1;
        txtCorreo = new JTextField();
        panelFormulario.add(txtCorreo, gbc);

        // Campo Teléfono (obligatorio)
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Teléfono:*"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField();
        panelFormulario.add(txtTelefono, gbc);

        // Panel de botones del formulario (Limpiar y Buscar)
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;        // Ocupa dos columnas
        gbc.insets = new Insets(15, 5, 5, 5);  // Mayor margen superior
        JPanel panelBotonesForm = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotonesForm.setBackground(Color.WHITE);
        
        btnLimpiar = crearBoton("Limpiar", new Color(243, 156, 18));  // Naranja para acciones secundarias
        btnBuscar = crearBoton("Buscar", new Color(52, 152, 219));    // Azul para acciones de búsqueda
        
        panelBotonesForm.add(btnLimpiar);
        panelBotonesForm.add(btnBuscar);
        panelFormulario.add(panelBotonesForm, gbc);

        return panelFormulario;
    }

    /**
     * Crea y configura la tabla de sedes ICA dentro de un JScrollPane.
     * 
     * @return JScrollPane conteniendo la tabla de sedes ICA configurada
     * 
     * Características de la tabla:
     * - No editable directamente (solo mediante formulario)
     * - Selección única de filas
     * - Encabezado con estilo personalizado en verde ICA
     * - Filas alternadas con colores diferentes para mejor legibilidad
     * - Renderer personalizado para resaltado de selección
     * - Altura de fila optimizada para lectura
     */
    private JScrollPane crearScrollTabla() {
        // Modelo de tabla no editable para prevenir modificaciones directas
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Todas las celdas son de solo lectura
            }
        };
        
        // CAMBIO: Definición de columnas sin ID
        modeloTabla.addColumn("Correo Electrónico");
        modeloTabla.addColumn("Teléfono");

        // Configuración de la tabla visual
        tablaSedes = new JTable(modeloTabla);
        tablaSedes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // Solo una selección a la vez
        
        // Estilo del encabezado de la tabla
        tablaSedes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaSedes.getTableHeader().setBackground(new Color(22, 160, 133));  // Verde ICA corporativo
        tablaSedes.getTableHeader().setForeground(Color.WHITE);
        tablaSedes.setRowHeight(25);  // Altura fija de filas para mejor legibilidad
        
        // Renderer personalizado para mejorar la experiencia visual
        tablaSedes.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    // Fila seleccionada: azul con texto blanco para claro contraste
                    c.setBackground(new Color(41, 128, 185));
                    c.setForeground(Color.WHITE);
                } else {
                    // Filas no seleccionadas - colores alternados: blanco y gris claro para mejor contraste
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(240, 240, 240));
                    }
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        // Scroll pane que contiene la tabla con borde temático
        JScrollPane scrollTabla = new JScrollPane(tablaSedes);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(22, 160, 133), 2),
            "Lista de Sedes ICA"
        ));

        return scrollTabla;
    }

    /**
     * Crea el panel de botones principales para operaciones CRUD.
     * 
     * @return JPanel con los botones de operaciones principales
     * 
     * Botones incluidos:
     * - Agregar Sede (Verde) - Para crear nuevos registros
     * - Actualizar Sede (Azul) - Para modificar registros existentes
     * - Eliminar Sede (Rojo) - Para eliminar registros seleccionados
     * - Refrescar Datos (Púrpura) - Para actualizar la vista con datos actualizados
     */
    private JPanel crearPanelBotonesPrincipales() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        // Creación de botones con colores semánticos para diferentes acciones
        btnAgregar = crearBoton("Agregar Sede", new Color(39, 174, 96));      // Verde para acciones positivas
        btnActualizar = crearBoton("Actualizar Sede", new Color(41, 128, 185)); // Azul para acciones de modificación
        btnEliminar = crearBoton("Eliminar Sede", new Color(231, 76, 60));    // Rojo para acciones destructivas
        btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182)); // Púrpura para acciones de actualización
        
        // Tooltips para mejorar la usabilidad y accesibilidad
        btnAgregar.setToolTipText("Agregar una nueva sede ICA a la base de datos");
        btnActualizar.setToolTipText("Actualizar la sede ICA seleccionada");
        btnEliminar.setToolTipText("Eliminar la sede ICA seleccionada");
        btnRefrescar.setToolTipText("Actualizar la tabla con los últimos datos");
        
        // Agregar botones al panel en orden lógico de uso
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        
        return panelBotones;
    }

    /**
     * Método de utilidad para crear botones con estilo consistente en toda la aplicación.
     * 
     * @param texto Texto que se mostrará en el botón
     * @param color Color de fondo del botón (usado semánticamente para diferentes acciones)
     * @return JButton configurado con el estilo uniforme de la aplicación
     * 
     * Características del botón:
     * - Texto en blanco sobre color de fondo
     * - Sin borde de foco pintado
     * - Sin borde pintado (flat design)
     * - Fuente Segoe UI en negrita
     * - Tamaño consistente (150x35 px)
     */
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);      // Elimina el borde de foco por estética
        boton.setBorderPainted(false);     // Elimina el borde por defecto (flat design)
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setPreferredSize(new Dimension(150, 35));  // Tamaño consistente para todos los botones
        return boton;
    }

    /**
     * Configura todos los ActionListeners para los botones y componentes interactivos.
     * Asocia cada botón con su método correspondiente y configura la selección de la tabla.
     * 
     * Listeners configurados:
     * - Botones CRUD principales
     * - Botones de utilidad (Limpiar, Buscar, Refrescar)
     * - Listener de selección de filas en la tabla
     */
    private void agregarActionListeners() {
        // Listeners para botones de operaciones CRUD principales
        btnAgregar.addActionListener(e -> agregarSede());
        btnActualizar.addActionListener(e -> actualizarSede());
        btnEliminar.addActionListener(e -> eliminarSede());
        
        // Listeners para botones de utilidad del formulario
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarSedes());
        btnRefrescar.addActionListener(e -> cargarDatos());

        // Listener para selección de filas en la tabla - permite edición de registros existentes
        tablaSedes.getSelectionModel().addListSelectionListener(e -> {
            // Evita múltiples eventos durante la selección y verifica que haya una fila seleccionada
            if (!e.getValueIsAdjusting() && tablaSedes.getSelectedRow() != -1) {
                seleccionarSedeDeTabla();
            }
        });
    }

    /**
     * Carga todas las sedes ICA desde la base de datos y las muestra en la tabla.
     * Limpia la tabla existente antes de cargar los nuevos datos.
     * Proporciona retroalimentación al usuario sobre el resultado de la operación.
     * 
     * Flujo de ejecución:
     * 1. Limpia la tabla actual
     * 2. Obtiene la lista de sedes desde el controlador
     * 3. Itera sobre la lista y agrega cada sede como fila en la tabla
     * 4. Muestra mensaje informativo con el conteo de registros
     */
    private void cargarDatos() {
        limpiarTabla();
        List<SedeICA> sedes = controller.obtenerTodasSedesICA();
        
        if (sedes.isEmpty()) {
            mostrarMensajeInformacion("No hay sedes ICA registradas en la base de datos.");
        } else {
            for (SedeICA sede : sedes) {
                Object[] fila = {
                    // CAMBIO: No mostrar ID en la tabla
                    sede.getCorreoElectronico(),
                    sede.getTelefono()
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeInformacion("Se cargaron " + sedes.size() + " sede(s) ICA desde la base de datos.");
        }
    }

    /**
     * Limpia todos los datos de la tabla.
     * Elimina todas las filas del modelo de tabla, preparándola para nuevos datos.
     */
    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        tablaSedes.clearSelection();
        txtCorreo.requestFocus();
    }

    private void seleccionarSedeDeTabla() {
        int filaSeleccionada = tablaSedes.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // CAMBIO: Obtener el ID usando los datos visibles
            String correo = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
            String telefono = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
            
            // Buscar la sede por correo y teléfono
            List<SedeICA> sedes = controller.buscarSedesICA(correo);
            for (SedeICA sede : sedes) {
                if (sede.getTelefono().equals(telefono)) {
                    txtId.setText(String.valueOf(sede.getIdSede()));
                    break;
                }
            }
            
            txtCorreo.setText(correo);
            txtTelefono.setText(telefono);
        }
    }

    private void agregarSede() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            SedeICA nuevaSede = new SedeICA();
            // NO establecer setIdSede() - se generará automáticamente
            nuevaSede.setCorreoElectronico(txtCorreo.getText().trim());
            nuevaSede.setTelefono(txtTelefono.getText().trim());

            if (controller.agregarSedeICA(nuevaSede)) {
                mostrarMensajeExito("Sede ICA agregada exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar la sede ICA a la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void actualizarSede() {
        if (txtId.getText().isEmpty()) {
            mostrarMensajeError("Seleccione una sede de la tabla para actualizar");
            return;
        }

        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            SedeICA sede = new SedeICA();
            sede.setIdSede(Integer.parseInt(txtId.getText()));
            sede.setCorreoElectronico(txtCorreo.getText().trim());
            sede.setTelefono(txtTelefono.getText().trim());

            if (controller.actualizarSedeICA(sede)) {
                mostrarMensajeExito("Sede ICA actualizada exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar la sede ICA en la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminarSede() {
        int filaSeleccionada = tablaSedes.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeError("Seleccione una sede de la tabla para eliminar");
            return;
        }

        // CAMBIO: Obtener el ID usando los datos visibles
        String correo = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
        String telefono = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
        
        List<SedeICA> sedes = controller.buscarSedesICA(correo);
        int idSede = 0;
        for (SedeICA sede : sedes) {
            if (sede.getTelefono().equals(telefono)) {
                idSede = sede.getIdSede();
                break;
            }
        }

        if (idSede == 0) {
            mostrarMensajeError("No se pudo encontrar la sede seleccionada");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar la sede ICA?\n" +
            "Correo: " + correo + "\n" +
            "Teléfono: " + telefono + "\n\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (controller.eliminarSedeICA(idSede)) {
                    mostrarMensajeExito("Sede ICA eliminada exitosamente");
                    cargarDatos();
                    limpiarFormulario();
                } else {
                    mostrarMensajeError("Error al eliminar la sede ICA de la base de datos");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error inesperado: " + ex.getMessage());
            }
        }
    }

    private void buscarSedes() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el correo o teléfono a buscar:",
            "Buscar Sedes ICA",
            JOptionPane.QUESTION_MESSAGE);

        if (criterio != null && !criterio.trim().isEmpty()) {
            try {
                List<SedeICA> resultados = controller.buscarSedesICA(criterio.trim());
                limpiarTabla();
                
                if (resultados.isEmpty()) {
                    mostrarMensajeInformacion("No se encontraron sedes con el criterio: " + criterio);
                } else {
                    for (SedeICA sede : resultados) {
                        Object[] fila = {
                            sede.getCorreoElectronico(),
                            sede.getTelefono()
                        };
                        modeloTabla.addRow(fila);
                    }
                    mostrarMensajeExito("Se encontraron " + resultados.size() + " sede(s)");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error durante la búsqueda: " + ex.getMessage());
            }
        } else if (criterio != null) {
            cargarDatos();
        }
    }

    private boolean validarCamposObligatorios() {
        if (txtCorreo.getText().trim().isEmpty()) {
            mostrarMensajeError("El campo Correo Electrónico es obligatorio");
            txtCorreo.requestFocus();
            return false;
        }

        if (txtTelefono.getText().trim().isEmpty()) {
            mostrarMensajeError("El campo Teléfono es obligatorio");
            txtTelefono.requestFocus();
            return false;
        }

        // Validación básica de formato de correo
        String correo = txtCorreo.getText().trim();
        if (!correo.contains("@") || !correo.contains(".")) {
            mostrarMensajeError("Por favor ingrese un correo electrónico válido");
            txtCorreo.requestFocus();
            return false;
        }

        return true;
    }

    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensajeInformacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GestionSedesICA().setVisible(true);
        });
    }
}