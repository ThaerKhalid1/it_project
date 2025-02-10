import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DevelopersTable extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/warehouse_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345";

    public DevelopersTable(MainPage mainPage) {
        // إعداد الإطار الرئيسي
        setTitle("Developers");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // إنشاء الجدول
        String[] columnNames = {"Std ID", "Std Name", "Contact No", "Email"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // تعطيل تحرير الخلايا
                return false;
            }
        };

        JTable table = new JTable(model);

        // إضافة تنسيق للجدول
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setGridColor(Color.GRAY);

        // إنشاء لوحة تمرير للجدول
        JScrollPane scrollPane = new JScrollPane(table);

        // إضافة لوحة التمرير إلى الإطار الرئيسي
        add(scrollPane, BorderLayout.CENTER);

        // تحسين مظهر الإطار
        setBackground(new Color(240, 240, 240));
        getContentPane().setBackground(new Color(240, 240, 240));

        // إضافة زر رجوع
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            mainPage.setVisible(true);
            this.dispose();
        });
        add(backButton, BorderLayout.SOUTH);

        // تحميل البيانات من قاعدة البيانات
        loadDevelopersData(model);
    }

    private void loadDevelopersData(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT std_id, std_name, contact_no, email FROM developers";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("std_id"),
                    rs.getString("std_name"),
                    rs.getString("contact_no"),
                    rs.getString("email")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error");
        }
    }

    public static void main(String[] args) {
        // إنشاء وعرض الإطار الرئيسي
        SwingUtilities.invokeLater(() -> {
            MainPage mainPage = new MainPage();
            DevelopersTable frame = new DevelopersTable(mainPage);
            frame.setVisible(true);
        });
    }
}
