package com.mygdx.platformer.Sprites.Projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;

public class RockProjectile extends Projectile {

    public RockProjectile(PlayScreen screen, float x, float y, Boolean shootRight) {
        super(
                new TextureRegion(new Texture(Gdx.files.internal("sprites/objects/Rock_05.png")), 64, 64),
                screen,
                x,
                y,
                shootRight,
                25f,
                new Vector2(10, 20)
        );
        b2body.setActive(true);
        setBounds(getX(), getY(), Platformer.getTileMultiplier(0.5f), Platformer.getTileMultiplier(0.5f));
    }

    @Override
    public void defineProjectile(){
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX()+0.1f, getY()+0.3f);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Platformer.getTileMultiplier(0.15f));
        fDef.filter.categoryBits = Platformer.PROJECTILE_BIT;
        fDef.filter.maskBits = Platformer.GROUND_BIT | Platformer.ENEMY_BIT | Platformer.OBJECT_BIT;

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
