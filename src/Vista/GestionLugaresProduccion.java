package vista;

import controlador.LugarProduccionController;
import modelo.LugarProduccion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/*
 * Clase principal para la gestión de lugares de producción mediante una interfaz gráfica.
 * Extiende JFrame para crear una ventana con funcionalidades CRUD (Crear, Leer, Actualizar, Eliminar).
 * Contiene componentes visuales como tabla, campos de texto y botones, además de un controlador encargado de la lógica.
 */
public class GestionLugaresProduccion extends JFrame {
    
    // Componentes de la interfaz gráfica
    private JTable tablaLugares;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNombre, txtRegistroICA, txtDireccion, txtIdProductor, txtIdCultivo;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar, btnRefrescar;
    
    // Controlador encargado de la lógica del CRUD
    private LugarProduccionController controller;

    /*
     * Constructor principal que inicializa el controlador,
     * los componentes de la interfaz y carga los datos iniciales en la tabla.
     */
    public GestionLugaresProduccion() {
        this.controller = new LugarProduccionController();
        initComponents();
        cargarDatos();
    }

    /*
     * Método encargado de construir toda la interfaz gráfica de la ventana.
     * Configura el layout principal e incluye el formulario, la tabla y los botones.
     */
    private void initComponents() {
        setTitle("Gestión de Lugares de Producción - CRUD Completo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Panel principal contenedor de todos los elementos
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Panel del formulario ubicado a la izquierda
        JPanel panelFormulario = crearPanelFormulario();

        // Panel inferior con botones principales de acciones CRUD
        JPanel panelBotonesPrincipales = crearPanelBotonesPrincipales();

        // Tabla con scroll que muestra los datos registrados
        JScrollPane scrollTabla = crearScrollTabla();

        panelPrincipal.add(panelFormulario, BorderLayout.WEST);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotonesPrincipales, BorderLayout.SOUTH);

        add(panelPrincipal);
        agregarActionListeners();
    }

    /*
     * Crea el panel de formulario con los campos de entrada para capturar los datos del lugar de producción.
     * Usa GridBagLayout para una distribución más ordenada y adaptable.
     */
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

