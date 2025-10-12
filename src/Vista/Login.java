package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class Login extends JFrame {
    private JTextField usuarioField;
    private JPasswordField passwordField;
    private JButton ingresarBtn;
    private JButton salirBtn;
    /**
 * Constructor de la clase Login que crea la interfaz gráfica para el inicio de sesión
 * Configura una ventana con diseño moderno, campos de usuario/contraseña y botones
 */
public Login() {
    // Configuración básica de la ventana
    setTitle("INICIAR SESIÓN");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    
    /**
     * Panel principal con fondo degradado azul
     * Se sobrescribe paintComponent para crear un efecto de gradiente
     */
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            Color color1 = new Color(0, 102, 204); // Azul oscuro
            Color color2 = new Color(0, 153, 255); // Azul claro
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    };
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
    
    /**
     * Panel de contenido con fondo blanco y bordes redondeados
     * Contiene todos los elementos del formulario de login
     */
    JPanel contentPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            g2.dispose();
        }
    };
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setBackground(Color.WHITE);
    contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    contentPanel.setMaximumSize(new Dimension(350, 400));
    
    // Título de la ventana
    JLabel titulo = new JLabel("INICIAR SESIÓN");
    titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
    titulo.setForeground(new Color(0, 102, 204));
    titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
    titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
    
    // Icono de usuario decorativo
    JLabel iconoUsuario = new JLabel();
    iconoUsuario.setIcon(crearIconoUsuario());
    iconoUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
    iconoUsuario.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
    
    /**
     * Campo de texto para ingresar el usuario
     * Configurado con tamaño, fuente y borde personalizado
     */
    usuarioField = new JTextField();
    usuarioField.setMaximumSize(new Dimension(280, 40));
    usuarioField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    usuarioField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(8, 10, 8, 10)
    ));
    
    /**
     * Campo de contraseña para ingresar la clave
     * Configurado con las mismas propiedades que el campo de usuario
     */
    passwordField = new JPasswordField();
    passwordField.setMaximumSize(new Dimension(280, 40));
    passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    passwordField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(8, 10, 8, 10)
    ));
    
    // Agregar placeholders a los campos para mejor usabilidad
    agregarPlaceholder(usuarioField, "Ingrese su usuario");
    agregarPlaceholder(passwordField, "Ingrese su contraseña");
    
    // Botones de acción
    ingresarBtn = new JButton("Ingresar");
    personalizarBoton(ingresarBtn, new Color(0, 102, 204), Color.WHITE);
    
    salirBtn = new JButton("Salir");
    personalizarBoton(salirBtn, new Color(102, 102, 102), Color.WHITE);
    
    // Ensamblar componentes en el panel de contenido
    contentPanel.add(iconoUsuario);
    contentPanel.add(titulo);
    
    contentPanel.add(crearCampoConEtiqueta("Usuario:", usuarioField));
    contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    contentPanel.add(crearCampoConEtiqueta("Contraseña:", passwordField));
    
    /**
     * Panel para organizar los botones horizontalmente
     * Usa FlowLayout para centrar los botones con espacio entre ellos
     */
    JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
    botonesPanel.setBackground(Color.WHITE);
    botonesPanel.add(ingresarBtn);
    botonesPanel.add(salirBtn);
    
    contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));
    contentPanel.add(botonesPanel);
    
    // Agregar el panel de contenido al panel principal y mostrar la ventana
    panel.add(contentPanel);
    add(panel);
    pack();
    setLocationRelativeTo(null); // Centrar la ventana en la pantalla
}

/**
 * Agrega un placeholder (texto guía) a un campo de texto JTextField
 * El placeholder desaparece cuando el campo recibe foco y reaparece si está vacío
 * 
 * @param field El campo de texto al que se agregará el placeholder
 * @param placeholder El texto del placeholder a mostrar
 */
private void agregarPlaceholder(JTextField field, String placeholder) {
    // Configurar el texto de placeholder
    field.setText(placeholder);
    field.setForeground(Color.GRAY);
    field.setFont(new Font("Segoe UI", Font.ITALIC, 14));
    
    // Agregar focus listener para manejar el placeholder
    field.addFocusListener(new java.awt.event.FocusAdapter() {
        /**
         * Se ejecuta cuando el campo recibe el foco
         * Elimina el placeholder si está presente
         */
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (field.getText().equals(placeholder)) {
                field.setText("");
                field.setForeground(Color.BLACK);
                field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
        }
        
        /**
         * Se ejecuta cuando el campo pierde el foco
         * Restaura el placeholder si el campo está vacío
         */
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (field.getText().isEmpty()) {
                field.setText(placeholder);
                field.setForeground(Color.GRAY);
                field.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            }
        }
    });
}

/**
 * Agrega un placeholder a un campo de contraseña JPasswordField
 * Maneja la visualización especial requerida para campos de contraseña
 * 
 * @param field El campo de contraseña al que se agregará el placeholder
 * @param placeholder El texto del placeholder a mostrar
 */
