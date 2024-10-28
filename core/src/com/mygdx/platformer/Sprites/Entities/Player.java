package com.mygdx.platformer.Sprites.Entities;

import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;

public abstract class Player extends Entity {
    public Player(
            PlayScreen screen,
            float x,
            float y,
            EntityStats entityStats
    ) {
        this(screen, x, y, entityStats, "Player");
    }

    public Player(
            PlayScreen screen,
            float x,
            float y,
            EntityStats entityStats,
            String entityTag
    ) {
        super(
                screen,
                x,
                y,
                entityStats,
                entityTag
        );

        setBounds(0, 0, Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
        setRegion(textures.get("stand"));
    }

    // Getters

    public void update(float deltaTime) {
        updatePositionAndBounds(deltaTime);
        handleDestruction();

        if (b2body.getLinearVelocity().y == 0 && shouldMove) {
            move();
        }
        if (!shouldMove) {
            b2body.setLinearVelocity(0f, 0f);
        }

        updateCombat(deltaTime);

        if (b2body.getPosition().y < -10 || currentHealth <= 0) {
            die();
        }
    }


    @Override
    protected void defineEntity() {
        defineCommonBody(
                Platformer.getTileMultiplier(0.375f),
                Platformer.PLAYER_BIT,
                (short) (Platformer.GROUND_BIT
                        | Platformer.COIN_BIT
                        | Platformer.BRICK_BIT
                        | Platformer.PLAYER_BIT
                        | Platformer.ENEMY_BIT
                        | Platformer.OBJECT_BIT
                        | Platformer.ENEMY_HEAD_BIT
                        | Platformer.ITEM_BIT
                        | Platformer.CHEST_BIT
                        | Platformer.PROJECTILE_BIT
                )
        );
    }
}
