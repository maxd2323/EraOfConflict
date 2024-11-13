package com.mygdx.platformer.Sprites.Entities.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.EntityStats;
import com.mygdx.platformer.Sprites.Entities.Player;

public class Succubus extends Player {
    public static TextureRegion textureRegion = new TextureRegion(new Texture("sprites/succubus/raw/Idle/0_Banshee_Idle_000.png"));;

    public Succubus(PlayScreen screen, float x, float y, EntityStats entityStats) {
        super(
                screen,
                x,
                y,
                entityStats,
                "Succubus",
                false
        );
    }

    @Override
    public void loadTexturesAndAnimations(PlayScreen screen) {
        TextureRegion playerStand = new TextureRegion(new Texture("sprites/succubus/raw/Idle/0_Banshee_Idle_000.png"));

        textures = new ArrayMap<>();
        textures.put("jump", playerStand);
        textures.put("stand", playerStand);

        Animation playerRun = new Animation(0.1f, screen.getAtlas("Succubus").findRegions("0_Banshee_Running"));
        Animation playerDead = new Animation(0.05f, screen.getAtlas("Succubus").findRegions("0_Banshee_Dying"));
        Animation playerThrow = new Animation(0.05f, screen.getAtlas("Succubus").findRegions("0_Banshee_Throwing"));
        Animation playerHurt = new Animation(0.05f, screen.getAtlas("Succubus").findRegions("0_Banshee_Hurt"));
        Animation playerSlash = new Animation(0.05f, screen.getAtlas("Succubus").findRegions("0_Banshee_Throwing"));
        Animation test = new Animation(0.1f, screen.loadTextureRegionsFromFolder("sprites/succubus/raw/Running"));

        animations = new ArrayMap<>();
        animations.put("run", playerRun);
        animations.put("dead", playerDead);
        animations.put("throw", playerThrow);
        animations.put("hurt", playerHurt);
        animations.put("slash", playerSlash);
    }
}
