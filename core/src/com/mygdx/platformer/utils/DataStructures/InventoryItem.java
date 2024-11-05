package com.mygdx.platformer.utils.DataStructures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Entity;
import com.mygdx.platformer.Sprites.Entities.Player;
import com.mygdx.platformer.Sprites.Items.Item;

public class InventoryItem<T> {
    private T value;
    public Class rootClass;
    public boolean shouldDestroy;
    public InventoryItem(T item, Class rootClass) {
        shouldDestroy = false;
        value = item;
        this.rootClass = rootClass;
    }

    public InventoryItem(Class rootClass) {
        shouldDestroy = false;
        value = null;
        this.rootClass = rootClass;
    }

    public TextureRegion getTextureRegion() {
        if (rootClass.isAssignableFrom(Player.class)) {
            return Player.textureRegion;
        }
        if(isArray()) {
            return ((Item) (((Array) value).get(0))).textureRegion;
        } else {
            return ((Item) value).textureRegion;
        }
    }

    public T getValue() {
        return value;
    }

    public void decrement() {
        if(isArray()) {
            ((Array) value).pop();
            if(((Array<?>) value).size < 1) {
                shouldDestroy = true;
            }
        } else {
            shouldDestroy = true;
        }
    }
    public int getSize() {
        if(isArray()) {
            return ((Array) value).size;
        } else {
            return 1;
        }
    }

    public void addToArray(Item item) {
        ((Array) value).add(item);
    }

    public boolean isArray() {
        return Array.class.isInstance(value);
    }
    public Class getItemClass() {
        if(Array.class.isInstance(value)) {
            return ((Array) value).get(0).getClass();
        } else {
            return value.getClass();
        }
    }

    public Entity spawnEntity(PlayScreen screen, float x, float y) {
        return EntityFactory.createEntity(rootClass, screen, x, y);
    }

}
