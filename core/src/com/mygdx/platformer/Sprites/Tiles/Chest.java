package com.mygdx.platformer.Sprites.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.Hud;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Player;

public class Chest extends Sprite {

    private boolean isOpen;
    private TextureRegion closedTexture;
    private TextureRegion openTexture;
    private Body b2body;
    private FixtureDef fDef;
    private World world;
    private int value;
    private boolean shouldDestroy;
    public Chest(PlayScreen screen, float x, float y, int value) {
        isOpen = false;
        this.value = value;
        closedTexture = new TextureRegion(new Texture(Gdx.files.local("sprites/chest1/Chest_01_Locked.png")), 128, 128);
        openTexture = new TextureRegion(new Texture(Gdx.files.local("sprites/chest1/Chest_01_Unlocked.png")), 128, 128);

        world = screen.getWorld();

        setPosition(x, y);
        setBounds(x, y, Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
        setRegion(closedTexture);
        defineChest();

        b2body.setActive(true);
    }

    public void defineChest() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        fDef = new FixtureDef();
        fDef.restitution = 0;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Platformer.getTileMultiplier(0.375f), Platformer.getTileMultiplier(0.375f));

        fDef.shape = shape;
        fDef.filter.categoryBits = Platformer.CHEST_BIT;
        fDef.filter.maskBits = Platformer.GROUND_BIT | Platformer.COIN_BIT | Platformer.BRICK_BIT | Platformer.ENEMY_BIT | Platformer.OBJECT_BIT | Platformer.PLAYER_BIT | Platformer.PROJECTILE_BIT;
        b2body.createFixture(fDef).setUserData(this);
    }

    public void update() {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 8 / Platformer.PPM);
        if(isOpen) {
            setRegion(openTexture);
        } else {
            setRegion(closedTexture);
        }
    }

    public void collideWithPlayer(Player player) {
        if(!isOpen) {
            Gdx.app.log("chest", "collision");
            isOpen = true;
            player.incrementCoins(value);
        }
    }
}
