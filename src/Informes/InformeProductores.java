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

public class InformeProductores extends JFrame {
    private JTextArea areaInforme;
    private JButton btnGenerar;

    public InformeProductores() {
        setTitle("Informe de Productores - Registro Completo");
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
        btnGenerar = new JButton("Generar Informe de Productores");
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

    // Clase interna para almacenar información de productores
    private class ProductorInfo {
        int id;
        String nombreCompleto;
        String tipoIdentificacion;
        String rol;
        
        public ProductorInfo(int id, String nombreCompleto, String tipoIdentificacion, String rol) {
            this.id = id;
            this.nombreCompleto = nombreCompleto;
            this.tipoIdentificacion = tipoIdentificacion;
            this.rol = rol;
        }
    }

    // Método que genera el informe de productores desde la base de datos
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
            
            // Consulta para obtener todos los datos de la tabla productores
            String sql = "SELECT ID_PRODUCTOR, NOMBRE_COMPLETO, TIPO_IDENTIFICACION, ROL FROM productores ORDER BY ID_PRODUCTOR";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            informe.append("=================== INFORME DE PRODUCTORES - REGISTRO COMPLETO ===================\n");
            informe.append("Fecha del informe: ").append(LocalDate.now()).append("\n\n");

            informe.append("ID\tNombre Completo\t\t\tTipo ID\t\tRol\n");
            informe.append("------------------------------------------------------------------------\n");
            
            // Variables para el resumen
            int totalProductores = 0;
            int productoresCC = 0;
            int productoresCE = 0;
            int productoresNIT = 0;
            int productoresTI = 0;
            int productoresOtros = 0;
            
            // Variables para roles
            int productoresAgricultor = 0;
            int productoresGanadero = 0;
            int productoresMixto = 0;
            int productoresSinRol = 0;
            
            // Listas para almacenar productores
            List<ProductorInfo> todosProductores = new ArrayList<>();
            List<ProductorInfo> productoresCCList = new ArrayList<>();
            List<ProductorInfo> productoresCEList = new ArrayList<>();
            List<ProductorInfo> productoresNITList = new ArrayList<>();
            List<ProductorInfo> productoresTIList = new ArrayList<>();
            List<ProductorInfo> productoresOtrosList = new ArrayList<>();
            List<ProductorInfo> productoresSinRolList = new ArrayList<>();

            // Procesar cada registro
            while (rs.next()) {
                int id = rs.getInt("ID_PRODUCTOR");
                String nombreCompleto = rs.getString("NOMBRE_COMPLETO");
                String tipoIdentificacion = rs.getString("TIPO_IDENTIFICACION");
                String rol = rs.getString("ROL");

                // Manejar valores nulos
                if (tipoIdentificacion == null) {
                    tipoIdentificacion = "No especificado";
                }
                if (rol == null) {
                    rol = "No asignado";
                }

                // Crear objeto de productor
                ProductorInfo productor = new ProductorInfo(id, nombreCompleto, tipoIdentificacion, rol);
                todosProductores.add(productor);

                // Formatear la línea del informe
                String nombreCorto = nombreCompleto.length() > 25 ? nombreCompleto.substring(0, 22) + "..." : nombreCompleto;
                String tipoIdCorto = tipoIdentificacion.length() > 12 ? tipoIdentificacion.substring(0, 9) + "..." : tipoIdentificacion;
                String rolCorto = rol.length() > 15 ? rol.substring(0, 12) + "..." : rol;
                
                informe.append(String.format("%d\t%-25s\t%-12s\t%s\n", 
                    id,
                    nombreCorto,
                    tipoIdCorto,
                    rolCorto));

                // Acumular para el resumen estadístico
                totalProductores++;
                
                // Estadísticas por tipo de identificación
                if (tipoIdentificacion != null) {
                    String tipoIdUpper = tipoIdentificacion.toUpperCase();
                    if (tipoIdUpper.contains("CC") || tipoIdUpper.contains("CÉDULA") || tipoIdUpper.contains("CEDULA")) {
                        productoresCC++;
                        productoresCCList.add(productor);
                    } else if (tipoIdUpper.contains("CE") || tipoIdUpper.contains("EXTRANJER")) {
                        productoresCE++;
                        productoresCEList.add(productor);
                    } else if (tipoIdUpper.contains("NIT")) {
                        productoresNIT++;
                        productoresNITList.add(productor);
                    } else if (tipoIdUpper.contains("TI") || tipoIdUpper.contains("TARJETA")) {
                        productoresTI++;
                        productoresTIList.add(productor);
                    } else {
                        productoresOtros++;
                        productoresOtrosList.add(productor);
                    }
                } else {
                    productoresOtros++;
                    productoresOtrosList.add(productor);
                }
                
                // Estadísticas por rol
                if (rol != null && !rol.equals("No asignado")) {
                    String rolUpper = rol.toUpperCase();
                    if (rolUpper.contains("AGRICULTOR") || rolUpper.contains("CULTIVO") || rolUpper.contains("AGRÍCOLA")) {
                        productoresAgricultor++;
                    } else if (rolUpper.contains("GANADERO") || rolUpper.contains("PECUARIO") || rolUpper.contains("GANADO")) {
                        productoresGanadero++;
                    } else if (rolUpper.contains("MIXTO") || rolUpper.contains("AMBOS") || rolUpper.contains("INTEGRAL")) {
                        productoresMixto++;
                    } else {
                        // Si tiene rol pero no coincide con las categorías principales
                        productoresSinRol++;
                        productoresSinRolList.add(productor);
                    }
                } else {
                    productoresSinRol++;
                    productoresSinRolList.add(productor);
                }
            }

            // Verificar si se encontraron datos
            if (totalProductores == 0) {
                informe.append("\n⚠️ No se encontraron datos de productores en la base de datos\n");
            } else {
                // Calcular porcentajes generales
                double porcentajeCC = totalProductores > 0 ? (productoresCC * 100.0) / totalProductores : 0;
                double porcentajeCE = totalProductores > 0 ? (productoresCE * 100.0) / totalProductores : 0;
                double porcentajeNIT = totalProductores > 0 ? (productoresNIT * 100.0) / totalProductores : 0;
                double porcentajeTI = totalProductores > 0 ? (productoresTI * 100.0) / totalProductores : 0;
                double porcentajeOtros = totalProductores > 0 ? (productoresOtros * 100.0) / totalProductores : 0;
                
                double porcentajeAgricultor = totalProductores > 0 ? (productoresAgricultor * 100.0) / totalProductores : 0;
                double porcentajeGanadero = totalProductores > 0 ? (productoresGanadero * 100.0) / totalProductores : 0;
                double porcentajeMixto = totalProductores > 0 ? (productoresMixto * 100.0) / totalProductores : 0;
                double porcentajeSinRol = totalProductores > 0 ? (productoresSinRol * 100.0) / totalProductores : 0;

                // ========== RESUMEN ESTADÍSTICO ==========
                informe.append("\n=================== RESUMEN ESTADÍSTICO ===================\n");
                informe.append("Total de productores registrados: ").append(totalProductores).append("\n\n");
                
                informe.append("📊 DISTRIBUCIÓN POR TIPO DE IDENTIFICACIÓN:\n");
                informe.append("   🆔 Cédula de Ciudadanía (CC): ").append(productoresCC)
                       .append(" (").append(String.format("%.1f", porcentajeCC)).append("%)\n");
                informe.append("   🆔 Cédula de Extranjería (CE): ").append(productoresCE)
                       .append(" (").append(String.format("%.1f", porcentajeCE)).append("%)\n");
                informe.append("   🆔 NIT: ").append(productoresNIT)
                       .append(" (").append(String.format("%.1f", porcentajeNIT)).append("%)\n");
                informe.append("   🆔 Tarjeta de Identidad (TI): ").append(productoresTI)
                       .append(" (").append(String.format("%.1f", porcentajeTI)).append("%)\n");
                informe.append("   🆔 Otros: ").append(productoresOtros)
                       .append(" (").append(String.format("%.1f", porcentajeOtros)).append("%)\n\n");
                
                informe.append("👨‍🌾 DISTRIBUCIÓN POR ROL:\n");
                informe.append("   🌱 Agricultores: ").append(productoresAgricultor)
                       .append(" (").append(String.format("%.1f", porcentajeAgricultor)).append("%)\n");
                informe.append("   🐄 Ganaderos: ").append(productoresGanadero)
                       .append(" (").append(String.format("%.1f", porcentajeGanadero)).append("%)\n");
                informe.append("   🔄 Mixtos: ").append(productoresMixto)
                       .append(" (").append(String.format("%.1f", porcentajeMixto)).append("%)\n");
                
                // Solo mostrar productores sin rol si existen
                if (productoresSinRol > 0) {
                    informe.append("   ❓ Otros roles o sin especificar: ").append(productoresSinRol)
                           .append(" (").append(String.format("%.1f", porcentajeSinRol)).append("%)\n");
                }

                // ========== DETALLE POR TIPO DE IDENTIFICACIÓN ==========
                informe.append("\n=================== DETALLE POR TIPO DE IDENTIFICACIÓN ===================\n");
                
                // Productores con Cédula de Ciudadanía
                if (!productoresCCList.isEmpty()) {
                    informe.append("\n🆔 PRODUCTORES CON CÉDULA DE CIUDADANÍA (").append(productoresCC).append("):\n");
                    for (ProductorInfo productor : productoresCCList) {
                        informe.append("   • ").append(productor.nombreCompleto)
                               .append(" (ID: ").append(productor.id).append(") - Rol: ").append(productor.rol).append("\n");
                    }
                }
                
                // Productores con Cédula de Extranjería
                if (!productoresCEList.isEmpty()) {
                    informe.append("\n🆔 PRODUCTORES CON CÉDULA DE EXTRANJERÍA (").append(productoresCE).append("):\n");
                    for (ProductorInfo productor : productoresCEList) {
                        informe.append("   • ").append(productor.nombreCompleto)
                               .append(" (ID: ").append(productor.id).append(") - Rol: ").append(productor.rol).append("\n");
                    }
                }
                
                // Productores con NIT
                if (!productoresNITList.isEmpty()) {
                    informe.append("\n🆔 PRODUCTORES CON NIT (").append(productoresNIT).append("):\n");
                    for (ProductorInfo productor : productoresNITList) {
                        informe.append("   • ").append(productor.nombreCompleto)
                               .append(" (ID: ").append(productor.id).append(") - Rol: ").append(productor.rol).append("\n");
                    }
                }

                // ========== RECOMENDACIONES ==========
                informe.append("\n=================== RECOMENDACIONES ===================\n");
                
                // Solo mostrar advertencia si hay productores sin rol categorizado
                if (productoresSinRol > 0) {
                    informe.append("🔴 ATENCIÓN: ").append(productoresSinRol)
                           .append(" productor(es) tienen roles no categorizados en el sistema\n");
                } else {
                    informe.append("✅ EXCELENTE: Todos los productores tienen roles categorizados correctamente\n");
                }
                
                if (productoresAgricultor > productoresGanadero) {
                    informe.append("🌱 ENFOQUE AGRÍCOLA: Mayoría de productores son agricultores (").append(productoresAgricultor).append(")\n");
                } else if (productoresGanadero > productoresAgricultor) {
                    informe.append("🐄 ENFOQUE GANADERO: Mayoría de productores son ganaderos (").append(productoresGanadero).append(")\n");
                } else {
                    informe.append("🔄 EQUILIBRIO: Distribución balanceada entre agricultores y ganaderos\n");
                }
                
                if (totalProductores > 50) {
                    informe.append("📊 GRAN BASE: Amplia base de productores registrada (").append(totalProductores).append(")\n");
                    informe.append("   Se recomienda segmentar por región o especialización\n");
                } else if (totalProductores > 20) {
                    informe.append("📊 BASE MEDIANA: Base moderada de productores (").append(totalProductores).append(")\n");
                    informe.append("   Continuar con el crecimiento controlado del registro\n");
                } else {
                    informe.append("📊 BASE PEQUEÑA: Base inicial de productores (").append(totalProductores).append(")\n");
                    informe.append("   Considerar estrategias para ampliar el registro\n");
                }
                
                informe.append("=========================================================\n");
            }

        } catch (SQLException e) {
            informe.append("❌ ERROR: No se pudo generar el informe de productores\n");
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
            new InformeProductores().setVisible(true);
        });
    }
}