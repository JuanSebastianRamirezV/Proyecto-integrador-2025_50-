package vista;

import controlador.InspectorController;
import modelo.Inspector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class GestionInspectores extends JFrame {
    private JTable tablaInspectores;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNumeroDocumento, txtNombresCompletos, txtTelefono, txtTarjetaProfesional, txtIdSede;
    private JComboBox<String> cmbTipoDocumento;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar, btnRefrescar;
    private InspectorController controller;

    /**
     * Constructor de la clase GestionInspectores
     * Inicializa el controlador y los componentes de la interfaz gráfica
     */
    public GestionInspectores() {
        this.controller = new InspectorController();
        initComponents();
        cargarDatos();
    }

    /**
     * Inicializa y configura todos los componentes visuales de la interfaz
     * Establece el diseño principal, tamaño, posición y comportamiento de la ventana
     */
    private void initComponents() {
        setTitle("Gestión de Inspectores - CRUD Completo");
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
     * Crea y configura el panel del formulario para ingreso de datos de inspectores
     * Utiliza GridBagLayout para un diseño organizado y flexible
     * 
     * @return JPanel configurado con todos los campos del formulario
     */
    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(142, 68, 173), 2),
            "Datos del Inspector"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(400, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Componentes del formulario
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID Inspector:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(240, 240, 240));
        panelFormulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Tipo Documento:*"), gbc);
        gbc.gridx = 1;
        cmbTipoDocumento = new JComboBox<>(new String[]{"CC", "NIT", "Pasaporte", "CE"});
        panelFormulario.add(cmbTipoDocumento, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Número Documento:*"), gbc);
        gbc.gridx = 1;
        txtNumeroDocumento = new JTextField();
        panelFormulario.add(txtNumeroDocumento, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Nombres Completos:*"), gbc);
        gbc.gridx = 1;
        txtNombresCompletos = new JTextField();
        panelFormulario.add(txtNombresCompletos, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField();
        panelFormulario.add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panelFormulario.add(new JLabel("Tarjeta Profesional:"), gbc);
        gbc.gridx = 1;
        txtTarjetaProfesional = new JTextField();
        panelFormulario.add(txtTarjetaProfesional, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panelFormulario.add(new JLabel("ID Sede:"), gbc);
        gbc.gridx = 1;
        txtIdSede = new JTextField();
        panelFormulario.add(txtIdSede, gbc);

        // Panel de botones del formulario
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        JPanel panelBotonesForm = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotonesForm.setBackground(Color.WHITE);
        
        btnLimpiar = crearBoton("Limpiar", new Color(243, 156, 18));
        btnBuscar = crearBoton("Buscar", new Color(52, 152, 219));
        
        panelBotonesForm.add(btnLimpiar);
        panelBotonesForm.add(btnBuscar);
        panelFormulario.add(panelBotonesForm, gbc);

        return panelFormulario;
    }

    /**
     * Crea y configura el componente de tabla con scroll para mostrar los inspectores
     * Incluye personalización visual con colores alternados y selección
     * 
     * @return JScrollPane que contiene la tabla de inspectores
     */
    private JScrollPane crearScrollTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Tipo Documento");
        modeloTabla.addColumn("Número Documento");
        modeloTabla.addColumn("Nombres Completos");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Tarjeta Profesional");
        modeloTabla.addColumn("ID Sede");

        tablaInspectores = new JTable(modeloTabla);
        tablaInspectores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInspectores.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaInspectores.getTableHeader().setBackground(new Color(142, 68, 173));
        tablaInspectores.getTableHeader().setForeground(Color.WHITE);
        tablaInspectores.setRowHeight(25);
        
        // Renderer para filas alternadas - CORREGIDO
        tablaInspectores.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        JScrollPane scrollTabla = new JScrollPane(tablaInspectores);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(142, 68, 173), 2),
            "Lista de Inspectores"
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
        
        btnAgregar = crearBoton("Agregar Inspector", new Color(39, 174, 96));
        btnActualizar = crearBoton("Actualizar Inspector", new Color(41, 128, 185));
        btnEliminar = crearBoton("Eliminar Inspector", new Color(231, 76, 60));
        btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182));
        
        // Tooltips para mejor usabilidad
        btnAgregar.setToolTipText("Agregar un nuevo inspector a la base de datos");
        btnActualizar.setToolTipText("Actualizar el inspector seleccionado");
        btnEliminar.setToolTipText("Eliminar el inspector seleccionado");
        btnRefrescar.setToolTipText("Actualizar la tabla con los últimos datos");
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        
        return panelBotones;
    }

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

    private void agregarActionListeners() {
        btnAgregar.addActionListener(e -> agregarInspector());
        btnActualizar.addActionListener(e -> actualizarInspector());
        btnEliminar.addActionListener(e -> eliminarInspector());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarInspectores());
        btnRefrescar.addActionListener(e -> cargarDatos());

        tablaInspectores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaInspectores.getSelectedRow() != -1) {
                seleccionarInspectorDeTabla();
            }
        });
    }

    private void cargarDatos() {
        limpiarTabla();
        List<Inspector> inspectores = controller.obtenerTodosInspectores();
        
        if (inspectores.isEmpty()) {
            mostrarMensajeInformacion("No hay inspectores registrados en la base de datos.");
        } else {
            for (Inspector inspector : inspectores) {
                Object[] fila = {
                    inspector.getIdInspector(),
                    inspector.getTipoDocumento(),
                    inspector.getNumeroDocumento(),
                    inspector.getNombresCompletos(),
                    inspector.getTelefono(),
                    inspector.getNumeroTarjetaProfesional(),
                    inspector.getIdSede()
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeInformacion("Se cargaron " + inspectores.size() + " inspector(es) desde la base de datos.");
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        txtId.setText("");
        cmbTipoDocumento.setSelectedIndex(0);
        txtNumeroDocumento.setText("");
        txtNombresCompletos.setText("");
        txtTelefono.setText("");
        txtTarjetaProfesional.setText("");
        txtIdSede.setText("");
        tablaInspectores.clearSelection();
        txtNumeroDocumento.requestFocus();
    }

    private void seleccionarInspectorDeTabla() {
        int filaSeleccionada = tablaInspectores.getSelectedRow();
        if (filaSeleccionada >= 0) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            cmbTipoDocumento.setSelectedItem(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtNumeroDocumento.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtNombresCompletos.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            txtTelefono.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
            txtTarjetaProfesional.setText(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
            txtIdSede.setText(modeloTabla.getValueAt(filaSeleccionada, 6).toString());
        }
    }

    private void agregarInspector() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Inspector nuevoInspector = new Inspector();
            // NO establecer setIdInspector - se generará automáticamente
            nuevoInspector.setTipoDocumento(cmbTipoDocumento.getSelectedItem().toString());
            nuevoInspector.setNumeroDocumento(txtNumeroDocumento.getText().trim());
            nuevoInspector.setNombresCompletos(txtNombresCompletos.getText().trim());
            nuevoInspector.setTelefono(txtTelefono.getText().trim());
            nuevoInspector.setNumeroTarjetaProfesional(txtTarjetaProfesional.getText().trim());
            nuevoInspector.setIdSede(parseInt(txtIdSede.getText().trim()));

            if (controller.agregarInspector(nuevoInspector)) {
                mostrarMensajeExito("Inspector agregado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar el inspector a la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void actualizarInspector() {
        if (txtId.getText().isEmpty()) {
            mostrarMensajeError("Seleccione un inspector de la tabla para actualizar");
            return;
        }

        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Inspector inspector = new Inspector();
            inspector.setIdInspector(Integer.parseInt(txtId.getText()));
            inspector.setTipoDocumento(cmbTipoDocumento.getSelectedItem().toString());
            inspector.setNumeroDocumento(txtNumeroDocumento.getText().trim());
            inspector.setNombresCompletos(txtNombresCompletos.getText().trim());
            inspector.setTelefono(txtTelefono.getText().trim());
            inspector.setNumeroTarjetaProfesional(txtTarjetaProfesional.getText().trim());
            inspector.setIdSede(parseInt(txtIdSede.getText().trim()));

            if (controller.actualizarInspector(inspector)) {
                mostrarMensajeExito("Inspector actualizado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar el inspector en la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminarInspector() {
        int filaSeleccionada = tablaInspectores.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeError("Seleccione un inspector de la tabla para eliminar");
            return;
        }

        int idInspector = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreInspector = modeloTabla.getValueAt(filaSeleccionada, 3).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar el inspector?\n" +
            "Nombre: " + nombreInspector + "\n" +
            "ID: " + idInspector + "\n\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (controller.eliminarInspector(idInspector)) {
                    mostrarMensajeExito("Inspector eliminado exitosamente");
                    cargarDatos();
                    limpiarFormulario();
                } else {
                    mostrarMensajeError("Error al eliminar el inspector de la base de datos");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error inesperado: " + ex.getMessage());
            }
        }
    }

    private void buscarInspectores() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese los nombres, número de documento o tarjeta profesional a buscar:",
            "Buscar Inspectores",
            JOptionPane.QUESTION_MESSAGE);

        if (criterio != null && !criterio.trim().isEmpty()) {
            try {
                List<Inspector> resultados = controller.buscarInspectores(criterio.trim());
                limpiarTabla();
                
                if (resultados.isEmpty()) {
                    mostrarMensajeInformacion("No se encontraron inspectores con el criterio: " + criterio);
                } else {
                    for (Inspector inspector : resultados) {
                        Object[] fila = {
                            inspector.getIdInspector(),
                            inspector.getTipoDocumento(),
                            inspector.getNumeroDocumento(),
                            inspector.getNombresCompletos(),
                            inspector.getTelefono(),
                            inspector.getNumeroTarjetaProfesional(),
                            inspector.getIdSede()
                        };
                        modeloTabla.addRow(fila);
                    }
                    mostrarMensajeExito("Se encontraron " + resultados.size() + " inspector(es)");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error durante la búsqueda: " + ex.getMessage());
            }
        } else if (criterio != null) {
            cargarDatos();
        }
    }

    private boolean validarCamposObligatorios() {
        if (txtNumeroDocumento.getText().trim().isEmpty()) {
            mostrarMensajeError("El campo Número Documento es obligatorio");
            txtNumeroDocumento.requestFocus();
            return false;
        }

        if (txtNombresCompletos.getText().trim().isEmpty()) {
            mostrarMensajeError("El campo Nombres Completos es obligatorio");
            txtNombresCompletos.requestFocus();
            return false;
        }

        return true;
    }

    private int parseInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
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
            new GestionInspectores().setVisible(true);
        });
    }
}