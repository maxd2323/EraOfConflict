package com.mygdx.platformer.Sprites.Items;

import com.mygdx.platformer.Screens.PlayScreen;

public abstract class Food extends Item{

    public int healthIncrease;
    public Food(PlayScreen screen, float x, float y, int healthIncrease) {
        super(screen, x, y);
        this.healthIncrease = healthIncrease;
    }
}
