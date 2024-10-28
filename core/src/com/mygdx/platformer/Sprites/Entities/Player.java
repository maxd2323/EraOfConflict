package com.mygdx.platformer.Sprites.Entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;

public class Player extends Entity {

    // Boolean Flags
    private boolean runThrowAnimation;
    private boolean runHurtAnimation;

    // Other
    public int jumpCount;


    public Player(
            PlayScreen screen,
            float x,
            float y,
            float health,
            float magicka,
            float speed,
            float baseDamage
    ) {
        this(screen, x, y, health, magicka, speed, baseDamage, "Player");
    }

    public Player(
            PlayScreen screen,
            float x,
            float y,
            float health,
            float magicka,
            float speed,
            float baseDamage,
            String entityTag
    ) {
        super(
                screen,
                x,
                y,
                health,
                magicka,
                speed,
                baseDamage,
                entityTag
        );

        attackCooldown = 5f;
        facingRight = true;

        setBounds(0, 0, Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
        setRegion(textures.get("stand"));
    }

    // Getters

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
            case SLASHING:
                region = (TextureRegion) animations.get("slash").getKeyFrame(stateTimer, false);
                if(animations.get("slash").isAnimationFinished(stateTimer) && stateTimer > 5){
                    currentState = State.STANDING;
                }
                break;
            case JUMPING:
                region = textures.get("jump");
                break;
            case RUNNING:
                region = (TextureRegion) animations.get("run").getKeyFrame(stateTimer, true);
                break;
            case THROWING:
                region = (TextureRegion) animations.get("throw").getKeyFrame(stateTimer);
                if(facingRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if(!facingRight && region.isFlipX()) {
                    region.flip(true, false);
                }
                if(animations.get("throw").isAnimationFinished(stateTimer)){
                    runThrowAnimation = false;
                }
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
                region = textures.get("stand");;
                break;
        }
        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if(isDead) {
            return State.DEAD;
        } else if (runAttackAnimation) {
            return State.SLASHING;
        } else if(runThrowAnimation) {
            return State.THROWING;
        }
        else if(runHurtAnimation) {
            return State.HURT;
        }
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
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

    public void update(float deltaTime) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 8 / Platformer.PPM);
        setRegion(getFrame(deltaTime));
        incrementMagicka(1f);

        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }

        if(b2body.getLinearVelocity().y == 0 && shouldMove) {
            move();
        }

        if(shouldMove == false) {
            this.b2body.setLinearVelocity(0f, 0f);
        }

        if(b2body.getPosition().y < -10 || currentHealth <= 0) {
            die();
        }
        if(currentState == State.STANDING || currentState == State.RUNNING) {
            jumpCount = 0;
        }

        if (inCombat) {
            timeSinceLastAttack += deltaTime;
            if (timeSinceLastAttack >= attackCooldown) {
                attack();
                timeSinceLastAttack = 0f;
            }
            if (opponent.isDead) {
                inCombat = false;
            }
        }
    }

    @Override
    protected void defineEntity() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(Platformer.getTileMultiplier(2f), Platformer.getTileMultiplier(2f));
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Platformer.getTileMultiplier(0.375f));
        fDef.filter.categoryBits = Platformer.PLAYER_BIT;
        fDef.filter.maskBits = Platformer.GROUND_BIT
                | Platformer.COIN_BIT
                | Platformer.BRICK_BIT
                | Platformer.PLAYER_BIT
                | Platformer.ENEMY_BIT
                | Platformer.OBJECT_BIT
                | Platformer.ENEMY_HEAD_BIT
                | Platformer.ITEM_BIT
                | Platformer.CHEST_BIT
                | Platformer.PROJECTILE_BIT;

        fDef.shape = shape;
        b2body.createFixture(fDef).setUserData(this);
    }

    @Override
    public void loadTexturesAndAnimations(PlayScreen screen) {
    }

}
