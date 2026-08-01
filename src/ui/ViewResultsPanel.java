package ui;

import dao.ResultDAO;
import model.Result;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ViewResultsPanel extends JPanel {

    private final User currentUser;
    private final ResultDAO resultDAO;
    private JTable tblResults;
    private DefaultTableModel tableModel;

    public ViewResultsPanel(User currentUser) {
        this.currentUser = currentUser;
        this.resultDAO = new ResultDAO();
        setLayout(new BorderLayout(15, 15));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initComponents();
        loadResultHistory();
    }

    private void initComponents() {
        // Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIStyle.COLOR_BG);

        JLabel lblTitle = new JLabel("My Quiz Performance History");
        lblTitle.setFont(UIStyle.FONT_TITLE);
        lblTitle.setForeground(UIStyle.COLOR_TEXT_DARK);

        JButton btnRefresh = UIStyle.createButton("Refresh History", UIStyle.COLOR_PRIMARY, Color.WHITE);
        btnRefresh.addActionListener(e -> loadResultHistory());

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(btnRefresh, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Center Table Card
        JPanel cardPanel = UIStyle.createCardPanel();
        cardPanel.setLayout(new BorderLayout(10, 10));

        String[] columnNames = {"Attempt #", "Quiz Date", "Correct Answers", "Total Questions", "Percentage"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblResults = new JTable(tableModel);
        UIStyle.styleTable(tblResults);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnNames.length; i++) {
            tblResults.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tblResults);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        cardPanel.add(scrollPane, BorderLayout.CENTER);
        add(cardPanel, BorderLayout.CENTER);
    }

    public void loadResultHistory() {
        tableModel.setRowCount(0);
        List<Result> results = resultDAO.getResultsByUserId(currentUser.getUserId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        int count = 1;
        for (Result r : results) {
            Object[] row = {
                    "Attempt " + count++,
                    r.getQuizDate() != null ? sdf.format(r.getQuizDate()) : "N/A",
                    r.getScore(),
                    r.getTotalQuestions(),
                    String.format("%.1f%%", r.getPercentage())
            };
            tableModel.addRow(row);
        }
    }
}
