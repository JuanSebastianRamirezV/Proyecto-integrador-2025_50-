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

public class InformeMunicipios extends JFrame {
    private JTextArea areaInforme;
    private JButton btnGenerar;

    public InformeMunicipios() {
        setTitle("Informe de Municipios - Catálogo Completo");
        setSize(700, 500);
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
        btnGenerar = new JButton("Generar Informe de Municipios");
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

    // Clase interna para almacenar información de municipios
    private class MunicipioInfo {
        int id;
        String nombre;
        
        public MunicipioInfo(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
    }

    // Método que genera el informe de municipios desde la base de datos
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
            
            // Consulta para obtener todos los datos de la tabla municipio
            String sql = "SELECT ID_MUNICIPIO, NOMBRE FROM municipio ORDER BY ID_MUNICIPIO";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            informe.append("=================== INFORME DE MUNICIPIOS - CATÁLOGO COMPLETO ===================\n");
            informe.append("Fecha del informe: ").append(LocalDate.now()).append("\n\n");

            informe.append("ID\tNombre del Municipio\n");
            informe.append("----------------------------------------\n");
            
            // Variables para el resumen
            int totalMunicipios = 0;
            List<MunicipioInfo> todosMunicipios = new ArrayList<>();
            List<MunicipioInfo> municipiosConA = new ArrayList<>();
            List<MunicipioInfo> municipiosConB = new ArrayList<>();
            List<MunicipioInfo> municipiosConC = new ArrayList<>();
            List<MunicipioInfo> municipiosConD = new ArrayList<>();

            // Procesar cada registro
            while (rs.next()) {
                int id = rs.getInt("ID_MUNICIPIO");
                String nombre = rs.getString("NOMBRE");

                // Manejar valores nulos
                if (nombre == null) {
                    nombre = "Nombre no disponible";
                }

                // Crear objeto de municipio
                MunicipioInfo municipio = new MunicipioInfo(id, nombre);
                todosMunicipios.add(municipio);

                // Formatear la línea del informe
                informe.append(String.format("%d\t%s\n", id, nombre));

                // Acumular para el resumen estadístico
                totalMunicipios++;
                
                // Agrupar por letra inicial para análisis
                if (nombre != null && !nombre.isEmpty()) {
                    char primeraLetra = Character.toUpperCase(nombre.charAt(0));
                    if (primeraLetra >= 'A' && primeraLetra <= 'D') {
                        municipiosConA.add(municipio);
                    } else if (primeraLetra >= 'E' && primeraLetra <= 'H') {
                        municipiosConB.add(municipio);
                    } else if (primeraLetra >= 'I' && primeraLetra <= 'Ñ') {
                        municipiosConC.add(municipio);
                    } else {
                        municipiosConD.add(municipio);
                    }
                }
            }

            // Verificar si se encontraron datos
            if (totalMunicipios == 0) {
                informe.append("\n⚠️ No se encontraron datos de municipios en la base de datos\n");
            } else {
                // Calcular porcentajes por grupo de letras
                int totalGrupoA = municipiosConA.size();
                int totalGrupoB = municipiosConB.size();
                int totalGrupoC = municipiosConC.size();
                int totalGrupoD = municipiosConD.size();
                
                double porcentajeA = totalMunicipios > 0 ? (totalGrupoA * 100.0) / totalMunicipios : 0;
                double porcentajeB = totalMunicipios > 0 ? (totalGrupoB * 100.0) / totalMunicipios : 0;
                double porcentajeC = totalMunicipios > 0 ? (totalGrupoC * 100.0) / totalMunicipios : 0;
                double porcentajeD = totalMunicipios > 0 ? (totalGrupoD * 100.0) / totalMunicipios : 0;

                // ========== RESUMEN ESTADÍSTICO ==========
                informe.append("\n=================== RESUMEN ESTADÍSTICO ===================\n");
                informe.append("Total de municipios registrados: ").append(totalMunicipios).append("\n\n");
                
                informe.append("📊 DISTRIBUCIÓN POR LETRA INICIAL:\n");
                informe.append("   🔤 Letras A-D: ").append(totalGrupoA)
                       .append(" municipios (").append(String.format("%.1f", porcentajeA)).append("%)\n");
                informe.append("   🔤 Letras E-H: ").append(totalGrupoB)
                       .append(" municipios (").append(String.format("%.1f", porcentajeB)).append("%)\n");
                informe.append("   🔤 Letras I-Ñ: ").append(totalGrupoC)
                       .append(" municipios (").append(String.format("%.1f", porcentajeC)).append("%)\n");
                informe.append("   🔤 Letras O-Z: ").append(totalGrupoD)
                       .append(" municipios (").append(String.format("%.1f", porcentajeD)).append("%)\n");

                // ========== LISTADO POR GRUPOS DE LETRAS ==========
                informe.append("\n=================== LISTADO POR GRUPOS DE LETRAS ===================\n");
                
                // Municipios con letras A-D
                if (!municipiosConA.isEmpty()) {
                    informe.append("\n🔤 MUNICIPIOS CON LETRAS A-D (").append(totalGrupoA).append("):\n");
                    for (MunicipioInfo municipio : municipiosConA) {
                        informe.append("   • ").append(municipio.nombre)
                               .append(" (ID: ").append(municipio.id).append(")\n");
                    }
                }
                
                // Municipios con letras E-H
                if (!municipiosConB.isEmpty()) {
                    informe.append("\n🔤 MUNICIPIOS CON LETRAS E-H (").append(totalGrupoB).append("):\n");
                    for (MunicipioInfo municipio : municipiosConB) {
                        informe.append("   • ").append(municipio.nombre)
                               .append(" (ID: ").append(municipio.id).append(")\n");
                    }
                }
                
                // Municipios con letras I-Ñ
                if (!municipiosConC.isEmpty()) {
                    informe.append("\n🔤 MUNICIPIOS CON LETRAS I-Ñ (").append(totalGrupoC).append("):\n");
                    for (MunicipioInfo municipio : municipiosConC) {
                        informe.append("   • ").append(municipio.nombre)
                               .append(" (ID: ").append(municipio.id).append(")\n");
                    }
                }
                
                // Municipios con letras O-Z
                if (!municipiosConD.isEmpty()) {
                    informe.append("\n🔤 MUNICIPIOS CON LETRAS O-Z (").append(totalGrupoD).append("):\n");
                    for (MunicipioInfo municipio : municipiosConD) {
                        informe.append("   • ").append(municipio.nombre)
                               .append(" (ID: ").append(municipio.id).append(")\n");
                    }
                }

                // ========== MUNICIPIOS DESTACADOS ==========
                informe.append("\n=================== MUNICIPIOS DESTACADOS ===================\n");
                
                // Encontrar el municipio con el nombre más largo
                MunicipioInfo municipioMasLargo = null;
                for (MunicipioInfo municipio : todosMunicipios) {
                    if (municipioMasLargo == null || municipio.nombre.length() > municipioMasLargo.nombre.length()) {
                        municipioMasLargo = municipio;
                    }
                }
                
                // Encontrar el municipio con el nombre más corto
                MunicipioInfo municipioMasCorto = null;
                for (MunicipioInfo municipio : todosMunicipios) {
                    if (municipio.nombre != null && !municipio.nombre.equals("Nombre no disponible")) {
                        if (municipioMasCorto == null || municipio.nombre.length() < municipioMasCorto.nombre.length()) {
                            municipioMasCorto = municipio;
                        }
                    }
                }
                
                if (municipioMasLargo != null) {
                    informe.append("📏 Municipio con nombre más largo:\n");
                    informe.append("   • ").append(municipioMasLargo.nombre)
                           .append(" (ID: ").append(municipioMasLargo.id)
                           .append(") - ").append(municipioMasLargo.nombre.length()).append(" caracteres\n");
                }
                
                if (municipioMasCorto != null) {
                    informe.append("📏 Municipio con nombre más corto:\n");
                    informe.append("   • ").append(municipioMasCorto.nombre)
                           .append(" (ID: ").append(municipioMasCorto.id)
                           .append(") - ").append(municipioMasCorto.nombre.length()).append(" caracteres\n");
                }

                // ========== RECOMENDACIONES ==========
                informe.append("\n=================== RECOMENDACIONES ===================\n");
                
                if (totalMunicipios >= 100) {
                    informe.append("🏙️  AMPLIA COBERTURA: Catálogo extenso de municipios (").append(totalMunicipios).append(")\n");
                    informe.append("   Se recomienda implementar filtros por región o departamento\n");
                } else if (totalMunicipios >= 50) {
                    informe.append("🏙️  COBERTURA MEDIA: Catálogo moderado de municipios (").append(totalMunicipios).append(")\n");
                    informe.append("   Base adecuada para la mayoría de aplicaciones regionales\n");
                } else if (totalMunicipios >= 20) {
                    informe.append("🏙️  COBERTURA BÁSICA: Catálogo inicial de municipios (").append(totalMunicipios).append(")\n");
                    informe.append("   Considerar expandir el registro según necesidades\n");
                } else {
                    informe.append("🏙️  COBERTURA LIMITADA: Catálogo reducido de municipios (").append(totalMunicipios).append(")\n");
                    informe.append("   Se recomienda completar el registro de municipios de interés\n");
                }
                
                // Verificar distribución balanceada
                if (porcentajeA > 40 || porcentajeB > 40 || porcentajeC > 40 || porcentajeD > 40) {
                    informe.append("📊 DISTRIBUCIÓN DESBALANCEADA: Un grupo de letras concentra más del 40%\n");
                    informe.append("   Esto puede indicar concentración geográfica específica\n");
                } else {
                    informe.append("📊 DISTRIBUCIÓN BALANCEADA: Los municipios están bien distribuidos alfabéticamente\n");
                }
                
                informe.append("=========================================================\n");
            }

        } catch (SQLException e) {
            informe.append("❌ ERROR: No se pudo generar el informe de municipios\n");
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
            new InformeMunicipios().setVisible(true);
        });
    }
}
