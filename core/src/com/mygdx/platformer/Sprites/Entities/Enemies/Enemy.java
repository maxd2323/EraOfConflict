package com.mygdx.platformer.Sprites.Entities.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Entity;
import com.mygdx.platformer.Sprites.Entities.EntityStats;
import com.mygdx.platformer.Sprites.Entities.Player;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;

public abstract class Enemy extends Entity {
    protected boolean isAwake;

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
        isAwake = true;
    }

    public void update(float deltaTime) {
        stateTimer += deltaTime;
        if(b2body.getPosition().y < -10 || currentHealth <= 0) {
            die();
        }
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        else if(!destroyed) {
            if(b2body.getLinearVelocity().y == 0 && shouldMove) {
                move();
            }

            if(shouldMove == false) {
                this.b2body.setLinearVelocity(0f, 0f);
            }

            if(isAwake) {
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
                setRegion(getFrame(deltaTime));
            }
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
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Platformer.getTileMultiplier(0.375f));
        fDef.filter.categoryBits = Platformer.ENEMY_BIT;
        fDef.filter.maskBits = Platformer.GROUND_BIT | Platformer.COIN_BIT | Platformer.BRICK_BIT | Platformer.ENEMY_BIT | Platformer.OBJECT_BIT | Platformer.PLAYER_BIT | Platformer.PROJECTILE_BIT;

        fDef.shape = shape;
        b2body.createFixture(fDef).setUserData(this);
    }

    public abstract void hitOnHead(Player player);
    public abstract void onEnemyHit(Enemy enemy);
    public abstract void loadTexturesAndAnimations(PlayScreen screen);
    public abstract void hit(Projectile projectile);
}
