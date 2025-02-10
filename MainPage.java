import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainPage extends JFrame {

    // Constants for UI configuration
    private static final Color HEADER_BG_COLOR = new Color(240, 240, 250);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Dimension WINDOW_SIZE = new Dimension(1024, 768);
    private static final String LOGO_PATH = "a.jpg"; // Ensure the image path is correct

    public MainPage() {
        initializeFrame();
        setupUIComponents();
    }

    private void initializeFrame() {
        setTitle("University Inventory Management");
        setSize(WINDOW_SIZE);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void setupUIComponents() {
        // Create the header panel with title and logo
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Create and set the menu bar
        setJMenuBar(createMainMenuBar());
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG_COLOR);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 25, 15, 25),
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY)
        ));

        // Create the title label
        JLabel title = new JLabel("University Inventory Management");
        title.setFont(TITLE_FONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Create the logo label and align it to the right
        JLabel logo = createScaledLogo(LOGO_PATH, 100, 100);
        logo.setHorizontalAlignment(SwingConstants.RIGHT);

        // Add the title and logo to the header
        header.add(title, BorderLayout.CENTER);
        header.add(logo, BorderLayout.EAST);

        return header;
    }

    private JLabel createScaledLogo(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                throw new Exception("Image not found or failed to load.");
            }
            Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading logo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new JLabel("LOGO");
        }
    }

    private JMenuBar createMainMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        String[] menuItems = {"Login", "Register", "Inventory", "Orders", "Developers", "About"};
        for (String item : menuItems) {
            menuBar.add(createMenu(item));
        }
        return menuBar;
    }

    private JMenu createMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add menu items dynamically
        if (title.equals("Inventory")) {
            menu.add(createMenuItem("Inventory"));
        } else if (title.equals("Login")) {
            menu.add(createMenuItem("Login"));
        } else if (title.equals("Register")) {
            menu.add(createMenuItem("Sign Up"));
        } else if (title.equals("Orders")) {
            menu.add(createMenuItem("View Orders"));
        } else if (title.equals("Developers")) {
            menu.add(createMenuItem("Developers"));
        } else if (title.equals("About")) {
            menu.add(createMenuItem("About"));
        }
        return menu;
    }

    private JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(e -> handleMenuAction(e));
        return item;
    }

    private void handleMenuAction(ActionEvent e) {
        String command = ((JMenuItem) e.getSource()).getText();
        switch (command) {
            case "Login":
                openLoginWindow();
                break;
            case "Sign Up":
                openRegistrationWindow();
                break;
            case "Inventory":
                openInventoryPage();
                break;
            case "View Orders":
                openOrderInventoryPage();
                break;
            case "Developers":
                developerstable();
                break;
            case "About":
                openAboutPage();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Selected: " + command);
                break;
        }
    }

    private void openLoginWindow() {
        SwingUtilities.invokeLater(() -> {
            LoginSystem loginSystem = new LoginSystem(this);
            loginSystem.setVisible(true);
            this.setVisible(false);
        });
    }

    private void openRegistrationWindow() {
        SwingUtilities.invokeLater(() -> {
            RegistrationSystem registrationSystem = new RegistrationSystem(this);
            registrationSystem.setVisible(true);
            this.setVisible(false);
        });
    }

    private void openInventoryPage() {
        SwingUtilities.invokeLater(() -> {
            InventoryPage inventoryPage = new InventoryPage(this);
            inventoryPage.setVisible(true);
            this.setVisible(false);
        });
    }

    private void openOrderInventoryPage() {
        SwingUtilities.invokeLater(() -> {
            OrderInventoryPage orderInventoryPage = new OrderInventoryPage(this);
            orderInventoryPage.setVisible(true);
            this.setVisible(false);
        });
    }

    private void developerstable() {
        SwingUtilities.invokeLater(() -> {
            DevelopersTable developerstable = new DevelopersTable(this);
            developerstable.setVisible(true);
            this.setVisible(false);
        });
    }

    private void openAboutPage() {
        SwingUtilities.invokeLater(() -> {
            AboutSystem aboutSystem = new AboutSystem();
            aboutSystem.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new MainPage().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error initializing application: " + e.getMessage(),
                        "Startup Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
