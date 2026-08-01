package ui;

import dao.QuestionDAO;
import dao.ResultDAO;
import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminDashboardFrame extends JFrame {

    private final User currentUser;
    private final QuestionDAO questionDAO;
    private final UserDAO userDAO;
    private final ResultDAO resultDAO;

    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    private ManageQuestionsPanel manageQuestionsPanel;
    private LeaderboardPanel leaderboardPanel;

    private JLabel lblStatQuestions;
    private JLabel lblStatUsers;
    private JLabel lblStatQuizzes;

    public AdminDashboardFrame(User user) {
        this.currentUser = user;
        this.questionDAO = new QuestionDAO();
        this.userDAO = new UserDAO();
        this.resultDAO = new ResultDAO();

        setTitle("Online Quiz Contest & Leaderboard System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        initMenuBar();
        initComponents();
        loadDashboardStats();
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuNav = new JMenu("Navigation");
        JMenuItem miManageQ = new JMenuItem("Manage Questions");
        JMenuItem miLeaderboard = new JMenuItem("View Leaderboard");
        JMenuItem miExit = new JMenuItem("Exit Application");

        miManageQ.addActionListener(e -> showCard("QUESTIONS"));
        miLeaderboard.addActionListener(e -> showCard("LEADERBOARD"));
        miExit.addActionListener(e -> System.exit(0));

        menuNav.add(miManageQ);
        menuNav.add(miLeaderboard);
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

        JLabel lblLogo = new JLabel("Admin Control Panel");
        lblLogo.setFont(UIStyle.FONT_TITLE);
        lblLogo.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setBackground(UIStyle.COLOR_NAV_BG);

        JLabel lblUserInfo = new JLabel("Logged in as: " + currentUser.getFullName() + " (Admin)");
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

        JButton btnNavDash = createSidebarButton("Dashboard Overview");
        JButton btnNavQuestions = createSidebarButton("Manage Questions");
        JButton btnNavLeaderboard = createSidebarButton("Leaderboard & Results");
        JButton btnNavLogout = createSidebarButton("Logout Session");

        btnNavDash.addActionListener(e -> {
            loadDashboardStats();
            showCard("DASHBOARD");
        });
        btnNavQuestions.addActionListener(e -> showCard("QUESTIONS"));
        btnNavLeaderboard.addActionListener(e -> showCard("LEADERBOARD"));
        btnNavLogout.addActionListener(e -> handleLogout());

        sidebar.add(btnNavDash);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnNavQuestions);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnNavLeaderboard);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnNavLogout);

        rootPanel.add(sidebar, BorderLayout.WEST);

        // --- CENTER CARDS ---
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(UIStyle.COLOR_BG);

        // Card 1: Dashboard Overview
        JPanel dashboardCard = createDashboardOverviewPanel();

        // Card 2: Question Management
        manageQuestionsPanel = new ManageQuestionsPanel();

        // Card 3: Leaderboard
        leaderboardPanel = new LeaderboardPanel();

        mainContentPanel.add(dashboardCard, "DASHBOARD");
        mainContentPanel.add(manageQuestionsPanel, "QUESTIONS");
        mainContentPanel.add(leaderboardPanel, "LEADERBOARD");

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

    private JPanel createDashboardOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(UIStyle.COLOR_BG);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblTitle = new JLabel("System Overview");
        lblTitle.setFont(UIStyle.FONT_TITLE);
        lblTitle.setForeground(UIStyle.COLOR_TEXT_DARK);
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 20));
        statsGrid.setBackground(UIStyle.COLOR_BG);

        lblStatQuestions = new JLabel("0", SwingConstants.CENTER);
        lblStatUsers = new JLabel("0", SwingConstants.CENTER);
        lblStatQuizzes = new JLabel("0", SwingConstants.CENTER);

        statsGrid.add(createStatCard("Total Questions", lblStatQuestions, UIStyle.COLOR_PRIMARY));
        statsGrid.add(createStatCard("Registered Students", lblStatUsers, UIStyle.COLOR_SUCCESS));
        statsGrid.add(createStatCard("Total Quiz Attempts", lblStatQuizzes, UIStyle.COLOR_ACCENT));

        panel.add(statsGrid, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatCard(String title, JLabel lblValue, Color accentColor) {
        JPanel card = UIStyle.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));

        JLabel lblCardTitle = new JLabel(title, SwingConstants.CENTER);
        lblCardTitle.setFont(UIStyle.FONT_SUBTITLE);
        lblCardTitle.setForeground(UIStyle.COLOR_TEXT_MUTED);

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValue.setForeground(accentColor);

        card.add(lblCardTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    private void loadDashboardStats() {
        lblStatQuestions.setText(String.valueOf(questionDAO.getTotalQuestionsCount()));
        lblStatUsers.setText(String.valueOf(userDAO.getTotalUsersCount()));
        lblStatQuizzes.setText(String.valueOf(resultDAO.getTotalQuizzesTakenCount()));
    }

    private void showCard(String cardName) {
        if ("QUESTIONS".equals(cardName)) {
            manageQuestionsPanel.loadQuestionTableData();
        } else if ("LEADERBOARD".equals(cardName)) {
            leaderboardPanel.loadLeaderboardData();
        }
        cardLayout.show(mainContentPanel, cardName);
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
