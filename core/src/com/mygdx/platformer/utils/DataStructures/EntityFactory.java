package com.mygdx.platformer.utils.DataStructures;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.DarkElf;
import com.mygdx.platformer.Sprites.Entities.Enemies.DevilArcher;
import com.mygdx.platformer.Sprites.Entities.Enemies.HellKnight;
import com.mygdx.platformer.Sprites.Entities.Enemies.Succubus;
import com.mygdx.platformer.Sprites.Entities.Entity;
import com.mygdx.platformer.Sprites.Entities.EntityStats;

public class EntityFactory {
    private static EntityStats darkElfStats = new EntityStats(50f, 100f, 5f, 5f, 1f, 30f);
    private static EntityStats hellKnightStats = new EntityStats(50f, 10f, 5f, 5f, 1f, 30f);
    private static EntityStats succubusStats = new EntityStats(50f, 10f, 5f, 5f, 1f, 30f);
    private static EntityStats devilArcher = new EntityStats(50f, 10f, 5f, 5f, 1f, 30f);

    public static void upgradeDarkElfStats(float healthIncrease, float magickaIncrease, float speedIncrease, float damageIncrease, float attackCooldown, float spawnCost) {
        darkElfStats.applyUpgrade(healthIncrease, magickaIncrease, speedIncrease, damageIncrease, attackCooldown, spawnCost);
    }

    public static void upgradeHellKnightStats(float healthIncrease, float magickaIncrease, float speedIncrease, float damageIncrease, float attackCooldown, float spawnCost) {
        hellKnightStats.applyUpgrade(healthIncrease, magickaIncrease, speedIncrease, damageIncrease, attackCooldown, spawnCost);
    }

    public static Entity createEntity(Class entityClass, PlayScreen screen, float x, float y) {
        if (entityClass == HellKnight.class) {
            return new HellKnight(screen, x, y, new EntityStats(hellKnightStats));
        } else if (entityClass == DarkElf.class) {
            return new DarkElf(screen, x, y, new EntityStats(darkElfStats));
        } else if (entityClass == Succubus.class) {
            return new Succubus(screen, x, y, new EntityStats(succubusStats));
        } else if (entityClass == DevilArcher.class) {
            return new DevilArcher(screen, x, y, new EntityStats(devilArcher));
        }
        // Add other entity classes as needed
        return null;
    }
}

