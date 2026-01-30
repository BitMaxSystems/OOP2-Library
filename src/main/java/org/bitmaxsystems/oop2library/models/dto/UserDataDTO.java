package org.bitmaxsystems.oop2library.models.dto;

import org.bitmaxsystems.oop2library.models.users.User;

public class UserDataDTO {
    private String firstName;
    private String lastName;
    private String age;
    private String phoneField;
    private String loyaltyPoints;
    private String usernameField;
    private boolean newPassword;
    private String passwordField;
    private String repeatPasswordField;
    private User user;


    public static class Builder
    {
        private String firstName;
        private String lastName;
        private String age;
        private String phoneField;
        private String loyaltyPoints;
        private String usernameField;
        private boolean newPassword;
        private String passwordField;
        private String repeatPasswordField;
        private User user = null;

        public Builder(String firstName, String lastName, String age ,String phoneField, String usernameField)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.phoneField = phoneField;
            this.usernameField = usernameField;
            this.newPassword = false;
            this.loyaltyPoints = "50";
        }

        public Builder setLoyaltyPoints(String loyaltyPoints)
        {
            this.loyaltyPoints = loyaltyPoints;
            return this;
        }

        public Builder setNewPassword (String passwordField, String repeatPasswordField)
        {
            this.newPassword = true;
            this.passwordField = passwordField;
            this.repeatPasswordField = repeatPasswordField;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public UserDataDTO build()
        {
            return new UserDataDTO(firstName,
                                    lastName,
                                    age,
                                    phoneField,
                                    loyaltyPoints,
                                    usernameField,
                                    newPassword,
                                    passwordField,
                                    repeatPasswordField,
                                    user);
        }
    }
//    public UserDataDTO(String firstName, String lastName, String age, String phoneField, String usernameField, String passwordField, String repeatPasswordField) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.age = age;
//        this.phoneField = phoneField;
//        this.loyaltyPoints = "50";
//        this.usernameField = usernameField;
//        this.passwordField = passwordField;
//        this.repeatPasswordField = repeatPasswordField;
//    }
//
//    public UserDataDTO(String firstName, String lastName, String age, String phoneField, String loyaltyPoints, String usernameField, String passwordField, String repeatPasswordField) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.age = age;
//        this.phoneField = phoneField;
//        this.loyaltyPoints = loyaltyPoints;
//        this.usernameField = usernameField;
//        this.passwordField = passwordField;
//        this.repeatPasswordField = repeatPasswordField;
//    }


    private UserDataDTO(String firstName, String lastName, String age, String phoneField, String loyaltyPoints, String usernameField, boolean newPassword, String passwordField, String repeatPasswordField, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneField = phoneField;
        this.loyaltyPoints = loyaltyPoints;
        this.usernameField = usernameField;
        this.newPassword = newPassword;
        this.passwordField = passwordField;
        this.repeatPasswordField = repeatPasswordField;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isNewPassword() {
        return newPassword;
    }

    public String getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public String getRepeatPasswordField() {
        return repeatPasswordField;
    }

    public User getUser() {
        return user;
    }

    public String getPasswordField() {
        return passwordField;
    }

    public String getUsernameField() {
        return usernameField;
    }

    public String getPhoneField() {
        return phoneField;
    }

    public String getAge() {
        return age;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
}
