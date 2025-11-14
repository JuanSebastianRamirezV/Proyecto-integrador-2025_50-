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

public class InformePlagas extends JFrame {
    private JTextArea areaInforme;
    private JButton btnGenerar;

    public InformePlagas() {
        setTitle("Informe de Plagas - Catálogo Completo");
        setSize(800, 600);
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
        btnGenerar = new JButton("Generar Informe de Plagas");
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

    // Clase interna para almacenar información de plagas
    private class PlagaInfo {
        int id;
        String nombre;
        String tipo;
        String cultivosAsociados;
        
        public PlagaInfo(int id, String nombre, String tipo, String cultivosAsociados) {
            this.id = id;
            this.nombre = nombre;
            this.tipo = tipo;
            this.cultivosAsociados = cultivosAsociados;
        }
    }

    // Método que genera el informe de plagas desde la base de datos
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
            
            // Consulta para obtener todos los datos de la tabla plaga
            String sql = "SELECT ID_PLAGA, NOMBRE_PLAGA, TIPO_PLAGA, CULTIVOS_ASOCIADOS FROM plaga ORDER BY ID_PLAGA";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            informe.append("=================== INFORME DE PLAGAS - CATÁLOGO COMPLETO ===================\n");
            informe.append("Fecha del informe: ").append(LocalDate.now()).append("\n\n");

            informe.append("ID\tNombre de Plaga\t\t\tTipo\t\tCultivos Asociados\n");
            informe.append("--------------------------------------------------------------------------------\n");
            
            // Variables para el resumen
            int totalPlagas = 0;
            int plagasInsectos = 0;
            int plagasHongos = 0;
            int plagasBacterias = 0;
            int plagasVirus = 0;
            int plagasOtros = 0;
            
            // Listas para almacenar plagas por tipo
            List<PlagaInfo> todasPlagas = new ArrayList<>();
            List<PlagaInfo> plagasInsectosList = new ArrayList<>();
            List<PlagaInfo> plagasHongosList = new ArrayList<>();
            List<PlagaInfo> plagasBacteriasList = new ArrayList<>();
            List<PlagaInfo> plagasVirusList = new ArrayList<>();
            List<PlagaInfo> plagasOtrosList = new ArrayList<>();

            // Procesar cada registro
            while (rs.next()) {
                int id = rs.getInt("ID_PLAGA");
                String nombre = rs.getString("NOMBRE_PLAGA");
                String tipo = rs.getString("TIPO_PLAGA");
                String cultivosAsociados = rs.getString("CULTIVOS_ASOCIADOS");

                // Manejar valores nulos
                if (tipo == null) {
                    tipo = "No especificado";
                }
                if (cultivosAsociados == null) {
                    cultivosAsociados = "No especificados";
                }

                // Crear objeto de plaga
                PlagaInfo plaga = new PlagaInfo(id, nombre, tipo, cultivosAsociados);
                todasPlagas.add(plaga);

                // Formatear la línea del informe
                String nombreCorto = nombre.length() > 20 ? nombre.substring(0, 17) + "..." : nombre;
                String tipoCorto = tipo.length() > 15 ? tipo.substring(0, 12) + "..." : tipo;
                String cultivosCorto = cultivosAsociados.length() > 30 ? cultivosAsociados.substring(0, 27) + "..." : cultivosAsociados;
                
                informe.append(String.format("%d\t%-20s\t%-15s\t%s\n", 
                    id,
                    nombreCorto,
                    tipoCorto,
                    cultivosCorto));

                // Acumular para el resumen estadístico
                totalPlagas++;
                
                // Estadísticas por tipo de plaga
                if (tipo != null) {
                    String tipoUpper = tipo.toUpperCase();
                    if (tipoUpper.contains("INSECTO") || tipoUpper.contains("INSECTOS")) {
                        plagasInsectos++;
                        plagasInsectosList.add(plaga);
                    } else if (tipoUpper.contains("HONGO") || tipoUpper.contains("HONGOS") || tipoUpper.contains("FUNGO")) {
                        plagasHongos++;
                        plagasHongosList.add(plaga);
                    } else if (tipoUpper.contains("BACTERIA") || tipoUpper.contains("BACTERIAS")) {
                        plagasBacterias++;
                        plagasBacteriasList.add(plaga);
                    } else if (tipoUpper.contains("VIRUS") || tipoUpper.contains("VIRAL")) {
                        plagasVirus++;
                        plagasVirusList.add(plaga);
                    } else {
                        plagasOtros++;
                        plagasOtrosList.add(plaga);
                    }
                } else {
                    plagasOtros++;
                    plagasOtrosList.add(plaga);
                }
            }

            // Verificar si se encontraron datos
            if (totalPlagas == 0) {
                informe.append("\n⚠️ No se encontraron datos de plagas en la base de datos\n");
            } else {
                // Calcular porcentajes generales
                double porcentajeInsectos = totalPlagas > 0 ? (plagasInsectos * 100.0) / totalPlagas : 0;
                double porcentajeHongos = totalPlagas > 0 ? (plagasHongos * 100.0) / totalPlagas : 0;
                double porcentajeBacterias = totalPlagas > 0 ? (plagasBacterias * 100.0) / totalPlagas : 0;
                double porcentajeVirus = totalPlagas > 0 ? (plagasVirus * 100.0) / totalPlagas : 0;
                double porcentajeOtros = totalPlagas > 0 ? (plagasOtros * 100.0) / totalPlagas : 0;

                // ========== RESUMEN ESTADÍSTICO ==========
                informe.append("\n=================== RESUMEN ESTADÍSTICO ===================\n");
                informe.append("Total de plagas registradas: ").append(totalPlagas).append("\n");
                informe.append("🐛 Plagas de insectos: ").append(plagasInsectos)
                       .append(" (").append(String.format("%.1f", porcentajeInsectos)).append("%)\n");
                informe.append("🍄 Plagas de hongos: ").append(plagasHongos)
                       .append(" (").append(String.format("%.1f", porcentajeHongos)).append("%)\n");
                informe.append("🦠 Plagas bacterianas: ").append(plagasBacterias)
                       .append(" (").append(String.format("%.1f", porcentajeBacterias)).append("%)\n");
                informe.append("🦠 Plagas virales: ").append(plagasVirus)
                       .append(" (").append(String.format("%.1f", porcentajeVirus)).append("%)\n");
                informe.append("📋 Otros tipos: ").append(plagasOtros)
                       .append(" (").append(String.format("%.1f", porcentajeOtros)).append("%)\n");

                // ========== CATÁLOGO POR TIPO DE PLAGA ==========
                informe.append("\n=================== CATÁLOGO POR TIPO DE PLAGA ===================\n");
                
                // Plagas de INSECTOS
                if (!plagasInsectosList.isEmpty()) {
                    informe.append("\n🐛 PLAGAS DE INSECTOS (").append(plagasInsectos).append("):\n");
                    for (PlagaInfo plaga : plagasInsectosList) {
                        informe.append("   • ").append(plaga.nombre)
                               .append(" (ID: ").append(plaga.id).append(")\n");
                        informe.append("     Cultivos afectados: ").append(plaga.cultivosAsociados).append("\n\n");
                    }
                }
                
                // Plagas de HONGOS
                if (!plagasHongosList.isEmpty()) {
                    informe.append("\n🍄 PLAGAS DE HONGOS (").append(plagasHongos).append("):\n");
                    for (PlagaInfo plaga : plagasHongosList) {
                        informe.append("   • ").append(plaga.nombre)
                               .append(" (ID: ").append(plaga.id).append(")\n");
                        informe.append("     Cultivos afectados: ").append(plaga.cultivosAsociados).append("\n\n");
                    }
                }
                
                // Plagas BACTERIANAS
                if (!plagasBacteriasList.isEmpty()) {
                    informe.append("\n🦠 PLAGAS BACTERIANAS (").append(plagasBacterias).append("):\n");
                    for (PlagaInfo plaga : plagasBacteriasList) {
                        informe.append("   • ").append(plaga.nombre)
                               .append(" (ID: ").append(plaga.id).append(")\n");
                        informe.append("     Cultivos afectados: ").append(plaga.cultivosAsociados).append("\n\n");
                    }
                }
                
                // Plagas VIRALES
                if (!plagasVirusList.isEmpty()) {
                    informe.append("\n🦠 PLAGAS VIRALES (").append(plagasVirus).append("):\n");
                    for (PlagaInfo plaga : plagasVirusList) {
                        informe.append("   • ").append(plaga.nombre)
                               .append(" (ID: ").append(plaga.id).append(")\n");
                        informe.append("     Cultivos afectados: ").append(plaga.cultivosAsociados).append("\n\n");
                    }
                }
                
                // Otros tipos de plagas
                if (!plagasOtrosList.isEmpty()) {
                    informe.append("\n📋 OTROS TIPOS DE PLAGAS (").append(plagasOtros).append("):\n");
                    for (PlagaInfo plaga : plagasOtrosList) {
                        informe.append("   • ").append(plaga.nombre)
                               .append(" (ID: ").append(plaga.id).append(") - Tipo: ").append(plaga.tipo).append("\n");
                        informe.append("     Cultivos afectados: ").append(plaga.cultivosAsociados).append("\n\n");
                    }
                }

                // ========== ANÁLISIS DE CULTIVOS AFECTADOS ==========
                informe.append("\n=================== ANÁLISIS DE CULTIVOS AFECTADOS ===================\n");
                
                // Contar frecuencia de cultivos mencionados
                java.util.Map<String, Integer> frecuenciaCultivos = new java.util.HashMap<>();
                for (PlagaInfo plaga : todasPlagas) {
                    if (plaga.cultivosAsociados != null && !plaga.cultivosAsociados.equals("No especificados")) {
                        // Dividir por comas u otros separadores comunes
                        String[] cultivos = plaga.cultivosAsociados.split("[,;]");
                        for (String cultivo : cultivos) {
                            String cultivoTrim = cultivo.trim();
                            if (!cultivoTrim.isEmpty()) {
                                frecuenciaCultivos.put(cultivoTrim, frecuenciaCultivos.getOrDefault(cultivoTrim, 0) + 1);
                            }
                        }
                    }
                }
                
                if (!frecuenciaCultivos.isEmpty()) {
                    informe.append("Cultivos más frecuentemente afectados:\n");
                    frecuenciaCultivos.entrySet().stream()
                        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                        .limit(10)
                        .forEach(entry -> {
                            informe.append("   • ").append(entry.getKey())
                                   .append(": ").append(entry.getValue())
                                   .append(" plaga(s) asociada(s)\n");
                        });
                } else {
                    informe.append("No se encontraron cultivos específicos asociados a las plagas\n");
                }

                // ========== RECOMENDACIONES ==========
                informe.append("\n=================== RECOMENDACIONES ===================\n");
                
                if (plagasInsectos > 0) {
                    informe.append("🐛 CONTROL DE INSECTOS: ").append(plagasInsectos)
                           .append(" plagas de insectos registradas\n");
                    informe.append("   Recomendación: Implementar rotación de insecticidas y control biológico\n");
                }
                
                if (plagasHongos > 0) {
                    informe.append("🍄 CONTROL DE HONGOS: ").append(plagasHongos)
                           .append(" plagas de hongos registradas\n");
                    informe.append("   Recomendación: Mejorar drenaje y aplicar fungicidas preventivos\n");
                }
                
                if (plagasBacterias + plagasVirus > 0) {
                    informe.append("🦠 CONTROL MICROBIOLÓGICO: ").append(plagasBacterias + plagasVirus)
                           .append(" plagas microbianas registradas\n");
                    informe.append("   Recomendación: Usar material vegetal certificado y control sanitario\n");
                }
                
                if (totalPlagas > 20) {
                    informe.append("📊 OBSERVACIÓN: Amplio catálogo de plagas registrado (").append(totalPlagas).append(")\n");
                    informe.append("   Se recomienda mantener actualizado el plan de manejo integrado\n");
                } else if (totalPlagas > 10) {
                    informe.append("📊 OBSERVACIÓN: Catálogo moderado de plagas (").append(totalPlagas).append(")\n");
                    informe.append("   Continuar con el monitoreo y registro sistemático\n");
                } else {
                    informe.append("📊 OBSERVACIÓN: Catálogo básico de plagas (").append(totalPlagas).append(")\n");
                    informe.append("   Considerar expandir el registro de plagas locales\n");
                }
                
                informe.append("=========================================================\n");
            }

        } catch (SQLException e) {
            informe.append("❌ ERROR: No se pudo generar el informe de plagas\n");
            informe.append("Detalles: ").append(e.getMessage()).append("\n");
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
            new InformePlagas().setVisible(true);
        });
    }
}