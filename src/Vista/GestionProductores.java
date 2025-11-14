package vista;

import controlador.ProductorController;
import modelo.Productor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class GestionProductores extends JFrame {
    private JTable tablaProductores;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNombre, txtRol;
    private JComboBox<String> cmbTipoIdentificacion;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar, btnRefrescar;
    private ProductorController controller;

    /**
     * Constructor de la clase GestionProductores.
     * Inicializa la interfaz gráfica y carga los datos iniciales.
     * 
     * Flujo de ejecución:
     * 1. Inicializa el controlador de productores
     * 2. Configura los componentes de la interfaz
     * 3. Carga los datos desde la base de datos
     */
    public GestionProductores() {
        this.controller = new ProductorController();
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
     * - Tabla de productores (Centro) para visualización
     * - Panel de botones principales (Sur) para operaciones CRUD
     */
    private void initComponents() {
        setTitle("Gestión de Productores - CRUD Completo");
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
     * Crea el panel del formulario para ingreso y edición de datos de productores.
     * 
     * @return JPanel configurado con GridBagLayout conteniendo todos los campos del formulario
     * 
     * Campos incluidos:
     * - ID Productor (campo oculto, auto-generado)
     * - Nombre Completo (campo obligatorio)
     * - Tipo Identificación (combo box con opciones predefinidas)
     * - Rol (campo obligatorio)
     * - Botones de Limpiar y Buscar
     */
    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(243, 156, 18), 2),
            "Datos del Productor"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(400, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // CAMBIO: Campo ID Productor OCULTO
        txtId = new JTextField();
        txtId.setVisible(false); // Campo oculto para uso interno

        // Campo Nombre Completo (obligatorio)
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre Completo:*"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre, gbc);

        // Campo Tipo Identificación (obligatorio)
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Tipo Identificación:*"), gbc);
        gbc.gridx = 1;
        cmbTipoIdentificacion = new JComboBox<>(new String[]{"Cédula", "NIT", "Pasaporte", "Cédula Extranjería"});
        panelFormulario.add(cmbTipoIdentificacion, gbc);

        // Campo Rol (obligatorio)
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Rol:*"), gbc);
        gbc.gridx = 1;
        txtRol = new JTextField();
        panelFormulario.add(txtRol, gbc);

        // Panel de botones del formulario
        gbc.gridx = 0; gbc.gridy = 3;
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
     * Crea y configura la tabla de productores dentro de un JScrollPane.
     * 
     * @return JScrollPane conteniendo la tabla de productores configurada
     * 
     * Características de la tabla:
     * - No editable directamente
     * - Selección única de filas
     * - Encabezado con estilo personalizado
     * - Filas alternadas con colores diferentes para mejor legibilidad
     * - Renderer personalizado para resaltado de selección
     */
    private JScrollPane crearScrollTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // CAMBIO: Definición de columnas sin ID
        modeloTabla.addColumn("Nombre Completo");
        modeloTabla.addColumn("Tipo Identificación");
        modeloTabla.addColumn("Rol");

        // Configuración de la tabla
        tablaProductores = new JTable(modeloTabla);
        tablaProductores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductores.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaProductores.getTableHeader().setBackground(new Color(243, 156, 18));
        tablaProductores.getTableHeader().setForeground(Color.WHITE);
        tablaProductores.setRowHeight(25);
        
        // Renderer para filas alternadas - CORREGIDO
        tablaProductores.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        // Scroll pane que contiene la tabla
        JScrollPane scrollTabla = new JScrollPane(tablaProductores);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(243, 156, 18), 2),
            "Lista de Productores"
        ));

        return scrollTabla;
    }

    /**
     * Crea el panel de botones principales para operaciones CRUD.
     * 
     * @return JPanel con los botones de operaciones principales
     * 
     * Botones incluidos:
     * - Agregar Productor (Verde)
     * - Actualizar Productor (Azul)
     * - Eliminar Productor (Rojo)
     * - Refrescar Datos (Púrpura)
     */
    private JPanel crearPanelBotonesPrincipales() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnAgregar = crearBoton("Agregar Productor", new Color(39, 174, 96));
        btnActualizar = crearBoton("Actualizar Productor", new Color(41, 128, 185));
        btnEliminar = crearBoton("Eliminar Productor", new Color(231, 76, 60));
        btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182));
        
        // Tooltips para mejor usabilidad
        btnAgregar.setToolTipText("Agregar un nuevo productor a la base de datos");
        btnActualizar.setToolTipText("Actualizar el productor seleccionado");
        btnEliminar.setToolTipText("Eliminar el productor seleccionado");
        btnRefrescar.setToolTipText("Actualizar la tabla con los últimos datos");
        
        // Agregar botones al panel
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        
        return panelBotones;
    }

    /**
     * Método de utilidad para crear botones con estilo consistente.
     * 
     * @param texto Texto que se mostrará en el botón
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
     * Configura todos los ActionListeners para los botones y componentes interactivos.
     * Asocia cada botón con su método correspondiente y configura la selección de la tabla.
     */
    private void agregarActionListeners() {
        btnAgregar.addActionListener(e -> agregarProductor());
        btnActualizar.addActionListener(e -> actualizarProductor());
        btnEliminar.addActionListener(e -> eliminarProductor());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarProductores());
        btnRefrescar.addActionListener(e -> cargarDatos());

        tablaProductores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaProductores.getSelectedRow() != -1) {
                seleccionarProductorDeTabla();
            }
        });
    }

    /**
     * Carga todos los productores desde la base de datos y los muestra en la tabla.
     * Limpia la tabla existente antes de cargar los nuevos datos.
     * Muestra un mensaje informativo con el resultado de la operación.
     */
    private void cargarDatos() {
        limpiarTabla();
        List<Productor> productores = controller.obtenerTodosProductores();
        
        if (productores.isEmpty()) {
            mostrarMensajeInformacion("No hay productores registrados en la base de datos.");
        } else {
            for (Productor productor : productores) {
                Object[] fila = {
                    // CAMBIO: No mostrar ID en la tabla
                    productor.getNombreCompleto(),
                    productor.getTipoIdentificacion(),
                    productor.getRol()
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeInformacion("Se cargaron " + productores.size() + " productor(es) desde la base de datos.");
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        cmbTipoIdentificacion.setSelectedIndex(0);
        txtRol.setText("");
        tablaProductores.clearSelection();
        txtNombre.requestFocus();
    }

    private void seleccionarProductorDeTabla() {
        int filaSeleccionada = tablaProductores.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // CAMBIO: Obtener el ID usando los datos visibles
            String nombre = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
            String tipoIdentificacion = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
            
            // Buscar el productor por nombre y tipo de identificación
            List<Productor> productores = controller.buscarProductores(nombre);
            for (Productor productor : productores) {
                if (productor.getTipoIdentificacion().equals(tipoIdentificacion)) {
                    txtId.setText(String.valueOf(productor.getIdProductor()));
                    break;
                }
            }
            
            txtNombre.setText(nombre);
            cmbTipoIdentificacion.setSelectedItem(tipoIdentificacion);
            txtRol.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
        }
    }

    private void agregarProductor() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Productor nuevoProductor = new Productor();
            // NO establecer setIdProductor() - se generará automáticamente
            nuevoProductor.setNombreCompleto(txtNombre.getText().trim());
            nuevoProductor.setTipoIdentificacion(cmbTipoIdentificacion.getSelectedItem().toString());
            nuevoProductor.setRol(txtRol.getText().trim());

            if (controller.agregarProductor(nuevoProductor)) {
                mostrarMensajeExito("Productor agregado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar el productor a la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void actualizarProductor() {
        if (txtId.getText().isEmpty()) {
            mostrarMensajeError("Seleccione un productor de la tabla para actualizar");
            return;
        }

        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Productor productor = new Productor();
            productor.setIdProductor(Integer.parseInt(txtId.getText()));
            productor.setNombreCompleto(txtNombre.getText().trim());
            productor.setTipoIdentificacion(cmbTipoIdentificacion.getSelectedItem().toString());
            productor.setRol(txtRol.getText().trim());

            if (controller.actualizarProductor(productor)) {
                mostrarMensajeExito("Productor actualizado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar el productor en la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminarProductor() {
        int filaSeleccionada = tablaProductores.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeError("Seleccione un productor de la tabla para eliminar");
            return;
        }

        // CAMBIO: Obtener el ID usando los datos visibles
        String nombre = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
        String tipoIdentificacion = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
        
        List<Productor> productores = controller.buscarProductores(nombre);
        int idProductor = 0;
        for (Productor productor : productores) {
            if (productor.getTipoIdentificacion().equals(tipoIdentificacion)) {
                idProductor = productor.getIdProductor();
                break;
            }
        }

        if (idProductor == 0) {
            mostrarMensajeError("No se pudo encontrar el productor seleccionado");
            return;
        }

        String rolProductor = modeloTabla.getValueAt(filaSeleccionada, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar el productor?\n" +
            "Nombre: " + nombre + "\n" +
            "Tipo Identificación: " + tipoIdentificacion + "\n" +
            "Rol: " + rolProductor + "\n\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (controller.eliminarProductor(idProductor)) {
                    mostrarMensajeExito("Productor eliminado exitosamente");
                    cargarDatos();
                    limpiarFormulario();
                } else {
                    mostrarMensajeError("Error al eliminar el productor de la base de datos");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error inesperado: " + ex.getMessage());
            }
        }
    }

    private void buscarProductores() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre, tipo de identificación o rol a buscar:",
            "Buscar Productores",
            JOptionPane.QUESTION_MESSAGE);

        if (criterio != null && !criterio.trim().isEmpty()) {
            try {
                List<Productor> resultados = controller.buscarProductores(criterio.trim());
                limpiarTabla();
                
                if (resultados.isEmpty()) {
                    mostrarMensajeInformacion("No se encontraron productores con el criterio: " + criterio);
                } else {
                    for (Productor productor : resultados) {
                        Object[] fila = {
                            // CAMBIO: No mostrar ID en la tabla
                            productor.getNombreCompleto(),
                            productor.getTipoIdentificacion(),
                            productor.getRol()
                        };
                        modeloTabla.addRow(fila);
                    }
                    mostrarMensajeExito("Se encontraron " + resultados.size() + " productor(es)");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error durante la búsqueda: " + ex.getMessage());
            }
        } else if (criterio != null) {
            cargarDatos();
        }
    }

    private boolean validarCamposObligatorios() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarMensajeError("El campo Nombre Completo es obligatorio");
            txtNombre.requestFocus();
            return false;
        }

        if (txtRol.getText().trim().isEmpty()) {
            mostrarMensajeError("El campo Rol es obligatorio");
            txtRol.requestFocus();
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
            new GestionProductores().setVisible(true);
        });
    }
}