package com.mygdx.platformer.Sprites.Entities.Enemies;

import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Entity;
import com.mygdx.platformer.Sprites.Entities.EntityStats;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;

public abstract class Enemy extends Entity {

    public Enemy(
            PlayScreen screen,
            float x,
            float y,
            EntityStats entityStats
    ) {
        this(screen, x, y, entityStats, "Enemy");
    }

    public Enemy(
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
        facingRight = false;

        setBounds(0, 0, Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
    }

    public void update(float deltaTime) {
        updatePositionAndBounds(deltaTime);
        incrementMagicka(1f);  // Player-specific logic

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
                Platformer.ENEMY_BIT,
                (short) (Platformer.GROUND_BIT
                        | Platformer.COIN_BIT
                        | Platformer.BRICK_BIT
                        | Platformer.ENEMY_BIT
                        | Platformer.OBJECT_BIT
                        | Platformer.PLAYER_BIT
                        | Platformer.PROJECTILE_BIT)
        );
    }
    public abstract void hit(Projectile projectile);
}
