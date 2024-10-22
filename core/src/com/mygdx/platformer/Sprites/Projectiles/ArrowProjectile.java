package com.mygdx.platformer.Sprites.Projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;

public class ArrowProjectile extends Projectile {

    public ArrowProjectile(PlayScreen screen, float x, float y, Boolean shootRight, float damage) {
        super(
                new TextureRegion(new Texture(Gdx.files.local("sprites/devil_archer1/raw/Arrow.png"))),
                screen, x, y, shootRight, damage, new Vector2(75, 20)
        );
        b2body.setActive(true);
        setBounds(getX(), getY(), Platformer.getTileMultiplier(0.5f), Platformer.getTileMultiplier(0.5f));
    }

    @Override
    public void defineProjectile() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX()+0.1f, getY()+0.3f);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Platformer.getTileMultiplier(0.15f), Platformer.getTileMultiplier(0.15f));
        fDef.filter.categoryBits = Platformer.PROJECTILE_BIT;
        fDef.filter.maskBits = Platformer.GROUND_BIT | Platformer.PLAYER_BIT | Platformer.OBJECT_BIT;

        fDef.shape = shape;
        b2body.createFixture(fDef).setUserData(this);
        if(shootRight.equals(Boolean.TRUE)) {
            b2body.applyLinearImpulse(velocity.x, velocity.y, getX(), getY(), true);
        } else {
            b2body.applyLinearImpulse(velocity.x*-1, velocity.y, getX(), getY(), true);
        }
    }

    @Override
    public void update(float deltaTime) {
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            getTexture().dispose();
        } else if(!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(texture);
        }
    }
}
