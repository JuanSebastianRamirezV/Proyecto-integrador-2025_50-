package vista;

import controlador.InspeccionController;
import controlador.InspectorController;
import modelo.Inspeccion;
import modelo.Inspector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ConsultaInspeccionesProductor extends JFrame {
    private JTable tablaInspecciones;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JComboBox<String> cmbFiltro;
    private JButton btnBuscar, btnRefrescar, btnCerrar, btnLimpiar;
    private InspeccionController controller;
    private InspectorController inspectorController;
    
    // Variable para almacenar el ID de la inspección seleccionada internamente
    private int idInspeccionSeleccionada = -1;

    public ConsultaInspeccionesProductor() {
        this.controller = new InspeccionController();
        this.inspectorController = new InspectorController();
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Consulta de Inspecciones - Modo Productor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650); // Reducido el ancho
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Panel de búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();

        // Crear tabla
        JScrollPane scrollTabla = crearScrollTabla();

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();

        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
        agregarActionListeners();
    }

    private JPanel crearPanelBusqueda() {
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(230, 126, 34), 1),
            "Opciones de Consulta"
        ));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setPreferredSize(new Dimension(0, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta de filtro
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        panelBusqueda.add(new JLabel("Filtrar por:"), gbc);

        // Combo de filtros
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.2;
        cmbFiltro = new JComboBox<>(new String[]{
            "Todos", "Estado", "Fecha", "Inspector", "Observaciones"
        }); // Eliminado "Cultivo"
        panelBusqueda.add(cmbFiltro, gbc);

        // Campo de búsqueda
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.weightx = 0.6;
        txtBuscar = new JTextField();
        txtBuscar.setToolTipText("Ingrese el criterio de búsqueda");
        panelBusqueda.add(txtBuscar, gbc);

        // Botón buscar
        gbc.gridx = 3; gbc.gridy = 0;
        gbc.weightx = 0.1;
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        panelBusqueda.add(btnBuscar, gbc);

        // Información
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        JLabel lblInfo = new JLabel("Puede consultar inspecciones por estado, fecha, inspector u observaciones");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(Color.GRAY);
        panelBusqueda.add(lblInfo, gbc);

        return panelBusqueda;
    }

    private JScrollPane crearScrollTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura
            }
        };
        
        // CAMBIO: Eliminar columna Cultivo
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Observaciones");
        modeloTabla.addColumn("Inspector");
        // Eliminada columna "Cultivo"

        // Configuración de la tabla
        tablaInspecciones = new JTable(modeloTabla);
        tablaInspecciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInspecciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaInspecciones.getTableHeader().setBackground(new Color(230, 126, 34));
        tablaInspecciones.getTableHeader().setForeground(Color.WHITE);
        tablaInspecciones.setRowHeight(25);
        
        // Renderer para filas alternadas
        tablaInspecciones.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                    c.setForeground(Color.BLACK);
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
            "Resultados de la Consulta (Solo Lectura)"
        ));

        return scrollTabla;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnLimpiar = crearBoton("Limpiar Búsqueda", new Color(243, 156, 18));
        btnRefrescar = crearBoton("Refrescar Datos", new Color(52, 152, 219));
        btnCerrar = crearBoton("Cerrar", new Color(192, 57, 43));
        
        btnLimpiar.setToolTipText("Limpiar criterios de búsqueda");
        btnRefrescar.setToolTipText("Actualizar la tabla con todos los datos");
        btnCerrar.setToolTipText("Cerrar la ventana de consulta");

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnCerrar);

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
        btnBuscar.addActionListener(e -> buscarInspecciones());
        btnLimpiar.addActionListener(e -> limpiarBusqueda());
        btnRefrescar.addActionListener(e -> cargarDatos());
        btnCerrar.addActionListener(e -> dispose());
        
        // Buscar al presionar Enter en el campo de texto
        txtBuscar.addActionListener(e -> buscarInspecciones());
        
        // Listener para selección en la tabla
        tablaInspecciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaInspecciones.getSelectedRow() != -1) {
                seleccionarInspeccionDeTabla();
            }
        });
    }

    private void seleccionarInspeccionDeTabla() {
        int filaSeleccionada = tablaInspecciones.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // CAMBIO: Obtener datos visibles para buscar el ID internamente
            String fecha = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
            String estado = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
            String inspectorNombre = modeloTabla.getValueAt(filaSeleccionada, 3).toString();
            
            // Buscar la inspección por los datos visibles para obtener el ID internamente
            List<Inspeccion> inspecciones = controller.buscarInspecciones(estado);
            for (Inspeccion inspeccion : inspecciones) {
                if (new SimpleDateFormat("yyyy-MM-dd").format(inspeccion.getFechaInspeccion()).equals(fecha)) {
                    idInspeccionSeleccionada = inspeccion.getIdInspeccion();
                    break;
                }
            }
            
            // Mostrar información detallada en un mensaje (opcional)
            String observaciones = modeloTabla.getValueAt(filaSeleccionada, 2).toString();
            
            String mensaje = String.format(
                "Detalles de la inspección seleccionada:\n\n" +
                "Fecha: %s\n" +
                "Estado: %s\n" +
                "Inspector: %s\n" +
                "Observaciones: %s",
                fecha, estado, inspectorNombre,
                observaciones.isEmpty() ? "Sin observaciones" : observaciones
            );
            
            JOptionPane.showMessageDialog(this, mensaje, "Detalles de Inspección", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarInspecciones() {
        String criterio = txtBuscar.getText().trim();
        String tipoFiltro = (String) cmbFiltro.getSelectedItem();
        
        if ("Todos".equals(tipoFiltro) || criterio.isEmpty()) {
            cargarDatos();
            return;
        }

        try {
            List<Inspeccion> resultados = controller.buscarInspecciones(criterio);
            limpiarTabla();
            
            if (resultados.isEmpty()) {
                mostrarMensajeInformacion("No se encontraron inspecciones con el criterio: " + criterio);
            } else {
                for (Inspeccion inspeccion : resultados) {
                    Inspector inspector = inspectorController.obtenerInspector(inspeccion.getIdInspector());
                    
                    // CAMBIO: No mostrar cultivo en la tabla
                    Object[] fila = {
                        new SimpleDateFormat("yyyy-MM-dd").format(inspeccion.getFechaInspeccion()),
                        inspeccion.getEstado(),
                        inspeccion.getObservaciones(),
                        inspector != null ? inspector.getNombresCompletos() : "N/A"
                    };
                    modeloTabla.addRow(fila);
                }
                mostrarMensajeExito("Se encontraron " + resultados.size() + " inspección(es)");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error durante la búsqueda: " + ex.getMessage());
        }
    }

    private void limpiarBusqueda() {
        txtBuscar.setText("");
        cmbFiltro.setSelectedIndex(0);
        idInspeccionSeleccionada = -1; // Reiniciar ID seleccionado
        tablaInspecciones.clearSelection();
        cargarDatos();
    }

    private void cargarDatos() {
        limpiarTabla();
        List<Inspeccion> inspecciones = controller.obtenerTodasInspecciones();
        
        if (inspecciones.isEmpty()) {
            mostrarMensajeInformacion("No hay inspecciones registradas en la base de datos.");
        } else {
            for (Inspeccion inspeccion : inspecciones) {
                Inspector inspector = inspectorController.obtenerInspector(inspeccion.getIdInspector());
                
                // CAMBIO: No mostrar cultivo en la tabla
                Object[] fila = {
                    new SimpleDateFormat("yyyy-MM-dd").format(inspeccion.getFechaInspeccion()),
                    inspeccion.getEstado(),
                    inspeccion.getObservaciones(),
                    inspector != null ? inspector.getNombresCompletos() : "N/A"
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeInformacion("Se cargaron " + inspecciones.size() + " inspección(es) desde la base de datos.");
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
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
            new ConsultaInspeccionesProductor().setVisible(true);
        });
    }
}