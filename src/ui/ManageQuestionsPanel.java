package ui;

import dao.QuestionDAO;
import model.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageQuestionsPanel extends JPanel {

    private final QuestionDAO questionDAO;
    private JTable tblQuestions;
    private DefaultTableModel tableModel;

    // Form Components
    private JTextField txtQuestionId;
    private JTextArea txtQuestionText;
    private JTextField txtOptionA;
    private JTextField txtOptionB;
    private JTextField txtOptionC;
    private JTextField txtOptionD;
    private JComboBox<String> cmbCorrectOption;
    private JTextField txtSearch;

    public ManageQuestionsPanel() {
        this.questionDAO = new QuestionDAO();
        setLayout(new BorderLayout(15, 15));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        initComponents();
        loadQuestionTableData();
    }

    private void initComponents() {
        // --- TOP SEARCH & TITLE BAR ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(UIStyle.COLOR_BG);

        JLabel lblTitle = new JLabel("Question Management (CRUD)");
        lblTitle.setFont(UIStyle.FONT_TITLE);
        lblTitle.setForeground(UIStyle.COLOR_TEXT_DARK);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setBackground(UIStyle.COLOR_BG);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(UIStyle.FONT_BOLD);
        txtSearch = UIStyle.createTextField();
        txtSearch.setPreferredSize(new Dimension(200, 32));

        JButton btnSearch = UIStyle.createButton("Search", UIStyle.COLOR_PRIMARY, Color.WHITE);
        JButton btnResetSearch = UIStyle.createButton("Show All", new Color(71, 85, 105), Color.WHITE);

        btnSearch.addActionListener(e -> handleSearch());
        btnResetSearch.addActionListener(e -> {
            txtSearch.setText("");
            loadQuestionTableData();
        });

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnResetSearch);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // --- MAIN CONTENT SPLIT (Table on Left/Top, Form on Right/Bottom) ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(260);

        // --- TOP: JTABLE ---
        JPanel tableCard = UIStyle.createCardPanel();
        tableCard.setLayout(new BorderLayout());

        String[] columns = {"ID", "Question Text", "Option A", "Option B", "Option C", "Option D", "Correct"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Non editable table
            }
        };

        tblQuestions = new JTable(tableModel);
        UIStyle.styleTable(tblQuestions);

        tblQuestions.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblQuestions.getColumnModel().getColumn(1).setPreferredWidth(300);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblQuestions.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblQuestions.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        // Listen for table row selection to populate form
        tblQuestions.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblQuestions.getSelectedRow() != -1) {
                populateFormFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblQuestions);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        splitPane.setTopComponent(tableCard);

        // --- BOTTOM: FORM & CRUD BUTTONS ---
        JPanel formCard = UIStyle.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));

        JLabel lblFormTitle = new JLabel("Question Form Details");
        lblFormTitle.setFont(UIStyle.FONT_SUBTITLE);
        formCard.add(lblFormTitle, BorderLayout.NORTH);

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(UIStyle.COLOR_CARD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Question ID (Read-only)
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblQId = new JLabel("Question ID:");
        lblQId.setFont(UIStyle.FONT_BOLD);
        formGrid.add(lblQId, gbc);

        txtQuestionId = UIStyle.createTextField();
        txtQuestionId.setEditable(false);
        txtQuestionId.setBackground(new Color(241, 245, 249));
        txtQuestionId.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 3;
        formGrid.add(txtQuestionId, gbc);

        // Question Text
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblQText = new JLabel("Question Text:");
        lblQText.setFont(UIStyle.FONT_BOLD);
        formGrid.add(lblQText, gbc);

        txtQuestionText = new JTextArea(2, 40);
        txtQuestionText.setFont(UIStyle.FONT_BODY);
        txtQuestionText.setLineWrap(true);
        txtQuestionText.setWrapStyleWord(true);
        JScrollPane qTextScroll = new JScrollPane(txtQuestionText);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3;
        formGrid.add(qTextScroll, gbc);

        // Option A & B
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        formGrid.add(new JLabel("Option A:"), gbc);
        txtOptionA = UIStyle.createTextField();
        gbc.gridx = 1; gbc.gridy = 2;
        formGrid.add(txtOptionA, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        formGrid.add(new JLabel("Option B:"), gbc);
        txtOptionB = UIStyle.createTextField();
        gbc.gridx = 3; gbc.gridy = 2;
        formGrid.add(txtOptionB, gbc);

        // Option C & D
        gbc.gridx = 0; gbc.gridy = 3;
        formGrid.add(new JLabel("Option C:"), gbc);
        txtOptionC = UIStyle.createTextField();
        gbc.gridx = 1; gbc.gridy = 3;
        formGrid.add(txtOptionC, gbc);

        gbc.gridx = 2; gbc.gridy = 3;
        formGrid.add(new JLabel("Option D:"), gbc);
        txtOptionD = UIStyle.createTextField();
        gbc.gridx = 3; gbc.gridy = 3;
        formGrid.add(txtOptionD, gbc);

        // Correct Option
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblCorr = new JLabel("Correct Option:");
        lblCorr.setFont(UIStyle.FONT_BOLD);
        formGrid.add(lblCorr, gbc);

        cmbCorrectOption = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        cmbCorrectOption.setFont(UIStyle.FONT_BOLD);
        cmbCorrectOption.setPreferredSize(new Dimension(80, 30));
        gbc.gridx = 1; gbc.gridy = 4;
        formGrid.add(cmbCorrectOption, gbc);

        formCard.add(formGrid, BorderLayout.CENTER);

        // --- BUTTON BAR ---
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnBar.setBackground(UIStyle.COLOR_CARD_BG);

        JButton btnAdd = UIStyle.createButton("Add Question", UIStyle.COLOR_SUCCESS, Color.WHITE);
        JButton btnUpdate = UIStyle.createButton("Update Question", UIStyle.COLOR_PRIMARY, Color.WHITE);
        JButton btnDelete = UIStyle.createButton("Delete Question", UIStyle.COLOR_DANGER, Color.WHITE);
        JButton btnClear = UIStyle.createButton("Clear Form", new Color(71, 85, 105), Color.WHITE);

        btnAdd.addActionListener(e -> handleAddQuestion());
        btnUpdate.addActionListener(e -> handleUpdateQuestion());
        btnDelete.addActionListener(e -> handleDeleteQuestion());
        btnClear.addActionListener(e -> clearForm());

        btnBar.add(btnAdd);
        btnBar.add(btnUpdate);
        btnBar.add(btnDelete);
        btnBar.add(btnClear);

        formCard.add(btnBar, BorderLayout.SOUTH);

        splitPane.setBottomComponent(formCard);
        add(splitPane, BorderLayout.CENTER);
    }

    public void loadQuestionTableData() {
        tableModel.setRowCount(0);
        List<Question> questions = questionDAO.getAllQuestions();
        for (Question q : questions) {
            Object[] row = {
                    q.getQuestionId(),
                    q.getQuestionText(),
                    q.getOptionA(),
                    q.getOptionB(),
                    q.getOptionC(),
                    q.getOptionD(),
                    q.getCorrectOption()
            };
            tableModel.addRow(row);
        }
    }

    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadQuestionTableData();
            return;
        }

        tableModel.setRowCount(0);
        List<Question> searchResults = questionDAO.searchQuestions(keyword);
        for (Question q : searchResults) {
            Object[] row = {
                    q.getQuestionId(),
                    q.getQuestionText(),
                    q.getOptionA(),
                    q.getOptionB(),
                    q.getOptionC(),
                    q.getOptionD(),
                    q.getCorrectOption()
            };
            tableModel.addRow(row);
        }

        if (searchResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No matching questions found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = tblQuestions.getSelectedRow();
        if (selectedRow != -1) {
            txtQuestionId.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtQuestionText.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtOptionA.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtOptionB.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtOptionC.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtOptionD.setText(tableModel.getValueAt(selectedRow, 5).toString());
            cmbCorrectOption.setSelectedItem(tableModel.getValueAt(selectedRow, 6).toString());
        }
    }

    private void handleAddQuestion() {
        if (!validateForm()) return;

        Question q = new Question(
                0,
                txtQuestionText.getText().trim(),
                txtOptionA.getText().trim(),
                txtOptionB.getText().trim(),
                txtOptionC.getText().trim(),
                txtOptionD.getText().trim(),
                cmbCorrectOption.getSelectedItem().toString()
        );

        if (questionDAO.addQuestion(q)) {
            JOptionPane.showMessageDialog(this, "Question added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadQuestionTableData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add question. Please check database connection.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateQuestion() {
        if (txtQuestionId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a question from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateForm()) return;

        int id = Integer.parseInt(txtQuestionId.getText().trim());
        Question q = new Question(
                id,
                txtQuestionText.getText().trim(),
                txtOptionA.getText().trim(),
                txtOptionB.getText().trim(),
                txtOptionC.getText().trim(),
                txtOptionD.getText().trim(),
                cmbCorrectOption.getSelectedItem().toString()
        );

        if (questionDAO.updateQuestion(q)) {
            JOptionPane.showMessageDialog(this, "Question updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadQuestionTableData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update question.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteQuestion() {
        if (txtQuestionId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a question from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(txtQuestionId.getText().trim());
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Question ID #" + id + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (questionDAO.deleteQuestion(id)) {
                JOptionPane.showMessageDialog(this, "Question deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadQuestionTableData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete question.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateForm() {
        if (txtQuestionText.getText().trim().isEmpty() ||
            txtOptionA.getText().trim().isEmpty() ||
            txtOptionB.getText().trim().isEmpty() ||
            txtOptionC.getText().trim().isEmpty() ||
            txtOptionD.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill in all question and option fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtQuestionId.setText("");
        txtQuestionText.setText("");
        txtOptionA.setText("");
        txtOptionB.setText("");
        txtOptionC.setText("");
        txtOptionD.setText("");
        cmbCorrectOption.setSelectedIndex(0);
        tblQuestions.clearSelection();
    }
}
