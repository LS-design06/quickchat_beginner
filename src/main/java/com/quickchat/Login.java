package com.quickchat;

/**
 * Login class for QuickChat.
 * This is for Part 1 of the PROG5121 PoE.
 */
public class Login {

    // variables for the user details
    private String username;
    private String password;
    private String cellPhone;
    private String firstName;
    private String lastName;

    // these are static so they dont get lost when we make a new Login object
    private static String savedUsername;
    private static String savedPassword;
    private static String savedFirstName;
    private static String savedLastName;

    // empty constructor 
    public Login() {
    }

    // constructor with all the fields
    public Login(String username, String password, String cellPhone, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.cellPhone = cellPhone;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * This method checks if the username is correct.
     * It needs an underscore and must be 5 characters or less.
     */
    public boolean checkUserName() {
        if (username == null) {
            return false;
        }

        boolean hasUnderscore = false;

        // check each character to see if there is an underscore
        for (int i = 0; i < username.length(); i++) {
            if (username.charAt(i) == '_') {
                hasUnderscore = true;
            }
        }

        // check length and underscore
        if (hasUnderscore == true && username.length() <= 5) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method checks if the password is strong enough.
     * It needs at least 8 characters, a capital letter, a number and a special character.
     */
    public boolean checkPasswordComplexity() {
        if (password == null) {
            return false;
        }

        if (password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        // go through each character in the password
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (c >= 'A' && c <= 'Z') {
                hasCapital = true;
            } else if (c >= '0' && c <= '9') {
                hasNumber = true;
            } else if (!(c >= 'a' && c <= 'z') && !(c >= 'A' && c <= 'Z') && !(c >= '0' && c <= '9')) {
                hasSpecial = true;
            }
        }

        if (hasCapital == true && hasNumber == true && hasSpecial == true) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method checks the cell phone number.
     */
    public boolean checkCellPhoneNumber() {
        if (cellPhone == null) {
            return false;
        }

    
        String pattern = "^\\+\\d{1,9}$";

        if (cellPhone.matches(pattern) && cellPhone.length() <= 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method registers the user.
     * It checks all the validations and returns the right message.
     */
    public String registerUser() {
        // first check username 
        boolean usernameOk = checkUserName();
        if (usernameOk == false) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        // then check password
        boolean passwordOk = checkPasswordComplexity();
        if (passwordOk == false) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        // then check cell phone
        boolean cellOk = checkCellPhoneNumber();
        if (cellOk == false) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        // if everything is ok, save the user details
        savedUsername = username;
        savedPassword = password;
        savedFirstName = firstName;
        savedLastName = lastName;

        return "Username successfully captured.";
    }

    /**
     * This method checks if the login details are correct.
     */
    public boolean loginUser() {
        if (username == null || password == null) {
            return false;
        }

        if (username.equals(savedUsername) && password.equals(savedPassword)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method returns the login message.
     */
    public String returnLoginStatus() {
        boolean loggedIn = loginUser();

        if (loggedIn == true) {
            return "Welcome " + savedFirstName + ", " + savedLastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    // static getters for testing
    public static String getSavedUsername() {
        return savedUsername;
    }

    public static String getSavedPassword() {
        return savedPassword;
    }

    public static String getSavedFirstName() {
        return savedFirstName;
    }

    public static String getSavedLastName() {
        return savedLastName;
    }

    // reset method for tests
    public static void reset() {
        savedUsername = null;
        savedPassword = null;
        savedFirstName = null;
        savedLastName = null;
    }
}
