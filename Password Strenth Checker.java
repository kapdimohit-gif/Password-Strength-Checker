import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * PasswordCheckerApp.java
 * Single-file Password Strength Checker — Swing UI + backend logic combined.
 *
 * Compile : javac PasswordCheckerApp.java
 * Run     : java PasswordCheckerApp
 */
public class PasswordCheckerApp extends JFrame {

    // ═══════════════════════════════════════════════════════════════════════════
    //  BACKEND — Password evaluation logic (inner class)
    // ═══════════════════════════════════════════════════════════════════════════

    enum Strength { WEAK, FAIR, STRONG, VERY_STRONG }

    static class PasswordResult {
        final Strength strength;
        final int      score;          // 0 – 4
        final boolean  hasMinLength;
        final boolean  hasUppercase;
        final boolean  hasLowercase;
        final boolean  hasSpecialChar;
        final boolean  hasDigit;

        PasswordResult(boolean hasMinLength, boolean hasUppercase,
                       boolean hasLowercase, boolean hasSpecialChar, boolean hasDigit) {
            this.hasMinLength   = hasMinLength;
            this.hasUppercase   = hasUppercase;
            this.hasLowercase   = hasLowercase;
            this.hasSpecialChar = hasSpecialChar;
            this.hasDigit       = hasDigit;

            int s = 0;
            if (hasMinLength)   s++;
            if (hasUppercase)   s++;
            if (hasLowercase)   s++;
            if (hasSpecialChar) s++;
            this.score = hasDigit ? Math.min(s + 1, 4) : s;  // digit = bonus

            if      (this.score <= 1) this.strength = Strength.WEAK;
            else if (this.score == 2) this.strength = Strength.FAIR;
            else if (this.score == 3) this.strength = Strength.STRONG;
            else                      this.strength = Strength.VERY_STRONG;
        }
    }

    /** Evaluate a password and return a PasswordResult. */
    static PasswordResult evaluate(String password) {
        if (password == null) password = "";
        boolean hasMinLength   = password.length() >= 8;
        boolean hasUppercase   = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase   = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit       = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(c ->
                "!@#$%^&*()_+-=[]{}|;':\",./<>?`~\\".indexOf(c) >= 0);
        return new PasswordResult(hasMinLength, hasUppercase, hasLowercase, hasSpecialChar, hasDigit);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  FRONTEND — Java Swing UI
    // ═══════════════════════════════════════════════════════════════════════════

    // ── Palette ───────────────────────────────────────────────────────────────
    private static final Color BG          = new Color(13,  17,  23);
    private static final Color SURFACE     = new Color(22,  27,  34);
    private static final Color BORDER_CLR  = new Color(48,  54,  61);
    private static final Color TEXT_PRI    = new Color(230, 237, 243);
    private static final Color TEXT_SEC    = new Color(125, 133, 144);
    private static final Color ACCENT      = new Color(88,  166, 255);
    private static final Color WEAK_CLR    = new Color(218,  54,  51);
    private static final Color FAIR_CLR    = new Color(210, 153,  34);
    private static final Color STRONG_CLR  = new Color(46,  160,  67);
    private static final Color VSTRONG_CLR = new Color(88,  166, 255);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD,  22);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BOLD  = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font FONT_MONO  = new Font("Consolas",  Font.PLAIN, 15);
    private static final Font FONT_BADGE = new Font("Segoe UI", Font.BOLD,  12);

    // ── Rule text ─────────────────────────────────────────────────────────────
    private static final String[] RULE_TEXT = {
        "At least 8 characters",
        "At least 1 uppercase letter (A–Z)",
        "At least 1 lowercase letter (a–z)",
        "At least 1 special symbol (!@#$…)",
        "At least 1 digit (0–9)  — bonus"
    };

    // ── Swing components ──────────────────────────────────────────────────────
    private JPasswordField passwordField;
    private JToggleButton  toggleVisibility;
    private JPanel         strengthBar;
    private JLabel         strengthLabel;
    private JLabel[]       ruleIcons;
    private JLabel[]       ruleLabels;

    // ── Constructor ───────────────────────────────────────────────────────────
    public PasswordCheckerApp() {
        setTitle("Password Strength Checker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        add(buildMainPanel(), BorderLayout.CENTER);
        pack();
        setMinimumSize(new Dimension(480, 0));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── Main panel ────────────────────────────────────────────────────────────
    private JPanel buildMainPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(36, 40, 36, 40));
        panel.add(buildHeader());
        panel.add(Box.createVerticalStrut(24));
        panel.add(buildInputSection());
        panel.add(Box.createVerticalStrut(20));
        panel.add(buildStrengthSection());
        panel.add(Box.createVerticalStrut(20));
        panel.add(buildRulesSection());
        return panel;
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setBackground(BG);
        p.setAlignmentX(LEFT_ALIGNMENT);

        JLabel title = new JLabel("🔐 Password Strength");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_PRI);

        JLabel sub = new JLabel("  Checker");
        sub.setFont(FONT_TITLE);
        sub.setForeground(ACCENT);

