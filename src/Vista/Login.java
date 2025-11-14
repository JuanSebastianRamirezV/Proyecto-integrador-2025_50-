package vista;

import controlador.SesionUsuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.TimerTask;
import java.util.Timer;

/**
 * Login — Interfaz de login avanzada y profesional (Swing)
 * Conserva el nombre original de la clase.
 */
public class Login extends JFrame {
    private static final Color PRIMARY = new Color(10, 132, 255);
    private static final Color PRIMARY_DARK = new Color(0, 102, 204);
    private static final Color CARD_BG = new Color(255, 255, 255, 230);
    private static final int WIDTH = 900;
    private static final int HEIGHT = 520;

    private Icon iconUser;
    private Icon iconPass;

    private IconLoader iconLoader = new IconLoader();

    private MaterialField tfUser;
    private MaterialPasswordField pfPass;
    private JButton btnLogin;
    private JButton btnCancel;
    private JLabel lblMessage;
    private LoadingSpinner spinner;

    public Login() {
        setTitle("Iniciar sesión - Software de Gestión ICA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0,0,WIDTH,HEIGHT,18,18));

        iconUser = iconLoader.load("/user.png");
        iconPass = iconLoader.load("/lock.png");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new ShadowPanel(), BorderLayout.CENTER);

        // Hacer la ventana draggable (sin dependencia de utilidades externas)
        final Point[] mouseDownCompCoords = new Point[1];
        addMouseListener(new MouseAdapter(){
            @Override public void mousePressed(MouseEvent e){ mouseDownCompCoords[0] = e.getPoint(); }
        });
        addMouseMotionListener(new MouseMotionAdapter(){
            @Override public void mouseDragged(MouseEvent e){
                Point currCoords = e.getLocationOnScreen();
                if (mouseDownCompCoords[0] != null) {
                    setLocation(currCoords.x - mouseDownCompCoords[0].x, currCoords.y - mouseDownCompCoords[0].y);
                }
            }
        });
        addWindowControls();
    }

