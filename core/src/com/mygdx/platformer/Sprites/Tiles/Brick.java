package com.mygdx.platformer.Sprites.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.Hud;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Player;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Platformer.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Player player) {
        Platformer.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }


}
