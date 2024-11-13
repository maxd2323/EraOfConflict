package com.mygdx.platformer.Sprites.Entities.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.EntityStats;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;
import com.mygdx.platformer.utils.Constants;

public class HellKnight extends Enemy {

    public HellKnight(PlayScreen screen, float x, float y, EntityStats entityStats) {
        super(
                screen,
                x,
                y,
                entityStats,
                "HellKnight",
                false
        );
    }

    @Override
    public void hit(Projectile projectile) {
    }

    @Override
    public void loadTexturesAndAnimations(PlayScreen screen) {
        TextureRegion hellKnightStand = new TextureRegion(new Texture("sprites/hell_knight/raw/Idle/0_Hell_Knight_Idle_000.png"));

        textures = new ArrayMap<>();
        textures.put("jump", hellKnightStand);
        textures.put("stand", hellKnightStand);

        Animation walk = new Animation(0.1f, screen.getAtlas("Hell Knight").findRegions("0_Hell_Knight_Walking"));
        Animation dead = new Animation(0.05f, screen.getAtlas("Hell Knight").findRegions("0_Hell_Knight_Dying"));
        Animation slash = new Animation(0.05f, screen.getAtlas("Hell Knight").findRegions("0_Hell_Knight_Slashing"));
        // Animation idle = new Animation(0.1f, screen.getAtlas("Hell Knight").findRegions("0_Hell_Knight_Idle"));

        animations = new ArrayMap<>();
        animations.put("run", walk);
        animations.put("dead", dead);
        animations.put("slash", slash);
        // animations.put("idle", idle);
    }
}
