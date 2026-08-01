package ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UIStyle {

    // Palette Colors
    public static final Color COLOR_NAV_BG = new Color(15, 23, 42);       // #0F172A Slate Dark
    public static final Color COLOR_PRIMARY = new Color(37, 99, 235);      // #2563EB Royal Blue
    public static final Color COLOR_PRIMARY_HOVER = new Color(29, 78, 216);// #1D4ED8
    public static final Color COLOR_BG = new Color(248, 250, 252);        // #F8FAFC
    public static final Color COLOR_CARD_BG = Color.WHITE;
    public static final Color COLOR_BORDER = new Color(226, 232, 240);     // #E2E8F0
    public static final Color COLOR_TEXT_DARK = new Color(30, 41, 59);     // #1E293B
    public static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);  // #64748B
    public static final Color COLOR_SUCCESS = new Color(22, 163, 74);     // #16A34A Green
    public static final Color COLOR_DANGER = new Color(220, 38, 38);      // #DC2626 Red
    public static final Color COLOR_ACCENT = new Color(245, 158, 11);     // #F59E0B Amber

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    /**
     * Creates a styled button with consistent padding, custom background rendering, and high-contrast text.
     */
    public static JButton createButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bg.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bg.brighter());
                } else {
                    g2.setColor(bg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(FONT_BOLD);
        button.setForeground(fg);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new CompoundBorder(
                new LineBorder(bg.darker(), 1, true),
                new EmptyBorder(8, 16, 8, 16)
        ));
        return button;
    }

    /**
     * Styles text field with clean border and padding.
     */
    public static JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(FONT_BODY);
        textField.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        return textField;
    }

    /**
     * Styles password field with clean border and padding.
     */
    public static JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(FONT_BODY);
        passwordField.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        return passwordField;
    }

    /**
     * Applies modern visual styling to JTable.
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(32);
        table.setGridColor(COLOR_BORDER);
        table.setSelectionBackground(new Color(224, 231, 255));
        table.setSelectionForeground(COLOR_TEXT_DARK);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(new Color(241, 245, 249));
        header.setForeground(COLOR_TEXT_DARK);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));

        // Center alignment for specific columns if renderer applied
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * Wraps a component inside a clean card panel with white background and border.
     */
    public static JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return card;
    }
}
