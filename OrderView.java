import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class OrderView extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/warehouse_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    private final JFrame parentFrame;

    public OrderView(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initializeFrame();
        setupUIComponents();
        loadOrderData();
    }

    private void initializeFrame() {
        setTitle("Order Management System");
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
        String[] columns = {"Order ID", "Item ID", "Item Name", "Qty", "Purchase Date", "Paid", "Approved"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 || columnIndex == 6 ? Boolean.class : super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            this.setVisible(false);
            parentFrame.setVisible(true);
        });

        buttonPanel.add(backButton);
        return buttonPanel;
    }

    private void loadOrderData() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT o.order_id, i.item_id, i.item_name, o.quantity, o.purchase_date, o.paid, o.approved " +
                           "FROM orders o " +
                           "JOIN items i ON o.item_id = i.item_id";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) ((JTable) ((JScrollPane) getContentPane().getComponent(0)).getViewport().getView()).getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("order_id"),
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getInt("quantity"),
                    rs.getDate("purchase_date"),
                    rs.getBoolean("paid"),
                    rs.getBoolean("approved")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error");
        }
    }
}
