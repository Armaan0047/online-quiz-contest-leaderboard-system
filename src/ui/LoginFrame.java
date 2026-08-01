package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private final UserDAO userDAO;

    public LoginFrame() {
        this.userDAO = new UserDAO();
        setTitle("Online Quiz Contest & Leaderboard System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(UIStyle.COLOR_BG);

        // Header Banner
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyle.COLOR_NAV_BG);
        headerPanel.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel lblTitle = new JLabel("Online Quiz Contest", SwingConstants.CENTER);
        lblTitle.setFont(UIStyle.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("College Minor Project System", SwingConstants.CENTER);
        lblSub.setFont(UIStyle.FONT_SMALL);
        lblSub.setForeground(UIStyle.COLOR_TEXT_MUTED);

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.SOUTH);

        rootPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Card Panel (Form)
        JPanel cardPanel = UIStyle.createCardPanel();
        cardPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblLoginHeader = new JLabel("Account Login", SwingConstants.CENTER);
        lblLoginHeader.setFont(UIStyle.FONT_SUBTITLE);
        lblLoginHeader.setForeground(UIStyle.COLOR_TEXT_DARK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        cardPanel.add(lblLoginHeader, gbc);

        gbc.gridwidth = 1;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(UIStyle.FONT_BOLD);
        gbc.gridx = 0; gbc.gridy = 1;
        cardPanel.add(lblUser, gbc);

        txtUsername = UIStyle.createTextField();
        txtUsername.setPreferredSize(new Dimension(220, 35));
        gbc.gridx = 1; gbc.gridy = 1;
        cardPanel.add(txtUsername, gbc);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(UIStyle.FONT_BOLD);
        gbc.gridx = 0; gbc.gridy = 2;
        cardPanel.add(lblPass, gbc);

        txtPassword = UIStyle.createPasswordField();
        txtPassword.setPreferredSize(new Dimension(220, 35));
        gbc.gridx = 1; gbc.gridy = 2;
        cardPanel.add(txtPassword, gbc);

        btnLogin = UIStyle.createButton("Log In", UIStyle.COLOR_PRIMARY, Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(220, 38));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(18, 10, 8, 10);
        cardPanel.add(btnLogin, gbc);

        // Submit on Enter key press
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        txtUsername.addKeyListener(enterKeyListener);
        txtPassword.addKeyListener(enterKeyListener);
        btnLogin.addActionListener(e -> handleLogin());

        // Footnote inside card
        JLabel lblInfo = new JLabel("Use demo accounts (Admin_Armaan / Achint)", SwingConstants.CENTER);
        lblInfo.setFont(UIStyle.FONT_SMALL);
        lblInfo.setForeground(UIStyle.COLOR_TEXT_MUTED);
        gbc.gridy = 4; gbc.insets = new Insets(5, 10, 5, 10);
        cardPanel.add(lblInfo, gbc);

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        centerWrapper.setBackground(UIStyle.COLOR_BG);
        centerWrapper.add(cardPanel);

        rootPanel.add(centerWrapper, BorderLayout.CENTER);

        setContentPane(rootPanel);
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User authenticatedUser = userDAO.authenticate(username, password);

        if (authenticatedUser != null) {
            JOptionPane.showMessageDialog(this, "Welcome, " + authenticatedUser.getFullName() + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

            if ("ADMIN".equalsIgnoreCase(authenticatedUser.getRole())) {
                new AdminDashboardFrame(authenticatedUser).setVisible(true);
            } else {
                new UserDashboardFrame(authenticatedUser).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password.\nPlease ensure MySQL database is running and schema.sql is executed.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
