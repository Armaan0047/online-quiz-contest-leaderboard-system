package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChangePasswordDialog extends JDialog {

    private final User currentUser;
    private final UserDAO userDAO;

    private JPasswordField txtOldPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;

    public ChangePasswordDialog(Frame parent, User user) {
        super(parent, "Change Password", true);
        this.currentUser = user;
        this.userDAO = new UserDAO();

        initComponents();
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIStyle.COLOR_CARD_BG);
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Change Password");
        lblTitle.setFont(UIStyle.COLOR_TEXT_DARK.equals(Color.BLACK) ? UIStyle.FONT_TITLE : UIStyle.FONT_SUBTITLE);
        lblTitle.setForeground(UIStyle.COLOR_TEXT_DARK);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBackground(UIStyle.COLOR_CARD_BG);

        JLabel lblOld = new JLabel("Current Password:");
        lblOld.setFont(UIStyle.FONT_BOLD);
        txtOldPassword = UIStyle.createPasswordField();

        JLabel lblNew = new JLabel("New Password:");
        lblNew.setFont(UIStyle.FONT_BOLD);
        txtNewPassword = UIStyle.createPasswordField();

        JLabel lblConfirm = new JLabel("Confirm New Password:");
        lblConfirm.setFont(UIStyle.FONT_BOLD);
        txtConfirmPassword = UIStyle.createPasswordField();

        formPanel.add(lblOld);
        formPanel.add(txtOldPassword);
        formPanel.add(lblNew);
        formPanel.add(txtNewPassword);
        formPanel.add(lblConfirm);
        formPanel.add(txtConfirmPassword);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(UIStyle.COLOR_CARD_BG);

        JButton btnCancel = UIStyle.createButton("Cancel", new Color(71, 85, 105), Color.WHITE);
        JButton btnSave = UIStyle.createButton("Update Password", UIStyle.COLOR_PRIMARY, Color.WHITE);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> handleChangePassword());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void handleChangePassword() {
        String oldPass = new String(txtOldPassword.getPassword()).trim();
        String newPass = new String(txtNewPassword.getPassword()).trim();
        String confirmPass = new String(txtConfirmPassword.getPassword()).trim();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All password fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "New password and confirm password do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPass.length() < 4) {
            JOptionPane.showMessageDialog(this, "New password should be at least 4 characters long.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = userDAO.changePassword(currentUser.getUserId(), oldPass, newPass);
        if (success) {
            JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            currentUser.setPassword(newPass);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect current password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
