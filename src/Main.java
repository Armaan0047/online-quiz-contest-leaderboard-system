import ui.LoginFrame;
import ui.UIStyle;

import javax.swing.*;

/**
 * Main Entry Point for Online Quiz Contest & Leaderboard System.
 * PIET B.Tech II Year - III Semester Minor Project (NSP).
 */
public class Main {
    public static void main(String[] args) {
        // Set System Look & Feel for clean OS-native window decorations
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback to default cross-platform Swing L&F
            System.err.println("Could not set System Look and Feel: " + e.getMessage());
        }

        // Launch Login Frame on Swing Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
