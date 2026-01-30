package org.bitmaxsystems.oop2library.models.users.enums;

public enum UserRole {
    BASIC_USER("User"),
    UNAPPROVED_USER("Unapproved user"),
    LIBRARIAN("Librarian"),
    ADMINISTRATOR("Administrator");

    private String roleName;

    UserRole (String roleName)
    {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