    private void addWindowControls() {
        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(false);

        JPanel left = new JPanel(new BorderLayout());
        left.setPreferredSize(new Dimension(420, HEIGHT));
        left.setOpaque(false);
        left.setBorder(new EmptyBorder(30,30,30,30));

        JPanel ill = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0,PRIMARY, getWidth(), getHeight(), new Color(170,200,255));
                g2.setPaint(gp);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
            }
        };
        ill.setOpaque(false);
        ill.setLayout(new BorderLayout());
        
        // Título más corto y centrado
        JLabel title = new JLabel("Bienvenidos al Software");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(40,18,20,18));
        ill.add(title, BorderLayout.NORTH);

        // Texto mejor centrado y con márgenes adecuados
        JTextArea txt = new JTextArea("Accede con tu cuenta para continuar y gestionar tus operaciones.\n\nSi aún no tienes cuenta, ponte en contacto con el administrador.");
        txt.setEditable(false);
        txt.setOpaque(false);
        txt.setForeground(new Color(240,240,255));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(new EmptyBorder(20,25,20,25));
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        ill.add(txt, BorderLayout.CENTER);

        left.add(ill, BorderLayout.CENTER);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(14, CARD_BG);
        card.setPreferredSize(new Dimension(380, 400));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18,18,18,18));

        JLabel logo = new JLabel("Gestión ICA");
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(new Color(34,34,34));
        logo.setBorder(new EmptyBorder(6,0,10,0));

        lblMessage = new JLabel(" ");
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMessage.setForeground(new Color(180,30,30));
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMessage.setBorder(new EmptyBorder(4,0,8,0));

        tfUser = new MaterialField(iconUser, "Correo o usuario");
        pfPass = new MaterialPasswordField(iconPass, "Contraseña");

        spinner = new LoadingSpinner(20);
        spinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        spinner.setVisible(false);

        btnLogin = new RoundedButton("Ingresar");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> attemptLogin());

        btnCancel = new JButton("Salir");
        btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancel.setBorderPainted(false);
        btnCancel.setContentAreaFilled(false);
        btnCancel.addActionListener(e -> System.exit(0));

        card.add(logo);
        card.add(lblMessage);
        card.add(tfUser);
        card.add(Box.createRigidArea(new Dimension(0,10)));
        card.add(pfPass);
        card.add(Box.createRigidArea(new Dimension(0,12)));
        card.add(btnLogin);
        card.add(Box.createRigidArea(new Dimension(0,8)));
        card.add(spinner);
        card.add(Box.createRigidArea(new Dimension(0,6)));
        card.add(btnCancel);

        right.add(card);

        root.add(left, BorderLayout.WEST);
        root.add(right, BorderLayout.CENTER);

        getContentPane().add(root, BorderLayout.CENTER);

        getRootPane().setDefaultButton(btnLogin);
    }

    private void attemptLogin() {
        lblMessage.setText(" ");
        String user = tfUser.getText().trim();
        String pass = new String(pfPass.getPassword());

        if (user.isEmpty()) { lblMessage.setText("Ingresa tu usuario o correo."); tfUser.requestFocus(); return; }
        if (pass.isEmpty()) { lblMessage.setText("Ingresa tu contraseña."); pfPass.requestFocus(); return; }

        spinner.setVisible(true);
        btnLogin.setEnabled(false);

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    spinner.setVisible(false);
                    btnLogin.setEnabled(true);

                    if (authenticate(user, pass)) {
                        openMenuPrincipalSafely(user);
                        dispose();
                    } else {
                        lblMessage.setText("Usuario o contraseña incorrectos.");
                        Animations.shakeWindow(Login.this);
                    }
                });
            }
        }, 1200);
    }

    private boolean authenticate(String user, String pass) {
        // Credenciales para administrador
        if ("admin".equals(user) && "1234".equals(pass)) {
            SesionUsuario.getInstance().iniciarSesion(user, "ADMIN");
            return true;
        }
        // Credenciales para productor
        if ("productor".equals(user) && "1234".equals(pass)) {
            SesionUsuario.getInstance().iniciarSesion(user, "PRODUCTOR");
            return true;
        }
        // Credenciales para inspector
        if ("inspector".equals(user) && "inspector123".equals(pass)) {
            SesionUsuario.getInstance().iniciarSesion(user, "INSPECTOR");
            return true;
        }
        return false;
    }

    private void openMenuPrincipalSafely(String usuario) {
        try {
            JFrame menuFrame;
            
            // Obtener el tipo de usuario de la sesión
            String tipoUsuario = SesionUsuario.getInstance().getTipoUsuario();
            
            if ("ADMIN".equals(tipoUsuario)) {
                menuFrame = (JFrame) Class.forName("vista.MenuPrincipal").getDeclaredConstructor().newInstance();
                vista.MenuPrincipal menuPrincipal = (vista.MenuPrincipal) menuFrame;
                menuPrincipal.setUsuarioActual(usuario);
            } else if ("PRODUCTOR".equals(tipoUsuario)) {
                menuFrame = (JFrame) Class.forName("vista.MenuPrincipalProductor").getDeclaredConstructor().newInstance();
                vista.MenuPrincipalProductor menuProductor = (vista.MenuPrincipalProductor) menuFrame;
                menuProductor.setUsuarioActual(usuario);
            } else if ("INSPECTOR".equals(tipoUsuario)) {
                // Redirigir al menú principal del inspector
                menuFrame = (JFrame) Class.forName("vista.MenuPrincipalInspector").getDeclaredConstructor().newInstance();
                vista.MenuPrincipalInspector menuInspector = (vista.MenuPrincipalInspector) menuFrame;
                menuInspector.setUsuarioActual(usuario);
            } else {
                // Por defecto, menú principal
                menuFrame = (JFrame) Class.forName("vista.MenuPrincipal").getDeclaredConstructor().newInstance();
                vista.MenuPrincipal menuPrincipal = (vista.MenuPrincipal) menuFrame;
                menuPrincipal.setUsuarioActual(usuario);
            }
            
            menuFrame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Inicio correcto, pero no se pudo abrir el menú: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static class RoundedPanel extends JPanel {
        private int radius; private Color bg;
        public RoundedPanel(int r, Color bg) { this.radius = r; this.bg = bg; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),radius,radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class ShadowPanel extends JPanel {
        public ShadowPanel() { setOpaque(false); setLayout(new BorderLayout()); }
        @Override protected void paintComponent(Graphics g) {
            int w = getWidth(), h = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0,0,0,30));
            g2.fillRoundRect(10,10,w-20,h-20,18,18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setPreferredSize(new Dimension(240,40));
            setMaximumSize(new Dimension(Short.MAX_VALUE,40));
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setBackground(PRIMARY);
            addMouseListener(new MouseAdapter(){
                @Override public void mouseEntered(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape s = new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),12,12);
            g2.setColor(getBackground().darker());
            g2.fill(s);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(1,1,getWidth()-2,getHeight()-2,10,10));
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int sw = fm.stringWidth(getText());
            int sh = fm.getAscent();
            g2.drawString(getText(), (getWidth()-sw)/2, (getHeight()+sh)/2-3);
            g2.dispose();
        }
    }

    private static class MaterialField extends JPanel {
        private JTextField field;
        public MaterialField(Icon icon, String placeholder) {
            setLayout(new BorderLayout());
            setOpaque(false);
            JLabel ic = new JLabel(icon);
            ic.setBorder(new EmptyBorder(0,6,0,8));
            field = new JTextField();
            field.setBorder(BorderFactory.createEmptyBorder(10,8,10,8));
            field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            field.setText(placeholder);
            field.setForeground(Color.GRAY);
            field.addFocusListener(new FocusAdapter(){
                public void focusGained(FocusEvent e){ if (field.getText().equals(placeholder)) { field.setText(""); field.setForeground(Color.BLACK); } }
                public void focusLost(FocusEvent e){ if (field.getText().isEmpty()) { field.setForeground(Color.GRAY); field.setText(placeholder); } }
            });
            add(ic, BorderLayout.WEST);
            add(field, BorderLayout.CENTER);
            setMaximumSize(new Dimension(Short.MAX_VALUE,44));
            setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        }
        public String getText(){ String t = field.getText(); return t == null ? "" : t; }
        public void requestFocus(){ field.requestFocus(); }
    }

    private static class MaterialPasswordField extends JPanel {
        private JPasswordField pf;
        public MaterialPasswordField(Icon icon, String placeholder) {
            setLayout(new BorderLayout()); setOpaque(false);
            JLabel ic = new JLabel(icon);
            ic.setBorder(new EmptyBorder(0,6,0,8));
            pf = new JPasswordField();
            pf.setBorder(BorderFactory.createEmptyBorder(10,8,10,8));
            pf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            pf.setEchoChar((char)0);
            pf.setText(placeholder);
            pf.setForeground(Color.GRAY);
            pf.addFocusListener(new FocusAdapter(){
                public void focusGained(FocusEvent e){ if (String.valueOf(pf.getPassword()).equals(placeholder)) { pf.setText(""); pf.setEchoChar('\u2022'); pf.setForeground(Color.BLACK); } }
                public void focusLost(FocusEvent e){ if (String.valueOf(pf.getPassword()).isEmpty()) { pf.setForeground(Color.GRAY); pf.setText(placeholder); pf.setEchoChar((char)0); } }
            });
            JButton eye = new JButton("👁");
            eye.setBorderPainted(false); eye.setContentAreaFilled(false); eye.setFocusPainted(false);
            eye.addActionListener(e -> {
                if (pf.getEchoChar() == (char)0) pf.setEchoChar('\u2022'); else pf.setEchoChar((char)0);
            });
            eye.setPreferredSize(new Dimension(40,40));
            add(ic, BorderLayout.WEST);
            add(pf, BorderLayout.CENTER);
            add(eye, BorderLayout.EAST);
            setMaximumSize(new Dimension(Short.MAX_VALUE,44));
            setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        }
        public char[] getPassword(){ return pf.getPassword(); }
        public void requestFocus(){ pf.requestFocus(); }
    }

    private static class IconLoader {
        public Icon load(String path) {
            try {
                java.net.URL res = Login.class.getResource(path);
                if (res != null) return new ImageIcon(res);
            } catch (Exception ignored) {}
            return null;
        }
    }

    private static class LoadingSpinner extends JComponent {
        private int size; private int angle = 0; private Timer t;
        public LoadingSpinner(int size) { this.size = size; setPreferredSize(new Dimension(size+4,size+4));
            t = new Timer();
            t.scheduleAtFixedRate(new TimerTask(){ public void run(){ angle = (angle+15)%360; SwingUtilities.invokeLater(() -> repaint()); } }, 0, 80);
            setVisible(false);
        }
        @Override protected void paintComponent(Graphics g){ super.paintComponent(g); Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2.setStroke(new BasicStroke(3f)); g2.setColor(PRIMARY); g2.drawArc(2,2,size,size,angle,90); g2.dispose(); }
    }

    private static class Animations {
        public static void shakeWindow(Window w) {
            final Point p = w.getLocation();
            final int shakeDist = 8; final int cycles = 10;
            new Thread(() -> {
                try {
                    for (int i=0;i<cycles;i++) {
                        int dx = (i%2==0)?shakeDist:-shakeDist;
                        SwingUtilities.invokeLater(() -> w.setLocation(p.x+dx, p.y));
                        Thread.sleep(20);
                    }
                } catch (InterruptedException ignored) {}
                SwingUtilities.invokeLater(() -> w.setLocation(p));
            }).start();
        }
    }
}