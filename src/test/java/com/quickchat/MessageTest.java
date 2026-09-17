package com.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for Message class.
 * These tests use the exact test data from the POE brief.
 */
public class MessageTest {

    @BeforeEach
    public void setUp() {
        Message.reset();
    }

    @Test
    public void testMessageLengthOK() {
        Message msg = new Message("1234567890", 0, "+27718693002", 
                                  "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", msg.checkMessageLength());
    }

    @Test
    public void testMessageLengthExceeded() {
        String longMessage = "";
        for (int i = 0; i < 260; i++) {
            longMessage = longMessage + "A";
        }

        Message msg = new Message("1234567890", 0, "+27718693002", longMessage);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", msg.checkMessageLength());
    }

    @Test
    public void testRecipientCorrectlyFormatted() {
        Message msg = new Message("1234567890", 0, "+27718693002", "Hi Mike");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testRecipientIncorrectlyFormatted() {
        Message msg = new Message("1234567890", 0, "08575975889", "Hi Keegan");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                   + "Please correct the number and try again.", msg.checkRecipientCell());
    }

    @Test
    public void testMessageHashCorrect() {
        Message msg = new Message("0012345678", 0, "+27718693002", 
                                  "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", msg.getMessageHash());
    }

    @Test
    public void testMessageIDCreated() {
        String id = Message.generateMessageID();
        assertNotNull(id);
        assertEquals(10, id.length());

        Message msg = new Message(id, 0, "+27718693002", "Test");
        assertEquals("Message ID generated: " + id, "Message ID generated: " + msg.getMessageID());
    }

    @Test
    public void testMessageSentStatusSend() {
        Message msg = new Message("1234567890", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", msg.sentMessage(1));
        assertEquals("sent", msg.getStatus());
    }

    @Test
    public void testMessageSentStatusDisregard() {
        Message msg = new Message("1234567890", 0, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", msg.sentMessage(2));
        assertEquals("disregarded", msg.getStatus());
    }

    @Test
    public void testMessageSentStatusStore() {
        Message msg = new Message("1234567890", 0, "+27718693002", "Test message");
        assertEquals("Message successfully stored.", msg.sentMessage(3));
        assertEquals("stored", msg.getStatus());
    }

    @Test
    public void testCheckMessageIDValid() {
        Message msg = new Message("123456789", 0, "+27718693002", "Test");
        assertTrue(msg.checkMessageID());
    }

    @Test
    public void testCheckMessageIDInvalid() {
        Message msg = new Message("12345678901", 0, "+27718693002", "Test");
        assertFalse(msg.checkMessageID());
    }

    @Test
    public void testReturnTotalMessages() {
        Message msg1 = new Message("1234567890", 0, "+27718693002", "Hi Mike");
        msg1.sentMessage(1);

        Message msg2 = new Message("0987654321", 1, "+27718693002", "Hi again");
        msg2.sentMessage(1);

        assertEquals(2, msg2.returnTotalMessages());
    }

    @Test
    public void testPrintMessages() {
        Message msg = new Message("1234567890", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        msg.sentMessage(1);

        String printed = msg.printMessages();
        assertTrue(printed.contains("1234567890"));
        assertTrue(printed.contains("Hi Mike"));
    }
}