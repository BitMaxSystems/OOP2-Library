package org.bitmaxsystems.oop2library.models.inventory.states;

import org.bitmaxsystems.oop2library.models.inventory.Inventory;

public class AvailableInventoryState implements IInventoryState {
    private Inventory inventory;
    private InventoryStateEnum stateEnum = InventoryStateEnum.AVAILABLE;

    public AvailableInventoryState() {
    }

    public AvailableInventoryState(Inventory inventory)
    {
        this.inventory = inventory;
    }

    @Override
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public InventoryStateEnum getStateEnum() {
        return stateEnum;
    }

    @Override
    public void lendInside() {
         inventory.setState(new LentInsideInventoryState(inventory));
    }

    @Override
    public void lendOutside() {
        if (inventory.isArchived()) {
            throw new IllegalStateException("Archived books cannot be lent outside.");
        }

        inventory.setState(new LentOutsideInventoryState(inventory));
    }

    @Override
    public void returnBook() {
        throw new IllegalStateException("Book is already available");
    }

    @Override
    public String toString() {
        return "Available";
    }
}
