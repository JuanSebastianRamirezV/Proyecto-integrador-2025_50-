package vista;

import controlador.PredioController;
import modelo.Predio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class GestionPredios extends JFrame {
    private JTable tablaPredios;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNombrePredio, txtNombrePropietario, txtIdMunicipio;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar, btnRefrescar;
    private PredioController controller;

    /**
     * Constructor principal de la clase GestionPredios
     * Inicializa el controlador y los componentes de la interfaz
     */
    public GestionPredios() {
        this.controller = new PredioController();
        initComponents();
        cargarDatos();
    }

    /**
     * Inicializa y configura todos los componentes visuales de la interfaz
     * Establece el diseño principal, tamaño, posición y comportamiento de la ventana
     */
    private void initComponents() {
        setTitle("Gestión de Predios - CRUD Completo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
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
     * Crea y configura el panel del formulario para ingreso de datos de predios
     * Utiliza GridBagLayout para un diseño organizado y flexible
     * 
     * @return JPanel configurado con todos los campos del formulario
     */
    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Datos del Predio"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(350, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Componentes del formulario
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID Predio:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(240, 240, 240));
        panelFormulario.add(txtId, gbc);

        // Campo Nombre Predio (obligatorio)
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombre Predio:*"), gbc);
        gbc.gridx = 1;
        txtNombrePredio = new JTextField();
        panelFormulario.add(txtNombrePredio, gbc);

        // Campo Nombre Propietario (obligatorio)
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Nombre Propietario:*"), gbc);
        gbc.gridx = 1;
        txtNombrePropietario = new JTextField();
        panelFormulario.add(txtNombrePropietario, gbc);

        // Campo ID Municipio
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("ID Municipio:"), gbc);
        gbc.gridx = 1;
        txtIdMunicipio = new JTextField();
        panelFormulario.add(txtIdMunicipio, gbc);

        // Panel de botones del formulario
        gbc.gridx = 0; gbc.gridy = 4;
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
     * Crea y configura el componente de tabla con scroll para mostrar los predios
     * Incluye personalización visual con colores alternados y selección
     * 
     * @return JScrollPane que contiene la tabla de predios
     */
    private JScrollPane crearScrollTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Definición de columnas de la tabla
        modeloTabla.addColumn("ID Predio");
        modeloTabla.addColumn("Nombre Predio");
        modeloTabla.addColumn("Nombre Propietario");
        modeloTabla.addColumn("ID Municipio");

        // Configuración de la tabla
        tablaPredios = new JTable(modeloTabla);
        tablaPredios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPredios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaPredios.getTableHeader().setBackground(new Color(70, 130, 180));
        tablaPredios.getTableHeader().setForeground(Color.WHITE);
        tablaPredios.setRowHeight(25);
        
        // Renderer para filas alternadas - CORREGIDO
        tablaPredios.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    // Fila seleccionada
                    c.setBackground(new Color(41, 128, 185));
                    c.setForeground(Color.WHITE);
                } else {
                    // Filas no seleccionadas - colores alternados
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

        JScrollPane scrollTabla = new JScrollPane(tablaPredios);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Lista de Predios"
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
        
        btnAgregar = crearBoton("Agregar Predio", new Color(39, 174, 96));
        btnActualizar = crearBoton("Actualizar Predio", new Color(41, 128, 185));
        btnEliminar = crearBoton("Eliminar Predio", new Color(231, 76, 60));
        btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182));
        
        // Tooltips para mejor usabilidad
        btnAgregar.setToolTipText("Agregar un nuevo predio a la base de datos");
        btnActualizar.setToolTipText("Actualizar el predio seleccionado");
        btnEliminar.setToolTipText("Eliminar el predio seleccionado");
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

    /**
     * Configura todos los ActionListeners para los componentes de la interfaz
     * Conecta los eventos de usuario con los métodos correspondientes
     */
    private void agregarActionListeners() {
        btnAgregar.addActionListener(e -> agregarPredio());
        btnActualizar.addActionListener(e -> actualizarPredio());
        btnEliminar.addActionListener(e -> eliminarPredio());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarPredios());
        btnRefrescar.addActionListener(e -> cargarDatos());

        tablaPredios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaPredios.getSelectedRow() != -1) {
                seleccionarPredioDeTabla();
            }
        });
    }

    private void cargarDatos() {
        limpiarTabla();
        List<Predio> predios = controller.obtenerTodosPredios();
        
        if (predios.isEmpty()) {
            mostrarMensajeInformacion("No hay predios registrados en la base de datos.");
        } else {
            for (Predio predio : predios) {
                Object[] fila = {
                    predio.getIdPredio(),
                    predio.getNombrePredio(),
                    predio.getNombrePropietario(),
                    predio.getIdMunicipio()
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeInformacion("Se cargaron " + predios.size() + " predio(s) desde la base de datos.");
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombrePredio.setText("");
        txtNombrePropietario.setText("");
        txtIdMunicipio.setText("");
        tablaPredios.clearSelection();
        txtNombrePredio.requestFocus();
    }

    private void seleccionarPredioDeTabla() {
        int filaSeleccionada = tablaPredios.getSelectedRow();
        if (filaSeleccionada >= 0) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtNombrePredio.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtNombrePropietario.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtIdMunicipio.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
        }
    }

    private void agregarPredio() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Predio nuevoPredio = new Predio();
            // NO establecer setIdPredio() - se generará automáticamente
            nuevoPredio.setNombrePredio(txtNombrePredio.getText().trim());
            nuevoPredio.setNombrePropietario(txtNombrePropietario.getText().trim());
            nuevoPredio.setIdMunicipio(parseInt(txtIdMunicipio.getText().trim()));

            if (controller.agregarPredio(nuevoPredio)) {
                mostrarMensajeExito("Predio agregado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar el predio a la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void actualizarPredio() {
        if (txtId.getText().isEmpty()) {
            mostrarMensajeError("Seleccione un predio de la tabla para actualizar");
            return;
        }

        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Predio predio = new Predio();
            predio.setIdPredio(Integer.parseInt(txtId.getText()));
            predio.setNombrePredio(txtNombrePredio.getText().trim());
            predio.setNombrePropietario(txtNombrePropietario.getText().trim());
            predio.setIdMunicipio(parseInt(txtIdMunicipio.getText().trim()));

            if (controller.actualizarPredio(predio)) {
                mostrarMensajeExito("Predio actualizado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar el predio en la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminarPredio() {
        int filaSeleccionada = tablaPredios.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeError("Seleccione un predio de la tabla para eliminar");
            return;
        }

        int idPredio = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombrePredio = modeloTabla.getValueAt(filaSeleccionada, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar el predio?\n" +
            "Nombre: " + nombrePredio + "\n" +
            "ID: " + idPredio + "\n\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (controller.eliminarPredio(idPredio)) {
                    mostrarMensajeExito("Predio eliminado exitosamente");
                    cargarDatos();
                    limpiarFormulario();
                } else {
                    mostrarMensajeError("Error al eliminar el predio de la base de datos");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error inesperado: " + ex.getMessage());
            }
        }
    }

    private void buscarPredios() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre del predio o propietario a buscar:",
            "Buscar Predios",
            JOptionPane.QUESTION_MESSAGE);

        if (criterio != null && !criterio.trim().isEmpty()) {
            try {
                List<Predio> resultados = controller.buscarPredios(criterio.trim());
                limpiarTabla();
                
                if (resultados.isEmpty()) {
                    mostrarMensajeInformacion("No se encontraron predios con el criterio: " + criterio);
                } else {
                    for (Predio predio : resultados) {
                        Object[] fila = {
                            predio.getIdPredio(),
                            predio.getNombrePredio(),
                            predio.getNombrePropietario(),
                            predio.getIdMunicipio()
                        };
                        modeloTabla.addRow(fila);
                    }
                    mostrarMensajeExito("Se encontraron " + resultados.size() + " predio(s)");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error durante la búsqueda: " + ex.getMessage());
            }
        } else if (criterio != null) {
            cargarDatos();
        }
    }

    private boolean validarCamposObligatorios() {
        if (txtNombrePredio.getText().trim().isEmpty() || txtNombrePropietario.getText().trim().isEmpty()) {
            mostrarMensajeError("Los campos Nombre Predio y Nombre Propietario son obligatorios");
            if (txtNombrePredio.getText().trim().isEmpty()) {
                txtNombrePredio.requestFocus();
            } else {
                txtNombrePropietario.requestFocus();
            }
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
            new GestionPredios().setVisible(true);
        });
    }
}