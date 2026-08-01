package ui;

import dao.QuestionDAO;
import dao.ResultDAO;
import model.Question;
import model.Result;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttemptQuizPanel extends JPanel {

    private final User currentUser;
    private final QuestionDAO questionDAO;
    private final ResultDAO resultDAO;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;

    // User Selected Answers (Map of questionId -> "A"/"B"/"C"/"D")
    private final Map<Integer, String> userAnswers;

    // UI Components
    private JLabel lblProgress;
    private JTextArea txtQuestionText;
    private JRadioButton rbOptionA;
    private JRadioButton rbOptionB;
    private JRadioButton rbOptionC;
    private JRadioButton rbOptionD;
    private ButtonGroup optionGroup;

    private JButton btnPrev;
    private JButton btnNext;
    private JButton btnSubmit;

    private JPanel quizCard;
    private JPanel emptyStatePanel;

    public AttemptQuizPanel(User currentUser) {
        this.currentUser = currentUser;
        this.questionDAO = new QuestionDAO();
        this.resultDAO = new ResultDAO();
        this.userAnswers = new HashMap<>();

        setLayout(new BorderLayout(15, 15));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initComponents();
        loadQuiz();
    }

    private void initComponents() {
        // Top Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIStyle.COLOR_BG);

        JLabel lblTitle = new JLabel("Online Quiz Contest");
        lblTitle.setFont(UIStyle.FONT_TITLE);
        lblTitle.setForeground(UIStyle.COLOR_TEXT_DARK);

        lblProgress = new JLabel("Question 0 of 0");
        lblProgress.setFont(UIStyle.FONT_BOLD);
        lblProgress.setForeground(UIStyle.COLOR_TEXT_MUTED);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(lblProgress, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Empty State Panel (if no questions exist in DB)
        emptyStatePanel = UIStyle.createCardPanel();
        emptyStatePanel.setLayout(new GridBagLayout());
        JLabel lblEmpty = new JLabel("No questions available in the database. Please contact the administrator.");
        lblEmpty.setFont(UIStyle.FONT_SUBTITLE);
        lblEmpty.setForeground(UIStyle.COLOR_TEXT_MUTED);
        emptyStatePanel.add(lblEmpty);

        // Quiz Question Card
        quizCard = UIStyle.createCardPanel();
        quizCard.setLayout(new BorderLayout(15, 15));

        txtQuestionText = new JTextArea(3, 40);
        txtQuestionText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtQuestionText.setEditable(false);
        txtQuestionText.setLineWrap(true);
        txtQuestionText.setWrapStyleWord(true);
        txtQuestionText.setBackground(UIStyle.COLOR_CARD_BG);
        txtQuestionText.setBorder(new EmptyBorder(10, 10, 10, 10));

        quizCard.add(txtQuestionText, BorderLayout.NORTH);

        // Radio Options Panel
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 12));
        optionsPanel.setBackground(UIStyle.COLOR_CARD_BG);
        optionsPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        rbOptionA = new JRadioButton("Option A");
        rbOptionB = new JRadioButton("Option B");
        rbOptionC = new JRadioButton("Option C");
        rbOptionD = new JRadioButton("Option D");

        Font optionFont = new Font("Segoe UI", Font.PLAIN, 15);
        rbOptionA.setFont(optionFont);
        rbOptionB.setFont(optionFont);
        rbOptionC.setFont(optionFont);
        rbOptionD.setFont(optionFont);

        rbOptionA.setBackground(UIStyle.COLOR_CARD_BG);
        rbOptionB.setBackground(UIStyle.COLOR_CARD_BG);
        rbOptionC.setBackground(UIStyle.COLOR_CARD_BG);
        rbOptionD.setBackground(UIStyle.COLOR_CARD_BG);

        optionGroup = new ButtonGroup();
        optionGroup.add(rbOptionA);
        optionGroup.add(rbOptionB);
        optionGroup.add(rbOptionC);
        optionGroup.add(rbOptionD);

        optionsPanel.add(rbOptionA);
        optionsPanel.add(rbOptionB);
        optionsPanel.add(rbOptionC);
        optionsPanel.add(rbOptionD);

        quizCard.add(optionsPanel, BorderLayout.CENTER);

        // Action Buttons Bar
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btnBar.setBackground(UIStyle.COLOR_CARD_BG);

        btnPrev = UIStyle.createButton("Previous", new Color(71, 85, 105), Color.WHITE);
        btnNext = UIStyle.createButton("Next", UIStyle.COLOR_PRIMARY, Color.WHITE);
        btnSubmit = UIStyle.createButton("Submit Quiz", UIStyle.COLOR_SUCCESS, Color.WHITE);

        btnPrev.addActionListener(e -> navigateQuestion(-1));
        btnNext.addActionListener(e -> navigateQuestion(1));
        btnSubmit.addActionListener(e -> handleSubmitQuiz());

        btnBar.add(btnPrev);
        btnBar.add(btnNext);
        btnBar.add(btnSubmit);

        quizCard.add(btnBar, BorderLayout.SOUTH);

        add(quizCard, BorderLayout.CENTER);
    }

    public void loadQuiz() {
        userAnswers.clear();
        questionList = questionDAO.getAllQuestions();

        if (questionList.isEmpty()) {
            remove(quizCard);
            add(emptyStatePanel, BorderLayout.CENTER);
            lblProgress.setText("Question 0 of 0");
        } else {
            remove(emptyStatePanel);
            add(quizCard, BorderLayout.CENTER);
            currentQuestionIndex = 0;
            displayQuestion(currentQuestionIndex);
        }
        revalidate();
        repaint();
    }

    private void displayQuestion(int index) {
        if (index < 0 || index >= questionList.size()) return;

        this.currentQuestionIndex = index;

        Question q = questionList.get(index);
        txtQuestionText.setText((index + 1) + ". " + q.getQuestionText());

        rbOptionA.setText("A) " + q.getOptionA());
        rbOptionB.setText("B) " + q.getOptionB());
        rbOptionC.setText("C) " + q.getOptionC());
        rbOptionD.setText("D) " + q.getOptionD());

        // Clear selection before restoring saved answer
        optionGroup.clearSelection();

        // Restore user choice if already answered
        String savedAnswer = userAnswers.get(q.getQuestionId());
        if ("A".equalsIgnoreCase(savedAnswer)) rbOptionA.setSelected(true);
        else if ("B".equalsIgnoreCase(savedAnswer)) rbOptionB.setSelected(true);
        else if ("C".equalsIgnoreCase(savedAnswer)) rbOptionC.setSelected(true);
        else if ("D".equalsIgnoreCase(savedAnswer)) rbOptionD.setSelected(true);

        lblProgress.setText("Question " + (index + 1) + " of " + questionList.size());

        btnPrev.setEnabled(index > 0);
        btnNext.setEnabled(index < questionList.size() - 1);
    }

    private void saveCurrentAnswer() {
        if (questionList == null || questionList.isEmpty() || currentQuestionIndex < 0 || currentQuestionIndex >= questionList.size()) {
            return;
        }
        Question q = questionList.get(currentQuestionIndex);

        if (rbOptionA.isSelected()) userAnswers.put(q.getQuestionId(), "A");
        else if (rbOptionB.isSelected()) userAnswers.put(q.getQuestionId(), "B");
        else if (rbOptionC.isSelected()) userAnswers.put(q.getQuestionId(), "C");
        else if (rbOptionD.isSelected()) userAnswers.put(q.getQuestionId(), "D");
        else userAnswers.remove(q.getQuestionId());
    }

    private void navigateQuestion(int direction) {
        saveCurrentAnswer();
        currentQuestionIndex += direction;
        displayQuestion(currentQuestionIndex);
    }

    private void handleSubmitQuiz() {
        saveCurrentAnswer();

        int totalQuestions = questionList.size();
        int answeredCount = userAnswers.size();

        if (answeredCount < totalQuestions) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "You have answered " + answeredCount + " out of " + totalQuestions + " questions.\nAre you sure you want to submit?",
                    "Submit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // Calculate score
        int score = 0;
        for (Question q : questionList) {
            String selectedOption = userAnswers.get(q.getQuestionId());
            if (selectedOption != null && selectedOption.equalsIgnoreCase(q.getCorrectOption())) {
                score++;
            }
        }

        double percentage = ((double) score / totalQuestions) * 100.0;

        // Save result to MySQL database
        Result result = new Result(0, currentUser.getUserId(), currentUser.getUsername(), currentUser.getFullName(), score, totalQuestions, percentage, null);
        boolean saved = resultDAO.saveResult(result);

        if (saved) {
            // Requirement #3: Display straightforward information (Score, Total Qs, Percentage)
            String message = String.format(
                    "Quiz Submitted Successfully!\n\n" +
                    "Score / Correct Answers: %d\n" +
                    "Total Questions: %d\n" +
                    "Percentage: %.1f%%\n\n" +
                    "Your score has been saved to the Leaderboard.",
                    score, totalQuestions, percentage
            );

            JOptionPane.showMessageDialog(this, message, "Quiz Result", JOptionPane.INFORMATION_MESSAGE);
            loadQuiz(); // Reset quiz
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save quiz results to database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JRadioButton getRbOptionA() { return rbOptionA; }
    public JRadioButton getRbOptionB() { return rbOptionB; }
    public JRadioButton getRbOptionC() { return rbOptionC; }
    public JRadioButton getRbOptionD() { return rbOptionD; }
}
