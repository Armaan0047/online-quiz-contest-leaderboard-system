package ui;

import dao.ResultDAO;
import model.Result;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class LeaderboardPanel extends JPanel {

    private final ResultDAO resultDAO;
    private JTable tblLeaderboard;
    private DefaultTableModel tableModel;
    private JLabel lblTotalAttempts;

    public LeaderboardPanel() {
        this.resultDAO = new ResultDAO();
        setLayout(new BorderLayout(15, 15));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initComponents();
        loadLeaderboardData();
    }

    private void initComponents() {
        // Top Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIStyle.COLOR_BG);

        JLabel lblTitle = new JLabel("Quiz Leaderboard & Standings");
        lblTitle.setFont(UIStyle.FONT_TITLE);
        lblTitle.setForeground(UIStyle.COLOR_TEXT_DARK);

        lblTotalAttempts = new JLabel("Total Attempts: 0");
        lblTotalAttempts.setFont(UIStyle.FONT_BOLD);
        lblTotalAttempts.setForeground(UIStyle.COLOR_TEXT_MUTED);

        JButton btnRefresh = UIStyle.createButton("Refresh Standings", UIStyle.COLOR_PRIMARY, Color.WHITE);
        btnRefresh.addActionListener(e -> loadLeaderboardData());

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightHeader.setBackground(UIStyle.COLOR_BG);
        rightHeader.add(lblTotalAttempts);
        rightHeader.add(btnRefresh);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(rightHeader, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Center Table Card
        JPanel cardPanel = UIStyle.createCardPanel();
        cardPanel.setLayout(new BorderLayout(10, 10));

        String[] columnNames = {"Rank", "Student Name", "Username", "Score", "Total Qs", "Percentage", "Quiz Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Non-editable JTable
            }
        };

        tblLeaderboard = new JTable(tableModel);
        UIStyle.styleTable(tblLeaderboard);

        // Center align Rank, Score, Percentage, Date columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblLeaderboard.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblLeaderboard.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblLeaderboard.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tblLeaderboard.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        tblLeaderboard.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        tblLeaderboard.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblLeaderboard.getColumnModel().getColumn(1).setPreferredWidth(180);

        JScrollPane scrollPane = new JScrollPane(tblLeaderboard);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        cardPanel.add(scrollPane, BorderLayout.CENTER);
        add(cardPanel, BorderLayout.CENTER);
    }

    public void loadLeaderboardData() {
        tableModel.setRowCount(0);
        List<Result> leaderboardList = resultDAO.getLeaderboard();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        int rank = 1;
        for (Result r : leaderboardList) {
            Object[] row = {
                    "#" + rank++,
                    r.getFullName(),
                    r.getUsername(),
                    r.getScore(),
                    r.getTotalQuestions(),
                    String.format("%.1f%%", r.getPercentage()),
                    r.getQuizDate() != null ? sdf.format(r.getQuizDate()) : "N/A"
            };
            tableModel.addRow(row);
        }

        lblTotalAttempts.setText("Total Attempts: " + leaderboardList.size());
    }
}
