package com.mygdx.platformer.utils.DataStructures;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.DarkElf;
import com.mygdx.platformer.Sprites.Entities.Enemies.HellKnight;
import com.mygdx.platformer.Sprites.Entities.Entity;
import com.mygdx.platformer.Sprites.Entities.EntityStats;

public class EntityFactory {
    public static Entity createEntity(Class entityClass, PlayScreen screen, float x, float y) {
        if (entityClass == HellKnight.class) {
            return new HellKnight(screen, x, y);
        } else if (entityClass == DarkElf.class) {
            return new DarkElf(screen, x, y);
        }
        // Add other entity classes as needed
        return null;
    }
}

