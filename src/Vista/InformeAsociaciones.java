package vista;

import controlador.InformeAsociacionesController;
import modelo.RelacionAsociados;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class InformeAsociaciones extends JFrame {
    
    private JTable tablaInforme;
    private DefaultTableModel modeloTabla;
    private JTextArea areaEstadisticas;
    private JButton btnRefrescar, btnBuscar, btnExportar, btnResumen, btnDiagnostico;
    private InformeAsociacionesController controller;

    public InformeAsociaciones() {
        this.controller = new InformeAsociacionesController();
        initComponents();
        cargarDatos();
        mostrarEstadisticas();
    }

    private void initComponents() {
        setTitle("Informe General de Asociaciones - Sistema Integrado");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Panel de estadísticas
        JPanel panelEstadisticas = crearPanelEstadisticas();
        
        // Panel de la tabla
        JScrollPane scrollTabla = crearScrollTabla();
        
        // Panel de botones
        JPanel panelBotones = crearPanelBotones();

        panelPrincipal.add(panelEstadisticas, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
        agregarActionListeners();
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panelEstadisticas = new JPanel(new BorderLayout());
        panelEstadisticas.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Estadísticas Generales"
        ));
        panelEstadisticas.setBackground(Color.WHITE);
        panelEstadisticas.setPreferredSize(new Dimension(0, 120));

        areaEstadisticas = new JTextArea();
        areaEstadisticas.setEditable(false);
        areaEstadisticas.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaEstadisticas.setBackground(new Color(248, 248, 248));
        areaEstadisticas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollEstadisticas = new JScrollPane(areaEstadisticas);
        panelEstadisticas.add(scrollEstadisticas, BorderLayout.CENTER);

        return panelEstadisticas;
    }

    private JScrollPane crearScrollTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloTabla.addColumn("Plaga");
        modeloTabla.addColumn("Cultivo");
        modeloTabla.addColumn("Lugar Producción");
        modeloTabla.addColumn("Total Plantas");
        modeloTabla.addColumn("Nivel Incidencia");
        modeloTabla.addColumn("Fecha Inspección");

        tablaInforme = new JTable(modeloTabla);
        tablaInforme.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInforme.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaInforme.getTableHeader().setBackground(new Color(52, 152, 219));
        tablaInforme.getTableHeader().setForeground(Color.WHITE);
        tablaInforme.setRowHeight(25);
        
        // Renderer para colorear por nivel de incidencia
        tablaInforme.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    // Colorear filas según el nivel de incidencia
                    String nivelIncidencia = (String) table.getValueAt(row, 4);
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

        JScrollPane scrollTabla = new JScrollPane(tablaInforme);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Detalle de Asociaciones"
        ));

        return scrollTabla;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnRefrescar = crearBoton("Refrescar Datos", new Color(41, 128, 185));
        btnBuscar = crearBoton("Buscar", new Color(52, 152, 219));
        btnResumen = crearBoton("Resumen Ejecutivo", new Color(39, 174, 96));
        btnExportar = crearBoton("Exportar Informe", new Color(155, 89, 182));
        btnDiagnostico = crearBoton("Diagnóstico", new Color(243, 156, 18)); // NUEVO BOTÓN
        
        btnRefrescar.setToolTipText("Actualizar el informe con los últimos datos");
        btnBuscar.setToolTipText("Buscar asociaciones por criterio específico");
        btnResumen.setToolTipText("Generar resumen ejecutivo del informe");
        btnExportar.setToolTipText("Exportar el informe a formato de texto");
        btnDiagnostico.setToolTipText("Ver diagnóstico de problemas de datos");
        
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnResumen);
        panelBotones.add(btnExportar);
        panelBotones.add(btnDiagnostico); // AGREGAR AL PANEL
        
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
        btnRefrescar.addActionListener(e -> {
            cargarDatos();
            mostrarEstadisticas();
        });
        
        btnBuscar.addActionListener(e -> buscarAsociaciones());
        btnResumen.addActionListener(e -> mostrarResumenEjecutivo());
        btnExportar.addActionListener(e -> exportarInforme());
        btnDiagnostico.addActionListener(e -> mostrarDiagnostico()); // NUEVO LISTENER
    }

    private void cargarDatos() {
        limpiarTabla();
        List<RelacionAsociados> relaciones = controller.obtenerInformeCompleto();
        
        if (relaciones.isEmpty()) {
            mostrarMensajeInformacion("No hay asociaciones registradas en el sistema.\n\n" +
                "Posibles causas:\n" +
                "• La tabla RELACION_ASOCIADOS está vacía\n" +
                "• Faltan datos en plagas, cultivos, inspecciones o lugares de producción\n" +
                "• Use el botón 'Diagnóstico' para más detalles");
        } else {
            for (RelacionAsociados relacion : relaciones) {
                Object[] fila = {
                    relacion.getNombrePlaga() != null ? relacion.getNombrePlaga() : "Plaga ID: " + relacion.getIdPlaga(),
                    relacion.getNombreCultivo() != null ? relacion.getNombreCultivo() : "Cultivo ID: " + relacion.getIdCultivo(),
                    relacion.getNombreLugarProduccion() != null ? relacion.getNombreLugarProduccion() : "No asignado",
                    relacion.getTotalPlantas(),
                    relacion.getNivelIncidencia() != null ? relacion.getNivelIncidencia() : "No especificado",
                    relacion.getFechaInspeccion() != null ? relacion.getFechaInspeccion() : "No asignada"
                };
                modeloTabla.addRow(fila);
            }
            mostrarMensajeExito("Datos cargados: " + relaciones.size() + " asociación(es) encontrada(s)");
        }
    }

    private void mostrarEstadisticas() {
        String estadisticas = controller.obtenerEstadisticasGenerales();
        areaEstadisticas.setText(estadisticas);
    }

    private void buscarAsociaciones() {
        String criterio = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre de plaga, cultivo o nivel de incidencia a buscar:",
            "Buscar Asociaciones",
            JOptionPane.QUESTION_MESSAGE);

        if (criterio != null && !criterio.trim().isEmpty()) {
            try {
                List<RelacionAsociados> resultados = controller.buscarRelaciones(criterio.trim());
                limpiarTabla();
                
                if (resultados.isEmpty()) {
                    mostrarMensajeInformacion("No se encontraron asociaciones con el criterio: " + criterio);
                } else {
                    for (RelacionAsociados relacion : resultados) {
                        Object[] fila = {
                            relacion.getNombrePlaga() != null ? relacion.getNombrePlaga() : "Plaga ID: " + relacion.getIdPlaga(),
                            relacion.getNombreCultivo() != null ? relacion.getNombreCultivo() : "Cultivo ID: " + relacion.getIdCultivo(),
                            relacion.getNombreLugarProduccion() != null ? relacion.getNombreLugarProduccion() : "No asignado",
                            relacion.getTotalPlantas(),
                            relacion.getNivelIncidencia() != null ? relacion.getNivelIncidencia() : "No especificado",
                            relacion.getFechaInspeccion() != null ? relacion.getFechaInspeccion() : "No asignada"
                        };
                        modeloTabla.addRow(fila);
                    }
                    mostrarMensajeExito("Se encontraron " + resultados.size() + " asociación(es)");
                }
            } catch (Exception ex) {
                mostrarMensajeError("Error durante la búsqueda: " + ex.getMessage());
            }
        } else if (criterio != null) {
            cargarDatos();
        }
    }

    private void mostrarResumenEjecutivo() {
        String resumen = controller.generarResumenEjecutivo();
        
        JTextArea areaResumen = new JTextArea(resumen);
        areaResumen.setEditable(false);
        areaResumen.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaResumen.setBackground(new Color(248, 248, 248));
        areaResumen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollResumen = new JScrollPane(areaResumen);
        scrollResumen.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollResumen, 
            "Resumen Ejecutivo - Informe de Asociaciones", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportarInforme() {
        try {
            StringBuilder contenido = new StringBuilder();
            contenido.append("INFORME GENERAL DE ASOCIACIONES\n");
            contenido.append("===============================\n\n");
            
            // Estadísticas
            contenido.append(controller.obtenerEstadisticasGenerales()).append("\n\n");
            
            // Resumen ejecutivo
            contenido.append(controller.generarResumenEjecutivo()).append("\n\n");
            
            // Detalle
            contenido.append("DETALLE COMPLETO DE ASOCIACIONES:\n");
            contenido.append("---------------------------------\n");
            
            List<RelacionAsociados> relaciones = controller.obtenerInformeCompleto();
            for (RelacionAsociados relacion : relaciones) {
                contenido.append(String.format(
                    "Plaga: %s | Cultivo: %s | Lugar: %s | Plantas: %d | Incidencia: %s | Fecha: %s\n",
                    relacion.getNombrePlaga() != null ? relacion.getNombrePlaga() : "Plaga ID: " + relacion.getIdPlaga(),
                    relacion.getNombreCultivo() != null ? relacion.getNombreCultivo() : "Cultivo ID: " + relacion.getIdCultivo(),
                    relacion.getNombreLugarProduccion() != null ? relacion.getNombreLugarProduccion() : "No asignado",
                    relacion.getTotalPlantas(),
                    relacion.getNivelIncidencia() != null ? relacion.getNivelIncidencia() : "No especificado",
                    relacion.getFechaInspeccion() != null ? relacion.getFechaInspeccion() : "No asignada"
                ));
            }
            
            // Mostrar en un área de texto para copiar
            JTextArea areaExportacion = new JTextArea(contenido.toString());
            areaExportacion.setEditable(false);
            areaExportacion.setFont(new Font("Consolas", Font.PLAIN, 11));
            areaExportacion.setBackground(new Color(248, 248, 248));
            areaExportacion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JScrollPane scrollExportacion = new JScrollPane(areaExportacion);
            scrollExportacion.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollExportacion, 
                "Informe Exportado - Copie el contenido", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception ex) {
            mostrarMensajeError("Error al exportar el informe: " + ex.getMessage());
        }
    }

    // NUEVO MÉTODO: Mostrar diagnóstico de problemas
    private void mostrarDiagnostico() {
        String diagnostico = controller.diagnosticarProblemas();
        
        JTextArea areaDiagnostico = new JTextArea(diagnostico);
        areaDiagnostico.setEditable(false);
        areaDiagnostico.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaDiagnostico.setBackground(new Color(248, 248, 248));
        areaDiagnostico.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollDiagnostico = new JScrollPane(areaDiagnostico);
        scrollDiagnostico.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(this, scrollDiagnostico, 
            "Diagnóstico del Informe", 
            JOptionPane.INFORMATION_MESSAGE);
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
            new InformeAsociaciones().setVisible(true);
        });
    }
}