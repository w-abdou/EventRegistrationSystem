

# Event Registration System

## Overview

The **Event Registration System** is a desktop application developed in **Java** with a **Graphical User Interface (GUI)**. It allows event organizers to create and manage events, register attendees, and view event details in a user-friendly interface. The system supports different types of attendees (Regular, Premium, VIP) and includes functionalities for event management, registration, and data persistence.

## Features

- **Graphical User Interface (GUI)**: A user-friendly interface for managing events and attendees.
- **Event Management**: Create, update, and delete events.
- **Attendee Registration**: Register attendees with different tiers (Regular, Premium, VIP).
- **Event Listing**: View all events and their details.
- **Login System**: Secure access for event organizers.
- **Persistent Storage (Optional)**: Save and load event and attendee data.

## Code Structure

The main components of the code include:

### 1. **Classes**
- `Event`: Represents a generic event with attributes like event name, date, and location.
- `Conference`: Subclass of `Event` representing a conference with a keynote speaker.
- `Workshop`: Subclass of `Event` representing a workshop with an instructor.
- `Attendee`: Stores attendee details such as name, email, and ticket type.
- `PremiumAttendee` & `VIPAttendee`: Subclasses of `Attendee` with additional benefits.
- `EventManager`: Handles event and attendee management.

### 2. **Graphical User Interface (GUI)**
- `MainFrame`: The main application window.
- `LoginPanel`: A login screen for event organizers.
- `EventPanel`: Interface for adding and managing events.
- `RegistrationPanel`: Interface for attendee registration.
- `EventDetailsPanel`: Displays event details, including the list of registered attendees.

## How to Use

### 1. Clone the Repository:
```bash
git clone https://github.com/w-abdou/EventRegistrationSystem.git
cd EventRegistrationSystem
```

### 2. Compile and Run the Program:
Ensure you have **Java and JavaFX (if applicable) installed**, then compile and run:

```bash
javac *.java
java MainFrame
```

### 3. Application Workflow:
1. **Login**: The system starts with a login screen (if authentication is enabled).
2. **Main Dashboard**:
   - View existing events.
   - Add a new event (conference or workshop).
   - Register attendees.
   - View event details, including registered attendees.
3. **Register Attendees**:
   - Choose an event.
   - Enter attendee details.
   - Select attendee type (Regular, Premium, VIP).
4. **View & Manage Events**:
   - Edit or delete events.
   - View attendee lists.

## Example Screenshots
*(Include screenshots of the UI for better documentation)*

## Notes

- Ensure **JavaFX** is installed if the application uses JavaFX for the GUI.
- If using **Swing**, no additional dependencies are required.

