package com.quickchat;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main QuickChat application.
 * This combines Part 1, Part 2 and Part 3.
 * Console application only - no GUI allowed.
 */
public class QuickChat {

    // scanner for user input
    private static Scanner scanner = new Scanner(System.in);

    // the current user
    private static Login currentUser = new Login();

    // flag to check if user is logged in
    private static boolean isLoggedIn = false;

    // arrays for Part 3
    private static ArrayList<String> sentMessages = new ArrayList<String>();
    private static ArrayList<String> disregardedMessages = new ArrayList<String>();
    private static ArrayList<String> storedMessages = new ArrayList<String>();
    private static ArrayList<String> messageHashes = new ArrayList<String>();
    private static ArrayList<String> messageIDs = new ArrayList<String>();

    /**
     * Main method - this is where the program starts.
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("       Welcome to QuickChat");
        System.out.println("========================================");

        boolean running = true;

        while (running == true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                register();
            } else if (choice.equals("2")) {
                boolean loginResult = login();
                if (loginResult == true) {
                    messageMenu();
                }
            } else if (choice.equals("3")) {
                running = false;
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    /**
     * This method handles user registration.
     */
    private static void register() {
        System.out.println("\n--- Registration ---");

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter username (must contain _ and be <=5 chars): ");
        String username = scanner.nextLine();

        System.out.print("Enter password (8+ chars, capital, number, special): ");
        String password = scanner.nextLine();

        System.out.print("Enter South African cell number (with + and <=10 chars): ");
        String cell = scanner.nextLine();

        // create a new Login object with the details
        currentUser = new Login(username, password, cell, firstName, lastName);

        // try to register
        String result = currentUser.registerUser();
        System.out.println(result);

        if (result.equals("Username successfully captured.")) {
            System.out.println("Registration successful!");
        }
    }

    /**
     * This method handles user login.
     */
    private static boolean login() {
        System.out.println("\n--- Login ---");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser.setUsername(username);
        currentUser.setPassword(password);

        String status = currentUser.returnLoginStatus();
        System.out.println(status);

        isLoggedIn = currentUser.loginUser();

        return isLoggedIn;
    }

    /**
     * This method shows the message menu after login.
     */
    private static void messageMenu() {
        System.out.println("\nWelcome to QuickChat.");

        boolean inMenu = true;

        while (inMenu == true) {
            System.out.println("\nMenu:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");
            System.out.println("4) Quit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                sendMessages();
            } else if (choice.equals("2")) {
                System.out.println("Coming Soon.");
            } else if (choice.equals("3")) {
                storedMessagesMenu();
            } else if (choice.equals("4")) {
                inMenu = false;
                isLoggedIn = false;
                System.out.println("Logged out.");
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    /**
     * This method handles sending messages.
     */
    private static void sendMessages() {
        System.out.print("\nHow many messages do you want to send this session? ");

        int numMessages = 0;

        try {
            numMessages = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return;
        }

        // loop for each message
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " ---");

            // generate message ID
            String msgID = Message.generateMessageID();
            System.out.println("Message ID generated: " + msgID);

            // get recipient
            System.out.print("Enter recipient cell number: ");
            String recipient = scanner.nextLine();

            // get message text
            System.out.print("Enter message text: ");
            String text = scanner.nextLine();

            // create message object
            Message msg = new Message(msgID, i, recipient, text);

            // check message length
            String lengthCheck = msg.checkMessageLength();
            if (lengthCheck.contains("exceeds")) {
                System.out.println(lengthCheck);
                System.out.println("Message sent");
            } else {
                System.out.println("Message sent");
            }

            // check recipient cell
            String cellCheck = msg.checkRecipientCell();
            System.out.println(cellCheck);

            // show hash
            System.out.println("Message Hash: " + msg.getMessageHash());

            // ask user what to do
            System.out.println("\nOptions:");
            System.out.println("1. Send Message");
            System.out.println("2. Disregard Message");
            System.out.println("3. Store Message to send later");
            System.out.print("Choose: ");

            String action = scanner.nextLine();
            int actionChoice = Integer.parseInt(action);

            String result = msg.sentMessage(actionChoice);
            System.out.println(result);

            // add to arrays for Part 3
            messageIDs.add(msg.getMessageID());
            messageHashes.add(msg.getMessageHash());

            if (msg.getStatus().equals("sent")) {
                sentMessages.add(msg.getMessageText());
            } else if (msg.getStatus().equals("disregarded")) {
                disregardedMessages.add(msg.getMessageText());
            } else if (msg.getStatus().equals("stored")) {
                storedMessages.add(msg.getMessageText());
            }

            // display full details
            System.out.println("\nMessage Details:");
            System.out.println(msg);
        }

        // show total
        System.out.println("\nTotal messages sent: " + Message.getMessageCounter());
    }

    /**
     * This method shows the stored messages menu.
     */
    private static void storedMessagesMenu() {
        System.out.println("\n--- Stored Messages Menu ---");
        System.out.println("1. Display sender and recipient of all stored messages");
        System.out.println("2. Display the longest stored message");
        System.out.println("3. Search for message by ID");
        System.out.println("4. Search all messages for a particular recipient");
        System.out.println("5. Delete a message by Message Hash");
        System.out.println("6. Display full report of stored messages");
        System.out.print("Choose: ");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            displayStoredSendersRecipients();
        } else if (choice.equals("2")) {
            displayLongestStored();
        } else if (choice.equals("3")) {
            searchByMessageID();
        } else if (choice.equals("4")) {
            searchByRecipient();
        } else if (choice.equals("5")) {
            deleteByHash();
        } else if (choice.equals("6")) {
            displayReport();
        } else {
            System.out.println("Invalid option.");
        }
    }

