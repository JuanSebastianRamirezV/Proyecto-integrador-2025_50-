package vista;

import controlador.CultivoController;
import modelo.Cultivo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import static java.lang.Integer.parseInt;
import java.util.List;

// Clase principal para la gestión de cultivos con interfaz gráfica tipo CRUD (Crear, Leer, Actualizar y Eliminar)
public class GestionCultivos extends JFrame {
       // Componentes de la tabla y su modelo
    private JTable tablaCultivos;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNombreCultivo, txtPlantasAfectadas, txtEstadoPlanta, 
                       txtTotalPlantas, txtNivelIncidencia, txtIdPredio;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar, btnRefrescar;
    private CultivoController controller;

     // Constructor principal: inicializa el controlador, configura la interfaz e inmediatamente carga los datos
    public GestionCultivos() {
        this.controller = new CultivoController();
        initComponents();
        cargarDatos();
    }

    // Método que inicializa y organiza todos los componentes gráficos de la ventana
    private void initComponents() {
        setTitle("Gestión de Cultivos - CRUD Completo"); // Título de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Al cerrar solo se cierra esta ventana
        setSize(1200, 700); // Tamaño por defecto
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(true); // Permitir que el usuario cambie el tamaño

        // Panel principal que contiene todo
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Panel donde están los campos de texto (lado izquierdo)
        JPanel panelFormulario = crearPanelFormulario();

        // Panel donde están los botones de acciones CRUD (parte inferior)
        JPanel panelBotonesPrincipales = crearPanelBotonesPrincipales();

        // Tabla con scroll para ver registros (centro)
        JScrollPane scrollTabla = crearScrollTabla();

        // Se agregan los paneles organizadamente
        panelPrincipal.add(panelFormulario, BorderLayout.WEST);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotonesPrincipales, BorderLayout.SOUTH);

        // Se agrega a la ventana principal
        add(panelPrincipal);

        // Se conectan los botones con sus acciones correspondientes
        agregarActionListeners();
    }

    // Método que construye el panel del formulario con GridBagLayout
    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            "Datos del Cultivo"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(400, 0)); // Fijar ancho para que quede lateral

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Que ocupen todo el ancho posible
        gbc.weightx = 1.0; // Expansión horizontal


        // Componentes del formulario
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID Cultivo:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(240, 240, 240));
        panelFormulario.add(txtId, gbc);

        // sección de campos del formulario donde se agregan las etiquetas y cajas de texto
gbc.gridx = 0; gbc.gridy = 1;
panelFormulario.add(new JLabel("Nombre Cultivo:*"), gbc);
gbc.gridx = 1;
txtNombreCultivo = new JTextField();
panelFormulario.add(txtNombreCultivo, gbc);

gbc.gridx = 0; gbc.gridy = 2;
panelFormulario.add(new JLabel("Plantas Afectadas:"), gbc);
gbc.gridx = 1;
txtPlantasAfectadas = new JTextField();
panelFormulario.add(txtPlantasAfectadas, gbc);

gbc.gridx = 0; gbc.gridy = 3;
panelFormulario.add(new JLabel("Estado Planta:"), gbc);
gbc.gridx = 1;
txtEstadoPlanta = new JTextField();
panelFormulario.add(txtEstadoPlanta, gbc);

gbc.gridx = 0; gbc.gridy = 4;
panelFormulario.add(new JLabel("Total Plantas:"), gbc);
gbc.gridx = 1;
txtTotalPlantas = new JTextField();
panelFormulario.add(txtTotalPlantas, gbc);

gbc.gridx = 0; gbc.gridy = 5;
panelFormulario.add(new JLabel("Nivel Incidencia:"), gbc);
gbc.gridx = 1;
txtNivelIncidencia = new JTextField();
panelFormulario.add(txtNivelIncidencia, gbc);

gbc.gridx = 0; gbc.gridy = 6;
panelFormulario.add(new JLabel("ID Predio:*"), gbc);
gbc.gridx = 1;
txtIdPredio = new JTextField();
panelFormulario.add(txtIdPredio, gbc);

// panel de botones que acompañan el formulario (limpiar y buscar)
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

