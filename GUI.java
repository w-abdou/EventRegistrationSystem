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
        frame.setSize(900, 500);
    
        JPanel panel = new JPanel(new BorderLayout());
    
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    
        JTable eventsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(eventsTable);
        loadEventsIntoTable(eventsTable);
    
        // Set column widths for better organization
        eventsTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Name
        eventsTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Type
        eventsTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Date
        eventsTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Organizer
        eventsTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Category
        eventsTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Capacity
        eventsTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Price
    
        JButton createEventButton = new JButton("Create Event");
        JButton modifyEventButton = new JButton("Modify Event");
        JButton deleteEventButton = new JButton("Delete Event");
        JButton logoutButton = new JButton("Logout");
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createEventButton);
        buttonPanel.add(modifyEventButton);
        buttonPanel.add(deleteEventButton);
        buttonPanel.add(logoutButton);
    
        // Button actions
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
        dialog.setSize(500, 400);
    
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        JLabel nameLabel = new JLabel("Event Name:");
        JTextField nameField = new JTextField();
    
        JLabel typeLabel = new JLabel("Event Type:");
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Conference", "Workshop", "Event"});
    
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField();
    
        JLabel organizerLabel = new JLabel("Organizer's Name:");
        JTextField organizerField = new JTextField();
    
        JLabel categoryLabel = new JLabel("Category:");
        JComboBox<String> categoryComboBox = new JComboBox<>(VALID_CATEGORIES);
    
        JLabel capacityLabel = new JLabel("Capacity:");
        JTextField capacityField = new JTextField();
    
        JLabel priceLabel = new JLabel("Ticket Price:");
        JTextField priceField = new JTextField();
    
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(nameField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(typeLabel, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(typeComboBox, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(dateLabel, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(dateField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(organizerLabel, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(organizerField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(categoryLabel, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(categoryComboBox, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(capacityLabel, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(capacityField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(priceLabel, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(priceField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(saveButton, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(cancelButton, gbc);
    
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String type = (String) typeComboBox.getSelectedItem();
            String date = dateField.getText().trim();
            String organizer = organizerField.getText().trim();
            String category = (String) categoryComboBox.getSelectedItem();
            String capacityText = capacityField.getText().trim();
            String priceText = priceField.getText().trim();
    
            if (name.isEmpty() || type == null || date.isEmpty() || organizer.isEmpty() || category == null || capacityText.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (!isValidDate(date)) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format or date in the past.", "Validation Error", JOptionPane.ERROR_MESSAGE);
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
    
                saveEventToFile(name, type, date, organizer, category, capacity, price);
    
                // Show additional dialog if the event is a Conference or Workshop
                if ("Conference".equals(type)) {
                    showAddSpeakersDialog(name);
                } else if ("Workshop".equals(type)) {
                    showAddTopicDialog(name);
                }
    
                JOptionPane.showMessageDialog(dialog, "Event created successfully.");
                dialog.dispose();
    
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Capacity and price must be valid numbers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        cancelButton.addActionListener(e -> dialog.dispose());
    
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    // Dialog for adding number of speakers
    private void showAddSpeakersDialog(String eventName) {
        JDialog dialog = new JDialog((JFrame) null, "Add Number of Speakers", true);
        dialog.setSize(300, 200);
    
        JPanel panel = new JPanel(new GridLayout(2, 2));
    
        JLabel speakersLabel = new JLabel("Number of Speakers:");
        JTextField speakersField = new JTextField();
    
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        saveButton.addActionListener(e -> {
            String speakersText = speakersField.getText().trim();
            if (speakersText.isEmpty() || !speakersText.matches("\\d+")) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number of speakers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            updateEventWithSpeakers(eventName, speakersText);
            JOptionPane.showMessageDialog(dialog, "Number of speakers added successfully.");
            dialog.dispose();
        });
    
        cancelButton.addActionListener(e -> dialog.dispose());
    
        panel.add(speakersLabel);
        panel.add(speakersField);
        panel.add(saveButton);
        panel.add(cancelButton);
    
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    // Dialog for adding workshop topic
    private void showAddTopicDialog(String eventName) {
        JDialog dialog = new JDialog((JFrame) null, "Add Workshop Topic", true);
        dialog.setSize(300, 200);
    
        JPanel panel = new JPanel(new GridLayout(2, 2));
    
        JLabel topicLabel = new JLabel("Workshop Topic:");
        JTextField topicField = new JTextField();
    
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        saveButton.addActionListener(e -> {
            String topic = topicField.getText().trim();
            if (topic.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a workshop topic.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            updateEventWithTopic(eventName, topic);
            JOptionPane.showMessageDialog(dialog, "Workshop topic added successfully.");
            dialog.dispose();
        });
    
        cancelButton.addActionListener(e -> dialog.dispose());
    
        panel.add(topicLabel);
        panel.add(topicField);
        panel.add(saveButton);
        panel.add(cancelButton);
    
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void updateEventWithSpeakers(String eventName, String speakers) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("event_list.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].equals(eventName)) {
                        // Add speakers to the matching event
                        while (parts.length < 9) {
                            line += ",N/A"; // Ensure the event line has enough columns
                        }
                        line = String.join(",", parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], speakers, parts[8]);
                    }
                    lines.add(line);
                }
            }
    
            // Write back the updated lines to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("event_list.txt"))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating event with number of speakers.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateEventWithTopic(String eventName, String topic) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("event_list.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].equals(eventName)) {
                        // Add topic to the matching event
                        while (parts.length < 9) {
                            line += ",N/A"; // Ensure the event line has enough columns
                        }
                        line = String.join(",", parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], topic);
                    }
                    lines.add(line);
                }
            }
    
            // Write back the updated lines to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("event_list.txt"))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating event with workshop topic.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
        
    // private void saveEventToFile(String name, String type, String date, String organizer, String category, int capacity, double price, String speakers, String topic) {
    //     try (BufferedWriter writer = new BufferedWriter(new FileWriter("event_list.txt", true))) {
    //         writer.write(String.join(",", name, type, date, organizer, category, String.valueOf(capacity), String.valueOf(price), speakers, topic));
    //         writer.newLine();
    //     } catch (IOException e) {
    //         JOptionPane.showMessageDialog(null, "Error saving event.", "File Error", JOptionPane.ERROR_MESSAGE);
    //     }
    // }
    
    
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
    
            if (newName.isEmpty()) {
                nameField.setBorder(BorderFactory.createLineBorder(Color.RED));
            } else {
                nameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
    
            if (newDate.isEmpty() || !isValidDate(newDate)) {
                dateField.setBorder(BorderFactory.createLineBorder(Color.RED));
                JOptionPane.showMessageDialog(dialog, "Invalid or past date. Use YYYY-MM-DD.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                dateField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
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
    
    private void deleteSelectedEvent(JTable eventsTable, int selectedRow) {
        try {
            // Read all lines from the file into a list
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("event_list.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
    
            // Remove the selected row
            if (selectedRow >= 0 && selectedRow < lines.size()) {
                lines.remove(selectedRow);
    
                // Write the updated list back to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("event_list.txt"))) {
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
    
                JOptionPane.showMessageDialog(null, "Event deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid selection.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
    
            if (rowIndex < 0 || rowIndex >= lines.size()) {
                JOptionPane.showMessageDialog(null, "Invalid row index.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            lines.set(rowIndex, name + "," + date + "," + category + "," + capacity + "," + price);
    
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("event_list.txt"))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
    
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error modifying event: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
            
    private void registerForSelectedEvent(JTable eventsTable, int selectedRow) {
        String eventName = (String) eventsTable.getValueAt(selectedRow, 0);
        JFrame frame = new JFrame("Register for Event: " + eventName);
        frame.setSize(400, 250);
    
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        JLabel nameLabel = new JLabel("Your Name:");
        JTextField nameField = new JTextField();
    
        JLabel emailLabel = new JLabel("Your Email:");
        JTextField emailField = new JTextField();
    
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
    
        // Adding components to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(nameField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(emailLabel, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(emailField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(submitButton, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(cancelButton, gbc);
    
        // Submit Button Action
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
    
        // Cancel Button Action
        cancelButton.addActionListener(e -> frame.dispose());
    
        frame.add(panel);
        frame.setVisible(true);
    }
    
    private void saveEventToFile(String name, String type, String date, String organizer, String category, int capacity, double price) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("event_list.txt", true))) {
            writer.write(name + "," + type + "," + date + "," + organizer + "," + category + "," + capacity + "," + price);
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
        String[] columnNames = {"Name", "Type", "Date", "Organizer", "Category", "Capacity", "Price", "Speakers", "Topic"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        eventsTable.setModel(model);
        eventsTable.setAutoCreateRowSorter(true); // Enable sorting for columns
    
        try (BufferedReader reader = new BufferedReader(new FileReader("event_list.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                
                if (parts.length == columnNames.length) {
                    model.addRow(parts);
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Data mismatch in event_list.txt", "Data Error", JOptionPane.ERROR_MESSAGE);
                }
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
