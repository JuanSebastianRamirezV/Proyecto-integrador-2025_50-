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
import java.util.ArrayList;
import java.util.List;
import database.ConexionBD;

public class InformeCultivos extends JFrame {
    private JTextArea areaInforme;
    private JButton btnGenerar;

    public InformeCultivos() {
        setTitle("Informe de Cultivos - Análisis Completo");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Área de texto donde se muestra el informe
        areaInforme = new JTextArea();
        areaInforme.setEditable(false);
        areaInforme.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaInforme);
        panel.add(scroll, BorderLayout.CENTER);

        // Botón para generar informe
        btnGenerar = new JButton("Generar Informe Completo");
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

    // Clase interna para almacenar información de cultivos con riesgo
    private class CultivoRiesgo {
        String nombre;
        int plantasAfectadas;
        int totalPlantas;
        double porcentaje;
        String nivelRiesgo;
        
        public CultivoRiesgo(String nombre, int plantasAfectadas, int totalPlantas, double porcentaje, String nivelRiesgo) {
            this.nombre = nombre;
            this.plantasAfectadas = plantasAfectadas;
            this.totalPlantas = totalPlantas;
            this.porcentaje = porcentaje;
            this.nivelRiesgo = nivelRiesgo;
        }
    }

    // Método que calcula el nivel de riesgo para cada cultivo
    private String calcularRiesgo(int plantasAfectadas, int totalPlantas) {
        if (totalPlantas == 0) return "SIN DATOS";
        
        double porcentajeAfectacion = (plantasAfectadas * 100.0) / totalPlantas;
        
        if (porcentajeAfectacion > 30) {
            return "ALTO RIESGO 🔴";
        } else if (porcentajeAfectacion > 15) {
            return "RIESGO MODERADO 🟡";
        } else if (porcentajeAfectacion > 5) {
            return "BAJO RIESGO 🟢";
        } else {
            return "SIN RIESGO ✅";
        }
    }

    // Método que genera el informe de cultivos desde la base de datos
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
            
            // Consulta para obtener todos los datos de la tabla cultivo
            String sql = "SELECT ID_CULTIVO, NOMBRE_CULTIVO, PLANTAS_AFECTADAS, ESTADO_PLANTA, ID_PREDIO, TOTAL_PLANTAS FROM cultivo ORDER BY ID_CULTIVO";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            informe.append("========== INFORME DE CULTIVOS - ANÁLISIS COMPLETO ==========\n");
            informe.append("Fecha: ").append(LocalDate.now()).append("\n\n");

            informe.append("ID\tCultivo\t\t\tAfect/Tot\t%\tEstado\t\tRiesgo\n");
            informe.append("-----------------------------------------------------------------------\n");
            
            // Variables para el resumen
            int totalCultivos = 0;
            int totalPlantasAfectadas = 0;
            int totalPlantas = 0;
            int cultivosSaludables = 0;
            int cultivosEnfermos = 0;
            int cultivosOtros = 0;
            int cultivosAltoRiesgo = 0;
            int cultivosModeradoRiesgo = 0;
            int cultivosBajoRiesgo = 0;
            int cultivosSinRiesgo = 0;
            
            // Listas para almacenar cultivos por nivel de riesgo
            List<CultivoRiesgo> cultivosAltoRiesgoList = new ArrayList<>();
            List<CultivoRiesgo> cultivosModeradoRiesgoList = new ArrayList<>();
            List<CultivoRiesgo> cultivosBajoRiesgoList = new ArrayList<>();
            List<CultivoRiesgo> cultivosSinRiesgoList = new ArrayList<>();

