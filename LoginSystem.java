import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginSystem extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/warehouse_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";
    private JFrame mainPage;

    public LoginSystem(JFrame mainPage) {
        this.mainPage = mainPage;
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Window title
        JLabel titleLabel = new JLabel("Login");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Input fields
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        // Adding elements to the window
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, gbc);

        JButton registerButton = new JButton("Sign Up");
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(registerButton, gbc);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new RegistrationSystem(mainPage).setVisible(true);
                this.setVisible(false);
            });
        });

        add(mainPanel);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // Check in all tables
            String[] tables = {"admins", "employees", "visitors", "suppliers", "customers"};
            boolean found = false;

            for (String table : tables) {
                String query = "SELECT * FROM " + table + " WHERE username = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    found = true;
                    JOptionPane.showMessageDialog(this, "Login successful");
                    mainPage.setVisible(true);
                    this.dispose();
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginSystem(new MainPage()).setVisible(true));
    }
}