    /**
     * This method displays sender and recipient of stored messages.
     */
    private static void displayStoredSendersRecipients() {
        ArrayList<Message> stored = Message.readStoredMessages();

        System.out.println("\nStored Messages:");

        for (int i = 0; i < stored.size(); i++) {
            Message m = stored.get(i);
            System.out.println("Sender: " + currentUser.getFirstName() + " " + currentUser.getLastName()
                + ", Recipient: " + m.getRecipient());
        }
    }

    /**
     * This method finds and displays the longest stored message.
     */
    private static void displayLongestStored() {
        ArrayList<Message> stored = Message.readStoredMessages();

        Message longest = null;

        for (int i = 0; i < stored.size(); i++) {
            Message m = stored.get(i);

            if (longest == null) {
                longest = m;
            } else if (m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }

        if (longest != null) {
            System.out.println("Longest stored message: " + longest.getMessageText());
        } else {
            System.out.println("No stored messages found.");
        }
    }

    /**
     * This method searches for a message by its ID.
     */
    private static void searchByMessageID() {
        System.out.print("Enter Message ID to search: ");
        String id = scanner.nextLine();

        ArrayList<Message> stored = Message.readStoredMessages();
        boolean found = false;

        for (int i = 0; i < stored.size(); i++) {
            Message m = stored.get(i);

            if (m.getMessageID().equals(id)) {
                System.out.println("Found - Recipient: " + m.getRecipient() + ", Message: " + m.getMessageText());
                found = true;
                break;
            }
        }

        if (found == false) {
            System.out.println("Message ID not found.");
        }
    }

    /**
     * This method searches for all messages sent to a recipient.
     */
    private static void searchByRecipient() {
        System.out.print("Enter recipient number: ");
        String recipient = scanner.nextLine();

        ArrayList<Message> all = new ArrayList<Message>();

        // add messages from memory
        for (int i = 0; i < Message.getAllMessages().size(); i++) {
            all.add(Message.getAllMessages().get(i));
        }

        // add messages from file
        for (int i = 0; i < Message.readStoredMessages().size(); i++) {
            all.add(Message.readStoredMessages().get(i));
        }

        System.out.println("Messages for " + recipient + ":");

        for (int i = 0; i < all.size(); i++) {
            Message m = all.get(i);

            if (m.getRecipient().equals(recipient)) {
                System.out.println("- " + m.getMessageText());
            }
        }
    }

    /**
     * This method deletes a message by its hash.
     */
    private static void deleteByHash() {
        System.out.print("Enter Message Hash to delete: ");
        String hash = scanner.nextLine();

        ArrayList<Message> stored = Message.readStoredMessages();
        boolean found = false;

        for (int i = 0; i < stored.size(); i++) {
            Message m = stored.get(i);

            if (m.getMessageHash().equals(hash)) {
                System.out.println("Message: '" + m.getMessageText() + "' successfully deleted.");
                found = true;
                break;
            }
        }

        if (found == false) {
            System.out.println("Hash not found.");
        }
    }

    /**
     * This method displays a report of all stored messages.
     */
    private static void displayReport() {
        ArrayList<Message> stored = Message.readStoredMessages();

        System.out.println("\n=== Stored Messages Report ===");
        System.out.printf("%-25s %-15s %-30s\n", "Message Hash", "Recipient", "Message");
        System.out.println("-------------------------------------------------------------------");

        for (int i = 0; i < stored.size(); i++) {
            Message m = stored.get(i);
            System.out.printf("%-25s %-15s %-30s\n", m.getMessageHash(), m.getRecipient(), m.getMessageText());
        }
    }

    // getters for arrays (used in tests)
    public static ArrayList<String> getSentMessages() {
        return sentMessages;
    }

    public static ArrayList<String> getDisregardedMessages() {
        return disregardedMessages;
    }

    public static ArrayList<String> getStoredMessages() {
        return storedMessages;
    }

    public static ArrayList<String> getMessageHashes() {
        return messageHashes;
    }

    public static ArrayList<String> getMessageIDs() {
        return messageIDs;
    }

    // reset arrays for testing
    public static void resetArrays() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
    }
}