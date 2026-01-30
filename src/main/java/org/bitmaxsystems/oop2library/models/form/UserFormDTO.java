package org.bitmaxsystems.oop2library.models.form;

import org.bitmaxsystems.oop2library.models.users.User;

public class UserFormDTO {
    private String firstName;
    private String lastName;
    private String age;
    private String phoneField;
    private String usernameField;
    private String passwordField;
    private String repeatPasswordField;
    private User user;
    private UserForm form;

    public UserFormDTO(String firstName, String lastName, String age, String phoneField, String usernameField, String passwordField, String repeatPasswordField) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneField = phoneField;
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.repeatPasswordField = repeatPasswordField;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setForm(UserForm form) {
        this.form = form;
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
