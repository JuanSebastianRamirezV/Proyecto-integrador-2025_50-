package vista;

import controlador.InspeccionController;
import controlador.InspectorController;
import controlador.CultivoController;
import modelo.Inspeccion;
import modelo.Inspector;
import modelo.Cultivo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GestionInspecciones extends JFrame {
     // Componentes de la interfaz gráfica
    private JTable tablaInspecciones;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtFecha, txtEstado, txtObservaciones;
    private JComboBox<String> cmbInspector, cmbCultivo;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar, btnRefrescar;
    // Controladores para acceso a datos
    private InspeccionController controller;
    private InspectorController inspectorController;
    private CultivoController cultivoController;

     /**
     * Constructor principal de la clase GestionInspecciones
     * Inicializa los controladores y componentes de la interfaz
     */
    public GestionInspecciones() {
        this.controller = new InspeccionController();
        this.inspectorController = new InspectorController();
        this.cultivoController = new CultivoController();
        initComponents();
        cargarDatos();
        cargarCombos();
    }
/**
     * Inicializa y configura todos los componentes visuales de la interfaz
     * Establece el diseño principal, tamaños y comportamientos básicos
     */
    private void initComponents() {
        setTitle("Gestión de Inspecciones - CRUD Completo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Panel de formulario
        JPanel panelFormulario = crearPanelFormulario();

        // Panel de botones principales
        JPanel panelBotonesPrincipales = crearPanelBotonesPrincipales();

        // Crear tabla y scroll pane directamente
        JScrollPane scrollTabla = crearScrollTabla();

        panelPrincipal.add(panelFormulario, BorderLayout.WEST);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotonesPrincipales, BorderLayout.SOUTH);

        add(panelPrincipal);
        agregarActionListeners();
    }

     /**
     * Crea y configura el panel del formulario para ingreso de datos
     * Utiliza GridBagLayout para un diseño flexible y organizado
     * 
     * @return JPanel configurado con todos los campos del formulario
     */
    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(230, 126, 34), 2),
            "Datos de la Inspección"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(400, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;// Peso para expansión horizontal
 /* SECCIÓN: CAMPOS DEL FORMULARIO */
        
        
        // Componentes del formulario
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID Inspección:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(240, 240, 240));
        panelFormulario.add(txtId, gbc);
// Campo Fecha (obligatorio) - Se auto-completa con fecha actual
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Fecha:*"), gbc);
        gbc.gridx = 1;
        txtFecha = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        panelFormulario.add(txtFecha, gbc);
 // Campo Estado (obligatorio) - Ej: Pendiente, Completada, Cancelada
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Estado:*"), gbc);
        gbc.gridx = 1;
        txtEstado = new JTextField();
        panelFormulario.add(txtEstado, gbc);
 // Campo Observaciones - Comentarios adicionales sobre la inspección
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        txtObservaciones = new JTextField();
        panelFormulario.add(txtObservaciones, gbc);
// ComboBox Inspector (obligatorio) - Lista de inspectores disponibles
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(new JLabel("Inspector:*"), gbc);
        gbc.gridx = 1;
        cmbInspector = new JComboBox<>();
        panelFormulario.add(cmbInspector, gbc);
// ComboBox Cultivo (obligatorio) - Lista de cultivos disponibles
        gbc.gridx = 0; gbc.gridy = 5;
        panelFormulario.add(new JLabel("Cultivo:*"), gbc);
        gbc.gridx = 1;
        cmbCultivo = new JComboBox<>();
        panelFormulario.add(cmbCultivo, gbc);

        // Panel de botones del formulario
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        JPanel panelBotonesForm = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotonesForm.setBackground(Color.WHITE);
        
        btnLimpiar = crearBoton("Limpiar", new Color(243, 156, 18));
        btnBuscar = crearBoton("Buscar", new Color(52, 152, 219));
        // Botones con colores
        panelBotonesForm.add(btnLimpiar);
        panelBotonesForm.add(btnBuscar);
        panelFormulario.add(panelBotonesForm, gbc);

        return panelFormulario;
    }
/**
     * Crea y configura el componente de tabla con scroll para mostrar las inspecciones
     * Incluye personalización visual con colores alternados y selección
     * 
     * @return JScrollPane que contiene la tabla de inspecciones
     */
    private JScrollPane crearScrollTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
         // Definición de columnas de la tabla
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Observaciones");
        modeloTabla.addColumn("Inspector");
        modeloTabla.addColumn("Cultivo");
// Configuración de la tabla
        tablaInspecciones = new JTable(modeloTabla);
        tablaInspecciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInspecciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaInspecciones.getTableHeader().setBackground(new Color(230, 126, 34));
        tablaInspecciones.getTableHeader().setForeground(Color.WHITE);
        tablaInspecciones.setRowHeight(25);
        
        // Renderer para filas alternadas - CORREGIDO
        tablaInspecciones.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                    c.setForeground(Color.BLACK); // ← CORRECCIÓN APLICADA: Texto negro para filas no seleccionadas
                } else {
                    c.setBackground(new Color(41, 128, 185));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaInspecciones);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(230, 126, 34), 2),
            "Lista de Inspecciones"
        ));

        return scrollTabla;
    }