// método que crea la tabla con sus columnas y estilo visual
private JScrollPane crearScrollTabla() {
    modeloTabla = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // las celdas no se pueden editar directamente
        }
    };
    modeloTabla.addColumn("ID");
    modeloTabla.addColumn("Nombre Cultivo");
    modeloTabla.addColumn("Plantas Afectadas");
    modeloTabla.addColumn("Estado Planta");
    modeloTabla.addColumn("Total Plantas");
    modeloTabla.addColumn("Nivel Incidencia");
    modeloTabla.addColumn("ID Predio");

    tablaCultivos = new JTable(modeloTabla);
    tablaCultivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaCultivos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    tablaCultivos.getTableHeader().setBackground(new Color(46, 204, 113));
    tablaCultivos.getTableHeader().setForeground(Color.WHITE);
    tablaCultivos.setRowHeight(25);
    
    // renderer para dar color alternado a las filas y resaltar selección - CORREGIDO
    tablaCultivos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

    JScrollPane scrollTabla = new JScrollPane(tablaCultivos);
    scrollTabla.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
        "Lista de Cultivos"
    ));

    return scrollTabla;
}

// método que crea el panel de botones principales (agregar, actualizar, eliminar, refrescar)
private JPanel crearPanelBotonesPrincipales() {
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    panelBotones.setBackground(new Color(240, 240, 240));
    
    btnAgregar = crearBoton("Agregar Cultivo", new Color(39, 174, 96));
    btnActualizar = crearBoton("Actualizar Cultivo", new Color(41, 128, 185));
    btnEliminar = crearBoton("Eliminar Cultivo", new Color(231, 76, 60));
    btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182));
    
    // tooltips para mejorar la usabilidad
    btnAgregar.setToolTipText("Agregar un nuevo cultivo a la base de datos");
    btnActualizar.setToolTipText("Actualizar el cultivo seleccionado");
    btnEliminar.setToolTipText("Eliminar el cultivo seleccionado");
    btnRefrescar.setToolTipText("Actualizar la tabla con los últimos datos");
    
    panelBotones.add(btnAgregar);
    panelBotones.add(btnActualizar);
    panelBotones.add(btnEliminar);
    panelBotones.add(btnRefrescar);
    
    return panelBotones;
}

// método auxiliar para crear botones con estilo uniforme
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

// método que conecta los botones con sus acciones correspondientes
private void agregarActionListeners() {
    btnAgregar.addActionListener(e -> agregarCultivo());
    btnActualizar.addActionListener(e -> actualizarCultivo());
    btnEliminar.addActionListener(e -> eliminarCultivo());
    btnLimpiar.addActionListener(e -> limpiarFormulario());
    btnBuscar.addActionListener(e -> buscarCultivos());
    btnRefrescar.addActionListener(e -> cargarDatos());

    // al seleccionar un cultivo de la tabla, se cargan los datos en el formulario
    tablaCultivos.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting() && tablaCultivos.getSelectedRow() != -1) {
            seleccionarCultivoDeTabla();
        }
    });
}

