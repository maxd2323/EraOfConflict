package com.mygdx.platformer.Sprites.Entities.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Player;
import com.mygdx.platformer.Sprites.Projectiles.ArrowProjectile;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;
import com.mygdx.platformer.utils.Constants;

public class DevilArcher extends Enemy {

    public DevilArcher(PlayScreen screen, float x, float y) {
        super(
                screen,
                x,
                y,
                Constants.DEVIL_ARCHER_STATS,
                "DevilArcher"
        );
        isAwake = true;

        setRegion((TextureRegion) animations.get("idle").getKeyFrames()[0]);
        setBounds(getX(), getY(), Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
    }

    public void shoot() {
        ArrowProjectile arrow = new ArrowProjectile(
                screen, getX(), getY(), facingRight, 10
        );
        screen.spawnProjectile(arrow);
    }

    @Override
    public void hit(Projectile projectile) {
        runHurtAnimation = true;
        applyDamage(projectile.damage);
    }

    @Override
    public void hitOnHead(Player player) {

    }

    @Override
    public void update(float deltaTime) {
        if(b2body.getPosition().y < -10 || currentHealth <= 0) {
            b2body.setActive(false);
            isDead = true;
        }
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        else if(!destroyed) {
            //b2body.setLinearVelocity(velocity);

            if(b2body.getLinearVelocity().y == 0 && shouldMove) {
                this.move();
            }

            if(shouldMove == false) {
                this.b2body.setLinearVelocity(0f, 0f);
            }

            if(isAwake) {
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
                setRegion(getFrame(deltaTime));
            }

//            if(Math.abs(playerPosition.x - b2body.getPosition().x) > 10) {
//                isAwake = false;
//            } else {
//                isAwake = true;
//            }
        }
    }

    @Override
    protected void attack() {
    }

    @Override
    public void onEnemyHit(Enemy enemy) {

    }

    @Override
    public void loadTexturesAndAnimations(PlayScreen screen) {
        TextureRegion playerStand = new TextureRegion(new Texture("sprites/dark_elf/raw/Idle/0_Dark_Elves_Idle_000.png"));

        textures = new ArrayMap<>();
        textures.put("jump", playerStand);
        textures.put("stand", playerStand);

        Animation run = new Animation(0.1f, screen.getAtlas("Devil Archer").findRegions("0_Archer_Running"));
        Animation dead = new Animation(0.1f, screen.getAtlas("Devil Archer").findRegions("0_Archer_Dying"));
        Animation _throw = new Animation(0.05f, screen.getAtlas("Devil Archer").findRegions("0_Archer_Throwing"));
        Animation idle = new Animation(0.1f, screen.getAtlas("Devil Archer").findRegions("0_Archer_Idle Blinking"));
        Animation hurt = new Animation(0.1f, screen.getAtlas("Devil Archer").findRegions("0_Archer_Hurt"));
        Animation shooting = new Animation(0.05f, screen.getAtlas("Devil Archer").findRegions("0_Archer_Shooting"));

        animations = new ArrayMap<>();
        animations.put("run", run);
        animations.put("dead", dead);
        animations.put("throw", _throw);
        animations.put("idle", idle);
        animations.put("hurt", hurt);
        animations.put("shoot", shooting);
    }
}
