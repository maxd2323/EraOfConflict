package com.mygdx.platformer.utils.DataStructures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.platformer.Sprites.Items.Item;

import java.util.Optional;

public class Inventory extends Array<Optional<InventoryItem>> {

    public int selectedIndex;
    public Inventory(int size) {
        super();
        for(int i = 0; i < size; i++) {
            this.add(Optional.empty());
        }
        selectedIndex = 0;
    }

    public void useSelected() {
        this.get(selectedIndex).get().decrement();
        if(this.getSelected().get().shouldDestroy) {
            this.set(selectedIndex, Optional.empty());
        }
    }
    public Optional<InventoryItem> getSelected() {
        return this.get(selectedIndex);
    }

    public boolean addIfSpace(Item obj, Class rootClass) {
        int index = getEmptyIndex(obj);
        if(index > -1) {
            if(obj.isStackable) {
                if(this.get(index).isPresent() && this.get(index).get().isArray()) {
                    Gdx.app.log("inventory add", "is present and is array");
                    InventoryItem<Array> inventoryArray = this.get(index).get();
                    inventoryArray.addToArray(obj);
                    this.set(index, Optional.of(inventoryArray));
                } else {
                    Gdx.app.log("inventory add", "create new array");
                    Array<Item> arr = new Array<>();
                    arr.add(obj);
                    this.set(index, Optional.of(new InventoryItem(arr, rootClass)));
                }
            } else {
                this.set(index, Optional.of(new InventoryItem(obj, rootClass)));
            }
            return true;
        } else {
            return false;
        }
    }

    public int getEmptyIndex() {
        for(int i = 0; i < size; i++) {
            if(!this.get(i).isPresent()) {
                return i;
            }
        }
        return -1;
    }

    public int getEmptyIndex(Item obj) {
        for(int i = 0; i < size; i++) {
            if(
                    this.get(i).isPresent()
                    && this.get(i).get().isArray()
                    && obj.getClass() == ((Array)this.get(i).get().getValue()).get(0).getClass()) {
                Gdx.app.log("inventory add", "all the is presents");
                return i;
            }
        }
        for(int i = 0; i < size; i++) {
            if(!this.get(i).isPresent()) {
                Gdx.app.log("inventory add", "empty space");
                return i;
            }
        }
        return -1;
    }

}