private void agregarPlaceholder(JPasswordField field, String placeholder) {
    // Para JPasswordField necesitamos un enfoque diferente
    field.setEchoChar((char) 0); // Mostrar texto normal inicialmente
    field.setText(placeholder);
    field.setForeground(Color.GRAY);
    field.setFont(new Font("Segoe UI", Font.ITALIC, 14));
    
    field.addFocusListener(new java.awt.event.FocusAdapter() {
        /**
         * Se ejecuta cuando el campo de contraseña recibe el foco
         * Cambia a modo contraseña (con puntos) y elimina el placeholder
         */
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (new String(field.getPassword()).equals(placeholder)) {
                field.setText("");
                field.setEchoChar('•'); // Cambiar a caracteres de contraseña
                field.setForeground(Color.BLACK);
                field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
        }
        
        /**
         * Se ejecuta cuando el campo de contraseña pierde el foco
         * Restaura el placeholder si no se ingresó contraseña
         */
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (field.getPassword().length == 0) {
                field.setEchoChar((char) 0);
                field.setText(placeholder);
                field.setForeground(Color.GRAY);
                field.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            }
        }
    });
}

/**
 * Personaliza la apariencia de un botón con colores y efectos hover
 * 
 * @param boton El botón a personalizar
 * @param colorFondo Color de fondo del botón
 * @param colorTexto Color del texto del botón
 */
private void personalizarBoton(JButton boton, Color colorFondo, Color colorTexto) {
    boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    boton.setBackground(colorFondo);
    boton.setForeground(colorTexto);
    boton.setFocusPainted(false);
    boton.setBorderPainted(false);
    boton.setPreferredSize(new Dimension(120, 40));
    
    // Efecto hover que oscurece el botón cuando el mouse pasa por encima
    boton.addMouseListener(new java.awt.event.MouseAdapter() {
        /**
         * Se ejecuta cuando el mouse entra en el área del botón
         * Oscurece el color de fondo para efecto visual
         */
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            boton.setBackground(colorFondo.darker());
        }
        
        /**
         * Se ejecuta cuando el mouse sale del área del botón
         * Restaura el color de fondo original
         */
        public void mouseExited(java.awt.event.MouseEvent evt) {
            boton.setBackground(colorFondo);
        }
    });
}

/**
 * Crea un icono de usuario personalizado dibujado programáticamente
 * 
 * @return Icon Un icono circular con silueta de usuario
 */
private Icon crearIconoUsuario() {
    // Crear un icono simple de usuario
    return new Icon() {
        /**
         * Dibuja el icono de usuario consistente en dos círculos concéntricos
         * 
         * @param c El componente donde se dibuja el icono
         * @param g El contexto gráfico para dibujar
         * @param x Coordenada x donde dibujar el icono
         * @param y Coordenada y donde dibujar el icono
         */
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Círculo exterior azul
            g2d.setColor(new Color(0, 102, 204));
            g2d.fillOval(x, y, 80, 80);
            
            // Círculo interior blanco (cabeza)
            g2d.setColor(Color.WHITE);
            g2d.fillOval(x + 15, y + 15, 50, 50);
            
            g2d.dispose();
        }
        
        /**
         * @return int El ancho del icono en píxeles
         */
        @Override
        public int getIconWidth() {
            return 80;
        }
        
        /**
         * @return int La altura del icono en píxeles
         */
        @Override
        public int getIconHeight() {
            return 80;
        }
    };
}
    private JPanel crearCampoConEtiqueta(String texto, JComponent campo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(80, 80, 80));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(campo);
        
        return panel;
    }
    
    // Métodos para que el controlador pueda acceder a los datos y configurar listeners
    public String getUsuario() {
        String usuario = usuarioField.getText();
        // Si el usuario todavía tiene el placeholder, devolver cadena vacía
        if (usuario.equals("Ingrese su usuario")) {
            return "";
        }
        return usuario;
    }
    
    public String getPassword() {
        String password = new String(passwordField.getPassword());
        // Si la contraseña todavía tiene el placeholder, devolver cadena vacía
        if (password.equals("Ingrese su contraseña")) {
            return "";
        }
        return password;
    }
    
    public void setLoginListener(ActionListener listener) {
        ingresarBtn.addActionListener(listener);
        passwordField.addActionListener(listener);
    }
    
    public void setSalirListener(ActionListener listener) {
        salirBtn.addActionListener(listener);
    }
    
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void limpiarCampos() {
        // Limpiar campos y restaurar placeholders
        usuarioField.setText("Ingrese su usuario");
        usuarioField.setForeground(Color.GRAY);
        usuarioField.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        
        passwordField.setEchoChar((char) 0);
        passwordField.setText("Ingrese su contraseña");
        passwordField.setForeground(Color.GRAY);
        passwordField.setFont(new Font("Segoe UI", Font.ITALIC, 14));
    }
}