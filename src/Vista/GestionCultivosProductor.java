package vista;

import controlador.CultivoController;
import modelo.Cultivo;
import modelo.Predio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import static java.lang.Integer.parseInt;
import java.util.List;

public class GestionCultivosProductor extends JFrame {
    
    private JTable tablaCultivos;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNombreCultivo, txtPlantasAfectadas, txtTotalPlantas, txtEstadoPlanta;
    private JComboBox<String> comboPredios;
    private JButton btnAgregar, btnActualizar, btnLimpiar, btnBuscar, btnRefrescar;
    private CultivoController controller;
    
    private java.util.Map<String, Integer> mapaPredios;

    public GestionCultivosProductor() {
        this.controller = new CultivoController();
        this.mapaPredios = new java.util.HashMap<>();
        initComponents();
        cargarPredios();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Gestión de Cultivos - Modo Productor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 600); // Tamaño aumentado para nueva columna
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
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            "Datos del Cultivo"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(350, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Campo ID (OCULTO - solo para uso interno)
        txtId = new JTextField();
        txtId.setVisible(false);

        // Campo Nombre Cultivo
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre Cultivo:*"), gbc);
        gbc.gridx = 1;
        txtNombreCultivo = new JTextField();
        panelFormulario.add(txtNombreCultivo, gbc);

        // ✅ NUEVO CAMPO: Total Plantas
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Total Plantas:*"), gbc);
        gbc.gridx = 1;
        txtTotalPlantas = new JTextField();
        panelFormulario.add(txtTotalPlantas, gbc);

        // Campo Plantas Afectadas
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Plantas Afectadas:"), gbc);
        gbc.gridx = 1;
        txtPlantasAfectadas = new JTextField();
        panelFormulario.add(txtPlantasAfectadas, gbc);

        // Campo Estado Planta
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Estado Planta:"), gbc);
        gbc.gridx = 1;
        txtEstadoPlanta = new JTextField();
        panelFormulario.add(txtEstadoPlanta, gbc);

        // Combo Box para Predio
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(new JLabel("Predio:*"), gbc);
        gbc.gridx = 1;
        comboPredios = new JComboBox<>();
        comboPredios.addItem("Seleccione un predio");
        comboPredios.setBackground(Color.WHITE);
        panelFormulario.add(comboPredios, gbc);

        // Panel inferior con botones de formulario
        gbc.gridx = 0; gbc.gridy = 5;
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
        
        // ✅ COLUMNAS ACTUALIZADAS CON "TOTAL PLANTAS"
        modeloTabla.addColumn("Nombre Cultivo");
        modeloTabla.addColumn("Total Plantas"); // ✅ NUEVA COLUMNA
        modeloTabla.addColumn("Plantas Afectadas");
        modeloTabla.addColumn("Estado Planta");
        modeloTabla.addColumn("Predio");

        tablaCultivos = new JTable(modeloTabla);
        tablaCultivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCultivos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaCultivos.getTableHeader().setBackground(new Color(46, 204, 113));
        tablaCultivos.getTableHeader().setForeground(Color.WHITE);
        tablaCultivos.setRowHeight(25);
        
        tablaCultivos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        JScrollPane scrollTabla = new JScrollPane(tablaCultivos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            "Lista de Cultivos"
        ));

