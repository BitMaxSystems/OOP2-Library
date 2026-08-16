package org.bitmaxsystems.oop2library.models.inventory.states;

public enum InventoryStateEnum {
    AVAILABLE("Available"),
    LENT_INSIDE("Lent inside"),
    LENT_OUTSIDE("Lent outside");

    private final String stateString;

    InventoryStateEnum(String stateString)
    {
        this.stateString = stateString;
    }

    public String toString() {
        return stateString;
    }
}
