package com.mygdx.platformer.Sprites.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class HealthBar extends Image {

    private float totalHealth;
    private float currentHealth;

    public HealthBar(float x, float y, float currentHealth, float totalHealth) {
        super(
                new TextureRegion(new Texture(Gdx.files.local("ui/health1.png")))
        );
        setPosition(x, y);
        setBounds(getX(), getY(), 68, 27);
        this.totalHealth = totalHealth;
        this.currentHealth = currentHealth;
    }
}
