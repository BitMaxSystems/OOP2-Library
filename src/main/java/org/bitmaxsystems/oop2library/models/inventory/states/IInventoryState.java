package org.bitmaxsystems.oop2library.models.inventory.states;


import org.bitmaxsystems.oop2library.models.inventory.Inventory;

public interface IInventoryState {

    InventoryStateEnum getStateEnum();

    void lendInside();

    void lendOutside();

    void returnBook();

    void setInventory(Inventory inventory);
}