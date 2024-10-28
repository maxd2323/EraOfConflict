package com.mygdx.platformer.Sprites.Entities.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;
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
    protected void attack() {
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
