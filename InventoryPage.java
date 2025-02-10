import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class InventoryPage extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/warehouse_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    private JTextField itemIdField;
    private JTextField itemNameField;
    private JTextField quantityField;
    private JTextField locationField;
    private JTextField expiryDateField;

    public InventoryPage(JFrame parentFrame) {
        initializeFrame();
        setupUIComponents();
        loadInventoryData();
    }

    private void initializeFrame() {
        setTitle("Inventory Page");
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void setupUIComponents() {
        add(createHeader(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setBackground(new Color(224, 238, 238));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Inventory Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(50, 50, 50));

        header.add(title);
        return header;
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(new Color(240, 248, 255));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 15, 10, 15);

        itemIdField = createInputField("");
        itemNameField = createInputField("");
        quantityField = createInputField("");
        locationField = createInputField("");
        expiryDateField = createInputField("");

        addFormField(content, gbc, 0, "Item ID:", itemIdField);
        addFormField(content, gbc, 1, "Item Name:", itemNameField);
        addFormField(content, gbc, 2, "Quantity:", quantityField);
        addFormField(content, gbc, 3, "Location:", locationField);
        addFormField(content, gbc, 4, "Expiry Date:", expiryDateField);

        return content;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbl = createLabel(label);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }

    private JTextField createInputField(String value) {
        JTextField field = new JTextField(value, 20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(Color.WHITE);
        field.setEditable(true);
        return field;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        JButton backButton = createActionButton("back", "back inventory details");
        JButton viewButton = createActionButton("View", "View inventory details");
        JButton addButton = createActionButton("Add", "Add new item");

        
        backButton.addActionListener(e -> {
            this.dispose();
            backButton.setVisible(true);
        });

        
        
        viewButton.addActionListener(e -> {
            Inventoryview viewPage = new Inventoryview(this);
            viewPage.setVisible(true);
            this.setVisible(false);
        });

        buttonPanel.add(backButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(addButton);

        return buttonPanel;
    }

    private JButton createActionButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(211, 211, 211));
        button.setFocusPainted(false);
        button.setToolTipText(tooltip);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(190, 190, 190));
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(211, 211, 211));
            }
        });

        return button;
    }

    private void loadInventoryData() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT item_id, item_name, quantity, location, expiry_date FROM items LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                itemIdField.setText(rs.getString("item_id"));
                itemNameField.setText(rs.getString("item_name"));
                quantityField.setText(rs.getString("quantity"));
                locationField.setText(rs.getString("location"));
                expiryDateField.setText(rs.getString("expiry_date"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error");
        }
    }
}
