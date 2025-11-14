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
import java.util.Date;
import java.util.List;
import java.util.Calendar;

public class ProximasInspeccionesProductor extends JFrame {
    private JTable tablaInspecciones;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cmbRango;
    private JButton btnRefrescar, btnCerrar, btnHoy;
    private InspeccionController controller;
    private InspectorController inspectorController;

    public ProximasInspeccionesProductor() {
        this.controller = new InspeccionController();
        this.inspectorController = new InspectorController();
        initComponents();
        cargarProximasInspecciones(7); // Por defecto, próximos 7 días
    }

    private void initComponents() {
        setTitle("Próximas Inspecciones - Modo Productor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600); // Reducido el ancho
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Panel de controles
        JPanel panelControles = crearPanelControles();

        // Crear tabla
        JScrollPane scrollTabla = crearScrollTabla();

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();

        panelPrincipal.add(panelControles, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
        agregarActionListeners();
    }

    private JPanel crearPanelControles() {
        JPanel panelControles = new JPanel(new GridBagLayout());
        panelControles.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(39, 174, 96), 1),
            "Filtrar Próximas Inspecciones"
        ));
        panelControles.setBackground(Color.WHITE);
        panelControles.setPreferredSize(new Dimension(0, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        panelControles.add(new JLabel("Mostrar inspecciones para:"), gbc);

        // Combo de rangos
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.3;
        cmbRango = new JComboBox<>(new String[]{
            "Próximos 7 días", "Próximos 15 días", "Próximos 30 días", "Este mes", "Próximo mes"
        });
        cmbRango.setToolTipText("Seleccione el rango de fechas para mostrar las inspecciones");
        panelControles.add(cmbRango, gbc);

        // Botón Hoy
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.weightx = 0.1;
        btnHoy = new JButton("Hoy");
        btnHoy.setBackground(new Color(243, 156, 18));
        btnHoy.setForeground(Color.WHITE);
        btnHoy.setFocusPainted(false);
        btnHoy.setToolTipText("Mostrar solo las inspecciones de hoy");
        panelControles.add(btnHoy, gbc);

        // Información
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        JLabel lblInfo = new JLabel("Visualice las inspecciones programadas para los próximos días");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(Color.GRAY);
        panelControles.add(lblInfo, gbc);

        return panelControles;
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
        modeloTabla.addColumn("Días Restantes");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Inspector");
        modeloTabla.addColumn("Observaciones");
        // Eliminada columna "Cultivo"

        // Configuración de la tabla
        tablaInspecciones = new JTable(modeloTabla);
        tablaInspecciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInspecciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaInspecciones.getTableHeader().setBackground(new Color(39, 174, 96));
        tablaInspecciones.getTableHeader().setForeground(Color.WHITE);
        tablaInspecciones.setRowHeight(25);
        
        // Renderer personalizado para resaltar fechas próximas
        tablaInspecciones.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                    c.setForeground(Color.BLACK);
                    
                    // Resaltar inspecciones próximas (columna 1 - Días Restantes)
                    if (column == 1 && value != null) {
                        try {
                            String diasStr = value.toString();
                            if (diasStr.equals("HOY")) {
                                c.setBackground(new Color(255, 230, 230)); // Rojo claro para hoy
                                c.setForeground(new Color(192, 57, 43));
                            } else if (diasStr.equals("Mañana")) {
                                c.setBackground(new Color(255, 250, 230)); // Amarillo claro para mañana
                                c.setForeground(new Color(243, 156, 18));
                            } else if (diasStr.contains("días")) {
                                int diasRestantes = Integer.parseInt(diasStr.split(" ")[0]);
                                if (diasRestantes <= 3) {
                                    c.setBackground(new Color(230, 255, 230)); // Verde claro para próximos 3 días
                                    c.setForeground(new Color(39, 174, 96));
                                }
                            }
                        } catch (NumberFormatException e) {
                            // Ignorar si no es un número
                        }
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
            BorderFactory.createLineBorder(new Color(39, 174, 96), 2),
            "Inspecciones Programadas (Solo Lectura)"
        ));

        return scrollTabla;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnRefrescar = crearBoton("Refrescar", new Color(52, 152, 219));
        btnCerrar = crearBoton("Cerrar", new Color(192, 57, 43));
        
        btnRefrescar.setToolTipText("Actualizar la lista de inspecciones");
        btnCerrar.setToolTipText("Cerrar la ventana");

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
        boton.setPreferredSize(new Dimension(120, 35));
        return boton;
    }