        // Campo ID (solo lectura)
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID Lugar:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(240, 240, 240));
        panelFormulario.add(txtId, gbc);

        // Campo Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombre:*"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre, gbc);

        // Campo Registro ICA
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Registro ICA:"), gbc);
        gbc.gridx = 1;
        txtRegistroICA = new JTextField();
        panelFormulario.add(txtRegistroICA, gbc);

        // Campo Dirección
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        txtDireccion = new JTextField();
        panelFormulario.add(txtDireccion, gbc);

        // Campo ID Productor
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(new JLabel("ID Productor:*"), gbc);
        gbc.gridx = 1;
        txtIdProductor = new JTextField();
        panelFormulario.add(txtIdProductor, gbc);

        // Campo ID Cultivo
        gbc.gridx = 0; gbc.gridy = 5;
        panelFormulario.add(new JLabel("ID Cultivo:*"), gbc);
        gbc.gridx = 1;
        txtIdCultivo = new JTextField();
        panelFormulario.add(txtIdCultivo, gbc);

        // Panel inferior con botones de formulario (Limpiar y Buscar)
        gbc.gridx = 0; gbc.gridy = 6;
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

    /*
     * Crea y configura la tabla donde se visualizarán los registros obtenidos de la base de datos.
     * Incluye estilos visuales, altura de filas y colores alternados para mejor legibilidad.
     */
    private JScrollPane crearScrollTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evitar que se editen las celdas directamente desde la tabla
            }
        };
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Registro ICA");
        modeloTabla.addColumn("Dirección");
        modeloTabla.addColumn("ID Productor");
        modeloTabla.addColumn("ID Cultivo");

        tablaLugares = new JTable(modeloTabla);
        tablaLugares.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaLugares.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaLugares.getTableHeader().setBackground(new Color(155, 89, 182));
        tablaLugares.getTableHeader().setForeground(Color.WHITE);
        tablaLugares.setRowHeight(25);
        
        // Renderer para cambiar el color de fondo de las filas según la selección - CORREGIDO
        tablaLugares.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        JScrollPane scrollTabla = new JScrollPane(tablaLugares);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "Lista de Lugares de Producción"
        ));

        return scrollTabla;
    }

    /*
     * Crea el panel inferior que contiene los botones principales para realizar operaciones CRUD.
     */
    private JPanel crearPanelBotonesPrincipales() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnAgregar = crearBoton("Agregar Lugar", new Color(39, 174, 96));
        btnActualizar = crearBoton("Actualizar Lugar", new Color(41, 128, 185));
        btnEliminar = crearBoton("Eliminar Lugar", new Color(231, 76, 60));
        btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182));
        
        // Tooltips para orientar al usuario sobre la función de cada botón
        btnAgregar.setToolTipText("Agregar un nuevo lugar de producción a la base de datos");
        btnActualizar.setToolTipText("Actualizar el lugar de producción seleccionado");
        btnEliminar.setToolTipText("Eliminar el lugar de producción seleccionado");
        btnRefrescar.setToolTipText("Actualizar la tabla con los últimos datos");
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        
        return panelBotones;
    }

    /*
     * Método auxiliar para crear botones con un estilo uniforme (color, tamaño y fuente).
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

    private void agregarActionListeners() {
        btnAgregar.addActionListener(e -> agregarLugar());
        btnActualizar.addActionListener(e -> actualizarLugar());
        btnEliminar.addActionListener(e -> eliminarLugar());
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
                    lugar.getIdLugarProduccion(),
                    lugar.getNombreLugar(),
                    lugar.getNumeroRegistroICA(),
                    lugar.getDireccionLugar(),
                    lugar.getIdProductor(),
                    lugar.getIdCultivo()
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeInformacion("Se cargaron " + lugares.size() + " lugar(es) de producción desde la base de datos.");
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtRegistroICA.setText("");
        txtDireccion.setText("");
        txtIdProductor.setText("");
        txtIdCultivo.setText("");
        tablaLugares.clearSelection();
        txtNombre.requestFocus();
    }

    private void seleccionarLugarDeTabla() {
        int filaSeleccionada = tablaLugares.getSelectedRow();
        if (filaSeleccionada >= 0) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtRegistroICA.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtDireccion.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            txtIdProductor.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
            txtIdCultivo.setText(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
        }
    }

    private void agregarLugar() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            LugarProduccion nuevoLugar = new LugarProduccion();
            // NO establecer setIdLugarProduccion() - se generará automáticamente
            nuevoLugar.setNombreLugar(txtNombre.getText().trim());
            nuevoLugar.setNumeroRegistroICA(txtRegistroICA.getText().trim());
            nuevoLugar.setDireccionLugar(txtDireccion.getText().trim());
            nuevoLugar.setIdProductor(Integer.parseInt(txtIdProductor.getText().trim()));
            nuevoLugar.setIdCultivo(Integer.parseInt(txtIdCultivo.getText().trim()));

            if (controller.agregarLugarProduccion(nuevoLugar)) {
                mostrarMensajeExito("Lugar de producción agregado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar el lugar de producción a la base de datos");
            }
        } catch (NumberFormatException ex) {
            mostrarMensajeError("ID Productor e ID Cultivo deben ser números válidos");
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
            LugarProduccion lugar = new LugarProduccion();
            lugar.setIdLugarProduccion(Integer.parseInt(txtId.getText()));
            lugar.setNombreLugar(txtNombre.getText().trim());
            lugar.setNumeroRegistroICA(txtRegistroICA.getText().trim());
            lugar.setDireccionLugar(txtDireccion.getText().trim());
            lugar.setIdProductor(Integer.parseInt(txtIdProductor.getText().trim()));
            lugar.setIdCultivo(Integer.parseInt(txtIdCultivo.getText().trim()));

            if (controller.actualizarLugarProduccion(lugar)) {
                mostrarMensajeExito("Lugar de producción actualizado exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar el lugar de producción en la base de datos");
            }
        } catch (NumberFormatException ex) {
            mostrarMensajeError("ID Productor e ID Cultivo deben ser números válidos");
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminarLugar() {
        int filaSeleccionada = tablaLugares.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeError("Seleccione un lugar de la tabla para eliminar");
            return;
        }

        int idLugar = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreLugar = modeloTabla.getValueAt(filaSeleccionada, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar el lugar de producción?\n" +
            "Nombre: " + nombreLugar + "\n" +
            "ID: " + idLugar + "\n\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (controller.eliminarLugarProduccion(idLugar)) {
                    mostrarMensajeExito("Lugar de producción eliminado exitosamente");
                    cargarDatos();
                    limpiarFormulario();
                } else {
                    mostrarMensajeError("Error al eliminar el lugar de producción de la base de datos");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error inesperado: " + ex.getMessage());
            }
        }
    }

    private void buscarLugares() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre, registro ICA o dirección a buscar:",
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
                            lugar.getIdLugarProduccion(),
                            lugar.getNombreLugar(),
                            lugar.getNumeroRegistroICA(),
                            lugar.getDireccionLugar(),
                            lugar.getIdProductor(),
                            lugar.getIdCultivo()
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

        if (txtIdProductor.getText().trim().isEmpty()) {
            mostrarMensajeError("El campo ID Productor es obligatorio");
            txtIdProductor.requestFocus();
            return false;
        }

        if (txtIdCultivo.getText().trim().isEmpty()) {
            mostrarMensajeError("El campo ID Cultivo es obligatorio");
            txtIdCultivo.requestFocus();
            return false;
        }

        // Validar que los IDs sean numéricos
        try {
            Integer.parseInt(txtIdProductor.getText().trim());
            Integer.parseInt(txtIdCultivo.getText().trim());
        } catch (NumberFormatException e) {
            mostrarMensajeError("ID Productor e ID Cultivo deben ser números válidos");
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
            new GestionLugaresProduccion().setVisible(true);
        });
    }
}