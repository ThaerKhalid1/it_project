import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

@SuppressWarnings("unused")
public class Inventoryview extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/warehouse_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    private final JFrame parentFrame;

    public Inventoryview(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initializeFrame();
        setupUIComponents();
        loadInventoryData();
    }

    private void initializeFrame() {
        setTitle("Inventory Management System");
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void setupUIComponents() {
        add(createTablePanel(), BorderLayout.CENTER);
        add(createBackButtonPanel(), BorderLayout.SOUTH);
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"Item ID", "Item Name", "Quantity", "Location", "Expiry Date", "Delete", "Update"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 || columnIndex == 6 ? Boolean.class : super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6; // Only delete and update columns are editable
            }
        };

        JTable table = new JTable(model);
        customizeTableAppearance(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        return scrollPane;
    }

    private void customizeTableAppearance(JTable table) {
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(220, 240, 255));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(50, 50, 50));
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                c.setForeground(Color.BLACK);
                return c;
            }
        });
    }

    private JPanel createBackButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(211, 211, 211));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);

        backButton.addActionListener(e -> {
            this.dispose();
            parentFrame.setVisible(true);
        });

        buttonPanel.add(backButton);
        return buttonPanel;
    }

    private void loadInventoryData() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT item_id, item_name, quantity, location, expiry_date " +
                           "FROM items";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) ((JTable) ((JScrollPane) getContentPane().getComponent(0)).getViewport().getView()).getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getInt("quantity"),
                    rs.getString("location"),
                    rs.getDate("expiry_date"),
                    false, // Default value for delete
                    false  // Default value for update
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error");
        }
    }
}
