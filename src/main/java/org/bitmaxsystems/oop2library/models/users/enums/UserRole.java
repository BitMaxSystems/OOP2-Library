package org.bitmaxsystems.oop2library.models.users.enums;

public enum UserRole {
    READER("Reader"),
    UNAPPROVED_READER("Unapproved reader"),
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
