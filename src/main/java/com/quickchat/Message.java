package com.quickchat;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;

/**
 * Message class for QuickChat.
 * This is for Part 2 of the PROG5121 PoE.
 * 
 * I used the org.json library for JSON.
 * I found out about it from: https://github.com/stleary/JSON-java
 */
public class Message {

    // variables for each message
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status;

    // static variables to keep track of all messages
    private static ArrayList<Message> allMessages = new ArrayList<Message>();
    private static int messageCounter = 0;

    // the file where we store messages
    private static final String JSON_FILE = "stored_messages.json";

    // empty constructor
    public Message() {
    }

    // constructor with fields
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        this.status = "";
    }

    // setters
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // getters
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getStatus() {
        return status;
    }

    public static ArrayList<Message> getAllMessages() {
        return allMessages;
    }

    public static int getMessageCounter() {
        return messageCounter;
    }

    public static void setMessageCounter(int count) {
        messageCounter = count;
    }

    /**
     * This method checks if the message ID is 10 characters or less.
     */
    public boolean checkMessageID() {
        if (messageID == null) {
            return false;
        }

        if (messageID.length() <= 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method checks if the recipient cell number is correct.
     * It must start with + and be 10 characters or less.
     */
    public String checkRecipientCell() {
        if (recipient == null) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }

        if (recipient.length() > 10) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }

        if (recipient.startsWith("+") == false) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }

        return "Cell phone number successfully captured.";
    }

    /**
     * This method creates the message hash.
     * Format: first 2 digits of ID : message number : FIRSTWORD + LASTWORD in caps
     */
    public String createMessageHash() {
        // get first 2 digits of message ID
        String firstTwo = "";
        if (messageID.length() >= 2) {
            firstTwo = messageID.substring(0, 2);
        } else {
            firstTwo = messageID;
        }

        // split the message into words
        String[] words = messageText.trim().split("\\s+");

        // get first word in caps
        String firstWord = words[0].toUpperCase();

        // get last word in caps
        String lastWord = words[words.length - 1].toUpperCase();

        // put it all together
        String hash = firstTwo + ":" + messageNumber + ":" + firstWord + lastWord;

        return hash;
    }

    /**
     * This method checks if the message is not too long.
     */
    public String checkMessageLength() {
        if (messageText == null) {
            return "Message exceeds 250 characters by 0; please reduce the size.";
        }

        if (messageText.length() > 250) {
            int over = messageText.length() - 250;
            return "Message exceeds 250 characters by " + over + "; please reduce the size.";
        }

        return "Message ready to send.";
    }

    /**
     * This method handles sending, disregarding or storing a message.
     * choice 1 = send, 2 = disregard, 3 = store
     */
    public String sentMessage(int choice) {
        if (choice == 1) {
            status = "sent";
            messageCounter = messageCounter + 1;
            allMessages.add(this);
            return "Message successfully sent.";
        } else if (choice == 2) {
            status = "disregarded";
            allMessages.add(this);
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            status = "stored";
            allMessages.add(this);
            storeMessage();
            return "Message successfully stored.";
        } else {
            return "Invalid choice.";
        }
    }

    /**
     * This method returns all the sent messages.
     */
    public String printMessages() {
        String result = "";

        for (int i = 0; i < allMessages.size(); i++) {
            Message m = allMessages.get(i);
            if (m.status.equals("sent")) {
                result = result + m.messageID + " " + m.messageHash + " " + m.recipient + " " + m.messageText + "\n";
            }
        }

        // remove the last newline
        if (result.endsWith("\n")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    /**
     * This method returns the total number of messages sent.
     */
    public int returnTotalMessages() {
        return messageCounter;
    }

    /**
     * This method stores a message to a JSON file.
     * I researched how to do this using the org.json library.
     * Reference: https://github.com/stleary/JSON-java
     */
    public void storeMessage() {
        try {
            File file = new File(JSON_FILE);
            JSONArray jsonArray;

            // check if file already exists
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                jsonArray = new JSONArray(content);
            } else {
                jsonArray = new JSONArray();
            }

            // create a JSON object for this message
            JSONObject obj = new JSONObject();
            obj.put("messageID", messageID);
            obj.put("messageNumber", messageNumber);
            obj.put("recipient", recipient);
            obj.put("messageText", messageText);
            obj.put("messageHash", messageHash);
            obj.put("status", status);

            // add to array and save
            jsonArray.put(obj);
            Files.write(file.toPath(), jsonArray.toString(2).getBytes());

        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    /**
     * This method reads stored messages from the JSON file.
     */
    public static ArrayList<Message> readStoredMessages() {
        ArrayList<Message> stored = new ArrayList<Message>();

        try {
            File file = new File(JSON_FILE);

            if (file.exists() == false) {
                return stored;
            }

            String content = new String(Files.readAllBytes(file.toPath()));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Message m = new Message(
                    obj.getString("messageID"),
                    obj.getInt("messageNumber"),
                    obj.getString("recipient"),
                    obj.getString("messageText")
                );

                m.messageHash = obj.getString("messageHash");
                m.status = obj.getString("status");

                stored.add(m);
            }

        } catch (IOException e) {
            System.out.println("Error reading stored messages: " + e.getMessage());
        }

        return stored;
    }

    /**
     * This method generates a random 10-digit message ID.
     */
    public static String generateMessageID() {
        Random rand = new Random();
        String id = "";

        for (int i = 0; i < 10; i++) {
            int num = rand.nextInt(10);
            id = id + num;
        }

        return id;
    }

    /**
     * This method resets everything for testing.
     */
    public static void reset() {
        allMessages.clear();
        messageCounter = 0;

        File file = new File(JSON_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * This is the toString method.
     */
    public String toString() {
        return messageID + " " + messageHash + " " + recipient + " " + messageText;
    }
}