package test;

import database.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestValidacionCruzada {
    public static void main(String[] args) {
        System.out.println("🎯 VALIDACIÓN CRUZADA JAVA-BD");
        
        testProductorConRestricciones();
    }
    
    private static void testProductorConRestricciones() {
        System.out.println("\n🔍 TESTEANDO PRODUCTOR CON RESTRICCIONES");
        
        try (Connection conn = ConexionBD.getConexion("PRODUCTOR")) {
            
            // 1. Operaciones que DEBERÍAN funcionar
            System.out.println("\n✅ OPERACIONES PERMITIDAS:");
            probarSelect(conn, "CULTIVO");
            probarSelect(conn, "PREDIO");
            probarInsertCultivo(conn);
            
            // 2. Operaciones que DEBERÍAN FALLAR
            System.out.println("\n❌ OPERACIONES DENEGADAS (ESPERADO):");
            probarDeleteCultivo(conn);
            probarInsertInspeccion(conn);
            probarDeleteCualquierTabla(conn);
            
        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
        }
    }
    
    private static void probarSelect(Connection conn, String tabla) {
        String sql = "SELECT COUNT(*) AS total FROM PROYECTO_INTEGRADOR2025." + tabla + " WHERE ROWNUM <= 3";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                System.out.println("   ✅ SELECT " + tabla + ": " + rs.getInt("total") + " registros");
            }
        } catch (Exception e) {
            System.out.println("   ❌ SELECT " + tabla + ": DENEGADO - " + e.getMessage());
        }
    }
    
    private static void probarInsertCultivo(Connection conn) {
        // Usar un ID muy alto para no interferir
        String sql = "INSERT INTO PROYECTO_INTEGRADOR2025.CULTIVO " +
                    "(ID_CULTIVO, NOMBRE_CULTIVO, ID_PREDIO, TOTAL_PLANTAS) " +
                    "VALUES (99999, 'CULTIVO_PRUEBA_PRODUCTOR', 1, 100)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int filas = pstmt.executeUpdate();
            System.out.println("   ✅ INSERT CULTIVO: PERMITIDO (" + filas + " filas)");
            conn.rollback(); // Deshacer para no guardar
        } catch (Exception e) {
            System.out.println("   ❌ INSERT CULTIVO: DENEGADO - " + e.getMessage());
        }
    }
    
    private static void probarInsertInspeccion(Connection conn) {
        String sql = "INSERT INTO PROYECTO_INTEGRADOR2025.INSPECCION " +
                    "(ID_INSPECCION, FECHA_INSPECCION, ESTADO, OBSERVACIONES, ID_INSPECTOR, ID_CULTIVO) " +
                    "VALUES (99999, SYSDATE, 'PRUEBA', 'PRUEBA_PRODUCTOR', 1, 1)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int filas = pstmt.executeUpdate();
            System.out.println("   ✅ INSERT INSPECCION: PERMITIDO (" + filas + " filas)");
            conn.rollback(); // Deshacer para no guardar
        } catch (Exception e) {
            System.out.println("   ❌ INSERT INSPECCION: DENEGADO - " + e.getMessage());
        }
    }
    
    private static void probarDeleteCultivo(Connection conn) {
        try {
            String sql = "DELETE FROM PROYECTO_INTEGRADOR2025.CULTIVO WHERE ID_CULTIVO = 99998";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.executeUpdate();
                System.out.println("   ❌ DELETE CULTIVO: PERMITIDO (INSEGURO!)");
            }
        } catch (Exception e) {
            System.out.println("   ✅ DELETE CULTIVO: DENEGADO (SEGURO) - " + e.getMessage());
        }
    }
    
    private static void probarDeleteCualquierTabla(Connection conn) {
        try {
            String sql = "DELETE FROM PROYECTO_INTEGRADOR2025.INSPECCION WHERE ID_INSPECCION = 99998";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.executeUpdate();
                System.out.println("   ❌ DELETE INSPECCION: PERMITIDO (INSEGURO!)");
            }
        } catch (Exception e) {
            System.out.println("   ✅ DELETE INSPECCION: DENEGADO (SEGURO) - " + e.getMessage());
        }
    }
}