package com.mygdx.platformer.Sprites.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Player;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;
import com.mygdx.platformer.Sprites.Projectiles.RockProjectile;

public class RockItem extends Item {

    public RockItem(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        textureRegion = new TextureRegion(new Texture(Gdx.files.local("sprites/objects/environment/Rock_01.png")));
        setRegion(textureRegion);
        setBounds(getX(), getY(), Platformer.getTileMultiplier(1f), Platformer.getTileMultiplier(1f));

        isStackable = true;
    }

    @Override
    public void update(float deltaTime) {
        if(toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        } else {
            setPosition(body.getPosition().x - getWidth() / 2, (body.getPosition().y - getHeight() / 2)+0.25f);
            setRegion(textureRegion);
        }
    }

    @Override
    public void defineItem() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Platformer.getTileMultiplier(0.15f));
        fDef.filter.categoryBits = Platformer.ITEM_BIT;
        fDef.filter.maskBits = Platformer.GROUND_BIT | Platformer.COIN_BIT | Platformer.BRICK_BIT | Platformer.OBJECT_BIT | Platformer.PLAYER_BIT;

        fDef.shape = shape;
        body.createFixture(fDef).setUserData(this);
    }

    @Override
    public void use(Player player) {
        if(screen.addItemToInventory(this, Projectile.class)) {
            toDestroy = true;
        }
    }

}
