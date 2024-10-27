package com.mygdx.platformer.Sprites.Entities.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Entity;
import com.mygdx.platformer.Sprites.Entities.Player;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;

public abstract class Enemy extends Entity {

    protected PlayScreen screen;
    protected World world;
    public Body b2body;
    public Vector2 velocity;
    public float collisionDamage;

    public Enemy(
            PlayScreen screen,
            float x,
            float y,
            float health,
            float magicka,
            float speed
    ) {
        super(
                screen,
                health,
                magicka,
                speed,
                "Enemy"
        );
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1, 0);
        b2body.setActive(true);
        collisionDamage = 10f;
    }

    public Enemy(
            PlayScreen screen,
            float x,
            float y,
            float health,
            float magicka,
            float speed,
            String entityTag
    ) {
        super(
                screen,
                health,
                magicka,
                speed,
                entityTag
        );
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1, 0);
        b2body.setActive(true);
        collisionDamage = 10f;
    }

    public void moveLeft() {
        this.b2body.setLinearVelocity(new Vector2(-1.5f, 0));
    }

    public void reverseVelocity(boolean x, boolean y) {
        if(x) velocity.x = -velocity.x;
        if(y) velocity.y = -velocity.y;
    }

    protected abstract void defineEnemy();
    public abstract void hitOnHead(Player player);
    public abstract void update(float deltaTime);
    public abstract void onEnemyHit(Enemy enemy);
    public abstract void loadTexturesAndAnimations(PlayScreen screen);
    public abstract void hit(Projectile projectile);
}
