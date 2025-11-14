package vista;

import controlador.InspeccionController;
import controlador.InspectorController;
import modelo.Inspeccion;
import modelo.Inspector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GestionInspeccionesInspector extends JFrame {
    private JTable tablaInspecciones;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtFecha, txtEstado, txtObservaciones;
    private JComboBox<String> cmbInspector;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar, btnRefrescar, btnProgramar;
    private InspeccionController controller;
    private InspectorController inspectorController;
    
    // Variable para almacenar el ID de la inspección seleccionada internamente
    private int idInspeccionSeleccionada = -1;
    // Calendario popup
    private JDialog calendarioDialog;
    private JPanel panelCalendario;
    private JLabel lblMesAnio;
    private int mesActual;
    private int anioActual;

    public GestionInspeccionesInspector() {
        this.controller = new InspeccionController();
        this.inspectorController = new InspectorController();
        
        // Inicializar variables del calendario
        Calendar cal = Calendar.getInstance();
        this.mesActual = cal.get(Calendar.MONTH);
        this.anioActual = cal.get(Calendar.YEAR);
        
        initComponents();
        crearCalendarioPopup();
        cargarDatos();
        cargarCombos();
    }

    private void initComponents() {
        setTitle("Gestión de Inspecciones - Modo Inspector");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
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
            BorderFactory.createLineBorder(new Color(230, 126, 34), 2),
            "Programar Inspección - Modo Inspector"
        ));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setPreferredSize(new Dimension(350, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Campo ID oculto
        txtId = new JTextField();
        txtId.setVisible(false);

        // Campo Fecha con CALENDARIO
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Fecha:*"), gbc);
        gbc.gridx = 1;
        
        JPanel panelFecha = new JPanel(new BorderLayout());
        panelFecha.setBackground(Color.WHITE);
        txtFecha = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtFecha.setToolTipText("Haga clic en el botón del calendario para seleccionar fecha");
        panelFecha.add(txtFecha, BorderLayout.CENTER);
        
        // Botón para abrir calendario
        JButton btnCalendario = new JButton("📅");
        btnCalendario.setToolTipText("Abrir calendario para seleccionar fecha");
        btnCalendario.setBackground(new Color(230, 126, 34));
        btnCalendario.setForeground(Color.WHITE);
        btnCalendario.setFocusPainted(false);
        btnCalendario.setBorderPainted(false);
        btnCalendario.addActionListener(e -> mostrarCalendario());
        panelFecha.add(btnCalendario, BorderLayout.EAST);
        panelFormulario.add(panelFecha, gbc);

        // Campo Estado con sugerencias para inspector
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Estado:*"), gbc);
        gbc.gridx = 1;
        
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBackground(Color.WHITE);
        txtEstado = new JTextField("Programada");
        txtEstado.setToolTipText("Estados sugeridos: Programada, Pendiente, Agendada, En proceso");
        panelEstado.add(txtEstado, BorderLayout.CENTER);
        
        JButton btnSugerirEstado = new JButton("💡");
        btnSugerirEstado.setToolTipText("Sugerencias de estado");
        btnSugerirEstado.setBorderPainted(false);
        btnSugerirEstado.setContentAreaFilled(false);
        btnSugerirEstado.setFocusPainted(false);
        btnSugerirEstado.addActionListener(e -> mostrarSugerenciasEstado());
        panelEstado.add(btnSugerirEstado, BorderLayout.EAST);
        panelFormulario.add(panelEstado, gbc);

        // Campo Observaciones
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        txtObservaciones = new JTextField();
        txtObservaciones.setToolTipText("Observaciones para la inspección programada");
        panelFormulario.add(txtObservaciones, gbc);

        // ComboBox Inspector
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Inspector:*"), gbc);
        gbc.gridx = 1;
        cmbInspector = new JComboBox<>();
        panelFormulario.add(cmbInspector, gbc);

        // Información para el inspector
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        JLabel lblInfoInspector = new JLabel("<html><small>💡 Haga clic en 📅 para abrir el calendario y seleccionar fecha fácilmente</small></html>");
        lblInfoInspector.setForeground(new Color(100, 100, 100));
        lblInfoInspector.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panelFormulario.add(lblInfoInspector, gbc);

        // Panel de botones del formulario
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
        
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Observaciones");
        modeloTabla.addColumn("Inspector");

        tablaInspecciones = new JTable(modeloTabla);
        tablaInspecciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInspecciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaInspecciones.getTableHeader().setBackground(new Color(230, 126, 34));
        tablaInspecciones.getTableHeader().setForeground(Color.WHITE);
        tablaInspecciones.setRowHeight(25);
        
        // Renderer personalizado para resaltar inspecciones futuras
        tablaInspecciones.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                    c.setForeground(Color.BLACK);
                    
                    // Resaltar inspecciones futuras
                    try {
                        String fechaStr = modeloTabla.getValueAt(row, 0).toString();
                        String estado = modeloTabla.getValueAt(row, 1).toString();
                        Date fechaInspeccion = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
                        Date hoy = new Date();
                        
                        if (fechaInspeccion.after(hoy) && ("Programada".equals(estado) || "Pendiente".equals(estado) || "Agendada".equals(estado))) {
                            c.setBackground(new Color(230, 255, 230)); // Verde claro para futuras
                        }
                    } catch (Exception e) {
                        // Ignorar errores de parseo
                    }
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
            "Lista de Inspecciones - Modo Inspector"
        ));

        return scrollTabla;
    }

    private JPanel crearPanelBotonesPrincipales() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnProgramar = crearBoton("🔄 Programar Futura", new Color(230, 126, 34));
        btnAgregar = crearBoton("➕ Agregar", new Color(39, 174, 96));
        btnActualizar = crearBoton("✏️ Actualizar", new Color(41, 128, 185));
        btnEliminar = crearBoton("🗑️ Eliminar", new Color(231, 76, 60));
        btnRefrescar = crearBoton("🔄 Refrescar", new Color(155, 89, 182));
        
        // Tooltips especializados para inspector
        btnProgramar.setToolTipText("Configurar automáticamente para programar inspección futura");
        btnAgregar.setToolTipText("Agregar nueva inspección a la base de datos");
        btnActualizar.setToolTipText("Actualizar la inspección seleccionada");
        btnEliminar.setToolTipText("Eliminar la inspección seleccionada");
        btnRefrescar.setToolTipText("Actualizar la tabla con los últimos datos");

        panelBotones.add(btnProgramar);
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
        boton.setPreferredSize(new Dimension(140, 35));
        return boton;
    }

    private void cargarCombos() {
        // Cargar inspectores
        List<Inspector> inspectores = inspectorController.obtenerTodosInspectores();
        cmbInspector.removeAllItems();
        for (Inspector inspector : inspectores) {
            cmbInspector.addItem(inspector.getNombresCompletos() + " (" + inspector.getIdInspector() + ")");
        }
    }

    private void agregarActionListeners() {
        btnProgramar.addActionListener(e -> programarInspeccionFutura());
        btnAgregar.addActionListener(e -> agregarInspeccion());
        btnActualizar.addActionListener(e -> actualizarInspeccion());
        btnEliminar.addActionListener(e -> eliminarInspeccion());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarInspecciones());
        btnRefrescar.addActionListener(e -> cargarDatos());

        tablaInspecciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaInspecciones.getSelectedRow() != -1) {
                seleccionarInspeccionDeTabla();
            }
        });
    }

    // MÉTODOS DEL CALENDARIO
    private void crearCalendarioPopup() {
        calendarioDialog = new JDialog(this, "Seleccionar Fecha", true);
        calendarioDialog.setSize(300, 350);
        calendarioDialog.setResizable(false);
        
        // Panel principal del calendario
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(Color.WHITE);
        
        // Panel de control (mes y año)
        JPanel panelControl = new JPanel(new BorderLayout());
        panelControl.setBackground(new Color(230, 126, 34));
        
        // Botones de navegación
        JPanel panelNavegacion = new JPanel(new GridLayout(1, 2));
        panelNavegacion.setBackground(new Color(230, 126, 34));
        
        JButton btnMesAnterior = new JButton("◀");
        btnMesAnterior.setBackground(new Color(210, 106, 14));
        btnMesAnterior.setForeground(Color.WHITE);
        btnMesAnterior.setFocusPainted(false);
        btnMesAnterior.setBorderPainted(false);
        btnMesAnterior.addActionListener(e -> cambiarMes(-1));
        
        JButton btnMesSiguiente = new JButton("▶");
        btnMesSiguiente.setBackground(new Color(210, 106, 14));
        btnMesSiguiente.setForeground(Color.WHITE);
        btnMesSiguiente.setFocusPainted(false);
        btnMesSiguiente.setBorderPainted(false);
        btnMesSiguiente.addActionListener(e -> cambiarMes(1));
        
        panelNavegacion.add(btnMesAnterior);
        panelNavegacion.add(btnMesSiguiente);
        
        // Label del mes y año
        lblMesAnio = new JLabel("", JLabel.CENTER);
        lblMesAnio.setForeground(Color.WHITE);
        lblMesAnio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        panelControl.add(panelNavegacion, BorderLayout.WEST);
        panelControl.add(lblMesAnio, BorderLayout.CENTER);
        
        // Panel del calendario
        panelCalendario = new JPanel(new GridLayout(0, 7, 2, 2));
        panelCalendario.setBackground(Color.WHITE);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(Color.WHITE);
        
        JButton btnHoy = new JButton("Hoy");
        btnHoy.setBackground(new Color(39, 174, 96));
        btnHoy.setForeground(Color.WHITE);
        btnHoy.setFocusPainted(false);
        btnHoy.addActionListener(e -> seleccionarFechaHoy());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(192, 57, 43));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> calendarioDialog.setVisible(false));
        
        panelBotones.add(btnHoy);
        panelBotones.add(btnCancelar);
        
        panelPrincipal.add(panelControl, BorderLayout.NORTH);
        panelPrincipal.add(panelCalendario, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        calendarioDialog.add(panelPrincipal);
        actualizarCalendario();
    }

    private void actualizarCalendario() {
        // Actualizar label
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        lblMesAnio.setText(meses[mesActual] + " " + anioActual);
        
        // Limpiar panel
        panelCalendario.removeAll();
        
        // Agregar encabezados de días
        String[] diasSemana = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
        for (String dia : diasSemana) {
            JLabel lblDia = new JLabel(dia, JLabel.CENTER);
            lblDia.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblDia.setForeground(new Color(100, 100, 100));
            lblDia.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            panelCalendario.add(lblDia);
        }
        
        // Obtener primer día del mes
        Calendar cal = Calendar.getInstance();
        cal.set(anioActual, mesActual, 1);
        int primerDiaSemana = cal.get(Calendar.DAY_OF_WEEK) - 1; // Domingo = 0
        
        // Obtener número de días en el mes
        int diasEnMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // Agregar espacios vacíos para alinear el primer día
        for (int i = 0; i < primerDiaSemana; i++) {
            panelCalendario.add(new JLabel(""));
        }
        
        // Agregar botones para cada día
        Calendar hoy = Calendar.getInstance();
        for (int dia = 1; dia <= diasEnMes; dia++) {
            final int diaSeleccionado = dia;
            JButton btnDia = new JButton(String.valueOf(dia));
            btnDia.setFocusPainted(false);
            btnDia.setBorderPainted(false);
            btnDia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            // Verificar si es hoy
            if (dia == hoy.get(Calendar.DAY_OF_MONTH) && 
                mesActual == hoy.get(Calendar.MONTH) && 
                anioActual == hoy.get(Calendar.YEAR)) {
                btnDia.setBackground(new Color(52, 152, 219));
                btnDia.setForeground(Color.WHITE);
            } else {
                btnDia.setBackground(Color.WHITE);
                btnDia.setForeground(Color.BLACK);
            }
            
            // Verificar si es fecha futura (para resaltar)
            cal.set(anioActual, mesActual, dia);
            if (cal.after(hoy)) {
                btnDia.setBackground(new Color(230, 255, 230)); // Verde claro para futuras
            }
            
            btnDia.addActionListener(e -> seleccionarFecha(diaSeleccionado));
            panelCalendario.add(btnDia);
        }
        
        panelCalendario.revalidate();
        panelCalendario.repaint();
    }

    private void cambiarMes(int cambio) {
        mesActual += cambio;
        if (mesActual < 0) {
            mesActual = 11;
            anioActual--;
        } else if (mesActual > 11) {
            mesActual = 0;
            anioActual++;
        }
        actualizarCalendario();
    }

    private void seleccionarFecha(int dia) {
        Calendar cal = Calendar.getInstance();
        cal.set(anioActual, mesActual, dia);
        Date fechaSeleccionada = cal.getTime();
        
        // Formatear fecha como YYYY-MM-DD
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        txtFecha.setText(sdf.format(fechaSeleccionada));
        
        // Si es fecha futura, sugerir estado "Programada"
        Calendar hoy = Calendar.getInstance();
        if (cal.after(hoy)) {
            txtEstado.setText("Programada");
            if (txtObservaciones.getText().isEmpty()) {
                txtObservaciones.setText("Inspección programada por el inspector - pendiente de realización");
            }
            
            JOptionPane.showMessageDialog(this,
                "✅ Fecha seleccionada: " + sdf.format(fechaSeleccionada) + "\n\n" +
                "📊 Estado establecido como 'Programada'\n" +
                "👨‍🌾 El productor podrá ver esta inspección en 'Próximas Inspecciones'",
                "Fecha Programada - Modo Inspector",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        calendarioDialog.setVisible(false);
    }

    private void seleccionarFechaHoy() {
        Calendar hoy = Calendar.getInstance();
        mesActual = hoy.get(Calendar.MONTH);
        anioActual = hoy.get(Calendar.YEAR);
        seleccionarFecha(hoy.get(Calendar.DAY_OF_MONTH));
    }

    private void mostrarCalendario() {
        // Actualizar el calendario con la fecha actual
        Calendar ahora = Calendar.getInstance();
        mesActual = ahora.get(Calendar.MONTH);
        anioActual = ahora.get(Calendar.YEAR);
        actualizarCalendario();
        
        // Posicionar el calendario cerca del campo de fecha
        Point location = txtFecha.getLocationOnScreen();
        calendarioDialog.setLocation(
            Math.max(0, location.x - 50), 
            Math.max(0, location.y + txtFecha.getHeight())
        );
        calendarioDialog.setVisible(true);
    }

    // MÉTODOS DE LA LÓGICA DE NEGOCIO
    private void mostrarSugerenciasEstado() {
        String[] estadosSugeridos = {
            "Programada - Para inspecciones futuras",
            "Pendiente - Por realizar", 
            "Agendada - Confirmada con productor",
            "En proceso - Actualmente en ejecución",
            "Completada - Finalizada exitosamente",
            "Cancelada - No realizada"
        };
        
        String seleccion = (String) JOptionPane.showInputDialog(this,
            "Seleccione un estado sugerido:",
            "Sugerencias de Estado - Modo Inspector",
            JOptionPane.QUESTION_MESSAGE,
            null,
            estadosSugeridos,
            estadosSugeridos[0]);
            
        if (seleccion != null) {
            // Extraer solo la primera palabra del estado
            String estado = seleccion.split(" - ")[0];
            txtEstado.setText(estado);
        }
    }

    private void programarInspeccionFutura() {
        // Establecer valores por defecto para una inspección futura
        txtFecha.setText(obtenerFechaDias(7)); // Por defecto en 7 días
        txtEstado.setText("Programada");
        txtObservaciones.setText("Inspección programada por el inspector - pendiente de realización");
        
        // Seleccionar primer inspector si está disponible
        if (cmbInspector.getItemCount() > 0) cmbInspector.setSelectedIndex(0);
        
        JOptionPane.showMessageDialog(this,
            "✅ Formulario configurado para programar inspección futura\n\n" +
            "📅 Fecha establecida: " + obtenerFechaDias(7) + " (en 7 días)\n" +
            "📊 Estado: Programada\n" +
            "👨‍🌾 El productor podrá ver esta inspección en 'Próximas Inspecciones'\n\n" +
            "💡 Puede cambiar la fecha haciendo clic en el botón 📅 del calendario",
            "Programar Inspección - Modo Inspector",
            JOptionPane.INFORMATION_MESSAGE);
            
        txtObservaciones.requestFocus();
    }

    private String obtenerFechaDias(int dias) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dias);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    private void cargarDatos() {
        limpiarTabla();
        List<Inspeccion> inspecciones = controller.obtenerTodasInspecciones();
        
        if (inspecciones.isEmpty()) {
            mostrarMensajeInformacion("No hay inspecciones registradas en la base de datos.");
        } else {
            int inspeccionesFuturas = 0;
            for (Inspeccion inspeccion : inspecciones) {
                Inspector inspector = inspectorController.obtenerInspector(inspeccion.getIdInspector());
                
                Object[] fila = {
                    new SimpleDateFormat("yyyy-MM-dd").format(inspeccion.getFechaInspeccion()),
                    inspeccion.getEstado(),
                    inspeccion.getObservaciones(),
                    inspector != null ? inspector.getNombresCompletos() : "N/A"
                };
                modeloTabla.addRow(fila);
                
                // Contar inspecciones futuras
                if (inspeccion.getFechaInspeccion().after(new Date())) {
                    inspeccionesFuturas++;
                }
            }
            
            String mensaje = "Se cargaron " + inspecciones.size() + " inspección(es) desde la base de datos.";
            if (inspeccionesFuturas > 0) {
                mensaje += "\n📅 " + inspeccionesFuturas + " inspección(es) programada(s) para el futuro.";
            }
            mostrarMensajeInformacion(mensaje);
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtEstado.setText("Programada");
        txtObservaciones.setText("");
        if (cmbInspector.getItemCount() > 0) cmbInspector.setSelectedIndex(0);
        tablaInspecciones.clearSelection();
        idInspeccionSeleccionada = -1;
        txtObservaciones.requestFocus();
    }

    private void seleccionarInspeccionDeTabla() {
        int filaSeleccionada = tablaInspecciones.getSelectedRow();
        if (filaSeleccionada >= 0) {
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
            
            txtFecha.setText(fecha);
            txtEstado.setText(estado);
            txtObservaciones.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            
            // Seleccionar el inspector correspondiente en el combo
            seleccionarEnCombo(cmbInspector, inspectorNombre);
        }
    }

    private void seleccionarEnCombo(JComboBox<String> combo, String valor) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).contains(valor)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void agregarInspeccion() {
        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Inspeccion nuevaInspeccion = new Inspeccion();
            nuevaInspeccion.setFechaInspeccion(new SimpleDateFormat("yyyy-MM-dd").parse(txtFecha.getText()));
            nuevaInspeccion.setEstado(txtEstado.getText().trim());
            nuevaInspeccion.setObservaciones(txtObservaciones.getText().trim());
            
            // Obtener ID del inspector
            nuevaInspeccion.setIdInspector(obtenerIdDeCombo(cmbInspector.getSelectedItem().toString()));

            if (controller.agregarInspeccion(nuevaInspeccion)) {
                // Verificar si es una inspección futura para mostrar mensaje especial
                Date fechaInspeccion = nuevaInspeccion.getFechaInspeccion();
                Date hoy = new Date();
                String mensajeExito = "✅ Inspección agregada exitosamente";
                
                if (fechaInspeccion.after(hoy)) {
                    mensajeExito += "\n\n📅 Esta inspección aparecerá en 'Próximas Inspecciones' del productor";
                }
                
                mostrarMensajeExito(mensajeExito);
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al agregar la inspección a la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void actualizarInspeccion() {
        if (idInspeccionSeleccionada == -1) {
            mostrarMensajeError("Seleccione una inspección de la tabla para actualizar");
            return;
        }

        if (!validarCamposObligatorios()) {
            return;
        }

        try {
            Inspeccion inspeccion = new Inspeccion();
            inspeccion.setIdInspeccion(idInspeccionSeleccionada);
            inspeccion.setFechaInspeccion(new SimpleDateFormat("yyyy-MM-dd").parse(txtFecha.getText()));
            inspeccion.setEstado(txtEstado.getText().trim());
            inspeccion.setObservaciones(txtObservaciones.getText().trim());
            
            // Obtener ID del inspector
            inspeccion.setIdInspector(obtenerIdDeCombo(cmbInspector.getSelectedItem().toString()));

            if (controller.actualizarInspeccion(inspeccion)) {
                mostrarMensajeExito("✅ Inspección actualizada exitosamente");
                cargarDatos();
                limpiarFormulario();
            } else {
                mostrarMensajeError("Error al actualizar la inspección en la base de datos");
            }
        } catch (Exception ex) {
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminarInspeccion() {
        int filaSeleccionada = tablaInspecciones.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeError("Seleccione una inspección de la tabla para eliminar");
            return;
        }

        if (idInspeccionSeleccionada == -1) {
            mostrarMensajeError("No se pudo identificar la inspección seleccionada");
            return;
        }

        String fecha = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
        String estado = modeloTabla.getValueAt(filaSeleccionada, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar la inspección?\n\n" +
            "📅 Fecha: " + fecha + "\n" +
            "📊 Estado: " + estado + "\n\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación - Modo Inspector",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (controller.eliminarInspeccion(idInspeccionSeleccionada)) {
                    mostrarMensajeExito("✅ Inspección eliminada exitosamente");
                    cargarDatos();
                    limpiarFormulario();
                } else {
                    mostrarMensajeError("Error al eliminar la inspección de la base de datos");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error inesperado: " + ex.getMessage());
            }
        }
    }

    private void buscarInspecciones() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el estado, fecha o observaciones a buscar:",
            "Buscar Inspecciones - Modo Inspector",
            JOptionPane.QUESTION_MESSAGE);

        if (criterio != null && !criterio.trim().isEmpty()) {
            try {
                List<Inspeccion> resultados = controller.buscarInspecciones(criterio.trim());
                limpiarTabla();
                
                if (resultados.isEmpty()) {
                    mostrarMensajeInformacion("No se encontraron inspecciones con el criterio: " + criterio);
                } else {
                    for (Inspeccion inspeccion : resultados) {
                        Inspector inspector = inspectorController.obtenerInspector(inspeccion.getIdInspector());
                        
                        Object[] fila = {
                            new SimpleDateFormat("yyyy-MM-dd").format(inspeccion.getFechaInspeccion()),
                            inspeccion.getEstado(),
                            inspeccion.getObservaciones(),
                            inspector != null ? inspector.getNombresCompletos() : "N/A"
                        };
                        modeloTabla.addRow(fila);
                    }
                    mostrarMensajeExito("✅ Se encontraron " + resultados.size() + " inspección(es)");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error durante la búsqueda: " + ex.getMessage());
            }
        } else if (criterio != null) {
            cargarDatos();
        }
    }

    private boolean validarCamposObligatorios() {
        if (txtFecha.getText().trim().isEmpty() || txtEstado.getText().trim().isEmpty() || 
            cmbInspector.getSelectedItem() == null) {
            mostrarMensajeError("Los campos Fecha, Estado e Inspector son obligatorios");
            if (txtFecha.getText().trim().isEmpty()) {
                txtFecha.requestFocus();
            } else if (txtEstado.getText().trim().isEmpty()) {
                txtEstado.requestFocus();
            }
            return false;
        }
        
        // Validar formato de fecha (permitir fechas futuras)
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(txtFecha.getText().trim());
        } catch (Exception e) {
            mostrarMensajeError("El formato de fecha debe ser YYYY-MM-DD (Ej: 2024-12-25)");
            txtFecha.requestFocus();
            return false;
        }
        
        return true;
    }

    private int obtenerIdDeCombo(String itemCombo) {
        return Integer.parseInt(itemCombo.substring(itemCombo.lastIndexOf("(") + 1, itemCombo.lastIndexOf(")")));
    }

    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito - Modo Inspector", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error - Modo Inspector", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensajeInformacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información - Modo Inspector", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GestionInspeccionesInspector().setVisible(true);
        });
    }
}