    private void agregarActionListeners() {
        cmbRango.addActionListener(e -> {
            String seleccion = (String) cmbRango.getSelectedItem();
            int dias = obtenerDiasDesdeSeleccion(seleccion);
            cargarProximasInspecciones(dias);
        });
        
        btnHoy.addActionListener(e -> cargarInspeccionesHoy());
        btnRefrescar.addActionListener(e -> {
            String seleccion = (String) cmbRango.getSelectedItem();
            int dias = obtenerDiasDesdeSeleccion(seleccion);
            cargarProximasInspecciones(dias);
        });
        btnCerrar.addActionListener(e -> dispose());
        
        // Listener para selección en la tabla
        tablaInspecciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaInspecciones.getSelectedRow() != -1) {
                mostrarDetallesInspeccion();
            }
        });
    }

    private int obtenerDiasDesdeSeleccion(String seleccion) {
        switch (seleccion) {
            case "Próximos 7 días": return 7;
            case "Próximos 15 días": return 15;
            case "Próximos 30 días": return 30;
            case "Este mes": return obtenerDiasRestantesMes();
            case "Próximo mes": return obtenerDiasProximoMes();
            default: return 7;
        }
    }

    private int obtenerDiasRestantesMes() {
        Calendar cal = Calendar.getInstance();
        int diaActual = cal.get(Calendar.DAY_OF_MONTH);
        int diasEnMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return diasEnMes - diaActual;
    }

    private int obtenerDiasProximoMes() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        int diasEnProximoMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return diasEnProximoMes + 30; // Mes actual + próximo mes
    }

    private void cargarProximasInspecciones(int dias) {
        limpiarTabla();
        List<Inspeccion> todasInspecciones = controller.obtenerTodasInspecciones();
        Date hoy = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(hoy);
        cal.add(Calendar.DATE, dias);
        Date fechaLimite = cal.getTime();
        
        int contador = 0;
        
        for (Inspeccion inspeccion : todasInspecciones) {
            Date fechaInspeccion = inspeccion.getFechaInspeccion();
            
            // Solo inspecciones futuras o de hoy
            if (!fechaInspeccion.before(hoy) && !fechaInspeccion.after(fechaLimite)) {
                Inspector inspector = inspectorController.obtenerInspector(inspeccion.getIdInspector());
                
                // Calcular días restantes
                long diff = fechaInspeccion.getTime() - hoy.getTime();
                int diasRestantes = (int) (diff / (1000 * 60 * 60 * 24));
                
                String textoDiasRestantes;
                if (diasRestantes == 0) {
                    textoDiasRestantes = "HOY";
                } else if (diasRestantes == 1) {
                    textoDiasRestantes = "Mañana";
                } else {
                    textoDiasRestantes = diasRestantes + " días";
                }
                
                // CAMBIO: Eliminar cultivo de la fila
                Object[] fila = {
                    new SimpleDateFormat("yyyy-MM-dd").format(fechaInspeccion),
                    textoDiasRestantes,
                    inspeccion.getEstado(),
                    inspector != null ? inspector.getNombresCompletos() : "N/A",
                    inspeccion.getObservaciones()
                };
                modeloTabla.addRow(fila);
                contador++;
            }
        }
        
        if (contador == 0) {
            mostrarMensajeInformacion("No hay inspecciones programadas para los próximos " + dias + " días.");
        } else {
            mostrarMensajeExito("Se encontraron " + contador + " inspección(es) programadas para los próximos " + dias + " días.");
        }
    }

    private void cargarInspeccionesHoy() {
        limpiarTabla();
        List<Inspeccion> todasInspecciones = controller.obtenerTodasInspecciones();
        Date hoy = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String hoyStr = sdf.format(hoy);
        
        int contador = 0;
        
        for (Inspeccion inspeccion : todasInspecciones) {
            String fechaInspeccionStr = sdf.format(inspeccion.getFechaInspeccion());
            
            if (fechaInspeccionStr.equals(hoyStr)) {
                Inspector inspector = inspectorController.obtenerInspector(inspeccion.getIdInspector());
                
                // CAMBIO: Eliminar cultivo de la fila
                Object[] fila = {
                    fechaInspeccionStr,
                    "HOY",
                    inspeccion.getEstado(),
                    inspector != null ? inspector.getNombresCompletos() : "N/A",
                    inspeccion.getObservaciones()
                };
                modeloTabla.addRow(fila);
                contador++;
            }
        }
        
        if (contador == 0) {
            mostrarMensajeInformacion("No hay inspecciones programadas para hoy.");
        } else {
            mostrarMensajeExito("Se encontraron " + contador + " inspección(es) programadas para hoy.");
        }
    }

    private void mostrarDetallesInspeccion() {
        int filaSeleccionada = tablaInspecciones.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String fecha = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
            String diasRestantes = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
            String estado = modeloTabla.getValueAt(filaSeleccionada, 2).toString();
            String inspector = modeloTabla.getValueAt(filaSeleccionada, 3).toString();
            String observaciones = modeloTabla.getValueAt(filaSeleccionada, 4).toString();
            
            // CAMBIO: Eliminar referencia a cultivo en el mensaje
            String mensaje = String.format(
                "Detalles de la Inspección Programada:\n\n" +
                "📅 Fecha: %s\n" +
                "⏰ Tiempo: %s\n" +
                "📊 Estado: %s\n" +
                "👤 Inspector: %s\n" +
                "📝 Observaciones: %s",
                fecha, diasRestantes, estado, inspector,
                observaciones.isEmpty() ? "Sin observaciones" : observaciones
            );
            
            JOptionPane.showMessageDialog(this, mensaje, "Detalles de Inspección Programada", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMensajeInformacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProximasInspeccionesProductor().setVisible(true);
        });
    }
}