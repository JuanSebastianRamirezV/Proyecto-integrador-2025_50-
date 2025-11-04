package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame {
    private JTextField usuarioField;
    private JPasswordField passwordField;
    private JButton ingresarBtn;
    private JButton salirBtn;

    public Login() {
        setTitle("Acceso al Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Para permitir esquinas redondeadas si el SO lo soporta
        setSize(420, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel principal con sombra y padding
        JPanel main = new JPanel();
        main.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);

        // Cabecera: Título y botón cerrar ligero
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Iniciar sesión");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        // Botón salir (icono simple)
        salirBtn = createFlatButton("Salir");
        salirBtn.setMnemonic(KeyEvent.VK_S);

        header.add(title, BorderLayout.WEST);
        header.add(salirBtn, BorderLayout.EAST);

        main.add(header);
        main.add(Box.createRigidArea(new Dimension(0, 12)));

        // Icono/Logo centrado
        JLabel logo = new JLabel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setIcon(scaleIcon(UIManager.getIcon("FileView.computerIcon"), 72, 72));
        logo.setBorder(BorderFactory.createEmptyBorder(8, 8, 18, 8));
        main.add(logo);

        // Panel de formulario
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        usuarioField = new JTextField();
        usuarioField.setMaximumSize(new Dimension(360, 44));
        usuarioField.setPreferredSize(new Dimension(360, 44));
        usuarioField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usuarioField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        setPlaceholder(usuarioField, "Usuario");

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(360, 44));
        passwordField.setPreferredSize(new Dimension(360, 44));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        setPlaceholder(passwordField, "Contraseña");

        form.add(usuarioField);
        form.add(Box.createRigidArea(new Dimension(0, 12)));
        form.add(passwordField);
        form.add(Box.createRigidArea(new Dimension(0, 18)));

        // Botones
        ingresarBtn = createPrimaryButton("Ingresar");
        ingresarBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        ingresarBtn.setMnemonic(KeyEvent.VK_I);

        // Agregar soporte para presionar Enter en password para disparar ingresar
        passwordField.addActionListener(e -> ingresarBtn.doClick());
        usuarioField.addActionListener(e -> passwordField.requestFocusInWindow());

        form.add(ingresarBtn);
        form.add(Box.createRigidArea(new Dimension(0, 10)));

        // Pie con ayuda o estado
        JLabel ayuda = new JLabel("¿Olvidaste tu contraseña? Contacta al administrador.");
        ayuda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ayuda.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.add(ayuda);

        main.add(form);

        // Panel contenedor con fondo y borde redondeado
        JPanel container = new RoundedPanel(20, new Color(250, 250, 250));
        container.setLayout(new BorderLayout());
        container.add(main, BorderLayout.CENTER);
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Fondo general
        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(238, 241, 246));
        background.add(container);

        add(background, BorderLayout.CENTER);

        // Detección de ESC para cerrar
        getRootPane().registerKeyboardAction(e -> salirBtn.doClick(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Para mostrar bordes redondeados en el frame (si el SO lo permite)
        try {
            setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        } catch (Exception ignored) {}

        // Inicializar con placeholders
        limpiarCampos();
    }

    // Conserva los mismos nombres de método para no romper la funcionalidad existente
    public void addIngresarListener(ActionListener listener) {
        ingresarBtn.addActionListener(listener);
    }

    public void addSalirListener(ActionListener listener) {
        salirBtn.addActionListener(listener);
    }

    public String getUsuario() {
        String t = usuarioField.getText();
        return t.equals("Usuario") ? "" : t;
    }

    public String getPassword() {
        String p = new String(passwordField.getPassword());
        return p.equals("Contraseña") ? "" : p;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void limpiarCampos() {
        usuarioField.setText("Usuario");
        usuarioField.setForeground(new Color(140, 140, 140));

        passwordField.setEchoChar((char) 0);
        passwordField.setText("Contraseña");
        passwordField.setForeground(new Color(140, 140, 140));

        // Reestablecer foco visual
        usuarioField.requestFocusInWindow();
    }

    // ------------------ Helpers visuales ------------------
    private static void setPlaceholder(final JTextField field, final String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar('\u2022');
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(new Color(140, 140, 140));
                    field.setText(placeholder);
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar((char) 0);
                    }
                }
            }
        });
    }

    private static ImageIcon scaleIcon(Icon base, int w, int h) {
        if (base == null) return null;
        Image img = ((ImageIcon) base).getImage();
        Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private static JButton createPrimaryButton(String text) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 120, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return b;
    }

    private static JButton createFlatButton(String text) {
        JButton b = new JButton(text);
        b.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return b;
    }

    // Panel con esquinas redondeadas
    private static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color backgroundColor;

        RoundedPanel(int radius, Color bg) {
            cornerRadius = radius;
            backgroundColor = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.setColor(new Color(220, 220, 220));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Para pruebas rápidas desde este archivo (opcional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            Login l = new Login();
            l.setVisible(true);
        });
    }
}