/**
     * Crea el panel de botones principales para operaciones CRUD
     * 
     * @return JPanel con los botones de acciones principales
     */
    private JPanel crearPanelBotonesPrincipales() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnAgregar = crearBoton("Agregar Inspección", new Color(39, 174, 96));
        btnActualizar = crearBoton("Actualizar Inspección", new Color(41, 128, 185));
        btnEliminar = crearBoton("Eliminar Inspección", new Color(231, 76, 60));
        btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182));
        
        // Tooltips para mejor usabilidad
        btnAgregar.setToolTipText("Agregar una nueva inspección a la base de datos");
        btnActualizar.setToolTipText("Actualizar la inspección seleccionada");
        btnEliminar.setToolTipText("Eliminar la inspección seleccionada");
        btnRefrescar.setToolTipText("Actualizar la tabla con los últimos datos");
        // Agregar botones al panel
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        
        return panelBotones;
    }

    /**
     * Factory method para crear botones estilizados consistentes
     * 
     * @param texto Texto que muestra el botón
     * @param color Color de fondo del botón
     * @return JButton configurado con el estilo de la aplicación
     */
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setPreferredSize(new Dimension(150, 35));
        return boton;
    }

    private void cargarCombos() {
        // Cargar inspectores
        List<Inspector> inspectores = inspectorController.obtenerTodosInspectores();
        cmbInspector.removeAllItems();
        for (Inspector inspector : inspectores) {
            cmbInspector.addItem(inspector.getNombresCompletos() + " (" + inspector.getIdInspector() + ")");
        }

        // Cargar cultivos
        List<Cultivo> cultivos = cultivoController.obtenerTodosCultivos();
        cmbCultivo.removeAllItems();
        for (Cultivo cultivo : cultivos) {
            cmbCultivo.addItem(cultivo.getNombreCultivo() + " (" + cultivo.getIdCultivo() + ")");
        }
    }

    /**
     * Configura todos los ActionListeners para los componentes de la interfaz
     * Conecta los eventos de usuario con los métodos correspondientes
     */
    private void agregarActionListeners() {
        btnAgregar.addActionListener(e -> agregarInspeccion());
        btnActualizar.addActionListener(e -> actualizarInspeccion());
        btnEliminar.addActionListener(e -> eliminarInspeccion());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarInspecciones());
        btnRefrescar.addActionListener(e -> cargarDatos());

        tablaInspecciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaInspecciones.getSelectedRow() != -1) {
                seleccionarInspeccionDeTabla();
            }
        });
    }
 /**
     * Carga los datos de inspecciones desde la base de datos y los muestra en la tabla
     */
    private void cargarDatos() {
        limpiarTabla();
        List<Inspeccion> inspecciones = controller.obtenerTodasInspecciones();
        
        if (inspecciones.isEmpty()) {
            mostrarMensajeInformacion("No hay inspecciones registradas en la base de datos.");
        } else {
            for (Inspeccion inspeccion : inspecciones) {
                Inspector inspector = inspectorController.obtenerInspector(inspeccion.getIdInspector());
                Cultivo cultivo = cultivoController.obtenerCultivo(inspeccion.getIdCultivo());
                
                Object[] fila = {
                    inspeccion.getIdInspeccion(),
                    new SimpleDateFormat("yyyy-MM-dd").format(inspeccion.getFechaInspeccion()),
                    inspeccion.getEstado(),
                    inspeccion.getObservaciones(),
                    inspector != null ? inspector.getNombresCompletos() : "N/A",
                    cultivo != null ? cultivo.getNombreCultivo() : "N/A"
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeInformacion("Se cargaron " + inspecciones.size() + " inspección(es) desde la base de datos.");
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtEstado.setText("");
        txtObservaciones.setText("");
        if (cmbInspector.getItemCount() > 0) cmbInspector.setSelectedIndex(0);
        if (cmbCultivo.getItemCount() > 0) cmbCultivo.setSelectedIndex(0);
        tablaInspecciones.clearSelection();
        txtEstado.requestFocus();
    }

    private void seleccionarInspeccionDeTabla() {
        int filaSeleccionada = tablaInspecciones.getSelectedRow();
        if (filaSeleccionada >= 0) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtFecha.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtEstado.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtObservaciones.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            
            // Seleccionar el inspector y cultivo correspondientes en los combos
            seleccionarEnCombo(cmbInspector, modeloTabla.getValueAt(filaSeleccionada, 4).toString());
            seleccionarEnCombo(cmbCultivo, modeloTabla.getValueAt(filaSeleccionada, 5).toString());
        }
    }

    private void seleccionarEnCombo(JComboBox<String> combo, String valor) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).contains(valor)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void agregarInspeccion() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Inspeccion nuevaInspeccion = new Inspeccion();
            nuevaInspeccion.setFechaInspeccion(new SimpleDateFormat("yyyy-MM-dd").parse(txtFecha.getText()));
            nuevaInspeccion.setEstado(txtEstado.getText().trim());
            nuevaInspeccion.setObservaciones(txtObservaciones.getText().trim());
            
            // Obtener IDs de los combos
            nuevaInspeccion.setIdInspector(obtenerIdDeCombo(cmbInspector.getSelectedItem().toString()));
            nuevaInspeccion.setIdCultivo(obtenerIdDeCombo(cmbCultivo.getSelectedItem().toString()));

            if (controller.agregarInspeccion(nuevaInspeccion)) {
                mostrarMensajeExito("Inspección agregada exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar la inspección a la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void actualizarInspeccion() {
        if (txtId.getText().isEmpty()) {
            mostrarMensajeError("Seleccione una inspección de la tabla para actualizar");
            return;
        }

        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Inspeccion inspeccion = new Inspeccion();
            inspeccion.setIdInspeccion(Integer.parseInt(txtId.getText()));
            inspeccion.setFechaInspeccion(new SimpleDateFormat("yyyy-MM-dd").parse(txtFecha.getText()));
            inspeccion.setEstado(txtEstado.getText().trim());
            inspeccion.setObservaciones(txtObservaciones.getText().trim());
            
            // Obtener IDs de los combos
            inspeccion.setIdInspector(obtenerIdDeCombo(cmbInspector.getSelectedItem().toString()));
            inspeccion.setIdCultivo(obtenerIdDeCombo(cmbCultivo.getSelectedItem().toString()));

            if (controller.actualizarInspeccion(inspeccion)) {
                mostrarMensajeExito("Inspección actualizada exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar la inspección en la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminarInspeccion() {
    int filaSeleccionada = tablaInspecciones.getSelectedRow();
    if (filaSeleccionada < 0) {
        mostrarMensajeError("Seleccione una inspección de la tabla para eliminar");
        return;
    }

    int idInspeccion = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
    String estadoInspeccion = modeloTabla.getValueAt(filaSeleccionada, 2).toString();

    int confirm = JOptionPane.showConfirmDialog(this,
        "¿Está seguro que desea eliminar la inspección?\n" +
        "Estado: " + estadoInspeccion + "\n" +
        "ID: " + idInspeccion + "\n\n" +
        "Se eliminarán automáticamente todos los lugares de producción asociados a esta inspección.\n" +
        "Esta acción no se puede deshacer.",
        "Confirmar Eliminación",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            if (controller.eliminarInspeccion(idInspeccion)) {
                mostrarMensajeExito("Inspección y lugares de producción asociados eliminados exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al eliminar la inspección de la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }
}

    private void buscarInspecciones() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el estado u observaciones a buscar:",
            "Buscar Inspecciones",
            JOptionPane.QUESTION_MESSAGE);

        if (criterio != null && !criterio.trim().isEmpty()) {
            try {
                List<Inspeccion> resultados = controller.buscarInspecciones(criterio.trim());
                limpiarTabla();
                
                if (resultados.isEmpty()) {
                    mostrarMensajeInformacion("No se encontraron inspecciones con el criterio: " + criterio);
                } else {
                    for (Inspeccion inspeccion : resultados) {
                        Inspector inspector = inspectorController.obtenerInspector(inspeccion.getIdInspector());
                        Cultivo cultivo = cultivoController.obtenerCultivo(inspeccion.getIdCultivo());
                        
                        Object[] fila = {
                            inspeccion.getIdInspeccion(),
                            new SimpleDateFormat("yyyy-MM-dd").format(inspeccion.getFechaInspeccion()),
                            inspeccion.getEstado(),
                            inspeccion.getObservaciones(),
                            inspector != null ? inspector.getNombresCompletos() : "N/A",
                            cultivo != null ? cultivo.getNombreCultivo() : "N/A"
                        };
                        modeloTabla.addRow(fila);
                    }
                    mostrarMensajeExito("Se encontraron " + resultados.size() + " inspección(es)");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error durante la búsqueda: " + ex.getMessage());
            }
        } else if (criterio != null) {
            cargarDatos();
        }
    }

    private boolean validarCamposObligatorios() {
        if (txtFecha.getText().trim().isEmpty() || txtEstado.getText().trim().isEmpty() || 
            cmbInspector.getSelectedItem() == null || cmbCultivo.getSelectedItem() == null) {
            mostrarMensajeError("Los campos Fecha, Estado, Inspector y Cultivo son obligatorios");
            if (txtFecha.getText().trim().isEmpty()) {
                txtFecha.requestFocus();
            } else if (txtEstado.getText().trim().isEmpty()) {
                txtEstado.requestFocus();
            }
            return false;
        }
        
        // Validar formato de fecha
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(txtFecha.getText().trim());
        } catch (Exception e) {
            mostrarMensajeError("El formato de fecha debe ser YYYY-MM-DD");
            txtFecha.requestFocus();
            return false;
        }
        
        return true;
    }

    private int obtenerIdDeCombo(String itemCombo) {
        return Integer.parseInt(itemCombo.substring(itemCombo.lastIndexOf("(") + 1, itemCombo.lastIndexOf(")")));
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
            new GestionInspecciones().setVisible(true);
        });
    }
}