            // Procesar cada registro
            while (rs.next()) {
                int id = rs.getInt("ID_CULTIVO");
                String nombre = rs.getString("NOMBRE_CULTIVO");
                int plantasAfectadas = rs.getInt("PLANTAS_AFECTADAS");
                String estado = rs.getString("ESTADO_PLANTA");
                int predio = rs.getInt("ID_PREDIO");
                int totalPlantasCultivo = rs.getInt("TOTAL_PLANTAS");

                // Manejar valores nulos
                if (rs.wasNull()) {
                    plantasAfectadas = 0;
                }
                
                if (estado == null) {
                    estado = "No especificado";
                }

                // Calcular porcentaje y nivel de riesgo
                double porcentaje = totalPlantasCultivo > 0 ? (plantasAfectadas * 100.0) / totalPlantasCultivo : 0;
                String nivelRiesgo = calcularRiesgo(plantasAfectadas, totalPlantasCultivo);

                // Formatear la línea del informe con análisis de riesgo
                informe.append(String.format("%d\t%-15s\t%d/%-5d\t%.1f%%\t%-12s\t%s\n", 
                    id, 
                    (nombre.length() > 15 ? nombre.substring(0, 12) + "..." : nombre),
                    plantasAfectadas,
                    totalPlantasCultivo,
                    porcentaje,
                    (estado.length() > 12 ? estado.substring(0, 9) + "..." : estado),
                    nivelRiesgo));

                // Acumular para el resumen estadístico
                totalCultivos++;
                totalPlantasAfectadas += plantasAfectadas;
                totalPlantas += totalPlantasCultivo;
                
                // Estadísticas por estado
                if (estado != null) {
                    if ("Saludable".equalsIgnoreCase(estado)) {
                        cultivosSaludables++;
                    } else if ("Enfermo".equalsIgnoreCase(estado) || "Enferma".equalsIgnoreCase(estado)) {
                        cultivosEnfermos++;
                    } else {
                        cultivosOtros++;
                    }
                } else {
                    cultivosOtros++;
                }
                
                // Estadísticas por nivel de riesgo y almacenar cultivos
                CultivoRiesgo cultivoRiesgo = new CultivoRiesgo(nombre, plantasAfectadas, totalPlantasCultivo, porcentaje, nivelRiesgo);
                
                switch (nivelRiesgo) {
                    case "ALTO RIESGO 🔴":
                        cultivosAltoRiesgo++;
                        cultivosAltoRiesgoList.add(cultivoRiesgo);
                        break;
                    case "RIESGO MODERADO 🟡":
                        cultivosModeradoRiesgo++;
                        cultivosModeradoRiesgoList.add(cultivoRiesgo);
                        break;
                    case "BAJO RIESGO 🟢":
                        cultivosBajoRiesgo++;
                        cultivosBajoRiesgoList.add(cultivoRiesgo);
                        break;
                    case "SIN RIESGO ✅":
                        cultivosSinRiesgo++;
                        cultivosSinRiesgoList.add(cultivoRiesgo);
                        break;
                }
            }

