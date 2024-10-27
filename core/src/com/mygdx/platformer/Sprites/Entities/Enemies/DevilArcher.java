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
import com.mygdx.platformer.Sprites.Projectiles.ArrowProjectile;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;
import com.mygdx.platformer.utils.Constants;

public class DevilArcher extends Enemy {

    // Boolean flags
    private boolean isAwake;
    private boolean runHurtAnimation;
    private boolean runShootingAnimation;
    public boolean facingRight;

    private float stateTimer;

    public DevilArcher(PlayScreen screen, float x, float y) {
        super(
                screen,
                x,
                y,
                Constants.DEVIL_ARCHER_INITIAL_HEALTH,
                Constants.DEVIL_ARCHER_INITIAL_MAGICKA,
                Constants.DEVIL_ARCHER_INITIAL_SPEED
        );
        stateTimer = 0;

        currentState = State.STANDING;
        previousState = State.STANDING;
        runHurtAnimation = false;
        facingRight = true;
        isDead = false;
        isAwake = true;

        setRegion((TextureRegion) animations.get("idle").getKeyFrames()[0]);
        setBounds(getX(), getY(), Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
    }

    public TextureRegion getFrame(float deltaTime) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region = (TextureRegion) animations.get("dead").getKeyFrame(stateTimer, false);
                if(animations.get("dead").isAnimationFinished(stateTimer) && stateTimer > 5){
                    setToDestroy = true;
                }
                break;
            case SHOOTING:
                region = (TextureRegion) animations.get("shoot").getKeyFrame(stateTimer, false);
                if(animations.get("shoot").isAnimationFinished(stateTimer) && stateTimer > 5){
                    runShootingAnimation = false;
                }
                break;
            case RUNNING:
                region = (TextureRegion) animations.get("run").getKeyFrame(stateTimer, true);
                break;
            case HURT:
                region = (TextureRegion) animations.get("hurt").getKeyFrame(stateTimer);
                if(facingRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if(!facingRight && region.isFlipX()) {
                    region.flip(true, false);
                }
                if(animations.get("hurt").isAnimationFinished(stateTimer)){
                    runHurtAnimation = false;
                }
                break;
            case FALLING:
            case STANDING:
            default:
                region = (TextureRegion) animations.get("idle").getKeyFrame(stateTimer, true);
                if(stateTimer > 5) {
                    shoot();
                }
                break;
        }
        if((b2body.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX()) {
            region.flip(true, false);
            facingRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || facingRight) && region.isFlipX()) {
            region.flip(true, false);
            facingRight = true;
        }
        stateTimer = (currentState == previousState) && isAwake ? stateTimer + deltaTime : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if(isDead) {
            return State.DEAD;
        } else if(runShootingAnimation) {
            return State.SHOOTING;
        }
        else if(runHurtAnimation) {
            return State.HURT;
        }
        else if(b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        }
        else if(b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        }
        else {
            return State.STANDING;
        }
    }

    public void shoot() {
        ArrowProjectile arrow = new ArrowProjectile(
                screen, getX(), getY(), facingRight, 10
        );
        screen.spawnProjectile(arrow);
        runShootingAnimation = true;
    }

    @Override
    protected void defineEnemy() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Platformer.getTileMultiplier(0.375f));
        fDef.filter.categoryBits = Platformer.ENEMY_BIT;
        fDef.filter.maskBits = Platformer.GROUND_BIT | Platformer.COIN_BIT | Platformer.BRICK_BIT | Platformer.ENEMY_BIT | Platformer.OBJECT_BIT | Platformer.PLAYER_BIT | Platformer.PROJECTILE_BIT;

        fDef.shape = shape;
        b2body.createFixture(fDef).setUserData(this);
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
    public void update(float deltaTime) {
        stateTimer += deltaTime;
        if(b2body.getPosition().y < -10 || currentHealth <= 0) {
            b2body.setActive(false);
            isDead = true;
        }
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        else if(!destroyed) {
            //b2body.setLinearVelocity(velocity);

            if(b2body.getLinearVelocity().y == 0 && shouldMove) {
                this.moveLeft();
            }

            if(shouldMove == false) {
                this.b2body.setLinearVelocity(0f, 0f);
            }

            if(isAwake) {
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
                setRegion(getFrame(deltaTime));
            }

//            if(Math.abs(playerPosition.x - b2body.getPosition().x) > 10) {
//                isAwake = false;
//            } else {
//                isAwake = true;
//            }
        }
    }

    @Override
    public void onEnemyHit(Enemy enemy) {

    }

    @Override
    public void loadTexturesAndAnimations(PlayScreen screen) {
        TextureRegion playerStand = new TextureRegion(new Texture("sprites/dark_elf/raw/Idle/0_Dark_Elves_Idle_000.png"));

        textures = new ArrayMap<>();
        textures.put("jump", playerStand);
        textures.put("stand", playerStand);

        Animation run = new Animation(0.1f, screen.getAtlas("Devil Archer").findRegions("0_Archer_Running"));
        Animation dead = new Animation(0.1f, screen.getAtlas("Devil Archer").findRegions("0_Archer_Dying"));
        Animation _throw = new Animation(0.05f, screen.getAtlas("Devil Archer").findRegions("0_Archer_Throwing"));
        Animation idle = new Animation(0.1f, screen.getAtlas("Devil Archer").findRegions("0_Archer_Idle Blinking"));
        Animation hurt = new Animation(0.1f, screen.getAtlas("Devil Archer").findRegions("0_Archer_Hurt"));
        Animation shooting = new Animation(0.05f, screen.getAtlas("Devil Archer").findRegions("0_Archer_Shooting"));

        animations = new ArrayMap<>();
        animations.put("run", run);
        animations.put("dead", dead);
        animations.put("throw", _throw);
        animations.put("idle", idle);
        animations.put("hurt", hurt);
        animations.put("shoot", shooting);
    }
}
