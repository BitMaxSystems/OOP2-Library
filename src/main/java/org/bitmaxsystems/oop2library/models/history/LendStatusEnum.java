package org.bitmaxsystems.oop2library.models.history;

public enum LendStatusEnum {
    IN_TIME("In time"),
    RETURNED("Returned"),
    OVERDUE("Overdue"),
    RETURNED_OVERDUE("Returned (Overdue)");

    private final String status;

    LendStatusEnum (String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