        return scrollTabla;
    }

    private JPanel crearPanelBotonesPrincipales() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnAgregar = crearBoton("Agregar Cultivo", new Color(39, 174, 96));
        btnActualizar = crearBoton("Actualizar Cultivo", new Color(41, 128, 185));
        btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182));
        
        btnAgregar.setToolTipText("Agregar un nuevo cultivo a la base de datos");
        btnActualizar.setToolTipText("Actualizar el cultivo seleccionado");
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
        btnAgregar.addActionListener(e -> agregarCultivo());
        btnActualizar.addActionListener(e -> actualizarCultivo());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarCultivos());
        btnRefrescar.addActionListener(e -> {
            cargarPredios();
            cargarDatos();
        });

        tablaCultivos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaCultivos.getSelectedRow() != -1) {
                seleccionarCultivoDeTabla();
            }
        });
    }

    private void cargarPredios() {
        comboPredios.removeAllItems();
        comboPredios.addItem("Seleccione un predio");
        mapaPredios.clear();
        
        List<Predio> predios = controller.obtenerTodosPrediosParaCultivo();
        
        // Ordenar los predios alfabéticamente por nombre
        predios.sort((p1, p2) -> p1.getNombrePredio().compareToIgnoreCase(p2.getNombrePredio()));
        
        for (Predio predio : predios) {
            String nombrePredio = predio.getNombrePredio();
            comboPredios.addItem(nombrePredio);
            mapaPredios.put(nombrePredio, predio.getIdPredio());
        }
        
        if (predios.isEmpty()) {
            mostrarMensajeInformacion("No hay predios registrados. Debe agregar predios primero.");
        }
    }

    private int obtenerIdPredioSeleccionado() {
        String predioSeleccionado = (String) comboPredios.getSelectedItem();
        if (predioSeleccionado != null && !predioSeleccionado.equals("Seleccione un predio")) {
            return mapaPredios.get(predioSeleccionado);
        }
        return -1;
    }

    private void seleccionarPredioEnCombo(String nombrePredio) {
        for (int i = 0; i < comboPredios.getItemCount(); i++) {
            if (comboPredios.getItemAt(i).equals(nombrePredio)) {
                comboPredios.setSelectedIndex(i);
                return;
            }
        }
        comboPredios.setSelectedIndex(0);
    }

    private String obtenerNombrePredio(int idPredio) {
        try {
            Predio predio = controller.obtenerPredio(idPredio);
            return (predio != null) ? predio.getNombrePredio() : "Predio ID: " + idPredio;
        } catch (Exception e) {
            return "Error cargando predio";
        }
    }

    private void cargarDatos() {
        limpiarTabla();
        List<Cultivo> cultivos = controller.obtenerTodosCultivos();
        
        if (cultivos.isEmpty()) {
            mostrarMensajeInformacion("No hay cultivos registrados en la base de datos.");
        } else {
            for (Cultivo cultivo : cultivos) {
                String nombrePredio = obtenerNombrePredio(cultivo.getIdPredio());
                
                Object[] fila = {
                    cultivo.getNombreCultivo(),
                    cultivo.getTotalPlantas(), // ✅ NUEVO DATO EN TABLA
                    cultivo.getPlantasAfectadas(),
                    cultivo.getEstadoPlanta(),
                    nombrePredio
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeInformacion("Se cargaron " + cultivos.size() + " cultivo(s) desde la base de datos.");
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombreCultivo.setText("");
        txtTotalPlantas.setText(""); // ✅ LIMPIAR NUEVO CAMPO
        txtPlantasAfectadas.setText("");
        txtEstadoPlanta.setText("");
        comboPredios.setSelectedIndex(0);
        tablaCultivos.clearSelection();
        txtNombreCultivo.requestFocus();
    }

    private void seleccionarCultivoDeTabla() {
        int filaSeleccionada = tablaCultivos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
                // Obtener datos visibles de la tabla
                String nombreCultivo = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
                String totalPlantas = modeloTabla.getValueAt(filaSeleccionada, 1).toString(); // ✅ NUEVO DATO
                String plantasAfectadas = modeloTabla.getValueAt(filaSeleccionada, 2).toString();
                String estado = modeloTabla.getValueAt(filaSeleccionada, 3).toString();
                String nombrePredio = modeloTabla.getValueAt(filaSeleccionada, 4).toString();

                // Buscar el cultivo para obtener el ID
                List<Cultivo> cultivos = controller.buscarCultivos(nombreCultivo);
                Cultivo cultivoSeleccionado = null;
                
                for (Cultivo cultivo : cultivos) {
                    String nombrePredioActual = obtenerNombrePredio(cultivo.getIdPredio());
                    if (nombrePredioActual.equals(nombrePredio)) {
                        cultivoSeleccionado = cultivo;
                        break;
                    }
                }

                if (cultivoSeleccionado != null) {
                    txtId.setText(String.valueOf(cultivoSeleccionado.getIdCultivo()));
                    txtNombreCultivo.setText(cultivoSeleccionado.getNombreCultivo());
                    txtTotalPlantas.setText(String.valueOf(cultivoSeleccionado.getTotalPlantas())); // ✅ NUEVO CAMPO
                    txtPlantasAfectadas.setText(String.valueOf(cultivoSeleccionado.getPlantasAfectadas()));
                    txtEstadoPlanta.setText(cultivoSeleccionado.getEstadoPlanta());
                    seleccionarPredioEnCombo(obtenerNombrePredio(cultivoSeleccionado.getIdPredio()));
                }
            } catch (Exception e) {
                mostrarMensajeError("Error al cargar el cultivo seleccionado: " + e.getMessage());
            }
        }
    }

    private void agregarCultivo() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            String nombre = txtNombreCultivo.getText().trim();
            int idPredio = obtenerIdPredioSeleccionado();

            // Validar duplicados antes de insertar
            if (controller.existeNombreCultivo(nombre, idPredio)) {
                mostrarMensajeError("Ya existe un cultivo con el nombre '" + nombre + "' en el predio seleccionado");
                txtNombreCultivo.requestFocus();
                return;
            }

            Cultivo nuevoCultivo = new Cultivo();
            nuevoCultivo.setNombreCultivo(nombre);
            
            // ✅ VALIDAR Y ESTABLECER TOTAL_PLANTAS
            String totalPlantasText = txtTotalPlantas.getText().trim();
            if (totalPlantasText.isEmpty()) {
                mostrarMensajeError("El campo Total Plantas es obligatorio");
                txtTotalPlantas.requestFocus();
                return;
            }
            nuevoCultivo.setTotalPlantas(parseInt(totalPlantasText));
            
            // Manejar plantas afectadas (puede estar vacío)
            String plantasAfectadasText = txtPlantasAfectadas.getText().trim();
            if (plantasAfectadasText.isEmpty()) {
                nuevoCultivo.setPlantasAfectadas(0);
            } else {
                nuevoCultivo.setPlantasAfectadas(parseInt(plantasAfectadasText));
            }
            
            nuevoCultivo.setEstadoPlanta(txtEstadoPlanta.getText().trim());
            nuevoCultivo.setIdPredio(idPredio);

            if (controller.agregarCultivo(nuevoCultivo)) {
                mostrarMensajeExito("Cultivo agregado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar el cultivo a la base de datos");
            }
        } catch (NumberFormatException ex) {
            mostrarMensajeError("Los campos numéricos deben contener valores válidos");
            txtTotalPlantas.requestFocus();
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void actualizarCultivo() {
        if (txtId.getText().isEmpty()) {
            mostrarMensajeError("Seleccione un cultivo de la tabla para actualizar");
            return;
        }

        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            int idCultivo = Integer.parseInt(txtId.getText());
            String nombre = txtNombreCultivo.getText().trim();
            int idPredio = obtenerIdPredioSeleccionado();

            // Validar duplicados excluyendo el ID actual
            if (controller.existeNombreCultivoExcluyendoId(nombre, idPredio, idCultivo)) {
                mostrarMensajeError("Ya existe otro cultivo con el nombre '" + nombre + "' en el predio seleccionado");
                txtNombreCultivo.requestFocus();
                return;
            }

            Cultivo cultivo = new Cultivo();
            cultivo.setIdCultivo(idCultivo);
            cultivo.setNombreCultivo(nombre);
            
            // ✅ VALIDAR Y ESTABLECER TOTAL_PLANTAS
            String totalPlantasText = txtTotalPlantas.getText().trim();
            if (totalPlantasText.isEmpty()) {
                mostrarMensajeError("El campo Total Plantas es obligatorio");
                txtTotalPlantas.requestFocus();
                return;
            }
            cultivo.setTotalPlantas(parseInt(totalPlantasText));
            
            // Manejar plantas afectadas (puede estar vacío)
            String plantasAfectadasText = txtPlantasAfectadas.getText().trim();
            if (plantasAfectadasText.isEmpty()) {
                cultivo.setPlantasAfectadas(0);
            } else {
                cultivo.setPlantasAfectadas(parseInt(plantasAfectadasText));
            }
            
            cultivo.setEstadoPlanta(txtEstadoPlanta.getText().trim());
            cultivo.setIdPredio(idPredio);

            if (controller.actualizarCultivo(cultivo)) {
                mostrarMensajeExito("Cultivo actualizado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar el cultivo en la base de datos");
            }
        } catch (NumberFormatException ex) {
            mostrarMensajeError("Los campos numéricos deben contener valores válidos");
            txtTotalPlantas.requestFocus();
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void buscarCultivos() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre, estado de planta o predio a buscar:",
            "Buscar Cultivos",
            JOptionPane.QUESTION_MESSAGE);

        if (criterio != null && !criterio.trim().isEmpty()) {
            try {
                List<Cultivo> resultados = controller.buscarCultivos(criterio.trim());
                limpiarTabla();
                
                if (resultados.isEmpty()) {
                    mostrarMensajeInformacion("No se encontraron cultivos con el criterio: " + criterio);
                } else {
                    for (Cultivo cultivo : resultados) {
                        String nombrePredio = obtenerNombrePredio(cultivo.getIdPredio());
                        
                        Object[] fila = {
                            cultivo.getNombreCultivo(),
                            cultivo.getTotalPlantas(), // ✅ NUEVO DATO EN BÚSQUEDA
                            cultivo.getPlantasAfectadas(),
                            cultivo.getEstadoPlanta(),
                            nombrePredio
                        };
                        modeloTabla.addRow(fila);
                    }
                    mostrarMensajeExito("Se encontraron " + resultados.size() + " cultivo(s)");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error durante la búsqueda: " + ex.getMessage());
            }
        } else if (criterio != null) {
            cargarDatos();
        }
    }

    private boolean validarCamposObligatorios() {
        if (txtNombreCultivo.getText().trim().isEmpty()) {
            mostrarMensajeError("El campo Nombre Cultivo es obligatorio");
            txtNombreCultivo.requestFocus();
            return false;
        }

        if (txtTotalPlantas.getText().trim().isEmpty()) { // ✅ NUEVA VALIDACIÓN
            mostrarMensajeError("El campo Total Plantas es obligatorio");
            txtTotalPlantas.requestFocus();
            return false;
        }

        if (obtenerIdPredioSeleccionado() == -1) {
            mostrarMensajeError("Debe seleccionar un predio");
            comboPredios.requestFocus();
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
            new GestionCultivosProductor().setVisible(true);
        });
    }
}