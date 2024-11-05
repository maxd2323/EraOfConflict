package com.mygdx.platformer.utils.DataStructures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Entity;
import com.mygdx.platformer.Sprites.Entities.Player;
import com.mygdx.platformer.Sprites.Items.Item;

import java.util.Optional;

public class EntityInventory extends Inventory {

    public EntityInventory(int size) {
        super(size);
    }

    public Player spawnSelectedInventorySpace(int i, PlayScreen screen, float x, float y) {
        if (this.get(i).isPresent()) {
            return (Player) this.get(i).get().spawnEntity(screen, x, y);
        }
        return null;
    }

    public boolean addIfSpace(final Class rootClass) {
        int index = getEmptyIndex();

        this.set(index, Optional.of(new InventoryItem(rootClass)));
        return true;
    }

}
