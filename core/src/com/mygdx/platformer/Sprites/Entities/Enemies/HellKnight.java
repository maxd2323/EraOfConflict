package com.mygdx.platformer.Sprites.Entities.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Player;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;
import com.mygdx.platformer.utils.Constants;

public class HellKnight extends Enemy {

    public HellKnight(PlayScreen screen, float x, float y) {
        super(
                screen,
                x,
                y,
                Constants.HELL_KNIGHT_STATS,
                "HellKnight"
        );

        setRegion((TextureRegion) animations.get("run").getKeyFrames()[0]);
        setBounds(getX(), getY(), Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
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
    public void onEnemyHit(Enemy enemy) {

    }

    @Override
    public void loadTexturesAndAnimations(PlayScreen screen) {
        TextureRegion hellKnightStand = new TextureRegion(new Texture("sprites/hell_knight/raw/Idle/0_Hell_Knight_Idle_000.png"));

        textures = new ArrayMap<>();
        textures.put("jump", hellKnightStand);
        textures.put("stand", hellKnightStand);

        Animation walk = new Animation(0.1f, screen.getAtlas("Hell Knight").findRegions("0_Hell_Knight_Walking"));
        Animation dead = new Animation(0.1f, screen.getAtlas("Hell Knight").findRegions("0_Hell_Knight_Dying"));
        Animation slash = new Animation(0.05f, screen.getAtlas("Hell Knight").findRegions("0_Hell_Knight_Slashing"));
        // Animation idle = new Animation(0.1f, screen.getAtlas("Hell Knight").findRegions("0_Hell_Knight_Idle"));

        animations = new ArrayMap<>();
        animations.put("run", walk);
        animations.put("dead", dead);
        animations.put("slash", slash);
        // animations.put("idle", idle);
    }
}
