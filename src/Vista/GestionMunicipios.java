package vista;

import controlador.MunicipioController;
import modelo.Municipio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class GestionMunicipios extends JFrame {
    private JTable tablaMunicipios;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNombre;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar, btnRefrescar;
    private final MunicipioController controller;

    public GestionMunicipios() {
        this.controller = new MunicipioController();
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Gestión de Municipios - CRUD Completo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
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

    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "Datos del Municipio"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Componentes del formulario
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID Municipio:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(240, 240, 240));
        panelFormulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombre:*"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre, gbc);

        // Panel de botones del formulario
        gbc.gridx = 0; gbc.gridy = 2;
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
        modeloTabla.addColumn("ID Municipio");
        modeloTabla.addColumn("Nombre");

        tablaMunicipios = new JTable(modeloTabla);
        tablaMunicipios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMunicipios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaMunicipios.getTableHeader().setBackground(new Color(155, 89, 182));
        tablaMunicipios.getTableHeader().setForeground(Color.WHITE);
        tablaMunicipios.setRowHeight(25);
        
        // Renderer para filas alternadas - CORREGIDO
        tablaMunicipios.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        JScrollPane scrollTabla = new JScrollPane(tablaMunicipios);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "Lista de Municipios"
        ));

        return scrollTabla;
    }

    private JPanel crearPanelBotonesPrincipales() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnAgregar = crearBoton("Agregar Municipio", new Color(39, 174, 96));
        btnActualizar = crearBoton("Actualizar Municipio", new Color(41, 128, 185));
        btnEliminar = crearBoton("Eliminar Municipio", new Color(231, 76, 60));
        btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182));
        
        // Tooltips para mejor usabilidad
        btnAgregar.setToolTipText("Agregar un nuevo municipio a la base de datos");
        btnActualizar.setToolTipText("Actualizar el municipio seleccionado");
        btnEliminar.setToolTipText("Eliminar el municipio seleccionado");
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
        btnAgregar.addActionListener(e -> agregarMunicipio());
        btnActualizar.addActionListener(e -> actualizarMunicipio());
        btnEliminar.addActionListener(e -> eliminarMunicipio());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarMunicipios());
        btnRefrescar.addActionListener(e -> cargarDatos());

        tablaMunicipios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaMunicipios.getSelectedRow() != -1) {
                seleccionarMunicipioDeTabla();
            }
        });
    }

    private void cargarDatos() {
        limpiarTabla();
        List<Municipio> municipios = controller.obtenerTodosMunicipios();
        
        if (municipios.isEmpty()) {
            mostrarMensajeInformacion("No hay municipios registrados en la base de datos.");
        } else {
            for (Municipio municipio : municipios) {
                Object[] fila = {
                    municipio.getIdMunicipio(),
                    municipio.getNombre()
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeInformacion("Se cargaron " + municipios.size() + " municipio(s) desde la base de datos.");
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        tablaMunicipios.clearSelection();
        txtNombre.requestFocus();
    }

    private void seleccionarMunicipioDeTabla() {
        int filaSeleccionada = tablaMunicipios.getSelectedRow();
        if (filaSeleccionada >= 0) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
        }
    }

    private void agregarMunicipio() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Municipio nuevoMunicipio = new Municipio();
            // NO establecer setIdMunicipio() - se generará automáticamente
            nuevoMunicipio.setNombre(txtNombre.getText().trim());

            if (controller.agregarMunicipio(nuevoMunicipio)) {
                mostrarMensajeExito("Municipio agregado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar el municipio a la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void actualizarMunicipio() {
        if (txtId.getText().isEmpty()) {
            mostrarMensajeError("Seleccione un municipio de la tabla para actualizar");
            return;
        }

        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Municipio municipio = new Municipio();
            municipio.setIdMunicipio(Integer.parseInt(txtId.getText()));
            municipio.setNombre(txtNombre.getText().trim());

            if (controller.actualizarMunicipio(municipio)) {
                mostrarMensajeExito("Municipio actualizado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar el municipio en la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminarMunicipio() {
        int filaSeleccionada = tablaMunicipios.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeError("Seleccione un municipio de la tabla para eliminar");
            return;
        }

        int idMunicipio = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreMunicipio = modeloTabla.getValueAt(filaSeleccionada, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar el municipio?\n" +
            "Nombre: " + nombreMunicipio + "\n" +
            "ID: " + idMunicipio + "\n\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (controller.eliminarMunicipio(idMunicipio)) {
                    mostrarMensajeExito("Municipio eliminado exitosamente");
                    cargarDatos();
                    limpiarFormulario();
                } else {
                    mostrarMensajeError("Error al eliminar el municipio de la base de datos");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error inesperado: " + ex.getMessage());
            }
        }
    }

    private void buscarMunicipios() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre del municipio a buscar:",
            "Buscar Municipios",
            JOptionPane.QUESTION_MESSAGE);

        if (criterio != null && !criterio.trim().isEmpty()) {
            try {
                List<Municipio> resultados = controller.buscarMunicipios(criterio.trim());
                limpiarTabla();
                
                if (resultados.isEmpty()) {
                    mostrarMensajeInformacion("No se encontraron municipios con el criterio: " + criterio);
                } else {
                    for (Municipio municipio : resultados) {
                        Object[] fila = {
                            municipio.getIdMunicipio(),
                            municipio.getNombre()
                        };
                        modeloTabla.addRow(fila);
                    }
                    mostrarMensajeExito("Se encontraron " + resultados.size() + " municipio(s)");
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
            new GestionMunicipios().setVisible(true);
        });
    }
}