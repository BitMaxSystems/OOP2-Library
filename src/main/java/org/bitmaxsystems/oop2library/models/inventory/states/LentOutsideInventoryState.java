package org.bitmaxsystems.oop2library.models.inventory.states;

import org.bitmaxsystems.oop2library.models.inventory.Inventory;

public class LentOutsideInventoryState implements IInventoryState {
    private Inventory inventory;
    private InventoryStateEnum stateEnum = InventoryStateEnum.LENT_OUTSIDE;

    public LentOutsideInventoryState() {
    }

    public LentOutsideInventoryState(Inventory inventory)
    {
        this.inventory = inventory;
    }

    @Override
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public InventoryStateEnum getStatusEnum() {
        return stateEnum;
    }

    @Override
    public void lendInside() {
        throw new IllegalStateException("Book is already lent");
    }

    @Override
    public void lendOutside() {
        throw new IllegalStateException("Book is already lent");
    }

    @Override
    public void returnBook() {
        inventory.setStatus(new AvailableInventoryState(inventory));
    }

    @Override
    public String toString() {
        return "Lent outside";
    }
}
