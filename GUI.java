import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class GUI {

    private String currentUserRole = ""; // Tracks the role of the logged-in user
    private static final String[] VALID_CATEGORIES = {"Tech", "Business", "Health", "Education", "Art"};

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }

    public GUI() {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            System.out.println("Failed to set Look and Feel");
        }
        showLoginPage();
    }

    private void showLoginPage() {
        JFrame frame = new JFrame("Login Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JLabel errorLabel = new JLabel();

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(errorLabel, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateLogin(username, password)) {
                frame.dispose();
                if ("admin".equals(currentUserRole)) {
                    showAdminDashboard();
                } else {
                    showUserDashboard();
                }
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private boolean validateLogin(String username, String password) {
        File loginFile = new File("login_info.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(loginFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String storedUsername = parts[0];
                    String storedPassword = parts[1];
                    String role = parts[2];
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        currentUserRole = role;
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: login_info.txt file not found.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    

    private void showAdminDashboard() {
        JFrame frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
    
        JPanel panel = new JPanel(new BorderLayout());
    
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    
        JTable eventsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(eventsTable);
        loadEventsIntoTable(eventsTable);
    
        JButton createEventButton = new JButton("Create Event");
        JButton modifyEventButton = new JButton("Modify Event");
        JButton deleteEventButton = new JButton("Delete Event");
        JButton logoutButton = new JButton("Logout");
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createEventButton);
        buttonPanel.add(modifyEventButton);
        buttonPanel.add(deleteEventButton);
        buttonPanel.add(logoutButton);
    
        createEventButton.addActionListener(e -> {
            showCreateEventDialog(frame);
            loadEventsIntoTable(eventsTable);
        });
    
        modifyEventButton.addActionListener(e -> {
            int selectedRow = eventsTable.getSelectedRow();
            if (selectedRow != -1) {
                modifySelectedEvent(frame, eventsTable, selectedRow);
                loadEventsIntoTable(eventsTable);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an event to modify.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        deleteEventButton.addActionListener(e -> {
            int selectedRow = eventsTable.getSelectedRow();
            if (selectedRow != -1) {
                deleteSelectedEvent(eventsTable, selectedRow);
                loadEventsIntoTable(eventsTable);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an event to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        logoutButton.addActionListener(e -> {
            frame.dispose();
            showLoginPage();
        });
    
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    
        frame.add(panel);
        frame.setVisible(true);
    }
    private void showUserDashboard() {
        JFrame frame = new JFrame("User Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
    
        JPanel panel = new JPanel(new BorderLayout());
    
        JLabel titleLabel = new JLabel("User Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    
        JTable eventsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(eventsTable);
        loadEventsIntoTable(eventsTable);
    
        JButton registerButton = new JButton("Register for Event");
        JButton logoutButton = new JButton("Logout");
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(logoutButton);
    
        registerButton.addActionListener(e -> {
            int selectedRow = eventsTable.getSelectedRow();
            if (selectedRow != -1) {
                registerForSelectedEvent(eventsTable, selectedRow);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an event to register.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        logoutButton.addActionListener(e -> {
            frame.dispose();
            showLoginPage();
        });
    
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    
        frame.add(panel);
        frame.setVisible(true);
    }
        
    private void showCreateEventDialog(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Create Event", true);
        dialog.setSize(400, 300);
    
        JPanel panel = new JPanel(new GridLayout(6, 2));
    
        JLabel nameLabel = new JLabel("Event Name:");
        JTextField nameField = new JTextField();
    
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField();
    
        JLabel categoryLabel = new JLabel("Category:");
        JComboBox<String> categoryComboBox = new JComboBox<>(VALID_CATEGORIES);
    
        JLabel capacityLabel = new JLabel("Capacity:");
        JTextField capacityField = new JTextField();
    
        JLabel priceLabel = new JLabel("Ticket Price:");
        JTextField priceField = new JTextField();
    
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String date = dateField.getText().trim();
            String category = (String) categoryComboBox.getSelectedItem();
            String capacityText = capacityField.getText().trim();
            String priceText = priceField.getText().trim();
    
            if (name.isEmpty() || date.isEmpty() || category == null || capacityText.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (!isValidDate(date)) {
                JOptionPane.showMessageDialog(dialog, "Date must be in the format YYYY-MM-DD and not in the past.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            try {
                int capacity = Integer.parseInt(capacityText);
                double price = Double.parseDouble(priceText);
    
                if (capacity <= 0 || capacity > 1000) {
                    JOptionPane.showMessageDialog(dialog, "Capacity must be between 1 and 1000.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                if (price <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Price must be a positive number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                saveEventToFile(name, date, category, capacity, price);
                JOptionPane.showMessageDialog(dialog, "Event created successfully.");
                dialog.dispose();
    
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Capacity and price must be valid numbers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        cancelButton.addActionListener(e -> dialog.dispose());
    
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(categoryLabel);
        panel.add(categoryComboBox);
        panel.add(capacityLabel);
        panel.add(capacityField);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(saveButton);
        panel.add(cancelButton);
    
        dialog.add(panel);
        dialog.setVisible(true);
    }
    private void modifySelectedEvent(JFrame parentFrame, JTable eventsTable, int selectedRow) {
        String eventName = (String) eventsTable.getValueAt(selectedRow, 0);
        String eventDate = (String) eventsTable.getValueAt(selectedRow, 1);
        String eventCategory = (String) eventsTable.getValueAt(selectedRow, 2);
        String eventCapacity = (String) eventsTable.getValueAt(selectedRow, 3);
        String eventPrice = (String) eventsTable.getValueAt(selectedRow, 4);
    
        JDialog dialog = new JDialog(parentFrame, "Modify Event", true);
        dialog.setSize(400, 300);
    
        JPanel panel = new JPanel(new GridLayout(6, 2));
    
        JLabel nameLabel = new JLabel("Event Name:");
        JTextField nameField = new JTextField(eventName);
    
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField(eventDate);
    
        JLabel categoryLabel = new JLabel("Category:");
        JComboBox<String> categoryComboBox = new JComboBox<>(VALID_CATEGORIES);
        categoryComboBox.setSelectedItem(eventCategory);
    
        JLabel capacityLabel = new JLabel("Capacity:");
        JTextField capacityField = new JTextField(eventCapacity);
    
        JLabel priceLabel = new JLabel("Ticket Price:");
        JTextField priceField = new JTextField(eventPrice);
    
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        saveButton.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newDate = dateField.getText().trim();
            String newCategory = (String) categoryComboBox.getSelectedItem();
            String newCapacity = capacityField.getText().trim();
            String newPrice = priceField.getText().trim();
    
            if (newName.isEmpty() || newDate.isEmpty() || newCategory == null || newCapacity.isEmpty() || newPrice.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (!isValidDate(newDate)) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format or date in the past.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            try {
                int capacity = Integer.parseInt(newCapacity);
                double price = Double.parseDouble(newPrice);
    
                if (capacity <= 0 || capacity > 1000) {
                    JOptionPane.showMessageDialog(dialog, "Capacity must be between 1 and 1000.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                if (price <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Price must be positive.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                modifyEventInFile(selectedRow, newName, newDate, newCategory, capacity, price);
                JOptionPane.showMessageDialog(dialog, "Event modified successfully.");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Capacity and price must be valid numbers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        cancelButton.addActionListener(e -> dialog.dispose());
    
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(dateLabel);
        panel.add(dateField);
    }
       
        private void deleteSelectedEvent(JTable eventsTable, int selectedRow) {
            try {
                List<String> lines = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader("event_list.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                }
        
                lines.remove(selectedRow);
        
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("event_list.txt"))) {
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
        
                JOptionPane.showMessageDialog(null, "Event deleted successfully.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error deleting event.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        private void modifyEventInFile(int rowIndex, String name, String date, String category, int capacity, double price) {
            try {
                List<String> lines = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader("event_list.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                }
        
                // Update the specific row
                lines.set(rowIndex, name + "," + date + "," + category + "," + capacity + "," + price);
        
                // Rewrite the file with updated data
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("event_list.txt"))) {
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
        
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error modifying event.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
                
    private void registerForSelectedEvent(JTable eventsTable, int selectedRow) {
        String eventName = (String) eventsTable.getValueAt(selectedRow, 0);
        JFrame frame = new JFrame("Register for Event: " + eventName);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Your Name:");
        JTextField nameField = new JTextField();

        JLabel emailLabel = new JLabel("Your Email:");
        JTextField emailField = new JTextField();

        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        submitButton.addActionListener(e -> {
            String userName = nameField.getText().trim();
            String userEmail = emailField.getText().trim();

            if (userName.isEmpty() || userEmail.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(userEmail)) {
                JOptionPane.showMessageDialog(frame, "Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            saveRegistration(eventName, userName, userEmail);
            JOptionPane.showMessageDialog(frame, "Successfully registered for " + eventName + "!");
            frame.dispose();
        });

        cancelButton.addActionListener(e -> frame.dispose());

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(submitButton);
        panel.add(cancelButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void saveEventToFile(String name, String date, String category, int capacity, double price) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("event_list.txt", true))) {
            writer.write(name + "," + date + "," + category + "," + capacity + "," + price);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving event.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void saveRegistration(String eventName, String userName, String userEmail) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("registrations.txt", true))) {
            writer.write(eventName + "," + userName + "," + userEmail);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving registration.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    
    private void loadEventsIntoTable(JTable eventsTable) {
        String[] columnNames = {"Name", "Date", "Category", "Capacity", "Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        eventsTable.setModel(model);
        eventsTable.setAutoCreateRowSorter(true); // Enable sorting

        try (BufferedReader reader = new BufferedReader(new FileReader("event_list.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                model.addRow(parts);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading events.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate enteredDate = LocalDate.parse(date, formatter);
            return !enteredDate.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
