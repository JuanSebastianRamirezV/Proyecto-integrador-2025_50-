package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class Login extends JFrame {
    private JTextField usuarioField;
    private JPasswordField passwordField;
    private JButton ingresarBtn;
    private JButton salirBtn;

    public Login() {
        setTitle("Software de Gestión - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 580); // Aumenté la altura para que no corte el mensaje
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Panel principal con fondo azul similar al sistema
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradiente azul similar al del sistema
                Color color1 = new Color(0, 78, 152);  // Azul oscuro
                Color color2 = new Color(0, 112, 192); // Azul medio
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Panel de contenido (tarjeta blanca) - Más padding inferior
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 60, 40)); // Más padding abajo
        contentPanel.setOpaque(false);
        contentPanel.setPreferredSize(new Dimension(400, 500));

        // Título del sistema - SOLO "SOFTWARE DE GESTIÓN"
        JLabel tituloSistema = new JLabel("SOFTWARE DE GESTIÓN");
        tituloSistema.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tituloSistema.setForeground(new Color(0, 78, 152));
        tituloSistema.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloSistema.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Icono o logo
        JLabel iconoLabel = new JLabel();
        iconoLabel.setIcon(crearIconoSistema());
        iconoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconoLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));

        // Título del login
        JLabel loginTitle = new JLabel("INICIAR SESIÓN");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginTitle.setForeground(new Color(60, 60, 60));
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        // Panel de formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(300, 200));

        // Campo usuario
        JPanel usuarioPanel = new JPanel(new BorderLayout());
        usuarioPanel.setOpaque(false);
        usuarioPanel.setMaximumSize(new Dimension(300, 60));
        
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usuarioLabel.setForeground(new Color(80, 80, 80));
        usuarioLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
        
        usuarioField = new JTextField();
        usuarioField.setMaximumSize(new Dimension(300, 40));
        usuarioField.setPreferredSize(new Dimension(300, 40));
        usuarioField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usuarioField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        
        usuarioPanel.add(usuarioLabel, BorderLayout.NORTH);
        usuarioPanel.add(usuarioField, BorderLayout.CENTER);

        // Espacio entre campos
        formPanel.add(usuarioPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Campo contraseña
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.setMaximumSize(new Dimension(300, 60));
        
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(80, 80, 80));
        passwordLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
        
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 35)));

        // Panel de botones
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botonesPanel.setOpaque(false);
        botonesPanel.setMaximumSize(new Dimension(300, 50));

        ingresarBtn = crearBotonEstiloSistema("Ingresar", new Color(0, 112, 192));
        salirBtn = crearBotonEstiloSistema("Salir", new Color(120, 120, 120));

        // Atajos de teclado
        ingresarBtn.setMnemonic(KeyEvent.VK_I);
        salirBtn.setMnemonic(KeyEvent.VK_S);
        passwordField.addActionListener(e -> ingresarBtn.doClick());

        botonesPanel.add(ingresarBtn);
        botonesPanel.add(salirBtn);

        // Más espacio antes del mensaje de ayuda
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Mensaje de ayuda - con suficiente espacio
        JPanel ayudaPanel = new JPanel();
        ayudaPanel.setLayout(new BoxLayout(ayudaPanel, BoxLayout.Y_AXIS));
        ayudaPanel.setOpaque(false);
        ayudaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel ayudaLabel = new JLabel("Contacte al administrador si tiene problemas de acceso");
        ayudaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ayudaLabel.setForeground(new Color(120, 120, 120));
        ayudaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        ayudaPanel.add(ayudaLabel);
        ayudaPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Ensamblar componentes
        contentPanel.add(tituloSistema);
        contentPanel.add(iconoLabel);
        contentPanel.add(loginTitle);
        contentPanel.add(formPanel);
        contentPanel.add(botonesPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(ayudaPanel);

        // Centrar el contenido con más margen inferior
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 30, 0); // Más margen inferior
        centerPanel.add(contentPanel, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Configurar ESC para salir
        getRootPane().registerKeyboardAction(
            e -> salirBtn.doClick(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // Inicializar
        limpiarCampos();
    }

    private Icon crearIconoSistema() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Círculo exterior azul
                g2d.setColor(new Color(0, 112, 192));
                g2d.fillOval(x, y, 80, 80);
                
                // Icono de usuario/sistema
                g2d.setColor(Color.WHITE);
                g2d.fillOval(x + 15, y + 15, 50, 50);
                
                // Símbolo de usuario
                g2d.setColor(new Color(0, 112, 192));
                g2d.fillOval(x + 30, y + 30, 20, 20);
                
                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return 80;
            }

            @Override
            public int getIconHeight() {
                return 80;
            }
        };
    }

    private JButton crearBotonEstiloSistema(String texto, Color color) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo del botón
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setPreferredSize(new Dimension(120, 40));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }

    // Métodos existentes (se mantienen igual para no romper funcionalidad)
    public void addIngresarListener(ActionListener listener) {
        ingresarBtn.addActionListener(listener);
    }

    public void addSalirListener(ActionListener listener) {
        salirBtn.addActionListener(listener);
    }

    public String getUsuario() {
        return usuarioField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de Acceso", JOptionPane.ERROR_MESSAGE);
    }

    public void limpiarCampos() {
        usuarioField.setText("");
        passwordField.setText("");
        usuarioField.requestFocusInWindow();
    }

    // Para compatibilidad con el código existente
    public void setLoginListener(ActionListener listener) {
        addIngresarListener(listener);
    }

    public void setSalirListener(ActionListener listener) {
        addSalirListener(listener);
    }

    // Método main para pruebas
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            Login login = new Login();
            login.setVisible(true);
            
            // Ejemplo de listeners
            login.addIngresarListener(e -> {
                System.out.println("Usuario: " + login.getUsuario());
                System.out.println("Password: " + login.getPassword());
            });
            
            login.addSalirListener(e -> System.exit(0));
        });
    }
}