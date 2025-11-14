package vista;

import controlador.PlagaController;
import modelo.Plaga;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class GestionPlagas extends JFrame {
    private JTable tablaPlagas;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombrePlaga, txtTipoPlaga, txtCultivosAsociados;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar, btnRefrescar;
    private PlagaController controller;
    
    // Variable para almacenar el ID de la plaga seleccionada internamente
    private int idPlagaSeleccionada = -1;

    /**
     * Constructor de la clase GestionPlagas
     * Inicializa el controlador y los componentes de la interfaz gráfica
     */
    public GestionPlagas() {
        this.controller = new PlagaController();
        initComponents();
        cargarDatos();
    }

    /**
     * Inicializa y configura todos los componentes visuales de la interfaz
     * Establece el diseño principal, tamaño, posición y comportamiento de la ventana
     */
    private void initComponents() {
        setTitle("Gestión de Plagas - CRUD Completo");
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
     * Crea y configura el panel del formulario para ingreso de datos de plagas
     * Utiliza GridBagLayout para un diseño organizado y flexible
     * 
     * @return JPanel configurado con todos los campos del formulario
     */
    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(231, 76, 60), 2),
            "Datos de la Plaga"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(350, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // ELIMINADO: Campo ID Plaga - ya no se muestra
        
        // Campo Nombre Plaga (obligatorio)
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre Plaga:*"), gbc);
        gbc.gridx = 1;
        txtNombrePlaga = new JTextField();
        panelFormulario.add(txtNombrePlaga, gbc);

        // Campo Tipo Plaga
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Tipo Plaga:"), gbc);
        gbc.gridx = 1;
        txtTipoPlaga = new JTextField();
        panelFormulario.add(txtTipoPlaga, gbc);

        // Campo Cultivos Asociados
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Cultivos Asociados:"), gbc);
        gbc.gridx = 1;
        txtCultivosAsociados = new JTextField();
        panelFormulario.add(txtCultivosAsociados, gbc);

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
     * Crea y configura el componente de tabla con scroll para mostrar las plagas
     * Incluye personalización visual con colores alternados y selección
     * 
     * @return JScrollPane que contiene la tabla de plagas
     */
    private JScrollPane crearScrollTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // MODIFICADO: Eliminar columna "ID Plaga" de la vista
        modeloTabla.addColumn("Nombre Plaga");
        modeloTabla.addColumn("Tipo Plaga");
        modeloTabla.addColumn("Cultivos Asociados");

        tablaPlagas = new JTable(modeloTabla);
        tablaPlagas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPlagas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaPlagas.getTableHeader().setBackground(new Color(231, 76, 60));
        tablaPlagas.getTableHeader().setForeground(Color.WHITE);
        tablaPlagas.setRowHeight(25);
        
        // Renderer para filas alternadas - CORREGIDO
        tablaPlagas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        JScrollPane scrollTabla = new JScrollPane(tablaPlagas);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(231, 76, 60), 2),
            "Lista de Plagas"
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
        
        btnAgregar = crearBoton("Agregar Plaga", new Color(39, 174, 96));
        btnActualizar = crearBoton("Actualizar Plaga", new Color(41, 128, 185));
        btnEliminar = crearBoton("Eliminar Plaga", new Color(231, 76, 60));
        btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182));
        
        // Tooltips para mejor usabilidad
        btnAgregar.setToolTipText("Agregar una nueva plaga a la base de datos");
        btnActualizar.setToolTipText("Actualizar la plaga seleccionada");
        btnEliminar.setToolTipText("Eliminar la plaga seleccionada");
        btnRefrescar.setToolTipText("Actualizar la tabla con los últimos datos");
        
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
        btnAgregar.addActionListener(e -> agregarPlaga());
        btnActualizar.addActionListener(e -> actualizarPlaga());
        btnEliminar.addActionListener(e -> eliminarPlaga());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarPlagas());
        btnRefrescar.addActionListener(e -> cargarDatos());

        tablaPlagas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaPlagas.getSelectedRow() != -1) {
                seleccionarPlagaDeTabla();
            }
        });
    }

    private void cargarDatos() {
        limpiarTabla();
        List<Plaga> plagas = controller.obtenerTodasPlagas();
        
        if (plagas.isEmpty()) {
            mostrarMensajeInformacion("No hay plagas registradas en la base de datos.");
        } else {
            for (Plaga plaga : plagas) {
                // MODIFICADO: No mostrar el ID en la tabla
                Object[] fila = {
                    plaga.getNombrePlaga(),
                    plaga.getTipoPlaga(),
                    plaga.getCultivosAsociados()
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeInformacion("Se cargaron " + plagas.size() + " plaga(s) desde la base de datos.");
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        // ELIMINADO: txtId.setText("");
        txtNombrePlaga.setText("");
        txtTipoPlaga.setText("");
        txtCultivosAsociados.setText("");
        tablaPlagas.clearSelection();
        idPlagaSeleccionada = -1; // Reiniciar ID seleccionado
        txtNombrePlaga.requestFocus();
    }

    private void seleccionarPlagaDeTabla() {
        int filaSeleccionada = tablaPlagas.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // MODIFICADO: Obtener el nombre de la plaga seleccionada para buscar su ID
            String nombrePlagaSeleccionada = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
            String tipoPlagaSeleccionada = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
            
            // Buscar la plaga en la base de datos para obtener su ID
            List<Plaga> plagas = controller.buscarPlagas(nombrePlagaSeleccionada);
            for (Plaga plaga : plagas) {
                if (plaga.getNombrePlaga().equals(nombrePlagaSeleccionada) && 
                    plaga.getTipoPlaga().equals(tipoPlagaSeleccionada)) {
                    idPlagaSeleccionada = plaga.getIdPlaga();
                    break;
                }
            }
            
            // Llenar formulario con datos visibles
            txtNombrePlaga.setText(nombrePlagaSeleccionada);
            txtTipoPlaga.setText(tipoPlagaSeleccionada);
            txtCultivosAsociados.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
        }
    }

    private void agregarPlaga() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Plaga nuevaPlaga = new Plaga();
            // NO establecer setIdPlaga() - se generará automáticamente
            nuevaPlaga.setNombrePlaga(txtNombrePlaga.getText().trim());
            nuevaPlaga.setTipoPlaga(txtTipoPlaga.getText().trim());
            nuevaPlaga.setCultivosAsociados(txtCultivosAsociados.getText().trim());

            if (controller.agregarPlaga(nuevaPlaga)) {
                mostrarMensajeExito("Plaga agregada exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar la plaga a la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void actualizarPlaga() {
        // MODIFICADO: Usar idPlagaSeleccionada en lugar de txtId
        if (idPlagaSeleccionada == -1) {
            mostrarMensajeError("Seleccione una plaga de la tabla para actualizar");
            return;
        }

        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Plaga plaga = new Plaga();
            plaga.setIdPlaga(idPlagaSeleccionada); // Usar el ID almacenado internamente
            plaga.setNombrePlaga(txtNombrePlaga.getText().trim());
            plaga.setTipoPlaga(txtTipoPlaga.getText().trim());
            plaga.setCultivosAsociados(txtCultivosAsociados.getText().trim());

            if (controller.actualizarPlaga(plaga)) {
                mostrarMensajeExito("Plaga actualizada exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar la plaga en la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminarPlaga() {
        int filaSeleccionada = tablaPlagas.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeError("Seleccione una plaga de la tabla para eliminar");
            return;
        }

        // MODIFICADO: Usar idPlagaSeleccionada en lugar de obtenerlo de la tabla
        if (idPlagaSeleccionada == -1) {
            mostrarMensajeError("No se pudo identificar la plaga seleccionada");
            return;
        }

        String nombrePlaga = modeloTabla.getValueAt(filaSeleccionada, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar la plaga?\n" +
            "Nombre: " + nombrePlaga + "\n\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (controller.eliminarPlaga(idPlagaSeleccionada)) {
                    mostrarMensajeExito("Plaga eliminada exitosamente");
                    cargarDatos();
                    limpiarFormulario();
                } else {
                    mostrarMensajeError("Error al eliminar la plaga de la base de datos");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error inesperado: " + ex.getMessage());
            }
        }
    }

    private void buscarPlagas() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre, tipo o cultivos a buscar:",
            "Buscar Plagas",
            JOptionPane.QUESTION_MESSAGE);

        if (criterio != null && !criterio.trim().isEmpty()) {
            try {
                List<Plaga> resultados = controller.buscarPlagas(criterio.trim());
                limpiarTabla();
                
                if (resultados.isEmpty()) {
                    mostrarMensajeInformacion("No se encontraron plagas con el criterio: " + criterio);
                } else {
                    for (Plaga plaga : resultados) {
                        // MODIFICADO: No mostrar el ID en la tabla
                        Object[] fila = {
                            plaga.getNombrePlaga(),
                            plaga.getTipoPlaga(),
                            plaga.getCultivosAsociados()
                        };
                        modeloTabla.addRow(fila);
                    }
                    mostrarMensajeExito("Se encontraron " + resultados.size() + " plaga(s)");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error durante la búsqueda: " + ex.getMessage());
            }
        } else if (criterio != null) {
            cargarDatos();
        }
    }

    private boolean validarCamposObligatorios() {
        if (txtNombrePlaga.getText().trim().isEmpty()) {
            mostrarMensajeError("El campo Nombre Plaga es obligatorio");
            txtNombrePlaga.requestFocus();
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
            new GestionPlagas().setVisible(true);
        });
    }
}