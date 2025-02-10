import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("unused")
public class AboutSystem extends JFrame {

    // استخدام نفس الألوان والخطوط المستخدمة في MainPage
    private static final Color HEADER_BG_COLOR = new Color(240, 240, 250);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font CONTENT_FONT = new Font("Segoe UI", Font.PLAIN, 18);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 16);

    public AboutSystem() {
        initializeFrame();
        setupUIComponents();
    }

    private void initializeFrame() {
        setTitle("About University Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void setupUIComponents() {
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(HEADER_BG_COLOR);
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY));

        JLabel titleLabel = new JLabel("University Management System");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.BLACK);
        titlePanel.add(titleLabel);

        return titlePanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel descriptionLabel = new JLabel(
                "<html><div style='text-align: center;'>Welcome to the University Management System!<br><br>" +
                "This system is designed to manage university operations efficiently.<br>" +
                "It includes features like student management, course management,<br>" +
                "and more. For support, please contact our IT department.</div></html>",
                SwingConstants.CENTER);
        descriptionLabel.setFont(CONTENT_FONT);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(descriptionLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton okButton = createOkButton();
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(okButton);

        return contentPanel;
    }

    private JButton createOkButton() {
        JButton okButton = new JButton("OK");
        okButton.setFont(BUTTON_FONT);
        okButton.setBackground(new Color(0, 153, 255));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        okButton.addActionListener(e -> dispose());

        return okButton;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(HEADER_BG_COLOR);
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        footerPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY));

        JLabel footerLabel = new JLabel("© 2025 University Management System. All rights reserved.");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);

        return footerPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AboutSystem frame = new AboutSystem();
            frame.setVisible(true);
        });
    }
}