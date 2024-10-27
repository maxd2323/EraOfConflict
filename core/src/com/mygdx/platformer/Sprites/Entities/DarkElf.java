package com.mygdx.platformer.Sprites.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.utils.Constants;

public class DarkElf extends Player {

    public DarkElf(PlayScreen screen, float x, float y) {
        super(
                screen,
                x,
                y,
                Constants.PLAYER_INITIAL_HEALTH,
                Constants.PLAYER_INITIAL_MAGICKA,
                Constants.PLAYER_INITIAL_SPEED,
                "DarkElf"
        );
        stateTimer = 0;

        currentState = State.STANDING;
        previousState = State.STANDING;

        setRegion((TextureRegion) animations.get("run").getKeyFrames()[0]);
        setBounds(getX(), getY(), Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
    }

    @Override
    public void loadTexturesAndAnimations(PlayScreen screen) {
        TextureRegion playerStand = new TextureRegion(new Texture("sprites/dark_elf/raw/Idle/0_Dark_Elves_Idle_000.png"));

        textures = new ArrayMap<>();
        textures.put("jump", playerStand);
        textures.put("stand", playerStand);

        Animation playerRun = new Animation(0.1f, screen.getAtlas("Dark Elf").findRegions("0_Dark_Elves_Running"));
        Animation playerDead = new Animation(0.1f, screen.getAtlas("Dark Elf").findRegions("0_Dark_Elves_Dying"));
        Animation playerThrow = new Animation(0.05f, screen.getAtlas("Dark Elf").findRegions("0_Dark_Elves_Throwing"));
        Animation playerHurt = new Animation(0.05f, screen.getAtlas("Dark Elf").findRegions("0_Dark_Elves_Hurt"));

        animations = new ArrayMap<>();
        animations.put("run", playerRun);
        animations.put("dead", playerDead);
        animations.put("throw", playerThrow);
        animations.put("hurt", playerHurt);
    }

}