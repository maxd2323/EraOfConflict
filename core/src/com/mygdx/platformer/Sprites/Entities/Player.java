package com.mygdx.platformer.Sprites.Entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;

public class Player extends Entity {

    // Other
    public int jumpCount;


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

        attackCooldown = 5f;
        facingRight = true;

        setBounds(0, 0, Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
        setRegion(textures.get("stand"));
    }

    // Getters

    public void update(float deltaTime) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 8 / Platformer.PPM);
        setRegion(getFrame(deltaTime));
        incrementMagicka(1f);

        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }

        if(b2body.getLinearVelocity().y == 0 && shouldMove) {
            move();
        }

        if(shouldMove == false) {
            this.b2body.setLinearVelocity(0f, 0f);
        }

        if(b2body.getPosition().y < -10 || currentHealth <= 0) {
            die();
        }
        if(currentState == State.STANDING || currentState == State.RUNNING) {
            jumpCount = 0;
        }

        if (inCombat && !runAttackAnimation) {
            timeSinceLastAttack += deltaTime;
            if (timeSinceLastAttack >= attackCooldown) {
                attack();
                timeSinceLastAttack = 0f;
            }
            if (opponent.isDead) {
                inCombat = false;
            }
        }
    }

    @Override
    protected void defineEntity() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(Platformer.getTileMultiplier(2f), Platformer.getTileMultiplier(2f));
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Platformer.getTileMultiplier(0.375f));
        fDef.filter.categoryBits = Platformer.PLAYER_BIT;
        fDef.filter.maskBits = Platformer.GROUND_BIT
                | Platformer.COIN_BIT
                | Platformer.BRICK_BIT
                | Platformer.PLAYER_BIT
                | Platformer.ENEMY_BIT
                | Platformer.OBJECT_BIT
                | Platformer.ENEMY_HEAD_BIT
                | Platformer.ITEM_BIT
                | Platformer.CHEST_BIT
                | Platformer.PROJECTILE_BIT;

        fDef.shape = shape;
        b2body.createFixture(fDef).setUserData(this);
    }

    @Override
    public void loadTexturesAndAnimations(PlayScreen screen) {
    }

}