        p.add(title);
        p.add(sub);
        return p;
    }

    // ── Password input ────────────────────────────────────────────────────────
    private JPanel buildInputSection() {
        JPanel wrapper = new JPanel();
        wrapper.setBackground(BG);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lbl = styledLabel("Enter Password", TEXT_SEC, FONT_LABEL);
        lbl.setBorder(new EmptyBorder(0, 0, 6, 0));
        wrapper.add(lbl);

        JPanel inputRow = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(BORDER_CLR);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
            }
        };
        inputRow.setOpaque(false);
        inputRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        inputRow.setBorder(new EmptyBorder(4, 14, 4, 8));

        passwordField = new JPasswordField(20);
        passwordField.setFont(FONT_MONO);
        passwordField.setForeground(TEXT_PRI);
        passwordField.setCaretColor(ACCENT);
        passwordField.setOpaque(false);
        passwordField.setBorder(BorderFactory.createEmptyBorder());
        passwordField.setEchoChar('●');
        passwordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { onPasswordChanged(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { onPasswordChanged(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { onPasswordChanged(); }
        });

        toggleVisibility = new JToggleButton("Show");
        toggleVisibility.setFont(FONT_BOLD);
        toggleVisibility.setForeground(ACCENT);
        toggleVisibility.setOpaque(false);
        toggleVisibility.setBorderPainted(false);
        toggleVisibility.setFocusPainted(false);
        toggleVisibility.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleVisibility.addActionListener(e -> {
            if (toggleVisibility.isSelected()) {
                passwordField.setEchoChar((char) 0);
                toggleVisibility.setText("Hide");
            } else {
                passwordField.setEchoChar('●');
                toggleVisibility.setText("Show");
            }
        });

        inputRow.add(passwordField,     BorderLayout.CENTER);
        inputRow.add(toggleVisibility,  BorderLayout.EAST);
        wrapper.add(inputRow);
        return wrapper;
    }

    // ── Strength bar ──────────────────────────────────────────────────────────
    private JPanel buildStrengthSection() {
        JPanel wrapper = new JPanel();
        wrapper.setBackground(BG);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setAlignmentX(LEFT_ALIGNMENT);

        JPanel labelRow = new JPanel(new BorderLayout());
        labelRow.setBackground(BG);
        labelRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        labelRow.add(styledLabel("Password Strength", TEXT_SEC, FONT_LABEL), BorderLayout.WEST);
        strengthLabel = styledLabel("—", TEXT_SEC, FONT_BADGE);
        labelRow.add(strengthLabel, BorderLayout.EAST);
        wrapper.add(labelRow);
        wrapper.add(Box.createVerticalStrut(8));

        JPanel barRow = new JPanel(new GridLayout(1, 4, 5, 0));
        barRow.setBackground(BG);
        barRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        strengthBar = barRow;

        for (int i = 0; i < 4; i++) {
            JPanel seg = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                    g2.dispose();
                }
            };
            seg.setOpaque(false);
            seg.setBackground(BORDER_CLR);
            barRow.add(seg);
        }
        wrapper.add(barRow);
        return wrapper;
    }

    // ── Rules checklist ───────────────────────────────────────────────────────
    private JPanel buildRulesSection() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(BORDER_CLR);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setAlignmentX(LEFT_ALIGNMENT);
        card.add(styledLabel("Requirements", TEXT_PRI, FONT_BOLD));
        card.add(Box.createVerticalStrut(12));

        ruleIcons  = new JLabel[RULE_TEXT.length];
        ruleLabels = new JLabel[RULE_TEXT.length];

        for (int i = 0; i < RULE_TEXT.length; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
            row.setOpaque(false);

            ruleIcons[i] = new JLabel("○");
            ruleIcons[i].setFont(FONT_BOLD);
            ruleIcons[i].setForeground(TEXT_SEC);

            ruleLabels[i] = new JLabel(RULE_TEXT[i]);
            ruleLabels[i].setFont(FONT_LABEL);
            ruleLabels[i].setForeground(TEXT_SEC);

            row.add(ruleIcons[i]);
            row.add(ruleLabels[i]);

            if (i == 4) {                                       // bonus rule badge
                JLabel bonus = new JLabel("(bonus)");
                bonus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                bonus.setForeground(new Color(88, 166, 255, 150));
                row.add(bonus);
            }
            card.add(row);
        }
        return card;
    }

    // ── Live update ───────────────────────────────────────────────────────────
    private void onPasswordChanged() {
        String pwd = new String(passwordField.getPassword());
        PasswordResult r = evaluate(pwd);        // ← calls the backend method above
        updateStrengthBar(r);
        updateRules(r);
    }

    private void updateStrengthBar(PasswordResult r) {
        Color  color;
        String label;

        if (r.score == 0) {
            color = BORDER_CLR; label = "—";
        } else {
            switch (r.strength) {
                case WEAK:       color = WEAK_CLR;    label = "Weak";        break;
                case FAIR:       color = FAIR_CLR;    label = "Fair";        break;
                case STRONG:     color = STRONG_CLR;  label = "Strong";      break;
                default:         color = VSTRONG_CLR; label = "Very Strong"; break;
            }
        }

        strengthLabel.setForeground(r.score == 0 ? TEXT_SEC : color);
        strengthLabel.setText(label);

        Component[] segs = strengthBar.getComponents();
        for (int i = 0; i < segs.length; i++) {
            JPanel seg = (JPanel) segs[i];
            seg.setBackground(i < r.score ? color : BORDER_CLR);
            seg.repaint();
        }
    }

    private void updateRules(PasswordResult r) {
        boolean[] met = { r.hasMinLength, r.hasUppercase, r.hasLowercase, r.hasSpecialChar, r.hasDigit };
        for (int i = 0; i < met.length; i++) {
            if (met[i]) {
                ruleIcons[i].setText("✓");
                ruleIcons[i].setForeground(STRONG_CLR);
                ruleLabels[i].setForeground(TEXT_PRI);
            } else {
                ruleIcons[i].setText("○");
                ruleIcons[i].setForeground(TEXT_SEC);
                ruleLabels[i].setForeground(TEXT_SEC);
            }
        }
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private JLabel styledLabel(String text, Color color, Font font) {
        JLabel l = new JLabel(text);
        l.setForeground(color);
        l.setFont(font);
        return l;
    }

    // ── Entry point ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(PasswordCheckerApp::new);
    }
}