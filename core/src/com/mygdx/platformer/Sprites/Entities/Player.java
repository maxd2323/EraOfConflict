package com.mygdx.platformer.Sprites.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.Hud;
import com.mygdx.platformer.Screens.PlayScreen;

public class Player extends Entity {

    // Boolean Flags
    private boolean runningRight;
    private boolean runThrowAnimation;
    private boolean runHurtAnimation;

    // Other
    public int jumpCount;
    private PlayScreen screen;
    private float swingCooldown = 1.0f;  // 1 second between swings
    private Entity opponent;


    public Player(
            PlayScreen screen,
            float x,
            float y,
            float health,
            float magicka,
            float speed
    ) {
        super(
                screen,
                health,
                magicka,
                speed,
                "Player"
        );
        this.world = screen.getWorld();
        this.screen = screen;

        runningRight = true;

        definePlayer();

        setPosition(x, y);
        setBounds(0, 0, Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
        setRegion(textures.get("stand"));

        Hud.setHealth((int) currentHealth);
        Hud.setMagicka((int) currentMagicka);
    }

    public Player(
            PlayScreen screen,
            float x,
            float y,
            float health,
            float magicka,
            float speed,
            String entityTag
    ) {
        super(
                screen,
                health,
                magicka,
                speed,
                entityTag
        );
        this.world = screen.getWorld();
        this.screen = screen;

        runningRight = true;

        definePlayer();

        setPosition(x, y);
        setBounds(0, 0, Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
        setRegion(textures.get("stand"));

        Hud.setHealth((int) currentHealth);
        Hud.setMagicka((int) currentMagicka);
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
            case JUMPING:
                region = textures.get("jump");
                break;
            case RUNNING:
                region = (TextureRegion) animations.get("run").getKeyFrame(stateTimer, true);
                break;
            case THROWING:
                region = (TextureRegion) animations.get("throw").getKeyFrame(stateTimer);
                if(runningRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if(!runningRight && region.isFlipX()) {
                    region.flip(true, false);
                }
                if(animations.get("throw").isAnimationFinished(stateTimer)){
                    runThrowAnimation = false;
                }
                break;
            case HURT:
                region = (TextureRegion) animations.get("hurt").getKeyFrame(stateTimer);
                if(runningRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if(!runningRight && region.isFlipX()) {
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
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if(isDead) {
            return State.DEAD;
        }
        else if(runThrowAnimation) {
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

    public void moveRight() {
        this.b2body.setLinearVelocity(new Vector2(1.5f, 0));
    }

    public void update(float deltaTime) {
        Hud.setHealth((int) currentHealth);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 8 / Platformer.PPM);
        setRegion(getFrame(deltaTime));
        incrementMagicka(1f);

        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }

        if(b2body.getLinearVelocity().y == 0 && shouldMove) {
            this.moveRight();
        }

        if(shouldMove == false) {
            this.b2body.setLinearVelocity(0f, 0f);
        }

        if(b2body.getPosition().y < -10) {
            isDead = true;
        }
        if(currentState == State.STANDING || currentState == State.RUNNING) {
            jumpCount = 0;
        }
        if(currentHealth <= 0) {
            isDead = true;
        }
    }

    public void definePlayer() {
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
