package com.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * JUnit tests for Part 3 - Arrays, Stored Messages, and Reports.
 * These tests use the exact test data from the POE brief.
 */
public class TaskTest {

    @BeforeEach
    public void setUp() {
        Message.reset();
        QuickChat.resetArrays();
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        Message msg1 = new Message("1234567890", 0, "+27834557896", "Did you get the cake?");
        msg1.sentMessage(1);

        Message msg2 = new Message("0987654321", 1, "+27838884567", 
                                   "Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage(3);

        Message msg3 = new Message("1122334455", 2, "+27834484567", "Yohoooo, I am at your gate.");
        msg3.sentMessage(2);

        Message msg4 = new Message("5566778899", 3, "0838884567", "It is dinner time!");
        msg4.sentMessage(1);

        QuickChat.getSentMessages().add(msg1.getMessageText());
        QuickChat.getStoredMessages().add(msg2.getMessageText());
        QuickChat.getDisregardedMessages().add(msg3.getMessageText());
        QuickChat.getSentMessages().add(msg4.getMessageText());

        assertTrue(QuickChat.getSentMessages().contains("Did you get the cake?"));
        assertTrue(QuickChat.getSentMessages().contains("It is dinner time!"));
    }

    @Test
    public void testDisplayLongestMessage() {
        String msg1 = "Did you get the cake?";
        String msg2 = "Where are you? You are late! I have asked you to be on time.";
        String msg3 = "Yohoooo, I am at your gate.";
        String msg4 = "It is dinner time!";

        ArrayList<String> messages = new ArrayList<String>();
        messages.add(msg1);
        messages.add(msg2);
        messages.add(msg3);
        messages.add(msg4);

        String longest = "";
        for (int i = 0; i < messages.size(); i++) {
            String m = messages.get(i);
            if (m.length() > longest.length()) {
                longest = m;
            }
        }

        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    @Test
    public void testSearchByMessageID() {
        Message msg4 = new Message("5566778899", 3, "0838884567", "It is dinner time!");
        msg4.sentMessage(1);

        String searchID = "5566778899";
        String foundMessage = "";

        for (int i = 0; i < Message.getAllMessages().size(); i++) {
            Message m = Message.getAllMessages().get(i);
            if (m.getMessageID().equals(searchID)) {
                foundMessage = m.getMessageText();
                break;
            }
        }

        assertEquals("It is dinner time!", foundMessage);
    }

    @Test
    public void testSearchAllMessagesForRecipient() {
        Message msg2 = new Message("0987654321", 1, "+27838884567", 
                                   "Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage(3);

        Message msg5 = new Message("9998887776", 4, "+27838884567", "Ok, I am leaving without you.");
        msg5.sentMessage(3);

        String recipient = "+27838884567";
        ArrayList<String> foundMessages = new ArrayList<String>();

        ArrayList<Message> all = new ArrayList<Message>();

        for (int i = 0; i < Message.getAllMessages().size(); i++) {
            all.add(Message.getAllMessages().get(i));
        }

        for (int i = 0; i < Message.readStoredMessages().size(); i++) {
            all.add(Message.readStoredMessages().get(i));
        }

        for (int i = 0; i < all.size(); i++) {
            Message m = all.get(i);
            if (m.getRecipient().equals(recipient)) {
                foundMessages.add(m.getMessageText());
            }
        }

        assertTrue(foundMessages.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(foundMessages.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteByMessageHash() {
        Message msg2 = new Message("0987654321", 1, "+27838884567", 
                                   "Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage(3);

        String hashToDelete = msg2.getMessageHash();

        ArrayList<Message> stored = Message.readStoredMessages();
        String deletedMsg = "";
        boolean found = false;

        for (int i = 0; i < stored.size(); i++) {
            Message m = stored.get(i);
            if (m.getMessageHash().equals(hashToDelete)) {
                deletedMsg = m.getMessageText();
                found = true;
                break;
            }
        }

        assertTrue(found);
        assertEquals("Where are you? You are late! I have asked you to be on time.", deletedMsg);
        assertEquals("Message: 'Where are you? You are late! I have asked you to be on time' successfully deleted.",
                     "Message: '" + deletedMsg + "' successfully deleted.");
    }

    @Test
    public void testDisplayReport() {
        Message msg1 = new Message("1234567890", 0, "+27834557896", "Did you get the cake?");
        msg1.sentMessage(3);

        Message msg2 = new Message("0987654321", 1, "+27838884567", 
                                   "Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage(3);

        ArrayList<Message> stored = Message.readStoredMessages();

        assertFalse(stored.isEmpty());

        for (int i = 0; i < stored.size(); i++) {
            Message m = stored.get(i);
            assertNotNull(m.getMessageHash());
            assertNotNull(m.getRecipient());
            assertNotNull(m.getMessageText());
        }
    }

    @Test
    public void testArraysPopulatedCorrectly() {
        QuickChat.getMessageIDs().add("1234567890");
        QuickChat.getMessageHashes().add("12:0:HICAKE");
        QuickChat.getSentMessages().add("Did you get the cake?");

        QuickChat.getMessageIDs().add("0987654321");
        QuickChat.getMessageHashes().add("09:1:WHERETIME");
        QuickChat.getStoredMessages().add("Where are you? You are late! I have asked you to be on time.");

        assertEquals(2, QuickChat.getMessageIDs().size());
        assertEquals(2, QuickChat.getMessageHashes().size());
        assertEquals(1, QuickChat.getSentMessages().size());
        assertEquals(1, QuickChat.getStoredMessages().size());
    }
}