package vista;

import controlador.PredioController;
import modelo.Predio;
import modelo.Municipio;
import controlador.MunicipioController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class GestionPrediosProductor extends JFrame {
    private JTable tablaPredios;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombrePredio, txtNombrePropietario;
    private JComboBox<String> comboMunicipios;
    private JButton btnAgregar, btnActualizar, btnLimpiar, btnBuscar, btnRefrescar;
    private PredioController controller;
    private MunicipioController municipioController;
    
    // Lista para almacenar los municipios y sus IDs
    private java.util.Map<String, Integer> mapaMunicipios;
    // Variable para almacenar el ID del predio seleccionado internamente
    private int idPredioSeleccionado = -1;

    public GestionPrediosProductor() {
        this.controller = new PredioController();
        this.municipioController = new MunicipioController();
        this.mapaMunicipios = new java.util.HashMap<>();
        initComponents();
        cargarMunicipios();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Gestión de Predios - Modo Productor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
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
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Datos del Predio"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(350, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Campo Nombre Predio (obligatorio)
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre Predio:*"), gbc);
        gbc.gridx = 1;
        txtNombrePredio = new JTextField();
        panelFormulario.add(txtNombrePredio, gbc);

        // Campo Nombre Propietario (obligatorio)
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombre Propietario:*"), gbc);
        gbc.gridx = 1;
        txtNombrePropietario = new JTextField();
        panelFormulario.add(txtNombrePropietario, gbc);

        // Campo Municipio (ComboBox en lugar de texto)
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Municipio:*"), gbc);
        gbc.gridx = 1;
        comboMunicipios = new JComboBox<>();
        comboMunicipios.addItem("Seleccione un municipio");
        panelFormulario.add(comboMunicipios, gbc);

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

    private JScrollPane crearScrollTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Definición de columnas de la tabla (sin ID Predio)
        modeloTabla.addColumn("Nombre Predio");
        modeloTabla.addColumn("Nombre Propietario");
        modeloTabla.addColumn("Municipio");

        // Configuración de la tabla
        tablaPredios = new JTable(modeloTabla);
        tablaPredios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPredios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaPredios.getTableHeader().setBackground(new Color(70, 130, 180));
        tablaPredios.getTableHeader().setForeground(Color.WHITE);
        tablaPredios.setRowHeight(25);
        
        // Renderer para filas alternadas
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

    private JPanel crearPanelBotonesPrincipales() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnAgregar = crearBoton("Agregar Predio", new Color(39, 174, 96));
        btnActualizar = crearBoton("Actualizar Predio", new Color(41, 128, 185));
        btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182));
        
        btnAgregar.setToolTipText("Agregar un nuevo predio a la base de datos");
        btnActualizar.setToolTipText("Actualizar el predio seleccionado");
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
        btnAgregar.addActionListener(e -> agregarPredio());
        btnActualizar.addActionListener(e -> actualizarPredio());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarPredios());
        btnRefrescar.addActionListener(e -> {
            cargarMunicipios();
            cargarDatos();
        });

        tablaPredios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaPredios.getSelectedRow() != -1) {
                seleccionarPredioDeTabla();
            }
        });
    }

    /**
     * Carga los municipios desde la base de datos al ComboBox ordenados alfabéticamente
     */
    private void cargarMunicipios() {
        comboMunicipios.removeAllItems();
        comboMunicipios.addItem("Seleccione un municipio");
        mapaMunicipios.clear();
        
        List<Municipio> municipios = municipioController.obtenerTodosMunicipios();
        
        // Ordenar los municipios alfabéticamente por nombre
        municipios.sort((m1, m2) -> m1.getNombre().compareToIgnoreCase(m2.getNombre()));
        
        for (Municipio municipio : municipios) {
            String nombreMunicipio = municipio.getNombre();
            comboMunicipios.addItem(nombreMunicipio);
            mapaMunicipios.put(nombreMunicipio, municipio.getIdMunicipio());
        }
        
        if (municipios.isEmpty()) {
            mostrarMensajeInformacion("No hay municipios registrados. Debe agregar municipios primero.");
        }
    }

    /**
     * Obtiene el ID del municipio seleccionado en el ComboBox
     * 
     * @return ID del municipio seleccionado, o -1 si no hay selección válida
     */
    private int obtenerIdMunicipioSeleccionado() {
        String municipioSeleccionado = (String) comboMunicipios.getSelectedItem();
        if (municipioSeleccionado != null && !municipioSeleccionado.equals("Seleccione un municipio")) {
            return mapaMunicipios.get(municipioSeleccionado);
        }
        return -1;
    }

    /**
     * Establece la selección del ComboBox basado en el nombre del municipio
     * 
     * @param nombreMunicipio Nombre del municipio a seleccionar
     */
    private void seleccionarMunicipioEnCombo(String nombreMunicipio) {
        for (int i = 0; i < comboMunicipios.getItemCount(); i++) {
            if (comboMunicipios.getItemAt(i).equals(nombreMunicipio)) {
                comboMunicipios.setSelectedIndex(i);
                return;
            }
        }
        comboMunicipios.setSelectedIndex(0); // Seleccionar "Seleccione un municipio" si no se encuentra
    }

    /**
     * Método auxiliar para obtener el nombre del municipio
     */
    private String obtenerNombreMunicipio(int idMunicipio) {
        Municipio municipio = municipioController.obtenerMunicipio(idMunicipio);
        return municipio != null ? municipio.getNombre() : "Municipio no encontrado";
    }

    private void cargarDatos() {
        limpiarTabla();
        List<Predio> predios = controller.obtenerTodosPredios();
        
        if (predios.isEmpty()) {
            mostrarMensajeInformacion("No hay predios registrados en la base de datos.");
        } else {
            // Ordenar predios por nombre de municipio y luego por nombre de predio
            predios.sort((p1, p2) -> {
                String municipio1 = obtenerNombreMunicipio(p1.getIdMunicipio());
                String municipio2 = obtenerNombreMunicipio(p2.getIdMunicipio());
                int comparacionMunicipio = municipio1.compareToIgnoreCase(municipio2);
                if (comparacionMunicipio != 0) {
                    return comparacionMunicipio;
                }
                return p1.getNombrePredio().compareToIgnoreCase(p2.getNombrePredio());
            });
            
            for (Predio predio : predios) {
                String nombreMunicipio = obtenerNombreMunicipio(predio.getIdMunicipio());
                
                Object[] fila = {
                    predio.getNombrePredio(),
                    predio.getNombrePropietario(),
                    nombreMunicipio
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
        txtNombrePredio.setText("");
        txtNombrePropietario.setText("");
        comboMunicipios.setSelectedIndex(0);
        tablaPredios.clearSelection();
        idPredioSeleccionado = -1; // Reiniciar ID seleccionado
        txtNombrePredio.requestFocus();
    }

    private void seleccionarPredioDeTabla() {
        int filaSeleccionada = tablaPredios.getSelectedRow();
        if (filaSeleccionada >= 0) {
            txtNombrePredio.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtNombrePropietario.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            
            // Obtener y seleccionar el municipio en el ComboBox
            String nombreMunicipio = modeloTabla.getValueAt(filaSeleccionada, 2).toString();
            seleccionarMunicipioEnCombo(nombreMunicipio);
            
            // Buscar el predio para obtener su ID internamente
            String nombrePredioSeleccionado = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
            String nombrePropietarioSeleccionado = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
            
            List<Predio> predios = controller.buscarPredios(nombrePredioSeleccionado);
            for (Predio predio : predios) {
                if (predio.getNombrePredio().equals(nombrePredioSeleccionado) && 
                    predio.getNombrePropietario().equals(nombrePropietarioSeleccionado)) {
                    idPredioSeleccionado = predio.getIdPredio();
                    break;
                }
            }
        }
    }

    private void agregarPredio() {
    if (!validarCamposObligatorios()) {
        return;
    }

    try {
        Predio nuevoPredio = new Predio();
        nuevoPredio.setNombrePredio(txtNombrePredio.getText().trim());
        nuevoPredio.setNombrePropietario(txtNombrePropietario.getText().trim());
        
        // ✅ CORREGIDO: Obtener el ID del municipio y establecerlo en el objeto Predio
        int idMunicipio = obtenerIdMunicipioSeleccionado();
        if (idMunicipio == -1) {
            mostrarMensajeError("Error: No se pudo obtener el ID del municipio seleccionado");
            return;
        }
        nuevoPredio.setIdMunicipio(idMunicipio);

        // ✅ CORREGIDO: Usar el método correcto del controlador (solo el objeto Predio)
        if (controller.agregarPredio(nuevoPredio)) {
            mostrarMensajeExito("Predio agregado exitosamente");
            cargarDatos();
            limpiarFormulario();
        } else {
            mostrarMensajeError("Error al agregar el predio a la base de datos");
        }
    } catch (Exception ex) {
        mostrarMensajeError("Error inesperado: " + ex.getMessage());
        ex.printStackTrace();
    }
}

    private void actualizarPredio() {
        if (idPredioSeleccionado == -1) {
            mostrarMensajeError("Seleccione un predio de la tabla para actualizar");
            return;
        }

        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Predio predio = new Predio();
            predio.setIdPredio(idPredioSeleccionado);
            predio.setNombrePredio(txtNombrePredio.getText().trim());
            predio.setNombrePropietario(txtNombrePropietario.getText().trim());
            predio.setIdMunicipio(obtenerIdMunicipioSeleccionado());

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
                    // Ordenar resultados por municipio y nombre de predio
                    resultados.sort((p1, p2) -> {
                        String municipio1 = obtenerNombreMunicipio(p1.getIdMunicipio());
                        String municipio2 = obtenerNombreMunicipio(p2.getIdMunicipio());
                        int comparacionMunicipio = municipio1.compareToIgnoreCase(municipio2);
                        if (comparacionMunicipio != 0) {
                            return comparacionMunicipio;
                        }
                        return p1.getNombrePredio().compareToIgnoreCase(p2.getNombrePredio());
                    });
                    
                    for (Predio predio : resultados) {
                        String nombreMunicipio = obtenerNombreMunicipio(predio.getIdMunicipio());
                        
                        Object[] fila = {
                            predio.getNombrePredio(),
                            predio.getNombrePropietario(),
                            nombreMunicipio
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
        if (txtNombrePredio.getText().trim().isEmpty() || 
            txtNombrePropietario.getText().trim().isEmpty()) {
            mostrarMensajeError("Los campos Nombre Predio y Nombre Propietario son obligatorios");
            if (txtNombrePredio.getText().trim().isEmpty()) {
                txtNombrePredio.requestFocus();
            } else {
                txtNombrePropietario.requestFocus();
            }
            return false;
        }
        
        if (obtenerIdMunicipioSeleccionado() == -1) {
            mostrarMensajeError("Debe seleccionar un municipio");
            comboMunicipios.requestFocus();
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
            new GestionPrediosProductor().setVisible(true);
        });
    }
}