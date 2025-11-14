package vista;

import controlador.LugarProduccionController;
import modelo.LugarProduccion;
import modelo.Productor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class GestionLugaresProduccionProductor extends JFrame {
    
    private JTable tablaLugares;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNombre, txtRegistroICA, txtDireccion;
    private JComboBox<Productor> comboProductores;
    private JButton btnAgregar, btnActualizar, btnLimpiar, btnBuscar, btnRefrescar;
    
    private LugarProduccionController controller;

    public GestionLugaresProduccionProductor() {
        this.controller = new LugarProduccionController();
        initComponents();
        cargarDatos();
        cargarProductores();
    }

    private void initComponents() {
        setTitle("Gestión de Lugares de Producción - Modo Productor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        JPanel panelFormulario = crearPanelFormulario();
        JPanel panelBotonesPrincipales = crearPanelBotonesPrincipales();
        JScrollPane scrollTabla = crearScrollTabla();

        panelPrincipal.add(panelFormulario, BorderLayout.WEST);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotonesPrincipales, BorderLayout.SOUTH);

        add(panelPrincipal);
        agregarActionListeners();
    }

    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "Datos del Lugar de Producción"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(400, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Campo ID (OCULTO - solo para uso interno)
        txtId = new JTextField();
        txtId.setVisible(false); // Oculta el campo ID

        // Campo Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre:*"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre, gbc);

        // Campo Registro ICA
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Registro ICA:"), gbc);
        gbc.gridx = 1;
        txtRegistroICA = new JTextField();
        panelFormulario.add(txtRegistroICA, gbc);

        // Campo Dirección
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        txtDireccion = new JTextField();
        panelFormulario.add(txtDireccion, gbc);

        // Combo Box para Productor
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Productor:*"), gbc);
        gbc.gridx = 1;
        comboProductores = new JComboBox<>();
        comboProductores.setBackground(Color.WHITE);
        panelFormulario.add(comboProductores, gbc);

        // Panel inferior con botones de formulario (Limpiar y Buscar)
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

    private JScrollPane crearScrollTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // QUITAR la columna ID - solo mostrar datos visibles al usuario
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Registro ICA");
        modeloTabla.addColumn("Dirección");
        modeloTabla.addColumn("Productor");

        tablaLugares = new JTable(modeloTabla);
        tablaLugares.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaLugares.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaLugares.getTableHeader().setBackground(new Color(155, 89, 182));
        tablaLugares.getTableHeader().setForeground(Color.WHITE);
        tablaLugares.setRowHeight(25);
        
        tablaLugares.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    c.setBackground(new Color(41, 128, 185));
                    c.setForeground(Color.WHITE);
                } else {
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

        JScrollPane scrollTabla = new JScrollPane(tablaLugares);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "Lista de Lugares de Producción"
        ));

        return scrollTabla;
    }

    private JPanel crearPanelBotonesPrincipales() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnAgregar = crearBoton("Agregar Lugar", new Color(39, 174, 96));
        btnActualizar = crearBoton("Actualizar Lugar", new Color(41, 128, 185));
        btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182));
        
        btnAgregar.setToolTipText("Agregar un nuevo lugar de producción a la base de datos");
        btnActualizar.setToolTipText("Actualizar el lugar de producción seleccionado");
        btnRefrescar.setToolTipText("Actualizar la tabla con los últimos datos");
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
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
        btnAgregar.addActionListener(e -> agregarLugar());
        btnActualizar.addActionListener(e -> actualizarLugar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarLugares());
        btnRefrescar.addActionListener(e -> cargarDatos());

        tablaLugares.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaLugares.getSelectedRow() != -1) {
                seleccionarLugarDeTabla();
            }
        });
    }

    private void cargarDatos() {
        limpiarTabla();
        List<LugarProduccion> lugares = controller.obtenerTodosLugaresProduccion();
        
        if (lugares.isEmpty()) {
            mostrarMensajeInformacion("No hay lugares de producción registrados en la base de datos.");
        } else {
            for (LugarProduccion lugar : lugares) {
                Object[] fila = {
                    // NO incluir el ID en la tabla - solo datos visibles
                    lugar.getNombreLugar(),
                    lugar.getNumeroRegistroICA(),
                    lugar.getDireccionLugar(),
                    lugar.getNombreProductor()
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeInformacion("Se cargaron " + lugares.size() + " lugar(es) de producción desde la base de datos.");
        }
    }

    private void cargarProductores() {
        List<Productor> productores = controller.obtenerTodosProductores();
        comboProductores.removeAllItems();
        for (Productor productor : productores) {
            comboProductores.addItem(productor);
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        txtId.setText(""); // Limpiar pero mantener oculto
        txtNombre.setText("");
        txtRegistroICA.setText("");
        txtDireccion.setText("");
        comboProductores.setSelectedIndex(-1);
        tablaLugares.clearSelection();
        txtNombre.requestFocus();
    }

    private void seleccionarLugarDeTabla() {
        int filaSeleccionada = tablaLugares.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // Obtener todos los lugares para encontrar el ID correspondiente
            List<LugarProduccion> lugares = controller.obtenerTodosLugaresProduccion();
            if (filaSeleccionada < lugares.size()) {
                LugarProduccion lugarSeleccionado = lugares.get(filaSeleccionada);
                txtId.setText(String.valueOf(lugarSeleccionado.getIdLugarProduccion()));
                txtNombre.setText(lugarSeleccionado.getNombreLugar());
                txtRegistroICA.setText(lugarSeleccionado.getNumeroRegistroICA());
                txtDireccion.setText(lugarSeleccionado.getDireccionLugar());
                seleccionarProductorEnCombo(lugarSeleccionado.getNombreProductor());
            }
        }
    }

    private void seleccionarProductorEnCombo(String nombreProductor) {
        for (int i = 0; i < comboProductores.getItemCount(); i++) {
            Productor productor = comboProductores.getItemAt(i);
            if (productor.getNombreCompleto().equals(nombreProductor)) {
                comboProductores.setSelectedIndex(i);
                break;
            }
        }
    }

    private void agregarLugar() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            String nombre = txtNombre.getText().trim();
            String registroICA = txtRegistroICA.getText().trim();

            // Validar duplicados antes de insertar
            if (controller.existeNombreLugar(nombre)) {
                mostrarMensajeError("Ya existe un lugar de producción con el nombre: " + nombre);
                txtNombre.requestFocus();
                return;
            }

            if (!registroICA.isEmpty() && controller.existeRegistroICA(registroICA)) {
                mostrarMensajeError("Ya existe un lugar de producción con el registro ICA: " + registroICA);
                txtRegistroICA.requestFocus();
                return;
            }

            LugarProduccion nuevoLugar = new LugarProduccion();
            nuevoLugar.setNombreLugar(nombre);
            nuevoLugar.setNumeroRegistroICA(registroICA);
            nuevoLugar.setDireccionLugar(txtDireccion.getText().trim());

            Productor productorSeleccionado = (Productor) comboProductores.getSelectedItem();
            nuevoLugar.setIdProductor(productorSeleccionado.getIdProductor());

            if (controller.agregarLugarProduccion(nuevoLugar)) {
                mostrarMensajeExito("Lugar de producción agregado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar el lugar de producción a la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void actualizarLugar() {
        if (txtId.getText().isEmpty()) {
            mostrarMensajeError("Seleccione un lugar de la tabla para actualizar");
            return;
        }

        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            int idLugar = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText().trim();
            String registroICA = txtRegistroICA.getText().trim();

            // Validar duplicados excluyendo el ID actual
            if (controller.existeNombreLugarExcluyendoId(nombre, idLugar)) {
                mostrarMensajeError("Ya existe otro lugar de producción con el nombre: " + nombre);
                txtNombre.requestFocus();
                return;
            }

            if (!registroICA.isEmpty() && controller.existeRegistroICAExcluyendoId(registroICA, idLugar)) {
                mostrarMensajeError("Ya existe otro lugar de producción con el registro ICA: " + registroICA);
                txtRegistroICA.requestFocus();
                return;
            }

            LugarProduccion lugar = new LugarProduccion();
            lugar.setIdLugarProduccion(idLugar);
            lugar.setNombreLugar(nombre);
            lugar.setNumeroRegistroICA(registroICA);
            lugar.setDireccionLugar(txtDireccion.getText().trim());

            Productor productorSeleccionado = (Productor) comboProductores.getSelectedItem();
            lugar.setIdProductor(productorSeleccionado.getIdProductor());

            if (controller.actualizarLugarProduccion(lugar)) {
                mostrarMensajeExito("Lugar de producción actualizado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar el lugar de producción en la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void buscarLugares() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre, registro ICA, dirección o nombre del productor a buscar:",
            "Buscar Lugares de Producción",
            JOptionPane.QUESTION_MESSAGE);

        if (criterio != null && !criterio.trim().isEmpty()) {
            try {
                List<LugarProduccion> resultados = controller.buscarLugaresProduccion(criterio.trim());
                limpiarTabla();
                
                if (resultados.isEmpty()) {
                    mostrarMensajeInformacion("No se encontraron lugares con el criterio: " + criterio);
                } else {
                    for (LugarProduccion lugar : resultados) {
                        Object[] fila = {
                            lugar.getNombreLugar(),
                            lugar.getNumeroRegistroICA(),
                            lugar.getDireccionLugar(),
                            lugar.getNombreProductor()
                        };
                        modeloTabla.addRow(fila);
                    }
                    mostrarMensajeExito("Se encontraron " + resultados.size() + " lugar(es)");
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
            mostrarMensajeError("El campo Nombre es obligatorio");
            txtNombre.requestFocus();
            return false;
        }

        if (comboProductores.getSelectedItem() == null) {
            mostrarMensajeError("Debe seleccionar un productor");
            comboProductores.requestFocus();
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
            new GestionLugaresProduccionProductor().setVisible(true);
        });
    }
}