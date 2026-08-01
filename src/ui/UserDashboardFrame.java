package ui;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserDashboardFrame extends JFrame {

    private final User currentUser;
    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    private AttemptQuizPanel attemptQuizPanel;
    private LeaderboardPanel leaderboardPanel;
    private ViewResultsPanel viewResultsPanel;

    public UserDashboardFrame(User user) {
        this.currentUser = user;

        setTitle("Online Quiz Contest & Leaderboard System - Student Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setLocationRelativeTo(null);

        initMenuBar();
        initComponents();
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuNav = new JMenu("Navigation");
        JMenuItem miQuiz = new JMenuItem("Attempt Quiz");
        JMenuItem miLeaderboard = new JMenuItem("View Leaderboard");
        JMenuItem miHistory = new JMenuItem("My Performance History");
        JMenuItem miExit = new JMenuItem("Exit Application");

        miQuiz.addActionListener(e -> showCard("QUIZ"));
        miLeaderboard.addActionListener(e -> showCard("LEADERBOARD"));
        miHistory.addActionListener(e -> showCard("HISTORY"));
        miExit.addActionListener(e -> System.exit(0));

        menuNav.add(miQuiz);
        menuNav.add(miLeaderboard);
        menuNav.add(miHistory);
        menuNav.addSeparator();
        menuNav.add(miExit);

        JMenu menuAccount = new JMenu("Account");
        JMenuItem miChangePass = new JMenuItem("Change Password");
        JMenuItem miLogout = new JMenuItem("Logout");

        miChangePass.addActionListener(e -> handleChangePassword());
        miLogout.addActionListener(e -> handleLogout());

        menuAccount.add(miChangePass);
        menuAccount.add(miLogout);

        menuBar.add(menuNav);
        menuBar.add(menuAccount);

        setJMenuBar(menuBar);
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(UIStyle.COLOR_BG);

        // --- TOP HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyle.COLOR_NAV_BG);
        headerPanel.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lblLogo = new JLabel("Student Portal");
        lblLogo.setFont(UIStyle.FONT_TITLE);
        lblLogo.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setBackground(UIStyle.COLOR_NAV_BG);

        JLabel lblUserInfo = new JLabel("Welcome, " + currentUser.getFullName());
        lblUserInfo.setFont(UIStyle.FONT_BOLD);
        lblUserInfo.setForeground(Color.WHITE);

        JButton btnChangePass = UIStyle.createButton("Change Password", UIStyle.COLOR_PRIMARY, Color.WHITE);
        JButton btnLogout = UIStyle.createButton("Logout", UIStyle.COLOR_DANGER, Color.WHITE);

        btnChangePass.addActionListener(e -> handleChangePassword());
        btnLogout.addActionListener(e -> handleLogout());

        userPanel.add(lblUserInfo);
        userPanel.add(btnChangePass);
        userPanel.add(btnLogout);

        headerPanel.add(lblLogo, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        rootPanel.add(headerPanel, BorderLayout.NORTH);

        // --- LEFT SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIStyle.COLOR_NAV_BG);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        JButton btnNavDash = createSidebarButton("Dashboard Home");
        JButton btnNavQuiz = createSidebarButton("Attempt Quiz");
        JButton btnNavLeaderboard = createSidebarButton("Leaderboard");
        JButton btnNavHistory = createSidebarButton("My History");
        JButton btnNavLogout = createSidebarButton("Logout Session");

        btnNavDash.addActionListener(e -> showCard("DASHBOARD"));
        btnNavQuiz.addActionListener(e -> showCard("QUIZ"));
        btnNavLeaderboard.addActionListener(e -> showCard("LEADERBOARD"));
        btnNavHistory.addActionListener(e -> showCard("HISTORY"));
        btnNavLogout.addActionListener(e -> handleLogout());

        sidebar.add(btnNavDash);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnNavQuiz);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnNavLeaderboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnNavHistory);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnNavLogout);

        rootPanel.add(sidebar, BorderLayout.WEST);

        // --- CENTER CARDS ---
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(UIStyle.COLOR_BG);

        // Card 1: Dashboard Welcome Panel
        JPanel dashboardCard = createStudentWelcomePanel();

        // Card 2: Attempt Quiz
        attemptQuizPanel = new AttemptQuizPanel(currentUser);

        // Card 3: Leaderboard
        leaderboardPanel = new LeaderboardPanel();

        // Card 4: History
        viewResultsPanel = new ViewResultsPanel(currentUser);

        mainContentPanel.add(dashboardCard, "DASHBOARD");
        mainContentPanel.add(attemptQuizPanel, "QUIZ");
        mainContentPanel.add(leaderboardPanel, "LEADERBOARD");
        mainContentPanel.add(viewResultsPanel, "HISTORY");

        rootPanel.add(mainContentPanel, BorderLayout.CENTER);

        setContentPane(rootPanel);
    }

    private JButton createSidebarButton(String text) {
        Color bg = new Color(30, 41, 59);
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(new Color(15, 23, 42));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(51, 65, 85));
                } else {
                    g2.setColor(bg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(UIStyle.FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return btn;
    }

    private JPanel createStudentWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(UIStyle.COLOR_BG);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel welcomeCard = UIStyle.createCardPanel();
        welcomeCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel lblHeading = new JLabel("Online Quiz Contest", SwingConstants.CENTER);
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblHeading.setForeground(UIStyle.COLOR_PRIMARY);

        JLabel lblSub = new JLabel("Welcome " + currentUser.getFullName() + "! Select an option to get started.", SwingConstants.CENTER);
        lblSub.setFont(UIStyle.FONT_SUBTITLE);
        lblSub.setForeground(UIStyle.COLOR_TEXT_DARK);

        gbc.gridy = 0; welcomeCard.add(lblHeading, gbc);
        gbc.gridy = 1; welcomeCard.add(lblSub, gbc);

        // Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        actionPanel.setBackground(UIStyle.COLOR_CARD_BG);

        JButton btnStartQuiz = UIStyle.createButton("Start Quiz Attempt", UIStyle.COLOR_PRIMARY, Color.WHITE);
        JButton btnViewRank = UIStyle.createButton("View Leaderboard", UIStyle.COLOR_SUCCESS, Color.WHITE);
        JButton btnViewHistory = UIStyle.createButton("My Performance History", UIStyle.COLOR_ACCENT, Color.WHITE);

        btnStartQuiz.setPreferredSize(new Dimension(200, 45));
        btnViewRank.setPreferredSize(new Dimension(200, 45));
        btnViewHistory.setPreferredSize(new Dimension(200, 45));

        btnStartQuiz.addActionListener(e -> showCard("QUIZ"));
        btnViewRank.addActionListener(e -> showCard("LEADERBOARD"));
        btnViewHistory.addActionListener(e -> showCard("HISTORY"));

        actionPanel.add(btnStartQuiz);
        actionPanel.add(btnViewRank);
        actionPanel.add(btnViewHistory);

        gbc.gridy = 2; gbc.insets = new Insets(25, 10, 10, 10);
        welcomeCard.add(actionPanel, gbc);

        panel.add(welcomeCard, BorderLayout.CENTER);
        return panel;
    }

    private void showCard(String cardName) {
        if ("QUIZ".equals(cardName)) {
            attemptQuizPanel.loadQuiz();
        } else if ("LEADERBOARD".equals(cardName)) {
            leaderboardPanel.loadLeaderboardData();
        } else if ("HISTORY".equals(cardName)) {
            viewResultsPanel.loadResultHistory();
        }
        cardLayout.show(mainContentPanel, cardName);
    }

    public AttemptQuizPanel getAttemptQuizPanel() {
        return attemptQuizPanel;
    }

    private void handleChangePassword() {
        new ChangePasswordDialog(this, currentUser).setVisible(true);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
