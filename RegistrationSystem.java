import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegistrationSystem extends JFrame {
    private JComboBox<String> userTypeCombo;
    private JPanel inputPanel;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/warehouse_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234"; // استبدل بكلمة المرور الخاصة بك
    private JFrame mainPage;

    public RegistrationSystem(JFrame mainPage) {
        this.mainPage = mainPage;
        setTitle("Create New Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Window title
        JLabel titleLabel = new JLabel("Create New Account");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // User type selection dropdown
        String[] userTypes = {"Admin", "Employee", "Visitor", "Supplier", "Customer"};
        userTypeCombo = new JComboBox<>(userTypes);
        userTypeCombo.setMaximumSize(new Dimension(300, 30));
        userTypeCombo.addActionListener(e -> updateInputFields());

        // Dynamic input panel
        inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.insets = new Insets(5, 5, 5, 5);
        inputGbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("User Type:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(userTypeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(inputPanel, gbc);

        add(mainPanel);
        updateInputFields();
    }

    private void updateInputFields() {
        inputPanel.removeAll();
        String userType = (String) userTypeCombo.getSelectedItem();

        // Common fields
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField addressField = new JTextField(20);

        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.insets = new Insets(5, 5, 5, 5);
        inputGbc.anchor = GridBagConstraints.WEST;

        // Add fields based on user type
        inputGbc.gridx = 0;
        inputGbc.gridy = 0;
        inputPanel.add(new JLabel("Username:"), inputGbc);
        inputGbc.gridx = 1;
        inputPanel.add(usernameField, inputGbc);

        inputGbc.gridx = 0;
        inputGbc.gridy = 1;
        inputPanel.add(new JLabel("Password:"), inputGbc);
        inputGbc.gridx = 1;
        inputPanel.add(passwordField, inputGbc);

        if (userType.equals("Visitor")) {
            JTextField identityField = new JTextField(20);
            JTextField purposeField = new JTextField(20);
            JTextField dateField = new JTextField(20);

            inputGbc.gridx = 0;
            inputGbc.gridy = 2;
            inputPanel.add(new JLabel("Identity Number:"), inputGbc);
            inputGbc.gridx = 1;
            inputPanel.add(identityField, inputGbc);

            inputGbc.gridx = 0;
            inputGbc.gridy = 3;
            inputPanel.add(new JLabel("Purpose of Visit:"), inputGbc);
            inputGbc.gridx = 1;
            inputPanel.add(purposeField, inputGbc);

            inputGbc.gridx = 0;
            inputGbc.gridy = 4;
            inputPanel.add(new JLabel("Visit Date:"), inputGbc);
            inputGbc.gridx = 1;
            inputPanel.add(dateField, inputGbc);
        } else {
            inputGbc.gridx = 0;
            inputGbc.gridy = 2;
            inputPanel.add(new JLabel("Email:"), inputGbc);
            inputGbc.gridx = 1;
            inputPanel.add(emailField, inputGbc);

            inputGbc.gridx = 0;
            inputGbc.gridy = 3;
            inputPanel.add(new JLabel("Phone Number:"), inputGbc);
            inputGbc.gridx = 1;
            inputPanel.add(phoneField, inputGbc);

            inputGbc.gridx = 0;
            inputGbc.gridy = 4;
            inputPanel.add(new JLabel("Address:"), inputGbc);
            inputGbc.gridx = 1;
            inputPanel.add(addressField, inputGbc);
        }

        JButton registerButton = new JButton("Submit");
        inputGbc.gridx = 0;
        inputGbc.gridy = 5;
        inputGbc.gridwidth = 1;
        inputGbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(registerButton, inputGbc);

        registerButton.addActionListener(e -> register(userType, usernameField.getText(),
                new String(passwordField.getPassword()), emailField.getText(),
                phoneField.getText(), addressField.getText()));

        inputPanel.revalidate();
        inputPanel.repaint();

        JButton backButton = new JButton("Back to Login");
        inputGbc.gridx = 1;
        inputGbc.gridy = 5;
        inputGbc.gridwidth = 1;
        inputGbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(backButton, inputGbc);
        backButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new LoginSystem(mainPage).setVisible(true);
                this.dispose();
            });
        });
    }

    private void register(String userType, String username, String password,
                          String email, String phone, String address) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String tableName = getTableName(userType);
            String query = "INSERT INTO " + tableName +
                           " (username, password, email, phone, address) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Account created successfully");
                mainPage.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Account creation failed");
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error");
        }
    }

    private String getTableName(String userType) {
        switch (userType) {
            case "Admin": return "admins";
            case "Employee": return "employees";
            case "Visitor": return "visitors";
            case "Supplier": return "suppliers";
            case "Customer": return "customers";
            default: return "";
        }
    }
}


