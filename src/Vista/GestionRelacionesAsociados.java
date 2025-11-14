package vista;

import controlador.RelacionAsociadosController;
import controlador.PlagaController;
import controlador.CultivoController;
import controlador.InspeccionController;
import controlador.LugarProduccionController;
import controlador.InspectorController;
import modelo.RelacionAsociados;
import modelo.Plaga;
import modelo.Cultivo;
import modelo.Inspeccion;
import modelo.LugarProduccion;
import modelo.Inspector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class GestionRelacionesAsociados extends JFrame {
    
    private JTable tablaRelaciones;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cmbPlaga;
    private JComboBox<String> cmbCultivo;
    private JComboBox<String> cmbInspeccion;
    private JComboBox<String> cmbLugarProduccion;
    private JTextField txtTotalPlantas;
    private JComboBox<String> cmbNivelIncidencia;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar, btnRefrescar;
    private RelacionAsociadosController controller;
    private PlagaController plagaController;
    private CultivoController cultivoController;
    private InspeccionController inspeccionController;
    private LugarProduccionController lugarProduccionController;
    private InspectorController inspectorController;

    // Mapas para almacenar la relación entre el texto mostrado y los IDs
    private java.util.Map<String, Integer> mapaPlagas;
    private java.util.Map<String, Integer> mapaCultivos;
    private java.util.Map<String, Integer> mapaInspecciones;
    private java.util.Map<String, Integer> mapaLugaresProduccion;

    // Variables para almacenar los IDs de la relación seleccionada
    private int idPlagaSeleccionada = -1;
    private int idCultivoSeleccionado = -1;
    private int idInspeccionSeleccionada = -1;
    private int idLugarProduccionSeleccionado = -1;

    public GestionRelacionesAsociados() {
        this.controller = new RelacionAsociadosController();
        this.plagaController = new PlagaController();
        this.cultivoController = new CultivoController();
        this.inspeccionController = new InspeccionController();
        this.lugarProduccionController = new LugarProduccionController();
        this.inspectorController = new InspectorController();
        this.mapaPlagas = new java.util.HashMap<>();
        this.mapaCultivos = new java.util.HashMap<>();
        this.mapaInspecciones = new java.util.HashMap<>();
        this.mapaLugaresProduccion = new java.util.HashMap<>();
        initComponents();
        cargarDatos();
        cargarCombos();
    }

    private void initComponents() {
        setTitle("Gestión de Relaciones de Asociados - Inspector");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 700);
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
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Datos de la Relación"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(500, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // ComboBox Plaga - Solo nombre
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblPlaga = new JLabel("Seleccionar Plaga:*");
        lblPlaga.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelFormulario.add(lblPlaga, gbc);
        gbc.gridx = 1;
        cmbPlaga = new JComboBox<>();
        cmbPlaga.setPreferredSize(new Dimension(200, 25));
        panelFormulario.add(cmbPlaga, gbc);

        // ComboBox Cultivo - Solo nombre
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblCultivo = new JLabel("Seleccionar Cultivo:*");
        lblCultivo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelFormulario.add(lblCultivo, gbc);
        gbc.gridx = 1;
        cmbCultivo = new JComboBox<>();
        cmbCultivo.setPreferredSize(new Dimension(200, 25));
        panelFormulario.add(cmbCultivo, gbc);

        // ComboBox Inspección - Formato mejorado
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblInspeccion = new JLabel("Seleccionar Inspección:*");
        lblInspeccion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelFormulario.add(lblInspeccion, gbc);
        gbc.gridx = 1;
        cmbInspeccion = new JComboBox<>();
        cmbInspeccion.setPreferredSize(new Dimension(200, 25));
        panelFormulario.add(cmbInspeccion, gbc);

        // ComboBox Lugar Producción - Solo nombre
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblLugarProduccion = new JLabel("Seleccionar Lugar Producción:*");
        lblLugarProduccion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelFormulario.add(lblLugarProduccion, gbc);
        gbc.gridx = 1;
        cmbLugarProduccion = new JComboBox<>();
        cmbLugarProduccion.setPreferredSize(new Dimension(200, 25));
        panelFormulario.add(cmbLugarProduccion, gbc);

        // Campo Total Plantas (automático)
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblTotalPlantas = new JLabel("Total de Plantas:*");
        lblTotalPlantas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelFormulario.add(lblTotalPlantas, gbc);
        gbc.gridx = 1;
        txtTotalPlantas = new JTextField();
        txtTotalPlantas.setEditable(false);
        txtTotalPlantas.setBackground(new Color(240, 240, 240));
        txtTotalPlantas.setPreferredSize(new Dimension(200, 25));
        panelFormulario.add(txtTotalPlantas, gbc);

        // ComboBox Nivel Incidencia
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblNivelIncidencia = new JLabel("Nivel de Incidencia:*");
        lblNivelIncidencia.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelFormulario.add(lblNivelIncidencia, gbc);
        gbc.gridx = 1;
        cmbNivelIncidencia = new JComboBox<>();
        cmbNivelIncidencia.addItem("BAJA");
        cmbNivelIncidencia.addItem("MEDIA");
        cmbNivelIncidencia.addItem("ALTA");
        cmbNivelIncidencia.setPreferredSize(new Dimension(200, 25));
        panelFormulario.add(cmbNivelIncidencia, gbc);

        // Información para el usuario
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        JLabel lblInfo = new JLabel("<html><small>💡 <b>Información:</b><br>" +
                                  "• Total de Plantas se calcula automáticamente<br>" +
                                  "• Seleccione todos los campos obligatorios</small></html>");
        lblInfo.setForeground(new Color(70, 70, 70));
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panelFormulario.add(lblInfo, gbc);

        // Panel de botones del formulario
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        JPanel panelBotonesForm = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotonesForm.setBackground(Color.WHITE);
        
        btnLimpiar = crearBoton("Limpiar Formulario", new Color(243, 156, 18));
        btnBuscar = crearBoton("Buscar Relaciones", new Color(52, 152, 219));
        
        panelBotonesForm.add(btnLimpiar);
        panelBotonesForm.add(btnBuscar);
        panelFormulario.add(panelBotonesForm, gbc);

        return panelFormulario;
    }

    private void cargarCombos() {
        cargarComboPlagas();
        cargarComboCultivos();
        cargarComboInspecciones();
        cargarComboLugaresProduccion();
    }

    private void cargarComboPlagas() {
        cmbPlaga.removeAllItems();
        mapaPlagas.clear();
        
        List<Plaga> plagas = plagaController.obtenerTodasPlagas();
        for (Plaga plaga : plagas) {
            String texto = plaga.getNombrePlaga();
            cmbPlaga.addItem(texto);
            mapaPlagas.put(texto, plaga.getIdPlaga());
        }
    }

    private void cargarComboCultivos() {
        cmbCultivo.removeAllItems();
        mapaCultivos.clear();
        
        List<Cultivo> cultivos = cultivoController.obtenerTodosCultivos();
        for (Cultivo cultivo : cultivos) {
            String texto = cultivo.getNombreCultivo();
            cmbCultivo.addItem(texto);
            mapaCultivos.put(texto, cultivo.getIdCultivo());
        }
    }

    private void cargarComboInspecciones() {
        cmbInspeccion.removeAllItems();
        mapaInspecciones.clear();
        
        List<Inspeccion> inspecciones = inspeccionController.obtenerTodasInspecciones();
        for (Inspeccion inspeccion : inspecciones) {
            // Formato mejorado: "ID - Fecha - Estado - Inspector"
            String nombreInspector = obtenerNombreInspector(inspeccion.getIdInspector());
            String fecha = new java.text.SimpleDateFormat("yyyy-MM-dd").format(inspeccion.getFechaInspeccion());
            
            String texto = String.format("ID:%d - %s - %s - %s", 
                inspeccion.getIdInspeccion(),
                fecha,
                inspeccion.getEstado(),
                nombreInspector);
            cmbInspeccion.addItem(texto);
            mapaInspecciones.put(texto, inspeccion.getIdInspeccion());
        }
    }

    private String obtenerNombreInspector(int idInspector) {
        try {
            Inspector inspector = inspectorController.obtenerInspector(idInspector);
            return inspector != null ? inspector.getNombresCompletos() : "Inspector No Encontrado";
        } catch (Exception e) {
            return "Inspector No Encontrado";
        }
    }

    private void cargarComboLugaresProduccion() {
        cmbLugarProduccion.removeAllItems();
        mapaLugaresProduccion.clear();
        
        List<LugarProduccion> lugares = lugarProduccionController.obtenerTodosLugaresProduccion();
        for (LugarProduccion lugar : lugares) {
            String texto = lugar.getNombreLugar();
            cmbLugarProduccion.addItem(texto);
            mapaLugaresProduccion.put(texto, lugar.getIdLugarProduccion());
        }
    }

    private JScrollPane crearScrollTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // QUITAR columna "ID" - usar IDs compuestos
        modeloTabla.addColumn("Plaga");
        modeloTabla.addColumn("Cultivo");
        modeloTabla.addColumn("Inspección");
        modeloTabla.addColumn("Lugar Producción");
        modeloTabla.addColumn("Total Plantas");
        modeloTabla.addColumn("Nivel Incidencia");

        tablaRelaciones = new JTable(modeloTabla);
        tablaRelaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaRelaciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaRelaciones.getTableHeader().setBackground(new Color(52, 152, 219));
        tablaRelaciones.getTableHeader().setForeground(Color.WHITE);
        tablaRelaciones.setRowHeight(25);
        
        // Ajustar anchos de columnas
        tablaRelaciones.getColumnModel().getColumn(0).setPreferredWidth(150); // Plaga
        tablaRelaciones.getColumnModel().getColumn(1).setPreferredWidth(150); // Cultivo
        tablaRelaciones.getColumnModel().getColumn(2).setPreferredWidth(250); // Inspección
        tablaRelaciones.getColumnModel().getColumn(3).setPreferredWidth(200); // Lugar Producción
        tablaRelaciones.getColumnModel().getColumn(4).setPreferredWidth(100); // Total Plantas
        tablaRelaciones.getColumnModel().getColumn(5).setPreferredWidth(120); // Nivel Incidencia
        
        // Renderer para filas alternadas y colorear por nivel de incidencia
        tablaRelaciones.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    // Colorear filas según el nivel de incidencia
                    String nivelIncidencia = (String) table.getValueAt(row, 5);
                    if (nivelIncidencia != null) {
                        switch (nivelIncidencia.toUpperCase()) {
                            case "ALTA":
                                c.setBackground(new Color(255, 230, 230)); // Rojo claro
                                break;
                            case "MEDIA":
                                c.setBackground(new Color(255, 255, 200)); // Amarillo claro
                                break;
                            case "BAJA":
                                c.setBackground(new Color(230, 255, 230)); // Verde claro
                                break;
                            default:
                                if (row % 2 == 0) {
                                    c.setBackground(Color.WHITE);
                                } else {
                                    c.setBackground(new Color(240, 240, 240));
                                }
                        }
                    } else {
                        if (row % 2 == 0) {
                            c.setBackground(Color.WHITE);
                        } else {
                            c.setBackground(new Color(240, 240, 240));
                        }
                    }
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(41, 128, 185));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaRelaciones);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Lista de Relaciones de Asociados Registradas"
        ));

        return scrollTabla;
    }

    private JPanel crearPanelBotonesPrincipales() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnAgregar = crearBoton("Agregar Relación", new Color(39, 174, 96));
        btnActualizar = crearBoton("Actualizar Relación", new Color(41, 128, 185));
        btnEliminar = crearBoton("Eliminar Relación", new Color(231, 76, 60));
        btnRefrescar = crearBoton("Refrescar Datos", new Color(155, 89, 182));
        
        btnAgregar.setToolTipText("Agregar una nueva relación de asociados");
        btnActualizar.setToolTipText("Actualizar la relación seleccionada");
        btnEliminar.setToolTipText("Eliminar la relación seleccionada");
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
        boton.setPreferredSize(new Dimension(160, 35));
        return boton;
    }

    private void agregarActionListeners() {
        btnAgregar.addActionListener(e -> agregarRelacion());
        btnActualizar.addActionListener(e -> actualizarRelacion());
        btnEliminar.addActionListener(e -> eliminarRelacion());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarRelaciones());
        btnRefrescar.addActionListener(e -> {
            cargarCombos();
            cargarDatos();
        });

        // Listener para actualizar total de plantas cuando se selecciona un cultivo
        cmbCultivo.addActionListener(e -> actualizarTotalPlantas());

        tablaRelaciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaRelaciones.getSelectedRow() != -1) {
                seleccionarRelacionDeTabla();
            }
        });
    }

    private void actualizarTotalPlantas() {
        String cultivoSeleccionado = (String) cmbCultivo.getSelectedItem();
        if (cultivoSeleccionado != null && mapaCultivos.containsKey(cultivoSeleccionado)) {
            int idCultivo = mapaCultivos.get(cultivoSeleccionado);
            Cultivo cultivo = cultivoController.obtenerCultivo(idCultivo);
            if (cultivo != null) {
                txtTotalPlantas.setText(String.valueOf(cultivo.getTotalPlantas()));
            }
        } else {
            txtTotalPlantas.setText("");
        }
    }

    private void cargarDatos() {
        limpiarTabla();
        List<RelacionAsociados> relaciones = controller.obtenerTodasRelaciones();
        
        if (relaciones.isEmpty()) {
            mostrarMensajeInformacion("No hay relaciones de asociados registradas en la base de datos.");
        } else {
            for (RelacionAsociados relacion : relaciones) {
                // Obtener nombres de las entidades relacionadas
                String nombrePlaga = obtenerNombrePlaga(relacion.getIdPlaga());
                String nombreCultivo = obtenerNombreCultivo(relacion.getIdCultivo());
                String nombreInspeccion = obtenerNombreInspeccion(relacion.getIdInspeccion());
                String nombreLugarProduccion = obtenerNombreLugarProduccion(relacion.getIdLugarProduccion());
                
                Object[] fila = {
                    nombrePlaga,
                    nombreCultivo,
                    nombreInspeccion,
                    nombreLugarProduccion,
                    relacion.getTotalPlantas(),
                    relacion.getNivelIncidencia()
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeExito("Se cargaron " + relaciones.size() + " relación(es) desde la base de datos.");
        }
    }

    private String obtenerNombrePlaga(int idPlaga) {
        Plaga plaga = plagaController.obtenerPlaga(idPlaga);
        return plaga != null ? plaga.getNombrePlaga() : "Plaga ID: " + idPlaga;
    }

    private String obtenerNombreCultivo(int idCultivo) {
        Cultivo cultivo = cultivoController.obtenerCultivo(idCultivo);
        return cultivo != null ? cultivo.getNombreCultivo() : "Cultivo ID: " + idCultivo;
    }

    private String obtenerNombreInspeccion(int idInspeccion) {
        Inspeccion inspeccion = inspeccionController.obtenerInspeccion(idInspeccion);
        if (inspeccion != null) {
            String nombreInspector = obtenerNombreInspector(inspeccion.getIdInspector());
            String fecha = new java.text.SimpleDateFormat("yyyy-MM-dd").format(inspeccion.getFechaInspeccion());
            
            return String.format("ID:%d - %s - %s - %s", 
                inspeccion.getIdInspeccion(),
                fecha,
                inspeccion.getEstado(),
                nombreInspector);
        }
        return "Inspección ID: " + idInspeccion;
    }

    private String obtenerNombreLugarProduccion(int idLugarProduccion) {
        LugarProduccion lugar = lugarProduccionController.obtenerLugarProduccion(idLugarProduccion);
        return lugar != null ? lugar.getNombreLugar() : "Lugar ID: " + idLugarProduccion;
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        // Limpiar variables de selección
        idPlagaSeleccionada = -1;
        idCultivoSeleccionado = -1;
        idInspeccionSeleccionada = -1;
        idLugarProduccionSeleccionado = -1;
        
        cmbPlaga.setSelectedIndex(-1);
        cmbCultivo.setSelectedIndex(-1);
        cmbInspeccion.setSelectedIndex(-1);
        cmbLugarProduccion.setSelectedIndex(-1);
        txtTotalPlantas.setText("");
        cmbNivelIncidencia.setSelectedIndex(0);
        tablaRelaciones.clearSelection();
        cmbPlaga.requestFocus();
    }

    private void seleccionarRelacionDeTabla() {
        int filaSeleccionada = tablaRelaciones.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // Obtener los nombres de la tabla
            String nombrePlaga = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
            String nombreCultivo = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
            String nombreInspeccion = modeloTabla.getValueAt(filaSeleccionada, 2).toString();
            String nombreLugarProduccion = modeloTabla.getValueAt(filaSeleccionada, 3).toString();
            
            // Buscar los textos correspondientes en los combos
            seleccionarEnCombo(cmbPlaga, nombrePlaga);
            seleccionarEnCombo(cmbCultivo, nombreCultivo);
            seleccionarEnCombo(cmbInspeccion, nombreInspeccion);
            seleccionarEnCombo(cmbLugarProduccion, nombreLugarProduccion);
            
            // Actualizar total de plantas y nivel de incidencia
            txtTotalPlantas.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
            String nivelIncidencia = modeloTabla.getValueAt(filaSeleccionada, 5).toString();
            cmbNivelIncidencia.setSelectedItem(nivelIncidencia);
            
            // Guardar los IDs de la relación seleccionada
            guardarIdsSeleccionados();
        }
    }

    private void seleccionarEnCombo(JComboBox<String> combo, String texto) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equals(texto)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        // Si no encuentra exacto, buscar por contenido
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).contains(texto)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void guardarIdsSeleccionados() {
        // Obtener IDs de los combos seleccionados
        if (cmbPlaga.getSelectedItem() != null) {
            idPlagaSeleccionada = mapaPlagas.get(cmbPlaga.getSelectedItem().toString());
        }
        if (cmbCultivo.getSelectedItem() != null) {
            idCultivoSeleccionado = mapaCultivos.get(cmbCultivo.getSelectedItem().toString());
        }
        if (cmbInspeccion.getSelectedItem() != null) {
            idInspeccionSeleccionada = mapaInspecciones.get(cmbInspeccion.getSelectedItem().toString());
        }
        if (cmbLugarProduccion.getSelectedItem() != null) {
            idLugarProduccionSeleccionado = mapaLugaresProduccion.get(cmbLugarProduccion.getSelectedItem().toString());
        }
    }

    private void agregarRelacion() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            String plagaSeleccionada = (String) cmbPlaga.getSelectedItem();
            String cultivoSeleccionado = (String) cmbCultivo.getSelectedItem();
            String inspeccionSeleccionada = (String) cmbInspeccion.getSelectedItem();
            String lugarSeleccionado = (String) cmbLugarProduccion.getSelectedItem();
            
            int idPlaga = mapaPlagas.get(plagaSeleccionada);
            int idCultivo = mapaCultivos.get(cultivoSeleccionado);
            int idInspeccion = mapaInspecciones.get(inspeccionSeleccionada);
            int idLugarProduccion = mapaLugaresProduccion.get(lugarSeleccionado);
            int totalPlantas = Integer.parseInt(txtTotalPlantas.getText().trim());
            String nivelIncidencia = (String) cmbNivelIncidencia.getSelectedItem();

            // Verificar si ya existe la relación
            if (controller.existeRelacion(idPlaga, idCultivo, idInspeccion, idLugarProduccion)) {
                mostrarMensajeError("Ya existe una relación con los mismos elementos seleccionados");
                return;
            }

            RelacionAsociados nuevaRelacion = new RelacionAsociados(
                idPlaga, idCultivo, idInspeccion, idLugarProduccion, totalPlantas, nivelIncidencia
            );

            if (controller.agregarRelacion(nuevaRelacion)) {
                mostrarMensajeExito("Relación agregada exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar la relación a la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void actualizarRelacion() {
        // Verificar que hay una relación seleccionada
        if (idPlagaSeleccionada == -1 || idCultivoSeleccionado == -1 || 
            idInspeccionSeleccionada == -1 || idLugarProduccionSeleccionado == -1) {
            mostrarMensajeError("Seleccione una relación de la tabla para actualizar");
            return;
        }

        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            String plagaSeleccionada = (String) cmbPlaga.getSelectedItem();
            String cultivoSeleccionado = (String) cmbCultivo.getSelectedItem();
            String inspeccionSeleccionada = (String) cmbInspeccion.getSelectedItem();
            String lugarSeleccionado = (String) cmbLugarProduccion.getSelectedItem();
            
            int idPlaga = mapaPlagas.get(plagaSeleccionada);
            int idCultivo = mapaCultivos.get(cultivoSeleccionado);
            int idInspeccion = mapaInspecciones.get(inspeccionSeleccionada);
            int idLugarProduccion = mapaLugaresProduccion.get(lugarSeleccionado);
            int totalPlantas = Integer.parseInt(txtTotalPlantas.getText().trim());
            String nivelIncidencia = (String) cmbNivelIncidencia.getSelectedItem();

            RelacionAsociados relacion = new RelacionAsociados(
                idPlaga, idCultivo, idInspeccion, idLugarProduccion, totalPlantas, nivelIncidencia
            );

            if (controller.actualizarRelacion(relacion)) {
                mostrarMensajeExito("Relación actualizada exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar la relación en la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarRelacion() {
        int filaSeleccionada = tablaRelaciones.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeError("Seleccione una relación de la tabla para eliminar");
            return;
        }

        try {
            // Obtener los IDs de la fila seleccionada
            guardarIdsSeleccionados();
            
            if (idPlagaSeleccionada == -1 || idCultivoSeleccionado == -1 || 
                idInspeccionSeleccionada == -1 || idLugarProduccionSeleccionado == -1) {
                mostrarMensajeError("No se pudo identificar la relación seleccionada");
                return;
            }

            String nombrePlaga = (String) cmbPlaga.getSelectedItem();
            String nombreCultivo = (String) cmbCultivo.getSelectedItem();

            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar la relación?\n" +
                "Plaga: " + nombrePlaga + "\n" +
                "Cultivo: " + nombreCultivo + "\n\n" +
                "Esta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (controller.eliminarRelacion(idPlagaSeleccionada, idCultivoSeleccionado, 
                                              idInspeccionSeleccionada, idLugarProduccionSeleccionado)) {
                    mostrarMensajeExito("Relación eliminada exitosamente");
                    cargarDatos();
                    limpiarFormulario();
                } else {
                    mostrarMensajeError("Error al eliminar la relación de la base de datos");
                }
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void buscarRelaciones() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre de plaga, cultivo o nivel de incidencia a buscar:",
            "Buscar Relaciones",
            JOptionPane.QUESTION_MESSAGE);

        if (criterio != null && !criterio.trim().isEmpty()) {
            try {
                List<RelacionAsociados> resultados = controller.buscarRelaciones(criterio.trim());
                limpiarTabla();
                
                if (resultados.isEmpty()) {
                    mostrarMensajeInformacion("No se encontraron relaciones con el criterio: " + criterio);
                } else {
                    for (RelacionAsociados relacion : resultados) {
                        String nombrePlaga = obtenerNombrePlaga(relacion.getIdPlaga());
                        String nombreCultivo = obtenerNombreCultivo(relacion.getIdCultivo());
                        String nombreInspeccion = obtenerNombreInspeccion(relacion.getIdInspeccion());
                        String nombreLugarProduccion = obtenerNombreLugarProduccion(relacion.getIdLugarProduccion());
                        
                        Object[] fila = {
                            nombrePlaga,
                            nombreCultivo,
                            nombreInspeccion,
                            nombreLugarProduccion,
                            relacion.getTotalPlantas(),
                            relacion.getNivelIncidencia()
                        };
                        modeloTabla.addRow(fila);
                    }
                    mostrarMensajeExito("Se encontraron " + resultados.size() + " relación(es)");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error durante la búsqueda: " + ex.getMessage());
            }
        } else if (criterio != null) {
            cargarDatos();
        }
    }

    private boolean validarCamposObligatorios() {
        if (cmbPlaga.getSelectedItem() == null) {
            mostrarMensajeError("Debe seleccionar una plaga");
            cmbPlaga.requestFocus();
            return false;
        }
        if (cmbCultivo.getSelectedItem() == null) {
            mostrarMensajeError("Debe seleccionar un cultivo");
            cmbCultivo.requestFocus();
            return false;
        }
        if (cmbInspeccion.getSelectedItem() == null) {
            mostrarMensajeError("Debe seleccionar una inspección");
            cmbInspeccion.requestFocus();
            return false;
        }
        if (cmbLugarProduccion.getSelectedItem() == null) {
            mostrarMensajeError("Debe seleccionar un lugar de producción");
            cmbLugarProduccion.requestFocus();
            return false;
        }
        if (txtTotalPlantas.getText().trim().isEmpty()) {
            mostrarMensajeError("El campo Total Plantas es obligatorio");
            txtTotalPlantas.requestFocus();
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
            new GestionRelacionesAsociados().setVisible(true);
        });
    }
}