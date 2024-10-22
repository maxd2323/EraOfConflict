package com.mygdx.platformer.Sprites.Projectiles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Enemies.Enemy;
import com.mygdx.platformer.Sprites.Entities.Player;

public abstract class Projectile extends Sprite {

    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected Boolean shootRight;
    protected boolean setToDestroy;
    protected boolean destroyed;
    protected TextureRegion texture;
    public Body b2body;
    public float damage;

    public Projectile(
            TextureRegion textureRegion,
            PlayScreen screen,
            float x,
            float y,
            Boolean shootRight,
            float damage,
            Vector2 velocity
    ) {
        super(textureRegion);
        this.world = screen.getWorld();
        this.screen = screen;
        this.shootRight = shootRight;
        this.damage = damage;
        this.texture = textureRegion;
        this.velocity = velocity;
        destroyed = false;
        setToDestroy = false;
        setRegion(this.texture);
        setPosition(x, y);
        defineProjectile();
    }

    public void collideWithEnemy(Enemy enemy) {
        enemy.hit(this);
        setToDestroy();
    }
    public void collideWithPlayer(Player player) {
        player.applyDamage(this.damage);
        setToDestroy();
    }
    public void setToDestroy() {
        setToDestroy = true;
    }
    public boolean isDestroyed() {
        return destroyed;
    }
    public abstract void defineProjectile();
    public abstract void update(float deltaTime);
}