            // Verificar si se encontraron datos
            if (totalCultivos == 0) {
                informe.append("\n⚠️ No se encontraron datos de cultivos en la base de datos\n");
            } else {
                // Calcular porcentajes generales
                double porcentajeAfectacionGeneral = totalPlantas > 0 ? (totalPlantasAfectadas * 100.0) / totalPlantas : 0;
                double porcentajeSaludables = totalCultivos > 0 ? (cultivosSaludables * 100.0) / totalCultivos : 0;
                double porcentajeEnfermos = totalCultivos > 0 ? (cultivosEnfermos * 100.0) / totalCultivos : 0;
                double porcentajeOtros = totalCultivos > 0 ? (cultivosOtros * 100.0) / totalCultivos : 0;

                // ========== RESUMEN ESTADÍSTICO ==========
                informe.append("\n============== RESUMEN ESTADÍSTICO ==============\n");
                informe.append("Total de cultivos registrados: ").append(totalCultivos).append("\n");
                informe.append("Cultivos saludables: ").append(cultivosSaludables)
                       .append(" (").append(String.format("%.1f", porcentajeSaludables)).append("%)\n");
                informe.append("Cultivos enfermos: ").append(cultivosEnfermos)
                       .append(" (").append(String.format("%.1f", porcentajeEnfermos)).append("%)\n");
                if (cultivosOtros > 0) {
                    informe.append("Otros estados: ").append(cultivosOtros)
                           .append(" (").append(String.format("%.1f", porcentajeOtros)).append("%)\n");
                }
                informe.append("Total plantas afectadas: ").append(totalPlantasAfectadas).append("\n");
                informe.append("Total plantas cultivadas: ").append(totalPlantas).append("\n");
                informe.append("Porcentaje general de afectación: ").append(String.format("%.1f", porcentajeAfectacionGeneral)).append("%\n");

                // ========== ANÁLISIS DE RIESGO ==========
                informe.append("\n============== ANÁLISIS DE RIESGO ==============\n");
                informe.append("🔴 Cultivos con ALTO RIESGO: ").append(cultivosAltoRiesgo)
                       .append(" (").append(String.format("%.1f", (cultivosAltoRiesgo * 100.0) / totalCultivos)).append("%)\n");
                informe.append("🟡 Cultivos con RIESGO MODERADO: ").append(cultivosModeradoRiesgo)
                       .append(" (").append(String.format("%.1f", (cultivosModeradoRiesgo * 100.0) / totalCultivos)).append("%)\n");
                informe.append("🟢 Cultivos con BAJO RIESGO: ").append(cultivosBajoRiesgo)
                       .append(" (").append(String.format("%.1f", (cultivosBajoRiesgo * 100.0) / totalCultivos)).append("%)\n");
                informe.append("✅ Cultivos SIN RIESGO: ").append(cultivosSinRiesgo)
                       .append(" (").append(String.format("%.1f", (cultivosSinRiesgo * 100.0) / totalCultivos)).append("%)\n");
                
                // ========== RECOMENDACIONES ESPECÍFICAS ==========
                informe.append("\n============== RECOMENDACIONES ESPECÍFICAS ==============\n");
                
                // Cultivos con ALTO RIESGO
                if (!cultivosAltoRiesgoList.isEmpty()) {
                    informe.append("🔴 PRIORIDAD ALTA - Atención INMEDIATA requerida:\n");
                    for (CultivoRiesgo cultivo : cultivosAltoRiesgoList) {
                        informe.append("   • ").append(cultivo.nombre)
                               .append(" (").append(String.format("%.1f", cultivo.porcentaje))
                               .append("% de afectación)\n");
                    }
                }
                
                // Cultivos con RIESGO MODERADO
                if (!cultivosModeradoRiesgoList.isEmpty()) {
                    informe.append("\n🟡 PRIORIDAD MEDIA - Monitoreo constante:\n");
                    for (CultivoRiesgo cultivo : cultivosModeradoRiesgoList) {
                        informe.append("   • ").append(cultivo.nombre)
                               .append(" (").append(String.format("%.1f", cultivo.porcentaje))
                               .append("% de afectación)\n");
                    }
                }
                
                // Cultivos con BAJO RIESGO
                if (!cultivosBajoRiesgoList.isEmpty()) {
                    informe.append("\n🟢 SITUACIÓN CONTROLADA - Mantener vigilancia:\n");
                    for (CultivoRiesgo cultivo : cultivosBajoRiesgoList) {
                        informe.append("   • ").append(cultivo.nombre)
                               .append(" (").append(String.format("%.1f", cultivo.porcentaje))
                               .append("% de afectación)\n");
                    }
                }
                
                // Cultivos SIN RIESGO
                if (!cultivosSinRiesgoList.isEmpty()) {
                    informe.append("\n✅ SITUACIÓN ÓPTIMA - Continuar prácticas actuales:\n");
                    for (CultivoRiesgo cultivo : cultivosSinRiesgoList) {
                        informe.append("   • ").append(cultivo.nombre)
                               .append(" (").append(String.format("%.1f", cultivo.porcentaje))
                               .append("% de afectación)\n");
                    }
                }
                
                // Análisis adicional basado en porcentaje general
                informe.append("\n============== ANÁLISIS GENERAL ==============\n");
                if (porcentajeAfectacionGeneral > 20) {
                    informe.append("⚠️ ALERTA GENERAL: Alto porcentaje de afectación (").append(String.format("%.1f", porcentajeAfectacionGeneral)).append("%)\n");
                    informe.append("   Se recomienda revisar urgentemente los protocolos de control\n");
                } else if (porcentajeAfectacionGeneral > 10) {
                    informe.append("📊 OBSERVACIÓN GENERAL: Nivel moderado de afectación (").append(String.format("%.1f", porcentajeAfectacionGeneral)).append("%)\n");
                    informe.append("   Mantener medidas preventivas y aumentar monitoreo\n");
                } else {
                    informe.append("✅ SITUACIÓN GENERAL: Bajo nivel de afectación (").append(String.format("%.1f", porcentajeAfectacionGeneral)).append("%)\n");
                    informe.append("   Las prácticas actuales son efectivas, continuar así\n");
                }
                
                informe.append("================================================\n");
            }

        } catch (SQLException e) {
            informe.append("❌ ERROR: No se pudo generar el informe\n");
            informe.append("Detalles: ").append(e.getMessage()).append("\n");
            informe.append("Causa: ").append(e.getSQLState()).append("\n");
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
            new InformeCultivos().setVisible(true);
        });
    }
}