// método que carga todos los cultivos desde la base de datos al modelo de la tabla
private void cargarDatos() {
    limpiarTabla();
    List<Cultivo> cultivos = controller.obtenerTodosCultivos();
    
    if (cultivos.isEmpty()) {
        mostrarMensajeInformacion("No hay cultivos registrados en la base de datos.");
    } else {
        for (Cultivo cultivo : cultivos) {
            Object[] fila = {
                cultivo.getIdCultivo(),
                cultivo.getNombreCultivo(),
                cultivo.getPlantasAfectadas(),
                cultivo.getEstadoPlanta(),
                cultivo.getTotalPlantas(),
                cultivo.getNivelIncidencia(),
                cultivo.getIdPredio()
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
        txtPlantasAfectadas.setText("");
        txtEstadoPlanta.setText("");
        txtTotalPlantas.setText("");
        txtNivelIncidencia.setText("");
        txtIdPredio.setText("");
        tablaCultivos.clearSelection();
        txtNombreCultivo.requestFocus();
    }

    private void seleccionarCultivoDeTabla() {
        int filaSeleccionada = tablaCultivos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtNombreCultivo.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtPlantasAfectadas.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtEstadoPlanta.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            txtTotalPlantas.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
            txtNivelIncidencia.setText(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
            txtIdPredio.setText(modeloTabla.getValueAt(filaSeleccionada, 6).toString());
        }
    }

    private void agregarCultivo() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Cultivo nuevoCultivo = new Cultivo();
            // NO establecer setIdCultivo - se generará automáticamente
            nuevoCultivo.setNombreCultivo(txtNombreCultivo.getText().trim());
            nuevoCultivo.setPlantasAfectadas(parseInt(txtPlantasAfectadas.getText().trim()));
            nuevoCultivo.setEstadoPlanta(txtEstadoPlanta.getText().trim());
            nuevoCultivo.setTotalPlantas(parseInt(txtTotalPlantas.getText().trim()));
            nuevoCultivo.setNivelIncidencia(txtNivelIncidencia.getText().trim());
            nuevoCultivo.setIdPredio(parseInt(txtIdPredio.getText().trim()));

            if (controller.agregarCultivo(nuevoCultivo)) {
                mostrarMensajeExito("Cultivo agregado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar el cultivo a la base de datos");
            }
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
            Cultivo cultivo = new Cultivo();
            cultivo.setIdCultivo(Integer.parseInt(txtId.getText()));
            cultivo.setNombreCultivo(txtNombreCultivo.getText().trim());
            cultivo.setPlantasAfectadas(parseInt(txtPlantasAfectadas.getText().trim()));
            cultivo.setEstadoPlanta(txtEstadoPlanta.getText().trim());
            cultivo.setTotalPlantas(parseInt(txtTotalPlantas.getText().trim()));
            cultivo.setNivelIncidencia(txtNivelIncidencia.getText().trim());
            cultivo.setIdPredio(parseInt(txtIdPredio.getText().trim()));

            if (controller.actualizarCultivo(cultivo)) {
                mostrarMensajeExito("Cultivo actualizado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar el cultivo en la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminarCultivo() {
        int filaSeleccionada = tablaCultivos.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeError("Seleccione un cultivo de la tabla para eliminar");
            return;
        }

        int idCultivo = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreCultivo = modeloTabla.getValueAt(filaSeleccionada, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar el cultivo?\n" +
            "Nombre: " + nombreCultivo + "\n" +
            "ID: " + idCultivo + "\n\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (controller.eliminarCultivo(idCultivo)) {
                    mostrarMensajeExito("Cultivo eliminado exitosamente");
                    cargarDatos();
                    limpiarFormulario();
                } else {
                    mostrarMensajeError("Error al eliminar el cultivo de la base de datos");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error inesperado: " + ex.getMessage());
            }
        }
    }

    private void buscarCultivos() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre, estado o nivel de incidencia a buscar:",
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
                        Object[] fila = {
                            cultivo.getIdCultivo(),
                            cultivo.getNombreCultivo(),
                            cultivo.getPlantasAfectadas(),
                            cultivo.getEstadoPlanta(),
                            cultivo.getTotalPlantas(),
                            cultivo.getNivelIncidencia(),
                            cultivo.getIdPredio()
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
        if (txtNombreCultivo.getText().trim().isEmpty() || txtIdPredio.getText().trim().isEmpty()) {
            mostrarMensajeError("Los campos Nombre Cultivo e ID Predio son obligatorios");
            if (txtNombreCultivo.getText().trim().isEmpty()) {
                txtNombreCultivo.requestFocus();
            } else {
                txtIdPredio.requestFocus();
            }
            return false;
        }

        // Validar que ID Predio sea numérico y mayor a 0
        try {
            int idPredio = Integer.parseInt(txtIdPredio.getText().trim());
            if (idPredio <= 0) {
                mostrarMensajeError("El ID Predio debe ser un número mayor a 0");
                txtIdPredio.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarMensajeError("El ID Predio debe ser un número válido");
            txtIdPredio.requestFocus();
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
            new GestionCultivos().setVisible(true);
        });
    }
}