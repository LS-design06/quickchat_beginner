package com.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for Login class.
 * These tests use the exact test data from the POE brief.
 */
public class LoginTest {

    private Login login;

    @BeforeEach
    public void setUp() {
        Login.reset();
        login = new Login();
    }

    // ========== assertEquals Tests ==========

    @Test
    public void testUsernameCorrectlyFormatted() {
        login.setUsername("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhone("+27838968976");
        login.setFirstName("Kyle");
        login.setLastName("Smith");

        String result = login.registerUser();
        assertEquals("Username successfully captured.", result);
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        login.setUsername("kyle!!!!!!");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhone("+27838968976");

        String result = login.registerUser();
        assertEquals("Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.", result);
    }

    @Test
    public void testPasswordMeetsComplexity() {
        login.setUsername("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhone("+27838968976");
        login.setFirstName("Kyle");
        login.setLastName("Smith");

        String result = login.registerUser();
        assertEquals("Username successfully captured.", result);

        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        login.setUsername("kyl_1");
        login.setPassword("password");

        String result = login.registerUser();
        assertEquals("Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, and a special character.", result);
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        login.setUsername("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhone("+27838968976");
        login.setFirstName("Kyle");
        login.setLastName("Smith");

        String result = login.registerUser();
        assertEquals("Username successfully captured.", result);
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        login.setUsername("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhone("08966553");

        String result = login.registerUser();
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.", result);
    }

    // ========== assertTrue / assertFalse Tests ==========

    @Test
    public void testLoginSuccessful() {
        login.setUsername("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhone("+27838968976");
        login.setFirstName("Kyle");
        login.setLastName("Smith");
        login.registerUser();

        Login loginAttempt = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertTrue(loginAttempt.loginUser());
    }

    @Test
    public void testLoginFailed() {
        Login loginAttempt = new Login("wrong", "wrong", "+27838968976", "Kyle", "Smith");
        assertFalse(loginAttempt.loginUser());
    }

    @Test
    public void testCheckUserNameTrue() {
        login.setUsername("kyl_1");
        assertTrue(login.checkUserName());
    }

    @Test
    public void testCheckUserNameFalse() {
        login.setUsername("kyle!!!!!!");
        assertFalse(login.checkUserName());
    }

    @Test
    public void testCheckPasswordComplexityTrue() {
        login.setPassword("Ch&&sec@ke99!");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testCheckPasswordComplexityFalse() {
        login.setPassword("password");
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testCheckCellPhoneNumberTrue() {
        login.setCellPhone("+27838968976");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testCheckCellPhoneNumberFalse() {
        login.setCellPhone("08966553");
        assertFalse(login.checkCellPhoneNumber());
    }

    @Test
    public void testReturnLoginStatusSuccess() {
        login.setUsername("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhone("+27838968976");
        login.setFirstName("Kyle");
        login.setLastName("Smith");
        login.registerUser();

        Login attempt = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals("Welcome Kyle, Smith it is great to see you again.", attempt.returnLoginStatus());
    }

    @Test
    public void testReturnLoginStatusFailure() {
        Login attempt = new Login("wrong", "wrong", "+27838968976", "Kyle", "Smith");
        assertEquals("Username or password incorrect, please try again.", attempt.returnLoginStatus());
    }
}