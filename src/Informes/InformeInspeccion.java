/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Informe;

/**
 *
 * @author SALA-4
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import database.ConexionBD;

public class InformeInspeccion extends JFrame {
    private JTextArea areaInforme;
    private JButton btnGenerar;

    public InformeInspeccion() {
        setTitle("Informe de Inspecciones - Detallado");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Área de texto donde se muestra el informe
        areaInforme = new JTextArea();
        areaInforme.setEditable(false);
        areaInforme.setFont(new Font("Monospaced", Font.PLAIN, 10));
        JScrollPane scroll = new JScrollPane(areaInforme);
        panel.add(scroll, BorderLayout.CENTER);

        // Botón para generar informe
        btnGenerar = new JButton("Generar Informe de Inspecciones");
        panel.add(btnGenerar, BorderLayout.SOUTH);

        // Acción del botón
        btnGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarInforme();
            }
        });

        add(panel);
    }

    // Clase interna para almacenar información de inspecciones
    private class InspeccionInfo {
        int idInspeccion;
        String fecha;
        String estado;
        String observaciones;
        int idInspector;
        int idCultivo;
        String nombreCultivo;
        
        public InspeccionInfo(int idInspeccion, String fecha, String estado, String observaciones, 
                            int idInspector, int idCultivo, String nombreCultivo) {
            this.idInspeccion = idInspeccion;
            this.fecha = fecha;
            this.estado = estado;
            this.observaciones = observaciones;
            this.idInspector = idInspector;
            this.idCultivo = idCultivo;
            this.nombreCultivo = nombreCultivo;
        }
    }

    // Método que genera el informe de inspecciones desde la base de datos
    private void generarInforme() {
        StringBuilder informe = new StringBuilder();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            // Usar tu clase ConexionBD existente - conexión por sesión
            conn = ConexionBD.getConexionPorSesion();
            
            if (conn == null) {
                informe.append("❌ ERROR: No se pudo establecer conexión con la base de datos\n");
                areaInforme.setText(informe.toString());
                return;
            }
            
            // Consulta SOLO con las tablas que existen: inspeccion y cultivo
            String sql = "SELECT i.ID_INSPECCION, i.FECHA_INSPECCION, i.ESTADO, i.OBSERVACIONES, " +
                        "i.ID_INSPECTOR, i.ID_CULTIVO, c.NOMBRE_CULTIVO " +
                        "FROM inspeccion i " +
                        "LEFT JOIN cultivo c ON i.ID_CULTIVO = c.ID_CULTIVO " +
                        "ORDER BY i.FECHA_INSPECCION DESC, i.ID_INSPECCION";
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            informe.append("========================= INFORME DE INSPECCIONES - DETALLADO =========================\n");
            informe.append("Fecha del informe: ").append(LocalDate.now()).append("\n\n");

            informe.append("ID\tFecha\t\tEstado\t\tInspector\tCultivo(ID)\tCultivo(Nombre)\tObservaciones\n");
            informe.append("--------------------------------------------------------------------------------------------------------\n");
            
            // Variables para el resumen
            int totalInspecciones = 0;
            int inspeccionesAprobadas = 0;
            int inspeccionesRechazadas = 0;
            int inspeccionesPendientes = 0;
            
            // Listas para almacenar inspecciones
            List<InspeccionInfo> todasInspecciones = new ArrayList<>();
            List<InspeccionInfo> inspeccionesRechazadasList = new ArrayList<>();
            List<InspeccionInfo> inspeccionesPendientesList = new ArrayList<>();

            // Procesar cada registro
            while (rs.next()) {
                int idInspeccion = rs.getInt("ID_INSPECCION");
                Date fecha = rs.getDate("FECHA_INSPECCION");
                String estado = rs.getString("ESTADO");
                String observaciones = rs.getString("OBSERVACIONES");
                int idInspector = rs.getInt("ID_INSPECTOR");
                int idCultivo = rs.getInt("ID_CULTIVO");
                String nombreCultivo = rs.getString("NOMBRE_CULTIVO");

                // Manejar valores nulos
                if (estado == null) {
                    estado = "Pendiente";
                }
                if (observaciones == null) {
                    observaciones = "Sin observaciones";
                }
                if (nombreCultivo == null) {
                    nombreCultivo = "No especificado";
                }
                
                // Formatear fecha
                String fechaStr = "N/A";
                if (fecha != null) {
                    fechaStr = fecha.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }

                // Crear objeto de inspección
                InspeccionInfo inspeccion = new InspeccionInfo(
                    idInspeccion, fechaStr, estado, observaciones, 
                    idInspector, idCultivo, nombreCultivo
                );
                
                todasInspecciones.add(inspeccion);

                // Formatear la línea del informe
                String observacionesCorta = observaciones.length() > 25 ? observaciones.substring(0, 22) + "..." : observaciones;
                String cultivoCorto = nombreCultivo.length() > 15 ? nombreCultivo.substring(0, 12) + "..." : nombreCultivo;
                
                informe.append(String.format("%d\t%-10s\t%-12s\t%d\t\t%d\t\t%-15s\t%s\n", 
                    idInspeccion,
                    fechaStr,
                    (estado.length() > 12 ? estado.substring(0, 9) + "..." : estado),
                    idInspector,
                    idCultivo,
                    cultivoCorto,
                    observacionesCorta));

                // Acumular para el resumen estadístico
                totalInspecciones++;
                
                // Estadísticas por estado
                if (estado != null) {
                    switch (estado.toUpperCase()) {
                        case "APROBADO":
                        case "APROBADA":
                            inspeccionesAprobadas++;
                            break;
                        case "RECHAZADO":
                        case "RECHAZADA":
                            inspeccionesRechazadas++;
                            inspeccionesRechazadasList.add(inspeccion);
                            break;
                        case "PENDIENTE":
                            inspeccionesPendientes++;
                            inspeccionesPendientesList.add(inspeccion);
                            break;
                        default:
                            // Para otros estados no clasificados
                            break;
                    }
                }
            }

            // Verificar si se encontraron datos
            if (totalInspecciones == 0) {
                informe.append("\n⚠️ No se encontraron datos de inspecciones en la base de datos\n");
            } else {
                // Calcular porcentajes generales
                double porcentajeAprobadas = totalInspecciones > 0 ? (inspeccionesAprobadas * 100.0) / totalInspecciones : 0;
                double porcentajeRechazadas = totalInspecciones > 0 ? (inspeccionesRechazadas * 100.0) / totalInspecciones : 0;
                double porcentajePendientes = totalInspecciones > 0 ? (inspeccionesPendientes * 100.0) / totalInspecciones : 0;

                // ========== RESUMEN ESTADÍSTICO ==========
                informe.append("\n========================= RESUMEN ESTADÍSTICO =========================\n");
                informe.append("Total de inspecciones realizadas: ").append(totalInspecciones).append("\n");
                informe.append("✅ Inspecciones aprobadas: ").append(inspeccionesAprobadas)
                       .append(" (").append(String.format("%.1f", porcentajeAprobadas)).append("%)\n");
                informe.append("❌ Inspecciones rechazadas: ").append(inspeccionesRechazadas)
                       .append(" (").append(String.format("%.1f", porcentajeRechazadas)).append("%)\n");
                informe.append("⏳ Inspecciones pendientes: ").append(inspeccionesPendientes)
                       .append(" (").append(String.format("%.1f", porcentajePendientes)).append("%)\n");

                // ========== DETALLE DE INSPECCIONES RECHAZADAS ==========
                if (!inspeccionesRechazadasList.isEmpty()) {
                    informe.append("\n========================= INSPECCIONES RECHAZADAS =========================\n");
                    for (InspeccionInfo inspeccion : inspeccionesRechazadasList) {
                        informe.append("🔴 ID Inspección: ").append(inspeccion.idInspeccion).append("\n");
                        informe.append("   Cultivo: ").append(inspeccion.nombreCultivo)
                               .append(" (ID: ").append(inspeccion.idCultivo).append(")\n");
                        informe.append("   Inspector ID: ").append(inspeccion.idInspector).append("\n");
                        informe.append("   Fecha: ").append(inspeccion.fecha).append("\n");
                        informe.append("   Observaciones: ").append(inspeccion.observaciones).append("\n\n");
                    }
                }

                // ========== DETALLE DE INSPECCIONES PENDIENTES ==========
                if (!inspeccionesPendientesList.isEmpty()) {
                    informe.append("\n========================= INSPECCIONES PENDIENTES =========================\n");
                    for (InspeccionInfo inspeccion : inspeccionesPendientesList) {
                        informe.append("⏳ ID Inspección: ").append(inspeccion.idInspeccion).append("\n");
                        informe.append("   Cultivo: ").append(inspeccion.nombreCultivo)
                               .append(" (ID: ").append(inspeccion.idCultivo).append(")\n");
                        informe.append("   Inspector ID: ").append(inspeccion.idInspector).append("\n");
                        informe.append("   Fecha: ").append(inspeccion.fecha).append("\n\n");
                    }
                }

                // ========== ESTADÍSTICAS POR INSPECTOR ==========
                informe.append("\n========================= ESTADÍSTICAS POR INSPECTOR =========================\n");
                // Agrupar inspecciones por inspector
                java.util.Map<Integer, Integer> inspeccionesPorInspector = new java.util.HashMap<>();
                java.util.Map<Integer, Integer> aprobadasPorInspector = new java.util.HashMap<>();
                
                for (InspeccionInfo inspeccion : todasInspecciones) {
                    int inspectorId = inspeccion.idInspector;
                    inspeccionesPorInspector.put(inspectorId, inspeccionesPorInspector.getOrDefault(inspectorId, 0) + 1);
                    
                    if (inspeccion.estado.toUpperCase().contains("APROB")) {
                        aprobadasPorInspector.put(inspectorId, aprobadasPorInspector.getOrDefault(inspectorId, 0) + 1);
                    }
                }
                
                for (java.util.Map.Entry<Integer, Integer> entry : inspeccionesPorInspector.entrySet()) {
                    int inspectorId = entry.getKey();
                    int total = entry.getValue();
                    int aprobadas = aprobadasPorInspector.getOrDefault(inspectorId, 0);
                    double porcentaje = total > 0 ? (aprobadas * 100.0) / total : 0;
                    
                    informe.append("Inspector ID ").append(inspectorId).append(": ")
                           .append(total).append(" inspecciones, ")
                           .append(aprobadas).append(" aprobadas (")
                           .append(String.format("%.1f", porcentaje)).append("%)\n");
                }

                // ========== RECOMENDACIONES ==========
                informe.append("\n========================= RECOMENDACIONES =========================\n");
                
                if (inspeccionesRechazadas > 0) {
                    informe.append("🔴 PRIORIDAD ALTA: ").append(inspeccionesRechazadas)
                           .append(" inspección(es) rechazada(s) requieren atención inmediata\n");
                }
                
                if (inspeccionesPendientes > 0) {
                    informe.append("🟡 PRIORIDAD MEDIA: ").append(inspeccionesPendientes)
                           .append(" inspección(es) pendientes necesitan ser completadas\n");
                }
                
                if (porcentajeAprobadas >= 80) {
                    informe.append("✅ EXCELENTE: Alto porcentaje de inspecciones aprobadas (").append(String.format("%.1f", porcentajeAprobadas)).append("%)\n");
                } else if (porcentajeAprobadas >= 60) {
                    informe.append("📊 SATISFACTORIO: Nivel aceptable de inspecciones aprobadas (").append(String.format("%.1f", porcentajeAprobadas)).append("%)\n");
                } else {
                    informe.append("⚠️ MEJORABLE: Bajo porcentaje de inspecciones aprobadas (").append(String.format("%.1f", porcentajeAprobadas)).append("%)\n");
                    informe.append("   Se recomienda revisar los protocolos de inspección\n");
                }
                
                informe.append("===================================================================\n");
            }

        } catch (SQLException e) {
            informe.append("❌ ERROR: No se pudo generar el informe de inspecciones\n");
            informe.append("Detalles: ").append(e.getMessage()).append("\n");
            informe.append("Posible causa: La tabla 'inspeccion' o 'cultivo' no existe o no tienes permisos\n");
            e.printStackTrace();
        } finally {
            // Cerrar recursos en el bloque finally
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) ConexionBD.cerrarConexion(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        areaInforme.setText(informe.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InformeInspeccion().setVisible(true);
        });
    }
}